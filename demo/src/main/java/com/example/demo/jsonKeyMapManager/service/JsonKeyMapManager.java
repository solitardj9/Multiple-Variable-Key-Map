package com.example.demo.jsonKeyMapManager.service;

import java.util.Map;

import com.example.demo.jsonKeyMapManager.model.exception.ExceptionAlreadyExist;
import com.example.demo.jsonKeyMapManager.model.exception.ExceptionNotFound;

public interface JsonKeyMapManager {
	//
	public Boolean addMap(String mapName) throws ExceptionAlreadyExist;
	
	public Boolean deleteMap(String mapName) throws ExceptionNotFound;
	
	public Object/*previous value*/ put(String mapName, String jsonKey, Object value) throws ExceptionNotFound;
	
	public Object get(String mapName, String jsonKey) throws ExceptionNotFound;
	
	public Map<String, Object> getMap(String mapName) throws ExceptionNotFound;
	
	public Map<String, Object> find(String mapName, String predicate) throws ExceptionNotFound;
	
	public Object remove(String mapName, String jsonKey) throws ExceptionNotFound;
	
	public void clear(String mapName) throws ExceptionNotFound;
	
	public void remove(String mapName) throws ExceptionNotFound;
	
	public void clear();
	
	public int size(String mapName) throws ExceptionNotFound;
	
	public String toString(String mapName);
}