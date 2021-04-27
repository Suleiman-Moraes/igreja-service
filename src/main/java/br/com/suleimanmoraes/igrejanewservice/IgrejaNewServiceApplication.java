package br.com.suleimanmoraes.igrejanewservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

import br.com.suleimanmoraes.igrejanewservice.config.ApiProperty;

@SpringBootApplication
@EnableConfigurationProperties(ApiProperty.class)
public class IgrejaNewServiceApplication {

	private static ApplicationContext APPLICATION_CONTEXT;

	public static void main(String[] args) {
		APPLICATION_CONTEXT = SpringApplication.run(IgrejaNewServiceApplication.class, args);
	}

	public static <T> T getBean(Class<T> type) {
		return APPLICATION_CONTEXT.getBean(type);
	}
}
