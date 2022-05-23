package com.project.ithome.controller;

import com.project.ithome.authentication.annotation.PassToken;
import com.project.ithome.authentication.annotation.UserLoginToken;
import com.project.ithome.authentication.exception.UserNotFoundException;
import com.project.ithome.dto.RestError;
import com.project.ithome.dto.user.*;
import com.project.ithome.exception.BaseException;
import com.project.ithome.exception.user.NoNewEditInfoException;
import com.project.ithome.exception.user.RegisterException;
import com.project.ithome.exception.user.WrongPasswordException;
import com.project.ithome.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    //用户注册
    @PassToken
    @PostMapping(value = "/register", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<UserRegisterResponseDTO> userRegister(@RequestBody UserRegisterRequestDTO request) throws RegisterException {
        logger.info("userName:{} and password:{}",request.getUserName(), request.getPassword());
        UserRegisterResponseDTO response = userService.userRegister(request);
        logger.info("userName:{} register successfully and userId is {}", request.getUserName(), response.getUserId());
        return ResponseEntity.ok(response);
    }

    //用户登录
    @PassToken
    @PostMapping(value = "/login", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<UserLoginResponseDTO> userLogin(@RequestBody UserLoginRequestDTO request) throws WrongPasswordException, UserNotFoundException {
        logger.info("userId:{} request userLogin. password:{}", request.getUserId(), request.getPassword());
        UserLoginResponseDTO response = userService.userLogin(request);
        logger.info("userId:{} login successfully. Pass token:{}", response.getUserId(), response.getToken());
        return ResponseEntity.ok(response);
    }

    //获取用户信息
    @UserLoginToken
    @GetMapping(value = "/info", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<UserInfoGetResponseDTO> getUserInfo(@RequestBody  UserInfoGetRequestDTO request) {
        UserInfoGetResponseDTO response = userService.getUserInfo(request);
        logger.info("Get {} info: {}", request.getUserId(), response.getUser());
        return ResponseEntity.ok(response);
    }

    //编辑用户信息
    @UserLoginToken
    @PostMapping(value = "/info", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<UserInfoEditResponseDTO> editUserInfo(@RequestBody UserInfoEditRequestDTO request) throws NoNewEditInfoException {
        UserInfoEditResponseDTO response = userService.editUserInfo(request);
        logger.info("Edit info:{}", response.getUser());
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<RestError> BaseExceptionHandler(BaseException e){
        return ResponseEntity.status(e.getStatus()).body(new RestError(e));
    }
}
