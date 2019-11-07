package com.najie.activiti.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.najie.activiti.exception.BizException;
import com.najie.activiti.service.ActivitiService;
import com.najie.activiti.util.ResStatus;
import com.najie.activiti.util.ResponseResult;

/**
 * @desc 员工异动管理
 * @author admin
 *
 */
@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private Logger logger = LoggerFactory.getLogger(TransactionController.class);
    //员工异动申请
    public static final String TRANSACTION_PROCESS = "transaction-process";
    
    @Autowired
    private ActivitiService activitiService;
    
    /**
     * @desc 启动流程实例
     * @param application
     * @return
     */
    @GetMapping("/startProcess")
	public Map<String,Object> startProcessInstance(@RequestParam("application") String application){
		if(StringUtils.isEmpty(application)) {
			throw new BizException(ResStatus.APPLICATION_ISEMPTY.getCode(),ResStatus.APPLICATION_ISEMPTY.getMessage());
		}
    	try {
			Map<String,Object> variables = new HashMap<>();
			variables.put("application", application);
			Map<String, Object> instanceMap = activitiService.startProcessInstanceByKey(TRANSACTION_PROCESS, variables);
			logger.info("["+TRANSACTION_PROCESS+"]流程实例启动成功");
			logger.info("流程实例返回的报文信息：{}",instanceMap);
			Map<String, Object> taskMap = null;
			if(instanceMap!=null && instanceMap.size()>0) {
				String processInstanceId = String.valueOf(instanceMap.get("processInstanceId"));
				taskMap = activitiService.getTaskByInstanceId(processInstanceId);
				logger.info("返回申请人的当前任务信息：{}",taskMap);
			}
			return ResponseResult.newInstance().success(taskMap);
		} catch (Exception e) {
			throw new BizException(ResStatus.START_PROCESS_FAIL.getCode(),ResStatus.START_PROCESS_FAIL.getMessage());
		}
		
	}
    
    /**
     * @desc 批量启动流程实例
     * @param applicationList
     *  传参： applicationList : ["张三","李四","王五","赵六"]
     * @return
     */
    @PostMapping("/startProcessBatch")
    public Map<String,Object> startProcessInstanceBatch(@RequestBody Map<String,Object> params){
    	List<String> applicationList =(List<String>)params.get("applicationList");
    	logger.info("applicationList:"+applicationList);
    	if(applicationList==null || applicationList.size()<1) {
    		throw new BizException(ResStatus.APPLICATION_ISEMPTY.getCode(),ResStatus.APPLICATION_ISEMPTY.getMessage());
    	}
    	List<Map<String,Object>> list = new ArrayList<>();
    	try {
    		for(String application:applicationList) {
    			Map<String,Object> variables = new HashMap<>();
    			variables.put("application", application);
    			Map<String, Object> instanceMap = activitiService.startProcessInstanceByKey(TRANSACTION_PROCESS, variables);
    			logger.info("["+TRANSACTION_PROCESS+"]流程实例启动成功");
    			logger.info("流程实例返回的报文信息：{}",instanceMap);
    			if(instanceMap!=null && instanceMap.size()>0) {
    				String processInstanceId = String.valueOf(instanceMap.get("processInstanceId"));
    				Map<String, Object> taskMap = activitiService.getTaskByInstanceId(processInstanceId);
    				logger.info("返回申请人的当前任务信息：{}",taskMap);
    				list.add(taskMap);
    			}
    		}
			return ResponseResult.newInstance().success(list);
		} catch (Exception e) {
			throw new BizException(ResStatus.START_PROCESS_FAIL.getCode(),ResStatus.START_PROCESS_FAIL.getMessage());
		}
    }
    
    /**
     * @desc 取消流程实例
     * @param processInstanceId
     * @return
     */
    @GetMapping("/cancelProcess")
    public Map<String,Object> cancelProcessInstance(@RequestParam("processInstanceId") String processInstanceId){
    	try {
    		activitiService.cancelProcessInstance(processInstanceId);
        	return ResponseResult.newInstance().success();
		} catch (Exception e) {
			throw new BizException(ResStatus.CANCEL_PROCESS_FAIL.getCode(),ResStatus.CANCEL_PROCESS_FAIL.getMessage());
		}
    }
    
    
    /**
     * @desc 根据流程实例ID查询当前任务
     * @param processInstanceId
     * @return
     */
    @GetMapping("/getTaskByInstanceId")
    public Map<String,Object> getTaskByInstanceId(@RequestParam("processInstanceId") String processInstanceId){
    	if(StringUtils.isEmpty(processInstanceId)) {
    		throw new BizException(ResStatus.PROCESSINSTANCE_ISEMPTY.getCode(),ResStatus.PROCESSINSTANCE_ISEMPTY.getMessage());
    	}
    	try {
    		Map<String, Object> taskMap = activitiService.getTaskByInstanceId(processInstanceId);
    		return ResponseResult.newInstance().success(taskMap);
		} catch (Exception e) {
			throw new BizException(ResStatus.SEARCH_TASK_FAIL.getCode(),ResStatus.SEARCH_TASK_FAIL.getMessage());
		}
    }
    
    /**
     * @desc 根据指派人查询当前任务
     * @param assignee
     * @return
     */
    @GetMapping("/getTaskByAssignee")
    public Map<String,Object> getTaskByAssignee(@RequestParam("assignee") String assignee){
    	logger.info("===进入指派人查询当前任务===");
    	if(StringUtils.isEmpty(assignee)) {
    		throw new BizException(ResStatus.ASSIGNEE_ISEMPTY.getCode(),ResStatus.ASSIGNEE_ISEMPTY.getMessage());
    	}
    	List<Map<String,Object>> resultList = new ArrayList<>();
    	try {
    		List<Map<String,Object>> list = activitiService.getTasksByAssignee(assignee);
    		logger.info("查询的结果记录条数为："+list.size());
    		for(Map<String,Object> map:list) {
    			String processDefinitionId =String.valueOf(map.get("processDefinitionId"));
    			if(processDefinitionId.contains(TRANSACTION_PROCESS)) {
    				resultList.add(map);
    			}
    		}
    		return ResponseResult.newInstance().success(resultList);
		} catch (Exception e) {
			throw new BizException(ResStatus.SEARCH_TASK_FAIL.getCode(),ResStatus.SEARCH_TASK_FAIL.getMessage());
		}
    }
    
    /**
     * @desc 根据候选组查询当前任务信息
     * @param candidateGroup
     * @return
     */
    @GetMapping("/getTaskByGroup")
    public Map<String,Object> getTaskByCandidateGroup(@RequestParam("candidateGroup") String candidateGroup){
    	logger.info("===进入根据候选组查询当前任务===");
    	if(StringUtils.isEmpty(candidateGroup)) {
    		throw new BizException(ResStatus.CANDIDATE_GROUP_ISEMPTY.getCode(),ResStatus.CANDIDATE_GROUP_ISEMPTY.getMessage());
    	}
    	List<Map<String,Object>> resultList = new ArrayList<>();
    	try {
    		List<Map<String,Object>> list = activitiService.getTasksByGroup(candidateGroup);
    		logger.info("查询的结果记录条数为："+list.size());
    		for(Map<String,Object> map:list) {
    			String processDefinitionId =String.valueOf(map.get("processDefinitionId"));
    			if(processDefinitionId.contains(TRANSACTION_PROCESS)) {
    				resultList.add(map);
    			}
    		}
    		return ResponseResult.newInstance().success(resultList);
		} catch (Exception e) {
			throw new BizException(ResStatus.SEARCH_TASK_FAIL.getCode(),ResStatus.SEARCH_TASK_FAIL.getMessage());
		}
    }
    
    /**
     * @desc 根据流程实例ID查询历史任务
     * @param processInstanceId
     * @return
     */
    @GetMapping("/getHistoryTask")
    public Map<String,Object> getHistoryLocalList(@RequestParam("processInstanceId") String processInstanceId){
    	if(StringUtils.isEmpty(processInstanceId)) {
    		throw new BizException(ResStatus.PROCESSINSTANCE_ISEMPTY.getCode(),ResStatus.PROCESSINSTANCE_ISEMPTY.getMessage());
    	}
    	try {
    		List<Map<String,Object>> list = activitiService.getHistoryLocalTask(processInstanceId);
    		return ResponseResult.newInstance().success(list);
		} catch (Exception e) {
			throw new BizException(ResStatus.SEARCH_TASK_FAIL.getCode(),ResStatus.SEARCH_TASK_FAIL.getMessage());
		}
    }
    
    /**
     * @desc 完成任务
     * @param params
     * @return
     */
    @PostMapping("/finishTask")
    public Map<String,Object> completeTask(@RequestBody Map<String,Object> params){
    	if(params==null || params.size()<1) {
    		throw new BizException(ResStatus.PARAMS_ISEMPTY.getCode(),ResStatus.PARAMS_ISEMPTY.getMessage());
    	}
    	//任务ID
    	String taskId = String.valueOf(params.get("taskId"));
    	if(StringUtils.isEmpty(taskId)) {
    		throw new BizException(ResStatus.TASKID_ISEMPTY.getCode(),ResStatus.TASKID_ISEMPTY.getMessage());
    	}
    	try {
    		Map<String, Object> taskMap = activitiService.getTaskById(taskId);
            logger.info("完成任务功能-查询的任务信息:"+taskMap);
    		String taskName = String.valueOf(taskMap.get("taskName"));
    		//设置局部变量
    		setProcessLocalVariables(params, taskId, taskName);
    		activitiService.completeGlobalTask(taskId, params);
    		return ResponseResult.newInstance().success();
		} catch (Exception e) {
			throw new BizException(ResStatus.TASK_SUBMIT_FAIL.getCode(),ResStatus.TASK_SUBMIT_FAIL.getMessage());
		}
    }

    /**
     * @desc 设置流程局部变量
     * @param params
     * @param taskId
     * @param taskName
     */
	private void setProcessLocalVariables(Map<String, Object> params, String taskId, String taskName) {
		if(taskName.equals("异动信息申请")) {
			Map<String,Object> variables = new HashMap<>();
			variables.put("transactionApprove", params.get("transactionApprove"));
			activitiService.setActivitiVariableLocal(taskId, variables);
		}else if(taskName.equals("人事部确认")) {
			Map<String,Object> variables = new HashMap<>();
			variables.put("personalApprove", params.get("personalApprove"));
			activitiService.setActivitiVariableLocal(taskId, variables);
		}else if(taskName.equals("原上级审批")) {
			Map<String,Object> variables = new HashMap<>();
			variables.put("managerApprove", params.get("managerApprove"));
			activitiService.setActivitiVariableLocal(taskId, variables);
		}else if(taskName.equals("现上级审批")) {
			Map<String,Object> variables = new HashMap<>();
			variables.put("superiorApprove", params.get("superiorApprove"));
			activitiService.setActivitiVariableLocal(taskId, variables);
		}else if(taskName.equals("工作交接")) {
			Map<String,Object> variables = new HashMap<>();
			variables.put("handoverApprove", params.get("handoverApprove"));
			activitiService.setActivitiVariableLocal(taskId, variables);
		}else if(taskName.equals("物资移交确认")) {
			Map<String,Object> variables = new HashMap<>();
			variables.put("adminApprove", params.get("adminApprove"));
			activitiService.setActivitiVariableLocal(taskId, variables);
		}
	}
}
