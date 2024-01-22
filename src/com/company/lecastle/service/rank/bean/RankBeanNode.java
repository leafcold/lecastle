package com.company.lecastle.service.rank.bean;

public class RankBeanNode {

    private RankBean rankBean;
    //前驱节点
    private RankBean  rankBeanPrev;
    //后继节点
    private RankBean  rankBeanNext;

    public RankBean getRankBean() {
        return rankBean;
    }

    public void setRankBean(RankBean rankBean) {
        this.rankBean = rankBean;
    }

    public RankBean getRankBeanPrev() {
        return rankBeanPrev;
    }

    public void setRankBeanPrev(RankBean rankBeanPrev) {
        this.rankBeanPrev = rankBeanPrev;
    }

    public RankBean getRankBeanNext() {
        return rankBeanNext;
    }

    public void setRankBeanNext(RankBean rankBeanNext) {
        this.rankBeanNext = rankBeanNext;
    }
}
