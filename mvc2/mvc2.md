# <타임리프 - 기본 기능>
## [타임리프 소개]
공식문서 잘보자!!

타임리프
* SSR, 네추럴 템플릿, 스프링 통합 지원

타임리프 사용 선언
* `<html xmlns:th="http//www.thymeleaf.org">`

기본 표현식
* 간단한 표현:
  * 변수 표현식: `${...}`
  * 선택 변수 표현식: `*{...}`
  * 메시지 표현식: `#{...}`
  * 링크 URL 표현식: `@{...}`
  * 조각 표현식: `~{...}`
* 리터럴
  * `텍스트`: 'one text', 'Another one!',…
  * `숫자`: 0, 34, 3.0, 12.3,…
  * `불린`: true, false
  * `널`: null
  * `리터럴 토큰`: one, sometext, main,…
* 문자 연산:
  * 문자 합치기: `+`
  * 리터럴 대체: `|The name is ${name}|`
* 산술 연산:
  * Binary operators: `+`, `-`, `*`, `/`, `%`
  * Minus sign (unary operator): `-`
* 불린 연산:
  * Binary operators: `and`, `or`
  * Boolean negation (unary operator): `!`, `not`
* 비교와 동등:
  * 비교: `>`, `<`, `>=`, `<=` (gt, lt, ge, le)
  * 동등 연산: `==`, `!=` (eq, ne)
* 조건 연산:
  * If-then: `(if) ? (then)`
  * If-then-else: `(if) ? (then) : (else)`
  * Default: `(value) ?: (defaultvalue)`
* 특별한 토큰:
  * No-Operation: `_`

<br><br>

## [텍스트 - text, utext]
타임리프의 가장 기본 기능인 텍스트를 출력하는 기능

* HTML에 콘텐츠에 데이터 출력할 때 이용   
  * `<span th:text="${data}"></span>`   
* 콘텐츠 영역안에 직접 데이터 출력
  * `<span>컨텐츠 안에서 직접 출력 = [[${data}]]</span>`

<br>

**Escape**   
HTML은 <, >를 이용해 태그를 정의한다. 따라서 이러한 문자가 뷰 템플릿에 데이터를 전달할 때 주의해야 한다.

만약 `model.attribute("data", "hello <b>spring!</b>");` 으로 spring이라는 문구를 강조하고 싶어 저렇게 보내게 되면
실제로는 태그 자체가 문자로 찍히게 된다.   
> 소스보기를 하면 < 부분이 &lt; 로 변경

<br>

**HTML 엔티티**   
웹 브라우저는 `<` 를 태그의 시작으로 인식하기 때문에, 데이터에 `<`, `>`문자가 들어오게 되면 태그의 시작이 아니라   
문자로 인식하기 위해 `HTML 엔티티`로 변경한다. 또한, 이러한 HTML 엔티티로의 변경하는 것을 `이스케이프`라 말함   
> 타임리프 제공 태그 th:text, [[...]]은 기본적으로 이스케이프를 제공   

만약 이스케이프 기능을 사용하고 싶지 않다면 아래와 같이 변경하여 사용한다.
* th:text -> th:utext
* [[...]] -> [(...)]

> 실제 서비스 개발시 escape 미사용으로 인해 HTML의 비정상 렌더링이 발생하기도 함, 따라서 escape를 기본으로 두고   
> 꼭 필요한 곳에서만 unescape를 사용

<br>

> **참고**   
> **th:inline="none"** : 타임리프는 [[...]] 를 해석하기 때문에, 화면에 [[...]] 글자를 보여줄 수 없다.
이 테그 안에서는 타임리프가 해석하지 말라는 옵션이다

<br><br>

## [변수 - SpringEL]
타임리프에서 변수를 사용할 때는 변수 표현식을 사용한다.   
**변수 표현식**: `${...}`

해당 변수 표현식에는 스프링 EL이라는 스프링이 제공하는 표현식을 사용할 수 있다.

### Spring EL
**Object**
* `user.username`: 자바빈 프로퍼티 접근법을 이용해 접근 (`user.getUsername()`)
* `user['username']`: 위와 같음 -> `user.getUsername();`
* `user.getUsername()`: getUsername() 메소드 직접 호출

**List**
* `users[0].username` -> List 첫번째 요소에서 자바 빈 프로퍼티 접근 (`list.get(0).getUsername()`)
* `users[0]['username']`: 동일
* `users[0].getUsername()`: List 첫번째 요소에서 getUsername() 메소드 직접 호출

**Map**
* `userMap['userA'].username`: 키를 이용해 찾은 유저를 자바 빈 프로퍼티 접근
* `userMap['userA']['username']`: 위와 같음
* `userMap['userA'].getUsername()`: Map에서 찾은 객체의 메소드 직접 호출

<br><br>

## [기본 객체들]
타임리프는 기본 객체들을 제공함
* `${#request}`
* `${#response}`
* `${#session}`
* `${#servletContext}`
* `${#locale}`

HttpServletRequest의 경우 getParameter()를 이용해 데이터를 조회하므로 좀 더 편리한 편의 객체도 제공함   
* HTTP 요청 파라미터: `param`
  * `${param.paramData}`
* HTTP 세션 접근: `session`
  * `${session.sessionData}`
* 스프링 빈 접근: `@`
  * `${@helloBean.hello('Spring!')}`

<br><br>

## [유틸리티 객체와 날짜]
타임리프는 문자, 숫자, 날짜, URI등을 편리하게 다루는 다양한 유틸리티 객체들을 제공함타임리프 유틸리티 객체들
* `#message` : 메시지, 국제화 처리
* `#uris` : URI 이스케이프 지원
* `#dates` : java.util.Date 서식 지원
* `#calendars` : java.util.Calendar 서식 지원
* `#temporals` : 자바8 날짜 서식 지원
* `#numbers` : 숫자 서식 지원
* `#strings` : 문자 관련 편의 기능
* `#objects` : 객체 관련 기능 제공
* `#bools` : boolean 관련 기능 제공
* `#arrays` : 배열 관련 기능 제공
* `#lists` , `#sets` , `#maps` : 컬렉션 관련 기능 제공
* `#ids` : 아이디 처리 관련 기능 제공, 뒤에서 설명

필요할때 찾아서 사용하자! (타임리프 공식문서 `Utility Objects`)

자바8 날짜를 사용하려면(`LocalDate`, `LocalDateTime`, `Instant`) 라이브러리를 따로 추가해야 한다.   
하지만, 스프링 부트 타임리프 이용시 자동으로 라이브러리가 추가되고 통합됨

<br><br>

## [URL 링크]
타임리프에서 URL을 생성할때는 `@{...}`을 이용

* 단순 URL
  * `@{/hello}` -> `/hello`
* 쿼리 파라미터
  * `@{/hello(param1=${param1}, param2=${param2})}` -> `/hello?param1=data1&param2=data2`
  * `()` 사이에 있는 부분이 쿼리 파라미터로 처리
* PathVariable(경로변수)
  * `@{/hello/{param1}/{param2}(param1=${param1}`, `param2=${param2})}` -> `/hello/data1/data2`
  * URL 경로에 변수가 있으면 ()부분이 경로 변수로 처리
* 경로변수 + 쿼리 파라미터 같이 사용
  * `@{/hello/{param1}(param1=${param1}, param2=${param2})}` -> /hello/data1?param2=data2

<br><br>

## [리터럴]
소스 코드상에 고정된 값을 말하는 용어

타임리프는 `문자`, `숫자`, `불린`, `null` 리터럴을 지원한다.

타임리프에서 문자 리터럴은 항상 작은 따옴표로 감싸거나 리터럴 대체(`|,|`)를 이용한다.   
(공백이 없다면 따옴표 생략이 가능하지만 공백이 있다면 무조건 감싸줘야함)

<br><br>

## [연산]
타임리프의 연산은 자바의 연산과 유사하나, HTML안에서 사용하므로 HTML 엔티티를 사용해야한다.

* 비교
  * `> (gt)`, `< (lt)`, `>= (ge)`, `<= (le)`, `! (not)`, `== (eq)`, `!= (neq, ne)`
  * 괄호로 감싼 부분이 HTML 엔티티
  * `조건식`: 자바의 조건식과 유사(3항 연산자)
  * `Elvis 연산자`: 조건식의 편의 버전
  * `No-Operation`: Elvis에서 `?:` 뒤에 값이 `_`라면 타임리프가 실행되지 않는것처럼 동작하여, HTML내용 그 자체를 보여준다.(코드 확인)

<br><br>

## [속성 값 설정]
**타임리프 태그 속성 설정(Attribute)**   
* 주로 HTML 태그에 `th:*` 속성 지정하는 방식으로 동작, 이는 기존 속성을 대체하고 없다면 새로 만든다.

**속성 추가**   
* `th:attrappend` : 속성 값의 뒤에 값을 추가한다.   
* `th:attrprepend` : 속성 값의 앞에 값을 추가한다.   
* `th:classappend` : class 속성에 자연스럽게 추가한다.(띄어쓰기 포함한 편의 기능)   

**checked 처리**   
* 기본 HTML은 checked 속성이 있다면 해당 값이 true, false 상관 없이 자동적으로 checked 처리가 된다.   
이를 편리하게 사용하기 위해 `th:checked`이용
* 만약 th:checked의 값이 false라면 checked 속성 자체를 삭제해 자동으로 checked 되는것을 막아준다.

<br>

## [반복]
`th:each`를 이용한 반복문

**반복기능**   
`<tr th:each="user : ${users}">`: 컬렉션에서 값을 하나씩 꺼내어 user에 담아 반복문을 돈다.   
List 및 배열 혹은 Java.util.Iterable, java.util.Enumeration을 구현한 모든 객체를 사용할 수 있음   
(Map도 사용가능 Map은 Map.Entry가 담긴다)

**반복 상태 유지**   
`<tr th:each="user, userStat : ${users}">`   
두 번째 파라미터로 반복의 상태를 확인할 수 있다. 이는 생략가능하며 생략시 지정 변수명 + 'Stat'으로 이름이 정해진다.

**반복 상태 종류**
* index: 0부터 시작함
* count: 1부터 시작
* size: 전체 사이즈
* even, odd: 홀 짝
* first, last: 처음 끝
* current: 현재 객체

<br>

## [조건부 평가]
**if, unless**   
`th:if`, `th:unless`(if의 반대)   
타임리프는 위의 식을 이용해 해당 조건이 맞지 않으면 태그 자체를 렌더링하지 않음   
즉, 조건이 false면 html 태그 자체가 렌더링 되지 않음   
`<span th:text="'미성년자'" th:if="${user.age lt 20}"></span>`

**switch**   
`*`은 만족하는 조건이 없을 떄 default 값 

<br>

## [주석]
**1. 표준 HTML 주석 \<!-- 내용 -->**   
JS 표준 HTML 주석은 타임리프가 렌더링 하지 않고 남겨둠

**2. 타임리프 파서 주석 \<!--/\* 내용 \*/-->**   
타임리프에서의 주석, 렌더링시 해당 주석 부분 제거함

**3. 타임리프 프로토 타입 주석 \<!--/\*/ 내용 /\*/-->**   
HTML 주석 + 약간의 구문   
렌더링 전 정적 파일을 열면 웹 브라우저가 렌더링하지 않지만   
타입리프 렌더링을 거치면 이 부분이 정상 렌더링 됨

<br>

## [블록]
`<th:block>` 은 HTML 태그가 아닌 타임리프의 유일한 자체 태그이다.

타임리프는 HTML 태그 안에서 속성으로 기능을 사용하는데,    
만약 div 태그 2개를 반복문을 돌려야 하는 애매한 상황인 경우에 th:block 태그로 이 둘을 묶어서 block 태그안에 th:each를 사용할 수 있다.   
이렇게 되면 실제 렌더링 이후에는 `<th:block>`은 제거된다.

<br>

## [자바스크립트 인라인]
타임리프에선 자바스크립트에서 타임리프를 편리하게 사용할 수 있는 자바스크립트 인라인 기능을 제공한다.   
다음과 같이 적용 `<script th:inline="javascript">`

### **자바스크립트 인라인의 이점**   
1. **텍스트 렌더링**
   * 텍스트 렌더링으로 `var username = [[${user.username}]];` 값을 넘기게 되면 다음과 같이 동작
   * 인라인 사용전: `var username = userA;`
   * 인라인 사용후: `var username = "userA";`

문자일 경우에 자동으로 쌍따옴표를 붙여주고, 만약 숫자라면 숫자 그대로 렌더링 한다.   
만약 문제가 될 수 있는 "," 같은 문자들은 자동적으로 이스케이프 처리 해줌

2. **자바스크립트 내추럴 템플릿**
   * 타입리프에서 HTML 파일을 직접 열어도 내추럴 템플릿이 동작하는 것처럼 자바스크립트 인라인도 이를 지원해준다.
   * `var username2 = /*[[${user.username}]]*/ "test username";`
   * 인라인 사용전: `var username2 = /*userA*/ "test username";`
   * 인라인 사용후: `var username2 = "userA";`

인라인을 사용하면 주석 부분이 제거 되고 "userA" 값이 렌더링 되어 입력된다.

3. **객체**
   * 타임리프의 자바스크립트 인라인 기능을 이용하면 객체를 JSON으로 자동 변환해준다.
   * `var user = [[${user}]];`
   * 인라인 사용전: `var user = user의 toString()값이 들어감`
   * 인라인 사용후: `var user = {"username":"userA", "age":10};` (JSON)

### 자바스크립트 인라인 each
```html
<!-- 자바스크립트 인라인 each -->
<script th:inline="javascript">
  
 [# th:each="user, stat : ${users}"]
 var user[[${stat.count}]] = [[${user}]];
 [/]
         
</script>
```
반복문을 사용해야할 때 타임리프에서는 each기능을 script에서도 지원해준다.