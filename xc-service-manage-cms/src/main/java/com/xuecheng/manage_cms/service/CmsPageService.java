package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.*;
import com.xuecheng.manage_cms.dao.CmsPageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Date:2018/12/8
 * Author:gyc
 * Desc:
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class CmsPageService {

    @Autowired
    private CmsPageDao cmsPageDao;

    public Page<CmsPage> findList(int page, int size, QueryPageRequest queryPageRequest){

        if(page <= 0){
            page = 1;
        }
        page--;

        PageRequest of = PageRequest.of(page, size);

        return  cmsPageDao.findAll(of);
    }

}

