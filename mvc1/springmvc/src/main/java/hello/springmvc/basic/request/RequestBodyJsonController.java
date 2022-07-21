package hello.springmvc.basic.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.springmvc.basic.HelloData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * {"username":"hello", "age":20}
 * content-type: application/json
 */
@Slf4j
@Controller
public class RequestBodyJsonController {
    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/request-body-json-v1")
    public void requestBodyJsonV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // request를 이용해 직접 HTTP 메시지 바디를 읽어서 문자로 저장
        // 저장한 문자를 objectMapper를 통해 JSON을 파싱해 자바 객체로 변환
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}", messageBody);
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        response.getWriter().write("ok");
    }

    // @RequestBody 사용: HTTP message body를 파라미터로 받기
    @ResponseBody
    @PostMapping("/request-body-json-v2")
    public String requestBodyJsonV2(@RequestBody String messageBody) throws IOException {
        // @RequestBody를 통해 메시지 바디를 파라미터로 받아옴
        log.info("messageBody={}", messageBody);
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        return "ok";
    }

    // @RequestBody 사용: 객체를 파라미터로 받기
    @ResponseBody
    @PostMapping("/request-body-json-v3")
    public String requestBodyJsonV3(@RequestBody HelloData data) {
        // @RequestBody: 애초에 메시지 바디가 아닌 파싱되고, 만들어진 객체 HelloData를 파라미터로 받음
        log.info("username={}, age={}", data.getUsername(), data.getAge());
        return "ok";
    }

    // HttpEntity 사용: HttpEntity 객체를 이용하면  메시지 컨버터가 HTTP 메시지 바디의 내용을 우리가
    //원하는 문자나 객체 등으로 변환해준다. 이후 해당 객체가 HttpEntity에 담기고 getBody()메소드를 통해, HelloData 객체를 받고 로그를 출력하고 종료
    @ResponseBody
    @PostMapping("/request-body-json-v4")
    public String requestBodyJsonV4(HttpEntity<HelloData> httpEntity) {
        // Generic 타입인 HelloData 클래스로 반환된다.
        HelloData data = httpEntity.getBody();
        log.info("username={}, age={}", data.getUsername(), data.getAge());
        return "ok";
    }

    // HttpMessageConverter는 요청을 받을때도 이용되지만 응답을 할때도 이용할 수 있다. 단, @ResponseBody가 있어야한다.
    // 메시지 컨버터는 HelloData객체를 JSON으로 변환시켜서 반환한다.
    @ResponseBody
    @PostMapping("/request-body-json-v5")
    public HelloData requestBodyJsonV5(@RequestBody HelloData data) {
        log.info("username={}, age={}", data.getUsername(), data.getAge());
        return data;
    }

    // V5에서 반환 타입을 객체가 아닌, HttpEntity 사용
    @ResponseBody
    @PostMapping("/request-body-json-v6")
    public HttpEntity<HelloData> requestBodyJsonV6(@RequestBody HelloData data) {
        // Generic 타입인 HelloData 클래스로 반환된다.
        log.info("username={}, age={}", data.getUsername(), data.getAge());
        return new HttpEntity<>(data);
    }
}
