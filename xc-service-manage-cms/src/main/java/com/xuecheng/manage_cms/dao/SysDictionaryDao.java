package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.system.SysDictionary;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * project name : xuecheng
 * Date:2019/1/9
 * Author: yc.guo
 * DESC:
 */
public interface SysDictionaryDao extends MongoRepository<SysDictionary,String> {
    public List<SysDictionary> findByDType(String type);
}
