from fastapi import FastAPI
import pymysql.cursors
import numpy as np
from datetime import datetime
from pydub import AudioSegment
from datetime import datetime

app = FastAPI()

@app.get("/media/ss")
def read_root():
    return {"Hello": "FastAPI"}


# MySQL 데이터베이스 연결
connection = pymysql.connect(host=,
                             port=,
                             user=,
                             password=,
                             database=,
                             charset=,
                             cursorclass=pymysql.cursors.DictCursor)



with connection.cursor() as cursor:
    # SQL 쿼리 실행
    # 일단 cover_pk 3이라고 가정하고 가져옴
    cover_pk = '7'
    sql_cover = "SELECT * FROM cover WHERE cover_pk = %s"
    cursor.execute(sql_cover, (cover_pk, ))
    
    # 결과 가져오기
    cover = cursor.fetchall()
    print(cover)
    song_pk = cover[0]['song_pk']
    sql_lyric = "SELECT * FROM lyric WHERE song_pk = %s"
    cursor.execute(sql_lyric, (song_pk, ))
    lyrics = cursor.fetchall()
    

    sql_cover_user = "SELECT * FROM cover_user WHERE cover_pk = %s"
    cursor.execute(sql_cover_user,(cover_pk, ))
    cover_users = cursor.fetchall()
    print(cover_users)


    allPart = []
    for r in cover_users:
        allPart.append([])
        allPart[len(allPart)-1].append([r['part_pk']])
        allPart[len(allPart)-1].append([])
        allPart[len(allPart)-1].append([])
    print(cover_users)
    
    for part in allPart:
        for lyric in lyrics:
            if lyric['part_pk'] == part[0][0]:
                part[1].append(lyric)
                part[2].append((lyric['start_time'], lyric['end_time']))

    # 1. 원곡 DNA파일 받아와서
    # 2. 파트별로 시간 짤라서 무음처리하고 그 파일 S3에 업로드
print(allPart)

for part in allPart:
    # 오디오 파일 불러오기
    audio = AudioSegment.from_file("C:/Users/SSAFY/Desktop/ourdream_music.wav", format="wav")

    # 노래가 끝나는 시간 설정
    song_end_time_ms=len(audio)

    # 무음 처리하지 말아야 할 구간 설정
    keep_silent_ranges = part[2]

    # 전체 노래에서 무음 처리할 구간 계산
    mute_ranges = []
    last_end_time_ms = 0

    # 각 구간을 순회하며 무음 처리할 구간 계산
    for start_time_str, end_time_str in keep_silent_ranges:
        start_time = datetime.strptime(start_time_str, "%H:%M:%S")
        end_time = datetime.strptime(end_time_str, "%H:%M:%S")
        start_time_ms = (start_time.hour * 3600 + start_time.minute * 60 + start_time.second) * 1000
        end_time_ms = (end_time.hour * 3600 + end_time.minute * 60 + end_time.second) * 1000
        
        # 이전 종료 시간과 현재 시작 시간 사이를 무음 처리할 구간으로 추가
        if last_end_time_ms < start_time_ms:
            mute_ranges.append([last_end_time_ms, start_time_ms])
        last_end_time_ms = end_time_ms

    # 마지막 무음 처리하지 않을 구간과 노래 끝나는 시간 사이의 구간 추가
    if last_end_time_ms < song_end_time_ms:
        mute_ranges.append([last_end_time_ms, song_end_time_ms])

    # 계산된 무음 처리할 구간을 사용하여 오디오에서 무음 처리
    working_audio = audio[:]
    for start_ms, end_ms in mute_ranges:
        silence = AudioSegment.silent(duration=end_ms - start_ms)
        working_audio = working_audio[:start_ms] + silence + working_audio[end_ms:]

    working_audio.export("{}_{}.wav".format(song_pk,part[0][0]), format="wav")
