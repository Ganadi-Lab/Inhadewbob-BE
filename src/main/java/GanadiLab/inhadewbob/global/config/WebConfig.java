package GanadiLab.inhadewbob.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private String filePath = System.getProperty("user.dir") + "/uploads/";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "https://inha-dewbob.p-e.kr/")
                .allowedMethods("*")
                .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // /uploads/** -> 실제 폴더로 매핑
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + filePath);
    }
}
