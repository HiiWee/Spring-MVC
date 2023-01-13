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

<br>

## [템플릿 조각]
타임리프는 웹 페이지 개발시 공통영역 처리를 위한 (상단, 하단, 좌측 카테고리 등) 템플릿 조각 및 레이아웃 기능을 지원함

### **th:fragment**   
다른곳에 포함되는 코드 조각   
* `template/fragment/footer :: copy`: `template/fragment/footer.html` 템플릿에 있는 `th:fragment="copy"` 부분을
  템플릿 조각으로 가져와 사용

### 부분 포함 insert
`<div th:insert="~{template/fragment/footer :: copy}"></div>`   
현재 `div` 태그 내부에 해당 조각을 추가함

### 부분 포함 replace
`<div th:insert="~{template/fragment/footer :: copy}"></div>`   
현재 div 태그를 대체함

### 부분 포함 단순 표현식
`<div th:replace="template/fragment/footer :: copy"></div>`   
`~{...}`이 원칙이지만 단순 표현식을 지원함 하지만, 기본적으로 붙여주는것을 권장(코드 복잡해지면 오류 발생 가능성 높음)

### 파라미터 사용
파라미터를 전달해 동적으로 조각을 렌더링 할 수 있음   
`<div th:replace="~{template/fragment/footer :: copyParam ('데이터1', '데이터2')}"></div>`

<br>

## [템플릿 레이아웃1]
큰 모양이 정해져 있고, 내 코드를 해당 모양에 맞춰서 넣는 방법   
(이전에 템플릿 조각은 모양이 정해져있고 해당 모양을 불러서 사용)

공통적인 css, javascript를 설정할때 유용하다.

### layoutMain.html
이 페이지의 태그들을 base.html로 넘겨서 base.html을 레이아웃으로 이용
즉, layoutMain.html의 요소들을 base.html의 원하는 부분에 추가한다.

* `common_header(~{::title}, ~{::link})` 부분이 핵심
  * `::title`은 현재 페이지의 titla 태그들을 전달한다.
  * `::link`는 현재 페이지의 link 태그들을 전달

### 결과
* base.html의 title 부분이 layoutMain.html에서 전달한 부분으로 교체
* base.html의 공통부분은 유지 되고, layoutMain.html에서 추가 부분에 전달한 link 태그들이 포함됨

> 결과적으로 레이아웃 개념을 두고, 레이아웃에 필요한 코드 조각을 전달해서 완성    
> 헷갈린다면 코드를 살펴보고 실행해서 다시 이해하자

**[참고]**   
위의 레이아웃에선 반드시 추가 해야 하는 링크, 타이틀 태그등을 초기에 설정하고 넘겨주어야 한다.   
반면에 넘겨줄만한 태그가 없는 경우 `thymeleaf-layout-dialect`를 이용해보자   
참고글: https://inflearn.com/questions/278792

<br>

## [템플릿 레이아웃2]
html 전체에 템플릿 레이아웃을 적용해보자

`<html></html>` 태그에 `th:fragment` 속성을 정의하여 레이아웃으로 이용한다.   
이후 필요한 내용들을 전달해 부분 변경함

`layoutExtendMain.html`은 현재 페이지임 `<html>` 자체를 `th:replace`를 사용해 변경할 수 있다.   
(layoutFile.html에 필요한 내용을 전달하며 html 자체를 layoutExtendMain.html로 변경)

<br><br>

# <타임리프 - 스프링 통합과 폼>
## [타임리프 스프링 통합]
타임리프는 스프링 통합 메뉴얼도 제공해줌: 
https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html

**스프링 통합으로 제공하는 기능**
* 스프링 SpringEL 문법 통합
* `${@myBean.doSomething()}` 처럼 스프링 빈 호출 지원
* 편리한 폼 관리 위한 추가 속성
  * `th:object` (기능 강화, 폼 커맨드 객체 선택)
  * `th:field`, `th:errors`, `th:errorclass`
* 폼 컴포넌트 기능
  * checkbox, radio button, List 등을 편리하게 사용할 수 있는 기능 지원
* 스프링의 메시지, 국제화 기능의 편리한 통합
* 스프링의 검증, 오류 처리 통합
* 스프링의 변환 서비스 통합(ConversionService)

Spring을 사용하면 타임리프 관련 설정들을 모두 직접 해주어야 하지만, Spring Boot는 DI를 통해 초기에
자동으로 설정을 완료해줌
* 수동 설정: https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html#the-springstandard-dialect

<br><br>

## [입력 폼 처리]
타임리프 지원 기능을 통해 등록 폼, 수정 폼을 효율적으로 변경할 수 있음

1. **`th:object`를 통해 커맨드 객체를 지정(주로 form 태그에 지정하여 사용)**   
2. **`*{...}`: 선택 변수식을 통해 커맨드 객체에 접근할 수 있다.**   
3. **`*{...}`은 주로 `th:field`와 같이 사용하는데 이렇게 하면 id, name, value 속성을 자동으로 처리해준다**

```html
폼 태그에 커맨드 객체 지정
<form action="item.html" th:action th:object="${item}" method="post">
    렌더링 전
    <input type="text" th:field="*{itemName}" />
    렌더링 후
    <input type="text" id="itemName" name="itemName" th:value="*{itemName}" />
</form>
```
* `th:field`는 `id`, `name`, `value`속성을 자동으로 만들어주는데 th:field에서 지정한 변수 이름 및 값과 같다.

th:object, th:field는 검증 부분에서 그 효과가 더욱 부각된다. 잘 알아두자!

<br><br>

## [요구사항 추가]

* `판매 여부`
  * 판매 오픈 여부
  * 체크 박스로 선택할 수 있다.
* `등록 지역`
  * 서울, 부산, 제주
  * 체크 박스로 다중 선택할 수 있다.
* `상품 종류`
  * 도서, 식품, 기타
  * 라디오 버튼으로 하나만 선택할 수 있다.
* `배송 방식`
  * 빠른 배송
  * 일반 배송
  * 느린 배송
  * 셀렉트 박스로 하나만 선택할 수 있다.

<br><br>

## [체크박스 - 단일1]
일반적인 HTML 체크박스는 다음과 같다   
`<input type="checkbox" id="open" name="open" class="form-check-input">`   
체크박스의 속성값은 `on`으로 전송되는데 스프링은 `on`글자를 `true`타입으로 변환해준다.(**스프링 타입 컨버터**)

체크박스를 선택하고 POST 전송 한 값과, 선택 해제하고 한 값을 서버에서 확인하면 다음과 같다.
* 체크선택: `true`
* 체크해제: `null` (속성이 Wrapper 클래스여야함)

여기서 문제가 되는 부분은 체크박스를 선택하지 않고 POST 전송을하면 메시지바디에 `open`이라는 필드 자체가
서버로 전송되지 않는다.(속성 값 뿐만 아니라 이름까지도)

### **HTML checkbox**   
HTML 체크박스는 선택이 안되면 클라이언트에서 서버로 값 자체를 보내지 않음   
**따라서 수정과 같은 상황에서 체크 -> 체크 해제하면 값 자체가 넘어가지 않아 수정 사항 없음으로 판단할 소지가 있다.**

### **스프링 MVC의 약간의 트릭**   
hidden 필드로 `_propertyName`으로 name값을 주고 기존 체크박스에 같이 붙여준다.
```html
<input type="checkbox" id="open" name="open" class="form-check-input">
<input type="hidden" name="_open" value="on"> <!-- 히든 필드 추가 -->
```
이렇게 되면 체크를 하면 `_open`, `open` 둘 다 `on`이라는 값이 전송되고,   
체크 해제를 하면 `_open`만 `on`이라는 값이 전송된다.
* 체크선택: `true`
* 체크해제: `false`

Spring MVC는 이 둘의 차이를 이용해 체크를 하면 `true`를 하지 않으면 `false`를 저장한다.

> 참고: HTTP 요청 메시지 로깅(application.properties 속성): `logging.level.org.apache.coyote.http11=debug`

<br><br>

## [체크박스 - 단일2]
히든필드를 일일이 추가해 주는 작업은 번거로움, 타임리프 제공 폼을 이용하면 이런 부분을 자동으로 처리할 수 있다.

```html
<input type="checkbox" id="open" name="open" class="form-check-input">
<input type="hidden" name="_open" value="on"> <!-- 히든 필드 추가 -->
```
다음과 같이 변경할 수 있다.

```html
<input type="checkbox" id="open" th:field="*{open}" class="form-check-input">
```

**HTML 폼에서 자동생성 확인**   
타임리프를 렌더링하여 결과를 보면 체크박스의 히든 필드 관련 부분도 함께 해결해준다.   
(이 부분은 form을 등록하기 전 상태에서 _open이 보이는것이지 등록하여 서버에 전송을 한뒤에는 히든태그가 아니라 결과만 보인다.)

**타임리프의 체크 확인**   
`checked="checked"`   
체크 박스에서 판매 여부를 선택해 저장하면 조회시 checked 속성이 추가된다, 타임리프의 th:field를 사용하면
값이 true인 경우만 체크를 자동으로 처리해준다.

> 참고: 체크가 되든 안되든 th:value의 값은 항상 true이다. 따라서 서버는 value값을 무시하고(value="true"가 디폴트)
> 서버상의 true, false는 checked 태그로만 확인한다.

<br><br>

## [체크박스 - 멀티]
체크 박스를 멀티로 사용해 하나 이상의 값을 체크해보자

* 등록 지역
  * 서울, 부산, 제주
  * 체크 박스로 다중 선택할 수 있다.

<br>

### @ModelAttribute의 특별한 사용방법   
등록 폼, 상세화면, 수정 폼에서 모두 동일한 체크 박스를 반복해서 보여줘야 함
따라서 각각의 컨트롤러에 model.addAttribute를 하는 작업은 3번이나 반복한다.   

@ModelAttribute를 컨트롤러에 별도의 메서드에 적용하면   
해당 컨트롤러를 요청시 해당 메서드의 반환값이 자동으로 모델에 담겨짐

<br>

### `th:for="${#ids.prev('regions')}"`
멀티 체크박스는 같은 이름의 여러 체크박스를 만드는데 id는 중복이 되면 안된다.   
위의 태그를 이용하면(label에) 타임리프는 체크박스를 each 루프 안에서 반복해서 임의로 1, 2, 3같은   
숫자를 붙여줌   
`ids.prev외에도 next, seq가 존재`

### 지역 선택
컨트롤러에 regions를 찍는 로그를 추가하고 지역을 선택하여 찍어보면, List 자료형에 선택한 지역들의 값이 있는걸 확인   
하지만 아무것도 선택하지 않으면 빈 List가 넘어온다.   
이는 상품 등록 화면에서 소스를 보면 알 수 있는데, _regions가 존재한다. 
타임리프는 이를 이용해 웹 브라우저에서 체크를 하나도 하지 않았을 때, 클라이언트가 서버에
아무런 데이터를 보내지 않는 것을 방지한다. 


### 멀티 체크 박스에서 checked
th:field에 지정한 값과, th:value의 값을 비교해 th:value에 있는 key값이 th:field의 item.regions에 존재하면   
checked 표시를 한다.

<br><br>

## [라디오 버튼]
라디오 버튼은 여러 선택지 중 하나를 선택할 떄 사용된다.   
Enum을 통해 만든 `ItemType`을 활용한다.

* 상품 종류
  * 도서, 식품, 기타
  * 라디오 버튼으로 하나만 선택할 수 있다.

<br>


**여기서도 @ModelAttribute를 이용해 enum 정보를 배열로 Model에 추가**

### 라디오 버튼에서 아무것도 선택하지 않으면 NULL
라디오 버튼은 이미 선택이 되어 있다면 수정할때도 반드시 하나를 선택하도록 되어 있으므로 체크박스와 달리   
NULL값 방지를 위한 별도의 히든 필드를 사용할 필요 없음


**타임리프에서 ENUM 직접 접근**   
```html
<div th:each="type : ${T(hello.itemservice.domain.item.ItemType).values()}">
```
스프링EL 문법으로 ENUM을 직접 사용 할 수 있음, 하지만 패키지의 위치 변경과 같은 변경사항에 취약함

<br><br>

## [셀렉트 박스]
셀렉트 박스는 여러 선택지 중에 하나를 선택할 때 사용할 수 있다. --> 자바 객체를 활용해 만들어보자!

* 배송방식
  * 빠른 배송
  * 일반 배송
  * 느린 배송
  * 셀렉트 박스로 하나만 선택할 수 있다.

이 또한 @ModelAttribute의 사용법을 이용해 미리 DeliveryCode 객체를 생성하여 List에 담고 Model에 담음   
`참고: @ModelAttribute 가 있는 deliveryCodes() 메서드는 컨트롤러가 호출 될 때 마다 사용되므로
deliveryCodes 객체도 계속 생성된다. 이런 부분은 미리 생성해두고 재사용하는 것이 더 효율적`

나머지의 부분들은 이전에 멀티 체크, 라디오 버튼에서 고려했던 사항을 그대로 가져와서 진행하면 된다. 

<br><br>

# <메시지, 국제화>
## [메시지, 국제화 소개]
### 메시지
화면에 보이는 문구, 상품명, 가격, 수량 등 `label`에 있는 단어들의 내용들과 같이 공통으로 사용하는 것들을
메시지라고 하고 이들을 한 곳에서 관리하는 기능을 `메시지 기능`이라 함

```html
예시 message.properties
item=상품
item.id=상품 ID
item.itemName=상품명
item.price=가격
item.quantity=수량
```

이를 이용해 각 HTML은 데이터를 key 값으로 불러와 사용 `#{item.itemName}`

### 국제화
메시지에서 한 발 더 나아가 각 메시지 파일을 각 나라별로 별도로 관리하면 서비스를 국제화 할 수 있다.   
이를 구분하는 방법은 여러가지가 존재   
* HTTP: accept-header
* 사용자가 언어 선택 + 쿠키

메시지 및 국제화 기능은 직접 처리할 수 도 있지만, 스프링이 메시지와 국제화 기능을 편리하게 통합하여 제공해준다.

<br><br>

## [스프링 메시지 소스 설정]

스프링은 기본적인 메시지 관리 기능을 제공한다.

메시지 관리 기능은 스프링 제공 `MessageSource`를 스프링 빈으로 등록해야함   
이는 인터페이스며 따라서 구현체인 ResourceBundleMessageSource를 스프링 빈으로 등록해야함

### 직접등록

```java
@Bean
public MessageSource messageSource(){
        ResourceBundleMessageSource messageSource=new
        ResourceBundleMessageSource();
        messageSource.setBasenames("messages","errors");
        messageSource.setDefaultEncoding("utf-8");
        return messageSource;
        }
```

* `basenames` : 설정 파일의 이름을 지정한다.
  * `messages` 로 지정하면 `messages.properties` 파일을 읽어서 사용한다.(위 예시는 errors.properties도 읽음)
  * 추가로 국제화 기능을 적용하려면 `messages_en.properties` ,`messages_ko.properties` 와 같이
  파일명 마지막에 언어 정보를 주면된다. 만약 찾을 수 있는 국제화 파일이 없으면
  `messages.properties` (언어정보가 없는 파일명)를 기본으로 사용한다.
  * 파일의 위치는 `/resources/messages.properties` 에 두면 된다.
  * 여러 파일을 한번에 지정할 수 있다. 여기서는 `messages` , `errors` 둘을 지정했다.
* `defaultEncoding` : 인코딩 정보를 지정한다. utf-8 을 사용하면 된다.

스프링 부트를 이용하면 위 과정을 간단하게 자동으로 `MessageSouce`를 자동적으로 빈으로 등록한다.

<br>

### 스프링 부트 메시지 소스 설정
`application.properties` 밑에   
`spring.meessages.basename=messages,config.i18n.messages`

<br>

**스프링 부트 메시지 소스 기본값**   
`spring.messages.basename=messages`   

MessageSource 스프링 빈으로 등록하지 않고, 스프링 부트와 관련된 별도의 설정을 하지 않으면 `messages`라는 이름으로 기본등록됨
따라서 다음 파일만 등록하면 자동인식 됨
* messages_en.properties
* messages_ko.properties
* messages.properties

<br>

**한글과 영어용 파일을 resources/ 아래에 만들고 다음을 등록하자**
* messages.properties
* messages_en.properties

```html
# 한글
hello=안녕
hello.name=안녕 {0}

# 영어
hello=hello
hello.name=hello {0}
```

<br><br>

## [스프링 메시지 소스 사용]

스프링이 제공하는 메시지 소스를 사용하기 위해선 `MessageSource interface`를 사용한다.

이곳에서 제공해주는 메소드는 `String getMessage()`로 코드를 포함한 일부 파라미터로 메시지를 읽어오는 기능을 제공함

사용방법은 `MessageSourceTest`를 참고

* 다음과 같은 상황에선 default 국제화 파일을 선택한다(`messages.properties`)
  * message.getMessage("hello", null, Locale.KOREA): Locale 정보가 있지만, message_ko.properties 파일이 없으므로
    default인 messages.properties를 사용한다.

<br><br>

## [웹 애플리케이션에 메시지 적용하기]

타임리프에 실제 메시지를 적용하기 위해서는 다음과 같은 표현식을 이용
* `#{...}`
  * 주로 변하지 않는 태그들의 이름을 등록하기 때문에 `th:text`와 조합해서 사용한다. 
  
<br>

**파라미터는 다음과 같이 사용할 수 있다.**
* messages.properties: `hello.name=안녕 {0}`
* `<p th:text="#{hello.name(${item.itemName})}"></p>`

<br><br>

## [웹 애플리케이션에 국제화 적용하기]
웹 애플리케이셔넹 국제화를 적용해 영어 메시지를 추가해보자

단순히 이전시간에 적용하였으므로, messages_en.properties에 영어로 된 태그 이름을 동일하게 추가하고
서버를 시작한 뒤 언어 설정을 변경해주면 된다.

**이렇게 간단하게 사용할 수 있는 이유?**   
* request-header를 보면 `Accept-Language`헤더에서 언어 설정을 변경하게 되면 우선순위의 언어들이 변경된다.   
* 메시지 기능은 테스트를 했을때 봤듯이 Locale 정보를 알아야 하고, 스프링은 이런 Locale을 선택할때
  `Accept-Language` 헤더의 값을 이용하여 선택하게 된다.

**LocaleResolver**   
스프링은 Locale 선택 방식을 변경할 수 있게 `LocaleResolver` 인터페이스를 제공한다.   
여기서 기본 구현체는 `Accept-Language`를 활용하는 `AcceptHeaderLocaleResolver`를 이용한다.
> Locale 선택 방식을 변경하길 원한다면 LocaleResolver의 구현체를 변경해서 쿠키 혹은 세션 기반의 Locale 선택기능을
> 사용할 수 있다.


<br><br>

# <검증1 - Validation>
## [검증 요구사항]
상품 관리 시스템의 새로운 요구사항 추가

**요구사항: 검증 로직 추가**
* 타입 검증
  * 가격, 수량에 문자가 들어가면 검증 오류 처리
* 필드 검증
  * 상품명: 필수, 공백X
  * 가격: 1000원 이상, 1백만원 이하
  * 수량: 최대 9999
* 특정 필드의 범위를 넘어서는 검증
  * 가격 * 수량의 합은 10,000원 이상

**컨트롤러의 중요한 역할 중 하나는 HTTP 요청이 정상인지 검증하는 것**   
정상 로직 개발보다 검증로직을 잘 개발하는것이 더 어려울 수 있음..

<br>

검증은 **클라이언트 검증**, **서버 검증**으로 나뉘는데 이 둘은 약간의 차이가 존재한다.   
* **클라이언트 검증**
  * 조작할 수 있으므로(JS) 보안에 취약함
* **서버 검증**
  * 서버만으로 검증하면 즉각적인 고객 사용성 부족해짐


* 따라서 둘을 잘 섞어서 사용하되, 최종적인 서버 검증은 필수다.
* API 방식을 사용하면 API 스펙을 잘 정의해서 검증 오류를 API 응답 결과에 잘 남겨주어야 한다.

<br><br>

## [검증 직접 처리 - 소개]
상품등록을 실패하게 되면(상품명 미기입, 가격 및 수량이 미달이거나 초과) 서버 검증 로직이 실패해야 한다.   
검증로직이 실패한 경우, 고객에게 다시 상품 등록 폼을 보여주고, 어떤 값이 잘못되었는지 알려줘야 함

<br><br>

## [검증 직접 처리 - 개발]

### 상품 등록 검증

* 검증 오류 : HashMap을 이용해 보관(Key, Value)
  * StringUtils 객체를 이용해 값을 확인후 없다면 담는다.
* 글로벌 검증: 특정 필드가 아닌 복합 룰 검증도 해야할수 있다.
  * `가격 * 수량의 합은 10000만 이상`
* 검증이 실패하면 다시 입력폼으로
  > 상품등록 검증에 실패하면 현재 페이지를 다시 요청한다.   
    그런데 여기서 궁금한 부분은 페이지가 다시 요청되었는데 어떻게 이전에 입력한 값이 남아있을까?
  >    * `@ModelAttribute`의 자동 `Model.addAttribute()`으로 주입
  >      * 우리는 `GetMapping`때 빈 객체를 만들어서 form 데이터로 넘겨주었다.
  >      * 사용자가 `POST` 요청을 하게되면 빈 객체에 사용자가 입력한 값이 저장되어 `@ModelAttribute`로 넘어오게 되는데
  >      * 이때 `@ModelAttribute`는 `Model`에 자동으로 해당 객체를 추가해주기 때문에 오류로 인해 해당 페이지를 재 요청하면
  >      * 사용자가 입력한 값이 그대로 남아있음
  >    * 혹은 PRG를 이용하지 않고, 다시 POST요청을 하게되어 이전 값이 남아있을 가능성도 존재한다.

<br>

### **참고: Safe Navigation Operator**   
```html
<div>
    <label for="itemName" th:text="#{label.item.itemName}">상품명</label>
    <input type="text" id="itemName" th:field="*{itemName}"
           th:class="${errors?.containsKey('itemName')} ? 'form-control field-error' : 'form-control'"
           class="form-control" placeholder="이름을 입력하세요">
    <div class="field-error" th:if="${errors?.containsKey('itemName')}" th:text="${errors['itemName']}">
        상품명 오류
    </div>
</div>
```
위의 HTML 코드에서 다음 부분을 살펴보자   
* `th:class="${errors?.containsKey('itemName')} ? 'form-control field-error' : 'form-control'"` 

`th:if`를 통해 오류가 존재하면 오류를 표시하기 위한 클래스(`form-control filed-error`)를 가진 태그로 대체된다.   
한가지 다른점은 `${errors?.containsKey(...)`에서 `?`가 어떤 의미인지 헷갈릴 수 있다.   
위의 코드는 등록폼에서 가져왔는데(addForm.html) 등록폼에 진입한 시점(GET)일때는 `errors` 인스턴스가 아직 생성되기 이전이다.
따라서 `null`값을 가지게 되고, 기존의 `errors.containsKey()`를 호출하면 `NPE`가 발생한다.  

여기서 `errors?.containsKey()`는 `errors`가 `null`일때 `NPE`대신 null을 반환한다. `itemName`을 조회할 수 없고,   
th:if 에서 null 은 실패로 처리되므로 오류 메시지가 출력되지 않는다
> 이는 Spring EL 문법 : https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#expressions-operator-safe-navigation

<br>

### `classappend`를 활용해 좀 더 편리하게 HTML 필드 오류 처리하기   
```html
<input type="text" id="itemName" th:field="*{itemName}"
           th:class="${errors?.containsKey('itemName')} ? 'form-control field-error' : 'form-control'"
           class="form-control" placeholder="이름을 입력하세요">
```
위의 코드를 다음과 같이 간단하게 사용할 수 있다.

```html
<input type="text" id="itemName" th:field="*{itemName}"
           th:classappend="${errors?.containsKey('itemName')} ? 'field-error' : _" 
           class="form-control" placeholder="이름을 입력하세요">
```
`classappend`를 활용해 해당 필드에 오류가 존재하면 `field-error`클래스를 기존 클래스에 더하여 폼의 색상을 경고 색상으로 변경   
값이 없다면 `_` == `No-Operation`을 사용해서 아무 작업도 하지않음


### 정리
1. 검증 오류 발생 -> 입력 폼 다시 보여줌
2. 검증 오류들을 고객들에게 안내해서 다시 입력할 수 있게끔 함
3. 검증 오류가 발생해도 입력데이터 소실되지 않음

<br>

### 문제점
* 뷰 템플릿에서의 많은 중복 처리 --> 비슷함
* 타입 오류 처리 불가
* `Integer`타입의 `price`, `quantity`인 가격과 수량에 Integer 타입 이상의 값을 넣거나 해당 타입이 아닌 문자열을 입력하면   
  오류가 `Controller`를 거치지 않고 400오류(클라이언트 오류) 반환됨
  > ArgumentResolver에서 @ModelAttribute에 해당 인스턴스를 생성하고 입력값을 인스턴스에 setXXX를 통해 넣지만,   
    입력값의 데이터와 필드의 자료형이 맞지 않아 Controller 진입전에 오류가 반환된다.
* Integer 타입을 입력해야 하는 부분에 다른 자료형을 입력해 타입 오류가 발생해도 고객이 입력한 데이터는   
  다시 고객 화면에 남겨야 고객이 인지할 수 있음, 하지만 `Integer`타입으로 바인딩할 수 없다.
* 결국 고객 입력 데이터 값도 별도로 관리 되어야 함

<br><br>

## V2 프로젝트
## [BindingResult1]
스프링이 제공하는 검증 오류 처리 방법, 핵심은 **BindingResult**

Controller의 파라미터로 `BindingResult bindingResult`를 받아오는데 반드시 `@ModelAttribute` 다음으로 와야 한다.
해당 `@ModelAttribute`에서 바인딩을할 때 오류가 발생하면 `bindingResult`에 담기기 때문

* `BindingResult bindingResult`의 파라미터
  * `ObjectName`: `@ModelAttribute` 이름
  * `field`: 오류가 발생한 필드 이름
  * `defaultMessage`: 오류 기본 메시지

### 글로벌 오류 - ObjectError
특정 필드를 넘어서는 오류가 존재하면 `Global Error`이다. 이때는 `FieldError`객체가 아닌 `ObjectError` 객체를 생성해   
`bindingResult`에 담는다. 

<br>

### 타임리프 스프링 검증 오류 통합 기능
기존의 복잡했던 타임리프의 코드를 `BindingResult` 객체를 활용해 편리하게 오류검증을 표현하는 기능을 제공해줌
* `#fields`: `#fields`로 `BindingResult`가 제공하는 오류에 접근
* `th:errors`: `Controller`에서  `BindingResult` 파라미터에 담긴 `field`에 오류가 있는 경우 출력, `th:if`의 편의버전
* `th:errorclass`: `th:field`에서 지정한 최초 폼을 `GET할때 모델에 담아놨던 인스턴스`의 필드에 오류가 존재하면 `class` 정보를 추가

<br>

### HTML 코드의 변화
```html
BindingResult 사용 전
        <div>
            <label for="itemName" th:text="#{label.item.itemName}">상품명</label>
            <input type="text" id="itemName" th:field="*{itemName}"
                   th:class="${errors?.containsKey('itemName')} ? 'form-control field-error' : 'form-control'"
                   class="form-control" placeholder="이름을 입력하세요">
            <div class="field-error" th:if="${errors?.containsKey('itemName')}" th:text="${errors['itemName']}">
                상품명 오류
            </div>
        </div>
```
```html
BindingResult 사용 후
        <div>
            <label for="itemName" th:text="#{label.item.itemName}">상품명</label>
            <input type="text" id="itemName" th:field="*{itemName}"
                   th:errorclass="field-error" class="form-control" placeholder="이름을 입력하세요">
            <div class="field-error" th:errors="*{itemName}">
                상품명 오류
            </div>
        </div>
```

<br><br>

## [BindingResult2]
BindingResult에 대해 좀 더 자세히 알아보자

* 스프링이 제공하는 검증 오류 보관 객체, 검증 오류 발생시 BindingResult 객체에 보관
* `@ModelAttribute`에 데이터 바인딩 오류 발생시 `BindingResult`가 존재하면 `Controller`를 호출한다.
  * **만약 @ModelAttribute에 바인딩을 할 때 `타입`오류가 발생하면?**
    * `BindingResult X` -> **400 오류** 발생, 컨트롤러 미호출, 오류 페이지 반환
    * `BindingResult O` -> 오류 정보`(FieldError)`를 BindingResult에 담아 컨트롤러 정상 호출
      * `스프링이 데이터 타입을 검사하다 FieldError 객체를 자동 생성해 오류 정보를 담고 컨트롤러를 호출한다.`

<br>

### BindingResult에 검증 오류 적용방법
1. @ModelAttribute의 객체 타입 오류 등으로 인해 바인딩 실패시 스프링이 자동으로 `FieldError`를 생성해 `BindingResult`에 넣음
2. 비즈니스 로직에서 검증이 필요한 경우, 개발자가 직접 생성해서 넣어준다.
3. `Validator`사용

> BindingResult는 검증할 대상 바로 뒤에 와야함(주로 `@ModelAttribute`뒤)    
> BindingResult는 Model에 자동으로 포함됨(Model.addAttribute() 불필요)

<br>

### BindingResult와 Errors
BindingResult는 인터페이스이며, Errors 인터페이스를 상속받음 따라서, Errors를 이용할 수 있지만 관례상 BindingResult 사용   
대신에 Errors는 BindingResult보다 좀 더 단순한 오류 저장 및 조회 기능을 제공한다.   

<br>

### 현재의 문제점   
타입 오류가 아닌 우리가 직접 작성한 비즈니스 검증 에러가 발생하면, 사용자가 입력한 데이터가 모두 사라진다.

<br><br>

## [FieldError, ObjectError]
* 현재 문제가 되는 사용자 입력 오류 메시지가 화면에 남도록 해야 한다.
  * `예시`: 가격 1000원 미만 설정시 입력한 값 남아있어야 함
* FieldError와 ObejctError에 대해 자세히 알아보자

<br>

### FieldError는 2가지의 생성자를 가진다.
우리가 지금까지 이용한 방식은 다음과 같은 생성자
```java
public FieldError(String objectName, String field, String defaultMessage) {
    this(objectName, field, null, false, null, null, defaultMessage);
}
```

사용자가 입력한 값이 전송 후에도 다시 남아있기 위해서는 다음 생성자 이용
```java
  public FieldError(String objectName, String field, @Nullable Object rejectedValue, boolean bindingFailure,
          @Nullable String[] codes, @Nullable Object[] arguments, @Nullable String defaultMessage) {

      super(objectName, codes, arguments, defaultMessage);
      Assert.notNull(field, "Field must not be null");
      this.field = field;
      this.rejectedValue = rejectedValue;
      this.bindingFailure = bindingFailure;
  }
```
* 파라미터 목록
  * `objectName`: 오류가 발생한 객체 이름
  * `field`: 오류 필드
  * `rejectedValue`: 사용자가 입력한 값(거절된 값)
  * `bindingFailure`: 타입 오류 같은 바인딩 실패(true)인지, 검증 실패(false)인지 구분하는 값
  * `codes`: 메시지 코드
  * `arguments`: 메시지에서 사용하는 인자
  * `defaultMessage`: 기본 오류 메시지

따라서 rejectedValue는 거절된 값 혹은 사용자가 입력한 값을 담게 되는데 다음과 같이 담긴다.

1. **사용자 커스텀 검증에서 검증을 실패한 경우**
   * 이말은 즉, `@ModelAttribute`까지는 성공했으므로 Item item 인스턴스에 필드의 값은 존재하지만,   
     사용자의 검증 기준에 미치지 못해 생성된 `FieldError`의 r`ejectedValue`에 사용자가 입력한 값이 담겨지게 되고   
     `view`로 해당 `FieldError`를 가진 `bindingResult`가 보내지게 되어 사용자가 자신이 입력한 값을 확인할 수 있다.
2. **Integer 필드에 String 값이 들어와 `@ModelAttribute`바인딩이 실패하는 경우**
   * 이 상황에선 `Spring`이 내부적으로 `typeMismatch`가 발생하게 되어 입력값의 타입을 변경하여 typeMismatch가 발생하기 전   
     데이터인 사용자 입력값의 데이터를 알고 있게 되고, 해당 값을 이용해 `FieldError객체를 생성`하게 된다.
   * 이후 BindingResult 객체에 담고 컨트롤러를 호출한다.
   * 여기서 `사용자의 커스텀 검증`을 또 거치게 되면 또다른 FieldError 인스턴스가 bindingResult에 담기게 되지만,   
     View에서 보이는건 `먼저 들어간 오류 데이터가 출력`하게 된다.
   * 따라서 사용자는 `스프링의 typeMismatch 오류 담은 bindingResult를 마주`하게 된다.

> 사용자 커스텀 검증의 경우 binding의 실패가 아니므로 bindingFailure는 false값을 준다.

<br>

### 타임리프의 사용자 입력 값 유지
`th:field"*{price}"`   
위의 코드는 똑똑하게 동작한다.
* 정상 상황에선 Model 객체의 값을 사용한다.
* 오류 발생 상황에선 FieldError에서 보관한 값을 사용하여 출력한다.

따라서 잘못 입력된 사용자의 값을 전송 후에도 사용자가 다시 보고 확인할 수 있음

<br>

### 스프링 바인딩 오류 처리
위에서 설명했듯이, 타입 오류로 인한 바인딩 실패시 스프링은 FieldError를 생성하며 사용자가 입력한 값을 넣는다.   
이후 `BindingResult` 인스턴스에 담고 컨트롤러를 호출함, 따라서 타입 오류 같은 바인딩 실패시에도 사용자의 오류 메시지를 정상 출력한다.

<br><br>

## [오류 코드와 메시지 처리1]
오류 메시지를 이전에 메시지, 국제화에서 사용한 properties를 이용해 체계적으로 관리할 수 있음

* 오류 메시지의 관리 필요
  * 다양한 곳에서 사용되는 오류가 많아지고 코드의 중복이 많아짐 -> 하나로 합쳐서 사용

이전에 봤던 FieldError 생성자를 다시 살펴보면
```java
  public FieldError(String objectName, String field, @Nullable Object rejectedValue, boolean bindingFailure,
          @Nullable String[] codes, @Nullable Object[] arguments, @Nullable String defaultMessage) {

      super(objectName, codes, arguments, defaultMessage);
      Assert.notNull(field, "Field must not be null");
      this.field = field;
      this.rejectedValue = rejectedValue;
      this.bindingFailure = bindingFailure;
  }
```
* 파라미터 목록
  * `objectName`: 오류가 발생한 객체 이름
  * `field`: 오류 필드
  * `rejectedValue`: 사용자가 입력한 값(거절된 값)
  * `bindingFailure`: 타입 오류 같은 바인딩 실패(true)인지, 검증 실패(false)인지 구분하는 값
  * `codes`: 메시지 코드
  * `arguments`: 메시지에서 사용하는 인자
  * `defaultMessage`: 기본 오류 메시지

`codes`, `arguments`를 이용해 오류 코드로 메시지를 찾기 위해 사용한다.   

* application.properties 설정   
Error 처리를 위한 errors.properties를 만들어 별도의 파일로 관리하기 위해 application.properties에 설정 추가   
`spring.messages.basename=messages, errors`


* errors.properties 설정
  ```java
  required.item.itemName=상품 이름은 필수입니다.
  range.item.price=가격은 {0} ~ {1} 까지 허용합니다.
  max.item.quantity=수량은 최대 {0} 까지 허용합니다.
  totalPriceMin=가격 * 수량의 합은 {0}원 이상이어야 합니다. 현재 값 = {1}
  ```
  
* Controller에서 적용하기
  * `codes`는 `String[]`을 이용해 `errors.properties`의 key값을 입력한다.
    * 배열로 여러값을 전달하여 순서대로 매칭해 처음 매칭되는 메시지가 출력됨
  * `arguments`는 `Object[]`를 이용해 메시지의 `{0}`, `{1}` 같은 파라미터를 매칭한다.

> `MessageSource`를 이용하므로 errors_en.properties를 이용하면 국제화 기능도 당연히 이용할 수 있음

<br><br>

## [오류 코드와 메시지 처리2]
* FieldError, ObjectError는 다루기 번거로움
* 오류 코드도 좀 더 자동화 할 수 있지 않을까?

**컨트롤러에서 BindingResult 객체는 검증해야 할 객체인 Item item(`target`) 바로 다음에 온다.**   
이 말은 BingingResult는 본인이 검증해야 할 객체인 `target`을 이미 알고있다는 의미이다.

따라서 컨트롤러 최상단에(addErrors() 이전)로그를 이용해 다음을 찍어보면 실제 target 객체가 출력된다.
```java
log.info("objectName={}", bindingResult.getObjectName());
log.info("target={}", bindingResult.getTarget());
```

<br>

### rejectValue(), reject()
`BindingResult`에서 제공하는 메서드로 `FieldError` 혹은 `ObjectError`를 직접 생성하지 않고, 좀 더 깔끔하게 검증오류를 처리할 수 있음

```java
// 기존 FieldError 인스턴스 직접 주입 방식
bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{1000, 1000000}, null));
bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000, resultPrice}, null));

// rejectValue(), reject() 사용
bindingResult.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
```
한눈에 봐도 코드량과 가독성이 확 개선되었다.   


### rejectValue()
```java
void rejectValue(@Nullable String field, String errorCode,
@Nullable Object[] errorArgs, @Nullable String defaultMessage);
```
* `field`: 오류 필드명
* `errorCode`: 오류 코드(errors.messages에 등록된 코드가 아님, messageResolver를 위한 오류 코드)
* `errorsArgs`: 오류 메시지에서 {0}을 치환하기 위한 값
* `defaultMessage`: 기본 메시지

> BindingResult는 어떤 객체를 대상으로 검증하는지 target을 이미 알고 있으므로 objectName이 없어도 됨   
> `reject()`또한 앞의 내용과 동일하지만, 글로벌 에러를 처리한다.
<br>

**이전 코드와의 차이점**   
FieldError()는 오류 코드(codes)를 `range.item.price`와 같이 모두 입력함   
rejectValue()는 오류 코드(codes)를 `range`로 간단하게 입력
> MessageCodesResolver가 이런 차이를 해결해준다.

<br><br>

## [오류 코드와 메시지 처리3]
오류 코드를 만들때 다음과 같은 고민이 존재한다.   
* 오류 메시지를 세밀하게 작성하기: `required.item.itemName=상품 이름은 필수 입니다.`
* 오류 메시지를 범용성있게 작성하기: `required=필수 값 입니다.`

단순하게 만들면 범용성이 좋아 두루두루 사용 가능하지만, 세밀하게 작성이 어렵고   
세밀하게 만들면 범용성이 떨어진다 -> `trade off`

<br>

**스프링의 `MessageCodesResolver`는 다음과 같은 기능을 제공해준다.**

만약 오류코드(errorCode)로 `required`를 사용할때 우선순위는 세밀한것 -> 범용적인 것으로 사용한다.   
즉, `required.item.itemName`과 같이 객체명 + 필드명의 조합한 세밀한 메시지가 있다면 해당 메시지를 높은 우선순위로 두고 사용하고   
만약 `required`와 같이 범용적인 오류 메시지만 존재하면 해당 메시지를 선택해서 사용한다.

이렇게 하면 메시지의 추가 만으로 편리하게 개발할 수 있게 된다.

<br><br>

## [오류 코드와 메시지 처리4]
**MessageCodesResolver**   
테스트 코드를 이용해 `DefaultMessageCodesResolver.resolveMessageCodes(...)`을 실행하면   
실제 검증 오류 코드로 메시지 코드들을 생성함(String[] codes)   
* 주로 `ObjectError`, `FieldError`와 함께 사용한다.(`@Nullable String[] codes` 매개변수로)

<br>

**DefaultMessageCodesResolver의 기본 메시지 생성 규칙**

**객체 오류**   
다음 순서로 2가지 생성
1. `code + "." + object name`
2. `code`

예시) 오류 코드: required, object name: "item"
1. `required.item`
2. `required`

<br>

**핃르 오류**
4가지 메시지 코드 생성   
1. `code + "." + object name + "." + field`
2. `code + "." + field`
3. `code + "." + field type`
4. `code`

예시) 오류 코드: typeMismatch, object name: "user", field: "age", field type: int
1. "`typeMismatch.user.age`"
2. "`typeMismatch.age`"
3. "`typeMismatch.int`"
4. "`typeMismatch`"

<br>

**동작 방식**
* rejectValue(), reject()는 내부에서 MessageCodesResolver를 사용함 -> 여기서 메시지 코드들을 생성함
  ```java
  // 실제 AbstractBindingResult의 rejectValue()를 보면 다음과 같은 코드 존재
  FieldError fe = new FieldError(getObjectName(), fixedField, newVal, false,
                resolveMessageCodes(errorCode, field), errorArgs, defaultMessage);
  
  
  // 여기서 resolveMessageCodes(...)를 들어가면 다음과 같이 사용한다.
  @Override
  public String[] resolveMessageCodes(String errorCode, @Nullable String field) {
        return getMessageCodesResolver().resolveMessageCodes(
                errorCode, getObjectName(), fixedField(field), getFieldType(field));
  } 
  ```
* FieldError, ObjectError의 생성자를 보면, 오류 코드를 하나가 아니라 여러 오류 코드를 가질 수 있음
  `MessageCodesResolver`를 통해서 생성된 순서대로 오류 코드를 보관한다.
  ```java
  public FieldError(String objectName, String field, @Nullable Object rejectedValue, boolean bindingFailure,
            @Nullable String[] codes, @Nullable Object[] arguments, @Nullable String defaultMessage)
  ```
* 실제 `BindingResult`의 로그를 통해서 확인할 수 있다.   
  `log.info("errors={}", bindingResult);`

<br>

**실제 자동 생성된 오류 코드**   
* `FieldError`: `rejectValue("itemName", "required);` -> 4가지 오류 코드 자동 생성
  * required.item.itemName
  * required.itemName
  * required.java.lang.String
  * required

* `ObjectError` `reject("totalPriceMin")` -> 2가지 오류 코드 자동 생성
  * totalPriceMin.item
  * totalPriceMin


따라서 타임리프에서 화면을 렌더링할 때 `th:errors`가 실행되며, 이때 오류가 있다면 생성된 오류 메시지 코드를   
순서대로 돌아가며 메시지를 찾음, 없다면 defaultMessage 출력

<br><br>

## [오류 코드와 메시지 처리5]
### 핵심, 구체적인 것 -> 덜 구체적인 것으로
`MessageCodesResolver`는 `required.item.itemName`처럼 구체적인 것을 먼저 만들어주고,
`required`처럼 덜 구체적인 것을 가장 나중에 만든다.

<br>

### 복잡하게 사용하는 이유?
모든 오류 코드에 대해 메시지를 전부 정의하면 관리하기 어렵다. 따라서 크게 중요하지 않은 메시지들은 범용성있는
덜 구체적인 것을 이용하고, 정말 중요한 메시지는 구체적으로 적어 사용하는것이 효과적이다.

<br>

### 객체 및 필드 오류를 나누고, 범용성에따라 필드 오류를 나눔
`itemName`의 경우 `required` 검증 오류 메시지가 발생하면 다음 코드 순서대로 메시지가 생성됨
1. `required.item.itemName`
2. `required.item`
3. `required.java.lang.String`
4. `required`

이렇게 생성된 메시지 코드 기반으로 `MessageSource`에서 찾는다.   
구체적 -> 덜 구체적으로 찾음, 따라서 크게 중요하지 않은 오류는 기존의 것을 재활용하면 된다.

<br>

### ValidationUtils
ValidationUtils를 이용하면 `Empty`, 공백등의 기능을 좀 더 간결하게 간단한 기능들을 사용할 수 있다.

```java
// 사용전
if (!StringUtils.hasText(item.getItemName())) {
    bindingResult.rejectValue("itemName", "required", "기본: 상품 이름은 필수입니다.");
}

// 사용후
ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName", "required");
```

<br>

**정리**
1. rejectValue() 호출
2. `MessageCodesResolver`를 사용해서 검증 오류 코드로 메시지 코드들을 생성
3. `new FieldError()`를 생성하며 메시지 코드들을 보관
4. **`th:errors`에서 메시지 코드들로 메시지를 순서대로 메시지에서 찾고, 노출한다.**

<br><br>

## [오류 코드와 메시지 처리6]
### 스프링이 직접 만든 오류 메시지 처리
검증 오류의 종류
1. 개발자가 직접 설정한 오류 코드 -> rejectValue() 직접 호출
2. 스프링이 직접 검증 오류에 추가한 경우(주로 타입 Mismatch)

지금까지 작성한 코드들은 개발자가 직접 설정한 오류에 대해서는 우리가 원하는 메시지를 출력했다.   
하지만 Integer가 들어가야 할 자리에 String이 들어가 Binding 자체가 되지 않아 스프링이 내부적으로 만드는   
타입 오류 같은 경우 typeMismatch가 발생하고, 조금 딱딱한 오류를 화면에 보여준다.

실제 로그를 찍어보면 다음과 같이 나타난다.
```html
codes[typeMismatch.item.price,typeMismatch.price,typeMismatch.java.lang.Integer,typeMismatch]
...
Failed to convert property value of type java.lang.String to required type 
java.lang.Integer for property price; nested exception is 
java.lang.NumberFormatException: For input string: "A"
```

위의 codes를 보면 자바에서 자동적으로 생성한 메시지 코드를 이용해 우리가 임의의 메시지를 넣을 수 있다.
1. `typeMismatch.item.price`
2. `typeMismatch.price`
3. `typeMismatch.java.lang.Integer`
4. `typeMismatch`

errors.properties에 추가
```html
#추가
typeMismatch.java.lang.Integer=숫자를 입력해주세요.
typeMismatch=타입 오류입니다.
```

<br><br>

## [Validator 분리1]
**목표**   
* 복잡한 검증 로직을 별도로 분리하자.

현재 컨트롤러의 절반 이상의 코드가 검증 코드로 이루어짐 -> 분리의 필요성을 느낌   
스프링은 `Validator`라는 인터페이스를 제공한다 이를 이용해서 검증로직을 분리해보자
```java
public interface Validator {
    boolean supports(Class<?> clazz);
    void validate(Object target, Errors errors);
}
```
* `supports(Class<?> clazz)` : 해당 검증기를 지원하는 여부 확인
* `validate(Object target, Errors errors)` : 검증 대상 객체와 BingingResult 넘겨서 검증

Validator 인터페이스를 구현한 구현체를 컴포넌트로 등록하고 스프링 빈으로 주입받아 컨트롤러에서 사용하면   
한 줄로 검증 코드를 줄일 수 있다.
```java
itemValidator.validate(item, bindingResult);
```

**그런데 굳이 Validator를 구현하지 않아도 검증이 충분히 가능할 것 같다. 왜 굳이 Validator를 구현하여   
검증 코드를 작성할까?**


<br><br>

## [Validator 분리2]
Validator 인터페이스를 제공하는 이유는 체계적으로 검증 기능을 도입하기 위해서이다.   
또한 Validator 인터페이스를 사용해 검증기를 만든다면 스프링의 추가적인 도움을 받을 수 있다.

<br>

### WebDataBinder를 통해 사용
`WebDataBinder`는 스프링의 파라미터 바인딩의 역할을 해주고, 검증 기능도 내부에 포함한다.

`Controller`에 다음과 같은 코드를 삽입하면 해당 컨트롤러는 검증기를 자동 적용한다.   
`@InitBinder`는 해당 컨트롤러만 영향을 주고 다른 클래스는 영향 X, 글로벌 설정이 따로 존재한다.
```java
@InitBinder
public void init(WebDataBinder dataBinder) {
    log.info("init binder {}", dataBinder);
    dataBinder.addValidators(itemValidator);
}
```


이후 검증을 적용하기 원하는 컨트롤러의 검증 대상 앞에 `@Validated` 어노테이션을 붙여주면 된다.
```java
public String addItemV6(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model)
```

<br>

**동작 방식**   
`@Validated`는 검증기를 실행하라는 어노테이션   
해당 어노테이션을 보고 스프링은 `WebDataBinder`에 등록한 검증기를 찾아 실행한다.   
그런데 `WebDataBinder`를 보면 여러 검증기를 등록할 수 있다.   
이 때 `Validator`인터페이스의 `supports()` 메서드가 사용된다.


<br>

### 글로벌 설정 - 모든 컨트롤러에 적용
```java
@SpringBootApplication
public class ItemServiceApplication implements WebMvcConfigurer {
    
    public static void main(String[] args) {
        SpringApplication.run(ItemServiceApplication.class, args);
    }
    
    @Override
    public Validator getValidator() {
        return new ItemValidator();
    }
    
}
```
위와같이 적용하면 기존에 컨트롤러에 적용했던 @InitBinder를 주석처리해도 동작함

### 검증시의 애노테이션(@Valid, @Validated)
* @Valid: `javax.validation.@Valid`, 자바 표준 검증 애노테이션으로 따로 gradle의 의존관계 추가 필요
  * implementation 'org.springframework.boot:spring-boot-starter-validation'
* @Validated: 스프링 전용 애노테이션

이 둘은 동일한 역할을 하지만, 약간의 차이가 존재한다.

<br><br>

# <검증2 - Bean Validation>
## [Bean Validation 소개]
검증 기능을 매번 코드로 작성하기 -> 번거롭고, 비효율적   
검증 로직의 대부분은 빈값, 특정 값 체크 같은 일반적 로직이므로 Bean Validation을 통해 간소화 가능

```java
// Bean Validation 사용 예시 코드
public class Item {
    private Long id;
    
    @NotBlank
    private String itemName;
    
    @NotNull
    @Range(min = 1000, max = 1000000)
    private Integer price;
    
    @NotNull
    @Max(9999)
    private Integer quantity;
    //...
}
```

### Bean Validation?
Bean Validation은 특정 구현체가 아닌 Bean Validation 2.0(JSR-380)이라는 기술 표준임   
검증 애노테이션 및 여러 인터페이스의 모음(JPA가 표준이고 구현체로 Hibernate가 있는것과 같음)   
Bean Validation의 일반적인 구현체는 Hibernate Validator가 있다.(ORM X)

<br><br>

## [Bean Validation - 시작]
Bean Validation 기능 사용을 위해 의존관계를 추가하고 스프링과 통합하지 않고 순순한 Bean Validation 사용법을 알아보자

<br>

### Item 객체에 검증 어노테이션 달기
**검증 애노테이션**
* `@NotBlank` : 빈값 + 공백만 있는 경우를 허용하지 않는다.
* `@NotNull` : null 을 허용하지 않는다.
* `@Range(min = 1000, max = 1000000)` : 범위 안의 값이어야 한다.
* `@Max(9999)` : 최대 9999까지만 허용한다.

> 여기서 @Range를 제외한 나머지 애노테이션 3개는 특정 구현에 관계없이 지원해주는 표준 인터페이스이고   
> @Range만 하이버네이트에서 제공하는 애노테이션이다.

<br>

### 테스트 하기
```java
    @Test
    void beanValidation() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Item item = new Item();
        item.setItemName(" "); // 공백
        item.setPrice(0);
        item.setQuantity(10000);

        Set<ConstraintViolation<Item>> violations = validator.validate(item);

        for (ConstraintViolation<Item> violation : violations) {
            System.out.println("violation = " + violation);
            System.out.println("violation.getMessage() = " + violation.getMessage());
        }
    }
```
`Validation.buildDefaultValidatorFactory()`를 생성해 검증기를 하나 생성하고, Item인스턴스를 우리가 설정한 검증을 통과하지 못하게
필드를 초기화 하고 검증기에 넣어 결과를 받아보면 검증 오류가 발생한 객체, 필드, 메시지 정보 등 여러정보 확인 가능하다.


> 스프링은 이미 Bean Validator를 스프링에 완전히 통합하였으므로 편리하게 가져다 사용할 수 있다.


<br><br>

## [Bean Validation - 스프링 적용]
기존의 v1 ~ v5를 제거하고, 기존에 등록한 ItemValidator를 제거한 이후 실행하게 되면 신기하게도
애노테이션 기반의 Bean Validation이 정상 동작한다.
> 특정 필드의 범위를 넘어서는 검증(가격 * 수량의 합은 10000이상) 기능이 빠짐

<br>

### 스프링 MVC가 Bean Validator를 사용할 수 있는 이유
스프링 부트에 spring-boot-starter-validation 라이브러리를 넣으면 자동으로 Bean Validator를 인지하고
스프링에 통합한다.

<br>

### 더하여 스프링 부트는 자동으로 글로벌 Validator로 등록한다.   
`LocalValidatorFactoryBean`을 글로벌 Validator로 등록한다. 이 Validator는 `@NotNull`같은 애노테이션을 보고
검증을 수행한다. 

글로벌 Validator가 등록됐으므로 우리는 `@Validated`, `@Valid` 애노테이션만 적용해도
실제 검증기가 동작한다.   
따라서 검증오류 발생시 `FieldError`, `ObjectError`를 생성해 `BindingResult`에 담는다.

> 단 글로벌 Validator 직접 등록시 기존의 Bean Validator를 글로벌 검증기로 등록하지 않으므로
> 애노테이션 기반의 빈 건증기는 동작하지 않음

<br>

### @Valid? @Validated   
둘 다 사용가능하다, 단 `@Valid`는 추가적인 의존관계를 gradle에 추가해야 사용할 수 있다.   
`@Valid`는 자바 표준 검증 애노테이션이고, `@Validated`는 스프링 전용 검증 애노테이션인데   
두 기능은 거의 동일하기 동작한다. 단, `@Validated`는 내부에 `groups`라는 기능을 포함함

<br>

### 검증 순서
1. `@ModelAttribute` 각각의 필드에 타입 변환 시도
   1. 성공하면 다음으로
   2. 실패하면 typeMismatch FieldError 추가
2. `Validator` 적용

**여기서 중요한 부분은 바인딩에 성공한 필드만 Bean Validation이 적용된다.**   
따지고 보면 타입 변환 및 바인딩에 성공한 필드가 BeanValidation을 적용하는 의미가 있다.   
(즉, 모델 객체에 바인딩 받는 값이 정상으로 들어와야 검증의 의미 있음)

@ModelAttribute -> 각 필드 타입변환 시도 -> 반환에 성공한 필드만 BeanValidation 적용

* `itemName` 에 문자 "A" 입력 타입 변환 성공 -> `itemName` 필드에 `BeanValidation` 적용 
* `price` 에 문자 "A" 입력 -> "A"를 숫자 타입 변환 시도 실패 -> typeMismatch FieldError 추가 ->
  `price` 필드는 `BeanValidation` 적용 X

<br><br>

## [Bean Validation - 에러 코드]
Bean Validation이 기본으로 제공하는 오류 메시지를 좀 더 자세히 변경해보자!

Bean Validation 적용 후 `bindingResult`에 등록된 검증 오류 코드를 살펴보자 오류 코드가 다음과 같이
애노테이션 이름으로 등록된다. (typeMismatch와 유사)
```
Field error in object 'item' on field 'itemName': rejected value []; 
codes [NotBlank.item.itemName,NotBlank.itemName,NotBlank.java.lang.String,NotBlank];
```

실제 애노테이션을 기반으로 오류 코드가 MessageCodesResolver를 통해 순서대로 생성된다.
```
@NotBlank
NotBlank.item.itemName 
NotBlank.itemName 
NotBlank.java.lang.String 
NotBlank

@Range
Range.item.price 
Range.price 
Range.java.lang.Integer 
Range
```

실제 메시지를 등록해보고 아이템 등록화면에서 오류코드를 발생시키면 커스텀 오류가 등록됐음을 알 수 있다.   
보통 `{0}`은 필드명이고 `{1}`, `{2}` ...은 각 애노테이션마다 다르다.

> Range={0}, {2} ~ {1} 허용 Range의 경우 {2} ~ {1}로 해야 정상적으로 낮은 값 ~ 큰 값으로 렌더링 된다.   
> Range에는 Min과 Max 두 가지 속성이 존재하는데 Max가 Min보다 알파벳 기준으로 우선순위가 높기 떄문에
> Max가 {1}로 등록된다.


### Bean Validation 메시지 찾는 순서
1. 생성된 메시지 코드 순서대로 `messageSource`에서 메시지 찾기
2. 애노테이션의 message 속성 사용 -> `@NotBlank(message = "공백! {0}")`
3. 라이브러리가 제공하는 기본 값 사용 -> 공백일 수 없습니다.

<br><br>

## [Bean Validation - 오브젝트 오류]
Bean Validation에서 Field가 아닌 ObjectError는 `@ScriptAssert()`를 사용한다.

하지만 제약이 많고 복합하다는 단점이 있다. 또한 실무에서 검증 기능이 해당 객체의 범위를 넘어서는 경우도
종종 존재하기에, 그런 경우에 대응하기 어렵다.

따라서 오브젝트 오류(글로벌 오류)의 경우 오브젝트 오류 관련 부분만 자바 코드로 작성하는 것을 권장한다.

검증 코드를 따로 메소드로 뽑아서 사용하거나, 클래스를 두어 사용해도 된다.

> 단순하지 않고 기술의 제약이 많다면 기존의 것을 사용해도 좋다!

<br><br>

## [Bean Validation - 수정에 적용]
상품 수정에도 Bean Validation을 적용해보자

- edit(): Item 모델 객체에 @Validated를 추가하고 BindingResult도 추가한다.
- 글로벌 오류 및 검증 오류 발생시 editForm으로 이동하는 코드를 추가한다.
- editForm.html에서도 기존의 addForm.html처럼 검증 코드들을 추가해주면 간단하게 완성 할 수 있음   
  (글로벌 오류 메시지, 상품명, 가격, 수량 필드에 검증 기능 추가)

<br><br>

## [Bean Validation - 한계]
기획자의 요구로 상품을 등록할때와 수정할떄의 요구사항이 달라졌다.

### 상품 등록 요구사항
- 타입 검증
  - 가격, 수량에 문자가 들어가면 검증 오류 처리
- 필드 검증
  - 상품명: 필수, 공백X
  - 가격: 1000원 이상 1백만원 이하
  - 수량: 최대 9999개
- 특정 필드의 범위를 넘어서는 검증
  - 가격 * 수량의 합은 10,000원 이상

### 수정시 요구사항
- 수정시에는 quantity 수량을 무제한으로 변경할 수 있다.
- 등록시에는 id값이 없어도 되지만 수정시에는 id값이 필수다.

<br>

위의 요구사항에 맞춰 Item.java를 변환
- id에 @NotNull을 붙임
- quantity의 @Max(9999) 어노테이션도 제거   

이렇게 되면 수정은 정상동작하지만 등록에서의 문제가 발생한다.
1. 등록시에는 id값이 없으므로 @NotNull의 조건을 위배해 상품이 등록되지 않음
2. 수량을 9999개를 넘겨도 오류가 발생하지 않음

그렇다면 어떻게 해결해야 할까?

> **참고**   
> 현재 구조에서는 수정시 item 의 id 값은 항상 들어있도록 로직이 구성되어 있다. 그래서 검증하지 않아도
된다고 생각할 수 있다. 그런데 HTTP 요청은 언제든지 악의적으로 변경해서 요청할 수 있으므로 서버에서 항상 검증해야 한다. 예를 들어서 HTTP 요청을 변경해서 item 의 id 값을 삭제하고 요청할 수도 있다. 따라서 최종 검증은 서버에서 진행하는 것이 안전한다.

<br><br>

## [Bean Validation - groups
동일한 모델 객체를 등록할때와 수정할 때 각각 다르게 검증하는 방법을 알아보자

### 2가지의 방법이 존재
1. BeanValidation의 groups 기능을 사용한다.
2. Item을 직접 사용하지 않고, ItemSaveForm, ItemUpdateForm과 같은 폼 전송을 위한 별도의 모델 객체를 만들어 사용

### BeanValidation groups 기능 사용
Bean Validation은 groups라는 기능을 제공한다.   
등록시에 검증할 기능, 수정시에 검증할 기능을 나누어 적용할 수 있음

- groups를 적용하기 위해서는 interface 타입으로 저장용, 등록용을 구분한다.(단순 구분용 추상메서드 없음)    
  (`SaveCheck`, `UpdateCheck`)
- javax, hibernate가 제공하는 검증 어노테이션은 groups라는 속성을 가진다 여기에 위에서 만든 인터페이스 타입을 적용한다.   
  (`@NotNull(groups = {SaveCheck.class, UpdateCheck.class})`)
- 이후 컨트롤러의 `@Validated`의 `value` 속성에 적용하고자 하는 인터페이스 타입을 적용하면 된다.(value속성은 생략 가능)
  - 단, groups 기능은 `@Validated`만 존재 `@Valid`는 사용할 수 없다.

<br>

groups의 방식은 코드의 복잡도가 올라간다. 따라서 주로 등록용 객체와 수정용 폼 객체를 분리하여 사용하는 방식을
많이 사용한다.

<br><br>

## [Form 전송 객체 분리 - 소개]
실무에서는 groups를 잘 사용하지 않는다 -> 등록시 폼에서 전달하는 데이터가 Item 도메인 객체와 딱 맞지 않으므로   
(회원 관련 데이터 + 약관 등 수 많은 부가 데이터 넘어옴)

따라서 폼의 데이터를 컨트롤러까지 전달할 별도의 객체를 만듦(ex: `ItemSaveForm`) 이를 이용해 @ModelAttribute 이용   
컨트롤러에서 폼 데이터를 전달받고 필요데이터만 뽑아 `Item`을 생성한다.

<br>

### 폼 데이터 전달을 위한 별도의 객체 사용
`HTML Form` -> `ItemSaveForm` -> `Controller` -> `Item 생성` -> `Repository`   
- 장점: 폼 데이터가 복잡해도 거기에 맞춘 객체를 사용하므로 데이터 전달 쉬움   
  폼 객체를 등록, 수정으로 나누어 사용하므로 검증이 중복되지 않음
- 단점: 컨트롤러에서 폼 데이터 기반 객체 -> Item 객체를 생성해 변환하는 과정 추가
> 일반적으로 픔 등록과 수정은 다른 객체를 사용하는것이 바람직하다. 다루는 데이터와 범위에 차이가 있기 때문   
> (등록: 로그인 ID, 주민번호 -> 수정시에는 필요 없음)

> **참고**: 이름은 어떻게?   
> 의미있게 짓자, ItemSave, ItemSaveForm, ItemSaveRequest, ItemSaveDto 등

> **참고**: 등록, 수정용 뷰 템플릿은 나누자!   
> 어설프게 합치면 분기문으로 인한 고통 받을 수 있음 -> 어설픈 분기문 == 분리해야 할 신호

<br><br>

## [Form 전송 객체 분리 - 개발]
등록 form은 web 패키지에 만든다 -> html 폼의 데이터를 그대로 받아서 Controller에서만 사용하기 때문(화면과 웹에 특화된 기술)

### 등록
`Item` 대신에 `ItemSaveForm`을 전달받음 @Validated로 검증 및 BindingResult로 검증 결과도 받는다.

### 수정
수정의 경우도 등록과 같이 `ItemUpdateForm`을 통해 전달받고 검증 및 검증 결과를 받는다.

### 주의사항
등록, 수정용 객체들의 이름은 기존 Item과는 다르다. 따라서 @ModelAttribute에서 모델의 이름을 `item`으로
변경하지 않게 되면 새로운 객체의 이름이 모델의 이름이 되고
기존의 뷰 템플릿에서 사용하던 "item"이라는 이름을 찾을 수 없게 되고, th:object의 이름도 같이 변경해주어야
오류가 발생하지 않는다.

<br><br>

## [Bean Validation - HTTP 메시지 컨버터]
`@Valid`, `@Validated`는 `HttpMessageConverter`(`@RequestBody`)에도 적용할 수 있다.

> **참고**
> `@ModelAttribute`는 HTTP 요청 파라미터(URL 쿼리 스트링, POST Form)를 다룰때 사용   
> `@RequestBody`sms HTTP Body의 데이터를 객체로 변환할 때 사용한다. 주로 API JSON 요청을 다룰때 사용

<br>

**API의 경우 3가지 경우를 나누어서 생각해야 한다.**
1. `성공 요청`: 성공
2. `실패 요청`: JSON을 객체로 생성하는것 자체가 실패함
    - api는 어떻게든 `JSON -> 객체`로 변경해야 검증을 할 수 있는데 JSON에 오류가 있으면 객체 자체를 생성할 수 없다. 
      따라서 Controller 자체가 호출되지 않고 예외가 발생한다.
3. `검증 오류 요청`: JSON을 객체로 생성하는 것은 성공했으나 검증에서 실패함

<br>

### 성공 요청
정상적으로 JSON 반환된다.

<br>

### 실패 요청
실패 요청의 경우 HttpMessageConverter에서 요청 JSON을 ItemSaveForm 객체로 생성하는데 실패한다.     
이 경우 ItemSaveForm 객체를 만들지 못하므로 **Controller 자체가 실행되지 않고 전에 예외가 발샏한다.**
물론 Validator도 실행되지 않는다.

<br>

### 검증 오류 요청
`return bindingResult.getAllErrors();` 는 `ObjectError` 와 `FieldError` 를 반환   
실제 개발시에는 이 객체를 그대로 사용하기 보단 필요 데이터만 뽑아서 별도의 API스펙을 정의하고   
그에 맞는 객체를 만들어서 반환해야 한다.

<br>

### @ModelAttribute vs @RequestBody
- **@ModelAttribute**   
`@ModelAttribute`는 필드 단위로 세세하게 동작하므로 하나의 필드 타입이 맞지 않아도 나머지 필드는 정상 처리가 가능하다.   
즉 특정 필드가 바인딩 되지 않아도 나머지 정상 바인딩 필드에 `Validator`를 사용한 검증적용 가능하다.

<br>

- **@RequestBody**   
  HttpMessageConverter(`@RequestBody`)는 각각의 필드가 아닌 전체 객체 단위로 적용된다.   
  따라서 메시지 컨버터의 정상 작동으로 ItemSaveForm 객체가 만들어져야 컨트롤러가 호출되고, 검증을 적용할 수 있다.
  @RequestBody는 HttpMessageConverter단계에서 JSON 데이터 -> 객체 바인딩을 실패하면 이후 컨트롤러 호출 및 검증 단계
  자체가 진행되지 않고 예외가 발생한다.

<br><br>

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