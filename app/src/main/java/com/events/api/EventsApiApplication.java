package com.events.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;

@SpringBootApplication
@ImportAutoConfiguration(classes = {
		CacheAutoConfiguration.class,
		RedisAutoConfiguration.class,
		MongoRepositoriesAutoConfiguration.class
})
public class EventsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventsApiApplication.class, args);
	}

}
