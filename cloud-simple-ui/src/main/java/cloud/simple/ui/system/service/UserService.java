/*
 * Copyright 2012-2020 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * @author lzhoumail@126.com/zhouli
 * Git http://git.oschina.net/zhou666/spring-cloud-7simple
 */
package cloud.simple.ui.system.service;

import cloud.simple.ui.system.model.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    RestTemplate restTemplate;

    //	 @Autowired
    //	 FeignUserService feignUserService;

    final String SERVICE_NAME = "cloud-simple-service";

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @HystrixCommand(fallbackMethod = "fallbackSearchAll")
    public List<User> readUserInfo() {
        this.loadBalancerClient.choose(SERVICE_NAME);
        return restTemplate.getForObject("http://" + SERVICE_NAME + "/user", List.class);
        //return feignUserService.readUserInfo();
    }

    private List<User> fallbackSearchAll() {
        System.out.println("HystrixCommand fallbackMethod handle!");
        List<User> ls = new ArrayList<User>();
        User user = new User();
        user.setId(1);
        user.setUsername("TestHystrixCommand");
        ls.add(user);
        return ls;
    }
}
