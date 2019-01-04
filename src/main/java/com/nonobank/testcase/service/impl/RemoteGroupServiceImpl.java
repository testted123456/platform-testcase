package com.nonobank.testcase.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nonobank.testcase.component.result.Result;
import com.nonobank.testcase.remotecontroller.RemoteGroup;
import com.nonobank.testcase.service.RemoteGroupService;

@Service
public class RemoteGroupServiceImpl implements RemoteGroupService {
	
	@Autowired
	RemoteGroup remoteGroup;

	@Override
	public Result isCaseInGroup(Integer caseId) {
		// TODO Auto-generated method stub
		return remoteGroup.isCaseInGroup(caseId);
	}

}
