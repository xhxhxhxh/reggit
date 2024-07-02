package com.study.reggit.filter;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;
import com.study.reggit.common.R;

@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
  private final AntPathMatcher matcher = new AntPathMatcher();
  private final String[] urls = {"/employee/login", "/employee/logout", "/backend/**", "/front/**"};

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;

    String url = request.getRequestURI();

    boolean matched = matchURL(urls, url);
    if (matched) {
      log.info("该请求不需要处理, {}", url);
      filterChain.doFilter(request, response);
      return;
    }

    Long userId = (Long) request.getSession().getAttribute("employee");
    if (userId != null) {
      log.info("用户已登录, 用户id: {}", userId);
      filterChain.doFilter(request, response);
      return;
    }
    log.info("用户未登录, {}", url);
    response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));

  }

  public boolean matchURL(String[] patterns, String url) {
    for (String pattern : patterns) {
      if (matcher.match(pattern, url)) {
        return true;
      }
    }
    return false;
  }

}
