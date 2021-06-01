package deu.se.volt.authorizationserver.service;

import com.mysql.cj.log.Log;
import deu.se.volt.authorizationserver.entity.User;
import deu.se.volt.authorizationserver.repository.UserAuthRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
;
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

    public User save (User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userAuthRepository.save(user);
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userAuthRepository.findByUsername(username);
//        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthorities());
        return user;
    }

    public User loadUserByEmail(String email) throws UsernameNotFoundException {
        User user = userAuthRepository.findByEmail(email);
        return user;
    }

    private Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
