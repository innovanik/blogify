# blogify

실행 파일 다운로드 : <https://github.com/innovanik/blogify/raw/main/api.jar>
``` C
java -jar api.jar
```
<br/>

## Library
> Spring Boot 2.7.9 (Aop, WebFlux, Validation, Reactor Netty, JPA, Json, R2dbc)<br/>
> Lombok 1.18.26 : 소스 코드 반복 작업(getter, setter 등) 자동화<br/>
> Janino 3.1.9 : logback 의 조건부 처리 기능<br/>
> Apache Commons Text 1.10.0 : 문자열 관련 처리<br/>
<br/>

## API
> ### [GET] /search/blog (블로그 검색)
> > #### Parameter
> > > query : 검색어<br/>
> > > sort  : 정렬 - accuracy(정확도순) 또는 recency(최신순), 기본값=accuracy<br/>
> > > page  : 페이지 번호, 기본 값 1<br/>
> > > size  : 페이지의 목록 수<br/>
> ### [GET] /search/word/popular (인기 검색어 목록)<br/>
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
