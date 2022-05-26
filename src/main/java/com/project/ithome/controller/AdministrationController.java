package com.project.ithome.controller;

import com.project.ithome.authentication.annotation.UserLoginToken;
import com.project.ithome.authentication.service.TokenService;
import com.project.ithome.dto.RestError;
import com.project.ithome.dto.administration.*;
import com.project.ithome.exception.BaseException;
import com.project.ithome.service.resource.ResourceService;
import com.project.ithome.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/administration")
@CrossOrigin
public class AdministrationController {
    private final Logger logger = LoggerFactory.getLogger(AdministrationController.class);
    private final ResourceService resourceService;
    private final UserService userService;
    private final TokenService tokenService;

    public AdministrationController(ResourceService resourceService, UserService userService, TokenService tokenService) {
        this.resourceService = resourceService;
        this.userService = userService;
        this.tokenService = tokenService;
    }

    //获取待审核的资源页
    @UserLoginToken
    @GetMapping(value = "examine", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<PendingResPageResponseDTO> getPendingResPage(
            HttpServletRequest request,
            @RequestParam int pageNum,
            @RequestParam int pageSize
    ) throws BaseException{
        String token = request.getHeader("token");
        logger.info("getPendingResPage token: {}", token);
        String userId = tokenService.getUserIdFromToken(token);
        PendingResPageResponseDTO responseDTO = resourceService.getPendingResPage(pageNum, pageSize, userId);
        return ResponseEntity.ok(responseDTO);
    }

    //获取某个待审资源的信息
    @UserLoginToken
    @GetMapping(value = "examine/{resId}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<PendingResInfoDTO> getOnePendingResInfo(
            HttpServletRequest request,
            @PathVariable String resId
    ) throws BaseException{
        String token = request.getHeader("token");
        logger.info("getOnePendingResInfo token: {}", token);
        String userId = tokenService.getUserIdFromToken(token);
        PendingResInfoDTO pendingResInfoDTO = resourceService.getPendingResInfoById(resId, userId);
        return ResponseEntity.ok(pendingResInfoDTO);
    }

    //审核资源
    @UserLoginToken
    @PostMapping(value = "examine/{resId}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<ExamineResResponseDTO> examineRes(
            HttpServletRequest request,
            @PathVariable String resId,
            @RequestBody ExamineResRequestDTO requestDTO
    ) throws BaseException{
        String token = request.getHeader("token");
        logger.info("getOnePendingResInfo token: {}", token);
        String userId = tokenService.getUserIdFromToken(token);
        ExamineResResponseDTO responseDTO = resourceService.examineRes(requestDTO, resId, userId);
        return ResponseEntity.ok(responseDTO);
    }

    //获取管理员列表和用户列表
    @UserLoginToken
    @GetMapping(value = "admin", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<UserPageInfoResponseDTO> getUserPageInfo(
            HttpServletRequest request,
            @RequestParam int pageNum,
            @RequestParam int pageSize
    ) throws BaseException{
        String token = request.getHeader("token");
        logger.info("getUserPageInfo token: {}", token);
        String userId = tokenService.getUserIdFromToken(token);
        UserPageInfoResponseDTO responseDTO = userService.getUserPageInfo(pageNum, pageSize, userId);
        return ResponseEntity.ok(responseDTO);
    }

    //搜索用户
    @UserLoginToken
    @GetMapping(value = "admin/{content}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<UserSearchResponseDTO> searchUser(        //若content值为空，则当作/admin接口响应
            HttpServletRequest request,
            @PathVariable String content, @RequestParam int pageNum, @RequestParam int pageSize
    ) throws  BaseException {
        String token = request.getHeader("token");
        logger.info("searchUser token: {}", token);
        String userId = tokenService.getUserIdFromToken(token);
        UserSearchResponseDTO responseDTO = userService.searchUser(pageNum, pageSize, userId, content);
        return ResponseEntity.ok(responseDTO);
    }

    //设立/废除管理员
    @UserLoginToken
    @PostMapping(value = "admin", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<OperaAdminResponseDTO> operaAdmin(
            HttpServletRequest request,
            @RequestBody OperaAdminRequestDTO requestDTO
    ) throws BaseException {
        String token = request.getHeader("token");
        logger.info("operaAdmin token: {}", token);
        String masterId = tokenService.getUserIdFromToken(token);
        OperaAdminResponseDTO responseDTO = userService.operaAdmin(requestDTO, masterId);
        return ResponseEntity.ok(responseDTO);
    }

    //发布公告
    @UserLoginToken
    @PostMapping(value = "announce", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<AnnounceResponseDTO> pushAnnouncement(
            HttpServletRequest request,
            @RequestBody AnnounceRequestDTO requestDTO
    ) throws BaseException {
        String token = request.getHeader("token");
        logger.info("pushAnnouncement token: {}", token);
        String masterId = tokenService.getUserIdFromToken(token);
        AnnounceResponseDTO responseDTO = userService.announce(requestDTO, masterId);
        return ResponseEntity.ok(responseDTO);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<RestError> BaseExceptionHandler(BaseException e){
        return ResponseEntity.status(e.getStatus()).body(new RestError(e));
    }
}
