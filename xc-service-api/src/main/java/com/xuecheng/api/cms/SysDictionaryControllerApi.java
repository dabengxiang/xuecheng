package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.system.SysDictionary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * project name : xuecheng
 * Date:2019/1/9
 * Author: yc.guo
 * DESC:
 */

@Api(value = "数据接口管理接口" , description = "根据数据管理接口来管理")
public interface SysDictionaryControllerApi {

    @ApiOperation(value = "数据字典查询接口")
    public SysDictionary getByType(String type);
}
