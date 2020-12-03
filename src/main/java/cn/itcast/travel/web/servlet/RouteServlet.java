package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
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
import java.util.Map;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {

    private RouteService routeService = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();

    /**
     * 查询数据并且展示数据 封装前端展示的数据内容
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 0. 获取到前端页面传递的cid的值
        String cidStr = request.getParameter("cid");
        String currentPageStr = request.getParameter("currentPage");
        // 由于get请求方式导致的
        String rname = request.getParameter("rname");// 可以拿到前端页面的搜索内容了
        // 0.1 将cid的值进行转换
        int cid = 0;
        if (cidStr != null && !"null".equals(cidStr)) {
            cid = Integer.parseInt(cidStr);
        }
        int currentPage = 1;
        if (currentPageStr != null) {
            currentPage = Integer.parseInt(currentPageStr);
        }

        // 1. 获取到cid之后 我们需要通过cid 去查询线路相关的信息
        RouteServiceImpl routeService = new RouteServiceImpl();
        // PageBean 对象中有所有我们想要在前端页面显示的数据
        PageBean<Route> pb = routeService.pageQuery(cid, currentPage, 5, rname); // 这样做的好处就是 程序具有了可扩展性

        // 2. 将结果响应给前端页面
        response.setContentType("application/json;charset=utf-8"); // 告知前端页面 我们响应的数据的格式和编码
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), pb);
    }

    /**
     * 此方法用来根据rid的值查找线路相关的所有数据信息
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");
        // 借助于service中的方法 封装Route对象
        RouteServiceImpl routeService = new RouteServiceImpl();
        // 返回了一个Route对象 该对象中我们封装了 所有需要在前端页面显示的数据
        Route route = routeService.findOne(rid);


        response.setContentType("application/json;charset=utf-8"); // 告知前端页面 我们响应的数据的格式和编码
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), route);
    }


    /**
     * 添加收藏 需要用户的id和线路的id
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 0. 获取参数 准备参数
        // 获取rid
        String rid = request.getParameter("rid");
        // 获取uid
        User user = (User) request.getSession().getAttribute("user");
        int uid = user.getUid();

        // 调用service层 处理逻辑
        FavoriteServiceImpl favoriteService = new FavoriteServiceImpl();
        favoriteService.add(rid, uid);


    }


    /**
     * 用来判断用户是否收藏
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 0. 获取参数 准备参数
        // 获取rid
        String rid = request.getParameter("rid");
        // 获取uid
        User user = (User) request.getSession().getAttribute("user");
        int uid = user.getUid();

        // 调用service层 处理逻辑
        FavoriteServiceImpl favoriteService = new FavoriteServiceImpl();
        boolean favorite = favoriteService.isFavorite(rid, uid);

        // 将favorite 响应给前端页面
        response.setContentType("application/json;charset=utf-8"); // 告知前端页面 我们响应的数据的格式和编码
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), favorite);

    }


    /**
     * 该方法用来给收藏线路进行排序
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void favoriteRank(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 0. 有前端参数传递吗?  需要
//        String currentPageStr = request.getParameter("currentPage");
//        String pageSizeStr = request.getParameter("pageSize");
        // 获取前端页面传递的参数的
        Map<String, String[]> map = request.getParameterMap();// map中可以获取全部的参数
        // map.get("currentPage").length > 0 && map.get("currentPage")


        int currentPage = 1;
        // 0.1 处理参数
        if (map.get("currentPage") != null && map.get("currentPage").length > 0) {
            String currentPageStr = map.get("currentPage")[0];
            currentPage = Integer.parseInt(currentPageStr);
        }

        int pageSize = 8;
        if (map.get("pageSize") != null && map.get("pageSize").length > 0) {
            String pageSizeStr = map.get("pageSize")[0];
            pageSize = Integer.parseInt(pageSizeStr);
        }

        // 1. 调用service中的方法 帮我完成pb对象的封装
        RouteServiceImpl routeService = new RouteServiceImpl();
        // int currentPage,int pageSize,ArrayList<String> rname
        PageBean<Route> pageBean = routeService.pageQuery(currentPage, pageSize, map);


        // 2. 将pageBean对象 响应给前端页面
        response.setContentType("application/json;charset=utf-8"); // 告知前端页面 我们响应的数据的格式和编码
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), pageBean);


    }




    /**
     * 查询热门线路
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findHotRoute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取前端页面的参数cid
        String cidStr = request.getParameter("cid");
        RouteServiceImpl routeService = new RouteServiceImpl();
        int cid = 0;
        if (cidStr != null && !"null".equals(cidStr)) {
            cid = Integer.parseInt(cidStr);
        }
        PageBean<Route> pageBean = routeService.pageQuery(cid);


        // 2. 将pageBean对象 响应给前端页面
        response.setContentType("application/json;charset=utf-8"); // 告知前端页面 我们响应的数据的格式和编码
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), pageBean);

    }
}
