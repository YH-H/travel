package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*") // /user/add /user/find
public class UserServlet extends BaseServlet {

    //声明UserService业务对象
    private UserService service = new UserServiceImpl();

    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 0. 获取前端页面传递的参数
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // 1. 将前端页面获取到的参数 传递给UserServiceImpl中的login方法
        UserServiceImpl userService = new UserServiceImpl();
        // 1.1 因为 login 方法需要的参数是User对象,所以我们把从前端页面获取的参数 封装为User对象
        User user = new User(); // user对象 相当于一个容纳参数的容器
        user.setUsername(username);
        user.setPassword(password);
        // 1.2 将封装好的User对象 传递给login方法
        User loginUser = userService.login(user); // loginUser对象是从UserServiceImpl的login方法中 返回的

        // 我期望在登录成功的时候 把loginUser存入 Session中
        if(loginUser != null){
            // 当代码执行到这个位置的时候 我们认为登录成功了

            // 下面这行代码 就是把loginUser存入session中了
            request.getSession().setAttribute("user",loginUser);
        }

        // 设置响应头信息 目的/功能 将servlet响应给前端页面的参数 指定类型和编码
        response.setContentType("application/json;charset=utf-8");// application/json 叫MIME类型

        // 将loginUser对象 序列化为前端  json对象(能够被前端读懂的对象)
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(),loginUser);



        // 在这个位置 我们可以根据loginUser对象是否为null值 来判断登录成功还是登录失败
       /* if(loginUser != null){
            // 说明登录成功
            // 响应给前端页面数据


            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(response.getWriter(),loginUser);
            // 跳转到首页
//            request.getRequestDispatcher("/index.html").forward(request,response);
        }else{
            // 说明登录失败
            // 响应给前端页面数据
            response.getWriter().write("恭喜你 登录失败");
        }*/
    }

    public void regist(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 0. 获取前端参数
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // 1. 调用service中的regist方法
        UserServiceImpl userService = new UserServiceImpl();

        // 1.1 将前端传递的参数 封装为User对象
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        // 1.2 执行regist方法
        boolean regist = userService.regist(user);

        // 2. 将结果响应给前端页面
        response.setContentType("application/json;charset=utf-8"); // 告知前端页面 我们响应的数据的格式和编码
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(),regist);
    }


    public void findAllUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 0. 从前端页面 获取参数
        String currentPage = request.getParameter("currentPage");

        // 1. 调用service中的方法 来进行查询所有普通用户
        UserServiceImpl userService = new UserServiceImpl();
        //  先将方法的整体结构写出来(参数,返回值)  alt+enter 在对应的位置生成方法
        PageBean<User> pageBean = userService.findAllUser(currentPage);

        // 2. 将结果响应给前端页面
        response.setContentType("application/json;charset=utf-8"); // 告知前端页面 我们响应的数据的格式和编码
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(),pageBean);


    }

    public void findOne(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 0. 获取前端页面参数
        String uid = request.getParameter("uid");
        // 1. 根据uid的值 去service中获取User对象
        UserServiceImpl userService = new UserServiceImpl();
        User user = userService.findOne(uid);


        response.setContentType("application/json;charset=utf-8"); // 告知前端页面 我们响应的数据的格式和编码
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(),user);

    }



    public void modifyUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 可以获取到前端页面所有的参数
        Map<String, String[]> map = request.getParameterMap();

        // 调用service中的方法 完成修改
        UserServiceImpl userService = new UserServiceImpl();
        boolean f = userService.modifyUser(map);

        response.setContentType("application/json;charset=utf-8"); // 告知前端页面 我们响应的数据的格式和编码
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(),f);
    }


    public void deleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uid = request.getParameter("uid");
        UserServiceImpl userService = new UserServiceImpl();
        boolean f = userService.deleteUser(uid);
        response.setContentType("application/json;charset=utf-8"); // 告知前端页面 我们响应的数据的格式和编码
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(),f);

    }


}
