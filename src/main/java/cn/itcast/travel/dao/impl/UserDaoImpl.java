package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
// 根据导包的位置 我们可以观察出 JdbcTemplate 是spring中的一个类
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserDaoImpl implements UserDao {

    // new 后边调用的方法就是构造方法(构造方法的方法名称与类名一致并且没有返回值)
    // JDBCUtils.getDataSource()  直接通过类名调用方法名称 那么该方法为静态方法(该方法被static修饰)
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 根据用户名查找用户
     *
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {
        // 0.定义sql语句
        String sql = "SELECT * FROM tab_user WHERE username = ?";
        // 1.执行sql语句
        // template的queryForObject 可以将sql语句的结果封装为一个对象
        User user = template.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), username);

        return user;
    }

    /**
     * 演示sql注入攻击
     *
     * @param username
     * @param password
     * @return
     */
    public List<User> findUser(String username, String password) {
        // 0.定义sql语句
        String sql = "SELECT * FROM tab_user WHERE username = ? AND password = ?";
        // 1.执行sql语句
        // 第一个参数是要执行的sql语句
        // 第二个参数是要将sql语句的结果封装为什么类型
        // 从第三个参数开始我们可以给每一个占位符(?就是占位符) 进行赋值
        /*
         *   query 是进行查询的方法 该方法会将sql语句的结果封装为list集合(集合中是一个一个的对象)
         *       如果你的sql语句是以SELECT开头 那么该sql语句就是查询语句
         * */
        List<User> list = template.query(sql,
                // 根据数据库的字段名称给 对象的成员变量赋值 (*** 单词拼写必须要完全一致才可以封装数据)
                new BeanPropertyRowMapper<>(User.class), username, password);
        return list;
    }


    @Override
    public void save(User user) {
        //1.定义sql
        String sql = "INSERT INTO tab_user(username,PASSWORD) VALUES(?,?)";
        //2.执行sql
        // query 和 queryForObject都是查询相关的sql(以SELECT开头)
        template.update(sql, user.getUsername(), user.getPassword());

    }

    /**
     * 根据激活码查询用户对象
     *
     * @param code
     * @return
     */
    @Override
    public User findByCode(String code) {
        User user = null;

        return user;
    }

    /**
     * 修改指定用户激活状态
     *
     * @param user
     */
    @Override
    public void updateStatus(User user) {
    }

    /**
     * 根据用户名和密码查询的方法 作为登录相关方法
     *
     * @param username 是从前端页面一步一步传递过来
     * @param password
     * @return
     */
    @Override
    public User findByUsernameAndPassword(String username, String password) {
        // 定义sql语句
        String sql = "select * from tab_user where username = ? and password = ?";
        // template.queryForObject 方法有一个异常 如果有数据则正常封装对象 如果没有数据则封装失败并且抛出异常
        try {
            //一旦try后边的大括号中的代码 出现异常 那么就会执行catch中的代码 而不会抛出异常
            User loginUser = template.queryForObject(sql,
                    new BeanPropertyRowMapper<>(User.class),
                    username, password);
            return loginUser;
        } catch (Exception e) {
            // 返回null值代表 用户名或密码错误
            return null;
        }
    }

    @Override
    public User findOne(String uid) {
        // 定义sql语句
        String sql = "select * from tab_user where uid = ?";
        // 执行sql语句
        return template.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), uid);
    }

    /**
     * dao中修改用户的信息
     *
     * @param map
     * @return
     */
    @Override
    public boolean modifyUser(Map<String, String[]> map) {
        String sql = "UPDATE tab_user SET PASSWORD = ?,NAME = ?,sex = ?,telephone = ?,email = ? WHERE uid = ?";
        ArrayList<Object> list = new ArrayList<>();
        if (map != null) {
            if (map.get("password")[0] != null && map.get("password")[0].length() > 0 && !"null".equals(map.get("password")[0])) {
                list.add(map.get("password")[0]);
            }
            // 选中一个区域 按Ctrl+R 可以替换选中区域的匹配文本
            if (map.get("name")[0] != null && map.get("name")[0].length() > 0 && !"null".equals(map.get("name")[0])) {
                list.add(map.get("name")[0]);
            }
            if (map.get("sex")[0] != null && map.get("sex")[0].length() > 0 && !"null".equals(map.get("sex")[0])) {
                list.add(map.get("sex")[0]);
            }
            if (map.get("telephone")[0] != null && map.get("telephone")[0].length() > 0 && !"null".equals(map.get("telephone")[0])) {
                list.add(map.get("telephone")[0]);
            }
            if (map.get("email")[0] != null && map.get("email")[0].length() > 0 && !"null".equals(map.get("email")[0])) {
                list.add(map.get("email")[0]);
            }
            if (map.get("uid")[0] != null && map.get("uid")[0].length() > 0 && !"null".equals(map.get("uid")[0])) {
                list.add(map.get("uid")[0]);
            }
        }

        // 受影响的行数
        int update = template.update(sql, list.toArray());

        return update == 1;
    }

    @Override
    public boolean deleteUser(String uid) {
        String sql = "DELETE FROM tab_user WHERE uid = ?";
        int update = template.update(sql, uid);
        return update == 1;
    }

    /**
     * 查询所有用户基本信息
     *
     * @return
     */
    public List<User> findAllUser() {
        String sql = "SELECT * FROM tab_user WHERE STATUS != 'Y'";
        List<User> list = template.query(sql, new BeanPropertyRowMapper<>(User.class));
        return list;
    }
}
