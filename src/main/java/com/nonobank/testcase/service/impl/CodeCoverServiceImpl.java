package com.nonobank.testcase.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nonobank.testcase.component.exception.TestCaseException;
import com.nonobank.testcase.component.result.ResultCode;
import com.nonobank.testcase.entity.CodeCover;
import com.nonobank.testcase.repository.CodeCoverRepository;
import com.nonobank.testcase.service.CodeCoverService;

@Service
public class CodeCoverServiceImpl implements CodeCoverService {
	
	@Autowired
	CodeCoverRepository codeCoverRepository;

	@Override
	public CodeCover add(CodeCover codeCover) {
		// TODO Auto-generated method stub
		if(codeCover.getId() == null){
			CodeCover cc =codeCoverRepository.findBySystemAndBranch(codeCover.getSystem(), codeCover.getBranch());
			
			if(null != cc){
				throw new TestCaseException(ResultCode.VALIDATION_ERROR.getCode(), "已存在相同的system、branch");
			}
		}
		return codeCoverRepository.save(codeCover);
	}

	@Override
	public void delete(CodeCover codeCover) {
		// TODO Auto-generated method stub
		codeCoverRepository.delete(codeCover);
	}

	@Override
	public void createReport(CodeCover codeCover) {
		// TODO Auto-generated method stub

	}

	@Override
	public void viewReport(CodeCover codeCover) {
		// TODO Auto-generated method stub
	}

	@Override
	public List<CodeCover> getAll() {
		// TODO Auto-generated method stub
		return codeCoverRepository.findAll();
	}

}
