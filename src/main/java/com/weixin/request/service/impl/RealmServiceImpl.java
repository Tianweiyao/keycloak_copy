package com.weixin.request.service.impl;

import com.weixin.request.entity.Realm;
import com.weixin.request.dao.RealmDao;
import com.weixin.request.service.RealmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author weiyao_tian 2025/3/17
 */
@Service
public class RealmServiceImpl implements RealmService {


    @Autowired
    private RealmDao realmMapper;


    @Override
    public List<Realm> selectAll() {

        return realmMapper.selectAll();
    }
}
