package com.smalldragon.yml.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author YML
 * @Date 2025/3/6 21:08
 **/
@Data
@ApiModel(value = "QueryPage", description = "公共响应类")
public class QueryPage implements Serializable {

    private static final int DEFAULT_PAGE_NO = 1;

    private static final int DEFAULT_PAGE_SIZE = 10;

    @ApiModelProperty(value = "页码，从1开始", example = "1")
    @NotNull(message = "页面不能为空!")
    @Min(value = 1,message = "页面数最低为1")
    private Integer pageNo;

    @ApiModelProperty(value = "每页大小", example = "10")
    @NotNull(message = "页面不能为空!")
    @Min(value = 1,message = "每页大小最低为1")
    private Integer pageSize;

    @ApiModelProperty(value = "模糊查询", example = "叶良辰")
    private String keywords;

    public Integer getPageNo() {
        if (pageNo == null || pageNo <= 0) {
            return DEFAULT_PAGE_NO;
        }
        return pageNo;
    }

    public Integer getPageSize() {
        if (pageSize == null || pageSize <= 0) {
            return DEFAULT_PAGE_SIZE;
        }
        return pageSize;
    }

}
