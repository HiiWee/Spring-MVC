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
  * `messages` 로 지정하면 `messages.properties` 파일을 읽어서 사용한다.
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