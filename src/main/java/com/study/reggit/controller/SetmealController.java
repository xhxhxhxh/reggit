package com.study.reggit.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.study.reggit.common.R;

import com.study.reggit.dto.SetmealDto;
import com.study.reggit.entity.Category;
import com.study.reggit.entity.Setmeal;
import com.study.reggit.service.CategoryService;
import com.study.reggit.service.SetmealService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
  @Autowired
  private SetmealService setmealService;

  @Autowired
  private CategoryService categoryService;

  @GetMapping("/page")
  public R<Page<SetmealDto>> page(int page, int pageSize, String name) {
    Page<Setmeal> pageInfo = new Page<>(page, pageSize);

    LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();

    queryWrapper.like(StringUtils.isNotEmpty(name), Setmeal::getName, name);

    queryWrapper.orderByDesc(Setmeal::getUpdateTime);

    setmealService.page(pageInfo, queryWrapper);

    Page<SetmealDto> setmealDtoPageInfo = new Page<>(page, pageSize);

    // 拷贝对象
    BeanUtils.copyProperties(pageInfo, setmealDtoPageInfo,"records");

    if (setmealDtoPageInfo.getRecords().isEmpty()) {
      return R.success(setmealDtoPageInfo);
    }

    List<Setmeal> records = pageInfo.getRecords();

    List<Long> ids = records.stream().map(Setmeal::getCategoryId).collect(Collectors.toList());

    List<Category> categories = categoryService.listByIds(ids);

    Map<Long, String> categoryNameMap = new HashMap<>();

    categories.forEach(item -> {
      categoryNameMap.put(item.getId(), item.getName());
    });

    List<SetmealDto> setmealDtoRecords = new ArrayList<>();
    records.forEach(item -> {
      SetmealDto setmealDto = new SetmealDto();
      BeanUtils.copyProperties(item, setmealDto);
      setmealDto.setCategoryName(categoryNameMap.get(item.getCategoryId()));
      setmealDtoRecords.add(setmealDto);
    });

    setmealDtoPageInfo.setRecords(setmealDtoRecords);

    return R.success(setmealDtoPageInfo);
  }

  @DeleteMapping
  public R<String> delete(@RequestParam List<Long> ids) {
    setmealService.removeByIds(ids);
    return R.success("删除成功");
  }
}
