package com.example.demo.jsonKeyMapManager.service;

import java.util.Map;

public interface JsonKeyMapManager {

	public void put(String jsonKey, Object object);
	
	public Object get(String jsonKey);
	
	public Map<String, Object> find(String predicate);
	
}