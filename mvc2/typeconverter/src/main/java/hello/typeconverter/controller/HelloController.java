package hello.typeconverter.controller;

import hello.typeconverter.type.IpPort;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HelloController {

    @GetMapping("/hello-v1")
    public String helloV1(HttpServletRequest request) {
        String data = request.getParameter("data"); // 문자 타입 조회
        int intValue = Integer.parseInt(data); // 숫자 타입으로 변경
        System.out.println("intValue = " + intValue);
        return "ok";
    }

    // 스프링에서 편한 타입 컨버팅
    @GetMapping("/hello-v2")
    public String helloV2(@RequestParam Integer data) {
        System.out.println("data = " + data);
        return "ok";
    }

    @GetMapping("/ip-port")
    public String ipPort(@RequestParam IpPort ipPort) {
        log.info("ipPort ip = {}", ipPort.getIp());
        log.info("ipPort port = {}", ipPort.getPort());
        return "ok";
    }

    // @RequestBody는 TypeConverter가 아닌 HttpMessageConverter가 동작한다.
    @GetMapping("/ip-port2")
    public String ipPortV2(@RequestBody IpPort ipPort) {
        log.info("ipPort ip = {}", ipPort.getIp());
        log.info("ipPort port = {}", ipPort.getPort());
        return "ok";
    }
}