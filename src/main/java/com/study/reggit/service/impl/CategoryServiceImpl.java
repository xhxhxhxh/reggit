package com.study.reggit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.reggit.common.CustomException;
import com.study.reggit.entity.Category;
import com.study.reggit.entity.Dish;
import com.study.reggit.entity.Setmeal;
import com.study.reggit.mapper.CategoryMapper;
import com.study.reggit.service.CategoryService;
import com.study.reggit.service.DishService;
import com.study.reggit.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
  @Autowired
  private DishService dishService;

  @Autowired
  private SetmealService setmealService;

  @Override
  public boolean removeById(Serializable id) {
    LambdaQueryWrapper<Dish> dishWrapper = new LambdaQueryWrapper<>();
    dishWrapper.eq(Dish::getCategoryId, id);

    Dish oneDish = dishService.getOne(dishWrapper, false);

    if (oneDish != null) {
      throw new CustomException("删除失败，该分类与菜品存在关联");
    }

    LambdaQueryWrapper<Setmeal> setmealWrapper = new LambdaQueryWrapper<>();
    setmealWrapper.eq(Setmeal::getCategoryId, id);

    Setmeal oneSetmeal = setmealService.getOne(setmealWrapper, false);

    if (oneSetmeal != null) {
      throw new CustomException("删除失败，该分类与套餐存在关联");
    }

    return super.removeById(id);
  }
}
