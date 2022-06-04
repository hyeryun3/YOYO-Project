package yy.project.YOYO;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import yy.project.YOYO.argumentresolver.LoginMemberArgumentResolver;
import yy.project.YOYO.interceptor.AdminInterceptor;
import yy.project.YOYO.interceptor.LoginCheckIntercept;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {




    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        //만든 애노테이션 추가. @Login
        resolvers.add(new LoginMemberArgumentResolver());
    }
}
