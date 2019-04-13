package ${package.Controller};


<#if restControllerStyle>
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.*;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import com.elens.data.rbces.vo.Wrapper;
import com.elens.data.rbces.vo.WrapMapper;

import com.elens.data.rbces.vo.QueryPageDto;

import ${package.Service}.${table.serviceName};
import ${package.Entity}.${entity};


/**
 *
 * @author ${author}
 * @since ${date}
 */
<#if restControllerStyle??>
@Log
@Api(value="${entity} 相关接口",tags ="${entity} 相关接口")
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName??>${package.ModuleName}</#if><#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>
    @Autowired
    public ${table.serviceName} ${table.entityPath}Service;


    /**
     * 分页查询数据
     *
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @PostMapping("getPage")
    public Wrapper get${entity}List(@RequestBody QueryPageDto queryPageDto){
        log.info("获取的参数：===>>" + queryPageDto);

        Page<${entity}> page = new Page<>(queryPageDto.getPage(), queryPageDto.getSize());
        try{
            QueryWrapper<${entity}> queryWrapper = new QueryWrapper<>();
        <#--queryWrapper.select("user_name", "user_company");-->
        <#--queryWrapper.like("user_name", "测试");-->
        <#--queryWrapper.ne("user_company", "");-->
            ${table.entityPath}Service.pageMaps(page, queryWrapper);

            }catch(Exception e){
                e.printStackTrace();
                return WrapMapper.error();
            }
            return WrapMapper.wrap(Wrapper.SUCCESS_CODE, Wrapper.SUCCESS_MESSAGE, page);
    }

    /**
     * 添加修改
     * @param ${table.entityPath}
     * @return
     */
    @ApiOperation(value = "添加或修改", notes = "根据id添加或修改")
    @PostMapping("addUpd")
    public Wrapper ${table.entityPath}AddUpd(${entity} ${table.entityPath}){
        log.info("获取的参数：===>>" + ${table.entityPath});
            try{
                ${table.entityPath}Service.saveOrUpdate(${table.entityPath});
            }catch(Exception e){
                 e.printStackTrace();
                return WrapMapper.error();
            }
            return WrapMapper.ok();
    }



    /**
     * 根据id删除对象
     * @param id  实体ID
     */
    @ApiOperation(value = "删除", notes = "根据id删除")
    @GetMapping("del/{id}")
    public Wrapper ${table.entityPath}Delete(@PathVariable int id){
        log.info("获取的参数：===>>" + id);
            try{
                ${table.entityPath}Service.removeById(id);
            }catch(Exception e){
                e.printStackTrace();
                return WrapMapper.error();
            }
            return WrapMapper.ok();
    }

}
