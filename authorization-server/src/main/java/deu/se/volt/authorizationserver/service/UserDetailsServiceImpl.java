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

    public User save (User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userAuthRepository.save(user);
    }

    @Override public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userAuthRepository.findByUsername(username);
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthorities());
    }

    private Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
