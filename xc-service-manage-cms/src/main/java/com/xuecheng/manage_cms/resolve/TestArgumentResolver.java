package com.xuecheng.manage_cms.resolve;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * project name : xuecheng
 * Date:2018/12/18
 * Author: yc.guo
 * DESC:
 */
@Slf4j
@Configuration
public class TestArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(Params.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, 
                                  ModelAndViewContainer modelAndViewContainer, 
                                  NativeWebRequest nativeWebRequest, 
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {
        
        System.out.println("TestArgumentResolver:resolveArgument");
        Params params = methodParameter.getParameterAnnotation(Params.class);
        String paramName = params.name();
        if (paramName == null) {
            paramName = methodParameter.getParameterName();
        }
        //简单的案例：如果客户端未传值，就设置默认值
        Object res = nativeWebRequest.getNativeRequest(HttpServletRequest.class).getParameter(paramName);
        if(res == null && params.required()){
            log.warn("resolveArgument error username or role is empty");
            return null;
        }else{
            return res == null ? params.defaultValue() : res;
        }
    }
}
