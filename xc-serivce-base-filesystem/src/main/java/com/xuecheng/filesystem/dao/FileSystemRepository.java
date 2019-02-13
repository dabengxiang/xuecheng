package com.xuecheng.filesystem.dao;

import com.xuecheng.framework.domain.filesystem.FileSystem;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Date:2019/2/1
 * Author:gyc
 * Desc:
 */
public interface FileSystemRepository  extends MongoRepository<FileSystem,String>{
}
