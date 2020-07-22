package org.xd.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xd.entity.Seckill;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisDao {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JedisPool jedisPool;

    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);



    public RedisDao(String ip,int port) {
        jedisPool = new JedisPool(ip,port);
    }

    public Seckill getSeckill(Long seckillId){
        // get byte[] -> 反序列化 -> object
        try (Jedis jedis = jedisPool.getResource()){
            String key = "seckill:" + seckillId;
            byte[] bytes = jedis.get(key.getBytes());
            if (bytes != null){
                Seckill seckill = schema.newMessage();
                ProtobufIOUtil.mergeFrom(bytes,seckill,schema);

                return seckill;
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }

        return null;
    }

    // 维护一致性：超时
    public String putSeckill(Seckill seckill){
        // object -> 序列化 -> byte[]
        try (Jedis jedis = jedisPool.getResource()){
            String key = "seckill:" + seckill.getSeckillId();
            final byte[] bytes = ProtobufIOUtil.toByteArray(seckill, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
            int timeout = 60*60;    // one hour
            String result = jedis.setex(key.getBytes(),timeout,bytes);

            return result;
        }catch (Exception e){
            logger.error(e.getMessage());
        }

        return null;
    }

}
