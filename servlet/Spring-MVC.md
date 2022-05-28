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

> HTTP 응답으로 HTML을 반환할 때는 content-type을 text/html 로 지정해야 한다
