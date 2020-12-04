package com.yyh.DataClassification.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.yyh.DataClassification.dto.BasicResponse;
import com.yyh.DataClassification.dto.UploadFile;
import com.yyh.DataClassification.service.fileService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/main")
public class mainController {
	
	private static final Logger logger = LoggerFactory.getLogger(mainController.class);
	
	@Autowired
	fileService fileservice;
	
	@ApiOperation("파일목록을 조회한다")
	@GetMapping("/file")
	public ResponseEntity<?> getFiles() throws Exception {
		ResponseEntity response = null;
		final BasicResponse result = new BasicResponse();
		List<UploadFile> files = fileservice.getFiles();
		result.status = true;
		result.data = "success";
		result.object = files;
		response = new ResponseEntity<>(result, HttpStatus.OK);
		return response;
	}
	
	@ApiOperation("파일을 등록한다")
	@PostMapping("/file")
	public ResponseEntity<?> registerFile(@RequestParam("file") MultipartFile file) throws Exception {
		ResponseEntity response = null;
		final BasicResponse result = new BasicResponse();
		UploadFile file_info = fileservice.storeFile(file);
		if(file_info == null) {
			logger.error("file already exist!");
			result.status = false;
			result.data = "fail";
			response = new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			logger.info(file_info.getFileName() + "is uploaded.");
			result.status = true;
			result.data = "success";
			result.object = file_info;
			response = new ResponseEntity<>(result, HttpStatus.OK);
		}
		return response;
		
	}
	
	@ApiOperation("파일을 삭제한다.")
	@DeleteMapping("/file/{id}")
	public ResponseEntity<?> deleteFile(@PathVariable("id") int id) throws Exception {
		ResponseEntity response = null;
		final BasicResponse result = new BasicResponse();
		if(fileservice.removeFile(id)) {
			result.status = true;
			result.data = "success";
			response = new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			result.status = false;
			result.data = "fail";
			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
		}
		return response;
	}
}
