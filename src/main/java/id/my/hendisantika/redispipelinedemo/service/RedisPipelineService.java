package id.my.hendisantika.redispipelinedemo.service;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-redis-pipeline-demo
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 10/08/25
 * Time: 05.52
 * To change this template use File | Settings | File Templates.
 */

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisPipelineService {

    private final RedisTemplate<String, Object> redisTemplate;

    public Map<String, Object> insertData10K() {
        long startTime = System.currentTimeMillis();

        // Execute pipeline operations
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (int i = 1; i <= 10000; i++) {
                String key = "user:" + i;

                // Store data as individual string values for simplicity
                connection.hSet(key.getBytes(), "id".getBytes(), String.valueOf(i).getBytes());
                connection.hSet(key.getBytes(), "name".getBytes(), ("User " + i).getBytes());
                connection.hSet(key.getBytes(), "email".getBytes(), ("user" + i + "@example.com").getBytes());
                connection.hSet(key.getBytes(), "age".getBytes(), String.valueOf(20 + (i % 50)).getBytes());

                // Set expiration to 1 hour
                connection.expire(key.getBytes(), 3600);
            }
            return null;
        });

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        log.info("Pipeline insert operation completed in {} ms", duration);

        Map<String, Object> result = new HashMap<>();
        result.put("operation", "INSERT_10K_PIPELINE");
        result.put("records", 10000);
        result.put("duration_ms", duration);
        result.put("status", "SUCCESS");

        return result;
    }

    public Map<String, Object> insertDataNormal() {
        long startTime = System.currentTimeMillis();

        // Execute normal operations (without pipeline)
        for (int i = 1; i <= 10000; i++) {
            String key = "normal_user:" + i;
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", i);
            userData.put("name", "Normal User " + i);
            userData.put("email", "normal_user" + i + "@example.com");
            userData.put("age", 20 + (i % 50));

            // Store as hash
            redisTemplate.opsForHash().putAll(key, userData);
            // Set expiration to 1 hour
            redisTemplate.expire(key, 1, TimeUnit.HOURS);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        log.info("Normal insert operation completed in {} ms", duration);

        Map<String, Object> result = new HashMap<>();
        result.put("operation", "INSERT_10K_NORMAL");
        result.put("records", 10000);
        result.put("duration_ms", duration);
        result.put("status", "SUCCESS");

        return result;
    }

    public Map<String, Object> readData10K() {
        long startTime = System.currentTimeMillis();

        // Execute pipeline read operations
        List<Object> pipelineResults = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (int i = 1; i <= 10000; i++) {
                String key = "user:" + i;
                connection.hGetAll(key.getBytes());
            }
            return null;
        });

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        log.info("Pipeline read operation completed in {} ms", duration);

        Map<String, Object> result = new HashMap<>();
        result.put("operation", "READ_10K_PIPELINE");
        result.put("records", 10000);
        result.put("duration_ms", duration);
        result.put("status", "SUCCESS");
        result.put("results_count", pipelineResults.size());

        return result;
    }

    public Map<String, Object> deleteData10K() {
        long startTime = System.currentTimeMillis();

        // Execute pipeline delete operations
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (int i = 1; i <= 10000; i++) {
                String key = "user:" + i;
                connection.del(key.getBytes());
            }
            return null;
        });

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        log.info("Pipeline delete operation completed in {} ms", duration);

        Map<String, Object> result = new HashMap<>();
        result.put("operation", "DELETE_10K_PIPELINE");
        result.put("records", 10000);
        result.put("duration_ms", duration);
        result.put("status", "SUCCESS");

        return result;
    }

    public Map<String, Object> getRedisInfo() {
        Map<String, Object> info = new HashMap<>();

        try {
            // Get database size
            Long dbSize = redisTemplate.getConnectionFactory().getConnection().dbSize();
            info.put("database_size", dbSize);

            // Check if sample keys exist
            Boolean userExists = redisTemplate.hasKey("user:1");
            Boolean normalUserExists = redisTemplate.hasKey("normal_user:1");

            info.put("pipeline_user_exists", userExists);
            info.put("normal_user_exists", normalUserExists);
            info.put("status", "SUCCESS");

        } catch (Exception e) {
            log.error("Error getting Redis info", e);
            info.put("status", "ERROR");
            info.put("error", e.getMessage());
        }

        return info;
    }

    public Map<String, Object> performanceComparison() {
        Map<String, Object> comparison = new HashMap<>();

        try {
            // Clear any existing data
            deleteData10K();
            redisTemplate.delete(redisTemplate.keys("normal_user:*"));

            // Test pipeline insert
            Map<String, Object> pipelineResult = insertData10K();
            long pipelineDuration = (Long) pipelineResult.get("duration_ms");

            // Test normal insert
            Map<String, Object> normalResult = insertDataNormal();
            long normalDuration = (Long) normalResult.get("duration_ms");

            comparison.put("pipeline_duration_ms", pipelineDuration);
            comparison.put("normal_duration_ms", normalDuration);
            comparison.put("performance_improvement",
                    String.format("%.2fx faster", (double) normalDuration / pipelineDuration));
            comparison.put("time_saved_ms", normalDuration - pipelineDuration);
            comparison.put("status", "SUCCESS");

        } catch (Exception e) {
            log.error("Error during performance comparison", e);
            comparison.put("status", "ERROR");
            comparison.put("error", e.getMessage());
        }

        return comparison;
    }

    public Map<String, Object> getSampleUserData(int userId) {
        Map<String, Object> result = new HashMap<>();

        try {
            String key = "user:" + userId;
            Map<Object, Object> userData = redisTemplate.opsForHash().entries(key);

            result.put("key", key);
            result.put("data", userData);
            result.put("exists", !userData.isEmpty());
            result.put("status", "SUCCESS");

        } catch (Exception e) {
            log.error("Error getting user data", e);
            result.put("status", "ERROR");
            result.put("error", e.getMessage());
        }

        return result;
    }
}