package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.service.FavoriteService;

public class FavoriteServiceImpl implements FavoriteService {

    private FavoriteDao favoriteDao = new FavoriteDaoImpl();

    /**
     * 根据ridStr和uid判断线路是否被收藏
     * @param ridStr
     * @param uid
     * @return
     */
    @Override
    public boolean isFavorite(String ridStr, int uid) {
        int rid = 0;
        if(ridStr != null && !"null".equals(ridStr)){
            rid = Integer.parseInt(ridStr);
        }
        //通过调用favoriteDao方法 可以获取到favorite对象
        Favorite favorite = favoriteDao.findByRidAndUid(rid,uid);

        //favorite != null 如果成立，代表该用户已经收藏 返回true
        return favorite != null;
    }

    /**
     *向表中插入数据
     * @param ridStr
     * @param uid
     */
    @Override
    public void add(String ridStr, int uid) {
        int rid = 0;
        if(ridStr != null && !"null".equals(ridStr)){
            rid = Integer.parseInt(ridStr);
        }
        favoriteDao.add(rid,uid);
    }
}
