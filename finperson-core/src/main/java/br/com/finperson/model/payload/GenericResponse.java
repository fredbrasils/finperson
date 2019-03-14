package br.com.finperson.model.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class GenericResponse {

	private Object object;
	private Boolean success;
    private String[] message;
}
