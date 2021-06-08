package com.gec.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
//import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.impl.util.json.JSONObject;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
import com.gec.pojo.ActiveUser;
import com.gec.pojo.Baoxiaobill;
import com.gec.pojo.Employee;
import com.gec.service.BaoXiaoBillService;
import com.gec.service.EmployeeService;
import com.gec.service.WorkFlowService;
import com.gec.utils.Constants;
import com.github.pagehelper.PageInfo;

@Controller
public class WorkFlowController {
	@Autowired
	private WorkFlowService workflowService;
	@Autowired
	private BaoXiaoBillService baoXiaoBillService;
	@Autowired
	private EmployeeService employeeService;
	//��������
	@RequestMapping("/deployProcess")
	public String deployProcess(MultipartFile fileName, String processName) {
		try {
			
			this.workflowService.addDeploymentProcess(fileName.getInputStream(), processName);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:/processDefinitionList";
	}
	
	/*
	 * ɾ������
	 */	
	@RequestMapping("delDeployment")
	public String delDeployment(String deploymentId) {
		
		this.workflowService.deleteDeploymentProcess(deploymentId);
		
		return "redirect:/processDefinitionList";
	}
	//�鿴�����б�
	@RequestMapping("/myTaskList")
	public String showTaskList(Model model) {
		
		ActiveUser emp = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
		//ͨ���û����Ʋ�ѯ��������е�����
		List<Task> taskList = this.workflowService.showTaskList(emp.getUserid());
		
		model.addAttribute("taskList", taskList);
		
		return "workflow_task";
	}
	//���뱨���뱣�汨����
	@RequestMapping("saveStartBaoxiao")
	public String saveStartLeaveBill(Baoxiaobill baoxiaobill) {
		
		baoxiaobill.setCreatdate(new Date());
		baoxiaobill.setState(1);
		
		ActiveUser emp = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
		
		String key = Constants.Leave_KEY;
		
		Integer id = (int) emp.getId();
		
		baoxiaobill.setUserId(id);
		
		//���汨����
		this.baoXiaoBillService.saveBaoxiaoBill(baoxiaobill);
		
		Integer leaveId = baoxiaobill.getId();
		
		//����������
		this.workflowService.saveStartBaoxiaoBill(key, emp.getUserid(), leaveId);
		
		
		return "redirect:/myTaskList";
		
	}
	
//	//�鿴�ҵı�����
	@RequestMapping("/myBaoxiaoBill")
	public String myBaoxiaoBillList(/* String page, Model model */) {
//		ActiveUser emp = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
//		System.out.println("ActiveUser_id===================="+emp.getId());
//		Integer pageNum = Integer.parseInt(/* page */"1");
//		Integer pageSize = 5;
//		List<Baoxiaobill> list = this.workflowService.selectBiaoxiaoList(emp.getId(), pageNum, pageSize);
//		
//		model.addAttribute("baoxiaoList", list);
		
		return "baoxiaobill";
		
	}
	
	//�鿴�ҵı�����2
	@RequestMapping("/myBaoxiaoBillList")
//	@ResponseBody
	public void myBaoxiaoBillList(Integer page, Integer limit,HttpServletResponse response) throws Exception {
//		ObjectMapper om = new ObjectMapper();
		JSONObject jb = new JSONObject();
		ActiveUser emp = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
		System.out.println("ActiveUser_id===================="+emp.getId());
		Integer pageNum = page;
		Integer pageSize = limit;

		if(page!=null&&limit!=null) {
			pageNum = page;
			pageSize = limit;
			
		}
		System.out.println("pageNum="+pageNum+"   pageSize="+pageSize);

		List<Baoxiaobill> list = this.workflowService.selectBiaoxiaoList(emp.getId(), pageNum, pageSize);
		
		PageInfo<Baoxiaobill> info = new PageInfo<Baoxiaobill>(list);
		//��ҳ��
		long total = info.getTotal();
	
//		Map<String,Object> map = new HashMap<String, Object>();
		jb.put("code", 0);
        jb.put("msg", "");
        jb.put("count", total);
        jb.put("data", list);

		
//		om.writeValueAsString(map);	
		 response.setContentType("application/json;charset=utf-8");
	        PrintWriter pw = response.getWriter();
	        pw.write(jb.toString());
	        pw.close();
		
	
	}
	//����������
	@RequestMapping("/viewTaskForm")
	public ModelAndView viewTaskFrom(String taskId) {
		ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
		ModelAndView mv = new ModelAndView();
		
		 Baoxiaobill baoxiaobill = this.workflowService.selectBaoXiaoBillbyId(taskId);
		
		List<Comment> list = this.workflowService.getCommitListById(taskId);
		
		mv.addObject("baoxiaoBill", baoxiaobill);
		
		mv.addObject("commentList", list);
		
		mv.addObject("taskId", taskId);
		Employee emp = this.employeeService.findEmployeeByName(activeUser.getUserid());
		//������ť
		List<String> submitList = new ArrayList<String>();
		
		if(emp.getRole()<=1) {
			submitList.add("�ύ");
		}else {
			submitList.add("ͬ��");
			submitList.add("��ͬ��");
			submitList.add("����");
		}
		mv.addObject("outcomeList", submitList);
		
		mv.setViewName("approve_baoxiao");
		return mv;
		
	}
	//�ύ���
	@RequestMapping("/submitTask")
	public String submitTask(String id, String taskId, String comment, String outcome) {

		ActiveUser emp = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
		Baoxiaobill baoxiaobill = this.workflowService.selectBaoXiaoBillbyId(taskId);
		BigDecimal money = baoxiaobill.getMoney();
		if (money==null) {
			money=new BigDecimal(0);
		}
		System.out.println("������Ϊ========"+money);
		//BigDecimal compareTo()==1Ϊ a����b; ==-1 a<b; ==0 a=b;
		BigDecimal bd = new BigDecimal(5000);
		String result = null;
		if("ͬ��".equals(outcome)) {
			if(money.compareTo(bd) == 1) {
				result = "������5000";
			}else {
				result = "���С�ڵ���5000";
			}
		}else if("��ͬ��".equals(outcome)) {
				result = outcome;
		}else if("�ύ".equals(outcome)){
				result = "�ύ";
		}else {
			result = "����";
		}
		//USERID==NAME
		this.workflowService.insertComment(result, taskId, comment, emp.getUserid(), id);
		
		return "redirect:/myTaskList";
	}
	//�鿴��ǰ����ͼ
	@RequestMapping("viewCurrentImage")
	public String viewCurrentImage(String taskId, ModelMap model) {
		
		ProcessDefinition pd =this.workflowService.findProcessDefinitionById(taskId);
		
		Map<String, Object> map = this.workflowService.findCordingById(taskId);
		
		model.addAttribute("deploymentId", pd.getDeploymentId());
		model.addAttribute("imageName", pd.getDiagramResourceName());
		model.addAttribute("acs", map);
		
		return "viewimage";
	}
	//�鿴��������ͼ
	@RequestMapping("viewCurrentImageByBill")
	public String viewCurrentImageByBill(String billId, ModelMap model) {
		
		String Businesskey = Constants.Leave_KEY + "." + billId;
		
		ProcessDefinition pd =this.workflowService.findProcessDefinitionByBillId(Businesskey);
		Task task = this.workflowService.selectTaskByBusinessKey(Businesskey);
		String taskId = task.getId();
		Map<String, Object> map = this.workflowService.findCordingById(taskId);
		
		model.addAttribute("deploymentId", pd.getDeploymentId());
		model.addAttribute("imageName", pd.getDiagramResourceName());
		model.addAttribute("acs", map);
		
		return "viewimage";
	}
	//����ͼ
	@RequestMapping("viewImage")
	public void viewImage(String deploymentId, String imageName, HttpServletResponse response) throws IOException {
		InputStream inputStream = this.workflowService.viewImage(deploymentId, imageName);
		
		OutputStream out = response.getOutputStream();
		
		for(int b = -1;(b=inputStream.read()) != -1;) {
			out.write(b);
		}
		
		out.close();
		inputStream.close();
	}

	
//	@RequestMapping("/myleaveBill")
//	public String myleaveBill(HttpSession session, Model model) {
//		Employee emp = (Employee) session.getAttribute(Constants.GLOBLE_USER_SESSION);
//		
//		List<Leavebill> list = this.leaveBillService.selectLeaveBill(emp.getId());
//		
//		model.addAttribute("leavebill", list);
//		
//		return "leavebill";
//	}
	/*
	 * �鿴����processList
	 */	
	@RequestMapping("/processDefinitionList")
	public String processDefinitionList(Model model) {
		
		List<ProcessDefinition> list = this.workflowService.selectDefinitionProcess();
		
		List<Deployment> delist = this.workflowService.selectDeploymentList();
		
		model.addAttribute("pdList", list);
		
		model.addAttribute("depList", delist);
		return "workflow_list";
		
	}
	//ɾ����������¼
	@RequestMapping("leaveBillAction_delete")
	@ResponseBody
	public Map<String,Object> deleteBaoxiaoBill(String id) {
		Map<String,Object> map = new HashMap<String,Object>();
		Integer result = this.workflowService.deleteBaoxiaoBillByBillId(id);
		if(result==1) {
			map.put("result", 1);
		}else {
			map.put("result", 0);
		}
		return map;
	}
	//�鿴��ʷ����
	@RequestMapping("viewHisComment")
	public String viewHisComment(String id, Model model) {
		String businesssKey = Constants.Leave_KEY + "." + id;
		Baoxiaobill bill = null;	
		List<Baoxiaobill> billList = this.baoXiaoBillService.selectBaoxiaoBill(Integer.parseInt(id));
		if(billList.size()>0) {
			bill = billList.get(0);
		}
		model.addAttribute("baoxiaoBill", bill);
		
		List<Comment> hisComments = this.workflowService.viewHisCommentByBillId(businesssKey);
		model.addAttribute("commentList", hisComments);
		return "workflow_commentlist";
	}
	
//	@RequestMapping("/findUserList")
//	public String categroyList(Model model) {
//		List<EmployeeCustom> list = this.workflowService.selectEmpList();
//		model.addAttribute("userList", list);
//		return "userlist";
//	}
}
