package com.weixin.request.controller;

import com.alibaba.fastjson.JSONObject;
import com.weixin.request.entity.User;
import com.weixin.request.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author weiyao_tian 2025/3/15
 */
@Slf4j
@RestController
public class WeiXinController {

    private static Logger logging = LoggerFactory.getLogger(WeiXinController.class);

    @Autowired
    private UserService userService;
    @Autowired
    public RedisTemplate redisTemplate;

    public String index(HttpServletRequest request, HttpServletResponse response, String code, String state, String redirect_uri) throws IOException {

        /**
         * 取出keycloak自己生成的state，解析state,获取用微信用户信息，unionid判断用户是否存在
         */
        String unionid = data("D:\\lz\\input.txt");
        logging.info("unionid:"+unionid);
        JSONObject jsonObject = JSONObject.parseObject(unionid);
        unionid = jsonObject.getString("unionid");
        User user = new User();
        user.setUnionId(unionid);
        User user1 = userService.selectByPrimaryKey(user);


        if (unionid != null){ //判断用户是否存在，已经存在，重定向到keycloak登录接口
            if (user1 != null){
                logging.info("有人进来了");
                request.getSession().setAttribute("auth_state",state);
                String qrconnect_state = data("D:\\lz\\output.txt");
                String qrconnect_url = "http://127.0.0.1:80/realms/test/broker/weixin/endpoint?code=" + code + "&state=" + qrconnect_state;
                response.sendRedirect(qrconnect_url);
            }else { //没有该用户，跳到注册页面
                logging.info("没人进来了");
                return "redirect:https://www.baidu.com";
            }
        }
        return null;
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


    /**
     * 该方法是第一步 用于拦截请求自定义重定向url及state
     */
    @GetMapping("/realms/test/protocol/openid-connect/auth")
    public String SendMessage(HttpServletRequest request, HttpServletResponse response, @RequestParam("response_type") String response_type, @RequestParam("client_id") String client_id,
                              @RequestParam("redirect_uri") String redirect_uri, @RequestParam("scope") String scope,
                              @RequestParam("state") String state, @RequestParam(name = "code", required = false) String code) throws IOException, ServletException, InterruptedException {

        String json = "response_type="+response_type+"&client_id="+client_id+"&redirect_uri="+redirect_uri+"&scope="+scope+"&state="+state;
        redisTemplate.opsForValue().set("json",json);


        if (code == null) { //如果是空代表是第一次请求,用于拦截请求自定义重定向url及state
            String url = "http://127.0.0.1:80/realms/test/protocol/openid-connect/auth?response_type=" + response_type + "&client_id=" + client_id + "&redirect_uri=" + redirect_uri + "&scope=" + scope + "&state=" + state;
            System.out.println("response_type:" + url);
            response.sendRedirect(url);
        } else if (code != null) { //不为空代表是微信扫码成功已获得用户信息，验证用户是否存在，然后跳转到第5步
            index(request, response, code, state, redirect_uri);
        }
        return null;
    }

}
