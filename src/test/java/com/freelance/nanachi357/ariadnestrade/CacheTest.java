package com.freelance.nanachi357.ariadnestrade;

import com.freelance.nanachi357.ariadnestrade.config.RedisConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

@SpringBootTest(classes = {CacheTest.TestConfig.class, RedisConfig.class})
public class CacheTest {

    private static final Logger logger = LoggerFactory.getLogger(CacheTest.class);

    @Autowired
    private SimpleCacheService simpleCacheService;

    @Autowired
    private ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    @BeforeEach
    public void clearRedis() {
        // Clear all keys in Redis before each test
        reactiveRedisTemplate.getConnectionFactory()
                .getReactiveConnection()
                .serverCommands()
                .flushAll()
                .doOnSuccess(unused -> System.out.println("Redis cache cleared."))
                .block();
    }

    @Test
    public void testReactiveCacheFunctionality() {
        // First call: cache miss, the data should be stored in the cache
        logger.info("First call - expecting cache miss");
        StepVerifier.create(simpleCacheService.getData("testKey"))
                .expectNext("CachedData")
                .verifyComplete();

        // Second call: cache hit, the value should be retrieved from the cache
        logger.info("Second call - expecting cache hit");
        StepVerifier.create(simpleCacheService.getData("testKey"))
                .expectNext("CachedData")
                .verifyComplete();
    }

    @Configuration
    static class TestConfig {
        @Bean
        public ReactiveRedisTemplate<String, String> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
            // Configure a ReactiveRedisTemplate with String serializers for keys and values
            RedisSerializationContext<String, String> context = RedisSerializationContext
                    .<String, String>newSerializationContext(new StringRedisSerializer())
                    .value(new StringRedisSerializer())
                    .build();
            return new ReactiveRedisTemplate<>(factory, context);
        }

        @Bean
        public SimpleCacheService simpleCacheService(@Qualifier("customReactiveRedisTemplate") ReactiveRedisTemplate<String, String> redisTemplate) {
            // Define the SimpleCacheService bean for dependency injection
            return new SimpleCacheService(redisTemplate);
        }
    }

    @Service
    public static class SimpleCacheService {

        private final ReactiveRedisTemplate<String, String> redisTemplate;

        // Constructor-based injection of the ReactiveRedisTemplate
        public SimpleCacheService(@Qualifier("customReactiveRedisTemplate") ReactiveRedisTemplate<String, String> redisTemplate) {
            this.redisTemplate = redisTemplate;
        }

        public Mono<String> getData(String key) {
            // Attempt to retrieve data from the cache
            return redisTemplate.opsForValue()
                    .get(key)
                    .switchIfEmpty(Mono.defer(() -> {
                        // Cache miss: log and store a default value in the cache
                        logger.info("Cache miss for key: " + key);
                        String value = "CachedData"; // Default value to cache
                        return redisTemplate.opsForValue()
                                .set(key, value, Duration.ofMinutes(5)) // Store in cache with a TTL of 5 minutes
                                .thenReturn(value);
                    }));
        }
    }
    @Test
    public void checkRedisKeys() {
        reactiveRedisTemplate.keys("*").collectList().subscribe(keys -> {
            logger.info("Keys in Redis: " + keys);
        });
    }
}
