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

<br>
<br>

## 자주 나타나는 에러

### 1. commit 입력시

	> Task :spotlessJavaCheck FAILED
	FAILURE: Build failed with an exception.

- spotless라는 코드 포맷터가 commit전에 실행되도록 해뒀습니다. 제가 파일 이름을 이상하게 해둬서 초반에는 실행이 잘 안되어 build ci에서 자꾸 위 같은 에러가 뜨면서 fail이 날텐데 > 커밋을
입력하는 터미널 창에 ```./gradlew spotlessApply``` 입력 후(코드가 정렬됨) 다시 commit하면 됩니다.
- 현재는 해결된 상태이니 처음 시작하실 때 develop를 pull 받고, ***한 번 gradle을 build한 후***에 작업하시면 됩니다!

### 2. gradle build시

	Execution failed for task ':spotlessMiscCheck'.
	> The following files had format violations:

- gradle을 build할 때 spotless라는 코드 포맷터를 실행하는 스크립트가 아직 작동이 안된 경우, 위와 같은 에러가 날 수 있는데 _**일단 무시하고 작업한 후 commit하면 됩니다!**_
- 일단 build를 실행함으로써 hook을 실행하는 스크립트를 .git 폴더 안으로 넣는 작업이 진행되면, commit 전에 spotless가 실행되어서 자동으로 정렬이 될 거에요!
