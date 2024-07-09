package com.study.reggit.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.study.reggit.common.R;
import com.study.reggit.dto.DishDto;
import com.study.reggit.entity.Category;
import com.study.reggit.entity.Dish;
import com.study.reggit.entity.DishFlavor;
import com.study.reggit.service.CategoryService;
import com.study.reggit.service.DishFlavorService;
import com.study.reggit.service.DishService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
public class DishController {
  @Autowired
  private DishService dishService;
  @Autowired
  private DishFlavorService dishFlavorService;
  @Autowired
  private CategoryService categoryService;

  /**
   * 新增菜品
   * @param dishDto
   * @return
   */
  @PostMapping
  public R<String> save(@RequestBody DishDto dishDto) {
    dishService.saveWithFlavor(dishDto);
    return R.success("新增菜品成功");
  }

  @PutMapping
  public R<String> update(@RequestBody DishDto dishDto) {
    dishService.updateWithFlavor(dishDto);
    return R.success("保存菜品成功");
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

  @GetMapping("/page")
  public R<Page<DishDto>> page(int page, int pageSize, String name) {
    Page<Dish> pageInfo = new Page<>(page, pageSize);

    LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

    queryWrapper.like(StringUtils.isNotEmpty(name), Dish::getName, name);

    queryWrapper.orderByDesc(Dish::getUpdateTime);

    dishService.page(pageInfo, queryWrapper);

    Page<DishDto> dishDtoPageInfo = new Page<>(page, pageSize);
    BeanUtils.copyProperties(pageInfo, dishDtoPageInfo, "records");

    if (pageInfo.getRecords().isEmpty()) {
      return R.success(dishDtoPageInfo);
    }

    List<Dish> records = pageInfo.getRecords();
    List<Long> categoryIds = records.stream().map(Dish::getCategoryId).collect(Collectors.toList());

    List<Category> categories = categoryService.listByIds(categoryIds);
    HashMap<Long, String> categoryIdToNameMap = new HashMap<>();
    categories.forEach(item -> {
      categoryIdToNameMap.put(item.getId(), item.getName());
    });

    List<DishDto> dishDtoList = new ArrayList<>();
    records.forEach(item -> {
      DishDto dishDto = new DishDto();
      String categoryName = categoryIdToNameMap.get(item.getCategoryId());
      dishDto.setCategoryName(categoryName);

      BeanUtils.copyProperties(item, dishDto);
      dishDtoList.add(dishDto);
    });

    dishDtoPageInfo.setRecords(dishDtoList);

    return R.success(dishDtoPageInfo);
  }

  @PostMapping("/status/{status}")
  public R<String> updateStatus(@PathVariable Integer status, @RequestParam List<Long> ids) {
    LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper<>();
    updateWrapper.in(Dish::getId, ids).set(Dish::getStatus, status);
    dishService.update(updateWrapper);
    return R.success("修改成功");
  }

  @DeleteMapping
  public R<String> delete(@RequestParam List<Long> ids) {
    dishService.removeByIds(ids);
    return R.success("删除成功");
  }

  @GetMapping("/{id}")
  public R<DishDto> getById(@PathVariable Long id) {
    Dish dish = dishService.getById(id);
    DishDto dishDto = new DishDto();

    BeanUtils.copyProperties(dish, dishDto);

    LambdaUpdateWrapper<DishFlavor> queryWrapper = new LambdaUpdateWrapper<>();
    queryWrapper.eq(DishFlavor::getDishId, id);
    List<DishFlavor> list = dishFlavorService.list(queryWrapper);

    dishDto.setFlavors(list);

    return R.success(dishDto);
  }
}
