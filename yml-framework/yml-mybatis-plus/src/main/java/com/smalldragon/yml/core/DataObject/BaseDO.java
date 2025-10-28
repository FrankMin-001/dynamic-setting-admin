package com.smalldragon.yml.core.DataObject;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author YML
 * @Date 2025/2/28 0:39
 **/
@Data
public class BaseDO implements Serializable {

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建人ID
     */
    @TableField(fill = FieldFill.INSERT,jdbcType = JdbcType.BIGINT)
    private String createId;

    /**
     * 更新人ID
     */
    @TableField(fill = FieldFill.UPDATE,jdbcType = JdbcType.BIGINT)
    private String updateId;

}
