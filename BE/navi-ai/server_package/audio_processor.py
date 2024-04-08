"""
학습데이터 전처리 모듈
"""
import os
import subprocess
from glob import glob
import shutil
import torchaudio
import torch
from torchaudio.pipelines import HDEMUCS_HIGH_MUSDB_PLUS
from torchaudio.transforms import Fade
import soundfile as sf
import librosa

class AudioProcessor:
    def __init__(self, src_dir, work_path):
        self.src_dir = src_dir
        self.work_path = work_path

    def make_dataset(self):
        try:
            # 경로 검증
            assert os.path.isdir(self.src_dir), "지정한 로컬 폴더가 없습니다."
            assert len(os.listdir(self.src_dir)) >= 1, "지정한 로컬 폴더에 파일이 없습니다."

            # 지원하는 파일 포맷 : m4a, mp3, webm
            src_files = [os.path.join(self.src_dir, i) for i in os.listdir(self.src_dir) if i.endswith(('.mp3','.m4a','.webm'))]
            assert len(src_files) != 0, '경로를 잘못 지정한 것 같습니다.'
        except Exception as e:
            raise Exception(f"Audio processing failed: {str(e)}")
        
        # 데이터셋 디렉토리 구조 생성
        dataset_dir = os.path.join(self.work_path, "work")
        dataset_raw_dir = os.path.join(self.work_path, "raw")
        os.makedirs(dataset_raw_dir, exist_ok=True)

        # wav 파일로 변환
        for i, src_file in enumerate(src_files):
            dataset_raw_wav_path = os.path.join(dataset_raw_dir, f"audio_{i}.wav")
            command = f"ffmpeg -i {src_file} -acodec pcm_s16le {dataset_raw_wav_path}"
            #print(command)
            subprocess.call(command, shell=True)

        sample_rate = 44100
        # 음성의 길이 (초)
        length = 10

        # 긴 오디오파일 자르기
        dataset_cut_dir = os.path.join(dataset_dir, 'cut')
        os.makedirs(dataset_cut_dir, exist_ok=True)

        dataset_raw_paths = glob(os.path.join(dataset_raw_dir, '*.wav'))
        for i, dataset_raw_path in enumerate(dataset_raw_paths):
            dataset_cut_format = os.path.join(dataset_cut_dir, f'cut-{i:04d}-%04d.wav')
            command = f"ffmpeg -i {dataset_raw_path} -f segment -segment_time 600 -c copy {dataset_cut_format}"
            subprocess.call(command, shell=True, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)

        # 음성만 떼어내기
        get_filename = lambda f: os.path.basename(f)

        dataset_seperate_dir = os.path.join(dataset_dir, 'seperate')
        dataset_cut_paths = glob(f'{dataset_cut_dir}/**/*.wav', recursive=True)
        os.makedirs(dataset_seperate_dir, exist_ok=True)

        seperator = demucs()
        for dataset_cut_path in dataset_cut_paths:
            dataset_seperate_path = os.path.join(dataset_seperate_dir, get_filename(dataset_cut_path))
            seperator.extract_vocal(dataset_cut_path, dataset_seperate_path)

        shutil.rmtree(dataset_cut_dir)

        # ffmpeg로 조용한 부분 없애기
        dataset_seperate_paths = glob(f'{dataset_seperate_dir}/**/*.wav', recursive=True)
        dataset_condense_dir = os.path.join(dataset_dir, 'condense')
        os.makedirs(dataset_condense_dir, exist_ok=True)

        for dataset_seperate_path in dataset_seperate_paths:
            dataset_condense_path = os.path.join(dataset_condense_dir, get_filename(dataset_seperate_path))
            command = f"ffmpeg -i {dataset_seperate_path} -af \"silenceremove=start_periods=1:stop_periods=-1:start_threshold=-30dB:stop_threshold=-30dB:start_silence=2:stop_silence=2\" {dataset_condense_path}"
            subprocess.call(command, shell=True, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)

        shutil.rmtree(dataset_seperate_dir)

        # ffmpeg로 알아서 절단하기
        dataset_split_dir = os.path.join(dataset_dir, 'split')
        os.makedirs(dataset_split_dir, exist_ok=True)

        dataset_condense_paths = glob(f'{dataset_condense_dir}/**/*.wav', recursive=True)
        for dataset_condense_path in dataset_condense_paths:
            dataset_split_path = os.path.join(dataset_split_dir, get_filename(dataset_condense_path)[:-4]+'_%04d.wav')
            command = f"ffmpeg -i {dataset_condense_path} -f segment -segment_time {length} -c copy {dataset_split_path}"
            subprocess.call(command, shell=True, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)

        shutil.rmtree(dataset_condense_dir)

        # librosa로 샘플링레이트, 채널 맞추기
        dataset_final_dir = os.path.join(dataset_dir, 'final')
        os.makedirs(dataset_final_dir, exist_ok=True)

        dataset_split_paths = glob(f'{dataset_split_dir}/**/*.wav', recursive=True)
        for dataset_split_path in dataset_split_paths:
            audio, sr = librosa.load(dataset_split_path, sr=None)
            length = len(audio) / sr
            if length <= length-1: continue
            audio = librosa.resample(audio, orig_sr=sr, target_sr=sample_rate)
            audio = librosa.to_mono(audio)
            dataset_final_path = os.path.join(dataset_final_dir, get_filename(dataset_split_path))
            sf.write(dataset_final_path, audio, sample_rate)

        shutil.rmtree(dataset_split_dir)

"""
배경음 제거, 목소리 분리
"""
class demucs():
  def __init__(self):
    self.device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')

    bundle = HDEMUCS_HIGH_MUSDB_PLUS
    self.model = bundle.get_model()
    device = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")
    self.model.to(self.device)
    self.sample_rate = bundle.sample_rate

  def separate_sources(
        self,
        model,
        mix,
        segment=10.,
        overlap=0.1,
        device=None,
        sample_rate=44100,
):
    if device is None:
        device = mix.device
    else:
        device = torch.device(device)

    batch, channels, length = mix.shape

    chunk_len = int(sample_rate * segment * (1 + overlap))
    start = 0
    end = chunk_len
    overlap_frames = overlap * sample_rate
    fade = Fade(fade_in_len=0, fade_out_len=int(overlap_frames), fade_shape='linear')

    final = torch.zeros(batch, len(model.sources), channels, length, device=device)

    while start < length - overlap_frames:
        chunk = mix[:, :, start:end]
        with torch.no_grad():
            out = model.forward(chunk)
        out = fade(out)
        final[:, :, :, start:end] += out
        if start == 0:
            fade.fade_in_len = int(overlap_frames)
            start += int(chunk_len - overlap_frames)
        else:
            start += chunk_len
        end += chunk_len
        if end >= length:
            fade.fade_out_len = 0
    return final

  def extract_vocal(self, input_file_path, output_file_path):
    waveform, self.sample_rate = torchaudio.load(input_file_path)
    waveform = waveform.to(self.device)
    mixture = waveform

    # parameters
    segment: int = 10
    overlap = 0.1

    ref = waveform.mean(0)
    waveform = (waveform - ref.mean()) / ref.std()  # normalization

    sources = self.separate_sources(
        self.model,
        waveform[None],
        device=self.device,
        segment=segment,
        overlap=overlap,
    )[0]
    sources = sources * ref.std() + ref.mean()

    sources_list = self.model.sources
    sources = list(sources)

    audios = dict(zip(sources_list, sources))

    sf.write(output_file_path, audios['vocals'][0].cpu().numpy(), self.sample_rate)
