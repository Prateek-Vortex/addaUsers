package com.prateek.addausers.addaUsers.addaService;

import com.prateek.addausers.addaUsers.addaElasticSearch.elasticData.UsersElasticModel;
import com.prateek.addausers.addaUsers.addaModel.UserRequestModel;
import com.prateek.addausers.addaUsers.addaModel.UserResponseModel;
import com.prateek.addausers.addaUsers.addaRedis.UserRedisModel;
import com.prateek.addausers.addaUsers.addaShared.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UsersService extends UserDetailsService {

    UserDto createUser(UserDto userDto);

    UserDto getUserByEmail(String userName);

    List<UserResponseModel> getAllUsers();

    UserResponseModel getUserByID(String userId);

    void deleteUserById(String userId);

    UserResponseModel updateUserByUserId(String userId,UserRequestModel userRequestModel);

    Optional<UsersElasticModel> getUserByIdFromElasticSearch(String id);

    UserRedisModel getUserByIdFromRedis(String id);
}
