package Wandera.IBM_Bank.Application.Repository;

import Wandera.IBM_Bank.Application.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
   Optional<User> findByIdNumber(String idNumber);

}
