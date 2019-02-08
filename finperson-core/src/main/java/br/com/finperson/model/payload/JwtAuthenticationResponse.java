package br.com.finperson.model.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class JwtAuthenticationResponse {

	private String accessToken;
    private String tokenType = "Bearer";

	public JwtAuthenticationResponse(String accessToken) {
		super();
		this.accessToken = accessToken;
	}
	
}
