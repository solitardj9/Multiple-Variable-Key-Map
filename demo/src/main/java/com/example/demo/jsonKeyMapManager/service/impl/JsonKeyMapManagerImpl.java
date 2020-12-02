package com.example.demo.jsonKeyMapManager.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.jsonKeyMapManager.model.exception.ExceptionAlreadyExist;
import com.example.demo.jsonKeyMapManager.model.exception.ExceptionNotFound;
import com.example.demo.jsonKeyMapManager.service.JsonKeyMapManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

@Service("jsonKeyMapManager")
public class JsonKeyMapManagerImpl implements JsonKeyMapManager {

	private static final Logger logger = LoggerFactory.getLogger(JsonKeyMapManagerImpl.class);
	
	/* main  repository */
	private Map<String, Map<JsonObject, Object>> repository = new ConcurrentHashMap<>();
	
	private static Gson gson = new Gson();
	
	@Override
	public Boolean addMap(String mapName) throws ExceptionAlreadyExist {
		//
		if (repository.containsKey(mapName)) {
			throw new ExceptionAlreadyExist(mapName + " is already exist");
		}
		else {
			repository.put(mapName, new ConcurrentHashMap<>());
			return true;
		}
	}
	
	@Override
	public Boolean deleteMap(String mapName) throws ExceptionNotFound {
		//
		if (!repository.containsKey(mapName)) {
			throw new ExceptionNotFound(mapName + " is not found");
		}
		else {
			repository.remove(mapName);
			return true;
		}
	}
	
	@Override
	public Object put(String mapName, String jsonKey, Object value) throws ExceptionNotFound {
		//
		if (!repository.containsKey(mapName)) {
			throw new ExceptionNotFound(mapName + " is not found");
		}
		else {
			return repository.get(mapName).put(makeJsonKey(jsonKey), value);	
		}
	}
	
	@Override
	public Object get(String mapName, String jsonKey) throws ExceptionNotFound {
		//
		if (!repository.containsKey(mapName)) {
			throw new ExceptionNotFound(mapName + " is not found");
		}
		else {
			return repository.get(mapName).get(makeJsonKey(jsonKey));
		}
	}

	@Override
	public Map<String, Object> getMap(String mapName) throws ExceptionNotFound {
		//
		if (!repository.containsKey(mapName)) {
			throw new ExceptionNotFound(mapName + " is not found");
		}
		else {
			Map<JsonObject, Object> map = repository.get(mapName);
			
			Map<String, Object> retMap = new HashMap<>();
			for (Entry<JsonObject, Object> iter : map.entrySet()) {
				retMap.put(iter.getKey().toString(), iter.getValue());
			}
			
			return retMap;
		}
	}
	
	public Map<String, Object> find(String mapName, String predicate) throws ExceptionNotFound {
		//
		if (!repository.containsKey(mapName)) {
			throw new ExceptionNotFound(mapName + " is not found");
		}
		else {
			Map<JsonObject, Object> map = repository.get(mapName);
			
			Set<JsonObject> keySet = map.keySet();
			String strKeySet = gson.toJson(keySet);
			
			DocumentContext dc = JsonPath.parse(strKeySet);
			Object object = dc.read(predicate);
			
			String strObject = gson.toJson(object);
			JsonArray jsonArray = gson.fromJson(strObject, JsonArray.class);
			
			Map<String, Object> ret = new HashMap<>();
			for (JsonElement jeIter : jsonArray) {
			    JsonObject selectedJsonkey = jeIter.getAsJsonObject();
			    ret.put(selectedJsonkey.toString(), map.get(selectedJsonkey));
			}
			
			return ret;
		}
	}
	
	private JsonObject makeJsonKey(String jsonKey) {
		//
		JsonObject jsonObject = gson.fromJson(jsonKey, JsonObject.class);
		return jsonObject;
	}
	
	@Override
	public Object remove(String mapName, String jsonKey) throws ExceptionNotFound {
		//
		if (!repository.containsKey(mapName)) {
			throw new ExceptionNotFound(mapName + " is not found");
		}
		else {
			Map<JsonObject, Object> map = repository.get(mapName);
			return map.remove(makeJsonKey(jsonKey));
		}
	}
	
	@Override
	public void clear(String mapName) throws ExceptionNotFound {
		//
		if (!repository.containsKey(mapName)) {
			throw new ExceptionNotFound(mapName + " is not found");
		}
		else {
			Map<JsonObject, Object> map = repository.get(mapName);
			synchronized (map) {
				map.clear();
			}
		}
	}
	
	@Override
	public void remove(String mapName) throws ExceptionNotFound {
		//
		if (!repository.containsKey(mapName)) {
			throw new ExceptionNotFound(mapName + " is not found");
		}
		else {
			synchronized (repository) {
				repository.remove(mapName);
			}
		}
	}
	
	@Override
	public void clear() {
		//
		synchronized (repository) {
			repository.clear();
		}
	}
	
	@Override
	public int size(String mapName) throws ExceptionNotFound {
		if (!repository.containsKey(mapName)) {
			throw new ExceptionNotFound(mapName + " is not found");
		}
		else {
			return repository.get(mapName).size();
		}
	}
	
	@Override
	public String toString() {
		return repository.toString();
	}
	
	@Override
	public String toString(String mapName) {
		return repository.get(mapName).toString();
	}
}