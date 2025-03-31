package com.weixin.request.controller;

import com.alibaba.fastjson.JSONObject;
import com.weixin.request.entity.Realm;
import com.weixin.request.entity.User;
import com.weixin.request.service.RealmService;
import com.weixin.request.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author weiyao_tian 2025/3/17
 */
@Slf4j
@RestController
public class ApiWeiXinController {

    Logger logging = LoggerFactory.getLogger(ApiWeiXinController.class);

    @Autowired
    private RealmService realmService;
    @Autowired
    private UserService userService;
    @Autowired
    public RedisTemplate redisTemplate;

    @GetMapping("/selectRealmAll")
    public Map<String,Object> selectRealmAll(){

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

    @GetMapping("/getUserDetil")
    public String getUserDetil(){
        String unionid = data("D:\\lz\\input.txt");
        logging.info("unionid:"+unionid);
        JSONObject jsonObject = JSONObject.parseObject(unionid);
        unionid = jsonObject.getString("unionid");
        User user = new User();
        user.setUnionId(unionid);
        User user1 = userService.selectByPrimaryKey(user);
        String url_copy = "http://127.0.0.1:65010/callback?data="+user1.getId();
        return url_copy;
    }

    public String data(String path){
        try {
            List<String> lines = Files.readAllLines(Paths.get(path));
            StringBuilder sb = new StringBuilder();
            for (String line : lines) {
                sb.append(line);
            }
            System.out.println(sb);
            return sb.toString();
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        return null;
    }



}
