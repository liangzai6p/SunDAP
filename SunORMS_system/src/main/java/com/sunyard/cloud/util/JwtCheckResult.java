package com.sunyard.cloud.util;

import io.jsonwebtoken.Claims;

public class JwtCheckResult{
	private Boolean retBoolean;
	private Claims claims;
	private String message;
	
	public JwtCheckResult(Boolean retBoolean,Claims claims, String message) {
		this.retBoolean = retBoolean;
		this.claims = claims;
		this.message = message;
	}
	
	public Boolean getRetBoolean() {
		return retBoolean;
	}
	
	public Claims getClaims() {
		return claims;
	}
	
	public String getMessage() {
		return message;
	}
}
