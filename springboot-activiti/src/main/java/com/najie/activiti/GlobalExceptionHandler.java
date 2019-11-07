package com.najie.activiti;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.najie.activiti.exception.BizException;
/**
 * 
 * @author admin
 *
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
	@ExceptionHandler({BizException.class})
	public Map<String,Object> handlerException(Throwable t){
		logger.info("===进入到异常处理===");
		Map<String,Object> map = new HashMap<>();
		BizException e = (BizException)t;
		map.put("code", e.getCode());
		map.put("message", e.getMsg());
		return map;
	}
}
