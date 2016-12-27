package com.jordykuijpers.spotify;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.wrapper.spotify.Api;

@Component
@Scope("singleton")
public class SpotifyAPIConfigurer {
	private String clientId = "7da5c2e103f24d54ae168730e8905823";
	private String clientSecret = "b58659565d5d4c3f9097420bd1b3ea97";
	private Api api;
	
	public SpotifyAPIConfigurer() {
		api = Api.builder().clientId(clientId).clientSecret(clientSecret).build();
	}

	public String getClientId() {
		return clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}
	
	public Api getApi() {

		return api;
	}

}
