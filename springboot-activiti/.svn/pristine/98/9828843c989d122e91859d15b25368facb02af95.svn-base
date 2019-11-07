package com.najie.activiti.controller;

import com.najie.activiti.exception.BizException;
import com.najie.activiti.service.ActivitiService;
import com.najie.activiti.util.ResStatus;
import com.najie.activiti.util.ResponseResult;

import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @desc 员工离职流程
 * @author admin
 *
 */
@RestController
@RequestMapping("/activiti")
public class LeaveProcessController {
    private Logger logger = LoggerFactory.getLogger(LeaveProcessController.class);
    //离职流程
    public static final String LEAVE_PROCESS="leave-process";
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
    		throw new BizException(ResStatus.START_PROCESS_FAIL.getCode(),ResStatus.START_PROCESS_FAIL.getMessage());
    	}
    	try {
    		Map<String, Object> result = null;
        	Map<String, Object> variables = new HashMap<>();
            variables.put("application",application);
            Map<String, Object> map = activitiService.startProcessInstanceByKey(LEAVE_PROCESS,variables);
            logger.info("["+LEAVE_PROCESS+"]流程实例启动成功");
			logger.info("流程实例返回的报文信息：{}",map);
            if(map!=null && map.size()>0) {
            	String processInstanceId = String.valueOf(map.get("processInstanceId"));
            	result = activitiService.getTaskByInstanceId(processInstanceId);
            	logger.info("返回申请人的当前任务信息：{}",result);
            }
            return ResponseResult.newInstance().success(result);
		} catch (Exception e) {
			throw new BizException(ResStatus.START_PROCESS_FAIL.getCode(),ResStatus.START_PROCESS_FAIL.getMessage());
		}
    	
    }
    
    /**
     * @desc 批量启动流程实例
     * @param applicationList
     * @return
     */
    @PostMapping("/startProcessBatch")
    public Map<String,Object> startProcessInstanceBatch(@RequestParam("applicationList") List<String> applicationList){
    	if(applicationList==null || applicationList.size()<1) {
    		throw new BizException(ResStatus.START_PROCESS_FAIL.getCode(),ResStatus.START_PROCESS_FAIL.getMessage());
    	}
    	try {
    		List<Map<String, Object>> list = new ArrayList<>();
    		for(String application:applicationList) {
    			Map<String, Object> variables = new HashMap<>();
                variables.put("application",application);
                Map<String, Object> map = activitiService.startProcessInstanceByKey(LEAVE_PROCESS,variables);
                if(map!=null && map.size()>0) {
                	String processInstanceId = String.valueOf(map.get("processInstanceId"));
                	Map<String, Object> result = activitiService.getTaskByInstanceId(processInstanceId);
                    list.add(result);
                }
    		}
            return ResponseResult.newInstance().success(list);
		} catch (Exception e) {
			throw new BizException(ResStatus.START_PROCESS_FAIL.getCode(),ResStatus.START_PROCESS_FAIL.getMessage());
		}
    }

    /**
     * @desc 根据流程实例ID获取当前任务
     * @param processInstanceId
     * @return
     */
    @GetMapping("/getTaskByInstanceId")
    public Map<String,Object> getTaskByInstanceId(@RequestParam("processInstanceId") String processInstanceId){
        if(StringUtils.isEmpty(processInstanceId)) {
        	throw new BizException(ResStatus.SEARCH_TASK_FAIL.getCode(),ResStatus.SEARCH_TASK_FAIL.getMessage());
        }
    	try {
        	Map<String, Object> map = activitiService.getTaskByInstanceId(processInstanceId);
            return ResponseResult.newInstance().success(map);
		} catch (Exception e) {
			throw new BizException(ResStatus.SEARCH_TASK_FAIL.getCode(),ResStatus.SEARCH_TASK_FAIL.getMessage());
		}
    	
    }
    
    
    /**
     * @desc 根据流程实例获取并行任务
     * @param processInstanceId
     * @return
     */
    @GetMapping("/getParallelTask")
    public Map<String,Object> getParallelTaskByInstanceId(@RequestParam("processInstanceId") String processInstanceId){
    	if(StringUtils.isEmpty(processInstanceId)) {
    		throw new BizException(ResStatus.SEARCH_TASK_FAIL.getCode(),ResStatus.SEARCH_TASK_FAIL.getMessage());
    	}
    	try {
    		List<Map<String,Object>> list = activitiService.getParallelTaskByInstanceId(processInstanceId);
        	return ResponseResult.newInstance().success(list);
		} catch (Exception e) {
			throw new BizException(ResStatus.SEARCH_TASK_FAIL.getCode(),ResStatus.SEARCH_TASK_FAIL.getMessage());
		}
    	
    }

    /**
     * @desc 完成任务
     * @param taskId
     * @param approve
     * @return
     */
    @PostMapping("/finishTask")
    public Map<String,Object> finishTask(@RequestBody Map<String,Object> params){
    	logger.info("完成任务传入的参数为："+params);
    	if(params==null || params.size()<1) {
    		throw new BizException(ResStatus.PARAMS_ISEMPTY.getCode(),ResStatus.PARAMS_ISEMPTY.getMessage());
    	}
    	String taskId = String.valueOf(params.get("taskId"));
    	logger.info("taskId:"+taskId);
    	if(StringUtils.isEmpty(taskId)) {
    		throw new BizException(ResStatus.TASKID_ISEMPTY.getCode(),ResStatus.TASKID_ISEMPTY.getMessage());
    	}
    	try {
    		Map<String, Object> taskMap = activitiService.getTaskById(taskId);
            logger.info("查询当前的任务taskMap:"+taskMap);
    		String taskName = String.valueOf(taskMap.get("taskName"));
    		//设置局部流程变量
    		setTaskLocalVariables(params, taskId, taskName);
            activitiService.completeGlobalTask(taskId,params); //任务完成,任务ID失效
            logger.info("任务完成");
            return ResponseResult.newInstance().success();
		} catch (Exception e) {
			throw new BizException(ResStatus.TASK_SUBMIT_FAIL.getCode(),ResStatus.TASK_SUBMIT_FAIL.getMessage());
		}
        
    }

    /**
     * @desc 设置局部流程变量
     * @param params
     * @param taskId
     * @param taskName
     */
	private void setTaskLocalVariables(Map<String, Object> params, String taskId, String taskName) {
		if(taskName.equals("离职申请")) {
			Map<String,Object> variables = new HashMap<>();
			variables.put("applyApprove", params.get("applyApprove"));//申请审批状态
			variables.put("approveSuggest", params.get("approveSuggest"));//审批意见
			activitiService.setActivitiVariableLocal(taskId, variables);
		}else if(taskName.equals("面谈信息")) {
			Map<String,Object> variables = new HashMap<>();
			variables.put("approveSuggest", params.get("approveSuggest"));//审批意见
			variables.put("interviewApprove", params.get("interviewApprove"));//面谈审批状态
			activitiService.setActivitiVariableLocal(taskId, variables);
		}else if(taskName.equals("上级审批1")) {
			Map<String,Object> variables = new HashMap<>();
			variables.put("approveSuggest", params.get("approveSuggest"));//审批意见
			variables.put("superior1Approve", params.get("superior1Approve")); //上级审批1状态
			activitiService.setActivitiVariableLocal(taskId, variables);
		}else if(taskName.equals("上级审批2")) {
			Map<String,Object> variables = new HashMap<>();
			variables.put("approveSuggest", params.get("approveSuggest"));//审批意见
			variables.put("superior2Approve", params.get("superior2Approve")); //上级审批2状态
			activitiService.setActivitiVariableLocal(taskId, variables);
		}else if(taskName.equals("上级审批3")) {
			Map<String,Object> variables = new HashMap<>();
			variables.put("approveSuggest", params.get("approveSuggest"));//审批意见
			variables.put("superior3Approve", params.get("superior3Approve")); //上级审批3状态
			activitiService.setActivitiVariableLocal(taskId, variables);
		}else if(taskName.equals("上级审批4")) {
			Map<String,Object> variables = new HashMap<>();
			variables.put("approveSuggest", params.get("approveSuggest"));//审批意见
			variables.put("superior4Approve", params.get("superior4Approve")); //上级审批4状态
			activitiService.setActivitiVariableLocal(taskId, variables);
		}else if(taskName.equals("上级审批5")) {
			Map<String,Object> variables = new HashMap<>();
			variables.put("approveSuggest", params.get("approveSuggest"));//审批意见
			variables.put("superior5Approve", params.get("superior5Approve")); //上级审批5状态
			activitiService.setActivitiVariableLocal(taskId, variables);
		}else if(taskName.equals("上级审批6")) {
			Map<String,Object> variables = new HashMap<>();
			variables.put("approveSuggest", params.get("approveSuggest"));//审批意见
			variables.put("superior6Approve", params.get("superior6Approve")); //上级审批6状态
			activitiService.setActivitiVariableLocal(taskId, variables);
		}else if(taskName.equals("上级审批7")) {
			Map<String,Object> variables = new HashMap<>();
			variables.put("approveSuggest", params.get("approveSuggest"));//审批意见
			variables.put("superior7Approve", params.get("superior7Approve")); //上级审批7状态
			activitiService.setActivitiVariableLocal(taskId, variables);
		}else if(taskName.equals("上级审批8")) {
			Map<String,Object> variables = new HashMap<>();
			variables.put("approveSuggest", params.get("approveSuggest"));//审批意见
			variables.put("superior8Approve", params.get("superior8Approve")); //上级审批8状态
			activitiService.setActivitiVariableLocal(taskId, variables);
		}else if(taskName.equals("上级审批9")) {
			Map<String,Object> variables = new HashMap<>();
			variables.put("approveSuggest", params.get("approveSuggest"));//审批意见
			variables.put("superior9Approve", params.get("superior9Approve")); //上级审批9状态
			activitiService.setActivitiVariableLocal(taskId, variables);
		}else if(taskName.equals("上级审批10")) {
			Map<String,Object> variables = new HashMap<>();
			variables.put("approveSuggest", params.get("approveSuggest"));//审批意见
			variables.put("superior10Approve", params.get("superior10Approve")); //上级审批10状态
			activitiService.setActivitiVariableLocal(taskId, variables);
		}else if(taskName.equals("工作移交")) {
			Map<String,Object> variables = new HashMap<>();
			variables.put("handoverApprove", params.get("handoverApprove")); //工作移交状态
			activitiService.setActivitiVariableLocal(taskId, variables);
		}else if(taskName.equals("服务运营部物资移交")) {
			Map<String,Object> variables = new HashMap<>();
			variables.put("operatorApprove", params.get("operatorApprove")); //服务运营部移交状态
			activitiService.setActivitiVariableLocal(taskId, variables);
		}else if(taskName.equals("行政会务部物资移交")) {
			Map<String,Object> variables = new HashMap<>();
			variables.put("adminApprove", params.get("adminApprove")); //行政会务部移交状态
			activitiService.setActivitiVariableLocal(taskId, variables);
		}else if(taskName.equals("技术部物资移交")) {
			Map<String,Object> variables = new HashMap<>();
			variables.put("technologyApprove", params.get("technologyApprove")); //技术部移交状态
			activitiService.setActivitiVariableLocal(taskId, variables);
		}else if(taskName.equals("简历部确认")) {
			Map<String,Object> variables = new HashMap<>();
			variables.put("resumeApprove", params.get("resumeApprove")); //简历部确认状态
			activitiService.setActivitiVariableLocal(taskId, variables);
		}else if(taskName.equals("财务部信息")) {
			Map<String,Object> variables = new HashMap<>();
			variables.put("financeApprove", params.get("financeApprove")); //财务部信息状态
			activitiService.setActivitiVariableLocal(taskId, variables);
		}else if(taskName.equals("人事部信息")) {
			Map<String,Object> variables = new HashMap<>();
			variables.put("personalApprove", params.get("personalApprove")); //人事部信息状态
			activitiService.setActivitiVariableLocal(taskId, variables);
		}
	}

    /**
     * @desc 查询历史任务
     * @param processInstanceId
     * @return
     */
    @GetMapping("/historyTask")
    public Map<String,Object> searchHistoryTask(@RequestParam("processInstanceId") String processInstanceId){
        if(StringUtils.isEmpty(processInstanceId)) {
        	throw new BizException(ResStatus.PROCESSINSTANCE_ISEMPTY.getCode(),ResStatus.PROCESSINSTANCE_ISEMPTY.getMessage());
        }
    	try {
        	List<Map<String,Object>> list= activitiService.getHistoryLocalTask(processInstanceId);
            return ResponseResult.newInstance().success(list);
		} catch (Exception e) {
			throw new BizException(ResStatus.SEARCH_TASK_FAIL.getCode(),ResStatus.SEARCH_TASK_FAIL.getMessage());
		}
    	
    }
    
   
    
    /**
     * 根据指派人查询任务
     * @param assignee
     * @return
     */
    @GetMapping("/getTaskByAssignee")
    public Map<String,Object> getTaskByAssignee(@RequestParam("assignee") String assignee){
    	if(StringUtils.isEmpty(assignee)) {
    		throw new BizException(ResStatus.ASSIGNEE_ISEMPTY.getCode(),ResStatus.ASSIGNEE_ISEMPTY.getMessage());
    	}
    	List<Map<String,Object>> resultList = new ArrayList<>();
    	try {
    		List<Map<String,Object>> list = activitiService.getTasksByAssignee(assignee);
    		for(Map<String,Object> map:list) {
    			String processDefinitionId =String.valueOf(map.get("processDefinitionId"));
    			if(processDefinitionId.contains(LEAVE_PROCESS)) {
    				resultList.add(map);
    			}
    		}
        	return ResponseResult.newInstance().success(resultList);
		} catch (Exception e) {
			throw new BizException(ResStatus.SEARCH_TASK_FAIL.getCode(),ResStatus.SEARCH_TASK_FAIL.getMessage());
		}
    }
    
    /**
     * 根据候选人查询任务
     * @param candidateUser
     * @return
     */
    @GetMapping("/getTaskByCandidateUser")
    public Map<String,Object> getTaskByCandidateUser(@RequestParam("candidateUser") String candidateUser){
    	if(StringUtils.isEmpty(candidateUser)) {
    		throw new BizException(ResStatus.CANDIDATE_USER_ISEMPTY.getCode(),ResStatus.CANDIDATE_USER_ISEMPTY.getMessage());
    	}
    	List<Map<String,Object>> resultList = new ArrayList<>();
    	try {
    		List<Map<String,Object>> list = activitiService.getTaskByCandidateUser(candidateUser);
    		for(Map<String,Object> map:list) {
    			String processDefinitionId =String.valueOf(map.get("processDefinitionId"));
    			if(processDefinitionId.contains(LEAVE_PROCESS)) {
    				resultList.add(map);
    			}
    		}
    		return ResponseResult.newInstance().success(resultList);
		} catch (Exception e) {
			throw new BizException(ResStatus.SEARCH_TASK_FAIL.getCode(),ResStatus.SEARCH_TASK_FAIL.getMessage());
		}
    	
    }
    
    /**
     * 根据候选组查询任务
     * @param candidateGroup
     * @return
     */
    @GetMapping("/getTaskByCandidateGroup")
    public Map<String,Object> getTaskByCandidateGroup(@RequestParam("candidateGroup") String candidateGroup){
    	if(StringUtils.isEmpty(candidateGroup)) {
    		throw new BizException(ResStatus.CANDIDATE_GROUP_ISEMPTY.getCode(),ResStatus.CANDIDATE_GROUP_ISEMPTY.getMessage());
    	}
    	List<Map<String,Object>> resultList = new ArrayList<>();
    	try {
    		List<Map<String,Object>> list = activitiService.getTasksByGroup(candidateGroup);
    		for(Map<String,Object> map:list) {
    			String processDefinitionId =String.valueOf(map.get("processDefinitionId"));
    			if(processDefinitionId.contains(LEAVE_PROCESS)) {
    				resultList.add(map);
    			}
    		}
    		return ResponseResult.newInstance().success(resultList);
		} catch (Exception e) {
			throw new BizException(ResStatus.SEARCH_TASK_FAIL.getCode(),ResStatus.SEARCH_TASK_FAIL.getMessage());
		}
    }
    
    /**
     * @desc 取消流程实例
     * @param processInstanceId
     * @return
     */
    @GetMapping("/cancelProcess")
    public Map<String,Object> cancelProcessInstance(@RequestParam("processInstanceId") String processInstanceId){
    	if(StringUtils.isEmpty(processInstanceId)) {
    		throw new BizException(ResStatus.PROCESSINSTANCE_ISEMPTY.getCode(),ResStatus.PROCESSINSTANCE_ISEMPTY.getMessage());
    	}
    	try {
    		activitiService.cancelProcessInstance(processInstanceId);
        	return ResponseResult.newInstance().success();
		} catch (Exception e) {
			throw new BizException(ResStatus.CANCEL_PROCESS_FAIL.getCode(),ResStatus.CANCEL_PROCESS_FAIL.getMessage());
		}
    }
    
    /**
     * @desc 领取任务
     * @param taskId
     * @param userId
     * @return
     */
    @GetMapping("/claimTask")
    public Map<String,Object> claimTaskById(@RequestParam("taskId") String taskId,
    		                                @RequestParam("userId") String userId){
    	try {
    		activitiService.claimTask(taskId, userId);
        	return ResponseResult.newInstance().success();
		} catch (Exception e) {
			throw new BizException(ResStatus.CLAIM_TASK_FAIL.getCode(),ResStatus.CLAIM_TASK_FAIL.getMessage());
		}	
    }
}
