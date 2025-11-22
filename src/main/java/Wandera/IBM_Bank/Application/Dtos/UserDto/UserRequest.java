package Wandera.IBM_Bank.Application.Dtos.UserDto;

import Wandera.IBM_Bank.Application.Entities.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String idNumber;
    private String password;
    private Role role;
}
