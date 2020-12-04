package com.yyh.DataClassification.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yyh.DataClassification.dto.UploadFile;

@Repository
public interface FileRepository extends JpaRepository<UploadFile, Integer>{

	Optional<UploadFile> findByFileName(String name);

}
