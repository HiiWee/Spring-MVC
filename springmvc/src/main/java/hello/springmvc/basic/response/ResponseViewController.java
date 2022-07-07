package hello.springmvc.basic.response;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ResponseViewController {

    // ModelAndView를 반환
    @RequestMapping("/response-view-v1")
    public ModelAndView responseViewV1() {
        ModelAndView mv = new ModelAndView("response/hello")
                .addObject("data", "hello!");

        return mv;
    }

    // 뷰의 논리적 이름(String) 반환
    @RequestMapping("/response-view-v2")
    public String responseViewV2(Model model) {
        model.addAttribute("data", "hello!");
        return "response/hello";
    }

    // @Controller 사용 및 경로의 이름과 논리적 뷰의 이름이 동일하면 void로 둘 수 있음: 권장하지 않음
    @RequestMapping("/response/hello")
    public void responseViewV3(Model model) {
        model.addAttribute("data", "hello!");
    }
}
