package com.elens.data.rbces.service;

import com.elens.data.rbces.mybatis.entity.ElensUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xuweichao
 * @since 2019-04-11
 */
public interface IElensUserService extends IService<ElensUser> {

    List<Map<String, Object>> getCompanyCount();

}
