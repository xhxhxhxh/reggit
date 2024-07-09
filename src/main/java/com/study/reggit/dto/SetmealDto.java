package com.study.reggit.dto;


import com.study.reggit.entity.Setmeal;
import com.study.reggit.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
