package br.com.finperson.model.payload;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.com.finperson.util.validation.annotation.PasswordMatches;
import br.com.finperson.util.validation.annotation.ValidEmail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@PasswordMatches
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

	private Long id;
	
	@NotNull
    @NotEmpty
    private String firstName;
     
    @NotNull
    @NotEmpty
    private String lastName;
     
    @NotNull
    @NotEmpty
    private String password;
    private String matchingPassword;
     
    @ValidEmail(acceptEmptyString = true)
    @NotNull
    @NotEmpty
    private String email;
    
    private String token;
}
