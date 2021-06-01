package deu.se.volt.authorizationserver.controller;

import deu.se.volt.authorizationserver.entity.User;
import deu.se.volt.authorizationserver.service.UserDetailsServiceImpl;
import deu.se.volt.authorizationserver.util.DefaultResponse;
import deu.se.volt.authorizationserver.util.ResponseMessage;
import deu.se.volt.authorizationserver.util.StatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


@RestController
@Api("사용자 컨트롤러 V1")
public class AuthController {

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
    }
