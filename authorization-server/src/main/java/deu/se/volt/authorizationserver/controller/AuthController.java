package deu.se.volt.authorizationserver.controller;

import deu.se.volt.authorizationserver.entity.User;
import deu.se.volt.authorizationserver.service.UserDetailsServiceImpl;
import deu.se.volt.authorizationserver.util.StatusCode;
import deu.se.volt.authorizationserver.util.DefaultResponse;
import deu.se.volt.authorizationserver.util.ResponseMessage;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {
    private static final String ID_REGEX = "^[a-zA-Z]{1}[a-zA-Z0-9_]{4,11}$";
    private static final String EMAIL_REGEX = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";

    @Autowired
    private UserDetailsServiceImpl userService;

    @PostMapping("/user")
    public User create(@RequestBody User user) {
        return userService.save(user);
    }

    @GetMapping("/user/email")
    @ApiOperation(value = "아이디 찾기", tags = "회원관리",
            httpMethod = "GET",
            notes = "아이디 찾기에 사용되는 API입니다."
    )
    @ApiImplicitParam(name = "Authorization", value = "Basic Auth", required = true, paramType = "header", defaultValue = "Basic c3Bhcmt6eGw6MTIzNDU2")
    public ResponseEntity getUserId(@RequestParam String email){
        User user = userService.loadUserByEmail(email);
        if(user != null) {
            Map<String, String> map = new HashMap<>();
            map.put("result", user.getUsername());
            return new ResponseEntity(DefaultResponse.res(
                    StatusCode.OK,
                    ResponseMessage.READ_USER,
                    map), HttpStatus.OK);
        } else{
            Map<String, String> map = new HashMap<>();
            map.put("result", "none");
            return new ResponseEntity(DefaultResponse.res(
                    StatusCode.NOT_FOUND,
                    ResponseMessage.NOT_FOUND_USER,
                    map), HttpStatus.OK);
        }
    }

    @RequestMapping("/auth/user")
    public String user() {
        return "test success";
    }

    @GetMapping("/user/id")
    @ApiOperation(value = "내 정보 조회", tags = "회원관리",
            httpMethod = "GET",
            notes = "내 정보 조회에 사용되는 API입니다."
    )
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity getUserInfo(@RequestParam String username){
        User user = userService.loadUserByUsername(username);
        if (user != null) {
            Map<String, String> map = new HashMap<>();
            map.put("id", user.getUsername());
            map.put("email", user.getEmail());
            return new ResponseEntity(DefaultResponse.res(
                    StatusCode.OK,
                    ResponseMessage.READ_USER,
                    map), HttpStatus.OK);
        }
        else {
            Map<String, String> map = new HashMap<>();
            map.put("result", "none");
            return new ResponseEntity(DefaultResponse.res(
                    StatusCode.NOT_FOUND,
                    ResponseMessage.NOT_FOUND_USER, map),
                    HttpStatus.OK);
        }
    }
}
