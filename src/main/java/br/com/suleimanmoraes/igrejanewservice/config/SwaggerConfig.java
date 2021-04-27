package br.com.suleimanmoraes.igrejanewservice.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SuppressWarnings("deprecation")
@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private ApiProperty apiProperty;

	@Bean
	public Docket greetingApi() {
		String swaggerToken = "";
		return new Docket(DocumentationType.SWAGGER_2).enable(apiProperty.getSwagger().isShow()).select()
				.apis(RequestHandlerSelectors.basePackage("br.com.suleimanmoraes.igrejanewservice.api.controller"))
				.paths(PathSelectors.any()).build().apiInfo(metaData())
				.globalOperationParameters(Collections.singletonList(new ParameterBuilder().name("Authorization")
						.modelRef(new ModelRef("string")).parameterType("header").required(false).hidden(true)
						.defaultValue(swaggerToken).build()));

	}

	private ApiInfo metaData() {
		return new ApiInfoBuilder().title("igreja-server Spring Boot REST API")
				.description("\"Web Service do igreja-server\"").version("1.0.0")
				.license("Apache License Version 2.0").licenseUrl("https://www.apache.org/licenses/LICENSE-2.0\"")
				.build();
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
}