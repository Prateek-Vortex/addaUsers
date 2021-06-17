package com.prateek.addausers.addaUsers.addaService;

import com.prateek.addausers.addaUsers.addaData.UsersEntity;
import com.prateek.addausers.addaUsers.addaData.UsersRepository;
import com.prateek.addausers.addaUsers.addaElasticSearch.elasticData.UsersElasticModel;
import com.prateek.addausers.addaUsers.addaElasticSearch.elasticData.UsersElasticRepository;
import com.prateek.addausers.addaUsers.addaModel.UserRequestModel;
import com.prateek.addausers.addaUsers.addaModel.UserResponseModel;
import com.prateek.addausers.addaUsers.addaRedis.UserRedisModel;
import com.prateek.addausers.addaUsers.addaRedis.UserRedisRepository;
import com.prateek.addausers.addaUsers.addaShared.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class UsersServiceImpl implements UsersService{

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UsersElasticRepository usersElasticRepository;

    @Autowired
    UserRedisRepository userRedisRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        userDto.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        UsersEntity usersEntity=modelMapper.map(userDto, UsersEntity.class);
        usersRepository.save(usersEntity);
        UsersElasticModel usersElasticModel=modelMapper.map(userDto,UsersElasticModel.class);
        usersElasticRepository.save(usersElasticModel);
        UserRedisModel userRedisModel=modelMapper.map(userDto,UserRedisModel.class);
        userRedisRepository.save(userRedisModel);
        return userDto;
    }

    @Override
    public UserDto getUserByEmail(String userName) {
        UsersEntity usersEntity=usersRepository.findByEmail(userName);
        if(usersEntity==null)
            throw new UsernameNotFoundException("Check your credentials");
        return modelMapper.map(usersEntity,UserDto.class);
    }

    @Override
    public List<UserResponseModel> getAllUsers() {
        List<UsersEntity> list= (List<UsersEntity>) usersRepository.findAll();
        if (list.isEmpty())
            throw new NullPointerException("No user found");
        List<UserResponseModel> res=new ArrayList<>();
        for(UsersEntity usersEntity:list){
            UserResponseModel userResponseModel=modelMapper.map(usersEntity,UserResponseModel.class);
            res.add(userResponseModel);
        }
        return res;
    }

    @Override
    public UserResponseModel getUserByID(String userId) {
        UsersEntity usersEntity=usersRepository.findByUserId(userId);
        if(usersEntity==null)
            throw new UsernameNotFoundException("Check your user id");
        return modelMapper.map(usersEntity,UserResponseModel.class);
    }

    @Override
    public void deleteUserById(String userId) {
        //long res=usersRepository.deleteByUserId(userId);
        UsersEntity usersEntity=usersRepository.findByUserId(userId);
        usersRepository.delete(usersEntity);
        userRedisRepository.delete(userId);
        usersElasticRepository.deleteById(userId);

    }

    @Override
    @CacheEvict(allEntries = true,value = "USER")
    public UserResponseModel updateUserByUserId(String userId, UserRequestModel userRequestModel) {
        UsersEntity usersEntity=usersRepository.findByUserId(userId);
        if(usersEntity==null)
            throw new UsernameNotFoundException("Check your user id");
        usersEntity.setFirstName(userRequestModel.getFirstName());
        usersEntity.setLastName(userRequestModel.getLastName());
        usersEntity.setEmail(userRequestModel.getEmail());
        usersEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userRequestModel.getPassword()));
        usersRepository.save(usersEntity);
        UsersElasticModel usersElasticModel=modelMapper.map(usersEntity,UsersElasticModel.class);
        usersElasticRepository.save(usersElasticModel);
        UserRedisModel userRedisModel=modelMapper.map(usersEntity,UserRedisModel.class);
        userRedisRepository.delete(userId);
        userRedisRepository.save(userRedisModel);
        return modelMapper.map(usersEntity,UserResponseModel.class);
    }

    @Override
    public Optional<UsersElasticModel> getUserByIdFromElasticSearch(String id) {
        Optional<UsersElasticModel> usersElasticModel=usersElasticRepository.findById(id);

        return usersElasticModel;
    }

    @Override
    public UserRedisModel getUserByIdFromRedis(String id) {
        UserRedisModel userRedisModel=userRedisRepository.findById(id);
        return userRedisModel;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UsersEntity usersEntity=usersRepository.findByEmail(s);
        if(usersEntity==null)
            throw new UsernameNotFoundException("Enter correct username");

        return new User(usersEntity.getEmail(),usersEntity.getEncryptedPassword(),true,true,true,true,new ArrayList<>());

    }
}
