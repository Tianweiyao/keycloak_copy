package com.weixin.request.service;

import com.weixin.request.entity.User;

/**
 * @author weiyao_tian 2025/3/17
 */
public interface UserService {

    User selectByPrimaryKey(User user);

    void insert(User user);


    void update(User user);

}
