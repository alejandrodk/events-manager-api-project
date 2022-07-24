package com.events.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@ImportAutoConfiguration(classes = {
		CacheAutoConfiguration.class,
		RedisAutoConfiguration.class
})
@EnableScheduling
public class EventsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventsApiApplication.class, args);
	}

}
