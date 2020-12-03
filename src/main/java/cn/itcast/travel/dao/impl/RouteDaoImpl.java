package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RouteDaoImpl implements RouteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 该方法要查询总记录数
     *
     * @param cid
     * @param rname
     * @return SQL : SELECT COUNT(*) FROM tab_route WHERE [ cid = '5' ] AND rname LIKE '%厦门%'
     */
    @Override
    public int findTotalCount(int cid, String rname) {
        // 此sql相当于 一个模板
        String sql = "SELECT COUNT(*) FROM tab_route WHERE 1 = 1 ";

        //可以使用集合来存储参数  args集合 用来存放参数
        ArrayList<Object> args = new ArrayList<>();
        // 判断参数是否有值,如果有值则进行条件的拼接
        if (cid > 0) {
            sql += " and cid = ? ";
            args.add(cid);
        }

        if (rname != null && rname.length() > 0 && !"null".equals(rname)) {
            // 只有三个条件都满足的时候 我们才认为rname有值 再进行sql的拼接
            sql += " and rname like ? ";
            args.add("%" + rname + "%");
        }

        // 因为在sql的拼接过程中 我们不能确定具体有几个参数
        // args.toArray() 将集合中的所有数据 都转换为数组
        // 可变参数
        Integer integer = template.queryForObject(sql,
                Integer.class, args.toArray());
        return integer;
    }


    @Override
    public List<Route> findByPage(int cid, int start, int pageSize, String rname) {
        //           SELECT * FROM tab_route WHERE cid = 5           LIMIT 0,5
        String sql = "SELECT * FROM tab_route WHERE 1 = 1 ";

        //可以使用集合来存储参数  args集合 用来存放参数
        ArrayList<Object> args = new ArrayList<>();
        if (cid > 0) {
            sql += " and cid = ? ";
            args.add(cid);
        }


        if (rname != null && rname.length() > 0 && !"null".equals(rname)) {
            // 只有三个条件都满足的时候 我们才认为rname有值 再进行sql的拼接
            sql += " and rname like ? ";
            args.add("%" + rname + "%");
        }

        sql += " LIMIT ?,?";
        args.add(start);
        args.add(pageSize);

        List<Route> list = template.query(sql,
                new BeanPropertyRowMapper<>(Route.class),
                args.toArray());

        return list;
    }

    /**
     * 是收藏排行需要使用的 方法
     *
     * @param start
     * @param pageSize
     * @param map
     * @return
     */
    @Override
    public List<Route> findByPage(int start, int pageSize, Map<String, String[]> map) {
//
//        String sql = "SELECT * FROM tab_route ORDER BY COUNT DESC,price limit ?,?";
        ArrayList list = new ArrayList();
        String sql = "select * from tab_route where 1 = 1 ";
        if(map.get("rname")!=null && map.get("rname").length > 0){
            sql +=" and rname like ? ";
            list.add("%"+map.get("rname")[0]+"%");
        }

        if(map.get("lowPrice")!=null && map.get("lowPrice").length > 0){
            sql +=" and price >= ? ";
            list.add(map.get("lowPrice")[0]);
        }

        if(map.get("highPrice")!=null && map.get("highPrice").length > 0){
            sql +=" and price <= ? ";
            list.add(map.get("highPrice")[0]);
        }
        sql += "ORDER BY COUNT DESC,price limit ?,?";
        list.add(start);
        list.add(pageSize);


        return  template.query(sql, new BeanPropertyRowMapper<>(Route.class), list.toArray());
    }

    @Override
    public Route findOne(int rid) {
        String sql = "select * from tab_route where rid =?";
        return template.queryForObject(sql, new BeanPropertyRowMapper<>(Route.class), rid);
    }

    /**
     *  根据cid 查找当前分类下 收藏次数较多的线路
     * @param cid
     * @return
     */
    @Override
    public List<Route> findByPage(int cid) {
        String sql ="SELECT * FROM tab_route WHERE cid = ? GROUP BY COUNT DESC,price ASC limit 5";
        return template.query(sql,new BeanPropertyRowMapper<>(Route.class),cid);
    }
}
