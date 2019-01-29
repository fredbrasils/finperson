package br.com.finperson.security.domain;

import javax.validation.constraints.NotEmpty;

import com.sun.istack.NotNull;

import br.com.finperson.util.validation.annotation.PasswordMatches;
import br.com.finperson.util.validation.annotation.ValidEmail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@PasswordMatches
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

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
     
    @ValidEmail
    @NotNull
    @NotEmpty
    private String email;
}
