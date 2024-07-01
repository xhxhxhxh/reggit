package com.study.reggit.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.study.reggit.common.R;
import com.study.reggit.entity.Employee;
import com.study.reggit.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
  @Autowired
  private EmployeeService employeeService;

  /**
   * 登录
   * @param request
   * @param employee
   * @return
   */
  @PostMapping("/login")
  public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
    String password = employee.getPassword();
    String username = employee.getUsername();

    LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
    Employee currentEmployee = employeeService.getOne(queryWrapper.eq(Employee::getUsername, username));

    String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
    if (currentEmployee == null || !md5Password.equals(currentEmployee.getPassword())) {
      return R.error("用户名或密码错误");
    }

    if (currentEmployee.getStatus() == 0) {
      return R.error("该账户已被禁用");
    }

    request.getSession().setAttribute("Employee", currentEmployee.getId());

    return R.success(currentEmployee);
  }
}
