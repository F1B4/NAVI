"""
s3로 파일을 다운로드, 업로드 할 수 있는 모듈
"""
import boto3
import os
import shutil
import zipfile
import glob
from urllib.parse import unquote

s3_client = boto3.client('s3',
                  aws_access_key_id='access_key',
                  aws_secret_access_key='secret_key'
                  )

bucket_name = 'bucket'

"""
s3에서 파일 다운로드
"""
def download_files(object_paths, local_directory):
    download_path = []

    for object_path in object_paths:
        # S3 오브젝트 키 추출
        object_key = object_path.replace(f's3_url', '')
        
        # 로컬 파일 이름에 대해 URL 디코딩 적용
        decoded_file_name = unquote(object_key)
        local_file_path = os.path.join(local_directory, decoded_file_name)
        
        # 파일 다운로드
        try:
            s3_client.download_file(bucket_name, decoded_file_name, local_file_path)
            print(f"Downloaded {object_path} to {local_file_path}")
            download_path.append(local_file_path)
        except Exception as e:
            print(f"Failed to download {object_path} to {local_file_path}. Error: {e}")
    
    return download_path
    
"""
s3에 파일 업로드
"""
def upload_file_to_s3(file_paths, contentType):
    result = []
    for file_path in file_paths:
        # 파일 이름을 S3 오브젝트 이름으로 사용
        object_name = os.path.basename(file_path)

        # 파일 업로드 시 ContentType을 올바르게 설정
        s3_client.upload_file(
            file_path, 
            bucket_name, 
            object_name, 
            ExtraArgs={'ContentType': contentType}
        )
        print(f"Uploaded {file_path} to {bucket_name}/{object_name}")
        uploaded_file_path = f's3_url'
        result.append(uploaded_file_path)
    
    return result

"""
지정 경로로 파일 이동
"""
def find_and_move_file(src_dir, partial_file_name, new_path):
    # src_dir에서 파일 탐색
    for filename in os.listdir(src_dir):
        # 파일 이름에 partial_file_name이 포함되어 있는 경우
        if partial_file_name in filename:
            # 원본 파일의 전체 경로
            original_path = os.path.join(src_dir, filename)
            
            # 파일을 새 위치로 이동시키고 이름 변경 (new_path에는 새 파일의 전체 경로를 포함)
            shutil.move(original_path, new_path)
            print(f"File {filename} moved to {new_path}")
            break  # 결과 파일이 1개만 보장되므로, 파일을 찾은 후 반복문 종료

"""
파일 압축
"""
def compress_file(zip_name, file_path):
    # 압축 파일 생성
    with zipfile.ZipFile(zip_name, 'w') as myzip:
        myzip.write(file_path, os.path.basename(file_path))
    print(f"Created zip file: {zip_name}")


"""
폴더 삭제
"""
def remove_dir(dir_path):
    try:
        shutil.rmtree(dir_path)
        print(f"directory {dir_path} and all its contents have been deleted.")
    except FileNotFoundError:
        print(f"The folder {dir_path} does not exist.")


"""
파일 삭제
"""
def remove_files(dir, partial_path, extension):
    # partial_path 가 포함되는 파일을 모두 찾음
    
    # 지정된 확장자를 가진, 특정 문자열을 포함하는 모든 파일에 대한 경로를 생성
    pattern = f"{dir}/*{partial_path}*.{extension}"
    files_to_delete = glob.glob(pattern)

    # 해당하는 파일 모두 삭제
    for file_path in files_to_delete:
        try:
            os.remove(file_path)
            print(f"Deleted {file_path}")
        except FileNotFoundError:
            print(f"The file {file_path} was not found")
        except Exception as e:
            print(f"Error deleting file {file_path}: {e}")
