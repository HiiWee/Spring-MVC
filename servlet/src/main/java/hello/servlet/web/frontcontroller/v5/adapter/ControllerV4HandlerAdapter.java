package hello.servlet.web.frontcontroller.v5.adapter;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.v4.ControllerV4;
import hello.servlet.web.frontcontroller.v5.MyHandlerAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ControllerV4HandlerAdapter implements MyHandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return (handler instanceof ControllerV4);
    }

    @Override
    public ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {
        ControllerV4 controller = (ControllerV4) handler;

        Map<String, String> paramMap = createParamMap(request);
        Map<String, Object> model = new HashMap<>();

        // 4. [핸들러 어댑터] `handler` 호출
        String viewName = controller.process(paramMap, model);

        ModelView mv = new ModelView(viewName);
        // 논리적 view 이름만 반환 하므로 model을 추가해줘야 한다.
        mv.setModel(model);

        // 5. [핸들러 어댑터] `ModelView` 반환 (프론트 컨트롤러로)
        return mv;
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
