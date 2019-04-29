package com.lmt.cache;

import com.lmt.util.JedisUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Set;

/**
 * Created by 张洲徽 on 2019/4/29.
 */
@Component
public class RedisCache<K,V> implements Cache<K,V> {

    private final String CAACHE_PREFIX="lmt-cache:";

    @Resource
    private JedisUtil jedisUtil;

    /*
    如果是字符串，则加了前缀后再转成二进制
    否则，返回二进制数据
     */
    private byte[] getKey(K k){
        if(k instanceof String){
            return (CAACHE_PREFIX+k).getBytes();
        }
        return SerializationUtils.serialize(k);
    }

    public V get(K k) throws CacheException {
        System.out.println("从redis中获取权限数据");
        //用二进制的key，从redis中获取二进制的值
        byte[] value=jedisUtil.get(getKey(k));
        if(value!=null){
            return (V)SerializationUtils.deserialize(value);
        }
        return null;
    }

    public V put(K k, V v) throws CacheException {
        byte[] key=getKey(k);
        byte[] value=SerializationUtils.serialize(v);
        jedisUtil.set(key,value);
        //10min
        jedisUtil.expire(key,600);
        return v;
    }

    public V remove(K k) throws CacheException {
        byte[] key=getKey(k);
        byte[] value=jedisUtil.get(key);
        jedisUtil.del(key);
        if(value != null){
            return (V)SerializationUtils.deserialize(value);
        }
        return null;
    }

    public void clear() throws CacheException {
        //不重写
    }

    public int size() {
        return 0;
    }

    public Set<K> keys() {
        return null;
    }

    public Collection<V> values() {
        return null;
    }
}
