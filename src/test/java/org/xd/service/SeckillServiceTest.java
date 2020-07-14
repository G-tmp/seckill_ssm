package org.xd.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xd.dto.Exposer;
import org.xd.dto.SeckillExecution;
import org.xd.entity.Seckill;
import org.xd.exception.SeckillCloseException;
import org.xd.exception.SeckillRepeatException;

import javax.annotation.Resource;
import javax.validation.constraints.Max;

import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})
public class SeckillServiceTest {

    @Resource
    private SeckillService seckillService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Test
    public void getSeckillList() {
        List<Seckill> list = seckillService.getSeckillList();

        logger.info("list={}",list);
    }

    @Test
    public void getSeckillById() {
        Long id = 1000L;
        Seckill s = seckillService.getSeckillById(id);

        logger.info("seckill={}",s);
        //seckill=Seckill{
        // seckillId=1000,
        // name='999元秒杀iphoneXI',
        // number=999,
        // startTime=Tue Jul 07 00:00:00 AWST 2020,
        // endTime=Thu Jul 08 00:00:00 AWST 2021,
        // createTime=Tue Jul 07 15:21:02 AWST 2020}
    }

    @Test
    public void exposeSeckillUrl() {

        Long id = 1000L;
        Exposer exposer = seckillService.exposeSeckillUrl(id);

        logger.info("exposer={}",exposer);
        //Exposer{
        // exposed=true,
        // md5='1102b2a59ca93a68608028a86cf37a25',
        // seckillId=1000,
        // now=null,
        // start=null,
        // end=null}
    }

    @Test
    public void executeSeckill() {
        try {
            SeckillExecution execution = seckillService.executeSeckill(1000L, 10089L, "1102b2a59ca93a68608028a86cf37a25");
            logger.info("execution={}",execution);
        }catch (SeckillRepeatException e){
            logger.error(e.getMessage());
        }catch (SeckillCloseException e){
            logger.error(e.getMessage());
        }
        //SeckillExecution{
        // seckillId=1000,
        // state=1,
        // stateInfo='秒杀成功',
        // successKilled=SuccessKilled{seckillId=1000,
        // userPhone=10089, state=0,
        // createTime=Tue Jul 14 14:55:13 AWST 2020,
        // seckill=Seckill{seckillId=1000,
        // name='999元秒杀iphoneXI',
        // number=998,
        // startTime=Tue Jul 07 00:00:00 AWST 2020,
        // endTime=Thu Jul 08 00:00:00 AWST 2021,
        // createTime=Tue Jul 07 15:21:02 AWST 2020}}}
    }


    @Test
    public void logic(){
        Long id = 1001L;
        Long phone = 48794651L;
        Exposer exposer = seckillService.exposeSeckillUrl(id);
        logger.info("exposer={}",exposer);


        // 开启秒杀
        if (exposer.isExposed()){
            try {
                SeckillExecution execution = seckillService.executeSeckill(id, phone, exposer.getMd5());
                logger.info("execution = {}",execution);
            }catch (SeckillRepeatException e){
                logger.error(e.getMessage());
            }catch (SeckillCloseException e){
                logger.error(e.getMessage());
            }
        }else {
            logger.warn("execution = {}","你秒杀你妈呢");
        }

    }
}
//Exposer{
// exposed=true,
// md5='1102b2a59ca93a68608028a86cf37a25',
// seckillId=1000,
// now=null,
// start=null,
// end=null
// }

// Exposer{
// exposed=false,
// md5='null',
// seckillId=1001,
// now=1594711413785,
// start=1594051200000,
// end=1594310400000
// }