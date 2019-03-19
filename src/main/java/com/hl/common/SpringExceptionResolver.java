package com.hl.common;

import com.hl.constant.Constant;
import com.hl.exception.ParamException;
import com.hl.exception.PermissionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class SpringExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String url = request.getRequestURL().toString();
        ModelAndView mv;
        String defaultMsg = "System error";

        // 这里我们要求项目中所有请求json数据，都使用.json结尾
        if (url.endsWith(".json")) {
            if (ex instanceof PermissionException || ex instanceof ParamException) {
                JsonData result = JsonData.fail(ex.getMessage());
                mv = new ModelAndView(Constant.JSON_VIEW, result.toMap());
            } else {
                log.error("unknown json exception, url:" + url, ex);
                JsonData result = JsonData.fail(defaultMsg);
                mv = new ModelAndView(Constant.JSON_VIEW, result.toMap());
            }
        } else if (url.endsWith(".page")) { // 这里我们要求项目中所有请求page页面，都使用.page结尾
            log.error("unknown page exception, url:" + url, ex);
            JsonData result = JsonData.fail(defaultMsg);
            mv = new ModelAndView(Constant.EXCEPTION, result.toMap());
        } else {
            log.error("unknow exception, url:" + url, ex);
            JsonData result = JsonData.fail(defaultMsg);
            mv = new ModelAndView(Constant.JSON_VIEW, result.toMap());
        }
        return mv;
    }
}
