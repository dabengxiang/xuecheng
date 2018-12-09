package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_cms.ManageCmsApplicaitonTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Date:2018/12/8
 * Author:gyc
 * Desc:
 */


@Slf4j
public class CmsPageDaoTest extends ManageCmsApplicaitonTest{


    @Autowired
    private CmsPageDao cmsPageDao;

    @Test
    public void findAll(){
        List<CmsPage> all = cmsPageDao.findAll();

        for (CmsPage cmsPage : all) {
            log.info(cmsPage.toString());
        }
    }


    @Test
    public void findPage(){
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<CmsPage> page = cmsPageDao.findAll(pageRequest);

        System.out.println(page.getTotalElements());
        System.out.println(page.getNumber());
        System.out.println(page.getSize());


    }

}