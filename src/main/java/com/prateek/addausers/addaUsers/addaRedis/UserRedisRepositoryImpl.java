package com.prateek.addausers.addaUsers.addaRedis;

import com.prateek.addausers.addaUsers.addaRedis.UserRedisModel;
import com.prateek.addausers.addaUsers.addaRedis.UserRedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class UserRedisRepositoryImpl implements UserRedisRepository {



    private RedisTemplate<String, UserRedisModel> redisTemplate;

    private HashOperations hashOperations;

    public UserRedisRepositoryImpl(){}

    @Autowired
    public UserRedisRepositoryImpl(RedisTemplate<String, UserRedisModel> redisTemplate) {
        this.redisTemplate = redisTemplate;
        hashOperations=redisTemplate.opsForHash();
    }

    @Override
    public void save(UserRedisModel user) {
        hashOperations.put("USER", user.getUserId(), user);
    }

    @Override
    public UserRedisModel findById(String id) {
        return (UserRedisModel) hashOperations.get("USER", id);

    }

    @Override
    public void update(UserRedisModel user) {
        save(user);
    }

    @Override
    public void delete(String id) {
        hashOperations.delete("USER", id);
    }
}
