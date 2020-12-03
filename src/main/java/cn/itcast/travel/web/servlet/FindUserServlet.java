package cn.itcast.travel.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/findUserServlet")
public class FindUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //session 会话相关的技术 我们可以向Session中存入数据 在其他位置可以从session中取出数据
        //可以存一个已经登录的User对象 当我们登录成功的时候 可以将User对象的信息 存入session中
        //从session中获取登录用户
        Object user = request.getSession().getAttribute("user");
        //将user写回客户端/
        response.setContentType("application/json;charset=utf-8");
        //是将Java对象转换为前端识别的对象（json对象）
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getWriter(),user);


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
