package br.com.suleimanmoraes.igrejanewservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties("igreja")
public class ApiProperty {
	private String originPermitida = "http://localhost:4200";

	private final Seguranca seguranca = new Seguranca();

	private final Mail mail = new Mail();

	private final Swagger swagger = new Swagger();

	private final Jwt jwt = new Jwt();

	public Mail getMail() {
		return mail;
	}

	public Seguranca getSeguranca() {
		return seguranca;
	}

	public Swagger getSwagger() {
		return swagger;
	}

	public Jwt getJwt() {
		return jwt;
	}

	public String getOriginPermitida() {
		return originPermitida;
	}

	public void setOriginPermitida(String originPermitida) {
		this.originPermitida = originPermitida;
	}

	@Getter
	@Setter
	public static class Seguranca {

		private boolean enableHttps;
		
		private String host;
	}

	public static class Mail {

		private String host;

		private Integer port;

		private String username;

		private String password;

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public Integer getPort() {
			return port;
		}

		public void setPort(Integer port) {
			this.port = port;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	}

	public static class Swagger {
		private boolean show;

		public boolean isShow() {
			return show;
		}

		public void setShow(boolean show) {
			this.show = show;
		}
	}

	public static class Jwt {
		private String key;

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}
	}
}
