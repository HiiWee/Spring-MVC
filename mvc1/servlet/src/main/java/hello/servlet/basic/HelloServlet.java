package hello.servlet.basic;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "helloServlet", urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("HelloServlet.service");
        System.out.println("request = " + request);
        System.out.println("response = " + response);
        
        // 쿼리스트링 값 가져오기
        String username = request.getParameter("username");
        System.out.println(username);

        // 응답 메시지 보내기
        // HTTP Content-Type에 들어감
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        // HTTP Message Body에 데이터가 들어간다.
        response.getWriter().write("hello " + username);
    }
}
