package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * project name : xuecheng
 * Date:2018/12/28
 * Author: yc.guo
 * DESC:
 */
public class ExceptionCast {
    
    public static void cast(ResultCode resultCode){
        throw new CustomerException(resultCode);
    }
}
