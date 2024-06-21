package hobby.volcano.config;

import hobby.volcano.common.CustomEnum;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${swagger.server.url}")
    private String serverUrl;

    @Bean
    public GroupedOpenApi customTestOpenAPIV1() {
        String[] paths = {"/api/**"};
        return GroupedOpenApi.builder()
                .group("api v1")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addServersItem(new Server().url(serverUrl))
                .info(new Info()
                        .title("volcano REST API")
                        .description("볼링 취미반 APP에서 사용하는 backend rest api 명세서입니다. login and get jwt token api를 통해 발급받은 토큰을 우측 상단 Authorize 버튼을 통해 입력 후 사용하세요.")
                        .version("v1.0.0"))
                .addSecurityItem(new SecurityRequirement().addList(CustomEnum.JWT_TOKEN.getContent()))
                .components(new Components()
                        .addSecuritySchemes(CustomEnum.JWT_TOKEN.getContent(),
                                new SecurityScheme()
                                        .name(CustomEnum.JWT_TOKEN.getContent())
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER))
                );
    }
}
