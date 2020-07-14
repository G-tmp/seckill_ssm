package org.xd.dto;

import org.xd.enmus.SeckillStateEnmu;
import org.xd.entity.SuccessKilled;

/**
 * 秒杀执行后的结果
 */
public class SeckillExecution {
    // 商品id
    private Long seckillId;
    private Integer state;
    private String stateInfo;
    // 秒杀成功对象
    private SuccessKilled successKilled;




    public SeckillExecution(Long seckillId, SeckillStateEnmu seckillState) {
        this.seckillId = seckillId;
        this.state = seckillState.getState();
        this.stateInfo = seckillState.getStateInfo();
    }

    public SeckillExecution(Long seckillId, SeckillStateEnmu seckillState, SuccessKilled successKilled) {
        this.seckillId = seckillId;
        this.state = seckillState.getState();
        this.stateInfo = seckillState.getStateInfo();
        this.successKilled = successKilled;
    }

    @Override
    public String toString() {
        return "SeckillExecution{" +
                "seckillId=" + seckillId +
                ", state=" + state +
                ", stateInfo='" + stateInfo + '\'' +
                ", successKilled=" + successKilled +
                '}';
    }

    public Long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(Long seckillId) {
        this.seckillId = seckillId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public SuccessKilled getSuccessKilled() {
        return successKilled;
    }

    public void setSuccessKilled(SuccessKilled successKilled) {
        this.successKilled = successKilled;
    }
}
