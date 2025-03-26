package com.weixin.request.dao;

import com.weixin.request.entity.User;
import org.springframework.stereotype.Repository;

/**
 * @author weiyao_tian 2025/3/17
 */
@Repository
public interface UserDao {

    User selectByPrimaryKey(User user);

    void insert(User user);

    void update(User user);

}
