package com.weixin.request.service.impl;

import com.weixin.request.dao.UserDao;
import com.weixin.request.entity.User;
import com.weixin.request.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author weiyao_tian 2025/3/17
 */
@Service
public class UserServiceImpl implements UserService {

     @Autowired
     private UserDao userDao;


    @Override
    public User selectByPrimaryKey(User user) {
        return userDao.selectByPrimaryKey(user);
    }

    @Override
    public void insert(User user) {
        userDao.insert(user);
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }
}
