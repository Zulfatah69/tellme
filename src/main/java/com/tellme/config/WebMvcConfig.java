package com.tellme.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC configuration for CORS, static resource serving, and
 * request interceptors.
 *
 * <p>Allowed CORS origins are controlled by the {@code tellme.cors.allowed-origins}
 * property (defaults to {@code http://localhost:8081}). Configure this to your
 * deployment domain in production.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;
    private final String uploadDir;
    private final String allowedOrigins;

    public WebMvcConfig(AuthInterceptor authInterceptor,
                        @Value("${tellme.upload.dir:uploads}") String uploadDir,
                        @Value("${tellme.cors.allowed-origins:http://localhost:8081}") String allowedOrigins) {
        this.authInterceptor = authInterceptor;
        this.uploadDir = uploadDir;
        this.allowedOrigins = allowedOrigins;
    }

    /**
     * Configures CORS. For development, {@code allowedOriginPatterns("*")} is
     * acceptable; for production set {@code tellme.cors.allowed-origins} to
     * your specific domain.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false);
    }

    /**
     * Serves uploaded files as static resources from the upload directory.
     * The directory path is configurable via {@code tellme.upload.dir}.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir + "/");
    }

    /**
     * Registers the token-based authentication interceptor for all
     * {@code /api/**} routes.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/**");
    }
}
