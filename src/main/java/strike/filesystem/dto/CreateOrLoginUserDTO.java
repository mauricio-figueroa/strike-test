package strike.filesystem.dto;


import javax.validation.constraints.NotNull;

public class CreateOrLoginUserDTO {

    @NotNull
    private String username;

    @NotNull
    private String password;

    public CreateOrLoginUserDTO() {
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
