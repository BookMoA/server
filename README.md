# 책모아 Server Part
### 참고 - client: [API Documentation 확인하기](https://twisty-hygienic-bea.notion.site/API-c1b59bbf40fe4b22b9d4ec8eb987990f)

<br>

## 목차

1. [기본 세팅](#기본-세팅)
	1. [gradle build 진행하기](#gradle-build-진행하기)
	2. [로컬 MySQL 데이터베이스 설정](#로컬-mysql-데이터베이스-설정)

2. [현재 적용된 CI 설명](#현재-적용된-ci-설명)

3. [자주 나타나는 에러](#자주-나타나는-에러)
	1. [pull request 등록 후 CI ❌ 표시가 날 경우](#pull-request-등록-후-ci--표시가-날-경우)
	2. [gradle build시](#gradle-build시)

4. [배포가 시작되었습니다](#배포가-시작되었습니다) 
	1. [코드 변경시](#코드-변경시)
	2. [db 변경시](#db-변경시) ✨updated✨
 
<br>

## 기본 세팅
### 1. gradle build 진행하기
- intellij에서 gradle > build 클릭
- 또는 ```[macOS] : ./gradlew build, [window] : ./gradlew.bat build```

### 2. 로컬 MySQL 데이터베이스 설정

#### 1단계: MySQL 설치

#### 2단계: MySQL 서비스 시작

* [local-db.env]() 파일의 환경변수와 동일하게 설정하기
* env 폴더는 .gitignore에 추가할 예정

#### 3단계: MySQL 데이터베이스 생성

* ```mysql -u root -p```
* ```CREATE DATABASE book_moa;```

<br>
<br>

## 현재 적용된 CI 설명
> 전제사항: 일단 모두 다시 pull 받은 후, gradle build를 실행하기
- commit 전에 일어나는 일 1: 코드가 정렬됨
- commit 전에 일어나는 일 2: 커밋 메세지의 형식을 체크함
- pr을 올리고 일어나는 일: build를 해보고 에러가 나는지 체크함
- pr에서 develop나 main으로 merge(즉, 두 branch로 push)를 하면 일어나는 일: 커밋 메세지 형식을 체크함

<br>
<br>

## 자주 나타나는 에러

### 1. pull request 등록 후 CI ❌ 표시가 날 경우!
##### 1) 이유1 - 커밋 메세지 형식이 맞지 않을 경우
##### 2) 이유2 - 정렬되지 않은 채로 commit 된 경우

	> Task :spotlessJavaCheck FAILED
	FAILURE: Build failed with an exception.

- develop을 pull 받고, ***한 번 gradle을 build한 후***에 작업하시면 됩니다!

### 2. gradle build시

	Execution failed for task ':spotlessMiscCheck'.
	> The following files had format violations:

- gradle을 build할 때 spotless라는 코드 포맷터를 실행하는 스크립트가 아직 작동이 안된 경우, 위와 같은 에러가 날 수 있는데 _**일단 무시하고 작업한 후 commit하면 됩니다!**_
- 일단 build를 실행함으로써 hook을 실행하는 스크립트를 .git 폴더 안으로 넣는 작업이 진행되면, commit 전에 spotless가 실행되어서 자동으로 정렬이 될 거에요!

<br>
<br>

## 배포가 시작되었습니다
- 배포가 시작되었습니다. 자세한 사항은 [discussion - [server] 배포과정](https://github.com/orgs/BookMoA/discussions/116)에 정리해두었으니 한번 확인해주세요
### 1. 코드 변경시
- 여러분의 코드에 수정사항이 생겼을 경우 해당 페이지의 `3번. 배포방법`을 확인해주세요!
### 2. db 변경시
- db를 직접 수정 또는 확인해야하는 경우 해당 페이지의 `5번. mysql 컨테이너 접속하기`를 확인해주세요!
