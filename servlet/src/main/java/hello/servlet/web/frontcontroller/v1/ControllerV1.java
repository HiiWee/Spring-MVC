package hello.servlet.web.frontcontroller.v1;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ControllerV1 {

    // 프론트 컨트롤러가 매핑정보를 찾아서 호출할 때
    // 각각의 컨트롤러에서 아래 메소드를 구현하면 프론트 컨트롤러는 일관성 있게 작업할 수 있다.
    void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
