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
- 현재는 해결된 상태이니 처음 시작하실 때 develop를 pull 받고, **한번 gradle을 build한 후**에 작업하시면 됩니다!

### 2. commit을 눌렀는데 반영이 안된다,,,?

	>  1 file changed, 1 insertion(+)

- 분명 commit을 했는데 또 새로운 변경사항이 나타난다면?
	- 이미 이전 commit은 반영된 상태 but, 어디선가 코드 포맷이 수정된 경우!
	- ```git commit -m "[style] 코드 정렬" ```로 정렬된 코드 다시 commit하면 됨!
	- 이게 싫다면 commit 전에 ```./gradlew spotlessApply```를 한 번씩 입력해주세요!
