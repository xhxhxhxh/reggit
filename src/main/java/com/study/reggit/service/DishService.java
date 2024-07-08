package com.study.reggit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.reggit.dto.DishDto;
import com.study.reggit.entity.Dish;

public interface DishService extends IService<Dish> {
  void saveWithFlavor(DishDto dishDto);
}
