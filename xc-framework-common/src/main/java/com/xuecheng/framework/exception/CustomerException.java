package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * Date:2018/12/27
 * Author:gyc
 * Desc:
 */

public class CustomerException extends  RuntimeException {
    
    private ResultCode resultCode;
    
     public CustomerException(ResultCode resultCode){
         this.resultCode = resultCode;
     }
     public ResultCode getResultCode(){
         return resultCode;
     }
     

}
