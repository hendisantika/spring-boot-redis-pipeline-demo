package id.my.hendisantika.redispipelinedemo.controller;

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

import id.my.hendisantika.redispipelinedemo.service.RedisPipelineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/redis")
@RequiredArgsConstructor
@Slf4j
public class RedisPipelineController {

    private final RedisPipelineService redisPipelineService;

    @PostMapping("/pipeline/insert")
    public ResponseEntity<Map<String, Object>> insertData10KPipeline() {
        log.info("Starting pipeline insert operation for 10K records");
        Map<String, Object> result = redisPipelineService.insertData10K();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/normal/insert")
    public ResponseEntity<Map<String, Object>> insertData10KNormal() {
        log.info("Starting normal insert operation for 10K records");
        Map<String, Object> result = redisPipelineService.insertDataNormal();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/pipeline/read")
    public ResponseEntity<Map<String, Object>> readData10KPipeline() {
        log.info("Starting pipeline read operation for 10K records");
        Map<String, Object> result = redisPipelineService.readData10K();
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/pipeline/delete")
    public ResponseEntity<Map<String, Object>> deleteData10KPipeline() {
        log.info("Starting pipeline delete operation for 10K records");
        Map<String, Object> result = redisPipelineService.deleteData10K();
        return ResponseEntity.ok(result);
    }


    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getRedisInfo() {
        log.info("Getting Redis database information");
        Map<String, Object> result = redisPipelineService.getRedisInfo();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/performance/compare")
    public ResponseEntity<Map<String, Object>> performanceComparison() {
        log.info("Starting performance comparison between pipeline and normal operations");
        Map<String, Object> result = redisPipelineService.performanceComparison();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserData(@PathVariable int userId) {
        log.info("Getting user data for user ID: {}", userId);
        Map<String, Object> result = redisPipelineService.getSampleUserData(userId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> health = Map.of(
                "status", "UP",
                "service", "Redis Pipeline Demo",
                "timestamp", String.valueOf(System.currentTimeMillis())
        );
        return ResponseEntity.ok(health);
    }
}