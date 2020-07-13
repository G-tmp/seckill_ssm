package org.xd.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xd.entity.SuccessKilled;

import javax.annotation.Resource;

import static org.junit.Assert.*;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {

    @Resource
    private SuccessKilledDao successKilledDao;


    @Test
    public void insertSuccessKilled() {
        long id = 1000;
        long phone = 10086;
        Integer i = successKilledDao.insertSuccessKilled(id, phone);

        System.out.println(i);
    }

    @Test
    public void queryByIdWithSeckill() {
        long id = 1000;
        long phone = 10086;
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(id, phone);

        System.out.println(successKilled);
    }
}