package com.project.ithome.controller;

import com.project.ithome.authentication.annotation.UserLoginToken;
import com.project.ithome.authentication.service.TokenService;
import com.project.ithome.dto.RestError;
import com.project.ithome.dto.social.*;
import com.project.ithome.exception.BaseException;
import com.project.ithome.service.social.FriendService;
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
    private final FriendService friendService;

    public SocialController(UserService userService, TokenService tokenService, FriendService friendService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.friendService = friendService;
    }

    //获取历史公告
    @UserLoginToken
    @GetMapping(value = "/announce", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<AnnounceListResponseDTO> getAnnounceList(@RequestParam int pageNum, @RequestParam int pageSize) {
        AnnounceListResponseDTO responseDTO = userService.getAnnounceList(pageNum, pageSize);
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
    public ResponseEntity<SelfRankDTO> getSelfRank(HttpServletRequest request) throws BaseException {
        String token = request.getHeader("token");
        logger.info("getSelfRank token: {}", token);
        String userId = tokenService.getUserIdFromToken(token);
        SelfRankDTO responseDTO = userService.getSelfRank(userId);
        return ResponseEntity.ok(responseDTO);
    }

    //找朋友：发布个人简介
    @UserLoginToken
    @PostMapping(value = "/friend", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<FriendInfoFillResponseDTO> fillFriendInfo(
            HttpServletRequest request,
            @RequestBody FriendInfoFillRequestDTO requestDTO
    ) throws BaseException {
        String token = request.getHeader("token");
        logger.info("fillFriendInfo token: {}", token);
        String userId = tokenService.getUserIdFromToken(token);
        FriendInfoFillResponseDTO responseDTO = friendService.fillFriendInfo(requestDTO, userId);
        return ResponseEntity.ok(responseDTO);
    }

    //找朋友：获取战友信息列表
    @UserLoginToken
    @GetMapping(value = "/friend", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<FriendListResponseDTO> getFriendList(@RequestParam int pageNum, @RequestParam int pageSize) {
        FriendListResponseDTO responseDTO = friendService.getFriendList(pageNum, pageSize);
        return ResponseEntity.ok(responseDTO);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<RestError> BaseExceptionHandler(BaseException e){
        return ResponseEntity.status(e.getStatus()).body(new RestError(e));
    }
}
