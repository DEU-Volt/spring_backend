package deu.se.volt.authorizationserver.controller;

import deu.se.volt.authorizationserver.entity.User;
import deu.se.volt.authorizationserver.service.UserDetailsServiceImpl;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
public class AuthController {

    @Autowired
    private UserDetailsServiceImpl userService;

    @PostMapping("/user")
    public User create(@RequestBody User user) {
        return userService.save(user);
    }

    @RequestMapping("/auth/user")
    public String user() {
        return "test success";
    }

//    @PostMapping(value = "/auth/user")     // /auth/user 로 매핑
//    public Map<String, Object> user(OAuth2Authentication user) {
//        System.out.println(user.getUserAuthentication());
//        System.out.println(user.getAuthorities());
//        System.out.println(user.getPrincipal());
//
//        Map<String, Object> userInfo = new HashMap<>();
//        userInfo.put("user", user.getUserAuthentication().getPrincipal());
//        userInfo.put("authorities", AuthorityUtils.authorityListToSet(user.getUserAuthentication().getAuthorities()));
//        return userInfo;
//    }



}
