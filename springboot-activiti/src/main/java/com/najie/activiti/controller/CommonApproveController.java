package com.najie.activiti.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.najie.activiti.exception.BizException;
import com.najie.activiti.service.ActivitiService;
import com.najie.activiti.util.ResStatus;
import com.najie.activiti.util.ResponseResult;
/**
 * @desc 查询审批待办信息
 * @author admin
 *
 */
@RestController
@RequestMapping("/web/approve")
public class CommonApproveController {
	private Logger logger = LoggerFactory.getLogger(CommonApproveController.class);
	
    @Autowired
	ActivitiService activitiService;
	
    /**
     * @desc 根据指派人查询任务信息
     * @param assignee
     * @return
     */
	@GetMapping("/getAllTaskByAssignee")
	public Map<String,Object> getAllTaskInfoByAssignee(@RequestParam("assignee") String assignee){
		logger.info("传入的指定人信息为：[assignee]="+assignee);
		try {
			List<Map<String,Object>> list = activitiService.getTasksByAssignee(assignee);
			return ResponseResult.newInstance().success(list);
		} catch (Exception e) {
            throw new BizException(ResStatus.SEARCH_TASK_FAIL.getCode(),ResStatus.SEARCH_TASK_FAIL.getMessage());
		}	
	}
	
	/**
	 * @desc 根据候选组查询任务信息
	 * @param candidateGroup
	 * @return
	 */
	@GetMapping("/getAllTaskByGroup")
	public Map<String,Object> getAllTaskInfoByGroup(@RequestParam("candidateGroup") String candidateGroup){
		logger.info("传入的候选组的信息为：[candidateGroup]="+candidateGroup);
		try {
			List<Map<String,Object>> list = activitiService.getTasksByGroup(candidateGroup);
			return ResponseResult.newInstance().success(list);
		} catch (Exception e) {
			throw new BizException(ResStatus.SEARCH_TASK_FAIL.getCode(),ResStatus.SEARCH_TASK_FAIL.getMessage());
		}
	}
}
