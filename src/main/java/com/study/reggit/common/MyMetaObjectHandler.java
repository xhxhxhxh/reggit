package com.study.reggit.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
  @Override
  public void insertFill(MetaObject metaObject) {
    LocalDateTime nowTime = LocalDateTime.now();
    Long userId = BaseContext.getCurrentId();
    metaObject.setValue("createTime", nowTime);
    metaObject.setValue("updateTime", nowTime);
    metaObject.setValue("createUser", userId);
    metaObject.setValue("updateUser", userId);
  }

  @Override
  public void updateFill(MetaObject metaObject) {
    LocalDateTime nowTime = LocalDateTime.now();
    Long userId = BaseContext.getCurrentId();
    metaObject.setValue("updateTime", nowTime);
    metaObject.setValue("updateUser", userId);
  }
}
