from celery import Celery
from kombu import Queue

# Celery 애플리케이션 인스턴스 생성 및 구성
celery_app = Celery(
    'tasks',
    broker='redis://localhost:6379/0',
    backend='redis://localhost:6379/0',
)

# 작업 큐 설정
celery_app.conf.task_queues = (
    Queue('train_queue', routing_key='task.#'),
    Queue('infer_queue', routing_key='task.#'),
)

# 기본 큐 설정
celery_app.conf.task_default_queue = 'default'
celery_app.conf.task_default_exchange = 'tasks'
celery_app.conf.task_default_routing_key = 'task.default'