package com.xuecheng.framework.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Date:2018/12/16
 * Author:gyc
 * Desc:
 */
@Data
@ToString
@NoArgsConstructor
public class CommonResponseResult  extends ResponseResult{

    Object data;

    public CommonResponseResult(ResultCode resultCode,Object data){
        super(resultCode);
        this.data = data;
    }

    public static CommonResponseResult SUCCESS(Object data){
        return new CommonResponseResult(CommonCode.SUCCESS,data);
    }

    public static CommonResponseResult FAIL(Object data){
        return new CommonResponseResult(CommonCode.FAIL,data);
    }

}