package com.study.reggit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.study.reggit.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
