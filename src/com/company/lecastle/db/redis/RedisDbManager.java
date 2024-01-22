package com.company.lecastle.db.redis;

import com.company.lecastle.service.rank.bean.RankBean;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

//这块位置是redis 缓存
public class RedisDbManager {

    //玩家的实际缓存 测试
    //使用currentHashMap 不同玩家高并发 出现hash 冲突  实际存储数据
    private final ConcurrentMap<Long,RankBean> playerIdRankBeanMap =new ConcurrentHashMap<>();

    //玩家的实际缓存 测试
    //ConcurrentSkipListMap 不同玩家高并发 出现hash 冲突  实际存储数据
    private final ConcurrentMap<Double,Long> scorePlayerIdMap =new ConcurrentSkipListMap<>();
    //单例模式
    private static final RedisDbManager redisDbManager = new RedisDbManager();

    private RedisDbManager() {
    }

    public static RedisDbManager getInstance(){
        return redisDbManager;
    }

    //数据入redis zset 目前用ConcurrentSkipListMap 来存储
    public void zSet(Double score , long uid) {
        scorePlayerIdMap.put(score,uid);
    }

    //数据直接进redis  目前用Hash map 模拟数据
    public void hSet(long uid, RankBean rankBean) {
        playerIdRankBeanMap.put(uid,rankBean);
    }
    //获取到全部排行榜玩家信息
    public ConcurrentMap<Long,RankBean> hGet()
    {
        return playerIdRankBeanMap;
    }
    //获取到玩家信息
    public ConcurrentMap<Double,Long> zGet() {
        return scorePlayerIdMap;
    }


}
