package com.weixin.request.controller;

import com.weixin.request.entity.Realm;
import com.weixin.request.entity.User;
import com.weixin.request.service.RealmService;
import com.weixin.request.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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


    @GetMapping("/getState")
    public String getState(){
        User user = new User();
        user.setName("/protocol/openid-connect/auth");
        User user1 = userService.selectByPrimaryKey(user);
        return user1.getPassword();
    }

    @GetMapping("/getUserId")
    public String getUserId(HttpServletRequest request){
        String unionid = (String) request.getSession().getAttribute("unionid");
        User user = new User();
        user.setUnionId(unionid);
        User user1 = userService.selectByPrimaryKey(user);
        if (user1 != null){
            return user1.getId();
        }
        return null;
    }

}
