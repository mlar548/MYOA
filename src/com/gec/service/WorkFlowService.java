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
	//ͨ��zip��������
	public void addDeploymentProcess(InputStream in, String processName);
	//չʾ�����б�
	public List<Task> showTaskList(String username);
	//��������������
	public void saveStartBaoxiaoBill(String key, String username, Integer leaveId);
	//ͨ��taskID��ѯ������
	public Baoxiaobill selectBaoXiaoBillbyId(String taskId);
	//ͨ��taskID��ѯ����
	public List<Comment> getCommitListById(String taskId);
	//�ύ������
	public void insertComment(String outcome, String taskId, String comment, String username, String id);
	//����ͼ����
	public Map<String, Object> findCordingById(String taskId);
	//��ǰ����ͼ
	public ProcessDefinition findProcessDefinitionById(String taskId);
	//��������ͼ
	public ProcessDefinition findProcessDefinitionByBillId(String businesskey);
	//����ͼƬ
	public InputStream viewImage(String deploymentId, String imageName);
	//����ҳ�ı������б�
	public List<Baoxiaobill> selectBiaoxiaoList(Long id, Integer pageNum, Integer pageSize);
	//��ѯ�Զ�������
	public List<ProcessDefinition> selectDefinitionProcess();
	//��ѯ��������
	public List<Deployment> selectDeploymentList();
	//��ѯ�û�
	public List<EmployeeCustom> selectEmpList();
	//ɾ������
	public void deleteDeploymentProcess(String deploymentId);
	//ͨ��businessKey��ѯ����
	public Task selectTaskByBusinessKey(String businesskey);
	//ͨ��������IDɾ��������
	public Integer deleteBaoxiaoBillByBillId(String id);
	//ͨ��businessKey��ѯ����
	public List<Comment> viewHisCommentByBillId(String businesssKey);
}
