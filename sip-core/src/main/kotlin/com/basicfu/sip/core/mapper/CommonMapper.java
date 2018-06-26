package com.basicfu.sip.core.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

public interface CommonMapper<T> {

    @SelectProvider(type = CommonProvider.class, method = "dynamicSQL")
    Integer selectCountBySql(@Param("sql") String sql);

    @SelectProvider(type = CommonProvider.class, method = "dynamicSQL")
    List<T> selectBySql(@Param("sql") String sql);

    @UpdateProvider(type = CommonProvider.class, method = "dynamicSQL")
    int updateBySql(@Param("sql") String sql);
}
