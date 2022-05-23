package com.project.ithome.controller;

import com.project.ithome.authentication.annotation.UserLoginToken;
import com.project.ithome.dto.RestError;
import com.project.ithome.dto.user.GetAnnounceRequestDTO;
import com.project.ithome.dto.user.GetAnnounceResponseDTO;
import com.project.ithome.exception.BaseException;
import com.project.ithome.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/social")
@CrossOrigin
public class SocialController {
    private final Logger logger = LoggerFactory.getLogger(SocialController.class);
    private final UserService userService;

    public SocialController(UserService userService) {
        this.userService = userService;
    }

    //获取历史公告
    @UserLoginToken
    @GetMapping(value = "/announce", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<GetAnnounceResponseDTO> getAnnounceList(@RequestBody GetAnnounceRequestDTO requestDTO) {
        GetAnnounceResponseDTO responseDTO = userService.getAnnounceList(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<RestError> BaseExceptionHandler(BaseException e){
        return ResponseEntity.status(e.getStatus()).body(new RestError(e));
    }
}
