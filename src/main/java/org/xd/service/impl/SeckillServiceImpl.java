package org.xd.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.xd.dao.SeckillDao;
import org.xd.dao.SuccessKilledDao;
import org.xd.dto.Exposer;
import org.xd.dto.SeckillExecution;
import org.xd.enmus.SeckillStateEnmu;
import org.xd.entity.Seckill;
import org.xd.entity.SuccessKilled;
import org.xd.exception.SeckillCloseException;
import org.xd.exception.SeckillException;
import org.xd.exception.SeckillRepeatException;
import org.xd.service.SeckillService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

// @Component @Service @dao @Controller
@Service
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private SeckillDao seckillDao;

    @Resource
    private SuccessKilledDao successKilledDao;

    // 加盐用于md5加密
    private final String salt = "JF5h%^wa89s3e$%^&(^DXH6_8%67%^*&23489/b56e$R";


    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0,10);
    }

    @Override
    public Seckill getSeckillById(Long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exposeSeckillUrl(Long seckillId) {
        Seckill seckill = seckillDao.queryById(seckillId);

        // 无此商品
        if (seckill == null){
            return new Exposer(false,seckillId);
        }

        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();

        // 秒杀开始或结束
        if (nowTime.getTime()<startTime.getTime()  ||  nowTime.getTime()>endTime.getTime() ){
            return new Exposer(false,seckillId, nowTime.getTime(),startTime.getTime(),endTime.getTime());
        }

        String md5 = generateMD5(seckillId);
        return new Exposer(true,seckillId,md5);
    }

    @Override
    @Transactional
    public SeckillExecution executeSeckill(Long seckillId, Long phone, String md5) throws SeckillException, SeckillRepeatException, SeckillCloseException {
        if (md5 == null || !md5.equals(generateMD5(seckillId))){
            throw new SeckillException("seckill data rewrite");
        }

        // 若发生异常 事物回滚
        try {
            Date now = new Date();
            int updateCount = seckillDao.reduceNumber(seckillId,now);

            // 无库存 或 时间结束
            if (updateCount<=0){
                throw new SeckillCloseException("seckill is closed");
            }else {
                int insertCount = successKilledDao.insertSuccessKilled(seckillId,phone);

                // 重复插入
                if (insertCount <= 0) {
                    throw new SeckillRepeatException("seckill repeated");
                }else {
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, phone);
                    return new SeckillExecution(seckillId, SeckillStateEnmu.SUCCESS,successKilled);
                }
            }
        }catch (SeckillCloseException e){
            throw e;
        }catch (SeckillRepeatException e2){
            throw e2;
        }catch (Exception e3) {
            logger.error(e3.getMessage(),e3);
            throw e3;
        }

    }


    /**
     *  generate md5 via seckill id
     *
     * @param seckillId
     * @return
     */
    private String generateMD5(Long seckillId){
        String base = seckillId+"/"+salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
}
