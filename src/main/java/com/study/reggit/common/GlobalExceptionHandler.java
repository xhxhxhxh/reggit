package com.study.reggit.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
  public R<String> exceptionHandler(Exception e) {
    log.error(e.getMessage());
    return R.error("数据库操作失败");
  }

  @ExceptionHandler(CustomException.class)
  public R<String> customExceptionHandler(CustomException e) {
    return R.error(e.getMessage());
  }
}
