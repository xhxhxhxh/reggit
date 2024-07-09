package com.study.reggit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.reggit.common.CustomException;
import com.study.reggit.dto.SetmealDto;
import com.study.reggit.entity.Setmeal;
import com.study.reggit.entity.SetmealDish;
import com.study.reggit.mapper.SetmealMapper;
import com.study.reggit.service.SetmealDishService;
import com.study.reggit.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
  @Autowired
  private SetmealDishService setmealDishService;
  public void removeByIds(List<Long> ids) {
    LambdaQueryWrapper<Setmeal> setmealWrapper = new LambdaQueryWrapper<>();
    setmealWrapper.in(Setmeal::getId, ids);
    setmealWrapper.eq(Setmeal::getStatus, 1);

    int count = this.count(setmealWrapper);
    if (count > 0) {
      throw new CustomException("存在未停售套餐，删除失败");
    }

    LambdaQueryWrapper<SetmealDish> setmealDishWrapper = new LambdaQueryWrapper<>();
    setmealDishWrapper.in(SetmealDish::getSetmealId, ids);
    setmealDishService.remove(setmealDishWrapper);

    super.removeByIds(ids);
  }


  public void save(SetmealDto setmealDto) {
    super.save(setmealDto);
    List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
    setmealDishes.forEach(item -> {
      item.setSetmealId(setmealDto.getId());
    });

    setmealDishService.saveBatch(setmealDishes);
  }
}
