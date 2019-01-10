package com.xuecheng.manage_cms.dao;

import com.xuecheng.api.cms.SysDictionaryControllerApi;
import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_cms.ManageCmsApplicaitonTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Optional;

/**
 * project name : xuecheng
 * Date:2019/1/9
 * Author: yc.guo
 * DESC:
 */
public class SysDictionaryDaoTest extends ManageCmsApplicaitonTest {
    
    
    @Autowired
    private SysDictionaryDao sysDictionaryDao; 
    
    @Test
    public void findAll(){
        SysDictionary sysDictionary = new SysDictionary();
        sysDictionary.setId("5a7d50bdd019f150f4ab8ef7");
        Example<SysDictionary> of = Example.of(sysDictionary);
        Optional<SysDictionary> one = sysDictionaryDao.findOne(of);
        System.out.println(one.get());

    }

}