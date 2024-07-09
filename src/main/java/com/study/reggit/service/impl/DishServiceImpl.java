package com.study.reggit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.reggit.dto.DishDto;
import com.study.reggit.entity.Dish;
import com.study.reggit.entity.DishFlavor;
import com.study.reggit.mapper.DishMapper;
import com.study.reggit.service.DishFlavorService;
import com.study.reggit.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
  @Autowired
  private DishFlavorService dishFlavorService;

  @Transactional
  public void saveWithFlavor(DishDto dishDto) {
    this.save(dishDto);

    Long id = dishDto.getId();

    List<DishFlavor> flavors = dishDto.getFlavors();
    flavors.forEach(flavorDto -> {
      flavorDto.setDishId(id);
    });

    dishFlavorService.saveBatch(flavors);

  }

  @Transactional
  public void updateWithFlavor(DishDto dishDto) {
    this.updateById(dishDto);

    LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
    dishFlavorService.remove(queryWrapper);

    List<DishFlavor> flavors = dishDto.getFlavors();
    flavors.forEach(flavorDto -> {
      flavorDto.setDishId(dishDto.getId());
    });

    dishFlavorService.saveBatch(flavors);
  }
}
