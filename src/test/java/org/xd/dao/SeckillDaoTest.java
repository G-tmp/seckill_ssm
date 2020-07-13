package org.xd.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xd.entity.Seckill;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {

    @Resource
    private SeckillDao seckillDao;


    @Test
    public void reduceNumber() {
        int i = seckillDao.reduceNumber((long) 1000,new Date());
        System.out.println(i);
    }

    @Test
    public void queryById() {
        long id = 1000;
        Seckill s = seckillDao.queryById(id);

        System.out.println(s);
    }

    @Test
    public void queryAll() {
        List<Seckill> seckills = seckillDao.queryAll(0, 20);
        for (Seckill e:seckills)
            System.out.println(e);
    }
}