package com.gec.service;

import java.util.List;

import com.gec.pojo.Baoxiaobill;


public interface BaoXiaoBillService {
	
	//���汨����
	public void saveBaoxiaoBill(Baoxiaobill baoxiaobill);
	//��ѯ������
	public List<Baoxiaobill> selectBaoxiaoBill(Integer id);
	
	
}
