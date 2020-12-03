package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface RouteDao {

    /**
     * 根据cid查询总记录数
     */
    public int findTotalCount(int cid,String rname);

    /**
     * 根据cid，start,pageSize查询当前页的数据集合
     */
    public List<Route> findByPage(int cid , int start , int pageSize,String rname);


    List<Route> findByPage(int start, int pageSize, Map<String, String[]> map);

    /**
     * 根据id查询
     * @param rid
     * @return
     */
    public Route findOne(int rid);

    List<Route> findByPage(int cid);
}
