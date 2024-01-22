package com.company.lecastle.service.rank;

import com.company.lecastle.db.redis.RedisDbManager;
import com.company.lecastle.player.Player;
import com.company.lecastle.service.rank.bean.RankBean;
import com.company.lecastle.service.rank.bean.RankBeanNode;
import com.company.lecastle.service.rank.conf.ActiveConfig;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentMap;


// 排名的service
public class GameRankService implements IGameRankService {
    //创建一个单例模式
    private static final GameRankService gameRankService = new GameRankService();

    private GameRankService() {
    }

    public static IGameRankService getInstance() {
        return gameRankService;
    }

    //玩家数据缓存
    private Map<Long, RankBeanNode> rankBeanNodeHashMap = new HashMap<>();


    // 对玩家操作进行加锁  不然会有并发问题
    @Override
    public void putPlayerScore(Player player, int score) {
        long time = System.currentTimeMillis();
        //判断当前时间是否大于结束时间
        if (time > ActiveConfig.OVER_ACTIVE_TIME) {
            throw new RuntimeException("活动已经结束");
        }
        if (score > ActiveConfig.MAX_SCORE || score < ActiveConfig.MIN_SCORE) {
            throw new RuntimeException("玩家非法输入");
        }
        double parseDouble =  Double.parseDouble("0." + (ActiveConfig.OVER_ACTIVE_TIME-time)+player.getUid());
        Double scoreAndTime = (double) score+parseDouble;
        RankBean rankBean = new RankBean();
        rankBean.setPlayer(player);
        rankBean.setScore(scoreAndTime);
        rankBean.setTime(time);
        //存redis  不要考虑重复了 如果说考虑重复就做个判断 实在不行就重新打 看策划需求吧
        if(RedisDbManager.getInstance().zGet().get(scoreAndTime)!=null)
        {
            System.out.println(scoreAndTime +"uid:"+player.getUid()+"score:"+rankBean.getScore()+"time:"+rankBean.getTime());
            RankBean uidBak=RedisDbManager.getInstance().hGet().get(RedisDbManager.getInstance().zGet().get(scoreAndTime));
            System.out.println(scoreAndTime +"uid:"+uidBak.getPlayer().getUid()+"score:"+uidBak.getScore()+"time:"+uidBak.getTime());
        }
        RedisDbManager.getInstance().zSet(scoreAndTime, player.getUid());
        RedisDbManager.getInstance().hSet(player.getUid(), rankBean);
    }

    @Override
    public Map<Long, RankBeanNode> getRankBeanNodeHashMap() {
        return rankBeanNodeHashMap;
    }

    @Override
    public void setRankBeanNodeHashMap(Map<Long, RankBeanNode> rankBeanNodeHashMap) {
        this.rankBeanNodeHashMap = rankBeanNodeHashMap;
    }

    @Override
    public Map<Long, RankBeanNode> rebuildDoubleLinkList(Map<Double, Long> scorePlayerIdMap, ConcurrentMap<Long, RankBean> playerIdRankBeanMap) {
        RankBean rankBeanPrev = null;
        //创建数据结构
        Map<Long, RankBeanNode> rankBeanNodeHashMap = new HashMap<>();
        //开始顺序遍历
        Set<Double> scores = scorePlayerIdMap.keySet();
        int rank = 0;
        for (Double doubleData : scores) {
            long uid = scorePlayerIdMap.get(doubleData);
            //获取到当前节点

            RankBean rankBeanValue = playerIdRankBeanMap.get(uid);
            if (rankBeanValue == null) {
                //打印一个错误日志 打印当前的uid 说明有问题了两个数据不一致了 应该不可能存在
                continue;
            }
            rankBeanValue.setRank(++rank);

            //这块位置构建双向循环链表
            RankBeanNode rankBeanNodePrev = null;
            if (rankBeanPrev != null) {
                long uidPrev = rankBeanPrev.getPlayer().getUid();
                rankBeanNodePrev = rankBeanNodeHashMap.get(uidPrev);
            }
            RankBeanNode rankBeanNode = new RankBeanNode();
            rankBeanNode.setRankBean(rankBeanValue);
            rankBeanNode.setRankBeanPrev(rankBeanPrev);
            if (rankBeanNodePrev != null) {
                rankBeanNodePrev.setRankBeanNext(rankBeanNode.getRankBean());
            }
            rankBeanPrev = rankBeanValue;
            rankBeanNodeHashMap.put(rankBeanValue.getPlayer().getUid(),rankBeanNode);
        }
        return rankBeanNodeHashMap;
    }

    @Override
    public List<RankBean> rankFindBeforeRank(int size, long uid) {
        Map<Long, RankBeanNode> rankBeanNodeMap = getLongRankBeanNodeMap(uid);
        List<RankBean> rankBeans =new ArrayList<>();
        RankBeanNode rankBeanNode =  rankBeanNodeMap.get(uid);
        int i = 0;
        while (null != rankBeanNode.getRankBeanPrev()) {
            if (i++==size) {
                break;
            }
            rankBeans.add(rankBeanNode.getRankBeanPrev());
            RankBean rankBean = rankBeanNode.getRankBeanPrev();
            rankBeanNode=rankBeanNodeMap.get(rankBean.getPlayer().getUid());
        }
        return rankBeans;
    }

    @Override
    public List<RankBean> rankFindNextRank(int size, long uid) {
        Map<Long, RankBeanNode> rankBeanNodeMap = getLongRankBeanNodeMap(uid);
        List<RankBean> rankBeans =new ArrayList<>();
        RankBeanNode rankBeanNode =  rankBeanNodeMap.get(uid);
        int i = 0;
        while (null != rankBeanNode.getRankBeanNext()) {
            if (i++==size) {
                break;
            }
            rankBeans.add(rankBeanNode.getRankBeanNext());
            RankBean rankBean = rankBeanNode.getRankBeanNext();
            rankBeanNode=rankBeanNodeMap.get(rankBean.getPlayer().getUid());
        }
        return rankBeans;
    }


    @Override
    public RankBean rankFindMyselfRank(long uid) {
        Map<Long, RankBeanNode> rankBeanNodeMap = getLongRankBeanNodeMap(uid);
        return rankBeanNodeMap.get(uid).getRankBean();
    }

    private Map<Long, RankBeanNode> getLongRankBeanNodeMap(long uid) {
        Map<Long, RankBeanNode> rankBeanNodeMap = getRankBeanNodeHashMap();
        if (rankBeanNodeMap == null) {
            throw new RuntimeException("活动排名目前还没开始");
        }
        if (rankBeanNodeMap.get(uid) == null) {
            throw new RuntimeException("玩家未参加排名");
        }
        return rankBeanNodeMap;
    }


}
