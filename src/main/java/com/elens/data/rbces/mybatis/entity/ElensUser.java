package com.elens.data.rbces.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author xuweichao
 * @since 2019-04-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ElensUser对象", description="")
public class ElensUser extends Model<ElensUser> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    private String userName;

    private String userCompany;

    private String userPassword;

    private String token;

    private LocalDateTime createTime;

    private Integer dr;

    private String reserve1;

    private String reserve2;

    private Integer accuracy;

    private Integer totalLabel;

    @ApiModelProperty(value = "0：注销，1：启用，3：删除")
    private Integer status;

    private String name;

    @ApiModelProperty(value = "是否系统用户（系统用户不显示）")
    private String sfxtyh;


    @Override
    protected Serializable pkVal() {
        return this.userId;
    }

}
