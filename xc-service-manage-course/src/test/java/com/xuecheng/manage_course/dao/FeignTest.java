package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.model.response.CommonResponseResult;
import com.xuecheng.manage_course.client.CmsPageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Date:2019/2/7
 * Author:gyc
 * Desc:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class FeignTest {

    @Autowired
    private CmsPageClient cmsPageClient;

    @Test
    public void findByCmsById(){
        CommonResponseResult byId = cmsPageClient.findById("5a754adf6abb500ad05688d9");
        System.err.println(byId);
    }

}
