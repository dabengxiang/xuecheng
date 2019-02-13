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
@ToString
@NoArgsConstructor
public class CmsPostPageResult extends ResponseResult{

    String pageUrl;

    public CmsPostPageResult(ResultCode resultCode, String pageUrl){
        super(resultCode);
        this.pageUrl = pageUrl;

    }

}
