package com.basicfu.sip.core.mapper

import tk.mybatis.mapper.common.IdsMapper
import tk.mybatis.mapper.common.Mapper
import tk.mybatis.mapper.common.MySqlMapper

interface CustomMapper<T> : Mapper<T>, MySqlMapper<T>, IdsMapper<T>,CommonMapper<T>