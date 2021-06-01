package deu.se.volt.authorizationserver.controller;

import deu.se.volt.authorizationserver.entity.User;
import deu.se.volt.authorizationserver.service.UserDetailsServiceImpl;
import deu.se.volt.authorizationserver.util.StatusCode;
import deu.se.volt.authorizationserver.util.DefaultResponse;
import deu.se.volt.authorizationserver.util.ResponseMessage;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;


@RestController
@Api("사용자 컨트롤러 V1")
public class AuthController {
    private static final String ID_REGEX = "^[a-zA-Z]{1}[a-zA-Z0-9_]{4,11}$";
    private static final String EMAIL_REGEX = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";

    @Autowired
    private UserDetailsServiceImpl userService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    /*
        회원가입 API POST USER
     */
    @ApiOperation(value = "회원가입", tags = "회원관리",
            httpMethod = "POST",
            notes = "회원가입에 사용되는 API 입니다."
    )
    @ApiImplicitParam(name = "Authorization", value = "Basic Auth", required = true, paramType = "header", defaultValue = "Basic c3Bhcmt6eGw6MTIzNDU2")
//    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @PostMapping("/user")
    public ResponseEntity create(@RequestBody @Valid User user) {
        Map<String, String> map = new HashMap<>();
        // User 객체 Null 검증, 입력 값 검증
        // -> @Validation 전역 Controller 처리
//        if(!Pattern.matches(EMAIL_REGEX,user.getEmail())
//                || !Pattern.matches(ID_REGEX,user.getUsername())
//                || user.getPassword().isBlank()) {
//            map.put("result","false");
//            return new ResponseEntity(DefaultResponse.res(
//                    StatusCode.BAD_REQUEST,
//                    ResponseMessage.INPUT_ERROR,map), HttpStatus.BAD_REQUEST);
//
//        }

        // 사용자 저장 로직
        if(Boolean.TRUE.equals(userService.save(user))) {
            map.put("result","true");
            return new ResponseEntity(DefaultResponse.res(
                    StatusCode.OK,
                    ResponseMessage.CREATED_USER,
                    map), HttpStatus.OK);
        }

        // 사용자 저장이 실패했을 경우 로직
        map.put("result","false");
        return new ResponseEntity (DefaultResponse.res(
                StatusCode.NO_CONTENT,
                ResponseMessage.EXIST_USER,
                map), HttpStatus.OK);
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


