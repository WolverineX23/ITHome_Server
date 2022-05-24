package com.project.ithome.controller;

import com.project.ithome.authentication.annotation.UserLoginToken;
import com.project.ithome.authentication.service.TokenService;
import com.project.ithome.dto.RestError;
import com.project.ithome.dto.resource.*;
import com.project.ithome.exception.BaseException;
import com.project.ithome.exception.resource.ResourceNotFoundException;
import com.project.ithome.service.resource.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/resource")
@CrossOrigin
public class ResourceController {

    private final Logger logger = LoggerFactory.getLogger(ResourceController.class);
    private final ResourceService resourceService;
    private final TokenService tokenService;

    public ResourceController(ResourceService resourceService, TokenService tokenService) {
        this.resourceService = resourceService;
        this.tokenService = tokenService;
    }

    //推荐资源
    @UserLoginToken
    @PostMapping(value = "/recommend", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<ResRecommendResponseDTO> resourceRecommend(HttpServletRequest request, @RequestBody ResRecommendRequestDTO requestDTO) throws BaseException{
        String token = request.getHeader("token");
        logger.info("resourceRecommend token: {}", token);
        String userId = tokenService.getUserIdFromToken(token);
        ResRecommendResponseDTO response = resourceService.recommendResource(requestDTO, userId);
        return ResponseEntity.ok(response);
    }

    //通过tag集合筛选资源
    @UserLoginToken
    @GetMapping(value = "",produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<ResGetByTechTagResponseDTO> getPassedResByTagArray(@RequestBody ResGetByTechTagRequestDTO requestDTO) {
        logger.info("Request data: {}", requestDTO);
        ResGetByTechTagResponseDTO responseDTO = resourceService.getPassedResByTagArray(requestDTO);
        logger.info("Response data: {}", responseDTO);
        return ResponseEntity.ok(responseDTO);
    }

    //通过资源ID获取该资源的数据(浏览量+1)  还需携带资源评价信息
    @UserLoginToken
    @PostMapping(value = "/{resId}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<PassedResInfoResponseDTO> getOnePassedResInfo(@RequestBody PassedResInfoRequestDTO requestDTO, @PathVariable String resId) throws ResourceNotFoundException {
        logger.info("Get res info of Id:{}", resId);
        PassedResInfoResponseDTO resInfoDTO = resourceService.getPassedResInfoById(requestDTO, resId);
        logger.info("getResInfoBtId:{}",resInfoDTO);
        return ResponseEntity.ok(resInfoDTO);
    }

    //资源搜索
    @UserLoginToken
    @GetMapping(value = "/{content}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<ResSearchResponseDTO> searchRes(@RequestBody ResSearchRequestDTO requestDTO, @PathVariable String content) {
        logger.info("Search Content:{}", content);
        ResSearchResponseDTO responseDTO = resourceService.searchRes(requestDTO, content);
        return ResponseEntity.ok(responseDTO);
    }

    //评价资源
    @UserLoginToken
    @PostMapping(value = "evaluate/{resId}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<EvaInfoResponseDTO> evaluateRes(
            HttpServletRequest request,
            @RequestBody EvaInfoRequestDTO requestDTO,
            @PathVariable String resId
    ) throws BaseException {
        String token = request.getHeader("token");
        logger.info("evaluateRes token: {}", token);
        String userId = tokenService.getUserIdFromToken(token);
        EvaInfoResponseDTO responseDTO = resourceService.evaluateRes(requestDTO, resId, userId);
        return ResponseEntity.ok(responseDTO);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<RestError> BaseExceptionHandler(BaseException e){
        return ResponseEntity.status(e.getStatus()).body(new RestError(e));
    }
}
