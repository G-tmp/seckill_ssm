package org.xd.dao.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xd.dao.SeckillDao;
import org.xd.entity.Seckill;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})
public class RedisDaoTest {

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private SeckillDao seckillDao;

    private long id = 1000;


    @Test
    public void getSeckill() {
        Seckill seckill = redisDao.getSeckill(id);

        // redis do not exist data and put
        if (seckill == null){
            seckill = seckillDao.queryById(id);
            if (seckill != null){
                String result = redisDao.putSeckill(seckill);
                System.out.println("result : "+result);     // ok

                seckill = redisDao.getSeckill(id);
                System.out.println("seckill : "+seckill);
            }
        }else {
            System.out.println(seckill);
        }


    }

}