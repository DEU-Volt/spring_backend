package deu.se.volt.authorizationserver.repository;

import deu.se.volt.authorizationserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthRepository extends JpaRepository<User, String> {

    User findByUsername(String username);
    User findByEmail(String email);


}
