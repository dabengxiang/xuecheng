package com.xuecheng.framework.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Date:2019/2/9
 * Author:gyc
 * Desc:
 */
@Data
@NoArgsConstructor
@ToString
public class CoursePublishResult extends  ResponseResult{
    String previewUrl;

    public CoursePublishResult(ResultCode resultCode, String previewUrl){
        super(resultCode);
        this.previewUrl = previewUrl;
    }
}
