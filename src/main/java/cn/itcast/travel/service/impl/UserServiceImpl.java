package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();

    /**
     * 注册用户
     *
     * @param user
     * @return
     */
    @Override
    public boolean regist(User user) {
        // 0. 因为是注册 所以我们需要向数据库中插入数据
        // 0.1 调用dao层的方法进行注册
        userDao.save(user);

        return true;
    }

    /**
     * 激活用户
     *
     * @param code
     * @return
     */
    @Override
    public boolean active(String code) {
        return true;
    }

    /**
     * 登录方法
     *
     * @param user
     * @return
     */
    @Override
    public User login(User user) {
        // 0. 获取前端页面传递的参数
        String username = user.getUsername();
        String password = user.getPassword();
        // 1. 查询数据库 查找是否有该用户  一旦涉及到查询数据库相关的代码 我们必须要写在DAO层(包)
        UserDaoImpl userDao = new UserDaoImpl();
        User loginUser = userDao.findByUsernameAndPassword(username, password);
        // 2.给前端页面响应数据
        return loginUser;
    }

    /**
     * 查询所有普通用户信息
     *
     * @param currentPage
     * @return
     */
    public PageBean<User> findAllUser(String currentPage) {
        // 0.构建PageBean对象
        PageBean<User> pageBean = new PageBean<>();
        // 1.进行参数的赋值
        // 1.1 给currentPage赋值
        int current = 1;
        if (currentPage != null && currentPage.length() > 0 && !"null".equals(currentPage)) {
            current = Integer.parseInt(currentPage);
        }
        pageBean.setCurrentPage(current);
        // 1.2 给list赋值 (需要去DAO中 查找)
        UserDaoImpl userDao = new UserDaoImpl();
        List<User> list = userDao.findAllUser();
        pageBean.setList(list);

        // 1.3 还有三个参数没有赋值(暂时先不进行赋值 等到完善功能的时候 再进行赋值)


        return pageBean;
    }

    public User findOne(String uid) {
        User user = userDao.findOne(uid);
        return user;
    }

    /**
     *  根据传递的参数 修改用户
     * @param map
     * @return
     */
    public boolean modifyUser(Map<String, String[]> map) {
        return userDao.modifyUser(map);
    }

    public boolean deleteUser(String uid) {
        return userDao.deleteUser(uid);
    }
}
