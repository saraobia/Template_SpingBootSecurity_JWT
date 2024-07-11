package com.project.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "application.security")
public class JwtConfig {
	
	private String jwtSecretKey;
	private Integer jwtExpiredAccessToken;
	private Integer jwtExpiredRefreshToken;
	
	
	public String getJwtSecretKey() {
		return jwtSecretKey;
	}
	public void setJwtSecretKey(String jwtSecretKey) {
		this.jwtSecretKey = jwtSecretKey;
	}
	public Integer getJwtExpiredAccessToken() {
		return jwtExpiredAccessToken;
	}
	public void setJwtExpiredAccessToken(Integer jwtExpiredAccessToken) {
		this.jwtExpiredAccessToken = jwtExpiredAccessToken;
	}
	public Integer getJwtExpiredRefreshToken() {
		return jwtExpiredRefreshToken;
	}
	public void setJwtExpiredRefreshToken(Integer jwtExpiredRefreshToken) {
		this.jwtExpiredRefreshToken = jwtExpiredRefreshToken;
	}
	
	
	
	
}
