package com.prateek.addausers.addaUsers.addaController;

import com.prateek.addausers.addaUsers.addaElasticSearch.elasticData.UsersElasticModel;
import com.prateek.addausers.addaUsers.addaModel.UserRequestModel;
import com.prateek.addausers.addaUsers.addaModel.UserResponseModel;
import com.prateek.addausers.addaUsers.addaRedis.UserRedisModel;
import com.prateek.addausers.addaUsers.addaService.UsersService;
import com.prateek.addausers.addaUsers.addaShared.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
//@EnableCaching
public class AddaController {

    @Autowired
    UsersService usersService;

    @Autowired
    UserDto userDto;

    @Autowired
    ModelMapper modelMapper;


    @GetMapping("/check")
    public String check(){
        return "working...";
    }

    @PostMapping(value = "/create",
            consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    }
    )
    public ResponseEntity<String> create(@RequestBody UserRequestModel userRequestModel){
        UserDto userDto=modelMapper.map(userRequestModel,UserDto.class);
        userDto=usersService.createUser(userDto);
        if(userDto==null)
            throw new NullPointerException("Please check arg provided or maybe its service layer fault");
        return ResponseEntity.status(HttpStatus.OK).body(userDto.getUserId());
    }

    @GetMapping(value = "/all")
    public List<UserResponseModel> getAllUsers(){
        List<UserResponseModel> result=usersService.getAllUsers();
        return  result;
    }

    @GetMapping("/{id}")
    public UserResponseModel getUserByID(@PathVariable("id") String id){
        UserResponseModel userResponseModel=usersService.getUserByID(id);
        return  userResponseModel;
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value ="USER")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") String id){
        usersService.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Users Successfully deleted");
    }

    @PutMapping(value = "/{id}",consumes = {MediaType.APPLICATION_JSON_VALUE},produces={MediaType.APPLICATION_JSON_VALUE})
    @Transactional
    @CachePut(value = "USER")
//    @CacheEvict(value = "USER")
    public UserResponseModel updateUserByUserId(@PathVariable("id")String id,@RequestBody UserRequestModel userRequestModel){
        UserResponseModel userResponseModel=usersService.updateUserByUserId(id,userRequestModel);
        return userResponseModel;
    }

   @GetMapping("/elastic/{userId}")
    public Optional<UsersElasticModel> getUserByIdFromElasticSearch(@PathVariable("userId") String id){
        Optional<UsersElasticModel> usersElasticModel=usersService.getUserByIdFromElasticSearch(id);
        return  usersElasticModel;
   }

   @GetMapping("/redis/{userId}")
   @Cacheable(value ="USER" )
    public UserRedisModel getUserByIdFromRedis(@PathVariable("userId") String id){
        UserRedisModel userRedisModel=usersService.getUserByIdFromRedis(id);
        return  userRedisModel;
   }


}
