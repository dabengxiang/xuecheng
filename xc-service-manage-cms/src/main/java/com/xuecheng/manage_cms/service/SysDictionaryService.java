package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_cms.dao.SysDictionaryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * project name : xuecheng
 * Date:2019/1/9
 * Author: yc.guo
 * DESC:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysDictionaryService {

    @Autowired
    SysDictionaryDao sysDictionaryDao;


    public SysDictionary getByType(String type) {

//        SysDictionary sysDictionaryExample = new SysDictionary();
//        sysDictionaryExample.setDType(type);
//        Example<SysDictionary> of = Example.of(sysDictionaryExample);
//
//        Optional<SysDictionary> optional = sysDictionaryDao.findOne(of);
//        return optional.get();


        List<SysDictionary> byDType = sysDictionaryDao.findByDType(type);
        
        return (byDType!=null && !byDType.isEmpty()) ? byDType.get(0) : null;


    }
}
