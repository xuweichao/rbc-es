package com.elens.data.rbces.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.elens.data.rbces.mybatis.entity.ElensUser;
import com.elens.data.rbces.service.IElensUserService;
import com.elens.data.rbces.vo.QueryPageDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xuweichao
 * @since 2019-04-11
 */
@Log
@Api(value = "mybatis-plus 测试", tags = "mybatis-plus 测试")
@RestController
@RequestMapping("/user")
public class ElensUserController {
    @Autowired
    IElensUserService elensUserService;

    @ApiOperation(value = "分页获取数据", notes = "分页查询")
    @PostMapping("page")
    public Page getList(@RequestBody QueryPageDto queryPageDto) {
        log.info(queryPageDto.toString());
        Page<ElensUser> userPage = new Page<>(queryPageDto.getPage(), queryPageDto.getSize());
        QueryWrapper<ElensUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("user_name", "user_company");
        queryWrapper.like("user_name", "测试");
        queryWrapper.ne("user_company", "");

        elensUserService.pageMaps(userPage, queryWrapper);
        return userPage;
    }

    @ApiOperation(value = "聚合", notes = "聚合")
    @GetMapping("group")
    public List<Map<String, Object>> getListGroup() {
        List<Map<String, Object>> result = elensUserService.getCompanyCount();
        return result;
    }

}

