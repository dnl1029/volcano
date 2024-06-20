package hobby.volcano.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        converters.stream()
                .filter(converter -> converter instanceof AbstractHttpMessageConverter)
                .forEach(converter -> {
                    if (converter instanceof AbstractHttpMessageConverter) {
                        ((AbstractHttpMessageConverter<?>) converter).setDefaultCharset(StandardCharsets.UTF_8);
                    }
                });
    }
}
