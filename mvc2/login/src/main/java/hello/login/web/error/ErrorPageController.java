package hello.login.web.error;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class ErrorPageController {

    @RequestMapping("/error-page/404")
    public String error404() throws IOException {
        log.info("404 Error!");
        return "error-page/404";
    }

    @RequestMapping("/error-page/500")
    public String error500() throws IOException {
        log.info("500 Error!");
        return "error-page/500";
    }
}
