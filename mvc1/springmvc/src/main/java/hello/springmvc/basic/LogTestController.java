package hello.springmvc.basic;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@Slf4j 이용하게 되면 필드로 Logger를 선언하지 않아도 된다.
@RestController // @Controller는 아래 컨트롤러의 반환은 뷰가 반환되지만, @RestController는 String이 바로 반환된다.
public class LogTestController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping("/log-test")
    public String logTest() {
        String name = "Spring";

        System.out.println("name = " + name);

        // 로그의 진가
        log.trace("trace log={}", name);
        log.debug("debug log={}", name);    // 디버깅시 출력되는 로그
        log.info(" info log={}", name);     // 중요한 정보(비즈니스정보), 운영 시스템서 봐야할 정보
        log.warn(" warn log={}", name);     // 경고
        log.error("error log={}", name);    // 에러, 반드시 확인해봐야함
        /*
         * 위는 로그의 레벨 순서
         * 특정 로그레벨을 설정하면 해당 로그레벨 포함 및 하위 레벨의 로그가 모두 출력
         * 개발서버는 디버그 정도로 해놓고, 로컬 피시에서는 트레이스 디버그로 주로 사용하다가
         * 운영서버는 info 레벨로 놓고 로그를 적용한다.
         */
        return "ok";
    }
}
