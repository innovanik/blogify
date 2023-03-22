# blogify

실행 파일 다운로드 : <https://github.com/innovanik/blogify/raw/main/api.jar>
``` C
java -jar api.jar
```
<br/>

## Library
|명칭|버전|사용 이유|비고|
|-|-|-|-|
|Spring Boot|2.7.9||Aop, WebFlux, Validation, Reactor Netty, JPA, Json, R2dbc|
|Lombok|1.18.26|소스 코드 반복 작업(getter, setter 등) 자동화||
|Janino|3.1.9|logback 의 조건부 처리 기능||
|Apache Commons Text|1.10.0|문자열 관련 처리||
<br/>

## API
URI : http://localhost:8080
> ### [GET] /search/blog (블로그 검색)
>> #### 요청
>> ##### Parameters
>> |이름|형식|설명|기본값|필수|
>> |-|:-:|-|-|:-:|
>> |query|String|검색어||O|
>> |sort|Integer|정렬 - accuracy(정확도순) 또는 recency(최신순)|accuracy||
>> |page|Integer|페이지 번호|1||
>> |size|Integer|페이지의 목록 수|10||
>> #### 응답
>> ##### Body
>> |이름|형식|설명|
>> |-|:-:|-|
>> |results|List|결과 목록|
>> |└ title|String|제목|
>> |└ content|String|내용|
>> |└ url|String|링크 주소|
>> |└ name|String|블로그명|
>> |└ date|String|작성시간|
>> |server|String|데이터 연동 서버 (Kakao 또는 Naver)|
>> |query|String|검색어|
>> |total|Integer|결과 수|
>> |sort|String|요청 정렬|
>> |page|Integer|요청 페이지 번호|
>> |size|Integer|요청 페이지의 목록 수|
>
>
> ### [GET] /search/word/popular (인기 검색어 목록)<br/>
>> #### 응답
>> ##### Body
>> |이름|형식|설명|
>> |-|:-:|-|
>> |results|List|결과 목록|
>> |└ keyword|String|검색 키워드|
>> |└ count|Integer|검색 수|
<br/>

## 모듈 구성
> ### blogify-core
> Application 기본 공통 기능
> ### blogify-ext
> 외부 연동 기본 공통 기능
> ### blogify-ext-kakao
> Kakao 연동 Client
> ### blogify-ext-naver
> Naver 연동 Client
> ### blogify-res-r2dbc
> DB Resource (JPA)
> ### blogify-app-api
> API Application
