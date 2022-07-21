package hello.servlet.web.frontcontroller.v2;

import hello.servlet.web.frontcontroller.MyView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ControllerV2 {

    // 기존의 V1과 동일하지만 반환 타입이 MyView 이다.
    MyView process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

}
