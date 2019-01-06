package com.xuecheng.framework.utils;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Date:2019/1/6
 * Author:gyc
 * Desc:
 */
public interface MyMapper<T> extends Mapper<T>,MySqlMapper<T> {
}
