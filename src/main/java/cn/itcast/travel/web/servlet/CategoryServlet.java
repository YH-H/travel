package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.service.impl.CategoryServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/category/*") //
public class CategoryServlet extends BaseServlet {
    public void findAll (HttpServletRequest request,HttpServletResponse response) throws IOException {
        //
        //
        CategoryServiceImpl categoryService = new CategoryServiceImpl();
        //
        List<Category> list = categoryService.findAll();
        //
        response.setContentType("application/json;charset=utf-8");

        ObjectMapper obj = new ObjectMapper();
        obj.writeValue(response.getWriter(),list);
    }


}
