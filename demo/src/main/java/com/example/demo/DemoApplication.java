package com.example.demo;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.example.demo.jsonKeyMapManager.model.exception.ExceptionAlreadyExist;
import com.example.demo.jsonKeyMapManager.model.exception.ExceptionBadRequest;
import com.example.demo.jsonKeyMapManager.model.exception.ExceptionNotFound;
import com.example.demo.jsonKeyMapManager.service.JsonKeyMapManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class DemoApplication {
	
	private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

	public static void main(String[] args) {
		
		ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);
		
		JsonKeyMapManager jsonKeyMapManager = (JsonKeyMapManager)context.getBean("jsonKeyMapManager");
		
		ObjectMapper om = new ObjectMapper();
		Random random = new Random();

		System.out.println("//--------------------------------------------------------------------------------------------------------");
		
		String mapName = "testMap";
		
		try {
			jsonKeyMapManager.addMap(mapName);
			
			System.out.println("// 1st insert ---------------------------------------------------------");
			for (Integer i = 0 ; i < 3 ; i++) {
				//MyClass myClass = new MyClass(i.toString(), UUID.randomUUID().toString(), random.nextInt(100), Boolean.valueOf(i.toString()).toString(), random.nextInt(200));
				MyClass myClass = new MyClass(i.toString(), "sadfwstw4t4e", 1, Boolean.valueOf(i.toString()).toString(), 1);
				try {
					String jsonKey = om.writeValueAsString(myClass);
					jsonKeyMapManager.put(mapName, jsonKey, "test value");
				} catch (JsonProcessingException e) {
					logger.error("[DemoApplication].main = " + e);
				}
			}
			
			System.out.println(jsonKeyMapManager.toString());
			
			Map<String, Object> result = jsonKeyMapManager.find(mapName, "$.[?(@.key != '0' && @.sex == 'false')]");
			System.out.println("result = " + result.toString());
	
			System.out.println("// 2nd update ---------------------------------------------------------");
			for (Integer i = 0 ; i < 3 ; i++) {
				//MyClass myClass = new MyClass(i.toString(), UUID.randomUUID().toString(), random.nextInt(100), Boolean.valueOf(i.toString()).toString(), random.nextInt(200));
				MyClass myClass = new MyClass(i.toString(), "sadfwstw4t4e", 1, Boolean.valueOf(i.toString()).toString(), 1);
				try {
					String jsonKey = om.writeValueAsString(myClass);
					jsonKeyMapManager.put(mapName, jsonKey, "test value 2");
				} catch (JsonProcessingException e) {
					logger.error("[DemoApplication].main = " + e);
				}
			}
			
			System.out.println(jsonKeyMapManager.toString());
			
			result = jsonKeyMapManager.find(mapName, "$.[?(@.key != '0' && @.sex == 'false')]");
			System.out.println("result = " + result.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("//--------------------------------------------------------------------------------------------------------");
		System.out.println();
		System.out.println("//--------------------------------------------------------------------------------------------------------");
		
		mapName = "testMap2";
		
		try {
			logger.info("test 1) addMap : " + jsonKeyMapManager.addMap(mapName).toString());
			
			//for (Integer i = 0 ; i < 10000 ; i++) {
			for (Integer i = 0 ; i < 10 ; i++) {
				MyClass myClass = new MyClass(i.toString(), UUID.randomUUID().toString(), random.nextInt(100), Boolean.valueOf(i.toString()).toString(), random.nextInt(200));
				try {
					String strMyClass = om.writeValueAsString(myClass);
					jsonKeyMapManager.put(mapName, strMyClass, myClass.toString());
					
					try {
						logger.info("key : " + strMyClass + ", stored data" + jsonKeyMapManager.get(mapName, strMyClass));
					} catch (ExceptionNotFound e) {
						logger.error(e.toString());
					}
				} catch (Exception e) {
					logger.error(e.toString());
				}
			}
			System.out.println("size = " + jsonKeyMapManager.size(mapName));
		} catch (ExceptionAlreadyExist | ExceptionNotFound e) {
			logger.error(e.toString());
		}
		System.out.println("test???");
		
		Integer testCase = 10;
		for (Integer i = 0 ; i < testCase ; i++) {
			//
			String predicate = "$.[?(@.age >= {age})]";
			Integer age = random.nextInt(100);
			predicate = predicate.replace("{age}", age.toString());
			System.out.println("predicate = " + predicate);
			
			Long startTime = System.nanoTime();
			
			//logger.info("index : " + i + " / filter : " + filter + " / ");
			try {
				Map<String, Object> ret = jsonKeyMapManager.find(mapName, predicate);
				logger.info("result map size : " + ret.size());
			} catch (ExceptionNotFound e) {
				logger.error(e.toString());
			}
			
			Long endTime = System.nanoTime();
			Long diffTime = endTime - startTime;
			logger.info("diffTime : " + diffTime + " in (ns)" + " / " + diffTime/1000000.0 + " in (ms)");
		}
		
		System.out.println("//--------------------------------------------------------------------------------------------------------");
	}
}

class MyClass {
	private String key;
	private String name;
	private Integer age;
	private String sex;
	private Integer height;
	
	public MyClass(String key, String name, Integer age, String sex, Integer height) {
		this.key = key;
		this.name = name;
		this.age = age;
		this.sex = sex;
		this.height = height;
	}
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getAge() {
		return age;
	}
	
	public void setAge(Integer age) {
		this.age = age;
	}
	
	public String getSex() {
		return sex;
	}
	
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	public Integer getHeight() {
		return height;
	}
	
	public void setHeight(Integer height) {
		this.height = height;
	}

	@Override
	public String toString() {
		return "MyClass [key=" + key + ", name=" + name + ", age=" + age + ", sex=" + sex + ", height=" + height + "]";
	}
}