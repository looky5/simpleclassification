package com.yyh.DataClassification.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
public class UploadFile {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성을 db에 위임
	@Column
	private int id;
	
	@Column
	@NonNull
	private String file_name;
	
	@Column
	@NonNull
	private String file_uri;
	
	@Column
	@NonNull
	private String file_date;
	
	@Column
	@NonNull
	private String file_status;
}
