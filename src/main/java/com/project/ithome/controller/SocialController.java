package com.project.ithome.controller;

import com.project.ithome.authentication.annotation.UserLoginToken;
import com.project.ithome.authentication.service.TokenService;
import com.project.ithome.dto.RestError;
import com.project.ithome.dto.social.AnnounceListRequestDTO;
import com.project.ithome.dto.social.AnnounceListResponseDTO;
import com.project.ithome.dto.social.SelfRankDTO;
import com.project.ithome.dto.social.ScoreboardDTO;
import com.project.ithome.exception.BaseException;
import com.project.ithome.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/social")
@CrossOrigin
public class SocialController {
    private final Logger logger = LoggerFactory.getLogger(SocialController.class);
    private final UserService userService;
    private final TokenService tokenService;

    public SocialController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    //获取历史公告
    @UserLoginToken
    @GetMapping(value = "/announce", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<AnnounceListResponseDTO> getAnnounceList(@RequestBody AnnounceListRequestDTO requestDTO) {
        AnnounceListResponseDTO responseDTO = userService.getAnnounceList(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    //激励榜
    @UserLoginToken
    @GetMapping(value = "/rank", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<ScoreboardDTO> getScoreboard() {
        ScoreboardDTO responseDTO = userService.getScoreboard();
        return ResponseEntity.ok(responseDTO);
    }

    //获取个人积分排名
    @UserLoginToken
    @GetMapping(value = "/rank/self", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<SelfRankDTO> getSelfRank(HttpServletRequest request) throws BaseException{
        String token = request.getHeader("token");
        logger.info("getSelfRank token: {}", token);
        String userId = tokenService.getUserIdFromToken(token);
        SelfRankDTO responseDTO = userService.getSelfRank(userId);
        return ResponseEntity.ok(responseDTO);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<RestError> BaseExceptionHandler(BaseException e){
        return ResponseEntity.status(e.getStatus()).body(new RestError(e));
    }
}
