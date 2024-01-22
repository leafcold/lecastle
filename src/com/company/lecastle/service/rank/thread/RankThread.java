package com.company.lecastle.service.rank.thread;


import com.company.lecastle.db.redis.RedisDbManager;
import com.company.lecastle.service.rank.GameRankService;
import com.company.lecastle.service.rank.bean.RankBean;
import com.company.lecastle.service.rank.bean.RankBeanNode;
import com.company.lecastle.service.rank.conf.ActiveConfig;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentMap;

/**
 * 定时器 触发 排名线程
 */
public class RankThread extends TimerTask {

    public static boolean flagRank =false;

    @Override
    public void run() {
        // 是否已经构建内存
        if(flagRank) {
            return;
        }
        long timeMillis=System.currentTimeMillis();
        if(timeMillis<ActiveConfig.OVER_ACTIVE_TIME){
            return;
        }
        Map<Long, RankBeanNode> rankBeanNodeHashMap = new HashMap<>();
        RedisDbManager redisDbManager = RedisDbManager.getInstance();
        //这个时候玩家的排名就已经排好了redis 自己排序的
        ConcurrentMap<Double,Long> scorePlayerIdMap =  redisDbManager.zGet();
        //这个时候玩家的信息数据
        ConcurrentMap<Long, RankBean> playerIdRankBeanMap =  redisDbManager.hGet();
        //重建双端链表
        Map<Long, RankBeanNode> rankBeanNodeMap = GameRankService.getInstance().rebuildDoubleLinkList(scorePlayerIdMap,playerIdRankBeanMap);
        GameRankService.getInstance().setRankBeanNodeHashMap(rankBeanNodeMap);

        flagRank = true;
    }
}
