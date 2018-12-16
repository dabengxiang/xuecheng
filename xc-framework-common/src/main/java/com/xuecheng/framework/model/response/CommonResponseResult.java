package com.xuecheng.framework.model.response;

import lombok.Data;
import lombok.ToString;

/**
 * Date:2018/12/16
 * Author:gyc
 * Desc:
 */
@Data
@ToString
public class CommonResponseResult<T>  extends ResponseResult{

    T data;

    public CommonResponseResult(ResultCode resultCode,T data){
        super(resultCode);
        this.data = data;
    }
}
