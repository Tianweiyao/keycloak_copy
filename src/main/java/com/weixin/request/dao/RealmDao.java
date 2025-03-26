package com.weixin.request.dao;

import com.weixin.request.entity.Realm;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author weiyao_tian 2025/3/17
 */
@Repository
public interface RealmDao {

    List<Realm> selectAll();

}
