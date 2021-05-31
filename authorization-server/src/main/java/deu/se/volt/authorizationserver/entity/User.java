package deu.se.volt.authorizationserver.entity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class User {

    @Id
    private String username;

    @Column
    private String password;
    
    @Column()
    private String email;

    


}