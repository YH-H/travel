package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteImgDaoImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.RouteService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RouteServiceImpl implements RouteService {
    private RouteDao routeDao = new RouteDaoImpl();

    private RouteImgDao routeImgDao = new RouteImgDaoImpl();

    private SellerDao sellerDao = new SellerDaoImpl();

    private FavoriteDao favoriteDao = new FavoriteDaoImpl();

    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize, String rname) {
        PageBean<Route> pageBean = new PageBean<Route>();
        // 我们想根据cid查询所有的该线路的数据
        // 此list中 有所有需要在前端页面展示的数据
        // ( 用当前页码 - 1 ) * 每页显示的条数
        int start = (currentPage - 1) * 5;
        List<Route> list = routeDao.findByPage(cid, start, 5, rname);
        pageBean.setList(list);
        // 将pageBean对象的pageSize属性进行赋值为5
        pageBean.setPageSize(5);
        // 给pageBean对象的totalCount进行赋值
        int totalCount = routeDao.findTotalCount(cid, rname);
        pageBean.setTotalCount(totalCount);
        // 给pageBean对象的totalPage进行赋值
        // 总页数 = 总记录数 / 每页显示的条数
        int totalPage = totalCount % 5 == 0 ? totalCount / 5 : totalCount / 5 + 1;
        pageBean.setTotalPage(totalPage);
        pageBean.setCurrentPage(currentPage);
        return pageBean;
    }


    public PageBean<Route> pageQuery(int cid) {
        PageBean<Route> pageBean = new PageBean<Route>();
        // 我们想根据cid查询所有的该线路的数据
        // 此list中 有所有需要在前端页面展示的数据
        // ( 用当前页码 - 1 ) * 每页显示的条数
        List<Route> list = routeDao.findByPage(cid);
        pageBean.setList(list);

        return pageBean;
    }

    /**
     *   该方法需要帮我们封装 所有需要在前端页面显示的数据
     * @param ridStr
     * @return
     */
    @Override
    public Route findOne(String ridStr) {
        // 0. 根据rid去数据库中查找到线路相关的基本信息
        RouteDaoImpl routeDao = new RouteDaoImpl();
        // 因为数据库中需要的是int类型的rid  所以我们需要将参数rid进行强转
        int rid = Integer.parseInt(ridStr);
        // 调用findOne方法 封装线路基本信息
        Route route = routeDao.findOne(rid);
        // 商家信息
        int sid = route.getSid(); // sid 就是商家的id
        Seller seller = sellerDao.findById(sid);
        route.setSeller(seller);

        // 图片信息
        int rid1 = route.getRid();
        List<RouteImg> list = routeImgDao.findByRid(rid1);
        route.setRouteImgList(list);



        return route;
    }



    @Override
    public PageBean<Route> pageQuery(int currentPage, int pageSize, Map<String, String[]> map) {
        if(currentPage <= 0){
            currentPage = 1;
        }

        if (pageSize <= 0) {
            pageSize = 8;
        }

        // 构建空的PageBean对象
        PageBean<Route> pageBean = new PageBean<>();
        pageBean.setCurrentPage(currentPage);
        pageBean.setPageSize(pageSize);

        // 需要去数据库中查询数据并且封装pageBean对象(list)
        List<Route> list = routeDao.findByPage(0, pageSize, map);
        pageBean.setList(list);

        return pageBean;
    }
}
