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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/redis")
@RequiredArgsConstructor
@Slf4j
public class RedisPipelineController {

    private final RedisPipelineService redisPipelineService;


}