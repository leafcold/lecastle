package com.company.lecastle.service.rank.bean;

import com.company.lecastle.player.Player;

import java.math.BigDecimal;

public class RankBean {
    private Player player;

    private Double score;

    private int rank;

    public long time;
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
