package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;

public class FavoriteDaoImpl implements FavoriteDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public Favorite findByRidAndUid(int rid, int uid) {
        Favorite favorite = null;
        try {
            String sql = " SELECT * FROM tab_favorite WHERE rid = ? AND uid = ? ";
            // queryForObject 如果有数据则正常封装 如果没有数据 抛出异常
            favorite = template.queryForObject(sql, new BeanPropertyRowMapper<>(Favorite.class), rid, uid);
        } catch (DataAccessException e) {
        }
        return favorite;
    }

    /**
     *  获取当前线路被收藏的次数
     * @param rid
     * @return
     */
    @Override
    public int findCountByRid(int rid) {
        String sql = "SELECT COUNT FROM tab_route WHERE rid = ?";
        Integer count = template.queryForObject(sql, Integer.class, rid);
        return count;
    }

    // 当我们添加线路成功的时候 需要我们修改收藏的次数 ?
    @Override
    public void add(int rid, int uid) {
        // 0. 编写sql语句
        String sql = "INSERT INTO tab_favorite VALUES(?,NOW(),?)";
        // 1. 执行sql
        template.update(sql, rid, uid);

        // 2. 因为我们 收藏线路了 所以我们需要将线路收藏次数+1
        // count 是收藏次数 收藏次数 = 之前的次数 + 1
        int count = findCountByRid(rid);
        count++;  // count = count + 1;
        String sql2 = "UPDATE tab_route SET COUNT = ? WHERE rid = ? ";
        template.update(sql2,count,rid);
    }
}