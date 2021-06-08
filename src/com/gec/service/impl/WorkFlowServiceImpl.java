package com.gec.service.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

//import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gec.mapper.BaoxiaobillMapper;
import com.gec.mapper.SysPermissionMapperCustom;
import com.gec.pojo.Baoxiaobill;
import com.gec.pojo.BaoxiaobillExample;
import com.gec.pojo.BaoxiaobillExample.Criteria;
import com.gec.pojo.EmployeeCustom;
import com.gec.service.WorkFlowService;
//import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageHelper;


@Service
public class WorkFlowServiceImpl implements WorkFlowService {

	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
//	@Autowired
//	private FormService formService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private BaoxiaobillMapper baoxiaoBillMapper;
//	@Autowired
//	private EmployeeMapper employeeMapper;
	@Autowired
	private SysPermissionMapperCustom sysPermissionMapperCustom;
	@Override
	public void addDeploymentProcess(InputStream in, String processName) {
		ZipInputStream zipin = new ZipInputStream(in);
		this.repositoryService.createDeployment()
		.name(processName)
		.addZipInputStream(zipin)
		.deploy();
	}

	@Override
	public List<Task> showTaskList(String username) {
		
		List<Task> list = this.taskService.createTaskQuery()
		.taskAssignee(username)
		.orderByTaskCreateTime()
		.desc()
		.list();
		
		if(list.size()>0) {
			return list;
		}
		return null;
	}

	@Override
	public void saveStartBaoxiaoBill(String key, String username, Integer leaveId) {
		
		
		Map<String,Object> map = new HashMap<>();
		
		map.put("inputUser", username);
		
		String businessKey = key + "." + leaveId;
		
		this.runtimeService.startProcessInstanceByKey(key, businessKey, map);
		
	}

	@Override
	public Baoxiaobill selectBaoXiaoBillbyId(String taskId) {
		
		Task task = this.taskService.createTaskQuery()
		.taskId(taskId)
		.singleResult();
		
		String processInstanceId = task.getProcessInstanceId();

		ProcessInstance processInstance = this.runtimeService.createProcessInstanceQuery()
		.processInstanceId(processInstanceId)
		.singleResult();
		
		String businessKey = processInstance.getBusinessKey();
		String id = "";
		
		if(businessKey!=null&&!"".equals(businessKey)) {
			id = businessKey.split("\\.")[1];
		}
		
		Baoxiaobill baoxiaobill = this.baoxiaoBillMapper.selectByPrimaryKey(Integer.parseInt(id));
		return baoxiaobill;
	}

	@Override
	public List<Comment> getCommitListById(String taskId) {
		Task task = this.taskService.createTaskQuery()
		.taskId(taskId)
		.singleResult();
		
		List<Comment> comments = this.taskService.getProcessInstanceComments(task.getProcessInstanceId());
		
		return comments;
	}

	@Override
	public void insertComment(String result, String taskId, String comment, String username, String id) {
		Task task = this.taskService.createTaskQuery()
		.taskId(taskId)
		.singleResult();
		
		Authentication.setAuthenticatedUserId(username);
		
		String processInstanceId = task.getProcessInstanceId();
		
		this.taskService.addComment(taskId, processInstanceId, comment);
		//审批结果处理
		HashMap<String,Object> map = new HashMap<String, Object>();	
		map.put("message", result);
		
		this.taskService.complete(taskId, map);
		
		ProcessInstance processInstance = this.runtimeService.createProcessInstanceQuery()
		.processInstanceId(processInstanceId)
		.singleResult();
		//当申请流程实例不存在时,证明审核已结束,状态改为2
		if(processInstance == null) {
			Baoxiaobill baoxiaobill = this.baoxiaoBillMapper.selectByPrimaryKey(Integer.parseInt(id));
			baoxiaobill.setState(2);		
			this.baoxiaoBillMapper.updateByPrimaryKey(baoxiaobill);
		}
		
	}

	@Override
	public Map<String, Object> findCordingById(String taskId) {
		
		Task task = this.taskService.createTaskQuery()
		.taskId(taskId).singleResult();
		
		String definitionId = task.getProcessDefinitionId();
		
		ProcessDefinitionEntity definitionEntity = (ProcessDefinitionEntity) this.repositoryService.getProcessDefinition(definitionId);
		
		String processInstanceId = task.getProcessInstanceId();
		
		ProcessInstance processInstance = this.runtimeService.createProcessInstanceQuery()
		.processInstanceId(processInstanceId)
		.singleResult();
		
		String activityId = processInstance.getActivityId();
		
		ActivityImpl activityImpl = definitionEntity.findActivity(activityId);
		
		Map<String, Object> map = new HashMap<>();
		map.put("x", activityImpl.getX());
		map.put("y", activityImpl.getY());
		map.put("width", activityImpl.getWidth());
		map.put("height", activityImpl.getHeight());
		System.out.println("x========="+activityImpl.getX());
		System.out.println("y========="+activityImpl.getY());
		System.out.println("Width========="+activityImpl.getWidth());
		System.out.println("Height========="+activityImpl.getHeight());
	
		return map;
	}

	@Override
	public ProcessDefinition findProcessDefinitionById(String taskId) {
		Task task = this.taskService.createTaskQuery()
		.taskId(taskId)
		.singleResult();
		
		String definitionId = task.getProcessDefinitionId();
		
		ProcessDefinition processDefinition = this.repositoryService.createProcessDefinitionQuery()
		.processDefinitionId(definitionId)
		.singleResult();
		return processDefinition;
	}

	@Override
	public ProcessDefinition findProcessDefinitionByBillId(String businessKey) {
		Task task = this.taskService.createTaskQuery()
		.processInstanceBusinessKey(businessKey)
		.singleResult();
		
		String definitionId = task.getProcessDefinitionId();
		
		ProcessDefinition processDefinition = this.repositoryService.createProcessDefinitionQuery()
		.processDefinitionId(definitionId)
		.singleResult();
		return processDefinition;
	}
	
	@Override
	public InputStream viewImage(String deploymentId, String imageName) {
		InputStream resourceAsStream = this.repositoryService.getResourceAsStream(deploymentId, imageName);
		return resourceAsStream;
	}

	@Override
	public List<Baoxiaobill> selectBiaoxiaoList(Long userId, Integer pageNum, Integer pageSize) {
		BaoxiaobillExample bx = new BaoxiaobillExample();
		Criteria criteria = bx.createCriteria();
		Integer id = userId.intValue();
		criteria.andUserIdEqualTo(id);
		PageHelper.startPage(pageNum, pageSize);
		List<Baoxiaobill> list = this.baoxiaoBillMapper.selectByExample(bx);
		if(list.size() > 0) {
			return list;
		}
		return null;
	}

	@Override
	public List<ProcessDefinition> selectDefinitionProcess() {
		List<ProcessDefinition> list = this.repositoryService.createProcessDefinitionQuery()
		.orderByDeploymentId()
		.desc()
		.list();
		if(list.size() > 0) {
			return list;
		}
		return null;
		
	}

	@Override
	public List<Deployment> selectDeploymentList() {
		List<Deployment> list = this.repositoryService.createDeploymentQuery()
		.orderByDeploymentId()
		.desc()
		.list();
		if(list.size() > 0) {
			return list;
		}
		return null;
	}

	@Override
	public List<EmployeeCustom> selectEmpList() {
		List<EmployeeCustom> list = this.sysPermissionMapperCustom.findUserAndRoleList();
		if(list.size()>0) {
			return list;
		}
//		List<Employee> list = this.employeeMapper.selectByExample(null);
////		Map<String,Long> map = new HashMap<>();
//		if(list.size() > 0) {
//			for (int i = 0;i<list.size();i++) {
////				map.put("managerId", list.get(i).getManagerId());			
////				if(map.containsValue(list.get(i).getId()))
//				for(int j = 0; j<list.size();j++) {
//					if(list.get(i).getManagerId()==list.get(j).getId()) {
//						list.get(i).setManagerName(list.get(j).getName());
//					}
//				}
//			}
//			return list;
//		}
		return null;
	}

	@Override
	public void deleteDeploymentProcess(String deploymentId) {
		
		this.repositoryService.deleteDeployment(deploymentId, true);
		
	}

	@Override
	public Task selectTaskByBusinessKey(String businesskey) {
		Task task = this.taskService.createTaskQuery()
		.processInstanceBusinessKey(businesskey)
		.singleResult();
		return task;
	}

	@Override
	public Integer deleteBaoxiaoBillByBillId(String id) {
		if(id!=null&&!"".equals(id)) {
			this.baoxiaoBillMapper.deleteByPrimaryKey(Integer.parseInt(id));
			return 1;
		}
		return 0;	
	}

	@Override
	public List<Comment> viewHisCommentByBillId(String businesssKey) {
		HistoricProcessInstance hisProcessInstance = this.historyService.createHistoricProcessInstanceQuery()
		.processInstanceBusinessKey(businesssKey)
		.singleResult();
		
		String hisProcessId = hisProcessInstance.getId();
		
		List<Comment> comments = this.taskService.getProcessInstanceComments(hisProcessId);
		return comments;
	}

	
	

}
