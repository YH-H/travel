package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

import java.util.List;
import java.util.Map;

public interface UserDao {

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    public User findByUsername(String username);

    /**
     * 用户保存
     * @param user
     */
    public void save(User user);

    User findByCode(String code);

    void updateStatus(User user);

    User findByUsernameAndPassword(String username, String password);

    User findOne(String uid);

    boolean modifyUser(Map<String, String[]> map);

    boolean deleteUser(String uid);


}
