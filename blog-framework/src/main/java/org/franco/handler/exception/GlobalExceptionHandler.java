package org.franco.handler.exception;


import lombok.extern.slf4j.Slf4j;
import org.franco.domain.ResponseResult;
import org.franco.enums.AppHttpCodeEnum;
import org.franco.exception.SystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handler for SystemException
     * @param e
     * @param response
     * @return
     */
    @ExceptionHandler(SystemException.class)
    public ResponseResult systemExceptionHandler(SystemException e, HttpServletResponse response){
        log.error("error", e);
        response.setStatus(e.getCode());
        return ResponseResult.errorResult(e.getCode(), e.getMsg());
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseResult exceptionHandler(Exception e){
//        log.error("error", e);
//        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, e.getMessage());
//    }
}
