package don.geronimo.testejwt;

import don.geronimo.testejwt.filter.JWTFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class TestejwtConfiguration implements WebMvcConfigurer {
    @Bean
    public FilterRegistrationBean jwtFilter(){
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(getFilter());
        registrationBean.addUrlPatterns("/secure/*");
        return registrationBean;

    }
    @Bean
    public JWTFilter getFilter(){
        return new JWTFilter();
    }
}
