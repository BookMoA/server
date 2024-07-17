# 책모아 server part

## 기본 세팅

### 1. 로컬 MySQL 데이터베이스 설정

#### 1단계: MySQL 설치

#### 2단계: MySQL 서비스 시작

* [local-db.env]() 파일의 환경변수와 동일하게 설정하기
* env 폴더는 .gitignore에 추가할 예정

#### 3단계: MySQL 데이터베이스 생성

* ```mysql -u root -p```
* ```CREATE DATABASE book_moa;```

## 자주 나타나는 에러

### 1. commit 입력시

    커밋 템플릿이 구성되어 있지 않습니다. 커밋 템플릿 설정을 해주세요. 
    git config --local commit.template .github/commit_template.txt

- 위와 같은 에러가 난다면 메세지가 뜨면 터미널에 ```git config --local commit.template .github/commit_template.txt```을 입력해주세요!