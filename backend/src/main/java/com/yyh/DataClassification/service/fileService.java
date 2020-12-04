package com.yyh.DataClassification.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.yyh.DataClassification.dto.UploadFile;
import com.yyh.DataClassification.repository.FileRepository;

@Service
public class fileService {

	private static final Logger logger = LoggerFactory.getLogger(fileService.class);

	@Autowired
	FileRepository fileRepository;

	@Transactional
	public UploadFile storeFile(MultipartFile file) throws Exception {
		if(checkFileExists(file.getOriginalFilename())) return null;
		SimpleDateFormat format1 = new SimpleDateFormat("yyMMddHHmmss");
		String time1 = format1.format(new Date());
		String filename = file.getOriginalFilename();
		String fileurl = "http://localhost:8082//DataClassification/files/openmarket_goods/" + filename;
		String repositoryUrl = "C:\\Users\\Yun_YoungHyun\\dananuo\\openmarket_goods/" + filename;
		File dest = new File(repositoryUrl);
		file.transferTo(dest);
		UploadFile uf = UploadFile.builder().fileName(filename).fileUri(fileurl).fileDate(time1).fileStatus("TODO").build();
		UploadFile saved = fileRepository.save(uf);
		return saved;
	}

	@Transactional
	public boolean removeFile(int id) throws Exception {
		Optional<UploadFile> opt_curFile = fileRepository.findById(id);
		UploadFile curFile = opt_curFile.get();
		logger.info("file: " + curFile.getFileName());
		File file = new File("C:\\Users\\Yun_YoungHyun\\dananuo\\openmarket_goods/" + curFile.getFileName());
		if (file.delete()) {
			fileRepository.deleteById(curFile.getId());
			logger.info("파일 삭제 성공!");
			return true;
		} else {
			logger.error("파일 삭제 실패");
			return false;
		}
	}

	public List<UploadFile> getFiles() throws Exception {
		return fileRepository.findAll();
	}

	public boolean checkFileExists(String name) throws Exception {
		Optional<UploadFile> opt_curFile = fileRepository.findByFileName(name);
		if (opt_curFile.isPresent()) {
			return true;
		} else {
			return false;
		}
	}
}
