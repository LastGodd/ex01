package org.zerock.controller;

import java.util.ArrayList;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.domain.SampleDTO;
import org.zerock.domain.SampleDTOList;
import org.zerock.domain.TodoDTO;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/sample/*")
@Log4j
public class SampleController {

	@RequestMapping("")
	public void basic() {
		log.info("basic.....");
	}

	// @RequestMapping은 GET,POST, PUT, DELETE 방식 모두를 지원해야 하는 경우에 배열로 처리해서 지정할 수 있다.
	@RequestMapping(value = "/basic", method = { RequestMethod.GET })
	public void basicGet() {
		log.info("basic get.....");
	}

	// @GetMapping의 경우 오직 GET 방식에만 사용할 수 있으므로, 간편하기는 하지만 기능에 대한 제한이 많은 편.
	@GetMapping("/basicOnlyGet")
	public void basicGet2() {
		log.info("basic only get.....");
	}

	/**
	 * 파라미터 수집과 변환
	 */

	// 파라미터와 DTO의 이름이 같은 경우 자동으로 타입을 변환해서 처리해준다.
	// 객체 처리
	// http://localhost:8080/sample/ex01?name=AAA&age=111
	@GetMapping("/ex01")
	public String ex01(SampleDTO dto) {
		log.info("" + dto);

		return "ex01";
	}

	// 기본 자료형이나 문자열 등을 이용한다면 타입만 맞게 선언해주는 방식을 사용하면 된다.
	// 단일 처리
	// http://localhost:8080/sample/ex02?name=AAA&age=111
	@GetMapping("/ex02")
	public String ex02(@RequestParam("name") String name, @RequestParam("age") int age) {
		log.info("name: " + name);
		log.info("age: " + age);

		return "ex02";
	}

	// 리스트 처리
	// http://localhost:8080/sample/ex02List?ids=111&ids=222&ids=333
	@GetMapping("/ex02List")
	public String ex02List(@RequestParam("ids") ArrayList<String> ids) {
		log.info("ids: " + ids);

		return "ex02List";
	}

	// 배열 처리
	// http://localhost:8080/sample/ex02Array?ids=111&ids=222&ids=333
	@GetMapping("/ex02Array")
	public String ex02Array(@RequestParam("ids") String[] ids) {
		log.info("array ids: " + Arrays.toString(ids));

		return "ex02Array";
	}

	// 객체 리스트
	// http://localhost:8080/sample/ex02Bean?list[0].name=aaa&list[1].name=bbb <-
	// Tomcat 버전에 따라서 []문자를 특수문자로 허용하지 않을 수 있음
	// [ = %5B, ] = %5D
	// http://localhost:8080/sample/ex02Bean?list%5B0%5D.name=aaa&list%5B1%5D.name=bbb
	// http://localhost:8080/sample/ex02Bean?list%5B0%5D.name=aaa&list%5B1%5D.name=bbb&list%5B2%5D.name=ccc
	@GetMapping("/ex02Bean")
	public String ex02Bean(SampleDTOList list) {
		log.info("list dtos: " + list);

		return "ex02Bean";
	}

	// Date 타입 처리를 위한 예제로 파라미터 객체에 java.util.Date를 임폴트 한 경우 시/분/초가 포함되어야 하는데 그렇지 않은
	// 경우
	// @InitBinder 처리를 해야한다. 만일 없다면 구문에러가 발생한다.
	// http://localhost:8080/sample/ex03?title=test&dueDate=2022-07-20
	// todo: TodoDTO(title=test, dueDate=Wed Jul 20 00:00:00 KST 2022)
	@GetMapping("/ex03")
	public String ex03(TodoDTO todo) {
		log.info("todo: " + todo);
		return "ex03";
	}

	// TodoDTO에서 @DateTimeFormat을 사용하면 @InitBinder 정의하지 않아도 됨
	/*
	 * @InitBinder public void initBinder(WebDataBinder binder) { SimpleDateFormat
	 * dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	 * binder.registerCustomEditor(java.util.Date.class, new
	 * CustomDateEditor(dateFormat, false)); }
	 */

	// MVC의 Controller는 기본적으로 Java Beans 규칙에 맞는 객체만 화면으로 전달되며, 즉 생성자가 있거나
	// getter/setter를 가져야함
	// 전달 되는 것 : 생성자가 없거나 빈 생성자를 가져야 하며, getter/setter를 가진 클래스의 객체들을 의미
	// 전달될 때 클래스명의 앞 글자는 소문자로 처리 됨 (jsp or html 화면에서 ${sampleDTO} 처럼 앞 글자가 소문자가 된다는
	// 의미)
	// http://localhost:8080/sample/ex04?name=aaa&age=11&page=9
	@GetMapping("/ex04")
	public String ex04(SampleDTO dto, int page) {
		log.info("dto: " + dto);
		log.info("page: " + page);

		return "ex04";
	}

	// @ModelAttribute는 강제로 전달받은 파라미터를 Model에 담아서 전달하도록 할 때 필요한 어노테이션
	// @ModelAttribute가 걸린 파라미터는 타입에 관계없이 무조건 Model에 담아서 전달된다.
	// http://localhost:8080/sample/ex04Model?name=aaa&age=11&page=9
	@GetMapping("/ex04Model")
	public String ex04Model(SampleDTO dto, @ModelAttribute("page") int page) {
		log.info("dto: " + dto);
		log.info("page: " + page);

		return "ex04";
	}

	// Controller의 리턴 타입
	// String : jsp를 이용하는 경우 jsp 파일의 경로와 파일이름을 나타내기 위해서 사용함
	// void : 호출하는 URL과 동일한 이름의 jsp를 의미함
	// VO, DTO(객체) 타입 : 주로 JSON 타입의 데이터를 만들어서 반환하는 용도로 사용함
	// ResponseEntity 타입 : response 할 때 Http 헤더 정보와 내용을 가공하는 용도로 사용함
	// Model, ModelAndView : Model로 데이터를 반환하거나 화면까지 같이 지정하는 경우에 사용함(최근에 많이 사용하지 않음)
	// HttpHeaders : 응답에 내용 없이 Http 헤더 메시지만 전달하는 용도로 사용함
	// void : 호출하는 URL과 동일한 이름의 jsp를 의미함
	// http://localhost:8080/sample/ex05 주소가 되며 ex05.jsp 파일을 열게 됨
	@GetMapping("/ex05")
	public void ex05() {
		log.info("/ex05.....");
	}

	// String : jsp를 이용하는 경우 jsp 파일의 경로와 파일이름을 나타내기 위해서 사용함
	// http://localhost:8080/sample/ex01Example
	// 아래 처럼 로직을 짜게되면 상단의 주소로 접속하여 return을 ex01Example로 하기 때문에 동일한 화면으로 이동하게 된다.
	// String 타입은 상황에 따라 다른 화면을 보여줄 필요가 있을 경우에 유용하게 사용한다(if ~ else와 같은 처리가 필요한 상황)
	@GetMapping("/ex01Example")
	public String ex01Example(SampleDTO dto) {
		log.info("" + dto);

		return "ex01Example";
	}

	// VO, DTO(객체) 타입 : 주로 JSON 타입의 데이터를 만들어서 반환하는 용도로 사용함
	// http://localhost:8080/sample/ex06
	@GetMapping("/ex06")
	public @ResponseBody SampleDTO ex06() {
		log.info("/ex06.....");
		SampleDTO dto = new SampleDTO();
		dto.setAge(10);
		dto.setName("홍길동");

		return dto;
	}

	// ResponseEntity 타입 : response 할 때 Http 헤더 정보와 내용을 가공하는 용도로 사용함
	// ResponseEntity는 HttpHeaders 객체를 같이 전달할 수 있고, 이를 통해서 원하는 HTTP 헤더 메시지를 가공하는 것이 가능함
	// 
	@GetMapping("/ex07")
	public ResponseEntity<String> ex07() {
		log.info("/ex07.....");
		
		// {"name":"홍길동"}
		String msg = "{\"name\":\"홍길동\"}";
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "application/json;charset=UTF-8");
		
		return new ResponseEntity<String>(msg, header, HttpStatus.OK);
	}
	
	@GetMapping("/exUpload")
	public void exUpload() {
		log.info("/exUpload.....");
	}
	
	@PostMapping("/exUploadPost")
	public void exUploadPost(ArrayList<MultipartFile> files) {
		files.forEach(file-> {
			log.info("========================================");
			log.info("name: " + file.getOriginalFilename());
			log.info("size: " + file.getSize());
		});
	}
}
