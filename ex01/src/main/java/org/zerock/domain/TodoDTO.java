package org.zerock.domain;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

// java.sql.Date는 java.util.Date를 상속받으며 시/분/초 정보까지 확인하려면 java.util.Date를 import해야한다. 
@Data
public class TodoDTO {
	private String title;

	// @InitBinder 처리 대신 하는 아주 간단한 방법은 아래와 같이 추가해주는 것이다.
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dueDate;
}
