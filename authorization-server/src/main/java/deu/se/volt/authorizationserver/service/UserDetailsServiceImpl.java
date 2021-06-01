package deu.se.volt.authorizationserver.service;

import deu.se.volt.authorizationserver.entity.User;
import deu.se.volt.authorizationserver.repository.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Autowired private PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userAuthRepository.findAll();
    }

    /*
        User Entity Save Function
     */
    public Boolean save (User user) {
        // ID 중복검사

        if (loadUserByUsername(user.getUsername()) == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userAuthRepository.save(user);
            return true;
        }
        return false;
    }

    @Override public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userAuthRepository.findByUsername(username);
    }

    private Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
