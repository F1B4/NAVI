import pymysql.cursors

def get_connection():
    return pymysql.connect(
        host='',
        port='',
        user='',
        password='',
        database='',
        charset='',
        cursorclass=pymysql.cursors.DictCursor
    )