package com.xuecheng.manage_cms.web.controller;

import com.xuecheng.api.cms.SysDictionaryControllerApi;
import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_cms.service.SysDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * project name : xuecheng
 * Date:2019/1/9
 * Author: yc.guo
 * DESC:
 */

@RestController
@RequestMapping("/sys/dictionary")
public class SysDictionaryController implements SysDictionaryControllerApi {
    
    @Autowired
    private SysDictionaryService sysDicthinaryService;
    
    
    @GetMapping("/get/{dType}")
    @Override
    public SysDictionary getByType(@PathVariable("dType") String type) {
        return sysDicthinaryService.getByType(type);
    }
}
