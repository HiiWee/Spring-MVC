# <웹 애플리케이션 이해>
## [WEB 서버, WAB(Web Application Server)]
### 웹 서버(Web Server)
* HTTP 기반으로 동작
* 정적 리소스 제공, 기타 부가기능
* 정적(파일) HTML, CSS, JS, 이미지, 영상들을 제공해줌
* 예) NGINX, APACHE

### 웹 애플리케이션 서버(WAS - Web Application Server)
* HTTP 기반으로 동작
* 웹 서버 기능 포함 + (정적 리소스 제공 가능)
* 프로그램 코드를 실행해서 애플리케이션 로직 수행
  * 동적 HTML, HTTP API(JSON)
  * 서블릿, JSP, 스프링 MVC
* 예) 톰캣(Tomcat), Jetty, Undertow

### 웹 서버, 웹 애플리케이션 서버(WAS)의 차이
* 웹 서버는 정적 리소스(파일) 제공, WAS는 애플리케이션 로직까지 실행하여 제공
* 사실은 둘의 용어 및 경계가 모호하다.
  * 웹 서버도 프로그램을 실행하는 기능을 포함하기도 함(플러그인 설치)
  * 웹 애플리케이션 서버도 웹 서버의 기능을 제공한다.
* 자바는 서블릿 컨테이너 기능을 제공하면 WAS
  * 서블릿 없이 자바코드를 실행하는 서버 프레임워크도 있다.
* WAS는 애플리케이션 코드를 실행하는데 더 특화되어 있다.

### 웹 시스템 구성 - WAS, DB (가장 간단한 조함)
* WAS, DB만으로 시스템 구성 가능
* WAS는 정적 리소스, 애플리케이션 로직 모두 제공 가능

한 개의 WAS만 사용하는 경우
* WAS가 너무 많은 역할을 담당, 서버 과부하 우려
* 가장 비싼 애플리케이션 로직이 정적 리소스 때문에 수행이 어려울 수 있다.
* WAS 장애시 오류 화면도 노출 불가능 (WAS자체가 장애가나면 오류도 확인할 수 없음)   
  (애플리케이션 로직은 DB, 및 다른 서버 호출을 위한 비즈니스 로직이 있기때문에 비싸다고 표현)

### 웹 시스템 구성(일반적) - WEB, WAS, DB
* 정적 리소스는 웹 서버가 처리
* 웹 서버는 애플리케이션 로직같은 동적인 처리가 필요하면 WAS에 요청을 위임
* WAS는 중요한 애플리케이션 로직 처리 전담   
* 또한 효율적인 리소스 관리에 좋다
  * 정적 리소스가 많이 사용되면 Web 서버 증설
  * 애플리케이션 리소스가 많이 사용되면 WAS 증설
* 웹 서버는 잘 죽지 않음(단순한 정적파일 제공이므로)
* WAS, DB 장애시 WEB서버가 오류 화면 제공 가능
* 단순히 API만 주고받는 경우 웹 서버 없어도 됨(화면에 표시할것이 없으므로)

<br>

## [서블릿]
* 서블릿은 특정 HTTP 메소드로 서버가 요청을 받게되면 일어나는 동작들(**동작은 PPT 확인**) 중에서   
  비즈니스 로직 실행(데이터베이스에 저장 요청)을 제외한 나머지 부분들을 전부 커버해준다.(서버를 지원하는 WAS들이)
### 서블릿의 특징
```java
@Webservlet(name = "helloServlet", urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {
  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) {
    //애플리케이션 로직
  }
}
```
* urlPatterns(/hello)의 URL이 호출되면 서블릿 코드가 실행된다.
* HTTP 요청 정보를 편리하게 사용할 수 있는 HttpServletRequest
* HTTP 응답 정보를 편리하게 제공할 수 있는 HttpServletResponse
* 개발자는 HTTP 스펙을 매우 편리하게 사용할 수 있지만, HTTP 스펙을 기본적으로 알아야 사용할때 편리하다.

###  HTTP 요청, 응답 흐름
* HTTP 요청 시
  * WAS : Request, Response 객체 새로 만든 후 서블릿 객체 호출(누가 호출하는지 모르므로 항상 새로 생성)
  * 개발자는 Request 객체에서 HTTP 요청 정보를 꺼내어 사용, Response 객체에는 HTTP 응답정보를 입력한다.
  * WAS는 Response 객체에 담겨있는 내용으로 HTTP 응답 정보를 생성한다.

### 서블릿 컨테이너 (톰캣처럼 서블릿을 지원하는 WAS를 서블릿 컨테이너라 한다.)
* 개발자는 서블릿 객체를 직접 생성하는것이 아닌 코드만 만듦
* WAS안에 서블릿 컨테이너에서 서블릿 객체를 자동으로 생성, 초기화, 호출, 종료하는 생명주기를 관리 해준다.
* 서블릿 객체는 **싱글톤으로 관리됨**
  * 고객의 요청이 올 때 마다 객체 생성 == 비효율
  * 최초 로딩 시점에 서블릿 객체를 미리 만들어두고 재활용
  * 모든 고객 요청은 동일한 서블릿 객체 인스턴스에 접근
  * **공유 변수 사용 주의(Stateless 해야 함)**
  * 서블릿 컨테이너 종료시 함께 종료됨
* **동시 요청을 위한 멀티 스레드 처리 지원** : 중요한 특징(개발자는 크게 신경쓰지 않아도 됨)

<br>

## [동시 요청 - 멀티 쓰레드]
트래픽이 많아질 경우 사용할 수 있는 기법
* 클라이언트에서 서버로 요청을 하게 되면 TCP/IP커넥션이 연결되고 Servlet을 호출한다.
* 그렇다면 Servlet은 누가 호출할까? **쓰레드**

### 쓰레드
* 애플리케이션 코드를 각각 순차적으로 실행함
* 자바 메인 메서드를 처음 실행하면 main이라는 이름의 쓰레드가 실행된다.
* 쓰레드가 없으면 자바 애플리케이션의 실행이 불가능하다.
* 쓰레드는 한번에 하나의 코드 라인만 수행함
* 동시 처리가 필요하면 쓰레드를 추가로 생성해줘야한다.

단일 요청 - 쓰레드 하나 사용
* 요청이 하나 오면 WAS에서 쓰레드를 하나 할당하고 이를 이용해 Servlet 코드 실행

다중 요청 - 쓰레드 하나 사용
* 요청1, 요청2가 들어올때 요청1에서 쓰레드의 Servlet처리가 지연될때 요청 2가 들어오면?
* 쓰레드를 사용할 수 있을때까지 기다림
* 결국 둘다 죽어버리는 상황이 발생함
* 이것을 해결하기 위해서 신규 쓰레드를 생성한다.
* 즉 **요청이 올 때 마다 쓰레드를 생성한다.**

### 요청이 올 때 마다 쓰레드 생성의 장단점
**장점**
* 동시 요청 처리 가능
* 리소스(CPU, 메모리)가 허용할 때 까지 처리 가능
* 하나의 쓰레드가 지연돼도, 나머지 쓰레드 정상 동작

**단점**
 * 생성 비용이 매우 비쌈
   * 요청 올 때마다 생성 -> 응답 속도 늦어짐
 * 컨텍스트 스위칭 비용이 발생 (쓰레드1에서 쓰레드2로 전환할때 발생하는 비용)   
   쓰레드는 실제적으로는 동시가 아닌 순차적으로 처리하지만 매우 빠른 CPU의 속도로 동시처리처럼 보임
* 쓰레드 생성에 제한이 없다.
  * CPU, 메모리가 임계점을 넘어서 서버가 다운되는 상황 발생함

### 쓰레드 풀(Thread Pool)
하나의 풀장을 생각(특정 양의 쓰레드를 pool안에 만들어 놓음)
* 요청이 오면 쓰레드 풀에서 쓰레드를 요청한다.
* 요청이 종료되면 쓰레드는 쓰레드 풀로 다시 반납함
* 즉 미리 만들어서 넣어놓고 빌려쓰고 가져다 놓는 방식
* 서버가 200개의 요청이 한계라면 200개를 넘어서는 요청들은 대기하거나, 거절할 수 있다.

### 쓰레드 풀 - 요청 마다 쓰레드 생성의 단점 보안
특징
* 필요한 쓰레드를 쓰레드 풀에 보관하고 관리
* 쓰레드 풀에 생성 가능한 쓰레드의 최대치를 관리한다. 톰캣은 최대 200개 기본 설정(변경 가능)

사용
* 쓰레드가 필요시, 이미 생성된 쓰레드를 쓰레드 풀에서 꺼내 사용
* 사용 종료하면 쓰레드 풀에 해당 쓰레드를 반납
* 최대 쓰레드가 모두 사용중이어서 쓰레드 풀에 쓰레드가 없으면?
  * 기다리는 요청은 거절하거나 특정 숫자만큼만 대기하도록 설정

장점
* 쓰레드가 미리 생성되어 있음, 쓰레드를 생성하고 종료하는 비용이 절약되고, 응답 시간 빠르다.
* 생성 가능한 쓰레드의 최대치가 있으므로 너무 많은 요청이 들어와도 기존 요청은 안전하게 처리할 수 있다.

### 쓰레드 풀 - 성능 튜닝 팁(실무)
* WAS의 주요 튜닝 포인트는 최대 쓰레드(max thread)의 수이다.
* 너무 낮게 설정
  * 동시 요청 많으면, 서버 리소스 여유롭지만, 클라이언트는 응답 지연
* 너무 높게 설정
  * 동시 요청 많으면, CPU, 메모리 리소스 임계점 초과로 서버 다운
* 장애 발생시?
  * 클라우드면 일단 서버부터 늘리고, 이후에 튜닝
  * 클라우드가 아니면 열심히 튜닝
  
### 쓰레드 풀의 적정 숫자는?
* 애플리케이션 로직의 복잡도, CPU, 메모리, IO 리소스 상황에 따라 모두 다름
* 성능 테스트
  * 최대한 실제 서비스와 유사하게 성능 테스트 시도
  * 툴: 아파치 ab. 제이미터, nGrinder
  
### 핵심 - WAS의 멀티 쓰레드 지원
* 개발자는 멀티 쓰레드 관련 코드를 신경쓰지 않아도 된다.
* 개발자는 마치 싱글 쓰레드 프로그래밍을 하듯이 편리하게 소스 코드를 개발
* 멀티 쓰레드 환경이므로 싱글톤 객체(서블릿, 스프링 빈)는 주의해서 사용(Stateless하게 사용)

<br>

## [HTML, HTTP API, CSR, SSR]
### HTML(정적, 동적), HTTP API
정적리소스 요청
* 고정된 파일은 그냥 **웹 서버**에서 이미 생성된 파일들을 보내준다.

HTML페이지(동적) 요청
* **WAS**에서 DB까지 조회해 동적으로 생성한 파일을 보내준다.

HTTP API
* HTML이 아닌 데이터를 전달한다. (주로 JSON)
* 다양한 곳에서 사용된다.
* **WAS**에서 DB를 조회해 JSON형식의 데이터를 전달한다.(페이지X)

HTTP API 사용   
HTTP API는 데이터만 주고 받으므로, 클라이언트가 별도로 처리한다.   
주로 앱 to 서버, 웹 클라이언트 to 서버, 서버 to 서버에서 사용된다.
* **웹 클라이언트**: 추가적으로 자바스크립트를 이용해(AJAX등) 필요한 페이지를 동적으로 만들어 뿌림

HTTP API는 다양한 시스템과 연동된다.
* 크게 두 가지의 접점으로 나뉜다.
    1. UI 클라이언트 접점: 앱, 웹 브라우저(자바스크립트통한 HTTP API 호출), React, Vue.js같은 웹 클라이언트
    2. 서버 to 서버: MAS(주문 서버 -> 결제 서버), 기업간 데이터 통신

> **백엔드 개발자는 크게 위의 3가지 방식을 고민해야 한다.**

### |SSR, CSR|
### Server Side Rendering
* 서버에서 최종 HTML을 생성해서 클라이언트에 전달      
  주로 정적인 화면에 이용
* 관련기술: JSP, 타입리프 -> **백엔드 개발자가 만듦**
### Client Side Rendering
* HTML결과를 자바스크립트를 사용해 웹 브라우저에서 동적으로 생성해 적용
* 주로 동적인 화면에서 이용한다. (웹 환경을 앱과같이 필요부분만 수정할 수 있음)   
  (구글지도, Gmail, 캘린더 등)
* 관련기술: React, Vue.js -> **웹 프론트엔드 개발자가 만듦**

> React, Vue.js를 CSR + SSR 동시에 지원하는 웹 프레임워크도 존재   
> SSR을 사용하더라도 자바스크립트를 사용해 화면 일부 동적으로 변경할 수 있음

### CSR 동작 과정
1. HTML을 클라이언트가 요청하면 서버는 HTML 내용은 없고 **자바스크립트 링크**만 반환함
2. 웹 브라우저는 다시 **자바스크립트를 서버**에 요청하고, 서버가 반환하는 자바스크립트 코드에 클라이언트 로직, HTML 렌더링 코드를 포함시켜 보내준다.
3. 웹 브라우저는 애플리케이션 로직을 가지고 있으므로, **HTTP API**를 서버에 요청한다. 서버는 해당 데이터를 클라이언트로 전송한다.(주로 JSON)
4. 웹 브라우저는 클라이언트 로직을 이용해 API를 조회했기 때문에 HTML 렌더링 코드에 섞어서 동적인 웹 페이지를 만든다.

### 어디까지 알아야 할까?
* **서버 사이드 렌더링(SSR) 기술만 필수로 잘 알아보자!** -> 주로 타임리프

<br>

## [자바 웹 기술 역사]
* 서블릿 -> JSP -> 서블릿 + JSP: MVC -> 이런 패턴을 가지고 프레임워크가 나타남(스트럿츠, 웹워크, 스프링 MVC)

* 이후 Annotation기반의 스프링 MVC가 등장함   
  거의 모든 사람들이 사용

* 스프링 부트의 등장   
  서버를 내장, 서버에 WAS 설치하고, War파일을 만들어 설치한 WAS에 배포하는 과정을 빌드 결과(Jar)에 WAS 서버를 포함해 -> **빌드 배포를 크게 단순화 시켜준다.**

* 현재의 최신 기술
  * Web Servlet - Spring MVC
  * Web Reactive - Spring WebFlux
  
* 스프링 웹 플럭스   
특징
  * 비동기 넌 블로킹 처리
  * 최소 쓰레드 최대 성능 (CPU코어와 쓰레드 개수를 맞춤 -> Context Switching 비용 효율 좋아짐)
  * 함수형 스타일 -> 동시처리 코드 효율화
  * 서블릿 기술 사용 X   
그러나
* 기술적 난이도 높고, RDB지원 적음, 아직 많이 활용하지 않음

### 자바 - 뷰 템플릿의 역사
HTML을 편리하게 생성하는 뷰 기능

* JSP
  * 속도 느림, 기능 부족
* 프리마커, 벨로시티
  * 속도 문제 해결, 다양한 기능
* 타임리프(Thymeleaf)
  * 내추럴 템플릿: HTML의 모양을 유지하면서 뷰 템플릿 적용 가능
  * 스프링 MVC와 기능 통합

<br><br>

# <서블릿>
## [프로젝트 생성]
* jar아닌 war로, 그래야 JSP를 사용할 수 있다.

<br>

## [Hello 서블릿]
스프링 부트 환경에서 서블릿을 등록하고 사용해보자
* 서블릿은 WAS를 직접 설치 및 위에 서블릿 코드를 클래스 파일로 빌드해서 올리고, WAS를 실행시켜야 한다.
* 하지만 스프링 부트의 내장 톰캣 서버를 이용해 설치없이 서블릿 코드를 실행할 수 있다.

### 스프링 부트 서블릿 환경 구성
* `@ServletComponentScan`
  * 스프링 부트는 서블릿을 직접 등록해서 사용할 수 있는 Annotation 지원
  * 어노테이션이 붙은 패키지 및 하위 패키지를 돌며 서블릿을 찾아서 자동으로 등록해줘 실행할 수 있게 한다.

### 서블릿 등록하기 hello.servlet.basic.HelloServlet.java
  * WAS는 HTTP 요청이 오면 Request, Response 객체를 만들어서 서블릿에게 넘겨준다.
  * 실제 Request, Response를 찍어보면 WAS서버가 서블릿 표준 스펙을 구현한 것을 알 수 있다.
  * Content-Body는 개발자 모드의 Response에서(혹은 우클릭 소스보기) 확인할 수 있다.

  * `@WebServlet`: 서블릿 어노테이션
    * `name`: 서블릿 이름
    * `urlPatterns`: URL매핑
    > 위 두개의 이름은 겹치면 안된다.
    * 서버 띄우고 매핑된 url을 호출하면 서블릿은 service() 메소드를 실행한다.

### HTTP 요청 메시지 로그로 확인하기
* `application.properties`에 `logging.level.org.apache.coyote.http11=debug` 추가   
  이후 서버 재시작하고 url을 요청하면 서버가 받은 HTTP 요청 메시지를 확인할 수 있다.
* 모든 요청을 남기면 성능저하의 원인이 되므로 개발단계에서만 적용하자

### 서블릿 컨테이너의 동작방법
1. 스프링 부트가 내장 톰켓 서버를 생성함
   1. 내부의 서블릿 컨테이너를 통해 모든 서블릿을 생성한다. (helloServlet)`
2. HTTP 요청, HTTP 응답 메시지: 요청을 하면 응답메시지 생성됨
3. 웹 브라우저가 요청하면, WAS는 HTTP 요청 메시지 기반으로 Request, Response 객체를 생성하고 helloServlet(싱글톤)   
   을 호출하고, service() 메소드를 실행하며 Request, Response 객체를 넘겨준다.
4. 필요작업을 수행하고(service()), 혹시 Response에 메시지를 넣게 되면 종료되고 나가면서 WAS서버가 Response정보를 가지고   
   HTTP 응답 메시지를 만들어서 반환한다.
5. 웹 브라우저에서 만든 메시지를 확인할 수 있다.
> Content-Length 및 부가적인 정보들은 웹 애플리케이션 서버가 자동으로 생성해준다.

### Welcome 페이지 추가
* webapp 경로에 index.html 을 두면 http://localhost:8080 호출시 index.html 페이지가 열린다.

<br>

## [HttpServletRequest - 개요]
**HttpServletRequest 역할**   
서블릿은 HTTP 요청 메시지를 편리하게 사용할 수 있게 HTTP 요청 메시지를 파싱한다.   
또한 그 결과를 `HttpServletRequest`객체에 담아서 제공한다.

**HTTP 요청 메시지 예시**
  ```
  POST /save HTTP/1.1
  Host: localhost:8080
  Content-Type: application/x-www-form-urlencoded
  
  username=kim&age=20 [바디]
  ```
* START LINE
  * HTTP 메소드
  * URL
  * 쿼리 스트링
  * 스키마, 프로토콜
* 헤더
  * 헤더 조회
* 바디
  * form 파라미터 형식 조회
  * message body 데이터 직접 조회
  

**HttpServletRequest의 부가기능**
1. 임시 저장소 기능
   * 저장: `request.setAttribute(name, value)`
   * 조회: `request.getAttribute(name)`

2. 세션 관리 기능
   * `request.getSession(create: true)`

> HttpServletRequest, HttpServletResponse는 객체들의 HTTP 요청 메시지, 응답 메시지를 편리하게 사용하도록
> 도와주는 객체라는 점이다.   
> **HTTP 스펙이 제공하는 요청, 응답 메시지 자체를 이해해야 깊이있는 이해를 할 수 있다.**

<br>

## [HttpServletRequest - 기본 사용법]
`hello/servlet/basic/request/RequestHeaderServlet.java` 코드 확인

> **참고**: 로컬 테스트는 IPv6 정보, IPv4 정보를 보고 싶다면 다음 옵션을 VM options에 추가   
> `-Djava.net.preferIpv4Stack=true`
> 
> HttpServletRequest를 통해 HTTP 메시지의 start-line, header 정보 조회 방법을 알아봄

<br>

## [HTTP 요청 데이터 - 개요]
HTTP 요청 메시지를 통해 클라이언트에서 서버로 데이터를 전달하는 대표적 방법

1. **GET - 쿼리 파라미터**
    * /url**?username=hello&age=20**
    * 메시지 바디 없이, URL의 쿼리 파라미터에 데이터를 포함해서 전달
    * 예) 검색, 필터, 페이징 등에서 많이 사용
2. **POST - HTML Form**
    * Content-Type: application/x-www-form-urlencoded
    * 메시지 바디에 쿼리 파라미터 형식으로 전달 username=hello&age=20
    * 예) 회원 가입, 상품 주문, HTML Form 사용
3. **HTTP message body**에 데이터를 직접 담아서 요청
    * HTTP API에서 주로 사용, JSON, XML, TEXT(주로 JSON사용)
    * POST, PUT, PATCH

> Content-Type은 Body에 대한 정보에 대해서 설명함(어떤 스타일의 데이터인지)

<br>

## [HTTP 요청 데이터 - GET 쿼리 파라미터]
전달 데이터
* username=hello
* age=20

위의 데이터를 메시지 바디 없이, URL의 **쿼리 파라미터**를 사용해서 데이터를 전달하자.   
쿼리 파라미터의 시작은 `?`, 추가 파라미터는 `&`로 구분한다.
* 만드려는 URL
  * /request-param?username=hello&age=20

HttpServletRequest가 제공하는 쿼리 파라미터 조회 메소드

```java
request.getParameter("username"); // 단일 파라미터 조회
request.getParameterNames(); // 파라미터 이름들 모두 조회
request.getParameterMap(); // 파라미터를 Map으로 조회
request.getParameterValues("username"); // 복수 파라미터 조회
```

**복수 파라미터에서 단일 파라미터 조회**   
`request.getParameter()` --> 하나의 파라미터 이름에 대해 하나의 값만 있을 때 사용해야 함   
`request.getParameterValues()` --> 하나의 파라미터 이름에 대해 여러 값이 있을때 모든 값을 반환함   

<br>

## [HTTP 요청 데이터 - POST HTML Form]
HTML Form을 이용해 클라이언트에서 서버로 데이터를 전송   
회원가입, 상품 주문 등에서 사용되는 방식이다.   

POST의 HTML Form을 전송하면 웹 브라우저는 다음과 같이 HTTP 메시지를 만듦
* request URL: http://localhost:8080/request-param
* **Content-Type:** `application/x-www-form-urlencoded`
* **message body:** `username=hello&age=20`

`application/x-www-form-urlencoded`형식과 GET의 쿼리 파라미터 형식은 동일하다.   
따라서 기존의 **쿼리 파라미터 조회 메서드를 그대로 활용할 수 있다.**   
이 둘의 차이는 클라이언트에서만 나타나고, 서버에서는 둘의 형식이 동일하기 때문

> **참고**
> **content-type은 HTTP 메시지 바디의 데이터 형식을 지정한다.**   
> `GET URL 쿼리 파라미터 형식`: HTTP 메시지 바디를 사용하지 않으므로 content-type 없음   
> `POST HTML Form 형식`: HTTP 메시지 바디에 데이터를 포함해서 보냄 따라서 데이터의 형식을 content-type으로 꼭     
> 지정해야한다. 이렇게 폼으로 데이터를 전송하는 형식을 applicaiton/x-www-form-urlencoded라 함
> 
> <br>
> HTML Form 대신 Postman으로 테스트하면 좀 더 간편하게 테스트할 수 있다.

<br>

## [HTTP 요청 데이터 - API 메시지 바디 - 단순 텍스트]
* **HTTP message body**에 데이터를 직접 담아서 요청
  * HTTP API에서 주로 사용, JSON, XML, TEXT
  * 데이터 형식은 주로 JSON 사용
  * POST, PUT, PATCH 에서 주로 사용된다.

**문자전송**
* POST `http://localhost:8080/request-body-string`
* content-type: text/plain
* message body: hello

<br>

## [HTTP 요청 데이터 - API 메시지 바디 - JSON]
HTTP API에서 주로 사용하는 JSON 형식으로 데이터를 전달

**JSON 형식 전송**
* POST http://localhost:8080/request-body-json
* content-type: **application/json**
* message body: `{"username": "hello", "age": 20}`

JSON 형식으로 파싱할 수 있는 객체를 만들어야함 `hello.servlet.basic.HelloData.java`   
(setter, getter는 롬복 라이브러리 이용)

Postman을 이용해 실제 위의 json 데이터를 전달하고,   
JSON 라이브러리인 `jackson`을 이용해 파싱한 값을 자바 객체를 이용해 뽑아낼 수 있다.

> **참고**   
> JSON 결과를 파싱해서 사용할 수 있는 자바 객체로 변환하기 위해선 Jackson, Gson같은 JSON 변환 라이브러리를 추가해서   
> 사용해야함. (스프링 부트의 Spring MVC를 선택하면 기본으로 Jackson라이브러리 제공(`ObjectMapper`))

> **참고**   
> HTML Form 데이터도 메시지 바디를 통해 전송됨 따라서 직접 읽기가 가능하다.   
> 하지만 편리한 파라미터 조회 기능을 이미 제공해주므로 그것을 사용하면 된다(request.getParameter(...))  
> 
> 실제로 Postman을 통해 Form데이터를 전송하면 messgae body에 담겨옴.   
> 하지만 json형식이 아니므로 파싱은 불가능하다.

<br>

## [HttpServletResponse - 기본 사용법]
**역할: HTTP 응답 메시지 생성**
* HTTP 응답 코드 지정
* 헤더 생성
* 바디 생성

**편의 기능 제공**
* Content-Type(헤더 편리하게 지정), 쿠키(편리하게 생성), Redirect(300대 리다이렉트 쉽게 세팅가능)

<br>

## [HTTP 응답 데이터 - 단순 텍스트, HTML]
HTTP 응답 메시지는 주로 다음 내용을 담아 전달
* 단순 텍스트 응답
  * 앞에서 살펴봄(`writer.print("ok);`)
* HTML 응답
* HTTP API - MessageBody JSON 응답

HTTP 응답으로 HTML을 반환할 때는 content-type을 `text/html` 로 지정해야 한다

<br>

## [HTTP 응답 데이터 - API JSON]
HTTP 응답으로 JSON을 반환할 때 `Content-Type`을 `application/json`으로 지정해야함.    
Jackson 라이브러리가 제공하는 `ObjectMapper.writeValueAsString()`을 사용해 객체를 JSON문자로 변경한다.

> **참고**   
> `application/json`은 스펙상 utf-8 형식을 사용하게 정의됨   
> 따라서 charset=utf-8 같은 추가 파라미터를 지원하지 않는다.   
> 즉 위의 결과와 같이 `applicaiton/json;charset=utf-8`에서 charset은 의미가 없다.  
> 
> response.getWriter()는 추가 파라미터를 자동으로 추가하지만,   
> response.getOutputStream()으로 출력하면 위와 같은 문제가 없다.

<br>

## [서블릿 정리]
`HttpServletRequest`는 HTTP 요청 메시지 스펙을 편리하게 사용할 수 있도록(조회 등) 도와주는 도구이다.
* start-line, header 등의 편의 사용

`HTTP 요청 데이터`는 크게 3가지의 방법이 존재
1. GET - 쿼리 파라미터
2. POST - HTML Form (POST 방식만 허용된다.)
3. HTTP message body (JSON이 거의 표준)
> 서버에서 읽을때는 1, 2 방식 모두 동일하게 읽을 수 있다. request.getParameter(); --> 요청 파라미터를 읽는다 라고 표현

`HTTP 응답 데이터`는 단순 텍스트, HTML, API JSON으로 응답할 수 있다.   
헤더들은 여러가지 편리한 메서드들을 통해 지정할 수 있다.

<br><br>

# <서블릿, JSP, MVC 패턴>
## [회원 관리 웹 애플리케이션 요구사항]
**회원 정보**
* 이름: `username`
* 나이: `age`

**기능 요구사항**
* 회원 저장
* 회원 목록 조회

스프링 없는 순수 자바, 서블릿 만으로 구성되는 간단한 코드

<br>

## [서블릿으로 회원 관리 웹 애플리케이션 만들기]
가장 먼저 서블릿으로 회원 등록 HTML 폼을 제공해보자

MemberSaveServlet 은 다음 순서로 동작한다.
1. 파라미터를 조회해서 Member 객체를 만든다.
2. Member 객체를 MemberRepository를 통해서 저장한다.
3. Member 객체를 사용해서 결과 화면용 HTML을 동적으로 만들어서 응답한다.

**템플릿 엔진의 등장**   
소스코드를 살펴보면 (hello/servlet/web/servlet.*) 동적인 HTML 코드를 서블릿으로 만드는 과정은   
상당히 힘들다는것을 알 수 있다.   
반면에 템플릿 엔진을 이용하면 HTML 문서에 동적으로 변경해야 하는 부분만 자바 코드를 넣을 수 있고, 훨씬 편리하다.   
템플릿 엔진은 대표적으로 JSP, Thymeleaf, Freemarker, Velocity등이 존재한다.   

> **참고**   
> Thymeleaf가 Spring과의 통합을 잘 지원해주므로 적절하다.

<br>

## [JSP로 회원 관리 웹 애플리케이션 만들기]
**JSP 라이브러리 추가**
```java
//JSP 추가 시작
    implementation 'org.apache.tomcat.embed:tomcat-embed-jasper'
    implementation 'javax.servlet:jstl'
//JSP 추가 끝
```
* JSP는 내부적으로 서블릿 코드로 변환된다.
  * 따라서 이전 시간에 서블릿으로 만들었던 코드들과 비슷한 모습을 보인다.

* JSP는 자바 코드를 그대로 다 사용할 수 있다.
  * `<%@ page import="hello.servlet.domain.member.MemberRepository" %>`   
    * 자바의 import 문과 같다. 
  * `<% ~~ %>`
    * 이 부분에는 자바 코드를 입력할 수 있다. 
  * `<%= ~~ %>`
    * 이 부분에는 자바 코드를 출력할 수 있다.

  > 위의 구문을 이용해 HTML중간에 자바코드를 삽입할 수 있다.

**서블릿, JSP의 한계점**   
서블릿은 뷰 화면에 대한 HTML을 만드는 과정이 복잡함   
이를 JSP를 이용해 뷰 화면을 깔끔하게 만들었고, 중간에 자바 코드를 끼워넣을 수 있다.   

하지만 회원의 정보를 저장, 가져오기 등과 같은 비즈니스 로직은 상위에 존재하고,   
하위 HTML은 보여주기 위한 뷰의 영역이다.   
**즉 비즈니스 로직과 뷰의 화면을 분리하는것에 필요성을 느낀다.**
* **MVC 패턴의 등장**
  * 비즈니스 로직은 서블릿 처럼 다른곳에서 처리하고, JSP는 HTML로 화면을 그리는(view) 역할에만 집중

<br>

## [MVC 패턴 - 개요]
하나의 서블릿, JSP로 뷰 렌더링까지 신경쓰다 보면 너무 많은 역할을 한가지에 담다보니 유지보수의 어려움이 존재   
**또한 비즈니스 로직과 뷰의 변경 라이프 사이클이 다르다.**
* UI의 변경과 비즈니스 로직의 변경은 각각 다르게 발생하고 서로에게 영향을 주지 않는경우가 빈번하므로   
  이 두가지를 한나의 파일로 관리하는것은 어려움이 따른다.
* 또한 기능을 특화시켜 뷰 템플릿(JSP)은 화면만 담당하는것이 효과적이다.

**Model View Controller**   
MVC 패턴은 하나의 서블릿, JSP로 처리하던 것을 컨트롤러와 뷰의 영역으로 서로 역할을 나눈것을 말한다.

* **Controller**: HTTP 요청을 받아 파라미터 검증, 비즈니스 로직 실행, 뷰에 전달할 데이터 조회 후 모델에 담음
* **Model**: 뷰에 출력할 데이터를 모두 담아 뷰에 전달함, 따라서 뷰는 화면을 렌더링하는것에만 집중할 수 있다.
* **View**: 모델에 담겨있는 데이터를 이용해 화면을 렌더링한다. (HTML을 생성하는 부분)

> **참고**: 실제 컨트롤러에 비즈니스 로직까지 같이 둔다면 컨트롤러의 해야할 일이 많아지므로   
> 비즈니스 로직의 부분은 서비스라는 계층을 만들어 별도로 처리한다.   
> 컨트롤러는 비즈니스 로직이 있는 서비스를 호출하는 역할을 한다.
> 
> 대략적 흐름   
> 컨트롤러는 고객의 요청을 받고 검증한다. 고객의 요청이 잘못된 형식이면 오류 반환   
> 그렇지 않다면 서비스 계층을 호출해 비즈니스 로직을 실행한다.(주문, 결제 등)
> 이후 컨트롤러는 결과를 받아서 모델에 비즈니스 로직의 결과(데이터)를 담고 뷰에 넘김.
> 뷰는 모델을 이용해 화면을 결과화면 생성

<br>

## [MVC 패턴 - 적용]
서블릿 - Controller   
JSP - View
Model - HttpServletRequest 객체 이용
* `request.setAttribute(...)`, `request.getAttribute(...)`

### **hello/servlet/web/servletmvc/MvcMemberFormServlet.java 설명**   
`WEB-INF`   
* 경로안에 JSP가 존재하면 외부에서 JSP를 직접호출할 수 있음, 하지만 항상 컨트롤러를 통해 JSP를 호출해야 하므로   
  이곳에 파일을 두면 외부에서의 접근을 막을 수 있다.

`dispatcher.forward()`: 서버 내부에서 다시 호출이 발생한다. 주로 다른 서블릿, JSP로 이동할때 사용
* **Redirect 와 forward**
  * Redirect는 클라이언트에 응답이 나간 후, 클라이언트가 redirect경로로 다시 요청한다.   
    이는 클라이언트가 인지할 수 있고, 실제 URL도 변경이 발생
  * forward는 서버 내부에서 일어나는 호출이므로 클라이언트가 인지하지 못함, 따라서 URL도 변경되지 않음

### **main/webapp/WEB-INF/views/new-form.jsp 설명**   
action의 위치를 상대경로로 지정했을때 생기는 일   
`URL 경로: http://[현재URL].../.../[상대경로 추가]`   
* 위와 같이 현재 URL의 계층 경로 + 상대경로가 호출된다.
* 반면에 절대경로는 기본 주소 + 절대경로가 직접 호출됨,   
> 절대경로를 추천하지만 현재 이 폼은 재사용을 할것이므로 다른 곳에서의 활용을 위해 상대경로로 작성

### **main/webapp/WEB-INF/views/save-result.jsp 설명** 
EL 태그를 이용해 ((Member)request.getAttribute("Member")).getXxx() 코드를 쉽게 호출할 수 있다.   
**${member.xxx}**

또한 request객체를 Model로 이용하여 값을 전달하고 받는다.

### **main/webapp/WEB-INF/views/members.jsp 설명**
JSTL(taglib) 태그를 이용해 리스트 객체를 반복문과 함꼐 쉽게 화면에 출력할 수 있다.

> **알아두자**: 모든 요청은 항상 컨트롤러가 받아서 이후 비즈니스 로직 처리후 화면에 결과를 렌더링함   
> 심지어 로직이 없어도 컨트롤러를 꼭 거친 후 뷰로 간다.(JSP를 WEB-INF/ 아래에 두는 이유)
> 
> **MVC 덕분에 컨트롤러 로직과 뷰 로직을 확실하게 분리할 수 있다. 또한 변경 사이클을 달리하여 관리할 수 있다.**

<br>

## [MVC패턴 - 한계]
컨트롤러에서 중복되는 코드가 많고, 필요하지 않은 코드들도 많이 보인다.   

### **MVC 컨트롤러의 단점**
**포워드 중복**   
view로 이동하는 코드가 항상 중복 호출되어야 함, 메서드로 빼내에도 해달 메소드도 직접 호출해야하는 불편함 존재

**ViewPath의 중복**
`String viewPath = "WEB-INF/views/new-form.jsp"`
* prefix: `/WEB-INF/views/`
* suffix: `.jsp`
또한 다른 뷰 템플릿으로 변경하고자 한다면 전체 코드를 전부 변경해야함

**사용하지 않는 코드들**   
`HttpServletRequest, HttpServletResponse 중에서 사용하지 않는 코드가 존재한다.`   
또한 위의 객체를 사용하는 코드는 테스트 케이스를 작성하기 어렵다.

**공통 처리의 어려움**   
기능이 복잡 -> 컨트롤러에서 공통 처리 요소가 많아짐 -> 공통 메소드로 묶는다 해도 -> 직접 호출해야함   
-> 호출이 누락되면(여러 컨트롤러 중 하나라도) 문제가 발생됨 -> 문제의 반복

> 결국 공통 처리의 어려움이 가장 큰 문제가 된다.   
> 이것을 해결하기 위해선 컨트롤러의 호출전에 콩통 기능을 처리해야 한다.   
> 이를 위해 도입된 것이 **Front Controller 패턴**이다. (입구를 하나로 좁힘)   
> 이는 스프링 MVC의 핵심(필터는 정해진 스펙대로 체인을 넘기는 것이므로 필터와는 다른 개념)

<br><br>

# <MVC 프레임워크 만들기>
## [프론트 컨트롤러 패턴 소개]
**Front Controller Pattern 특징**
* 프론트 컨트롤러 서블릿 하나로 클라이언트의 요청을 받음
* 프론트 컨트롤러가 요청에 맞는 컨트롤러를 찾아서 호출
* 입구를 하나로
* 공통 처리 가능
* 프론트 컨트롤러를 제외한 나머지 컨트롤러는 서블릿을 사용하지 않아도 된다.

**스프링 웹 MVC와 프론트 컨트롤러**   
스프링 웹 MVC의 **DispatcherServlet**이 FrontController 패턴으로 구현되어 있다.

<br>

## [프론트 컨트롤러 도입 - v1]
프론트 컨트롤러를 단계적으로 도입   
구조를 맞춰두고 점진적인 리팩토링

**v1의 구조**
1. HTTP 요청
2. Front Controller가 받아서 URL 매핑 정보에서 컨트롤러 조회
3. 컨트롤러 호출
4. 컨트롤러에서 JSP forward
5. HTML 응답

**프론트 컨트롤러-v1은 (서블릿과 비슷한 모양의)인터페이스로 구성됨**   
따라서 프론트 컨트롤러는 인터페이스를 호출해서 구현과 관계없이 로직의 일관성을 가짐   
각 컨트롤러들은 이 인터페이스를 구현하면 된다.   
**기존 로직을 유지하는 것이 핵심**

### **FrontControllerV1**   
* urlPattern 에서 .../*은 어떤 url이 들어와도 상위 url(...)을 포함하면 모두 프론트 컨트롤러 서블릿을 호출한다.
* requestURI를 이용해서 실제 호출 컨트롤러를 Map에서 찾고 ControllerV1.process(...)를 호출해 컨트롤러를 호출한다.   
  없다면 404 반환



> **TIP** : 구조적인 것을 개선할 때는 구조적인것만 개선하고 기존 코드는 최대한 유지   
> 구조 변경 후 문제가 없으면 이 떄 세세한 부분을 개선하는것이 좋다.   
> `구조 개선 - 커밋 - 배포 - 세세한 변경 - 커밋 - 배포`

<br>

## [View 분리 - v2]
프론트 컨트롤러를 도입했지만, 모든 컨트롤러에서 뷰로 이동하는 부분에 중복이 존재함.   
`viewPath, RequestDispatcher, forward 등`   
이를 해결하기 위한 별도로 뷰를 처리하는 객체를 만들자

**뷰 처리 객체의 동작 - HTTP 요청이 들어올때**   
1. Front Controller가 요청을 받음
2. URL 매핑 정보에서 컨트롤러 조회
3. 컨트롤러 호출
4. **MyView 객체 반환**
5. render() 호출
6. JSP Forward

* 이전 v1 에서는 컨트롤러가 직접 forward를 해주었으나, 이젠 MyView라는 객체를 다시 Front Controller에 반환한다.   
* v1, v2 등 범용적으로 사용하므로 `hello/servlet/web/frontcontroller/.`위치에 생성한다.

### **ControllerV2의 도입**   
`ControllerV2.process()`의 반환타입이 MyView 이므로 프론트 컨트롤러는 호출 결과로 `MyView`를 반환 받는다.   
이후 `MyView.render()`를 호출하면 forward 로직을 수행해 JSP가 실행됨
> **TIP:** 만약 JSP 말고 다른 템플릿을 렌더링 해야하는 상황이 발생하면 MyView 클래스도 인터페이스화 시켜서 이용하면 된다.

### **또다시 개선해야 할 부분**
> 이제 Model이라는 개념을 추가하고, process()에서 강제로 Request, Response 객체를 받아야 하는 부분을 수정해보자

<br>

## [Model 추가 - v3]
**서블릿 종속성 제거**   
컨트롤러 입장에서 HttpServletRequest, HttpServletResponse는 꼭 필요하지 않다.   
요청 파라미터 정보는 자바의 Map으로 변경해 대신 넘기도록 하면 현재 구조에선 컨트롤러가 서블릿 없이 동작가능하다.   
또한 request 객체를 Model로 사용하지 않고 별도의 Model을 만들어서 반환   
이렇게 되면 구현 코드의 단순화 및 테스트 용이성을 얻을 수 있다.

**뷰 이름 중복 제거**   
컨트롤러는 **뷰의 논리 이름(new-form, save, memebers)** 을 반환하고 실제 물리적인 위치는 프론트 컨트롤러에서 처리시킴  
/WEB-INF/views/, .jsp --> 중복되는 이름을 하나로 묶어주면 후에 기존 경로가 변경되어도 변경 지점이 하나이므로 좋은 설계

### **V3 구조**
HTTP 요청이 오면
1. front Controller는 `컨트롤러 조회`
2. front Controller가 `컨트롤러를 호출`
3. 컨트롤러는 `ModelView객체 반환`
4. frontController는 `viewResolver를 호출`
5. viewResolver는 `MyView 객체 반환`
6. front controller `render(model) 호출`
7. `HTML 응답`

서블릿의 종속성 제거 위해 Model 직접 만들고, View 이름까지 전달하는 객체를 만들어보자   
다른 버전에서도 사용하므로 frontcontroller 패키지에 둠

**뷰 리졸버**   
`MyView view = viewResolver(viewName)`   
컨트롤러가 반환한 논리 뷰 이름을 실제 물리 뷰 경로로 변경, 이후 물리경로 담은 MyView 객체 반환
* 논리 뷰: `members`
* 물리 뷰: `/WEB-INF/views/members.jsp`
`view.render(mv.getModel(), request, response)`   
* 뷰 객체를 통해 HTML 화면을 렌더링
* 뷰 객체의 `render()`는 모델정보도 같이 받는다.
* JSP는 `request.getAttribute()로 데이터를 조회함`
### front controller의 일을 더욱 많아 졌지만, 실제 구현하는 컨트롤러는 더욱 간단해짐

<br>

## [단순하고 실용적인 컨트롤러 - v4]
v3 컨트롤러는 항상 ModelView 객체를 생성하고 반환해야 하는 부분이 번거로움   
좋은 프레임워크 == 사용자 입장에서 단순하고 편리하게 사용가능(실용성)

**V4 구조**   
HTTP 요청이 들어옴
1. Front Controller의 `컨트롤러 조회`
2. Front Controller의 `컨트롤러 호출 (paramMap, model)`
3. 컨트롤러의 `ViewName 반환`
4. Front Controller의 `viewResolver 호출`
5. `MyView 반환`
6. Front Controller의 `render(model)호출`
> 기본적인 구조는 v3와 같다. 대신에 컨트롤러가 `ModelView`가 아닌 `viewName`만 반환한다.

**정리**   
뷰의 논리 이름을 직접 반환 -> 컨트롤러가 직접 뷰의 논리 이름을 반환하므로 이 값을 이용해 물리 뷰를 찾음   
> 단순하고 실용적인 방법 아주 작은 아이디어(뷰의 논리 이름만 반환)가 큰 변화를 주었다.   
> 프레임워크의 점진적 발전 과정 속에서 계속 새로운 방법을 찾음   
> **프레임 워크의 기능이 수고로워야 사용하는 개발자가 편해짐**

만약 V4 말고 V1도 같이 사용하고 싶다면? --> 현재 구조에선 사용할 수 없음(프론트 컨트롤러의 Map 필드가 특정 컨트롤러로 고정되어 있으므로)   
인터페이스로 제약하는것의 장점이자 단점임 --> **어떤 컨트롤러든지 호출할 수 있는 프론트 컨트롤러는 없을까?**

<br>

## [유연한 컨트롤러1 - v5]
사람마다 원하는 컨트롤러의 방식이 다를 수 있다. (ControllerV3, ControllerV4 등)   
하나의 프로젝트에서 다양한 컨트롤러를 사용하는 방법을 알아보자

### **Adapter Pattern**   
우리가 개발한 컨트롤러는 한가지 방식의 컨트롤러 인터페이스만 사용 가능   
컨트롤러 끼리는 완전히 다른 인터페이스로, 호환이 불가능하다.   
이것을 어댑터 패턴을 이용하면 프론트 컨트롤러가 다양한 방식의 컨트롤러를 처리할 수 있도록 변경해보자

**V5 구조**   
클라이언트의 HTTP 요청   
1. [Front Controller] `핸들러 매핑 정보`에서 핸들러 조회
2. [Front Controller] `핸들러 어댑터 목록`(특정 컨트롤러를 처리할 수 있는 어댑터를 찾아옴)에서 핸들러를 처리할 수 있는 핸들러 어댑터 조회
3. [Front Controller] 핸들러 어댑터로 `handle(handler)`실행
4. [핸들러 어댑터] `handler` 호출
5. [핸들러 어댑터] `ModelView` 반환 (프론트 컨트롤러로)
6. [Front Controller] `viewResolver` 호출
7. [viewResolver] `MyView` 반환
8. [FrontController] `render(model)`로 MyView를 호출해 `HTML응답`을 실행한다.
> **핸들러 어댑터**: 중간에서 어댑터의 역할을 함, 다양한 종류의 컨트롤러를 호출 할 수 있게 해준다.   
> **핸들러**: 컨트롤러의 이름을 더 넓은 범위인 핸들러로 변경, 어댑터가 존재하므로 꼭 컨트롤러의 개념 뿐 아니라   
> 어떤 것이든 해당하는 종류의 어댑터가 존재하면 처리할 수 있다.

> **[정리]**   
> V3의 컨트롤러들은 모두 ModelView를 반환하므로, ModelView를 반환하는 어댑터 `ControllerV3HandlerAdapter`와   
> 호환이 잘 되어 변환로직이 단순하다.

<br>

## [유연한 컨트롤러2 - v5]
`FrontControllerServletV5` 에 `ControllerV4` 기능도 추가해보자.

**핵심 코드**
```java
    String viewName = controller.process(paramMap, model);
    
    ModelView mv = new ModelView(viewName);
    mv.setModel(model);
    
    return mv;
```
* 어댑터 패턴의 역할이 잘 드러나는 부분이다.
  * 기존 v4컨트롤러는 view의 논리적인 주소값만 반환했지만 이를 `어댑터가 공통된 처리`를 위해 ModelView에 담아   
    ModelView 객체를 반환하여 실제 FrontControllerServletV5에선 이와같은 변환을 신경쓰지 않도록 할 수 있다.

> **[정리]**   
> `FrontControllerServletV5`에서 설정하는 부분(생성자에서 이루어짐)만 밖으로 빼내어 따로 DI를 하게 되면 완벽하게 OCP를 지킬 수 있다.   
> 즉 기능을 확장하더라도 코드를 변경 할 필요가 없다.  
> 
> 지금 만들어온 MVC 프레임워크들은 역할과 구현이 잘 분리되어 있다.   
> 인터페이스 기반으로 놓아두고 변경하고 싶은 부분만 구현체를 꽂아서 넣으면 됨
> 
> 이것이 스프링 MVC가 제공해주는 기능들이다.
> 스프링은 과거 인터페이스 기반으로 만들어오다 어노테이션을 도입하면서 @Controller만 이용하더라도   
> 기존의 핸들러 어댑터를 이용할 수 있게 되었다.

<br>

## [MVC 프레임워크 만들기 - 정리]
* **v1: 프론트 컨트롤러 도입**
  * 기존 구조 최대한 유지하며 프론트 컨트롤러 도입 (구조 변경, 세세한 변경은 따로따로하자)
* **v2: View 분류**
  * 단순 반복 되는 뷰 로직 분리
* **v3: Model 추가**
  * 컨트롤러의 서블릿 종속성 제거
  * 뷰 이름 중복 제거
* **v4: 단순하고 실용적인 컨트롤러**
  * v3와 거의 비슷
  * 구현 입장에서 ModelView를 직접 생성해서 반환하지 않음 -> 더 편리한 인터페이스 제공
* **v5: 유연한 컨트롤러**
  * 어댑터 도입
  * 어댑터를 추가해 프레임워크를 유연하고 확장성 있게 설계

애노테이션을 사용해서 컨트롤러를 편리하게 발전 시킬 수 있는데, 애노테이션을 지원하는 어댑터만 추가하면 된다.   
다형성과 어댑터 덕분에 기존 구조를 유지하며, 프레임워크의 기능을 확장 할 수 있다.

<br><br>
# <스프링 MVC - 구조 이해>
## [스프링 MVC 전체 구조]
우리가 만들었던 MVC 프레임워크 구조와, 실제 Spring MVC의 구조는 동일하다.   
다만 용어에서 차이가 존재한다.

**직접 만든 프레임워크 -> 스프링 MVC 비교**
* FrontController -> **DispatcherServlet**(핵심)
* handlerMappingMap -> HandlerMapping
* MyHandlerAdapter -> HandlerAdapter
* ModelView -> ModelAndView
* viewResolver -> ViewResolver
* MyView -> View

<br>

### DispatcherServlet 구조 살펴보기
`org.springframework.web.servlet.DispatcherServlet`

스프링 MVC도 프론트 컨트롤러 패턴 구조   
여기서 프론트 컨트롤러가 DispatcherServlet이다   
이것이 스프링 MVC의 핵심

<br>

**DispatcherServlet 서블릿 등록**
* `DispatcherServlet`도 부모 클래스에서 `HttpSerlvet`을 상속받아 사용함, 역시 서블릿으로 동작   
  * `DispatcherServlet` -> `FrameworkServlet` -> `HttpServletBean` -> `HttpServlet`
* 스프링 부트는 `DispatcherSerlvet`을 서블릿으로 자동 등록함 이때 **모든 경로(urlPatterns="/")에** 대해서 매핑   
  **(스프링 부트는 내장 톰캣을 띄우면서 디스패처 서블릿을 서블릿으로 등록한다.)**
  > 참고: 더 자세한 경로가 우선순위 높음, 따라서 기존에 등록한 서블릿도 함꼐 등록

<br>

**요청 흐름**
* 서블릿이 호출되면 `HttpServlet` 제공하는 `service()` 호출 됨   
  (스프링 MVC는 `DispatcherServlet`의 부모인 `FrameworkSerlvet`에서 `service()`를 오버라이딩함)
* `FrameworkServlet.service()`를 시작으로 여러 메서드가 호출되면서 `DispatcherServlet.doDispatch()`가 호출됨

DispatcherServlet의 핵심인 doDispatch() 코드의 분석(인터셉터 제외)은 강의자료를 보자(실제 DispatcherServlet도 분석)

<br>

**SpringMVC 구조의 동작 순서(그림은 자료 참고)**
1. **핸들러 조회**: 핸들러 매핑을 통해 요청 URL에 매핑된 핸들러(컨트롤러)를 조회한다.
2. **핸들러 어댑터 조회**: 핸들러를 실행할 수 있는 핸들러 어댑터를 조회한다.
3. **핸들러 어댑터 실행**: 핸들러 어댑터를 실행한다.
4. **핸들러 실행**: 핸들러 어댑터가 실제 핸들러를 실행한다.
5. **ModelAndView 반환**: 핸들러 어댑터는 핸들러가 반환하는 정보를 ModelAndView로 **변환**해서 반환함
6. **viewResolver 호출**: 뷰 리졸버를 찾고 실행한다.
    * JSP의 경우: `InternalResourceViewResolver`가 자동 등록되고, 사용된다.
7. **View 반환**: 뷰 리졸버는 뷰의 논리 이름을 물리 이름으로 바꾸고, 렌더링 역할을 담당하는 뷰 객체를 반환
    * JSP의 경우: `InternalResourceView(JstlView)`를 반환하는데 내부에 forward()로직 존재
8. **뷰 렌더링**: 뷰를 통해서 뷰를 렌더링 한다.

<br>

**인터페이스 살펴보기**
* Spring MVC의 가장 큰 강점은 `DispatcherServlet`코드의 변경 없이, 원하는 기능을 변경하거나 확장할 수 있다.   
  지금까지 설명한 대부분을 확장 가능할 수 있게 인터페이스로 제공한다.
* 인터페이스들만 구현해서 `DispatcherSerlvet`에 등록하면 개인의 컨트롤러 만들 수 있음

<br>

**주요 인터페이스 목록**
* 핸들러 매핑: `org.springframework.web.serlvet.HandlerMapping`
* 핸들러 어댑터: `org.springframework.web.serlvet.HandlerAdapter`
* 뷰 리졸버: `org.springframework.web.serlvet.ViewResolver` (우리는 단순히 메소드로 구현함)
* 뷰: `org.springframework.web.serlvet.View` (JSP, ThymeLeaf용 View 등 여러개로 나눠짐)

<br>

**정리**
> 핵심 동작을 잘 알아둬야 향후 문제가 발생했을때 어떤 부분에서의 문제인지 파악하기 쉽다.   
> 또한 확장 포인트가 필요할 떄, 어떤 부분을 확장해야 할지 알 수 있다.   
> 숲을 보고 나무를 보자

<br>

## [핸들러 매핑과 핸들러 어댑터]
지금은 거의 사용하지 않지만, 과거 스프링이 제공하는 간단한 컨트롤러로 핸들러 매핑과 어댑터를 이해하자

### Controller 인터페이스
**과거 버전 스프링 컨트롤러**
`org.springframework.web.serlvet.mvc.Controller`
```java
public interface Controller {
    ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
        throws Exception;
}
```
> 위의 Controller 인터페이스는 @Controller 어노테이션과 완전히 다름

**위의 컨트롤러는 어떻게 호출될 수 있을까?**   
위의 컨트롤러가 호출되려만 다음 2가지가 필요
* **HandlerMapping(핸들러 매핑)**
  * 핸들러 매핑에서 이 컨트롤러를 찾을 수 있어야 한다.
  * 예) **스프링 빈의 이름으로 핸들러를 찾을 수 있는 핸들러 매핑이 필요** (빈의 이름으로 조회했으므로)
* **HandlerAdapter(핸들러 어댑터)**
  * 핸들러 매핑을 통해서 찾은 핸들러를 실행할 수 있는 어댑터 필요
  * 예) `Controller` 인터페이스를 실행할 수 있는 핸들러 어댑터를 찾고 실행한다.

<br>

**스프링 부트가 자동 등록하는 핸들러 매핑과 핸들러 어댑터의 대표적 예시**

**HandlerMapping**
```
우선순위
0 = RequestMappingHandlerMapping     : 애노테이션 기반의 컨트롤러인 @RequestMapping에서 사용됨
1 = BeanNameUrlHandlerMapping        : 스프링 빈의 이름으로 핸들러를 찾는다.
```

**HandlerAdapter**
```
0 = RequestMappingHandlerAdapter    : 애노테이션 기반의 컨트롤러인 @RequestMapping에서 사용됨
1 = HttpRequestHandlerAdapter       : HttpRequestHandler 처리
2 = SimpleControllerAdapter         : Controller 인터페이스 (애노테이션x, 과거에 사용) 처리 
```
> 핸들러 매핑, 핸들러 어댑터 모두 우선순위 순서대로 찾고 없으면 다음 순위를 조회

<br>
 
**OldController의 핸들러 매핑 및 핸들러 어댑터 조회**

**1. 핸들러 매핑으로 핸들러 조회**
1. `HandlerMapping`을 순서대로 실행해, 핸들러를 찾음
2. 빈 이름으로 핸들러를 찾아야 하므로, `BeanNameUrlHandlerMapping`이 실행에 성공하고 핸들러인 `OldController` 반환

**2. 핸들러 어댑터 조회**
1. `HandlerAdapter`의 `supports()`를 순서대로 호출
2. `SimpleControllerHandlerAdapter`가 `Controller` 인터페이스를 지원하므로 대상이 된다.

**3. 핸들러 어댑터 실행**
1. 디스패처 서블릿이 조회한 `SimpleControllerHandlerAdapter`를 실행하면서 핸들러 정보도 함꼐 넘겨준다.
2. `SimplerControllerHandlerAdapter`는 핸들러인 `OldController`를 내부에서 실행하고, 그 결과를 반환한다.

**OldController의 핸들러 매핑, 어댑터**   
HandlerMapping - `BeanNameUrlHandlerMapping`   
HandlerAdapter - `SimpleControllerHandlerAdapter`

<br>

### HttpRequestHandler 실습 (HttpRequestHandlerAdapter 사용해보자) 
핸들러 매핑과 어댑터의 이해를 위해 Controller 인터페이스가 아닌 다른 핸들러를 알아보자   
`HttpRequestHandler`(컨트롤러)는 `서블릿과 가장 유사한 형태`의 핸들러다

<br>

**`MyHttpRequestHandler`의 핸들러 매핑 및 핸들러 어댑터 조회**

**1. 핸들러 매핑으로 핸들러 조회**
1. `HandlerMapping`을 순서대로 실행해서, 핸들러를 찾는다.
2. 이 경우 빈 이름으로 핸들러를 찾아야 하기 때문에 `BeanNameUrlHandlerMapping`가 실행에 성공하고   
   핸들러인 `MyHttpRequestHandler`를 반환한다.

**2. 핸들러 어댑터 조회**
1. `HandlerAdapter`의 `supports()`를 순서대로 호출한다.
2. `HttpRequestHandlerAdapter`가 `HttpRequestHandler` 인터페이스를 지원하므로 대상이 된다.

**3. 핸들러 어댑터 실행**
1. 디스패처 서블릿이 조회한 `HttpRequestHandlerAdapter`를 실행하면서 핸들러 정보도 함께 넘겨준다.
2. `HttpRequestHandlerAdapter`는 핸들러인 `MyHttpRequestHandler`를 내부에서 실행하고 결과를 반환한다.

**MyHttpRequestHandler의 핸들러 매핑, 어댑터**   
HandlerMapping - `BeanNameUrlHandlerMapping`   
HandlerAdapter - `HttpRequestHandlerAdapter`

<br>

**@RequestMapping**
가장 우선순위가 높은 핸들러 매핑과 핸들러 어댑터이다.   
(`RequestMappingHandlerMapping`, `RequestMappingHandlerAdapter`)   
`@RequestMapping`의 앞글자를 따서 만든 이름, 스프링에서 주로 사용하는 애노테이션 기반의 컨트롤러를 지원하는   
매핑과 어댑터이다. 거의 대부분 이것을 사용


## [뷰 리졸버]
기존 OldController.java에서 return 값을 `return new ModelAndView("new-form");`으로 설정하고 url에서 해당 핸들러(컨트롤러)
의 url을 입력하면 아직 뷰 리졸버가 없으므로 오류가 발생한다. 하지만 콘솔에 컨트롤러가 호출됐다는 것을 알 수 있다.

이후 `application.properties`에서 아래의 뷰 리졸버 설정을 추가하고 다시 실행하면 정상작동한다.
어떻게 동작할 수 있을까?
```
spring.mvc.view.prefix=/WEB-INF/views/
spring.mvc.view.suffix=.jsp
```

**뷰 리졸버 - InternalResourceViewResolver**   
스프링 부트는 `InternalResourceViewResolver`라는 뷰 리졸버를 자동으로 등록하는데 `application.properties`에 등록한
prefix, suffix 설정 정보를 사용해서 등록하게 된다.   
(전체 경로를 ModelAndView 객체에 담고 반환하면 동작하지만 권장하지 않는 방법이다.)

<br>

### **뷰 리졸버의 동작 방식**

**스프링 부트가 자동 등록하는 뷰 리졸버**
```
1 = BeanNameViewResolver            : 빈 이름으로 뷰를 찾아서 반환함 (액셀 파일 생성 등에서 사용됨)
2 = InternalResourceViewResolver   : JSP를 처리할 수 있는 뷰를 반환한다.(InternalResourceView)
```
**1. 핸들러 어댑터 호출**   
핸들러 어댑터를 통해 `new-form`이라는 논리적 뷰 이름 받음

**2. ViewResolver 호출**
* 논리적 뷰 이름으로 `viewResolver`를 순서대로 호출함
* 빈 이름으로 뷰를 반환하지 않았으므로 2순위의 `InternalResourceViewResolver`가 호출

**3. InternalResourceViewResolver**   
InternalResourceViewResolver를 반환함   
(뷰가 인터페이스화 되어있으므로 JSP 포워드의 기능을 하는 view를 반환 (MyView와 비슷))

**4. 뷰 - InternalResourceView**   
`InternalResourceView`는 JSP처럼 포워드 `forward()`를 호출해서 처리할 수 있는 경우에 사용된다.

**5. view.render()**   
view.render()가 호출되면 뷰는 forward()를 사용해 new-form.jsp를 실행한다.

> **참고**   
> 다른 뷰는 실제 뷰를 렌더링 하지만, JSP의 경우 forward()를 통해 해당 JSP로 이동해야 렌더링이 된다.   
> JSP제외 다른 뷰 템플릿은 포워드 과정없이 바로 렌더링 된다.
> 
> Thymeleaf 뷰 템플릿을 사용하면 `ThymeleafViewResolver`를 등록해야 한다. 최근에는 라이브러리만 추가하면
> 스프링 부트가 알아서 처리해줌

<br>

## [스프링 MVC - 시작하기]
@RequestMapping이 스프링 MVC에서 등장하면서 MVC의 핵심이 되었다.
`@RequestMapping`을 사용하려면 먼저 핸들러를 조회해 매핑정보를 찾고, 이후 해당 매핑에 맞는 어댑터를 찾아서
해당 핸들러를 지원하는지 확인하고 실제 핸들러를 실행하게 된다.
* `RequestMappingHandlerMapping`
* `RequestMappingHandlerAdapter`

위 두개의 핸들러 매핑과 핸들러 어댑터는 가장 많이 사용되고, 가장 우선순위가 높다.   
이는 애노테이션 기반의 컨트롤러를 지원하는 핸들러 매핑과 어댑터이다.

지금까지 만든 프레임워크의 컨트롤러를 `@RequestMapping`기반의 스프링 MVC 컨트롤러로 변경해보자

### **SpringMember...ControllerV1**
* `@Controller`
  * 스프링이 자동으로 스프링 빈으로 등록한다.(내부에 @Component존재 따라서 컴포넌트 스캔 대상)
  * 스프링 MVC에서 애노테이션 기반 컨트롤러로 인식한다.
  * 2가지 일을함 컴포넌트 스캔의 대상, `RequestMappingHandlerMapping`에서 사용됨
* `@RequestMapping`
  * 요청 정보를 매핑함, 해당 URL이 호출되면 이것이 붙은 메서드 호출됨, 애노테이션 기반 동작으로, 메서드의
    이름은 임의로 지으면 됨

> `RequestMappingHandlerMapping`이 내가 인식할 수 있는 핸들러인지 찾는 방법은 스프링 빈 중에서 `@RequestMapping`
> 혹은 `@Controller`가 클래스 레벨에 붙어있는 경우에 매핑 정보로 인식한다.   

* 기존의 @Controller를 -> @Component, @RequestMapping으로 클래스 레벨에 붙여주면 동일하게 동작한다. 
  이유는 아래와 같다.

```java
실제 RequestMappingHandlerMapping의 isHandler() 메소드를 살펴보면 @Controller 혹은 @RequestMapping 애노테이션이
클래스 레벨에 있으면 해당 매핑 클래스가 처리할 수 있는 핸들러로 인식된다.
	/**
	 * {@inheritDoc}
	 * <p>Expects a handler to have either a type-level @{@link Controller}
	 * annotation or a type-level @{@link RequestMapping} annotation.
	 */
	@Override
	protected boolean isHandler(Class<?> beanType) {
		return (AnnotatedElementUtils.hasAnnotation(beanType, Controller.class) ||
				AnnotatedElementUtils.hasAnnotation(beanType, RequestMapping.class));
	}

```
* 또는 컴포넌트 스캔 없이 스프링 빈으로 직접 등록해도 동작한다.   
  하지만 굳이 불편하게 사용하기 보단 @Controller만 사용하는것이 깔끔하다.
```java
    @Bean
    SpringMemberFormControllerV1 springMemberFormControllerV1() {
        return new SpringMemberFormControllerV1();
}
```

**SpringMember...ControllerV1**에서 `mv.addObject("member", member)`는 스프링이 제공하는 `ModelAndView` 클래스를 이용해
Model 데이터를 추가할 때 사용된다. 이후 뷰를 렌더링시 활용된다.


## [스프링 MVC - 컨트롤러 통합]
`@RequestMapping`은 클래스 단위가 아닌 메서드 단위로 적용이 된다. 따라서 컨트롤러 클래스를 유연하게 하나로 통합할 수 있다.
다만 어느정도 연관성이 있는 컨트롤러끼리 묶어주자.

**URL의 조합**   
컨트롤러 클래스를 통합하는것을 더해 조합을 할 수 도 있음   
클래스 레벨에서 중복되는 url을 `@RequestMapping("...")`하고, 논리적 URL만 메소드에 `@RequestMapping("...")`한다.
리스트를 조회하는 members()의 경우 클래스에 매핑된 url이 곧 메소드의 url과 동일하다
이럴때는 메소드에 `@RequestMapping`만 달아주자.
* `클래스 레벨`: @RequestMapping("/springmvc/v2/members")
  * `메서드 레벨`: @RequestMapping("/new-form") /springmvc/v2/members/new-form
  * `메서드 레벨`: @RequestMapping("/save") /springmvc/v2/members/save
  * `메서드 레벨`: @RequestMapping /springmvc/v2/members
  
**정리**   
다음 시간에는 항상 ModelAndView를 반환해야 하는 컨트롤러들을 뷰의 이름만 반환하는
좀 더 실용적인 컨트롤러(가장 많이 사용함)로 변경해보자(V4)

<br>

## [스프링 MVC - 실용적인 방식]
커스텀 MVC 프레임워크에서 v3 방식은 ModelView의 직접 반환이 불편했었다. 이후 v4를 통해 실용적으로 개선하기도 했다.

기존 SpringMemberControllerV2 방식을 v3 방식처럼 개선해보자

### **SpringMemberControllerV3**   
스프링 애노테이션 기반의 컨트롤러는 ModelAndView를 반환해도 되고, 문자(논리주소)를 반환해도 된다.

**매개변수를 통해 Model 받기**   
* `save()`, `members()`에서 Model을 파라미터로 받는것을 알 수 있다.

**@RequestParam 사용**   
* 스프링 MVC에서 요청 파라미터를 `@RequestParam`으로 받아옴   
* `@RequestParam("username") == request.getParameter("username");`과 거의 같다고 생각하면 된다.   
* 어노테이션을 이용하면 자동으로 해당 파라미터를 형변환하여 전달 해줄 수 있다.   
* `Get 쿼리 파라미터`, `POST Form`방식 모두 지원(**둘 다 요청 파라미터**)

**@RequestMapping -> @GetMapping, @PostMapping(애노테이션을 조합한 애노테이션)**
* @RequestMapping은 URL 매칭 + HTTP Method의 구분까지 해줄 수 있다.   
* `@RequestMapping(value = "/new-form", method = RequestMethod.GET)` 하지만 너무 길다는 불편함으로 아래와 같이 사용 가능하다.
* @GetMapping, @PostMapping (외에도 Put, Delete, Patch 전용 애노테이션 존재)
이들은 내부에 @RequestMapping을 가지고 있어서 동일한 동작을 하지만 HTTP Method의 구분을 하는 추가 기능까지 존재

<br>

## [정리]
**핵심**   
스프링 MVC에서 HTTP 요청이 왔을때 MVC 프레임워크가 어떻게 동작했는지 라이프사이클 잘 알기   
HandlerMapping, HandlerAdapter는 RequestMapping 방식이 거의 표준

**뷰 리졸버**   
뷰리졸버는 application.properties 에서 설정 (JSP는 InternalResourceViewResolver)

**핸들러 매핑, 어댑터**   
핸들러 매핑은 @RequestMapping을 매핑하는 RequestMappingHandlerMapping 클래스가 등장하면서 메소드 기준으로   
핸들러 매핑 정보를 관리한다.

메서드 단위로 RequestMapping 정보를 구할 수 있으므로, 메서드 단위로 매핑 정보를 관리하는 별도의 객체로 관리된다.
(RequestMappingHandlerMapping, RequestMappingHandlerAdapter 확인해보기)

<br><br>

# <스프링 MVC - 기본 기능>
## [프로젝트 생성]
프로젝트 생성시 Jar를 사용하는 이유(War 대신)   
JSP를 사용하지 않고, Jar를 사용하면 항상 내장 서버(톰캣)을 사용하고 `webapp`경로도 사용하지 않음 즉 내장서버 사용에
최적화 되어 있는 기능이다. War를 사용하면 내장 서버도 사용가능 하지만, 주로 외부 서버에 배포하는 목적용

스프링 부트 Jar 사용시 `/resources/static/`위치에 `index.html` 파일을 두면 Welcome 페이지로 처리해준다.   
(스프링 부트가 지원하는 정적 컨텐츠 위치에 /index.html이 있으면 된다.)

<br>

## 로깅 간단히 알아보기
* 운영 시스템에선 시스템 콘솔에 정보를 출력하지 않고, 별도의 로깅 라이브러리를 이용해 로그를 출력한다.

### **로깅 라이브러리**   
스프링 부트 라이브러리를 사용하면 spring-boot-starter-logging이 포함된다. 이는 다음 로깅 라이브러리를 사용한다.
* SLF4J: 인터페이스
* Logback: SLF4J 구현체

로그 라이브러리는 수 많은 라이브러리가 존재, 그것을 통합해 인터페이스로 제공하는 것이 SLF4J 라이브러리이다.
실무에서는 스프링 부트 기본 제공 라이브러리인 Logback을 대부분 사용

### **로그의 선언**
```java
private Logger log = LoggerFactory.getLogger(getClass());
private static final Logger log = LoggerFactory.getLogger(Xxx.class) 
        
Logger는 초기 생성 이후 변경될 필요가 없고, 유지보수와 가독성을 위해 fianl로 선언
```
* `slf4j`는 롬복이 사용 가능하다.

### **로그 호출**   
`log.info("hello")`   
시스템 콘솔로 직접 출력하는 것 보다 로그를 사용하면 필요한 로그의 레벨만 출력할 수 있고 부가적인 정보도 얻을 수 있음   
하지만 `log.debug("String concat log=" + name)`과 같이 연산이 들어가면 덧셈 연산이 먼저 실행됨으로 이렇게 사용하면 안된다.

### **@RestController**
* `@Controller`의 반환값이 String이면 뷰 이름으로 인식된다. 따라서 **뷰를 찾고 뷰가 렌더링** 된다.
* `@RestController`는 반환 값으로 뷰를 찾는 것이 아니라, **HTTP 메시지 바디**에 바로 입력함
  따라서 실행 결과로 Ok 메시지를 받을 수 있다. @ResponseBody와 관련 있음

### **LogTestController**
* 로그가 출력되는 포멧 확인
  * 시간, 로그 레벨, 프로세스 ID, 쓰레드 명, 클래스명, 로그 메시지
* 로그 레벨 설정을 변경해서 출력 결과를 보자.
  * LEVEL: `TRACE > DEBUG > INFO > WARN > ERROR`
  * 개발 서버는 debug 출력
  * 운영 서버는 info 출력
* @Slf4j로 변경

### **로그 레벨 설정**
`application.properties`에서 설정
```
# 전체 로그 레벨 설정 (default: info)
logging.level.root=info

@ hello.springmvc 패키지와 그 하위 로그 레벨 설정
logging.level.hello.springmvc=debug
```

### 올바른 로그 사용법
* `log.debug("data= + data)`
  * 로그 출력 레벨을 info로 설정해도 해당 코드에 있는 문자열 덧셈 연산이 실행되어 사용하지 않는 로그에서의
    리소스가 사용된다.
* `log.debug("data={}", data)`
  * 로그 출력 레벨을 info로 설정하면 아무일도 일어나지 않음, 따라서 의미없는 연산으로 리소스가 낭비되지 않는다.


### 로그 사용의 장점
* 쓰레드 정보, 클래스 이름 같은 부가 정보 함께 볼 수 있고, 출력 모양을 조정 할 수 있음
* 로그 레벨에 따라 개발 서버에서는 모든 로그를 출력하고, 운영서버에서는 출력하지 않는 등 로그를 상황에 맞게 조절 가능
* 콘솔에만 출력하는 것이 아니라, 파일이나 네트워크 등, 로그를 별도의 위치에 남길 수 있다. 특히 파일로 남길때는
  일별, 특정 용량에 따라 로그를 분할하는 것도 가능
* 성능도 일반 System.out보다 좋다. (내부 버퍼링, 멀티 쓰레드 등) 따라서 실무에선 꼭 사용함

<br>

## [요청 매핑]

**매핑 정보!**
* `@RequestController`
  * `@Controller`는 반환 값이 String이면 뷰 이름으로 인식, 따라서 뷰를 찾고 렌더링
  * `@RestController`는 반환 값으로 뷰가 아닌 HTTP 메시지 바디에 직접 입력 (@ResponseBody관련)
* `@RequestMapping("hello-basic")`
  * `/hello-basic` URL 호출이 오면 해당 메서드가 실행되도록 매핑
  * 대부분의 속성을 `배열`로 제공하므로 다중 설정 가능 -> `{"/hello-basic", "/hello-go"}`

<br>

**둘 다 허용**   
아래의 두 가지 요청은 다른 URL임, 하지만 스프링은 같은 요청으로 매핑
* 매핑: `/hello-basic`
* URL 요청: `/hello-basic`, `/hello-basic/`

<br>

**HTTP 메서드**   
@RequestMapping에 method속성으로 HTTP 메서드를 지정하지 않으면 HTTP 메서드와 무관하게 호출됨   
> 모두허용 GET, HEAD, POST, PUT, PATCH, DELETE   
> @RestController에서 method를 지정하고 해당 메소드가 아닌 다른 메소드로 호출하면 JSON타입으로 오류가 반환됨
> --> @RestController의 특징

<br>

**HTTP 메서드 축약**
* 편리한 축약 애노테이션
* @GetMapping
* @PostMapping
* @PutMapping
* @DeleteMapping
* @PatchMapping

<br>

### **PathVariable(경로 변수) 사용**
URL자체에 값이 들어있다.   
최근 HTTP API는 다음과 같이 리소스 경로에 식별자를 넣는 스타일을 선호
* /mapping/userA
* /users/1
* @RequestMapping 은 URL 경로를 템플릿화 할 수 있다. (`@GetMapping("/mapping/{userId}")`)   
  @PathVariable을 사용하면 매칭되는 부분을 편리하게 조회 가능
* @PathVariable의 이름과 파라미터 이름이 동일하면 생략가능
  * `public String mappingPath(@PathVariable("userId") String data)` 생략 전
  * `public String mappingPath(@PathVariable String userId)` 생략 후

<br>

**PathVariable 다중 사용**
```java
@GetMapping("/mapping/users/{userId}/orders/{orderId}")
public String mappingPath(@PathVariable String userId, @PathVariable Long orderId)
```

<br>

**특정 파라미터 조건 매핑**
```java
/*
* 파라미터로 추가 매핑
* params="mode",
* params="!mode"
* params="mode=debug"
* params="mode!=debug" (! = )
* params = {"mode=debug","data=good"}
* /
@GetMapping(value = "/mapping-param", params = "mode=debug")
```
> 파라미터로 조건을 넣은 값이 있어야 요청이 수행된다.

<br>

**특정 헤더로 조건 매핑**
```java
/**
* 특정 헤더로 추가 매핑
* headers="mode",
* headers="!mode"
* headers="mode=debug"
* headers="mode!=debug" (! = )
* /
@GetMapping(value = "/mapping-header", headers = "mode=debug")
```
> 특정 헤더에 조건을 지정한 헤더 값이 있어야 호출된다.(HTTP 헤더 사용)

<br>

**미디어 타입 조건 매핑-HTTP 요청 Content-Type, consume**   
```java
/**
* Content-Type 헤더 기반 추가 매핑 Media Type
* consumes="application/json"
* consumes="!application/json"
* consumes="application/*"
* consumes="*\/*"
* MediaType.APPLICATION_JSON_VALUE
  */
  @PostMapping(value = "/mapping-consume", consumes = MediaType.APPLICATION_JSON_VALUE)
  ```
이 방법은 특정 헤더로 조건 매핑에서 Content-Type을 작성하고 직접 넣어줘도 된다.   
하지만 이걸 사용하면 몇가지 부가적인 기능이 따라온다.   
Content-Type에 따라 서로 다른 것을 호출할때 분류할 수 있다. 이 경우 headers가 아닌 `consume`을 사용하자 (스프링 내부 처리 지원)
* `consume의 의미`: 컨트롤러 입장에선 요청의 Content-Type 정보를 소비하는 입장이므로 consume이라 함

<br>

**미디어 타입 조건 매핑 - HTTP 요청 Accept(`나는 이런 정보를 받아들일 수 있어`), produce**   
```java
/**
 * Accept 헤더 기반 Media Type
 * produces = "text/html"
 * produces = "!text/html"
 * produces = "text/*"
 * produces = "*\/*"
 */
@PostMapping(value = "/mapping-produce", produces = MediaType.TEXT_HTML_VALUE)
```
produce: 컨트롤러가 생산해내는 Content-Type이 이것이어야함    
HTTP 요청의 Accept 헤더를 기반으로 미디어 타입으로 매핑한다. 만약 맞지 않으면 HTTP 406 상태코드(Not Acceptable) 반환


### **Accept, Content-Type 정리**
**Accept**는 클라이언트가 선호하는 표현을 요청하는 것으로 클라이언트 입장에서 응답을 받을 때,
Accept헤더에 있는 데이터 타입이 오지 않으면 내가 말한 데이터가 아니잖아라고 거절을 하는 것이고,

**Content-Type**은 해당 헤더에 있는 데이터가 오지 않았을 경우 
서버 입장에서 내가 처리할 수 있는 데이터가 아닌데? 라고 거절을 하는 것

<br>

## [요청 매핑 - API 예시]
회원관리를 HTTP API로 만든다 생각하고 매핑을 어떻게 하는지 알아보자
(URL 매핑만)

**회원 관리 API**
* 회원 목록 조회: GET `/users`   
* 회원 등록: POST     `/users`
* 회원 조회: GET      `/users/{userId}`
* 회원 수정: PATCH    `/users/{userId}`
* 회원 삭제: DELETE   `/users/{userId}`

매핑 방법을 봤으니, HTTP 요청이 보내는 데이터들을 스프링 MVC로 어떻게 조회할까?

<br>

## [HTTP요청 - 기본, 헤더 조회]
애노테이션 기반의 스프링 컨트롤러는 다양한 파라미터를 지원하는데, HTTP 헤더 정보를 조회하는 방법을 알아보자

**RequestHeaderController**   
* HttpServletRequest, Response 조회
* HttpMethod 조회
* Locale 조회
* @RequestHeader MultiValueMap<String, String> headerMap
  * 모든 HTTP 헤더를 MultiValueMap 형식으로 조회
* @RequestHeader("host") String host
  * 특정 HTTP 헤더를 조회
  * 속성
    * 필수 값 여부: required
    * 기본 값 속성: defaultValue
* @CookieValue(value = "myCookie", required = false) String cookie
  * 특정 쿠키 조회
  * 속성
    * 필수 값 여부: required
    * 기본 값 속성: defaultValue


> MultiValueMap은 Map과 유사하지만, 하나의 키에 여러 값을 받을 수 있다.   
> HTTP header, HTTP 쿼리 파라미터와 같이 하나의 키에 여러 값을 받을 때 사용한다.   
> 내부에 List타입으로 value가 여러개 저장됨

**@Slf4j**
다음 코드를 자동으로 생성해서 로그를 선언해준다.
`private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RequestHeaderController.class);`

**참고**   
@Controller의 사용 가능한 파라미터 목록 메뉴   
https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-arguments

@Controller의 사용 가능한 응답 값 목록(리턴)   
https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-return-types

<br>

## [HTTP 요청 파라미터 - 쿼리 파라미터, HTML form]

HTTP 요청 메시지를 통해 클라이언트 -> 서버로 데이터를 전달하는 방법은 3가지가 존재
1. GET - 쿼리 파라미터
   * `?key=value&key=value` 형식
   * 메시지 바디 없이, URL에 쿼리 파라미터로 데이터 전달
   * 검색, 필터, 페이징 등에서 사용
2. POST - HTML Form
   * content-type: application/x-www-form-urlencoded
   * 메시지 바디에 쿼리 파라미터 형식으로 전달 (형식은 GET과 동일)
   * 회원가입, 상품주문, HTML Form 사용
3. HTTP message body
   * HTTP API에서 주로 사용 JSON, XML, TEXT
   * 주로 JSON이 보편화됨
   * POST, PUT, PATCH

HttpServletRequest를 사용하면 request.getParameter()로 GET과 POST로 받은 요청 파라미터를 조회할 수 있다.   
(같은 쿼리 파라미터 형식을 사용하므로)

GE(쿼리 파라미터), POST(HTML Form) 모두 형식이 같으므로 구분없이 조회 가능   
-> 간단하게 **요청 파라미터(request parameter) 조회** 라고 한다.

단계적으로 코드로 구현해보자

<br>

## [HTTP 요청 파라미터 - @RequestParam]
스프링이 제공하는 @RequestParam을 사용하면 요청 파라미터를 매우 편리하게 사용할 수 있다.   
형식: `@RequestParam(name = "", required = boolean, defaultValue = "")`   

### **requestParamV2**   
```java
    public String requestParamV2(
            @RequestParam("username") String memberName,
            @RequestParam("age") int memberAge)
```
`@RequestParam(name = "value") == request.getParameter("value");`

### **requestParamV3**   
```java
    public String requestParamV3(
            @RequestParam String username,
            @RequestParam int age)
```
HTTP 파라미터 이름과 매개변수의 이름이 동일하면 name 속성도 생략 가능하다.
`@RequestParam(name = "value") == request.getParameter("value");`

### **requestParamV4**
```java
    @ResponseBody
    @RequestMapping("/request-param-v4")
    public String requestParamV4(String username, int age)
```
@RequestParam 자체를 생략 할 수도 있다. 다만, 매개변수의 이름과 HTTP 파라미터 이름이 동일해야 하고,    
String, int, Integer 등의 단순 타입이어야 한다.   
애노테이션을 생략하면 required 옵션은 false로 적용된다.

<br>

### **파라미터 필수 여부 - requestParamRequired**   
* @RequestParam의 required 옵션은 default가 true(null 허용하지 않음)
* 만약 username이 true일떄 보내지 않으면 400 Bad Request를 내려준다.
  * 약속한 HTTP 스펙을 지키지 않은것이기 때문에 클라이언트쪽의 잘못이 맞다.

**주의!! - 파라미터 이름만 사용**   
* `/request-param?username=` 혹은 `/request-param?username`같이 작성하면   
  빈문자 ""로 통과되기 때문에 주의해야 한다. (파라미터의 이름만 있고 값이 없음)

**주의!! - Primitive type에 null 입력**   
* `/request-parma` 요청
* `@RequestParam(required = false) int age`인 경우

null을 int에 입력하는 것은 불가능함. (500 예외 발생)
따라서 null을 받을 수 있는 Wrapper class로 변경하거나, defaultValue를 사용해 null값 방지

### 기본 값 적용 - requestParamDefault
```java
@ResponseBody
@RequestMapping("/request-param-default")
public String requestParamDefault(@RequestParam(required = true, defaultValue = "guest") String username,
@RequestParam(required = false, defaultValue = "-1") int age)
```
파라미터에 값이 없는 경우 `defaultValue`이용   
기본값을 설정했으므로 required의 유무는 상관이 없다.   
또한 파라미터 이름만 사용하여 빈문자가 전달되는 경우에도 defaultValue가 적용됨


### 파라미터를 Map으로 조회하기 - requestParamMap
```java
@ResponseBody
@RequestMapping("/request-param-map")
public String requestParamMap(@RequestParam Map<String, Object> paramMap)
```
key의 값은 HTTP 파라미터의 이름과 동일

> 참고로 Map, MultiValueMap으로 둘 다 조회 가능한데, 파라미터의 값이 여러개라면 List 타입으로 받아올 수 있는
> MultiValueMap을 이용할 수 있다. 하지만 파라미터 값은 1개씩 보내는게 바랍직함.

<br>

## [HTTP 요청 파라미터 - @ModelAttribute]
보통 요청 파라미터를 받아서 필요한 객체를 만들고 객체에 값을 넣고 반환하지만 스프링은 이 과정을 자동화 시켜주는
`@ModelAttribute`를 제공해준다.

롬복의 @Data   
@Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor를 자동 적용

### **@ModelAttribute의 역할**
* HelloData 객체 생성
* 요청 파라미터의 이름으로 HelloData 프로퍼티를 찾아서 해당 프로퍼티의 setter를 호출해 파라미터의 값을 입력(바인딩)함

### **프로퍼티**
객체에 getUsername(), setUsername() 메서드가 존재하면 `username`이라는 프로퍼티를 가지고 있는것.   
username 프로퍼티의 값을 변경하면 setter가 조회하면 getter가 호출된다.   
(setXxx(), getXxx()에서 먼저 set, get을 때고 대문자를 소문자로 변환하여 프로퍼티를 만듦)

혹여나 쿼리 파라미터에 해당 자료형과 다른 자료형이 들어오면 BindException이 발생함

@ModelAttribute, @RequestParam모두 생략이 가능하다 그렇다면 어떻게 구분할까?
* String, int, Integer등의 단순 타입 -> @RequestParam
* 나머지 -> @ModelAttribute (argument resolver로 지정해둔 타입 외(HttpServletRequest 등))

<br>

## [HTTP 요청 파라미터 - 단순 텍스트]
* HTTP message body에 데이터를 직접 담아 요청
  * HTTP API에서 주로 사용 (JSON이 거의 표준)
  * POST, PUT, PATCH등의 메소드를 사용

요청 파라미터가 아닌 HTTP 메시지 바디 통해 직접 데이터가 넘어오면(GET, POST Form 제외) @RequestParam, @ModelAttribute를
사용할 수 없다. (HTML Form 형식으로 전달되는 경우는 요청 파라미터로 인정됨 == 쿼리 스트링과 같은 형식)

* 텍스트 메시지를 HTTP 메시지 바디에 담아 전송 후 읽어보자
* InputStream 이용해 읽음

### requestBodyString
HttpServletRequest 를 이용해 ServletInputStream 생성     
바이트 코드를 직접 스트링으로 변환(인코딩 타입 명시 필수)

### requestBodyStringV2
파라미터로 InputStream 객체와 Writer 객체를 받아옴 HttpServletRequest에서 InputStream 변환 공수 덜어줌    
Writer로 바로 HTTP messgae body에 리턴할 수 있음
* 스프링 MVC 파라미터 지원
  * InputStream(Reader): HTTP 요청 메시지 바디의 내용을 직접 조회   
    OutputStream(Writer): HTTP 응답 메시지의 바디에 직접 결과 출력

### requestBodyStringV3
HttpEntity를 파라미터로 받아옴    
* HttpEntity: HTTP header, body 정보 편리하게 조회
  * 메시지 바디 정보 직접 조회(요청 파라미터 조회 기능과는 무관!!)
* HttpEntity는 응답에도 사용 가능 (return 타입 HttpEntity)
  * 메시지 바디에 정보 직접 반환
  * 헤더 정보 포함 가능
  * view 조회 자동으로 X

부가적으로 HttpEntity를 상속받은 RequestEntity, ResponseEntity도 존재
* RequestEntity: HttpMethod, url 정보 추가, 요청에서 사용됨
* ResponseEntity: 응답 시 HTTP 상태 코드 설정 가능
  * `return new ResponseEntity<String>("Hello World", responseHeaders, HttpStatus.CREATED)`

### @RequestBody - requestBodyStringV4(주로 사용)
**@RequestBody**   
HTTP 메시지 바디 정보를 편리하게 조회, 만약 헤더 정보가 필요하다면 HttpEntity, @RequestHeader를 사용   
(메시지 바디 직접 조회 하는 기능은 `@RequestParam`, `@ModelAttribute`와는 전혀 관계 없음!!)

**요청 파라미터 vs HTTP 메시지 바디**
* 요청 파라미터 조회 기능: `@RequestParam`, `@ModelAttribute`
* HTTP 메시지 바디를 직접 조회하는 기능: `@RequestBody`

**@ResponseBody**
응답 결과를 HTTP 메시지 바디에 직접 담아 전달 따라서, view를 사용하지 않음

> **[참고]**스프링 MVC 내부에서 자동으로 메시지 바디를 읽어서 문자나 객체로 변환해서 전달해주는 
> HttpMessageConverter라는 기능을 사용한다. 내부적으로 이런 과정을 거치기 때문에 가능하다.

<br>

## [HTTP 요청 메시지 - JSON]
HTTP API에서 주로 사용하는 JSON 데이터 형식 조회   

### requestBodyJsonV1
가장 기본적으로 조회하는 방법은 HttpServletRequest를 사용해 직접 HTTP 메시지 바디에서 데이터를 읽고,   
Jackson 라이브러리인 ObjectMapper를 이용하는 방법

### requestBodyJsonV2
`@RequestBody`를 사용해 HTTP 메시지에서 데이터를 꺼내고 messageBody에 저장함 (매개변수)   
이 또한 ObjectMapper를 통해 문자로된 JSON을 파싱하여 자바 객체로 변환한다.

### requestBodyJsonV3
`HttpEntity`, `@RequestBody`는 HttpMessageConverter가 HTTP 메시지 바디의 내용을 문자 혹은 객체로 변환해줌   
V2에서와 같이 문자 뿐만 아니라 JSON을 객체로 변환하여 넘겨주기도 함 따라서 매개변수로 객체를 직접 받을 수 있다.

다만, **@RequestBody는 생략할 수 없다.** 요청 파라미터에서 @ModelAttribute는 int, Integer, String등 단순 타입이 아니라면
모두 @ModelAttribute가 적용되므로 요청 파라미터를 처리하게 된다.    
따라서 **명시적으로 @RequestBody를 작성해주어야 HTTP 메시지 바디로 동작한다.**

> 혹여나 Postman을 이용할 때 Content-type이 application/json이 아니라면 JSON을 처리하는 HTTP 메시지 컨버터가   
> 동작하지 않으니 꼭 주의하자!

### requestBodyJsonV4
`HttpEntity`를 이용해 객체를 받아올 수 도 있다. 이 역시 HTTP 메시지 컨버터가 내부에서 처리해준다.

### requestBodyJsonV5
`@ResponseBody`는 반환타입으로 String 뿐만 아니라 객체로 지정할 수 있다.   
이때 HttpMessageConverter가 다시 그 역할을 맡아서 한다.    
결국 메시지 컨버터는 요청을 받을때도 이용되지만 응답을 할때도 이용할 수 있다. **단, @ResponseBody가 있어야한다.**   
메시지 컨버터는 객체를 JSON으로 변환시켜 반환한다.
> Accept: application/json 인지 확인이 필요함

* `@RequestBody` 요청
  * JSON 요청 -> HTTP 메시지 컨버터 -> 객체
* `@ResponseBody` 응답
  * 객체 -> HTTP 메시지 컨버터 -> JSON 응답

### requestBodyJsonV6
V5에서 반환타입이 객체가 아니라 HttpEntity를 사용해도 메시지 컨버터가 동작하여 JSON으로 응답하는걸 확인할 수 있다.

<br>

## [HTTP 응답 - 정적 리소스, 뷰 템플릿]
스프링은 크게 3가지의 응답 데이터를 만드는 방법이 존재

* 정적 리소스
  * 웹 브라우저에 정적인 HTML, css, js를 제공할 때 사용
* 뷰 템플릿 사용
  * 웹 브라우저에 동적인 HTML을 제공할 떄 사용(SSR)
* HTTP 메시지 사용
  * HTTP API를 제공하는 경우에 데이터를 전달해야 하므로 JSON과 같은 타입을 HTTP 메시지 바디에 실어 응답

### 정적 리소스
스프링 부트는 클래스패스의 다음 디렉토리에 있는 정적 리소스를 제공함   
`/static`, `/public/`, `/resources`, `/META-INF/resources`   

`src/main/resources`는 리소스를 보관하는 곳, 클래스패스의 시작경로임   
아래 경로에 리소스를 넣으면 스프링 부트가 정적 리소스로 서비스 제공해줌

**정적 리소스:** `src/main/resources/static`    
(해당 파일의 변경없이 그대로 서비스, 위의 4가지 경로 중 아무거나 지정하면 해당 디렉토리로 사용됨)


### 뷰 템플릿
뷰 템플릿을 거쳐서 HTML이 생성되고 뷰가 응답을 만들어 전달   
HTML 동적 생성 용도 외 다른 뷰 템플릿이 만들 수 있는것이라면 뭐든지 가능하다.

**기본 뷰 템플릿 경로**: `src/main/resources/templates`   

**responseViewV1: ModelAndView를 반환 하는 경우**   
* 뷰의 논리적 이름, 뷰에 필요한 데이터(모델)을 설정하고 ModelAndView 자체를 반환

**responseViewV2: String을 반환하는 경우 - view or HTTP 메시지**   
* @ResponseBody 없으면 String값은 `논리적 뷰`로 인식되어 뷰 리졸버 실행되고 뷰 찾음 (view)   
* @ResponseBody 있으면 String값은 `HTTP 메시지 바디에 직접 입력`됨

**responseViewV3: void 타입인 경우**
* `@Controller`를 사용하고, HttpServletResponse, OutputStream(Writer)같은 HTTP 메시지 바디를 처리하는
  파라미터가 없으면 `요청 URL을 논리적 뷰 이름`으로 사용한다.
* **명시성이 떨어지고, 이렇게 맞는 경우가 드물어서 권장하지 않음**


### Thymeleaf 스프링 부트 설정   
초기 DI를 했으므로 자동 추가 되어있으므로 스프링 부트가 ThymeleafViewResolver등 필요 빈들 자동등록 됨   
또한 viewResolver의 prefix, postfix도 초기값으로 세팅 되어있다.

**스프링 부트 타임리프 관련 추가설정 공식 사이트**   
https://docs.spring.io/spring-boot/docs/2.4.3/reference/html/appendix-application-properties.html#common-application-properties-templating

<br>

## [HTTP 응답 - HTTP API, 메시지 바디에 직접 입력]
HTTP API를 제공하는 경우, HTML이 아니라 데이터를 전달해야 함 (TEXT, XML, JSON(주로사용))   
> HTML, 뷰 템플릿은 HTTP 응답 메시지 바디에 HTML 데이터가 담겨 나가고   
> 이번시간에 알아볼 것은 HTML 데이터가 아닌 직접 HTTP 응답 메시지를 전달하는 경우

**responseBodyV1**   
HttpServletResponse 객체를 직접 받아와 writer를 통해 HTTP 메시지 바디에 데이터 입력

**responseBodyV2**   
HttpEntity(HTTP 메시지 헤더, 바디 정보 가지고있음)를 구현한 ResponseEntity를 이용해 
메시지와 `HTTP 응답 코드를 추가로 설정`할 수 있음

**responseBodyV3**   
@ResponseBody를 이용해 view를 사용하지 않고, `HTTP 메시지 컨버터를 통해 HTTP 메시지를 직접 입력`할 수 있음   
(ResponseEntity도 동일한 방식으로 동작)

**responseBodyJsonV1**   
ResponseEntity를 반환한다. `HTTP 메시지 컨버터를 통해 JSON 형식으로 변환`되어 반환됨

**responseBodyJsonV2**
@ResponseBody를 이용해 객체를 반환하면 `HTTP 메시지 컨버터가 JSON 형식으로 변환하여 반환`함   
`HTTP 응답 코드 설정을 위해 @ResponseStatus(HttpStatus.OK)애노테이션`을 사용하여 응답 코드 설정   
(애노테이션이므로 정적임, 상황에 따라 동적으로 코드가 변환되어야 하면 ResponseEntity를 이용하자)

**@RestController**
@Controller + @ResponseBody의 역할을 해줌, `클래스 레벨에 붙이면 해당 컨트롤러 모두 @ResponseBody가 적용`된다.   
즉, HTTP 메시지 바디에 직접 데이터 입력   
@Controller와 @ResponseBody를 클래스레벨에 붙여도 동일한 적용 효과를 얻을 수 있다.   
@RestController는 REST API(HTTP API)를 만들때 사용하는 컨트롤러이다.

<br>

## [HTTP 메시지 컨버터]
* HttpMessageConverter가 적용되는 상황
  * HTTP 요청: @RequestBody, HttpEntity(or RequestEntity)
  * HTTP 응답: @ResponseBody, HttpEntity(or ResponseEntity)

* HttpMessageConverter 인터페이스의 메소드
  * canRead(): HTTP 요청시 해당 메시지 컨버터가 HTTP 바디 메시지를 읽을 수 있는지 확인(클래스 타입(매개인자)와 content-type)
  * canWrite(): HTTP 응답시 해당 메시지 컨버터가 HTTP 바디 메시지를 쓸 수 있는지 확인(반환의 클래스 타입과, Accept 미디어 타입)
  * read(): canRead() 조건 만족하면 호출, 해당 매개인자에 맞게 객체를 생성하고 반환함
  * write(): canWrite() 조건 만족하면 호출, HTTP 응답 메시지 바디에 데이터를 Accept 미디어 타입에 맞춰서 생성

> canRead(), canWrite()는 메시지 컨버터가 해당 클래스, 미디어 타입을 지원하는지 체크하고, read(), write()는 메시지 컨버터는 통해 메시지를 읽고 쓰는 기능을 한다.

HTTP 메시지 컨버터는 HTTP 요청, 응답 둘 다 사용됨   
항상 대상 클래스 타입과, 미디어 타입을 이용해 어떤 HttpMessageConverter의 구현체를 사용할지 결정

### **대표적 스프링 기본 메시지 컨버터 구현체**
* 0 = ByteArrayHttpMessageConverter
* 1 = StringHttpMessageConverter
* 2 = MappingJackson2HttpMessageConverter

두 가지 조건을 체크하면서 만족하지 않으면 다음 우선순위의 메시지 컨버터로 넘어간다.

<br>

### **각 메시지 컨버터 동작 개요**   
* `ByteArrayHttpMessageConverter`: `byte[]` 데이터 처리
  * 클래스 타입: byte[], 미디어타입: \*/\*
  * 요청 예) @RequestBody byte[] data
  * 응답 예) @ResponseBody return byte[] 쓰기 미디어 타입 application/octet-stream
* `StringHttpMessageConverter` : String 문자로 데이터를 처리한다. 
  * 클래스 타입: String , 미디어타입: \*/\*
  * 요청 예) @RequestBody String data
  * 응답 예) @ResponseBody return "ok" 쓰기 미디어타입 text/plain
* `MappingJackson2HttpMessageConverter` : application/json
  * 클래스 타입: 객체 또는 HashMap , 미디어타입 application/json 관련
  * 요청 예) @RequestBody HelloData data
  * 응답 예) @ResponseBody return helloData 쓰기 미디어타입 application/json 관련

<br>

### **실제 요청 예시**

```
content-type: application/json

@RequestMapping
void hello(@RequestBody String data) {...}
```
* 매개인자가 `String` 이고, `application/json`은 \*/\*에 포함되므로 `StringHttpMessageConverter` 선택

```
content-type: application/json

@RequestMapping
void hello(@RequestBody HelloData data) {...}
```
* 매개인자가 `객체`이고 `application/json` 타입 이므로 `MappingJackson2HttpMessageConverter` 선택

```
content-type: text/html

@RequestMapping
void hello(@RequestBody HelloData data) {...}
```
* 매개인자가 `객체`이고 text/html을 만족하는 메시지 컨버터는 존재하지 않으므로 오류 반환

<br>

### **HTTP 요청 및 응답시에 메시지 컨버터의 처리 과정**

**HTTP 요청 데이터 읽기**
* HTTP 요청 옴, (컨트롤러는 @RequestBody, HttpEntity) 파라미터 사용
* 메시지 컨버터 동작, 메시지 읽을 수 있는지 확인 위해 `canRead()` 호출
  1. 대상 클래스 타입 지원?
  2. HTTP 요청의 `Content-Type` 미디어 타입을 지원?
* `canRead()` 만족하면 `read()` 호출해 객체 생성 및 반환

**HTTP 응답 데이터 생성**
* 컨트롤러에서 `@ResponseBody`, `HttpEntity`로 값이 반환
* 메시지 컨버터가 메시지를 쓸 수 있는지 확인하기 위해 `canWrite()` 호출
  1. 대상 클래스 타입 지원?
  2. HTTP 요청의 Accept 미디어 타입 지원?   
     (정확히는 `@RequestMapping`, `produces` 옵션)
* `canWrite()` 조건을 만족하면 `write()`를 호출해 HTTP 응답 메시지 바디에 데이터 생성

<br>

## [요청 매핑 핸들러 어댑터 구조]
RequestMappingHandlerAdapter의 동작 방식
1. 디스패처 서블릿이 RequestMappingHandlerAdapter 호출
2. `RequestMappingHandlerAdapter`가 ArgumentResolver 호출
3. `ArgumentResolver`는 알맞은 매개변수 객체 생성후 반환
4. 핸들러 실행 후 `ReturnValueHandler` 호출
5. ReturnValueHandler이 컨트롤러의 반환 값을 생성 후 반환

### **ArgumentResolver**   
* 애노테이션 기반 컨트롤러는 다양한 파라미터를 사용할 수 있다. 이렇게 다양한 파라미터들을 유연하게 처리할 수 있게 도와주는 녀석이 `ArgumentResolver`임   

* ArgumentResolver는 컨트롤러가 필요로 하는 다양한 파라미터의 값(객체)들을 생성하고 준비가 완료되면 컨트롤러를 호출하며 값을 넘겨준다.

**`ArgumentResolver`동작 방식**   
`supportsParameter()`를 호출해 해당 파라미터의 지원여부 체크   
지원하는 파라미터라면 `resolveArgument()` 호출해 실제 객체 생성   
이후 실제 컨트롤러 호출하며 넘겨줌(약 30개가 넘는 ArgumentResolver 지원)

<br>

### **ReturnValueHandler**
``HandlerMethodReturnValueHandler``를 줄여 말함   
요청 값의 파라미터가 아닌 응답 값을 변환하고 처리함   
(컨트롤러에서 String으로 뷰 이름을 반환해도 동작하는 이유이다.)
약 10여개가 넘는 ReturnValueHandler를 지원해준다.

> ArgumentResolver 가능 파라미터 목록   
> https://docs.spring.io/spring-framework/docs/current/reference/html/web.html%23mvc-ann-arguments   
> ReturnValueHandler 가능 응답 값 목록   
> https://docs.spring.io/spring-framework/docs/current/reference/html/web.html%23mvc-ann-return-types    

<br>

### HTTP 메시지 컨버터는 어디?
HTTP 메시지 컨버터를 사용하는 `@RequestBody`, `HttpEntity`모두 컨트롤러가 필요로 하는 파라미터의 값에 사용된다.   
반대로 `@ResponseBody`, `HttpEntity`도 컨트롤러의 반환 값을 이용함

**요청의 경우**   
@RequestBody, HttpEntity를 처리하는 `ArgumentResolver`가 호출되면 이 리졸버는 내부적으로 HTTP 메시지 컨버터를 호출해 필요한 객체를 받아오고, 컨트롤러를 실행하며 인자를 넘겨준다.

**응답의 경우**
@ResponseBody, HttpEntity를 처리하는 `ReturnValueHandler`가 호출되면 이 리졸버는 내부적으로 HTTP 메시지 컨버터를 호출해 필요한 객체를 받아오고 해당 객체를 리턴값으로 반환한다.

> 스프링 MVCsms @RequestBody @ResponseBody가 존재하면 `RequestResponseBodyMethodProcessor`(ArgumentResolver)호출   
> 만약 HttpEntity가 존재하면 HttpEntityMethodProcessor(ArgumentResolver)를 사용한다.

<br>

### 확장
스프링은 HandlerMethodArgumentResolver, HandlerMethodReturnValueHandler, HttpMessageConverter를 모두 인터페이스로 제공하므로 기능을 확장할 수 있음

기능 확장은 WebMvcConfigurer를 상속받고 스프링 빈으로 등록해 원하는 부분을 구현하면 된다.

<br><br>

# <스프링 MVC - 웹 페이지 만들기>
## [요구사항 분석]
상품을 관리할 수 있는 서비스

### **상품 도메인 모델**
* 상품 ID, 상품명, 가격, 수량

### **상품 관리 기능**
* 상품 목록, 상품 상세, 상품 등록, 상품 수정, `상품 삭제(직접 만들어 보자)`

  
**상품 등록 과정**     
`클라이언트 -> 상품 목록(컨트롤러, HTML폼) -> 상품 등록 폼(컨트롤러, HTML 폼) -> 상품 저장(컨트롤러) -내부 호출> 상품 상세(컨트롤러, HTML 폼)`

**상품 상세**   
`클라이언트 -> 상품 목록 -> 상품 상세`

**상품 수정**   
`클라이언트 -> 상품 목록 -> 상품 상세 -> 상품 수정 폼(컨트롤러, HTML 폼) -> 상품 수정(컨트롤러) -리다이렉트> 상품 상세 다시 조회`

<br>

## [상품 도메인 개발]
예제 코드 잘 살펴보기 (테스트 코드도)

<br>

## [상품 서비스 HTML]
CSS는 모두 bootstrap 이용

정적 리소스가 공개되는 /resources/static 폴더에 HTML을 넣어두면, 실제 서비스에서도 공개됨, 따라서
실 서비스를 운영한다면 공개할 필요없는 HTML같은 정적 리소스 파일을 두는것은 주의해야 한다.

<br>

## [상품 목록 - 타임리프]
타임리프는 natural template이라고도 함 왜 그럴까?

### **타임리프 사용 선언**
`<html xmlns:th="http://www.thymeleaf.org">`

### **속성 변경 - th:href**
`th:href="@{/css/bootstrap.min.css}"`
* href="value1"을 th:href="value2"로 변경
* 타임리프를 거치면 th:xxx값으로 변경, 값이 없다면 새로 생성한다.
* 정적인 HTML은 기본 href 속성이 사용됨 (뷰 템플릿 거치면 동적으로 변경)
* 대부분의 HTML 속성 `th:xxx`형식으로 변경 가능

### **타임리프 핵심**
* `th:xxx`가 붙은 부분은 SSR이므로 기존것을 대체, 이게 없다면 기존 html의 속성을 이용
* 정적인 파일을 직접 열어도 th:xxx는 브라우저가 모르므로 무시됨
* HTML 보기와, 동적인 템플릿도 동시에 볼 수 있음 (**타임리프의 가장 큰 장점**)

### **URL 링크 표현식 - @{...}**
`th:href="@{/css/bootstrap.min.css}"`
* `@{...}` 타임리프는 URL 링크를 사용하는 경우 `@{...}`를 사용 이를 URL 링크 표현식이라함
* 이는 서블릿 컨텍스트 자동 포함함

### 상품 등록 폼 이동: 속성 변경 - th:onclick
* onclick="location.href='addForm.html'"
* th:onclick="|location.href=\`@{/basic/items/add}\`|" (**리터럴 대체 문법 사용됨**)

### 리터럴 대체 - |...|
|...| 으로 사용
* 기존 타임리프는 문자와 표현식이 분리되어 있으므로 더하기 연산을 사용해야 하지만, 리터럴 대체 문법을 이용하면
  더하기 없이 편리하게 사용할 수 있다.
  * (기존) `<span th:text="'Welcome to our application, ' + ${user.name} + '!'">`
  * (리터럴 대체 문법) `<span th:text="|Welcome to our application, ${user.name}!|">`

### 반복 출력 - th:each
* `<tr th:each="item : ${items}">`
* 반복은 th:each 를 사용 items 컬렉션 객체의 데이터들이 item에 하나씩 담김
* 컬렉션내 데이터의 수만큼 반복 (향상된 for 문)

### 변수 표현식 - ${...}
* `<td th:text="${item.price}">10000</td>`
* 모델에 포함된 값이나, 타임리프 변수로 선언한 값을 조회할 수 있음
* 프로퍼티 접근법 사용(get, set때고, 대문자를 소문자로 치환)

### 내용 변경 - th:text
* `<td th:text="${item.price}">10000</td>`
* 내용의 값을 `th:text`의 값으로 변경
* 10000을 `${item.price}`의 값으로 변경한다.

### URL 링크 표현식2 - @{...}
* `th:href="@{/basic/items/{itemId}(itemId=${item.id})}"`--> (itemId=${item.id}) 경로 변수 사용
* URL 링크 표현식을 사용하면 경로를 템플릿처럼 편리하게 사용할 수 있음
* 경로 변수 및 쿼리 파라미터도 생성 가능
* (문법)th:href="@{/basic/items/{itemId}(itemId=${item.id}, query='test')}"
* 결과 http://localhost:8080/basic/items/1?query=test

### 간단한 URL 링크(리터럴 대체 사용)
* `th:href="@{|/basic/items/${item.id}|}"`
* 상품이름 선택하는 링크를 리터럴 대체 문법을 사용해 간단히 표현


> 타임리프는 순수 HTML 파일을 웹 브라우저에서 열어도 꺠지지 않고, 서버를 통해 뷰 템플릿을 거쳐도 동적인 결과를
> 확인할 수 있다. 이런 타임리프의 특성때문에 Natural Template이라고도 한다.

<br>

## [상품 상세]
상품 상세 컨트롤로와 뷰를 개발하자.

```java
@GetMapping("/{itemId}")
public String item(@PathVariable Long itemId, Model model) {...}
```
`PathVariable` 로 넘어온 상품ID로 상품을 조회하고, 모델에 담아둔다. 그리고 뷰 템플릿을 호출한다.

### 속성 변경 - th:value
`th:value="${item.id}"`
* 모델에 있는 item 정보를 획득하고 프로퍼티 접근법으로 출력 (get, set 없애고 대문자를 소문자로)
* value 속성을 동적으로 렌더링하면 `th:value`속성으로 변경됨

**상품수정 링크**
* `th:onclick="|location.href='@{/basic/items/{itemId}/edit(itemId=${item.id})}'|"` 리터럴 대체, 경로 변수 사용
* `th:onclick="|location.href='@{/basic/items}'|"` 리터럴 대체 문법 이용
