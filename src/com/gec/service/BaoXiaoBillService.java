package com.gec.service;

import java.util.List;

import com.gec.pojo.Baoxiaobill;


public interface BaoXiaoBillService {
	
	//保存报销单
	public void saveBaoxiaoBill(Baoxiaobill baoxiaobill);
	//查询报销单
	public List<Baoxiaobill> selectBaoxiaoBill(Integer id);
	
	
}
