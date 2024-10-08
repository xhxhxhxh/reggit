package com.study.reggit.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.study.reggit.common.R;
import com.study.reggit.entity.Category;
import com.study.reggit.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
@Api(tags="分类接口")
public class CategoryController {
  @Autowired
  private CategoryService categoryService;

  /**
   * 新增分类
   * @param category
   * @return
   */
  @PostMapping
  @ApiOperation("添加分类接口")
  public R<String> add(@RequestBody Category category) {
    categoryService.save(category);
    return R.success("新增分类成功");
  }

  /**
   * 分类列表分页查询
   * @param page
   * @param pageSize
   * @return
   */
  @GetMapping("/page")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "page",value = "页面", required = true),
      @ApiImplicitParam(name = "pageSize",value = "每页记录数", required = true),
  })
  public R<Page> categoryListPage(int page, int pageSize) {
    Page pageInfo = new Page(page, pageSize);

    LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();

    queryWrapper.orderByAsc(Category::getSort);

    categoryService.page(pageInfo, queryWrapper);

    return R.success(pageInfo);
  }

  /**
   * 删除分类
   * @param id
   * @return
   */
  @DeleteMapping
  public R<String> deleteById(Long id) {
    categoryService.removeById(id);
    return R.success("删除成功");
  }

  /**
   * 修改分类
   * @param category
   * @return
   */
  @PutMapping
  public R<String> updateById(@RequestBody Category category) {
    categoryService.updateById(category);
    return R.success("修改成功");
  }

  @GetMapping("/list")
  public R<List<Category>> list(Category category) {
    LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(category.getType() != null, Category::getType, category.getType());

    queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

    List<Category> list = categoryService.list(queryWrapper);
    return R.success(list);
  }
}
