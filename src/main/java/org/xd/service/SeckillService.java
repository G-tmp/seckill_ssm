package org.xd.service;

import org.xd.dto.Exposer;
import org.xd.dto.SeckillExecution;
import org.xd.entity.Seckill;
import org.xd.exception.SeckillCloseException;
import org.xd.exception.SeckillException;
import org.xd.exception.SeckillRepeatException;

import java.util.List;


/**
 * 业务接口:站在"使用者"角度设计接口
 * 三个方面:方法定义粒度,参数,返回类型(return 类型/异常)
 */
public interface SeckillService {

    /**
     *  获取秒杀商品列表
     *
     * @return
     */
    List<Seckill> getSeckillList();


    /**
     *  单个秒杀详情
     *
     * @param seckillId
     * @return
     */
    Seckill getSeckillById(Long seckillId);


    /**
     * 开启秒杀输出秒杀接口地址
     * 否则输出系统时间和秒杀时间
     *
     * @param seckillId
     */
    Exposer exposeSeckillUrl (Long seckillId);


    /**
     *  执行秒杀
     *
     * @param seckillId
     * @param phone
     * @param md5
     * @throws SeckillException
     * @throws SeckillRepeatException
     * @throws SeckillCloseException
     */
    SeckillExecution executeSeckill(Long seckillId, Long phone, String md5)
            throws SeckillException, SeckillRepeatException, SeckillCloseException;
}
