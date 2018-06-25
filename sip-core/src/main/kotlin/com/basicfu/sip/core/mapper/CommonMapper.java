package com.basicfu.sip.core.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;

public interface CommonMapper<T> {
    @UpdateProvider(type = CommonProvider.class, method = "dynamicSQL")
    int updateBySql(@Param("sql") String sql);
}
