# 책모아 server part

## 1. 로컬 MySQL 데이터베이스 설정
- 1단계: MySQL 설치
- 2단계: MySQL 서비스 시작
- [local-db.env]() 파일의 환경변수와 동일하게 설정하기
- env 폴더는 .gitignore에 추가할 예정
- 3단계: MySQL 데이터베이스 생성
- ```mysql -u root -p```
- ```CREATE DATABASE book_moa;```
