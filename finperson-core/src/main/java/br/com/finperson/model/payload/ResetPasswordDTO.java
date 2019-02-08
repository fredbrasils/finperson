package br.com.finperson.model.payload;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.com.finperson.util.validation.annotation.ValidEmail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordDTO {

    @ValidEmail(acceptEmptyString = true)
    @NotNull
    @NotEmpty
    private String email;
}
