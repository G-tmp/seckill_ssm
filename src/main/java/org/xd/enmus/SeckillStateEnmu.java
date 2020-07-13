package org.xd.enmus;


/**
 *  秒杀状态枚举
 */
public enum  SeckillStateEnmu {
    SUCCESS(1,"秒杀成功"),
    END(0,"秒杀结束"),
    REPEAT(-1,"重复秒杀"),
    ERROR(-2,"系统异常"),
    DATA_REWRITE(-3,"数据篡改");


    private int state;
    private String stateInfo;


    SeckillStateEnmu(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }


    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public static SeckillStateEnmu stateOf(int state){
        for (SeckillStateEnmu s:values()){
            if (state == s.getState()){
                return s;
            }
        }

        return null;
    }
}
