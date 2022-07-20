package org.zerock.domain;

import lombok.Data;

// DTO(Data Transfer Object)
// 데이터 저장 담당 클래스이다. Controller, Service, View처럼 계층 간의 데이터 교환을 위해 쓰인다. 로직을 갖고 있지 않으며 순수한 데이터 객체이며 getter, setter 메소드만을 갖고 있다.
// @Data - getter/setter/equals()/toString()을 자동 생성해준다.
@Data
public class SampleDTO {
	private String name;
	private int age;
}
