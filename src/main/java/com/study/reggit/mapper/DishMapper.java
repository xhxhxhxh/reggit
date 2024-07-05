package com.study.reggit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.study.reggit.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
