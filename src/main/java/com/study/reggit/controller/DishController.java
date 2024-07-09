package com.study.reggit.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.study.reggit.common.R;
import com.study.reggit.dto.DishDto;
import com.study.reggit.entity.Dish;
import com.study.reggit.service.DishFlavorService;
import com.study.reggit.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dish")
public class DishController {
  @Autowired
  private DishService dishService;
  @Autowired
  private DishFlavorService dishFlavorService;

  /**
   * 新增菜品
   * @param dishDto
   * @return
   */
  @PostMapping
  public R<String> list(@RequestBody DishDto dishDto) {
    dishService.saveWithFlavor(dishDto);
    return R.success("新增菜品成功");
  }

  @GetMapping("/list")
  public R<List<Dish>> getListByCategory(Dish dish) {
    LambdaQueryWrapper<Dish> dishWrapper = new LambdaQueryWrapper<>();
    dishWrapper.eq(Dish::getCategoryId, dish.getCategoryId());
    dishWrapper.eq(Dish::getStatus, 1);
    dishWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
    List<Dish> dishList = dishService.list(dishWrapper);
    return R.success(dishList);
  }
}
