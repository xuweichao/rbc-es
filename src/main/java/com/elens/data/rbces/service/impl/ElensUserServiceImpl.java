package com.elens.data.rbces.service.impl;

import com.elens.data.rbces.mybatis.entity.ElensUser;
import com.elens.data.rbces.mybatis.mapper.ElensUserMapper;
import com.elens.data.rbces.service.IElensUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xuweichao
 * @since 2019-04-11
 */
@Service
public class ElensUserServiceImpl extends ServiceImpl<ElensUserMapper, ElensUser> implements IElensUserService {
    @Autowired
    ElensUserMapper elensUserMapper;

    @Override
    public List<Map<String, Object>> getCompanyCount() {
        return elensUserMapper.getGroupByCompany();
    }
}
