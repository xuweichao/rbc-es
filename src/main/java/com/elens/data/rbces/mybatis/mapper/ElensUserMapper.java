package com.elens.data.rbces.mybatis.mapper;

import com.elens.data.rbces.mybatis.entity.ElensUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xuweichao
 * @since 2019-04-11
 */
public interface ElensUserMapper extends BaseMapper<ElensUser> {

    List<Map<String, Object>> getGroupByCompany();

}
