package com.study.reggit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.reggit.dto.SetmealDto;
import com.study.reggit.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
  void save(SetmealDto setmealDto);
  void removeByIds(List<Long> ids);
}
