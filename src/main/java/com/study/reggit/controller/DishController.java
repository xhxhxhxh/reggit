package com.study.reggit.controller;

import com.study.reggit.common.R;
import com.study.reggit.dto.DishDto;
import com.study.reggit.entity.Dish;
import com.study.reggit.service.DishFlavorService;
import com.study.reggit.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
