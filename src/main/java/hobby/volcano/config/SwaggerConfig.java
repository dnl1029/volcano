package hobby.volcano.config;

import hobby.volcano.common.CustomEnum;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi customTestOpenAPIV1() {
        String [] paths = {"/api/**"};
        return GroupedOpenApi.builder()
                .group("api v1")
                .pathsToMatch(paths)
                .addOpenApiCustomizer(openApiCustomizer())
                .build();
    }

    public OpenApiCustomizer openApiCustomizer() {
        return openApi -> openApi.addSecurityItem(new SecurityRequirement().addList(CustomEnum.JWT_TOKEN.getContent()))
                .getComponents().addSecuritySchemes(CustomEnum.JWT_TOKEN.getContent(),new SecurityScheme().name(CustomEnum.JWT_TOKEN.getContent())
                        .type(SecurityScheme.Type.APIKEY) // custom key방식
                        .in(SecurityScheme.In.HEADER));
    }

}
