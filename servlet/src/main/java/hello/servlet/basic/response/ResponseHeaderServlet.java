package hello.servlet.basic.response;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "responseHeaderServlet", urlPatterns = "/response-header")
public class ResponseHeaderServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // [status-line]
        response.setStatus(HttpServletResponse.SC_OK); // 매직넘버 사용 지양, 직접 적는것 보다 상수를 지정하는것이 좋음

        // [response-headers]
        // response.setHeader("Content-Type", "text/plain;charset=utf-8");  // content-type 편의 메서드 존재
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("my-header", "hello");   //사용자가 만든 임의 헤더

        // [Header 편의 메서드: Content 편의 메서드]
        content(response);
        // [Header 편의 메서드: 쿠키 편의 메서드]
        cookie(response);
        // [Header 편의 메서드: redirect 편의 메서드]
        redirect(response);

        // [message body]
        PrintWriter writer = response.getWriter();
        writer.print("ok");
    }

    // Content 편의 메서드: 좀 더 편리하게 Content-Type 세팅 가능
    private void content(HttpServletResponse response) {
        //Content-Type: text/plain;charset=utf-8
        //Content-Length: 2
        //response.setHeader("Content-Type", "text/plain;charset=utf-8");
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        //response.setContentLength(2); //(생략시 자동 생성)
    }

    // 쿠키 편의 메서드: setHeader로도 가능하지만 좀 더 편리한 메서드 제공해줌
    private void cookie(HttpServletResponse response) {
        //Set-Cookie: myCookie=good; Max-Age=600;
        //response.setHeader("Set-Cookie", "myCookie=good; Max-Age=600");   // setHeader를 이용해 쿠키를 넣을 수 있다.
        Cookie cookie = new Cookie("myCookie", "good");
        cookie.setMaxAge(600); //600초
        response.addCookie(cookie);
    }

    // redirect 편의 메서드
    private void redirect(HttpServletResponse response) throws IOException {
        //Status Code 302
        //Location: /basic/hello-form.html

//        response.setStatus(HttpServletResponse.SC_FOUND); //302 redirect
//        response.setHeader("Location", "/basic/hello-form.html");
        response.sendRedirect("/basic/hello-form.html");    // 한 줄로 리다이렉트를 편리하게 사용할 수 있다.
    }
}
