package com.gec.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;

import com.gec.pojo.Baoxiaobill;
import com.gec.pojo.EmployeeCustom;

public interface WorkFlowService {
	//通过zip部署流程
	public void addDeploymentProcess(InputStream in, String processName);
	//展示任务列表
	public List<Task> showTaskList(String username);
	//创建报销单申请
	public void saveStartBaoxiaoBill(String key, String username, Integer leaveId);
	//通过taskID查询报销单
	public Baoxiaobill selectBaoXiaoBillbyId(String taskId);
	//通过taskID查询评论
	public List<Comment> getCommitListById(String taskId);
	//提交报销单
	public void insertComment(String outcome, String taskId, String comment, String username, String id);
	//流程图进程
	public Map<String, Object> findCordingById(String taskId);
	//当前流程图
	public ProcessDefinition findProcessDefinitionById(String taskId);
	//进度流程图
	public ProcessDefinition findProcessDefinitionByBillId(String businesskey);
	//流程图片
	public InputStream viewImage(String deploymentId, String imageName);
	//带分页的报销单列表
	public List<Baoxiaobill> selectBiaoxiaoList(Long id, Integer pageNum, Integer pageSize);
	//查询自定义流程
	public List<ProcessDefinition> selectDefinitionProcess();
	//查询部署流程
	public List<Deployment> selectDeploymentList();
	//查询用户
	public List<EmployeeCustom> selectEmpList();
	//删除流程
	public void deleteDeploymentProcess(String deploymentId);
	//通过businessKey查询任务
	public Task selectTaskByBusinessKey(String businesskey);
	//通过报销单ID删除报销单
	public Integer deleteBaoxiaoBillByBillId(String id);
	//通过businessKey查询评论
	public List<Comment> viewHisCommentByBillId(String businesssKey);
}
