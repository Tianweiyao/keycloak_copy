package com.weixin.request.controller;

import com.weixin.request.entity.Realm;
import com.weixin.request.entity.User;
import com.weixin.request.service.RealmService;
import com.weixin.request.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

/**
 * @author weiyao_tian 2025/3/17
 */
@RestController
public class ApiWeiXinController {

    @Autowired
    private RealmService realmService;
    @Autowired
    private UserService userService;
    @Autowired
    public RedisTemplate redisTemplate;

    @GetMapping("/selectRealmAll")
    public Map<String,Object> selectRealmAll(HttpServletRequest request, HttpServletResponse response){

        Map<String,Object> map = new HashMap<>();
        List<Realm> list =  realmService.selectAll();
        map.put("code",200);
        map.put("message","请求成功");
        map.put("data",list);
        return map;
    }

    @PostMapping("/insertUser")
    public Map<String,Object> insertUser(@RequestBody User user){
        String userId = UUID.randomUUID().toString();
        user.setId(userId);
        user.setCreateTime(new Date());
        userService.insert(user);
        Map<String,Object> map = new HashMap<>();
        map.put("code",200);
        map.put("message","请求成功");
        return map;
    }

    @GetMapping("/getDetil")
    public String getDetil(){
     String json = (String) redisTemplate.opsForValue().get("json");
     return json;
    }

}
