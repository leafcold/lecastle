package com.company.lecastle.control.rank;

import com.company.lecastle.player.Player;
import com.company.lecastle.service.rank.GameRankService;
import com.company.lecastle.service.rank.IGameRankService;
import com.company.lecastle.service.rank.bean.RankBean;

import java.util.List;

public class GameRankControl {

    private static final GameRankControl gameRankControl = new GameRankControl();

    private GameRankControl() {
    }

    public static GameRankControl getInstance() {
        return gameRankControl;
    }


    public void putPlayerScore(Player player, int score){
        GameRankService.getInstance().putPlayerScore(player,score);
    }


    public List<RankBean> rankFindBeforeRank(int size, long uid){
       return  GameRankService.getInstance().rankFindBeforeRank(size,uid);
    }
    /**
     * 查询某个后面的size个排名
     * @param size 查询前多少
     * @param uid 查询哪个玩家
     * @return 查询某个后面的size个排名
     */
    public List<RankBean> rankFindNextRank(int size,long uid){
        return  GameRankService.getInstance().rankFindNextRank(size,uid);
    }

    /**
     * 查询自己
     * @param uid
     * @return 查询自己
     */
    public RankBean rankFindMyselfRank(long uid){
        return  GameRankService.getInstance().rankFindMyselfRank(uid);
    }
}
