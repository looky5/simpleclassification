package com.yyh.DataClassification.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity(name = "csv_files")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Builder
@DynamicInsert
public class UploadFile {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성을 db에 위임
	@Column
	private int id;
	
	@Column(name = "file_name")
	@NonNull
	private String fileName;
	
	@Column(name = "file_uri")
	@NonNull
	private String fileUri;
	
	@Column(name = "file_date")
	@NonNull
	private String fileDate;
	
	@Column(name = "file_status")
	private String fileStatus;
	
	
}
