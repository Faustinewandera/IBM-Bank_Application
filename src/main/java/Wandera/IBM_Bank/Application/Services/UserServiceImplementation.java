package Wandera.IBM_Bank.Application.Services;

import Wandera.IBM_Bank.Application.Dtos.UserDto.UserRequest;
import Wandera.IBM_Bank.Application.Dtos.UserDto.UserResponse;
import Wandera.IBM_Bank.Application.Entities.Role;
import Wandera.IBM_Bank.Application.Entities.User;
import Wandera.IBM_Bank.Application.Mappers.UserMapper;
import Wandera.IBM_Bank.Application.Repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final EmailService emailService;

    List saveUser= new LinkedList<>();


    public UserResponse createUser(UserRequest userRequest) {

        User newUser = convertToEntity(userRequest);

        // Check if  user with the provided email already exists if not save the user
        if (!userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            saveUser.add(userRepository.save(newUser));

            // Send welcome email
            // Send welcome email
            try {
                emailService.sendWelcomeEmail(userRequest.getEmail(), userRequest.getLastName());
            } catch (MessagingException e) {
                e.printStackTrace();
            }

           return UserMapper.toDto(newUser);

        }
        throw new RuntimeException("User with this email already exists.");
    }

    //converting to entity
    private User convertToEntity(UserRequest userRequest) {

        Role assignedRole;

        // If role is not provided → automatically assign it to USER
        if (userRequest.getRole() == null) {
            assignedRole = Role.USER;
        }
        // If role is provided → accept it (mainly used for admin)
        else {
            assignedRole = userRequest.getRole();
        }
        return User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .email(userRequest.getEmail())
                .idNumber(userRequest.getIdNumber())
                .role(assignedRole)
                .build();

    }


    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDto)
                .toList();
    }

    public UserResponse getUserByIdNumber(String idNumber) {
         User user  = userRepository.findByIdNumber(idNumber)
                 .orElseThrow(()->new RuntimeException("User with this id number" + idNumber+" does not exist"));

        return UserMapper.toDto(user);
    }

    public UserResponse updateUser(String idNumber, UserRequest userRequest) {

        User user= userRepository.findByIdNumber(idNumber)
                .orElseThrow(()-> new RuntimeException("User with this id number does not exist "+userRequest.getEmail()));

        if (Objects.nonNull(user.getFirstName()) && !"".equalsIgnoreCase(user.getFirstName())) {
            user.setFirstName(userRequest.getFirstName());
        }

        if (Objects.nonNull(user.getLastName()) && !"".equalsIgnoreCase(user.getLastName())) {

            user.setLastName(userRequest.getLastName());
        }

        if (Objects.nonNull(user.getPassword()) && !"".equalsIgnoreCase(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }
        saveUser.add(userRepository.save(user));
        return UserMapper.toDto(user);
    }

    public String deleteUser(Long id) {
        User user=userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("User with this id "+ id +" does not exist"));
        saveUser.remove(user);
        return "User with this id "+ id +" has been deleted successfully";
    }

}
