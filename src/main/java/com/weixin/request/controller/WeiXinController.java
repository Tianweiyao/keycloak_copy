package com.weixin.request.controller;

import ch.qos.logback.core.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import com.weixin.request.entity.User;
import com.weixin.request.service.UserService;
import com.weixin.request.util.HttpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import static com.weixin.request.util.HttpUtil.doGet;

/**
 * @author weiyao_tian 2025/3/15
 */
@Slf4j
@Controller
public class WeiXinController {

    private static Logger logging = LoggerFactory.getLogger(WeiXinController.class);

    @Autowired
    private UserService userService;

    private static final String APPID = "wxa8de49759d00f0f9";
    private static final String SECRET = "b68dbef76b5701dc0c3d4bdbbd597a16";

    public void index(HttpServletRequest request,HttpServletResponse response,String code,String state,String redirect_uri) throws IOException {

        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + APPID + "&secret=" + SECRET+"&code="+ code +"&grant_type=authorization_code";
        String data = doGet(url);
        JSONObject jsonObject = JSONObject.parseObject(data);
        String access_token = jsonObject.getString("access_token");
        String openid = jsonObject.getString("openid");
        String unionid = jsonObject.getString("unionid");
        logging.info("access_token:{}",access_token);
        logging.info("openid:{}",openid);
        logging.info("unionid:{}",unionid);
        String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openid+"&lang=zh_CN";
        String userInfoData = doGet(userInfoUrl);
        logging.info("userInfoData:{}",userInfoData);
        JSONObject userInfoObject = JSONObject.parseObject(userInfoData);
        String nickname = userInfoObject.getString("nickname");
        if (StringUtil.notNullNorEmpty(nickname)){
            nickname = HttpUtil.decode(HttpUtil.encode(nickname,"iso-8859-1"),"UTF-8");
        }
        request.getSession().setAttribute("unionid",unionid);
        logging.info("nickname:{}",nickname);
        User user = new User();
        user.setUnionId(unionid);
        user = userService.selectByPrimaryKey(user);
        if (user != null){ // 已存在跳转到第5步
            user.setCode(code);
            user.setPassword(state);
            userService.update(user);
            if (redirect_uri != null){
                String redirect_uri_copy = redirect_uri + "?code="+code+"&data="+user.getId();
                response.sendRedirect(redirect_uri_copy);
            }

        }else {
            //跳转到注册页面
        }
    }


    @GetMapping("/realms/test/protocol/openid-connect/auth")
    public String SendMessage(HttpServletRequest request,HttpServletResponse response, @RequestParam("response_type") String response_type,@RequestParam("client_id") String client_id,
                            @RequestParam("redirect_uri") String redirect_uri,@RequestParam("scope") String scope,
                            @RequestParam("state") String state,@RequestParam(name = "code",required = false) String code) throws IOException {


        String url = "http://127.0.0.1:8081/realms/test/protocol/openid-connect/auth?response_type="+ response_type +"&client_id="+ client_id +"&redirect_uri="+ redirect_uri +"&scope="+ scope +"&state="+state;
        System.out.println("response_type:"+url);
        String weixin_url = "https://open.weixin.qq.com/connect/qrconnect?scope=snsapi_login&state="+stringToHex(url)+"&response_type="+response_type+"&appid="+APPID+"&redirect_uri=https://api.evn.magnetworks.cn/sso_cb";
        System.out.println("response_type:"+stringToHex(url));
        if (code == null){ //如果是空代表是第一次请求
            response.sendRedirect(weixin_url);
        }else if (code != null){
            index(request,response,code,state,redirect_uri);
        }
        return null;
    }

    public static String stringToHex(String str) { //转换16进制
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        StringBuilder hexBuilder = new StringBuilder();

        for (byte b : bytes) {
            // 字节转无符号整数处理负数问题
            int unsignedByte = b & 0xFF;
            // 格式化为两位十六进制，不足补0
            hexBuilder.append(String.format("%02x", unsignedByte));
        }
        return hexBuilder.toString();
    }

    public static String hexToString(String hex) { //16进制转换为中文
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i/2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i+1), 16));
        }
        return new String(data, StandardCharsets.UTF_8);
    }


}
