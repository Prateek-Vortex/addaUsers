package com.prateek.addausers.addaUsers.addaRedis;

import com.prateek.addausers.addaUsers.addaRedis.UserRedisModel;

public interface UserRedisRepository {
    void save(UserRedisModel user);
    UserRedisModel findById(String id);
    void update(UserRedisModel user);
    void delete(String id);
}
