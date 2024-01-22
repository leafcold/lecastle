package com.company.lecastle.service.rank;

import com.company.lecastle.player.Player;
import com.company.lecastle.service.rank.bean.RankBean;
import com.company.lecastle.service.rank.bean.RankBeanNode;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * 排行榜获取机制
 */
public interface IGameRankService {

    /**
     * 存放分数的时候注意用小数点后的数据表示时间 但是逻辑是时间越大那么越往后不能简单相加 用1减0.+时间戳 这样redis 就可以对分数直接排序了
     * @param player 玩家数据
     * @param score  玩家分数
     */
    public void putPlayerScore(Player player, int score);

    /**
     *
     * @return 获取到玩家数据的全部集合节点
     */
    public Map<Long, RankBeanNode> getRankBeanNodeHashMap();
    //定时器存放玩家全部的数据节点
    /**
     *
     * @param rankBeanNodeHashMap 定时器存放玩家数据
     */
    public void setRankBeanNodeHashMap(Map<Long, RankBeanNode> rankBeanNodeHashMap);

    /**
     *
     * @param scorePlayerIdMap 玩家的排名数据集合 已经排好序
     * @param playerIdRankBeanMap   玩家本身的数据集合
     * @return 构建排行榜数据
     */
    public Map<Long, RankBeanNode> rebuildDoubleLinkList(Map<Double,Long> scorePlayerIdMap, ConcurrentMap<Long, RankBean> playerIdRankBeanMap);

    /**
     * 查询某个前面的size个排名
     * @param size 查询前多少
     * @param uid 查询哪个玩家
     * @return 查询某个前面的size个排名
     */
    public List<RankBean> rankFindBeforeRank(int size,long uid);
    /**
     * 查询某个后面的size个排名
     * @param size 查询前多少
     * @param uid 查询哪个玩家
     * @return 查询某个后面的size个排名
     */
    public List<RankBean> rankFindNextRank(int size,long uid);

    /**
     * 查询自己
     * @param uid
     * @return 查询自己
     */
    public RankBean rankFindMyselfRank(long uid);
}
