package com.gec.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gec.mapper.BaoxiaobillMapper;
import com.gec.pojo.Baoxiaobill;
import com.gec.pojo.BaoxiaobillExample;
import com.gec.pojo.BaoxiaobillExample.Criteria;
import com.gec.service.BaoXiaoBillService;

@Service
public class BaoXiaoBillServiceImpl implements BaoXiaoBillService {

	@Autowired
	private BaoxiaobillMapper baoxiaobillMapper;
		
	@Override
	public void saveBaoxiaoBill(Baoxiaobill baoxiaobill) {
		
		if(baoxiaobill.getId() != null) {
			this.baoxiaobillMapper.updateByPrimaryKey(baoxiaobill);
		}else {
			this.baoxiaobillMapper.insert(baoxiaobill);
		}
		

	}

	@Override
	public List<Baoxiaobill> selectBaoxiaoBill(Integer id) {
		BaoxiaobillExample bx = new BaoxiaobillExample();
		Criteria criteria = bx.createCriteria();
		criteria.andIdEqualTo(id);
		List<Baoxiaobill> list = this.baoxiaobillMapper.selectByExample(bx);
		if(list.size() > 0) {
			return list;
		}
		return null;
	}



	
}
