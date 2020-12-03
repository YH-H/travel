package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/favorite/*")
public class FavoriteServlet extends BaseServlet {

    public void isLogin(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        // 判断登录状态不需要 去dao中查询
        // 因为登录状态是记录在session中的

        Object user = request.getSession().getAttribute("user");

        // 将user对象 响应给前端页面
        response.setContentType("application/json;charset=utf-8"); // 告知前端页面 我们响应的数据的格式和编码
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), user);

    }

}
