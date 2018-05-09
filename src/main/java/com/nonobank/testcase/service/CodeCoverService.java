package com.nonobank.testcase.service;

import java.util.List;
import com.nonobank.testcase.entity.CodeCover;

public interface CodeCoverService {
	public List<CodeCover> getAll();

	public CodeCover add(CodeCover codeCover);
	
	public void delete(CodeCover codeCover);
	
	public void createReport(CodeCover codeCover);
	
	public void viewReport(CodeCover codeCover);
}
