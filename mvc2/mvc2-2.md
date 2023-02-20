`6. 로그인 처리1 - 쿠키, 세션 ~ 11. 파일 업로드 까지!`

# <로그인 처리1 - 쿠키, 세션>
## [로그인 요구사항]
- 홈 화면 - 로그인 전 
  - 회원 가입
  - 로그인
- 홈 화면 - 로그인 후
  - 본인 이름(누구님 환영합니다.) 
  - 상품 관리
  - 로그 아웃
- 보안 요구사항
  - 로그인 사용자만 상품에 접근하고, 관리할 수 있음
  - 로그인 하지 않은 사용자가 상품 관리에 접근하면 로그인 화면으로 이동
- 회원 가입, 상품 관리

<br><br>

## [프로젝트 생성]
### 패키지 구조 설계
- hello.login
  - domain
    - item
    - member
    - login
  - web
    - item
    - member
    - login
  
### 도메인이 가장 중요하다.
도메인 == 화면, UI, DB 기술, 인프라 등등의 영역은 제외한 시스템이 구현해야 하는 
핵심 비즈니스 업무 영역을 말한다.

후에 web을 다른 기술로 바꾸어도 도메인은 그대로 유지할 수 있어야함   
이렇게 하기 위해 web -> domain 방향으로만 의존관계가 흐르도록 설계해야 한다.   
즉, domain은 web을 참조하면 안된다.
> 예를들어 `repository에서 전달 객체 도메인으로 변환하지 않고 web아래에 dto를 직접 전달하면`   
> domain에 해당되는 repository에서 web의 dto객체를 알아야 하므로 의존관계가 domain -> web으로 흐르게 된다.   
> 이는 적절하지 못한 설계

<br><br>

## [회원 가입]
예제에 나온대로 잘 따라서 만들어가보자!

<br><br>

## [로그인 기능]
로그인 기능 개발
- 로그인 ID, 비밀번호 입력하는 부분에 집중해 개발

로그인 컨틀롤러는 로그인 서비스를 호출해서 로그인에 성공시 홈 화면으로 이동
로그인 실패시 bindingResult.reject()를 통해 글로벌 오류로 등록한다.

### 실행
- 로그인 성공: 홈 화면으로 이동
- 로그인 실패: 경고와 함께 로그인 폼으로 다시 이동

하지만 초기 요구사항에선 로그인 성공시 홈 화면에 고객이름이 보여야 하는데 그러지 못하고 있다.
어떻게 고객의 이름을 보여줄 수 있을까?

<br><br>

## [로그인 처리하기 - 쿠키 사용]
쿠키를 사용해서 로그인, 로그아웃 기능을 구현해보자

### 로그인 상태 유지하기
로그인의 상태를 쿠키를 이용해 유지해보자

- 쿠키
  - 서버에서 로그인에 성공시 HTTP 응답에 쿠키를 담아 브라우저에 전달, 브라우저는 앞으로 해당 쿠키를 지속해서 보내줌

- 쿠키의 종류
  - 영속 쿠키: 쿠키의 만료 날짜를 입력하면 해당 날짜까지 유지
  - 세션 쿠키: 만료 날짜를 생략하면 브라우저 종료시 까지만 유지
- 쿠키 Annotation
  - `@CookieValue`를 사용하면 편리하게 쿠키 조회가능(더불어 자동 형변환도 해준다)   
    name으로 쿠키의 이름을 지정하고, required를 false로 두어야 로그인하지 않은(쿠키가 없는)사용자도 접근 가능

### 로그아웃 기능
- 세션 쿠키는 웹 브라우저 종료시 자동 로그아웃이 된다.
- 우리가 임의로 로그아웃하기 위해서는 서버에서 해당 쿠키의 종료날짜를 0으로 지정한다.   
  `cookie.setMaxAge(0)`

<br><br>

## [쿠키와 보안 문제]
쿠키를 사용해서 로그인을 유지하게 되면 심각한 보안 문제가 존재한다.

### 쿠키 로그인의 보안 문제
1. 쿠키 값은 임의 변경 가능(개발자 모드에서)
2. 쿠키에 보관된 정보는 훔쳐갈 수 있다.
   - 따라서 개인정보같이 민감한 사항들은 쿠키에 담으면 안됨
3. 누군가가 쿠키를 훔쳐가면 평생 사용 가능(만약 쿠키만료가 지정되어 있지 않다면)


### 대안책
- 쿠키에 중요 값을 노출하지 않고, 사용자 별로 임의의 토큰 값을 노출해, 서버에서 토큰과 사용자 id를 매핑해 인식   
   또한 서버에서 토큰을 관리
- 토큰은 임의의 값을 넣어도 찾을 수 없도록 예상 불가해야함
- 암호화된 토큰이어도 만료시간이 무한이라면 그대로 사용할 수 있음, 따라서 만료시간을 짧게 유지한다.   
  더불어 해킹이 의심되는 경우 서버에서 강제로 해당 토큰을 제거할 수 있다.

<br><br>

## [로그인 처리하기 - 세션 동작 방식]
세션을 이용하게 되면 세션이 관리하게 되는 세션 저장소가 생성된다.
이곳은 key : value 형식으로 관리된다.

우리는 쿠키 + 세션을 더해서 좀 더 보안을 신경쓴 방식으로 로그인 할 것이다.

### 동작원리
1. 클라이언트에서 로그인 -> 서버로 아이디, 비밀번호 전송
2. 서버측은 DB와 통신해 아이디 및 비밀번호를 검증한다.
3. 올바른 사용자 정보라면 임의의 변수(UUID)를 생성해 이를 key로 하고 value는 사용자(member)로 세션에 저장한다.
4. 쿠키를 통해 웹브라우저에게 전달해야 하는데 이때 쿠키값에 session의 암호화된 key값을 전달한다.

위와 같은 방식으로 로그인을 하게 되면 결국 회원과 관련된 정보는 전혀 클라이언트에 전달하지 않게 되고,   
추정 불가능한 세션 ID만 쿠키를 통해 클라이언트에게 전달한다.

만약 클라이언트가 요청하게 되면 요청으로 받은 쿠키에서 sessionID값을 세션 저장소에서 조회해 일치하는 멤버를 찾아서   
사용하면 된다.

### 기존의 보안 문제 해결
1. 쿠키값 변조 가능 -> 예상 불가능한 복잡한 세션 ID 사용
2. 쿠키 보관 정보는 털릴 가능성 있음 -> 우리가 만드는 세션 ID에는 중요 정보가 없음
3. 쿠키 탈취 후 재사용 -> 쿠키의 만료시간을 지정해놓아 일정 시간이 지나면 expired됨, 또한 해킹의심시 강제로 제거한다.

<br><br>

## [로그인 처리하기 - 세션 직접 만들기]
깊이있는 이해를 위해 직접 개발해서 적용해보자!

세션 관리는 크게 3가지 기능을 제공해야 한다.
1. **세션 생성**
   - 추정 불가능 랜덤값인 sessionId 생성
   - 세션 저장소에 sessionId 및 보관할 값 저장
   - sessionId로 응답 쿠키 생성해 클라이언트에 전달
2. **세션 조회**
   - 클라이언트가 요청한 sessionId 쿠키의 값으로, 세션 저장소에 보관한 값 조회
3. **세션 만료**
   - 클라이언트가 요청한 sessionId 쿠키 값으로, 세션 저장소에 보관한 sessionId와 값 제거

<br>

- sessionStore는 여러 스레드에서 접근할 수 있으므로 동시성 이슈가 없는 concurrentHashMap을 이용하자 
- 테스트는 MockHttpServletRequest, Response이용!

<br><br>

## [로그인 처리하기 - 직접 만든 세션 적용]
세션은 단지 쿠키를 사용하는데, 서버에서 데이터를 유지하기 위한 방법이다.   
프로젝트마다 이런 세션을 직접 만들어서 사용하는 것은 불편함   
따라서 서블릿이 지원해주는 세션을 이용할 수 있다.   
우리가 만든 기능을 모두 지원해주고 더하여 일정 시간 사용하지 않으면 expire해주는 세션 삭제 기능을 제공한다.

<br><br>

## [로그인 처리하기 - 서블릿 HTTP 세션1]
서블릿은 HttpSession이라는 기능을 제공한다.

### 소개
HttpSession도 직접 만든 SessionManager와 같은 방식으로 동작함   
서블릿을 통해 HttpSession을 생성하면 다음과 같은 쿠키를 생성한다. 쿠키 이름이 JSESSIONID이며, 값은 랜덤값

### 생성 및 조회
세션 생성은 `request.getSession(true)`를 사용한다.   

매개변수인 boolean create는 default가 true이며 기존 세션이 있다면 반환하고 없다면 새로운 세션을 생성한다.   
하지만 false로 두게되면 기존 세션이 존재하지 않는다면 null값을 반환한다.

세션에 데이터를 보관하는 방법은 session.setAttribute(name, Obejct)로 request.setAttribute()와 비슷하다.   
실제로 세션에는 여러 값들을 저장할 수 있다.

### 의문
로그인을 완료하게 되면 URL에 다음과 같이 세션이 보인다.   
`http://localhost:8080/;jsessionid=5FAC72206CC239062BF775342AD4EDFC` 왜 그럴까?

<br><br>

## [로그인 처리하기 - 서블릿 HTTP 세션2]
스프링은 세션을 편리하게 사용할 수 있도록 `@SessionAttribute`라는 어노테이션을 제공한다.   
`name` 옵션 으로 세션에 저장된 객체의 이름을 지정하고, `required` 옵션은 true 또는 false를 통해
세션이 없거나 속성이 없다면 null을 받게 하거나(false), 예외를 던지게 할 수 있다.(true)

### TrackingMode
로그인을 처음 시도하면 URL이 다음과 같이 jsessionid 를 포함하고 있는 것을 확인할 수 있다.   
`http://localhost:8080/;jsessionid=F59911518B921DF62D09F0DF8F83F872`

혹여나 웹 브라우저가 쿠키를 지원하지 않는경우 URL을 통해 세션 id를 전송할 수 있게 하기위해 지원해준다.
이를 끄기 위해서는 application.properties에 설정을 해주면 된다.   
`server.servlet.session.tracking-modes=cookie`

<br><br>

## [세션 정보와 타임아웃 설정]
세션이 제공하는 정보를 log를 찍어보면 세션 아이디, 유효시간, 생성시간, 마지막 접근 시간, 새로 생성됐는지의 여부를 알 수 있다.

세션은 서버에 부담이 크므로, 일정 시간동안 접근이 없다면 자동적으로 세션을 파기시켜주도록 사용하고, 주로 30분이 적절한 시간이다.
또한, 세션 유지 시간이 길어지면, 세션 id가 탈취됐을때 해킹의 위험성이 증가하므로 적절한 시간을 유지해야한다.

HttpSession은 세션의 마지막 접근 시간을 기준으로 유효 시간을 계산하는데 default 값은 1800초(30분)이다.
만약 글로벌하게 변경하고 싶다면 application.properties에 다음과 같이 설정한다.   
`server.servlet.session.timeout=60` (기본은 1800초)

혹은 특정 세션 단위로 시간을 설정한다.   
`session.setMaxInactiveInterval(1800); // 1800초`

### 세션 타임아웃
세션의 유지 시간은 마지막 HTTP 요청 시간을 기준으로 계산된다.(`LastAccessedTime`)   
LastAccessedTime 이후 timeout 시간이 지나면 WAS는 내부에서 해당 세션을 제거한다.

<br><br>

# <로그인 처리2 - 필터, 인터셉터>
## [서블릿 필터 - 소개]
우리가 작성한 만들어온 애플리케이션의 문제
- 로그인을 하지 않아도 상품 관리 페이지에 접속할 수 있다.
- 따라서 우리는 로그인 한 사용자만 접속할 수 있도록 해야 한다.

1. 모든 Controller에서 로그인 검증 -> 비효율적
2. 공통 관심사를 한번에 처리
   - AOP, 서블릿 필터, 스프링 인터셉터를 사용할 수 있음
   - 웹과 관련된 공통 관심사는 서블릿 필터, 스프링 인터셉트를 권장

### 서블릿 필터
서블릿이 지원하는 수문장 역할 필터는 다음과 같은 흐름을 가진다.   
`HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 컨트롤러`

만약 모든 고객의 요청 로그를 남겨야 하는 요구사항이 있다면 필터를 사용하면 손쉽게 해결할 수 있다.
또한 필터는 특정 URL 패턴에 적용할 수 있다.

만약 로그인 확인 필터에서 로그인이 되지 않은 사용자라면 필터 단계에서 거르게 되므로 후에 서블릿을 호출하지 않는다.
더불어 필터는 다음과 같이 여러 필터를 체인 형식으로 호출할 수 있고, 여러 필터를 중간에 자유롭게 추가 가능하다.   
`HTTP 요청 -> WAS -> 필터1 -> 필터2 -> 필터3 -> 서블릿 -> 컨트롤러`

필터 인터페이스는 서블릿 컨테이너가 싱글톤 객체로 생성하고 관리하며 3가지의 메소드가 존재한다.
1. init(): 필터 초기화, 서블릿 컨테이너 생성시 호출
2. doFilter(): 고객의 요청이 올때마다 해당 메서드 호출, 필터의 로직
3. destroy(): 필터 종료 메서드, 서블릿 컨테이너가 종료될 때 호출됨.

<br><br>

## [서블릿 필터 - 요청 로그]
```java
@Override
public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
        throws IOException, ServletException {
    chain.doFilter(request, response);  // 다음 필터가 있다면 호출 없다면 서블릿 호출
}
```
위의 doFilter메서드는 필터 체인을 이용하기 위해 필수적으로 작성해야 하는 코드이다.
다음으로 호출해야할 필터가 있다면 해당 필터를 호출하며 만약 마지막 필터라면 서블릿을 호출한다.

### 필터의 등록
`FilterRegistrationBean`을 이용해 등록할 수 있다.   
@ServletComponentScan @WebFilter를 이용해 등록할 수 있지만, 이는 필터의 순서 보장이 되지 않는다.

### 참고
**작성한 코드중에서 사용자를 구분하는 식별자를 모든 요청에 남기기 위해서는 logback mdc를 이용한다.**

<br><br>

## [서블릿 필터 - 인증 체크]

### 필터의 필요성
모든 곳에 로그인 인증을 적용하게 되면 로그인을 하려는 사용자도 로그인이 되어있지 않아 접근할 수 없는
상황이 발생한다. 따라서 홈, 회원가입, 로그인, css와 같은 리소스에는 로그인을하지 않아도 접근할 수 있어야 하는데
이런 리스트를 `whitelist`라고 한다.

### 필터 적용
위와 같은 리스트를 제외하고는 로그인 인증 체크 필터를 적용한다.

```java
try {
    log.info("인증 체크 필터 시작{}", requestURI);
    if (isLoginCheckPath(requestURI)) {
        log.info("인증 체크 로직 실행 {}", requestURI);
        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            log.info("미인증 사용자 요청 {}", requestURI);
            // 로그인으로 redirect
            httpResponse.sendRedirect("/login?redirectURL=" + requestURI);
            return;
        }
    }
    chain.doFilter(request, response);
} catch (Exception e) {
    throw e; // 예외 로깅 가능 하지만, 톰캣까지 예외를 보내주어야 함
} finally {
    log.info("인증 체크 필터 종료 {}", requestURI);
}
```

### 동작 원리
인증된 사용자가 들어온다면 다음 필터 혹은 서블릿을 호출하지만, 인증되지 않은(로그인 x) 사용자라면
현재 요청한 url을 쿼리 스트링에 붙여서 로그인 페이지로 리다이렉트한다.

여기서 현재 요청한 url을 붙여주는 이유는, 사용자가 로그인을 했을때 무작정 홈 화면으로 가기보단
직전에 요청한 인증이 필요했던 페이지로 이동해주는것이 UX적인 관점에서 훨씬 좋기 때문이다.

이후 로그인을 하게됐을때 쿼리스트링의 값으로 리다이렉트하면 된다.   
`@RequestParam(defaultValue = "/") String redirectURL`   
(default가 `/`이므로 쿼리스트링의 값이 없다면 홈 화면으로 이동한다.)

### 결과
서블릿 필터의 적용으로 인증되지 않은 사용자는 나머지 경로에 접근할 수 없음   
또한 공통적인 관심사를 각 컨트롤러가 아닌 필터로 분리했으므로 향후 변경시에도 유연하게 대처할 수 있다.

<br><br>

## [스프링 인터셉터 - 소개]
스프링 인터셉터와 필터의 차이는 호출 순서와 제공하는 기능에 차이가 있다. 스프링 인터셉터의 호출순서는 다음과 같다.   
`HTTP 요청` -> `WAS`-> `필터` -> `서블릿` -> `스프링 인터셉터` -> `컨트롤러`

서블릿과 컨트롤러 사이에서 컨트롤러 호출 직전에 호출 된다. (스프링 MVC가 제공하는 기능 이므로 디스패처 서블릿 이후에 등장)   
만약 스프링 인터셉터에서 적절하지 않은 요청이라 판단되면 컨트롤러는 호출하지 않는다.   
또한, 필터 처럼 인터셉터도 체인 방식으로 자유롭게 추가할 수 있다.

### 스프링 인터셉터 interface
HandlerIntercepter를 구현하면 되며, 3가지의 메서드를 가진다.

1. `boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)`
   - 컨트롤러 호출전 실행됨
2. `boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModealAndView modelAndView)`
   - 컨트롤러 호출 이후 실행됨 (컨트롤러에서 예외 발생시 호출되지 않음)
   - true를 반환하면 다음으로 진행하고, false이면 더는 진행하지 않음
3. `boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex)`
   - 뷰까지 렌더링 된 이후에 호출됨, 컨트롤러에서 예외 발생해도 반드시 호출함
   - 따라서 예외와 무관한 공통 처리를 처리할 수 있다.

### 정리
인터셉터는 스프링 MVC 구조에 특화된 필터 기능을 제공한다. 꼭 필터를 사용해야 하는 상황이 아니라면 인터셉터를 사용하자

<br><br>

## [스프링 인터셉터 - 요청 로그]
스프링 인터셉터에서 호출 시점의 분리로 인해 UUID같은 값들을 공유할 수 없다. 필드로 사용하고자 한다면
싱글톤으로 관리되는 인터셉터로 인해 중간에 값이 변경될 위험이 존재한다.

따라서 request.setAttribute()를 이용하면 된다.

### HandlerMethod
일반적으로 @Controller, @RequestMapping을 활용한 핸들러 매핑의 경우, 각 메소드 단위의 핸들러 정보를 담기위해
`HandlerMethod` 인스턴스가 넘어온다.
```java
@Override
public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
        throws Exception {
    // @RequestMapping: HandlerMethod
    // 정적 리소스: ResourceHttpRequestHandler

    if (handler instanceof HandlerMethod) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
    }
    return true;
}
```

### ResourceHttpRequestHandler
정적 리소스를 호출하는 경우 ResourceRequestHandler가 핸들러 정보로 넘어온다. 따라서 타입에 따른 분류가 필요하다.

### postHandle, afterCompletion
최종 RESPONSE 로그를 afterCompletion에서 출력하도록 한 이유는 만약 Controller에서 예외가 발생하면
postHandle을 실행하지 않기 때문이다. 반면에 afterCompletion은 뷰 렌더링 이후 반드시 실행하므로
로그를 확인해야 하는 경우 100% 출력을 보장한다.

### WebConfig 클래스에 인터셉트 등록
인터셉터를 등록하기 위해서는 WebMvcConfigurer 인터페이스를 구현하여 `addInterceptors(InterceptorRegistry registry)` 메서드를 오버라이딩한다.   
이후 메소드 체이닝 방식으로 필터를 등록하게 된다.
```java
@Configuration
  public class WebConfig implements WebMvcConfigurer {
      @Override
      public void addInterceptors(InterceptorRegistry registry) {
          registry.addInterceptor(new LogInterceptor())
                  .order(1) // 순서 지정
                  .addPathPatterns("/**") // 인터셉터 적용 URL 패턴 지정
                  .excludePathPatterns("/css/**", "/*.ico", "/error"); // 인터셉터 제외 URL 패턴 지정
      }
//...
}
```
필터와 달리 조금 더 정밀하게 URL패턴의 지정이 가능하다.

### 참고
자세한 PathPattern은 공식문서를 참조하자.   
https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/util/pattern/PathPattern.html

<br><br>

## [스프링 인터셉터 - 인증 체크]
인터셉터를 이용한 로그인 인증 체크는 필터보다 훨씬 간단하다.   
인증 체크는 `preHandle`에서만 검사하면 되므로 하나의 메서드만 구현한다.

```java
@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws Exception {
        String requestURI = request.getRequestURI();
        log.info("인증 체크 인터셉터 실행 {}", requestURI);

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            log.info("미인증 사용자 요청");
            // 로그인으로 redirect
            response.sendRedirect("/login?redirectURL=" + requestURI);
            return false;
        }

        return true;
    }
}
```
기존 필터에서와 달리 whitelist의 내용이 없고, 단지 인증에 관한 정보만 담겨있다.   
인터셉터를 적용할 URL 패턴과 적용하지 않을 패턴은 모두 인터셉터를 등록할때 설정할 수 있다.   

```java
registry.addInterceptor(new LoginCheckInterceptor())
        .order(2)
        .addPathPatterns("/**")
        .excludePathPatterns("/", "/members/add", "/login", "/logout", "/css/**", "/*.ico", "/error");
```
기존 필터보다 훨씬 정교하게 등록할 수 있고, 심지어 실제 preHandle 로직과 분리되어 조금 더 좋은 가독성을 보인다.

### 정리
서블릿 필터 vs 스프링 인터셉터라면 주로 인터셉터가 훨씬 편리함을 알 수 있었다.
필터를 사용해야하는 특별한 경우가 아니라면 스프링 인터셉터를 주로 사용할듯 하다.

<br><br>

## [ArgumentResolver 활용]
ArgumentResolver를 활용해 조금더 편리한 로그인 기능 만들기

### 기존 코드
```java
    @GetMapping("/")
    public String homeLoginV3Spring(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {
        // 세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }
        // 세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }
```
@SessionAttribute를 사용해, 담긴 객체의 이름과 및 여러 설정을 통해 Member를 가져온다.   
이를 단순하게 custom annotation을 활용해 만들어보자

### @Login Annotation
`@Login` annotation이 존재하면 `ArgumentResolver`가 동작해 자동으로 세션에 있는 회원을 찾아서 객체로
반환해주게 만들고 존재하지 않는다면 null을 반환하도록 해보자

어노테이션은 ElementType은 PARAMETER로 RetentionPolicy는 RUNTIME으로 둔다.
RUNTIME으로 두는 이유는 리플렉션 등을 활용할 수 있게 한다.

### ArgumentResolver 만들기
`HandlerMethodArgumentResolver`를 구현하는 클래스를 만들게 되면 두 가지의 메소드를 재정의한다.

1. `supportsParameter`
   - 여기서는 파라미터에 @Login 어노테이션이 붙어있는지와, @Login이 붙은 파라미터의 타입이 Member인지 모두 확인한다.
2. `resolveArgument`
   - 실제 세션에서 객체를 가져오는데 세션이 없다면 null을 반환, 세션이 존재하면 로그인 멤버를 찾아서 반환하여 Argument를 resolve함

### ArgumentResolver 등록하기
`WebMvcConfigurer`를 구현하는 WebConfig에서 addArgumentResolvers 메서드를 재정의하고 `리스트`에 
만들었던 LoginMemberArgumentResolver를 추가한다.

### 참고
supportsParameter 메소드는 이미 한번 호출했던 상황이라면 캐싱을 적용하기에 두번째 호출할때는
호출되지 않는다.

<br><br>

# <예외 처리와 오류 페이지>
## [서블릿 예외 처리 - 시작]
### 자바와 웹애플리케이션의 기본 Exception
자바는 기본적으로 예외가 발생되어 main() 메서드까지 예외가 넘어오고 여기서도 던져지게 되면, 예외 정보를 남기고 해당 쓰레드를 종료한다.

웹 애플리케이션은 다음과 같은 순서로 예외가 전달된다.   
`WAS(여기까지 전파) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러(예외발생)`

실제 스프링에서 제공하는 whitelabel 페이지를 꺼두고 컨트롤러에서 RuntimeException이 발생되는 url을 입력하여
예외를 발생시켜보면 500 에러를 톰캣의 기본화면으로 출력한다.

존재하지 않는 url을 입력하면 404에러를 발견할 수 있다.

### response.sendError(HTTP 상태코드, 메시지)
HttpServletResponse에 sendError로 상태코드와 메시지를 전달하면 응답 내부에 오류가 발생했다는 상태를 저장 한다.   
이후 서블릿 컨테이너는 응답 이전에 sendErorr()가 호출됐는지 확인하고 호출됐다면 입력된 상태코드에 맞는 오류 메시지를 보여준다.

하지만 이런 오류화면 조차도 사용자가 보기에는 상당히 불친절하며 크게 의미가 없는 페이지가 된다.

<br><br>

## [서블릿 예외 처리 - 오류 화면 제공]
스프링이 기본적으로 제공하는 예외 페이지는 친절하지 않으므로 이를 직접 커스텀해보자

### WebServerFactoryCustomizer\<ConfigurableWebServerFactory> 사용
이를 사용하여 customize() 메서드를 오버라이딩하면 factory에 원하는 에러 상태 코드와 해당 코드에 맞는 페이지의 경로를 등록할 수 있다.
```java
@Component
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
    @Override
    public void customize(final ConfigurableWebServerFactory factory) {
        ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error-page/404");
        ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");
        ErrorPage errorPageEx = new ErrorPage(RuntimeException.class, "/error-page/500");
    
        factory.addErrorPages(errorPage404, errorPage500, errorPageEx);
    }
}

```
예외가 WAS까지 올라가게 되고 만약 커스텀 등록한 에러 페이지가 존재한다면 WAS는 해당 코드에 맞는 페이지의 경로를 그대로 호출한다.

GET과 POST의 차이를 두지 않고, 에러가 발생하면 호출되어야 하므로 ErrorPageController에서는 @RequestMapping을 이용하여 HTTP 메소드를 구분하지 않는다.

<br><br>

## [서블릿 예외 처리 - 오류 페이지 작동 원리]
예외가 발생되고 WAS까지 예외가 전달되면 WAS는 오류 페이지 정보들을 확인한다.   
이후 WAS는 오류 페이지 출력을 위해 마치 HTTP 요청이 온 것 처럼 해당 페이지를 다시 요청한다.   
`WAS `/error-page/500` 다시 요청 -> 필터 -> 서블릿 -> 인터셉터 -> 컨트롤러(/error-page/ 500) -> View`

### 예외 발생과 오류 페이지 요청 흐름
1. WAS(여기까지 전파) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러(예외발생)
2. WAS `/error-page/500` 다시 요청 -> 필터 -> 서블릿 -> 인터셉터 -> 컨트롤러(/error- page/500) -> View

> 하지만 클라이언트 입장에서 이러한 서버 내부의 요청이 발생하는지 전혀 모른다.

결국 예외가 발생되면 WAS까지 전파되고 WAS는 오류 페이지 경로를 찾아서 내부에서 오류 페이지를 호출한다.
이때 필터, 서블릿, 인터셉터, 컨트롤러가 모두 호출되며 이는 마치 새로운 HTTP 요청과 같아 보인다.

### 오류 정보 추가
WAS는 단순히 오류 페이지를 요청할 뿐만아니라 오류 정보를 request의 attribute에 추가하여 넘겨준다.   
(예외, 예외 타입, 오류 메시지, 클라이언트 요청 URI, 오류 발생 서블릿 이름, HTTP 상태 코드 등)   
오류 정보는 `RequestDispatcher`를 살펴보면 확인할 수 있다.

<br><br>

## [서블릿 예외 처리 - 필터]
오류가 발생되어서 WAS가 오류페이지를 호출하게 되면 필터, 인터셉터를 모두 재호출 해야할까?   
일단 초기 요청에서 필요한 필터, 인터셉터를 통해 인증을 마쳤으므로 내부 호출에서 다시 필터, 인터셉터의 로직을 타는건
비효율적으로 보인다.

이러한 부분들을 방지하기 위해 Servlet은 `DispatcherType`이라는 Enum을 제공한다. 이를통해 고객의 요청인지, 혹은 단순히 ERROR가 발생해
서버 내부가 호출한 오류인지를 구분할 수 있다.

이런 타입은 HttpServletRequest에 담겨있으며 `getDispatcherType()`을 통해 받아올 수 있다.

### DispatcherType
- `REQUEST` : 클라이언트 요청 
- `ERROR` : 오류 요청
- `FORWARD` : MVC에서 배웠던 서블릿에서 다른 서블릿이나 JSP를 호출할 때 RequestDispatcher.forward(request, response);
- `INCLUDE` : 서블릿에서 다른 서블릿이나 JSP의 결과를 포함할 때 RequestDispatcher.include(request, response);
- `ASYNC` : 서블릿 비동기 호출

### 필터에 허용할 DispatcherType 적용
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter());
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");
        // 해당 필터는 REQUEST, ERROR 두 가지 경우에 호출되어 사용되는 필터이다.
        filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR);
        return filterRegistrationBean;
    }
}
```
> 참고로 default dispatcherType은 REQUEST임으로 특정 오류 페이지 검토가 필요하지 않다면 default 설정을 사용하면 된다. 

<br><br>

## [서블릿 예외 처리 - 인터셉터]
### 인터셉터 원리와 dispatcherType 구분
인터셉터는 필터와 같이 dispatcherType을 따로 선택하는 방식은 없다.   
서블릿이 아닌 스프링이 제공하는 기능이므로 type과 무관하게 항상 실행된다.

반면에 pathPatterns을 제외시키는 excludePathPatterns() 메서드를 이용해 빼줄 수 있다.   
대부분의 오류 페이지의 url은 정해져 있으므로 이 방법으로 오류 페이지 호출시 인터셉터를 호출하지 않을 수 있다.

### 전체 흐름 `/error-ex 오류 요청`
- 필터는 DispatchType 으로 중복 호출 제거 ( `dispatchType=REQUEST` )
- 인터셉터는 경로 정보로 중복 호출 제거( `excludePathPatterns("/error-page/**")` )

1. WAS(/error-ex, dispatchType=REQUEST) -> 필터 -> 서블릿 -> 인터셉터 -> 컨트롤러
2. WAS(여기까지 전파) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러(예외발생)
3. WAS 오류 페이지 확인
4. WAS(/error-page/500, dispatchType=ERROR) -> 필터(x) -> 서블릿 -> 인터셉터(x) -> 컨트롤러(/error-page/500) -> View

<br><br>

## [스프링 부트 - 오류 페이지1]
기존 `WebServerFactoryCustomizer<ConfigurableWebServerFactory>`를 활용한 오류 페이지 등록은 다음과 같은 절차를 가진다.
1. Custom 오류 페이지 등록
2. 예외 종류에 따른 페이지 추가(html or templates)
3. 예외 처리용 Controller 만듦(ErrorPageController.java)

하지만 스프링 부트에서는 이런 과정을 자동화 해준다. 사용자는 단지 적절한 위치에 error page만 만들어두면 된다.
- default error page는 /error에 저장되며 해당 페이지는 사용자 커스텀으로 변경 가능
- 서블릿 밖으로 예외 발생 혹은 response.sendError()를 호출하게 되면 해당 `/error`를 호출한다.
- `BasicErrorController`라는 스프링 컨트롤러를 자동으로 등록한다.
  - ErrorPage에서 등록한 /error를 매핑해서 처리하는 컨트롤러

### View 선택 우선순위
1. 뷰 템플릿 `resources/templates/error/**`
2. 정적 리소스 `resources/static/error/**`
3. 적용 대상이 없을 때 뷰 이름(error) `resources/templates/error.html`

파일 이름은 error code로 정해지며 만약 `4xx.html`이라면 400번대 오류는 해당 페이지를 오픈한다.   
또한 404.html과 같이 4xx보다 더 구체적이면 구체적인 페이지의 우선순위가 높다.

<br><br>

## [스프링 부트 - 오류 페이지2]
`BasicErrorController`는 단순히 에러 페이지만 나타내는것이 아닌 model에 추가 정보들을 담아서 전달하기도 한다.
- timestamp, status, error, exception, trace, message, errors, path 등
```thymeleafexpressions
<li th:text="|timestamp: ${timestamp}|"></li>
<li th:text="|path: ${path}|"></li>
<li th:text="|status: ${status}|"></li>
<li th:text="|message: ${message}|"></li>
<li th:text="|error: ${error}|"></li>
<li th:text="|exception: ${exception}|"></li>
<li th:text="|errors: ${errors}|"></li>
<li th:text="|trace: ${trace}|"></li>
```
따라서 다음과 같이 출력할 수도 있지만, 오류 관련 내부 정보를 보여주게 되면 오히려 사용자의 가독성을 해치거나
해킹의 위험성도 배제할 수 없다. 이러한 설정들을 만약에 보여주고 싶다면 application.properties에서 설정할 수 있다.

### 참고
에러 공통 처리 컨트롤러의 기능을 변경하고자 한다면 ErrorController 인터페이스를 구현하거나, BasicErrorController를 상속받아 기능을 추가할 수 있다.

<br><br>

## [정리]
스프링 부트를 이용해 오류 `페이지`를 출력하는 방법은 크게 어려움이 없다.   
하지만, API 통신을 이용한다면 실제 오류를 전달하는 방식에서 많은 고려 사항이 필요하다.
서버와 클라이언트간 어떤 오류 데이터를 주고 받을지에 대한 규약을 먼저 설정하고, 이후에 해당 데이터를 JSON으로 보내주어야 한다.

<br><br>

# <API 예외 처리>
## [시작]
단순 HTML 페이지로 오류 페이지를 구성하는 방식은 간단하다. 하지만 api는 어떨까?   
api는 각 오류 상황에 따른 응답 스펙을 정하고, JSON으로 데이터를 내려주어야 하므로 조금 더 복잡하고 고려해야할 부분들이 많다.

api 호출시 오류가 발생했을때 HTML 파일이 내려오면 안되므로 새로운 핸들러를 추가해야 한다.
동일하게 @RequestMapping을 사용하지만, 해당 속성 중 `produces = APPLICATION_JSON_VALUE`로 적용하면
HTTP의 `Accept` 헤더가 `application/json`인 요청을 받게 된다. 이를 통해 같은 URL 요청 핸들러가 2개가 있어도 구분할 수 있다.   
따라서 이를 통해 api 오류 응답을 구성해보자

```java
@RequestMapping("/error-page/500")
public String errorPage500(HttpServletRequest request, HttpServletResponse response) { ... }

@RequestMapping(value = "/error-page/500", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> errorPage500Api() { ... }
```
이렇게만 사용해도 기존의 HTML 반환을 JSON 반환으로 변경할 수 있다.
> ResponseEntity에 담긴 Map 구조는 Jackson 라이브러리를 통해 JSON 구조로 변경됨

<br><br>

## [스프링 부트 기본 오류 처리]
커스텀 오류 응답 api를 사용하지 않게 되면 스프링은 `BasicErrorController`를 이용해 기본 오류 api를 응답한다.

BasicErrorController를 살펴보면 errorHtml(), error() 핸들러로  html 응답과 api 응답을 구분하는 모습을 보여준다.
기본 설정인 /error로 오류 페이지로 요청하게 되고 만약 accept값이 application/json이라면 error() 핸들러를 호출한다.
```java
// BasicErrorController내 코드
@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
    HttpStatus status = getStatus(request);
    Map<String, Object> model = Collections
            .unmodifiableMap(getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.TEXT_HTML)));
    response.setStatus(status.value());
    ModelAndView modelAndView = resolveErrorView(request, response, status, model);
    return (modelAndView != null) ? modelAndView : new ModelAndView("error", model);
}

@RequestMapping
public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
    HttpStatus status = getStatus(request);
    if (status == HttpStatus.NO_CONTENT) {
        return new ResponseEntity<>(status);
    }
    Map<String, Object> body = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
    return new ResponseEntity<>(body, status);
}
```

기존의 WebServerCustomizer의 `@Configuration`을 주석처리하고 에러 페이지를 요청하게 되면 BasicErrorController의 error()가 호출되어
json 형식으로 응답값이 오는것을 알 수 있다.

api 오류의 경우 회원, 상품등과 같이 도메인에 따른 오류 응답 api의 스펙이 다를 수 있기에 api 구성은 세밀하고 복잡하다.    
이러한 부분을 조금더 편리하게 지원해주는 @ExceptionHandler 어노테이션이 존재한다.

<br><br>

## [HandlerExceptionResolver 시작]
`HandlerExceptionResolver`를 이용하면 기존 발생한 예외(500)를 잡아서 다른 예외(400, ...)으로 변경할 수 있다.
예를 들어 `IllegalArgumentException`과 같은 경우 사용자의 잘못된 입력으로 발생하는 경우가 많다.   
스프링 부트는 해당 예외가 발생하면 500 code를 반환하므로 이를 400(Bad Request)으로 반환할때 사용된다.

HandlerExcetionResolver를 적용하게 되면 핸들러에서 예외가 발생했을때 ExceptionResolver에서 예외 해결 시도를 한다.
이후 예외를 해결하게 되면 WAS에는 정상응답이 들어가고, 해결하지 못하는 상황이 되면 WAS에는 예외가 전달된다.   

정상 응답 또한 여러가지 방식의 응답이 존재한다.

### 구현
HandlerExceptionResolver 인터페이스를 구현한다. 이후 WebConfig에서 extendHandlerExceptionResolvers를 오버라이딩한다.   
> configureHandlerExceptionResolvers()를 사용하면 스프링이 기본으로 동작하는 ExceptionReoslver가 제거됨
```java
@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(final HttpServletRequest request, final HttpServletResponse response,
                                         final Object handler,
                                         final Exception ex) {
        log.info("call resolver", ex);
        try {
            if (ex instanceof IllegalArgumentException) {
                log.info("IllegalArgumentException resolver to 400");   // 400 오류로 변경
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
                return new ModelAndView();  // 기존 예외를 먹고, 400으로 변경한 sendError를 WAS까지 정상 리턴
            }
        } catch (IOException e) {
            log.error("resolver ex", e);
        }
        return null;
    }
}
```
`IllegalArgumentException`이 발생하면 해당 예외를 먹고, `response.sendError`를 통해 `400(Bad Request)`오류를 응답에 담아 WAS에 정상 전달한다.
정상적인 응답이라면 빈 객체인 `ModelAndView`를 반환한다.
> 빈 ModelAndView를 반환하는 이유는 마치 try, catch를 하듯 Exception을 처리해 정상 흐름 처럼 변경하기 위함이다.

### HandlerExceptionResolver 반환 값에 따른 DispatcherServlet의 동작 방식
- 빈 `ModelAndView`: new ModelAndView() 처럼 빈 객체를 반환하면 뷰를 렌더링 하지 않고 정상 흐름으로 서블릿이 리턴됨
- `ModelAndView` 지정: ModelAndView에 View, Model등의 정보를 지정해서 반환하면 뷰를 렌더링 함
- `null`: null을 반환하면, 다음 ExceptionResolver를 찾아서 실행함, 만약 처리할 수 있는 ExceptionResolver가 없으면
  예외 처리가 안되고, 기존에 발생한 예외를 서블릿 밖으로 던짐 -> `WAS까지 진행됨`

### ExceptionResolver 활용
1. 예외 상태 코드 변환
   - 예외를 `response.sendError(xxx)`를 통해 변경해 서블릿에서 상태에 따른 오류를 처리하도록 위임한다.
   - 이후 `WAS`는 서블릿 오류 페이지를 찾아서 내부 호출함
2. 뷰 템플릿 처리
   - `ModelAndView`에 값을 채워넣으면 에외에 따른 새로운 오류 화면 뷰 렌더링을 제공함
3. API 응답 처리
   - `response.getWriter().println("hello");`처럼 HTTP 응답 바디에 직접 데이터를 넣어준다. 여기에 JSON응답을 하면 API 응답처리가 가능하다.

<br><br>

## [HandlerExceptionResolver 활용]
현재 예외 처리 방식은 예외 발생시 ExceptionResolver를 통해 원하는 예외로 변경하지만 결국 WAS까지 예외가 전달되고
WAS에서 예외에 맞는 핸들러를 호출하여 사용자에게 오류 페이지 혹은 api를 전달한다.

여기서 굳이 WAS까지 예외가 전달되어야 할 필요가 있을지 고민해보자

### ExceptionResolver에서 한번에 해결
만약 UserException 이라는 커스텀 예외가 존재할때 이를 ExceptinoResolver에서 한번에 처리하도록 작성해보자
```java
@Slf4j
public class UserHandlerExceptionResolver implements HandlerExceptionResolver {

    public static final String APPLICATION_JSON = "application/json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ModelAndView resolveException(final HttpServletRequest request, final HttpServletResponse response,
                                         final Object handler,
                                         final Exception ex) {
        try {
            if (ex instanceof UserException) {
                log.info("UserException resolver to 400");
                // HTTP Accept헤더가 JSON인 경우와 HTML로 요청한 경우 두 가지로 분기
                String acceptHeader = request.getHeader("accept");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                if (APPLICATION_JSON.equals(acceptHeader)) {
                    Map<String, Object> errorResult = new HashMap<>();
                    errorResult.put("ex", ex.getClass());
                    errorResult.put("message", ex.getMessage());

                    String result = objectMapper.writeValueAsString(errorResult);

                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                    response.getWriter().write(result);
                    return new ModelAndView();
                }
                // TEXT/HTML
                return new ModelAndView("error/500");

            }
        } catch (IOException e) {
            log.error("resolver ex", e);
        }
        return null;
    }
}
```
### API 반환
위의 코드를 살펴보면 response 객체에 400에러와 반환타입의 정의를 한다.   
또한 에러 결과에 대해서는 Map을 Jackson 라이브러리를 통해 result라는 JSON으로 변환해
`response.getWriter().write(result);`를 통해 api로 응답한다.   
이후 비어있는 ModelAndView를 반환하며 응답을 정상처리한다.

이렇게 되면 WAS까지 정상응답이 넘어가고 예외는 넘겨주지 않는다.   
따라서 UserHandlerExceptionResolver에서 예외에 대한 처리를 마무리 짓게된다.   
서블릿 컨테이너까지 예외가 올라가며 복잡하고 지저분한 프로세스를 ExceptionResolver를 통해 깔끔하게 처리할 수 있다.
> 이전 response.sendError()를 통해 전달하게 되면 WAS는 정상응답이지만 예외가 있다고 판단해 오류 페이지를 호출하는데
> 그러한 로직들이 없고 순수한 API 반환만 존재하기에 가능한 일

### View 반환
만약 HTTP의 accept HEADER가 application/json이 아니라 html타입 이라면 우리가 만들었고 스프링에서 기본으로 사용하는 경로인
templates/error/500.html을 반환한다.

### 정리
하지만 ExceptionResolver를 구현하는 것 마지도 상당히 복잡함을 알 수 있다.
따라서 스프링이 제공하는 ExceptionResolver를 사용하면 이를 해소할 수 있다.

<br><br>

## [스프링이 제공하는 ExceptionResolver1]
### Spring Boot의 기본 제공 ExceptionResolver
`HandlerExceptionResolverComposite`에 다음과 같은 순서로 등록된다.
1. ExceptionHandlerExceptionResolver
2. ResponseStatusExceptionResolver
3. DefaultHandlerExceptionResolver (가장 낮은 우선순위)

가장 먼저 2번에 대해 알아보자

### ResponseStatusExceptionResolver
발생하는 예외에 따라서 HTTP 상태 코드를 지정해주는 역할이며 두 가지 경우를 처리함
1. `@ResponseStatus`가 달려있는 예외
2. `ResponseStatusException` 예외

### **@ResponseStatus가 달려있는 예외**   
커스텀 예외에 @ResponseStatus 애노테이션을 붙이면 지정한 HTTP Status의 code가 적용된다.   
(기존의 모든 예외가 발생하게 되면 500 code이지만 이를 변경할 수 있음)

```java
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "${message or messages.properties key}")
public class BadRequestException extends RuntimeException {
}
```

실제 위의 애노테이션을 처리하는 `@ResponseStatusExceptionResolver`의 코드를 살펴보면 다음과 같이 sendError를 호출해 우리가 지정한 코드로
오류를 WAS에서 다시 오류 페이지를 내부 요청한다.

```java
protected ModelAndView applyStatusAndReason(int statusCode, @Nullable String reason, HttpServletResponse response)
        throws IOException {

    if (!StringUtils.hasLength(reason)) {
        response.sendError(statusCode);
    }
    else {
        String resolvedReason = (this.messageSource != null ?
                this.messageSource.getMessage(reason, null, reason, LocaleContextHolder.getLocale()) :
                reason);
        response.sendError(statusCode, resolvedReason);
    }
    return new ModelAndView();
}
```

또한 애노테이션의 resson 속성은 messages.properties에 등록된 커스텀 에러 메시지의 key를 이용하여 MessageSource에서 찾는 기능도 있다.

### **ResponseStatusException**
위의 애노테이션 방법은 오로지 커스텀한 예외에서만 적용할 수 있고, 라이브러리의 에외 코드 같이 내가 수정할 수 없다면 적용 불가하다.
따라서 ResponseStatusException을 사용해 발생할 예외의 경우 원하는 HTTP code로 응답하도록 설정할 수 있다.
이곳에서도 messages.properties의 MessageSource를 적용할 수 있다.

```java
 @GetMapping("/api/response-status-ex2")
  public String responseStatusEx2() {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "error.bad", new
  IllegalArgumentException());
  }
```

이 또한 ResponseStatusExceptionResolver의 실제 코드를 살펴보면 다음과 같이 예외의 종류가 ResponseStatusException인지 확인하는 코드가 존재한다.

```java
if (ex instanceof ResponseStatusException) {
    return resolveResponseStatusException((ResponseStatusException) ex, request, response, handler);
}
```
그리고 sendError를 통해 특정 상태 코드 및 에러 발생 reason을 WAS에 전달한다.

> sendError와 API 통신   
> 우리가 이용한 ResponseStatusExceptionResolver는 모두 sendError를 통해 발생되는 예외에 우리가 원하는 HTTP 상태 코드를 지정한다. 그리고 빈 ModelAndView를 반환한다. 
> 
> ModelAndView를 반환하게 되면 WAS로 정상 응답이 반환되지만 예외가 발생했다는 error확인 이후 dispatcherType이 ERROR인 상태로 /error를 내부 요청한다.
> 
> 만약 여기서 sendError를 사용하지 않고 빈 ModelAndView를 반환하면 WAS의 오류 확인 및 내부 페이지의 호출 없이 즉시 api응답을 리턴한다.   
> 
> **따라서 우리가 만들었던 ResponseStatusExceptionResolver는 sendError를 반드시 거치므로 WAS의 내부 호출이 반드시 일어난다.**

<br><br>

## [스프링이 제공하는 ExceptionResolver2]
다음 컨트롤러를 살펴보자
```java
@GetMapping("/api/default-handler-ex")
public String defaultException(@RequestParam Integer data) {
    return "ok";
}
```

만약 요청 url이 `/api/default-handler-ex?data=10`과 같이 들어온다면 data 매개변수에 정상적으로 바인딩 될것이다.   
하지만, `/api/default-handler-ex?data=dd`와 같이 정수가 들어가야 할 쿼리스트링에 문자열값이 입력된다면 스프링은 데이터를 바인딩할 수 없다는 오류를 뱉는다.

그렇다면 이는 서버의 잘못일까 클라이언트의 잘못일까?   
위의 요청이 사용자가 form에 가격을 입력해야 하는 form일때 사용자의 숫자가 아닌 입력은 곧 사용자의 잘못이다. 따라서 400 Bad Request가 반환된다.  

### DefaultHandlerExceptionResolver의 역할
Spring은 모든 예외를 500 서버 에러를 내뱉는다. 하지 파라미터 바인딩은 대부분 클라이언트가 HTTP 요청 정보를 잘못 호출해서 발생하는 문제이다. 
HTTP 에서는 이런 경우 HTTP 상태 코드 400을 사용하도록 되어 있다.

이런 파라미터 바인딩 에러는 DefaultHandlerExceptionResolver에서 처리하고 있는데 실제 내부코드는 다음과 같다.
```java
@Override
	@Nullable
	protected ModelAndView doResolveException(
			HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception ex) {

		try {
			if (ex instanceof HttpRequestMethodNotSupportedException) {
				return handleHttpRequestMethodNotSupported(
						(HttpRequestMethodNotSupportedException) ex, request, response, handler);
			}
			else if (ex instanceof HttpMediaTypeNotSupportedException) {
				return handleHttpMediaTypeNotSupported(
						(HttpMediaTypeNotSupportedException) ex, request, response, handler);
			}
			else if (ex instanceof HttpMediaTypeNotAcceptableException) {
				return handleHttpMediaTypeNotAcceptable(
						(HttpMediaTypeNotAcceptableException) ex, request, response, handler);
			}
        ...
        ...
        중략
        ...
        
		}
		catch (Exception handlerEx) {
			if (logger.isWarnEnabled()) {
				logger.warn("Failure while trying to resolve exception [" + ex.getClass().getName() + "]", handlerEx);
			}
		}
		return null;
	}
```
위와 같이 파라미터 바인딩에 관한 오류는 모두 handleXXX 라는 메서드로 처리하는데 내부적으로는 다음과 같은 로직을 갖는다.
```java
protected ModelAndView handleBindException(BindException ex, HttpServletRequest request,
        HttpServletResponse response, @Nullable Object handler) throws IOException {

    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    return new ModelAndView();
}
```

여기서도 sendError를 통해 사용자가 잘못된 입력으로 인한 예외로 변경해주고 있다.
결국 WAS는 해당 에러에 맞는 오류 페이지(/error)를 호출한다.

### 정리
HandlerExceptionResolver는 api응답의 경우 response에 직접 데이터를 넣고 사용하지도 않을 빈 ModelAndView 객체를 반환해야한다.
이를 해결하기 위해 스프링은 ExceptionHandlerExceptionResolver 클래스의 @ExceptionHandler라는 강력한 Annotation을 제공한다.

<br><br>

## [@ExceptionHandler]
html을 제공할때는 알맞은 오류 페이지를 단순히 반환하면 되지만, API 호출의 경우 Exception을 처리하는 방식이 다양하고, 동일한 예외라도
컨트롤러마다 처리가 다를 수 있다. 외에도 다음과 같은 api 예외처리의 어려움이 존재한다.

1. `HandlerExceptionResolver`: API응답에 필요없는 ModelAndView 반환
2. `HandlerExceptionResolver`: HttpServletResponse에 직접 응답 데이터 삽입
3. 특정 컨트롤러에서만 발생하는 예외 별도 처리의 어려움

### `ExceptionHandlerExceptionResolver`의 @ExceptionHandler
이를 위해 스프링은 @ExceptionHandler라는 에외처리 어노테이션을 제공한다.   
스프링의 예외 처리를 위한 기본 ExceptionResolver 중에서 가장 우선순위가 높다.

### @ExceptionHandler 사용
- 어노테이션을 선언하고 해당 컨트롤러에서 처리할 예외를 지정한다.
  - 어노테이션의 속성으로 지정할 수 있고, 이를 생략하고 메소드 파라미터로 예외를 지정 할 수 도 있다.
  - 해당 예외 및 해당 예외의 자식 예외까지 잡을 수 있음
- 항상 자세한것이 우선순위를 갖는다.
  - RuntimException과 Exception을 처리하는 ExceptionHandler가 존재할때 RuntimeException이 발생하면 RuntimeException 처리기가 동작하고,
    Exception이 발생하면 Exception을 처리하는 처리기가 동작한다.
- 여러 예외를 한번에 처리할 수 있다.
  ```java
  @ExceptionHandler({AException.class, BException.class})
  public String ex(Exception e) {
  log.info("exception e", e);
  }
  ```
- 컨트롤러 파라미터 및 응답처럼 다양한 파라미터 및 응답을 지정할 수 있다.
  - 참고: https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-exceptionhandler-args

실제 예제 코드에 따른 동작은 pdf를 참고하자

<br><br>

## [@ControllerAdvice]
@ControllerAdvice를 이용하면 여러 컨트롤러에 @ExceptionHandler, @InitBinder 기능을 부여한다.
또한 여러개의 컨트롤러에 @InitBinder와 @ModelAttribute도 적용할 수 있다.

@RestControllerAdvice = @ResponseBody + @ControllerAdvice로 api 통신에서 사용된다.

### @ControllerAdvice 사용
- 특별한 대상을 지정하지 않으면 모든 컨트롤러에 적용된다.
- 특정 패키지의 모든 컨트롤러에 적용할 수 있다.
- 특정 컨트롤러 클래스에 적용할 수 있다.
- 자세한 사용법은 공식문서를 참고하자
  - https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-controller-advice

### 강의 질문에 대한 내 답변
- Q.
  - 안녕하세요. @ControllerAdvice로 예외처리를 하는 부분에서 궁금증이 생겨 질문남기게 되었습니다.
    @RequestBody를 통해 값을 받는 요청들은
    @ExceptionHandler(MethodArgumentNotValidException.class) 설정을 통해서 예외처리를 할 수 있는데, @ModelAttribute를 통해서 값을 받는 요청들은 ControllerAdvice에서 어떠한 Exception을 통해 걸러서 예외처리를 해야하는지 감이 잡히지 않습니다.
    @ModelAttribute를 통해 값을 받는 경우 어떠한 방식으로 공통예외처리를 할 수 있는 걸까요?
- A.
  - ModelAttribute의 바인딩 시점에 발생되는 오류시점에는 정수타입의 값이 입력되어야 하는곳에 문자열이 입력되는 부분에서 발생할 수 있습니다.(TypeMismatch)   
    실제로 쿼리스트링을 통해 int타입 값에 문자열을 입력하게되면 예외가 발생하지 않고 BeanPropertyBindingResult가 생성한 에러 메시지가 로그에 찍히게 됩니다.   
    이는 사실 직접적인 예외가 발생했다기 보다는 BeanPropertyBindingResult 객체가 예외를 먹고 예외에 관련된 정보를 로그로 출력해준다라고 생각하시면 될 것 같습니다.   
    스프링 공식문서에서는 기본적으로 @ModelAttribute에 대한 예외 처리는 BindingResult를 이용하여 처리하는 것을 권장하고 있습니다.   
    https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-modelattrib-method-args   
    더하여 강사님의 MVC1 BindingResult2 강의를 참고해보셔도 좋을것 같습니다!   
    > 직접적으로 @ModelAttribute의 예외를 캐치해서 @ExceptionHandler로 처리하는 방법은 찾아봐도 나오지 않아 잘 모르겠습니다만 BindingResult라는 좋은 객체가 존재하므로 해당 객체를 이용해도 좋을것 같다는 생각에 댓글 달아봤습니다!

<br><br>

# <스프링 타입 컨버터>
## [스프링 타입 컨버터 소개]
서블릿을 이용하게 되면 HttpServletRequest 객체의 getParameter를 통해 쿼리스트링 값을 접근할 수 있다.
이들은 모두 문자열 값으로 반환되므로 사용자가 원하는 타입으로 캐스팅하여 사용해왔다.

스프링은 이를 편리하게 하고자 중간에서 TypeConverting 과정을 지원해준다.
- 요청 파라미터, @RequestParam, @PathVariable, @ModelAttribute
- @Value 등으로 YML 정보 읽을때
- XML에 넣은 스프링 빈 정보를 변환
- 뷰 렌더링시   
와 같은 상황에서 타입 컨버팅을 지원해준다.

### 커스텀한 타입 컨버팅
Conveter<S, T>라는 인터페이스를 구현하면 S 타입을 T 타입으로 컨버팅하도록 커스텀하게 등록할 수 있다.
```java
public interface Converter<S, T> {
  T convert(S source);
}
```

> 과거에는 PropertyEditor를 이용해 타입을 변환했지만, 동시성 문제로 인해 타입 변환때마다 매번 객체를 생성해야 했다.
> 현재 Converter 인터페이스의 등장으로 해당 문제들은 해결됐다.

<br><br>

## [타입 컨버터 - Converter]
타입 컨버터를 사용하려면 `org.springframework.core.convert.converter.Converter`를 구현하면 된다.

실제 예제 코드를 참고하면 Converter의 간단한 convert 기능을 확인할 수 있다. 하지만, 직접 구현하여 사용하는 방식은 불편하다.   
스프링은 타입 컨버터를 등록하고 관리하며 편리하 변환 기능을 제공하기 위해 `ConversionService`기능을 제공한다.

### 용도에 따른 타입 컨버터
스프링은 용도에 따라 다양한 컨버터 인터페이스를 제공한다.
- Converter: 기본 타입 컨버터
- ConverterFactory: 전체 클래스 계층 구조가 필요할 때 사용되는 컨버터
- GenericConverter: 정교한 구현, 대상 필드의 애노테이션 정보 사용 가능
- ConditionalGenericConverter: 특정 조건이 참인 경우에만 실행

실제 위 인터페이스를 살펴보면 수많은 구현체가 존재한다.

> 공식 문서: https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#core-converter

<br><br>

## [컨버전 서비스 - ConversionService]
ConversionService는 개별 컨버터들을 모아서 편리하게 사용할 수 있도록 인터페이스를 제공해준다.

실제 ConversionService 인터페이스를 보면 canConvert()와 같이 컨버팅 할 수 있는지 확인 여부와 Convert()같은 실제 컨버팅을 위한
메소드가 존재한다.

### DefaultConversionService
ConversionService는 인터페이스이므로 그 자체로는 구현할 수 없다. 스프링에서는 해당 서비스의 구현체로 DefaultConversionService를 제공한다.
> 추가로 컨버터를 등록하는 ConversionRegistry 인터페이스도 구현하고 있다.

이렇게 두 개의 인터페이스는 각각 컨버터 사용과 등록에 초점이 맞춰져 있는 인터페이스이며 등록과 사용의 관심사를 분리할 수 있다.   
클라이언트 입장에선 등록하는 과정은 모르고 사용하는 방법만 관심이 있으므로 ISP를 만족한다.

### ConversionService Test
<img width="871" alt="스크린샷 2023-02-14 오후 4 05 33" src="https://user-images.githubusercontent.com/66772624/218665223-ca641c0e-ca03-48ca-86fe-330c9c9211e7.png">
실제 위의 테스트를 보면 DefaultConversionService에 우리가 만든 커스텀 컨버터를 등록하여 사용하고 있다.
특정 컨버터를 등록만 해놓으면 해당 컨버터가 컨버팅할 수 있는 데이터라면 컨버터가 동작한다.

각 컨버터의 로그를 통해 실제 커스텀 컨버터들이 동작함을 알 수 있다.
```java
15:30:46.745 [main] INFO hello.typeconverter.converter.IntegerToStringConverter - convert source=10
15:30:46.780 [main] INFO hello.typeconverter.converter.StringToIntegerConverter - convert source=10
15:30:46.782 [main] INFO hello.typeconverter.converter.StringToIpPortConverter - convert source=127.0.0.1:8080
15:30:46.782 [main] INFO hello.typeconverter.converter.IpPortToStringConverter - convert source=hello.typeconverter.type.IpPort@59cb0946
```

<br><br>

## [스프링에 Converter 적용하기]
이전 시간에는 DefaultConversionService를 생성하여 직접 등록하고 해당 DefafultConversionService의 인스턴스를 이용해 직접 컨버팅을 했다.   
스프링은 @RequestParam, @ModelAttribute, @PathVariable과 같은 곳에서는 자동으로 컨버전 서비스 빈을 이용해 데이터가 컨버팅 된다.

스프링은 내부적으로 수많은 컨버터 타입들을 미리 제공하고 있으므로 간단한 데이터(정수, 문자 등)에 대한 컨버터는 존재한다.   
하지만 IpPort와 같은 컨버팅 작업은 커스텀 컨버터를 등록해야 한다.

### 커스텀 컨버터 등록하기
WebMvcConfigurer 인터페이스를 구현한 WebConfig에서 addFormatters 메소드를 오버라이딩 한다.    
해당 메소드는 컨버터 혹은 포메터를 추가할때 사용된다.
![image](https://user-images.githubusercontent.com/66772624/218775549-617f5994-d013-482b-9075-39cd034854cf.png)

이후 registry 매개인자에 addConverter를 하여 이전 시간에 테스트 코드를 만들었던 것처럼 커스텀 컨버터를 등록한다.

![image](https://user-images.githubusercontent.com/66772624/218776026-e756304a-4077-4a46-a8e4-16bb30fa1044.png)

> 추가적으로 @RequestBody에서 가져오는 Json 데이터에 대해 StringToIpPortConverter가 등록할지 확인했지만,
> 기본적으로 @RequestBody는 ObjectMapper를 사용하는 HttpMessageConverter가 동작하므로 TypeConveter가 사용되지 않는다.

<br><br>

## [뷰 템플릿에 컨버터 적용하기]
뷰 템플릿에 데이터를 전달할떄도 컨버터를 적용할 수 있다.

### 테스트1 - 뷰 템플릿에서의 Converter 동작
<img width="733" alt="image" src="https://user-images.githubusercontent.com/66772624/218805672-e352f910-169d-4b98-98ec-5c98fdd1f654.png">
위와 같은 컨트롤러에서 Model에 값을 담고 View를 통해 값을 전달했을때 뷰 템플릿은 다음과 같다.

```html
  <li>${number}: <span th:text="${number}" ></span></li>
  <li>${{number}}: <span th:text="${{number}}" ></span></li>
  <li>${ipPort}: <span th:text="${ipPort}" ></span></li>
  <li>${{ipPort}}: <span th:text="${{ipPort}}" ></span></li>
```
Model에 담은 2개를 두 가지 방식으로 출력하고 있다. 여기서 하나의 중괄호는 변수에 대한 표현식이지만, 두 개의 중괄호는
자동으로 ConversionService를 호출해 변환된 결과를 출력한다. 실제 응답은 다음과 같이 나타난다.

```text
${number}: 10000
${{number}}: 10000
${ipPort}: hello.typeconverter.type.IpPort@59cb0946
${{ipPort}}: 127.0.0.1:8080
```
ipPort의 경우 변수에 대한 표현을 하게 되면 toString값이 출력되지만, 두 개의 중괄호는 컨버전 서비스를 통해 컨버팅한 결과를 나타낸다.
> 추가적으로 10000이 찍히는 부분은 컨버터를 적용할때와 하지 않을떄의 값이 동일한데 이는 타임리프에서 컨버터를 적용하지 않아도
> 자동으로 Integer -> String으로 변환하여 출력하기 때문이다.

<img width="600" alt="image" src="https://user-images.githubusercontent.com/66772624/218807090-2d5ca08e-32e9-4339-83ed-88e25b853c79.png">

실제 로그에서도 IpPortToStringConvert가 동작함을 알 수 있다.
```text
2023-02-15 02:07:38.452  INFO 91738 --- [nio-8080-exec-1] h.t.converter.IntegerToStringConverter   : convert source=10000
2023-02-15 02:07:38.452  INFO 91738 --- [nio-8080-exec-1] h.t.converter.IpPortToStringConverter    : convert source=hello.typeconverter.type.IpPort@59cb0946
```

### 테스트2 - 폼 데이터에서 Converter 동작
<img width="600" alt="image" src="https://user-images.githubusercontent.com/66772624/218807090-2d5ca08e-32e9-4339-83ed-88e25b853c79.png">

위와 같은 GET -> POST로 흐름이 이어지는 컨트롤러가 존재할때를 살펴보자   
우선 IpPort라는 값을 Form으로 감싸서 Model에 담고 View로 전송한다. View에서는 다음과 같이 값을 출력한다.
```html
<form th:object="${form}" th:method="post">
  th:field <input type="text" th:field="*{ipPort}"><br/>
  th:value <input type="text" th:value="*{ipPort}">(보여주기 용도)<br/> <input type="submit"/>
</form>
```
<img width="331" alt="image" src="https://user-images.githubusercontent.com/66772624/218809044-506c6e83-27bf-45b8-8ec5-bc79c89ed47b.png">

이때 ipPort에 대한 결과 값이 th:field에서는 IpPortToStringConverter를 통해 String으로 컨버팅된 값이 출력되지만, th:value는 toString값이 출력된다.   
이는 th:field에서 자동적으로 컨버전 서비스를 통해 컨버팅을 하기 때문이다.

이후 제출 버튼을 클릭하게 되면 POST로 현재 ipPort의 데이터가 전송되는데 이떄 @ModelAttribute를 통해 Form 객체를 만들때는
StringToIpPortConverter의 동작이 이루어지게 된다.

이후 POST시 Form 객체를 다시 Model에 담아 View를 호출하는데 이때도 IpPortToStringConverter가 동작한다.
따라서 위의 흐름은 총 3번의 컨버터를 호출하게 된다.
```text
2023-02-15 02:15:18.640  INFO 91738 --- [nio-8080-exec-7] h.t.converter.IpPortToStringConverter    : convert source=hello.typeconverter.type.IpPort@59cb0946
2023-02-15 02:15:21.911  INFO 91738 --- [nio-8080-exec-9] h.t.converter.StringToIpPortConverter    : convert source=127.0.0.1:8080
2023-02-15 02:15:21.913  INFO 91738 --- [nio-8080-exec-9] h.t.converter.IpPortToStringConverter    : convert source=hello.typeconverter.type.IpPort@59cb0946
```

<br><br>

## [포맷터 Formatter]
Converter는 입력 출력 타입에 제한이 없는, 범용 타입 변환기능을 제공한다.   
하지만 주로 문자 -> 타입 변환 혹은 타입 -> 문자 변환의 상황이 주가 되는 입장에선 컨버터는 더 많은 기능을 제공한다.   
이런 점을 조금 간편하게 지원하는 Formatter가 존재한다.

### Formatter
금액과 같은 입력에서 1000 -> "1,000"으로 출력하거나 반대의 상황,   날짜 객체를 문자로 변환하는 역할은 포맷터가 맡고있다.   
추가적으로 Locale과 같은 나라 정보를 추가하면 현지 정보를 다룰 수 있다.

결국 Converter는 객체 to 객체의 역할을 Formatter는 문자 -> 객체 혹은 반대상황의 역할을 맡는 Converter의 특별한 버전이라고 생각할 수 있다.

Formatter 인터페이스는 Printer와 Parser 인터페이스를 상속받는데 이는 각각 객체 -> 문자 변환과 문자 -> 객체 변환의 역할을 한다.

### CustomFormatter: MyNumberFormatter
<img width="715" alt="image" src="https://user-images.githubusercontent.com/66772624/219311639-afd129a4-6eb8-4b69-ac84-3c1fddb6939a.png">
위와 같은 포맷터는 다음과 같이 사용할 수 있다.

<img width="562" alt="image" src="https://user-images.githubusercontent.com/66772624/219312021-fa60e181-4287-4136-a166-0fbaa9355976.png">

> **참고**   
> 스프링은 다양한 포맷터를 용도에 따라 제공한다.
> `Formatter`: 포맷터
> `AnnotationFormatterFactory`: 필드 타입, 애노테이션 정보를 활용할 수 있는 포맷터    
> **공식문서**: https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#format

<br><br>
  
## [포맷터를 지원하는 컨버전 서비스]
`DefaultFormattingConversionService`를 이용하면 포맷터를 지원하는 컨버전 서비스를 사용할 수 있다. 이 둘은 어찌보면 성격이 비슷하기에
이렇게 합쳐서 지원한다.

<img width="1099" alt="image" src="https://user-images.githubusercontent.com/66772624/219322998-42cd3fef-1053-4b56-98d9-c699b1d54767.png">

위와 같은 테스트를 이용하면 실제 컨버터와 포맷터를 같이 사용할 수 있다.

### FormattingConversionService 인터페이스
DefaultFormattingConversionService는 FormattingConversionService 인터페이를 구현한다. 해당 인터페이스는 다음과 같이 포맷터와 컨버터를 사용할 수 있게 구성되어 있다.
<img width="612" alt="image" src="https://user-images.githubusercontent.com/66772624/219323917-45cbe664-83a8-49e7-b257-5c32de753859.png">

스프링 부트는 DefaultFormattingConversionService를 상속받는 WebConversionService를 내부에서 사용한다.
<img width="1800" alt="image" src="https://user-images.githubusercontent.com/66772624/219329961-5f592328-15cb-4d3c-bc06-0686d3dca437.png">

<br><br>

## [포맷터 적용하기]
포맷터를 적용할때는 컨버터와 동일하게 적용할 수 있다. (WebConfig의 addFormatters 메소드에서 addFormatter로 추가할 수 있다.)

이떄 StringToInteger 및 IntegerToString 컨버터를 등록한 부분을 등록하지 않도록 주석처리하는데
우리가 생성한 MyNumberFormatter와 동일하게 문자 -> 숫자 변환 혹은 반대 변환을 하기 때문인데 이런 경우에는 컨버터가 더 높은 우선순위를 갖기에 주석처리한다.

### 컨트롤러에서 확인
<img width="749" alt="image" src="https://user-images.githubusercontent.com/66772624/219334880-5ab1acc9-dc03-4576-98fb-fa5cd6dbfe3f.png">

위의 컨트롤러를 실행하면 기존 StringToInteger 컨버터가 아닌 MyNumberFormatter가 동작하므로 다음과 같이 포맷팅되어 출력된다.

<img width="402" alt="image" src="https://user-images.githubusercontent.com/66772624/219335138-1aa11e5e-24b2-415e-aaed-96f04cc5c49e.png">

또한 다음 컨트롤러에서 파라미터로 10,000을 넘기게 되어도 포맷터가 정상 동작한다.
<img width="439" alt="image" src="https://user-images.githubusercontent.com/66772624/219335261-08b50831-d39c-4add-bf76-41684f196d0a.png">
<img width="1002" alt="image" src="https://user-images.githubusercontent.com/66772624/219335395-67f6c750-6256-465f-acbd-3cb348bc7040.png">

<br><br>

## [스프링이 제공하는 기본 포맷터]
스프링은 다양한 포맷터를 기본적으로 제공한다.

포맷터의 기본형식이 아닌 원하는 형식을 지정해서 사용할 수 있는 방법으로 어노테이션 기반 방법이 존재한다.   
실제 개발을 하다보면 날짜 및 시간이나 숫자에 대한 포맷팅이 필요한데 어노테이션을 이용해 간단하게 지정할 수 있다.

<img width="526" alt="image" src="https://user-images.githubusercontent.com/66772624/219346883-a3a4bbe4-1d7e-4bbc-8277-392bde8b353c.png">

Form 클래스를 보면 @NumberFormat, @DateTimeFormat이 보인다. 이것이 실제 포맷터에 대한 포맷팅을 어노테이션으로 설정하는 것이다.   

`GET /formatter/edit` Integer -> String 및 LocalDateTime -> String 포맷팅이 완료됨

<img width="280" alt="image" src="https://user-images.githubusercontent.com/66772624/219347263-03786794-7b11-464a-9a8a-59e459b6edeb.png">

`POST /formatter/edit` String -> Integer 및 String -> LocalDateTime으로 포맷팅 된다.
<img width="369" alt="image" src="https://user-images.githubusercontent.com/66772624/219347305-a474ed1f-83ef-41b5-9a38-9429bc0bbea8.png">

> 포맷터 인터페이스를 보면 알겠지만, print 및 parser 메소드를 오버라이딩 하므로 a -> b 포맷팅이 된다면 b -> a 포맷팅도 가능하다.


### 추가 API 통신에서 Formatting?
<img width="493" alt="image" src="https://user-images.githubusercontent.com/66772624/219363984-989f2077-44f8-4368-946d-2f8b3490f566.png">

위와 같은 Form2 클래스를 이용해 api 통신에서의 Formatting이 이루어지는지 확인해보자

`GET /api/formatter/edit`

- 결과
  ```json
  # 기대결과
  {
    "number": "10,000",
    "localDateTime": "2023-02-16 21:22:47"
  }
  # 실제 결과
  {
  "number": 10000,
  "localDateTime": "2023-02-16T21:22:47.456561"
  }
  ```

`POST /api/formatting/edit`   
```json
body
{
  "number": 10000,
  "localDateTime": "2023-02-16T21:22:47.456561"
}
```
- 결과
  ```json
  # 기대 결과
  {
    "number": "10,000",
    "localDateTime": "2023-02-16 21:22:47"
  }
  # 실제 결과
  {
  "number": 10000,
  "localDateTime": "2023-02-16T21:22:47.456561"
  }
  ```
  
결과적으로 Formatting의 적용이 되지 않는 모습이다. 하지만 `@JsonFormat`을 이용한다면 JSON 데이터에 날짜 및 시간에 대한 포맷팅을 적용할 수 있다.
<img width="425" alt="image" src="https://user-images.githubusercontent.com/66772624/219366023-a4769ef0-bc2f-4673-a5bb-93fa3079af8f.png">

Form2에 다음과 같이 `@JsonFormat`을 붙여주고 다시한번 위의 POST를 요청하게 되면 Json Parse Error가 발생한다.      
이유는 `@JsonFormat`의 경우 이미 변환되어 있는 데이터를 해당 포맷에 맞게 담거나, 담겨 있는 데이터를 해당 포맷에 맞게 응답하기 때문이다.

즉, `2023-02-16T21:22:47.456561`과 같은 형식을 `2023-02-16 21:22:47`와 같이 변환 하기는 어렵지만, 해당 변환할 포맷을 미리 @JsonFormat으로 붙여놓으면   
`2023-02-16 21:22:47` 방식을 `localDateTime` 변수에 담을 수 있게 된다.

반대로 응답을 주는 부분에서도 포맷팅을 지정하면 기본 응답인 `2023-02-16T21:22:47.456561`이 아니라 `2023-02-16 21:22:47`와 같이 응답하게 된다.
![image](https://user-images.githubusercontent.com/66772624/219367297-e088111a-a508-4be8-8386-b8f27105bd8f.png)

<br><br>

## [정리]
메시지 컨버터(`HttpMessageConverter`)에는 ConversionService가 적용되지 않음
JSON결과로 만들어지는 숫자, 날짜 포맷 변경을 원하면 Jackson 라이브러리가 제공하는 설정에 포맷을 지정해야 한다.

ConversionService는 `@RequestParam`, `@ModelAttribute`, `@PathVariable`, `뷰 템플릿`에서 사용된다.

<br><br>
# <파일 업로드>
## [파일 업로드 소개]
폼 데이터를 전송하는 방식은 2가지가 존재한다.

### application/x-www-form-urlencoded 방식
폼 데이터를 서버로 전송하는 기본적인 방식으로 헤더에 `Content-Type: application/x-www-form-urlencoded`가 추가된다.

폼 데이터는 Http Body에 문자로 전송되며 username=kim&age=20과 같이 &로 구부하여 전송된다.

### multipart/form-data 방식
<img width="801" alt="image" src="https://user-images.githubusercontent.com/66772624/219600278-d6c695d3-80ee-464c-88d3-d9cf3d532649.png">

해당 방식은 다른 종류의 여러 파일과 폼 데이터를 같이 전송할 수 있다. 해당 방식을 사용하기 위해서는 form에 별도의 속성인 enctype을 지정한다.   
또한 Content-Type의 boundary=---xxx와 같은 문자로 각 폼의 데이터가 구분되며 각 폼은 Content-Disposition 항목별 헤더가 존재해 이를 통해 폼 데이터인지, 파일 데이터인지 구분할 수 있다. 
따라서 이들을 multi part라고 부른다.

여러개의 복잡한 part가 존재하는 HTTP 메시지를 어떻게 사용할까?

<br><br>

## [서블릿과 파일 업로드1]
<img width="260" alt="image" src="https://user-images.githubusercontent.com/66772624/219634343-a9762af8-7dfd-41bd-b435-28afb1993763.png">

위와 같이 파일을 전송하기 위해서는 form 태그에 enctype을 "multipart/form-data"로 지정해주어야 한다.

컨트롤러 코드는 다음과 같다.
![image](https://user-images.githubusercontent.com/66772624/219634919-b98e40d6-d778-40c3-b385-e81e16ec65db.png)

받아온 폼 데이터 및 파일 데이터를 로그로 찍는데 request.getParts()는 실제 multipart/form-data 전송 방식에서 각각 나누어진 부분을 받아서 확인할 수 있다.
여기서는 2개가 올것을 예상할 수 있다.

### application.properties에서 HTTP 요청 메시지 보이도록 설정
`logging.level.org.apache.coyote.http11=debug`

실제 requeste를 보면 다음과 같다.
```
Received [POST /servlet/v1/upload HTTP/1.1
Host: localhost:8080
Connection: keep-alive
Content-Length: 290
Cache-Control: max-age=0
sec-ch-ua: "Not_A Brand";v="99", "Google Chrome";v="109", "Chromium";v="109"
sec-ch-ua-mobile: ?0
sec-ch-ua-platform: "macOS"
Upgrade-Insecure-Requests: 1
Origin: http://localhost:8080
Content-Type: multipart/form-data; boundary=----WebKitFormBoundaryNASNHHrqfM800BHQ
User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: navigate
Sec-Fetch-User: ?1
Sec-Fetch-Dest: document
Referer: http://localhost:8080/servlet/v1/upload
Accept-Encoding: gzip, deflate, br
Accept-Language: ko,en-US;q=0.9,en;q=0.8,ja;q=0.7
Cookie: Idea-5ba2b975=4d2b9ec8-6fb6-4ef5-b682-bb1b965ac1d0

------WebKitFormBoundaryNASNHHrqfM800BHQ
Content-Disposition: form-data; name="itemName"

ìíB
------WebKitFormBoundary8mSgBeXcRgcWL3at
Content-Disposition: form-data; name="file"; filename="áá¬áá©áá©á¨ áá¥á·áá¦ááµá¯.001.jpeg"
Content-Type: image/jpeg

ÿØÿà�JFIF���H�H��ÿá�@Exif��MM�*���i������������ ������ ������8����ÿí�8Photoshop 3.0�8BIM������8BIM%�����ÔÙ�²é	ìøB~ÿâ(ICC_PROFILE���appl���mntrRGB XYZ æ��������acspAPPL����APPL������������������öÖ�����Ó-applìý£8GÃm´½OzÚ/�������������������������������
desc���ü���0cprt��,���Pwtpt��|���rXYZ�����gXYZ��¤���bXYZ��¸���rTRC��Ì��� chad��ì���,bTRC��Ì��� gTRC��Ì��� mluc����������enUS�������D�i�s�p�l�a�y� �P�3mluc����������enUS���4����C�o�p�y�r�i�g�h�t� �A�p�p�l�e� �I�n�c�.�,� �2�0�2�2XYZ ������öÕ�����Ó,XYZ ������ß��=¿ÿÿÿ»XYZ ������J¿��±7��
Y��Ð��
[sf32�����B��Þÿÿó&����ýÿÿû¢ÿÿý£��Ü��ÀnÿÀ�8�ÿÄ�����������	
ÿÄ�µ���}�!1AQa"q2#B±ÁRÑð$3br	
%&'()*456789:CDEFGHIJSTUVWXYZcdefghijstuvwxyz¢£¤¥¦§¨©ª²³´µ¶·¸¹ºÂÃÄÅÆÇÈÉÊÒÓÔÕÖ×ØÙÚáâãäåæçèéêñòóôõö÷øùúÿÄ��������	
ÿÄ�µ��w�!1AQaq"2B¡±Á	#3RðbrÑ
$4á%ñ&'()*56789:CDEFGHIJSTUVWXYZcdefghijstuvwxyz¢£¤¥¦§¨©ª²³´µ¶·¸¹ºÂÃÄÅÆÇÈÉÊÒÓÔÕÖ×ØÙÚâãäåæçèéêòóôõö÷øùúÿÛ�C�ÿÛ�CÿÝ��ðÿÚ���?�þ!ëØ<ð 
�(� 
�(� 
�(� 



------WebKitFormBoundaryNASNHHrqfM800BHQ--
]
```
이렇게 단순 form 데이터와 파일데이터가 동시에 전송됨을 알 수 있다.

### 멀티 파트 관련 application.properties 설정
1. 업로드 사이즈 제한
   ```
   spring.servlet.multipart.max-file-size=1MB # 파일 하나의 최대 사이즈 설정
   spring.servlet.multipart.max-request-size=10MB # 멀트파트 요청 하나에 여러 파일 업로드시 전체 합 사이즈 설정
   ```
2. 멀티파트 전송 끄고 켜기   
   멀티 파트 전송은 기본 application/x-www-form-urlencoded보다 복잡하므로 옵션으로 관련된 처리를 하도록하거나 하지 못하도록 설정할 수 있다.
   ```
   spring.serlvet.multipart.enabled=false # 기본은 켜기임 (true)
   ```

### 멀티파트 전송시 Request 객체
기본 form 데이터에서 Request 객체는 RequestFacade의 인스턴스이다. 하지만 multipart 전송을 하게되면 StandardMultipartHttpServletRequest
인스턴스가 기본 Request 객체로 들어옴을 알 수 있다.

멀티파트로 전송하게 되면 스프링의 `DispatcherServlet`에서 `MultipartResolver`를 실행한다. 실제 DispatcherServlet의 `doDispatch()` 메소드에서
`checkMultipart()` 메소드의 실행부분을 디버깅해보면 다음과 같이 MultipartResolver를 구현한 `StandardServletMultipartResolver`에서 `StandardMultipartHttpServletRequest`객체를 만들어 반환한다.
![image](https://user-images.githubusercontent.com/66772624/219640543-6d8620b4-f3ff-479a-9961-58916de9309f.png)

<br><br>

## [서블릿과 파일 업로드2]
이전 시간에 파일을 전송하고 로그를 찍었던 POST Controller 부분에 다음과 같이 코드를 추가한다.
<img width="787" alt="image" src="https://user-images.githubusercontent.com/66772624/219647684-a66050de-bb97-408b-8ded-bfad2b42c43c.png">

추가된 부분은 각 part를 돌면서 `이름`, `헤더`, `Content-Disposition` 포함 헤더 및 실제 데이터를 읽어와 우리가 지정한 경로에 파일을 저장한다.

fileDir은 application.properties에서 지정할 수 있다.   
`file.dir=/Users/ihoseok/develop/tools/file/` 이후 @Value(${file.dir})을 통해 해당 값을 불러올 수 있다.

### Part의 주요 메서드들
1. part.getSubmittedFileName(): 클라이언트가 전달한 파일명
2. part.getInputStream(): Part의 전송 데이터를 읽을 수 있다.
3. part.write(String path): Part를 통해 전송된 데이터를 저장할 수 있다.

### 브라우저를 통해 전송한 데이터 로그
<img width="579" alt="image" src="https://user-images.githubusercontent.com/66772624/219649358-36d4af9e-9f2c-4a59-a2bc-f489bba29c08.png">

위와 같이 데이터를 전송하게 되면 다음과 같은 로그가 찍힘을 알 수 있다.

<img width="1674" alt="image" src="https://user-images.githubusercontent.com/66772624/219649458-1b4b6c8f-43da-4781-958b-af8621d7885d.png">

더하여 서블릿이 제공하는 Part도 편리하지만 HttpServletRequest에 의존하게 되며, 파일 부분을 구현하는 코드의 양이 꽤 많다.   
스프링을 사용하면 이런 부분들을 대폭 줄일 수 있다.

<br><br>

## [스프링과 파일 업로드]
스프링은 파일 업로드를 MultipartFile이라는 인터페이스로 쉽게 제공해준다.

![image](https://user-images.githubusercontent.com/66772624/220063839-a4bd0224-4a55-4639-aecb-36df8e4d3ba9.png)

HttpServletRequest에서 직접 가져오지 않고 @RequestParam을 통해 MultipartFile을 직접 주입받아서 사용할 수 있다.
이후 저장할 fullPath에 transferTo() 메서드를 이용해 파일을 저장한다.

> @RequestParam뿐만 아니라 @ModelAttribute에서도 MultipartFile을 주입받을 수 있다.   
> 서블릿을 직접 이용하기보단 이렇게 파일을 직접 주입받게되면 훨씬 코드의 양이 줄어들고 명료해진다.

### @ModelAttribute를 통해 MultipartFile 주입받기
<img width="633" alt="image" src="https://user-images.githubusercontent.com/66772624/220066297-8c4b2647-33d4-45bc-815e-70a679434af3.png">

<br><br>

## [예제로 구현하는 파일 업로드, 다운로드]
### 요구사항
- 상품을 관리 
  - 상품 이름
  - 첨부파일 하나
  - 이미지 파일 여러개
- 첨부파일을 업로드 다운로드 할 수 있다.
- 업로드한 이미지를 웹 브라우저에서 확인할 수 있다.

### 상품을 관리
> 첨부 파일을 저장하기에 앞서서 중요한 사실이 있다. 파일은 주로 데이터베이스에 저장하지 않고 `스토리지`에 저장한다. (ex: AWS S3)   
따라서 DB에는 파일을 저장한 경로를 저장함 (FullPath를 다 하진 않고, 어딘가에 기본적으로 맞춰놓고 이후 상대적인 경로만 저장한다)

- 클라이언트에서 받아오는 ItemForm   
    ![image](https://user-images.githubusercontent.com/66772624/220178349-ced59b69-6105-46ae-96e5-abf853467934.png)

- 실제 DB 저장용 Item   
    ![image](https://user-images.githubusercontent.com/66772624/220178574-0c04f3ee-f960-48fc-b68f-e9d3b03d0fa1.png)

- 첨부파일이 실제 저장될때는 업로드한 파일명과 구분되고 중복을 방지하기 위해 다르게 저장한다.   
    ![image](https://user-images.githubusercontent.com/66772624/220178852-afd65602-a533-4fe2-9120-c5e28fa38b9f.png)

### 첨부파일 및 여러 이미지 업로드

- 첨부파일 업로드   
    ![image](https://user-images.githubusercontent.com/66772624/220179016-2585b5f2-619d-447c-9846-e4fe7fce77eb.png)

    FileStore 객체에게 파일을 저장하는 로직일 위임하고, 파일의 첨부 이름, 실제 저장이름을 가진 UploadFile 객체를 생성하고, Item을 저장한다.


- FileStore 로직
  ![image](https://user-images.githubusercontent.com/66772624/220179489-350c8c14-900a-4e61-8d1e-97a1ea6bc919.png)

   storeFile 메소드가 핵심이다. 결국 fullPath 경로에 실제 파일인 MultipartFile이 업로드되고, 반환은 UploadFile객체가 반환된다.


### 첨부파일 다운로드 및 이미지 확인
![image](https://user-images.githubusercontent.com/66772624/220180349-20a0e211-2ed1-4e45-8fae-9d9355fd6c48.png)

첨부파일 업로드 이후 `/item/{id}`으로 리다이렉트 됐다. 여기서는 실제 저장된 item을 모델에 담고 반환한다.


**첨부 파일 업로드**
- **item-view.html**
    ![image](https://user-images.githubusercontent.com/66772624/220180580-f1ec4061-27ef-4b30-ac63-90923bb7cea6.png)
    <img width="245" alt="image" src="https://user-images.githubusercontent.com/66772624/220180762-bcdb8f4d-9b8a-48ad-9354-bf43521b401a.png">

    하이퍼링크로 /attach/${item.id}를 호출한다 따라서 클릭하게 되면 첨부파일에 대한 다운로드가 진행되어야 한다.

다시 컨트롤러에서 `/attach/{itemId}`를 호출하는 컨트롤러를 보면 `fullPath`를 가져와서 UriResource를 통해 실제 해당 위치의 리소스를 가져온다.   
이후 사용자가 업로드한 파일 이름으로 보이도록 인코딩한 후 `Content-Disposition` 헤더에 파일 이름을 저장하고 바디에는 리소스를 응답해   
사용자가 링크를 누르면 다운로드 할 수 있도록 한다.

> 추가적으로 itemId로 조회하게 되면 사용자의 권한이 필요한 경우 해당 id값을 db에 조회해 권한 확인 후 조회할 수 있게 기능을 추가할 수 있다.

**이미지 확인**
html코드에서 img를 보면 th:src부분에 `/images/${imageFile.getStoreFileName()}`을 호출한다. 컨트롤러에선 해당 fullpath에 존재하는 UriResource를 응답하여 이미지를 볼 수 있게한다.

> 이미지 확인은 단순하게 리소스만 응답했지만, 실제라면 특정 권한을 체크하는 로직이 추가될 수 도 있다.