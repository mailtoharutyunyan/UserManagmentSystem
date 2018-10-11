package am.nova.user.management.demo.payload;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class UserPayload {

    @NotNull
    @NotBlank
    private String userName;

    @NotNull
    @NotBlank
    private String password;

    @NotNull
    @NotBlank
    private String repeatPassword;
}
