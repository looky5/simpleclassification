package com.yyh.DataClassification.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.yyh.DataClassification.dto.UploadFile;
import com.yyh.DataClassification.repository.FileRepository;

@Service
public class fileService {

	@Autowired
	FileRepository fileRepository;

	@Transactional
	public UploadFile storeFile(MultipartFile file) throws Exception {
		SimpleDateFormat format1 = new SimpleDateFormat("yyMMddHHmmss");
		String time1 = format1.format(new Date());
		String filename = file.getOriginalFilename();
		String fileurl = "http://localhost:8082/DataClassification/files/" + filename;
		String repositoryUrl = "C:\\DataClassification\\files/" + filename;
		File dest = new File(repositoryUrl);
		file.transferTo(dest);
		UploadFile uf = UploadFile.builder().file_name(filename).file_uri(fileurl).file_date(time1).build();
		UploadFile saved = fileRepository.save(uf);
		return saved;
	}
	
	@Transactional
	public void removeFile(int id) {
		fileRepository.deleteById(id);
	}
	
	public List<UploadFile> getFiles() throws Exception {
		return fileRepository.findAll();
	}
}
