package hello.servlet.web.frontcontroller.v5.adapter;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.v3.ControllerV3;
import hello.servlet.web.frontcontroller.v5.MyHandlerAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ControllerV3HandlerAdapter implements MyHandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return (handler instanceof ControllerV3);
    }

    @Override
    public ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {
        // supports에서 이미 ControllerV3만 가져오므로 캐스팅을 해줘도 안전하다.
        ControllerV3 controller = (ControllerV3) handler;

        Map<String, String> paramMap = createParamMap(request);
        // 4. [핸들러 어댑터] `handler` 호출
        ModelView mv = controller.process(paramMap);
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
