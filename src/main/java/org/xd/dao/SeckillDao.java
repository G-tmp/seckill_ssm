package org.xd.dao;

import org.apache.ibatis.annotations.Param;
import org.xd.entity.Seckill;

import java.util.Date;
import java.util.List;


/**
 *
 */
public interface SeckillDao {

    /**
     * 减库存
     * @param seckillId
     * @param killTime
     * @return 如果影响行数>1，表示更新库存的记录行数
     */
    Integer reduceNumber(@Param("seckillId") Long seckillId, @Param("killTime") Date killTime);


    /**
     * 根据id查询秒杀的商品信息
     * @param seckillId
     * @return
     */
    Seckill queryById(Long seckillId);


    /**
     * 根据偏移量查询秒杀商品列表
     * @param offset
     * @param limit
     * @return
     */
    List<Seckill> queryAll(@Param("offset") Integer offset, @Param("limit") Integer limit);

}
