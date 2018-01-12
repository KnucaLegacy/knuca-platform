package com.theopus.restservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurerAdapter {

    @Value("${api.swagger.path}")
    private String pathToSwagger;

    @Value("${api.swagger.basePackage}")
    private String pathToScanApi;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(pathToScanApi))
                .paths(PathSelectors.any())
                .build();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController(
                pathToSwagger + "/v2/api-docs",
                "/v2/api-docs");
        registry.addRedirectViewController(
                pathToSwagger + "/swagger-resources/configuration/ui",
                "/swagger-resources/configuration/ui");
        registry.addRedirectViewController(
                pathToSwagger + "/swagger-resources/configuration/security",
                "/swagger-resources/configuration/security");
        registry.addRedirectViewController(
                pathToSwagger + "/swagger-resources",
                "/swagger-resources");
        registry.addRedirectViewController(
                pathToSwagger,
                pathToSwagger + "/swagger-ui.html");
        registry.addRedirectViewController(
                pathToSwagger + "/",
                pathToSwagger + "/swagger-ui.html");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(pathToSwagger + "/**")
                .addResourceLocations("classpath:/META-INF/resources/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");
    }
}
