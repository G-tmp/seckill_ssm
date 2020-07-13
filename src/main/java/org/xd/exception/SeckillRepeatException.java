package org.xd.exception;


/**
 *  重复秒杀异常
 */
public class SeckillRepeatException extends SeckillException{


    public SeckillRepeatException(String message) {
        super(message);
    }

    public SeckillRepeatException(String message, Throwable cause) {
        super(message, cause);
    }
}
