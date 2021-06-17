package com.prateek.addausers.addaUsers.addaData;


import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<UsersEntity, Long> {
    UsersEntity findByEmail(String email);
    UsersEntity findByUserId(String userId);
    long deleteByUserId(String userId);
}
