package hello.servlet.web.frontcontroller.v4;

import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// * --> v1/ 하위에 어떤 URL이 들어와도 모두 현재 서블릿을 호출한다.
@WebServlet(name = "frontControllerServletV4", urlPatterns = "/front-controller/v4/*")
public class FrontControllerServletV4 extends HttpServlet {

    // key: URL, value: ControllerV1
    private Map<String, ControllerV4> controllerV4Map = new HashMap<>();

    // 서블릿이 최초에 생성될 때 아래와 같이 객체들이 미리 저장된다.
    public FrontControllerServletV4() {
        controllerV4Map.put("/front-controller/v4/members/new-form", new MemberFormControllerV4());
        controllerV4Map.put("/front-controller/v4/members/save", new MemberSaveControllerV4());
        controllerV4Map.put("/front-controller/v4/members", new MemberListControllerV4());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("FrontControllerServletV4.service");

        String requestURI = request.getRequestURI();
        // 다형성 활용
        ControllerV4 controllerV4 = controllerV4Map.get(requestURI);
        if (controllerV4 == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Map<String, String> paramMap = createParamMap(request);
        Map<String, Object> model = new HashMap<>();

        String viewName = controllerV4.process(paramMap, model);

        System.out.println(model);
        // 논리 이름을 물리 이름으로 변경해주는 view-resolve 작업을 해줘야 한다.
        MyView view = viewResolver(viewName);
        view.render(model, request, response);
    }
    
    // view의 논리이름을 가지고 실제 물리 이름을 만들어서 반환해줌
    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    private Map<String, String> createParamMap(HttpServletRequest request) {
        // paramMap을 넘겨줘야함 이런 디테일한 로직은 메소드로 뽑아내는것이 좋다.
        Map<String, String> paramMap = new HashMap<>();
        // 해당 URL로 들어온 모든 파라미터 Map에 저장
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}
