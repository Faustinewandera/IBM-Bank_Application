package Wandera.IBM_Bank.Application.Mappers;


import Wandera.IBM_Bank.Application.Dtos.UserDto.UserResponse;
import Wandera.IBM_Bank.Application.Entities.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserMapper {

    public static UserResponse toDto(User User) {
        UserResponse userResponse=new UserResponse();

        userResponse.setId(User.getId());
        userResponse.setFirstName(User.getFirstName());
        userResponse.setLastName(User.getLastName());
        userResponse.setEmail(User.getEmail());

        return userResponse;

    }

}
