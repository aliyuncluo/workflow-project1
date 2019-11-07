package com.najie.activiti.util;

public enum ResStatus {
     
	SUCCESS("10000","操作成功"),
	TASK_SUBMIT_FAIL("10001","任务提交失败"),
	CLAIM_TASK_FAIL("10002","领取任务失败"),
	CANCEL_PROCESS_FAIL("10003","取消流程失败"),
	SEARCH_TASK_FAIL("10004","查询任务失败"),
	START_PROCESS_FAIL("10005","启动流程失败"),
	APPLICATION_ISEMPTY("10006","传入的申请人信息为空"),
	PROCESSINSTANCE_ISEMPTY("10007","传入的流程实例为空"),
	ASSIGNEE_ISEMPTY("10008","传入的指派人信息为空"),
	CANDIDATE_GROUP_ISEMPTY("10009","传入的候选组信息为空"),
	PARAMS_ISEMPTY("10010","传入的参数为空"),
	TASKID_ISEMPTY("10011","任务ID传入的为空"),
	CANDIDATE_USER_ISEMPTY("10012","传入的候选人信息为空");
	
	private String code;
	private String message;
	
	ResStatus(String code,String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
