package com.prateek.addausers.addaUsers;

import com.prateek.addausers.addaUsers.addaRedis.UserRedisModel;
import com.prateek.addausers.addaUsers.addaRedis.UserRedisRepository;
import com.prateek.addausers.addaUsers.addaRedis.UserRedisRepositoryImpl;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableCaching
public class AddaUsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(AddaUsersApplication.class, args);
	}

	@Bean
	public ModelMapper getModelMapper(){
		ModelMapper modelMapper=new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper;
	}

	@Bean
	public BCryptPasswordEncoder getBCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}
	
}
