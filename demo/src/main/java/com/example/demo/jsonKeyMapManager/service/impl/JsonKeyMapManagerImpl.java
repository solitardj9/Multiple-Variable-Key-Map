package com.example.demo.jsonKeyMapManager.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.example.demo.jsonKeyMapManager.service.JsonKeyMapManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jayway.jsonpath.JsonPath;

@Service("jsonKeyMapManager")
public class JsonKeyMapManagerImpl implements JsonKeyMapManager {

	private Map<JsonObject, Object> map = new ConcurrentHashMap<>();
	
	private Gson gson = new Gson();
	
	@Override
	public void put(String jsonKey, Object object) {
		//
		map.put(makeJsonKey(jsonKey), object);
	}
	
	@Override
	public Object get(String jsonKey) {
		//
		return map.get(makeJsonKey(jsonKey));
	}
	
	@Override
	public Map<String, Object> find(String predicate) {
		// TODO :
		Set<JsonObject> keySet = map.keySet();
		String strKeySet = gson.toJson(keySet);
		
		Object object = JsonPath.parse(strKeySet).read(predicate);
		//System.out.println("object = " + object);
		
		String strObject = gson.toJson(object);
		//System.out.println("strObject = " + strObject);
		JsonArray jsonArray = gson.fromJson(strObject, JsonArray.class);
		//System.out.println("jsonArray = " + jsonArray);
		
		Map<String, Object> ret = new HashMap<>();
		for (JsonElement jeIter : jsonArray) {
		    JsonObject selectedJsonkey = jeIter.getAsJsonObject();
		    ret.put(selectedJsonkey.toString(), map.get(selectedJsonkey));
		}
		
		return ret;
	}
	
	private JsonObject makeJsonKey(String jsonKey) {
		//
		JsonObject jsonObject = gson.fromJson(jsonKey, JsonObject.class);
		return jsonObject;
	}
	
	@Override
	public String toString() {
		return map.toString();
	}
}