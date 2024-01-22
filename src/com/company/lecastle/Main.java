package com.company.lecastle;

import com.company.lecastle.control.rank.GameRankControl;
import com.company.lecastle.db.redis.RedisDbManager;
import com.company.lecastle.player.Player;
import com.company.lecastle.service.rank.bean.RankBean;
import com.company.lecastle.service.rank.schedule.RankScheduleThread;

import java.util.Timer;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        //测试代码
        for(int i=0;i<110000;i++){
            Player player = new Player();
            player.setName(String.valueOf(i));
            player.setLevel(i);
            player.setUid(i);
            GameRankControl.getInstance().putPlayerScore(player,(int)(Math.random()*10000));
        }
        RedisDbManager redisDbManager = RedisDbManager.getInstance();
        redisDbManager.hGet().size();
        Timer timer = new Timer();
        timer.schedule(new RankScheduleThread(),2000,1000);
        Thread.sleep(20*1000);
        System.out.println("uid=10:rank" +GameRankControl.getInstance().rankFindMyselfRank(10).getRank());
        for(RankBean rankBean:GameRankControl.getInstance().rankFindNextRank(10,10)){
            System.out.println("uid:"+ rankBean.getPlayer().getUid());
        }
    }
}
