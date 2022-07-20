package org.zerock.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;


// 객체 타입을 여러 개를 처리햐아 한다면 SampleDTO를 포함하는 SampleDTOList를 작성하여 처리한다.
@Data
public class SampleDTOList {
	
	private List<SampleDTO> list;
	
	public SampleDTOList() {
		list = new ArrayList<>();
	}
}
