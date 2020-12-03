package com.example.demo.jsonKeyMapManager.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
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
			//--------------------------------------------------
			Long startTime = System.nanoTime();
			//--------------------------------------------------
			Map<JsonObject, Object> map = repository.get(mapName);
			//--------------------------------------------------
			Long endTime = System.nanoTime();
			Long diffTime = endTime - startTime;
			logger.info("diffTime : " + diffTime + " in (ns)" + " / " + diffTime/1000000.0 + " in (ms)");
			//--------------------------------------------------
			
			//--------------------------------------------------
			Long startTime1 = System.nanoTime();
			//--------------------------------------------------
			Set<JsonObject> keySet = map.keySet();
			String strKeySet = gson.toJson(keySet);
			
			// https://stackoverflow.com/questions/43200432/how-to-get-filtered-json-nodes-through-gson
			JsonElement element = gson.toJsonTree(keySet);
			JsonArray array = element.getAsJsonArray();
			System.out.println("array = " + array.toString());
			
			Stream<JsonObject> filteredArray = StreamSupport.stream(array.spliterator(), false)
            			 .map(JsonElement::getAsJsonObject)
            			 //.filter(jsonObject -> jsonObject.get("key").getAsString().equals("3")
            			 .filter(jsonObject -> jsonObject.get("age").getAsDouble() >= 50
            					);
			filteredArray.forEach(System.out::println);
    //String predicate = "$.[?(@.age >= {age})]";
			
			//--------------------------------------------------
			Long endTime1 = System.nanoTime();
			Long diffTime1 = endTime1 - startTime1;
			logger.info("diffTime : " + diffTime1 + " in (ns)" + " / " + diffTime1/1000000.0 + " in (ms)");
			//--------------------------------------------------
			
			//--------------------------------------------------
			Long startTime2 = System.nanoTime();
			//--------------------------------------------------
			DocumentContext dc = JsonPath.parse(strKeySet);
			Object object = dc.read(predicate);
			//--------------------------------------------------
			Long endTime2 = System.nanoTime();
			Long diffTime2 = endTime2 - startTime2;
			logger.info("diffTime : " + diffTime2 + " in (ns)" + " / " + diffTime2/1000000.0 + " in (ms)");
			//--------------------------------------------------
			
			//--------------------------------------------------
			Long startTime3 = System.nanoTime();
			//--------------------------------------------------
			String strObject = gson.toJson(object);
			JsonArray jsonArray = gson.fromJson(strObject, JsonArray.class);
			//--------------------------------------------------
			Long endTime3 = System.nanoTime();
			Long diffTime3 = endTime3 - startTime3;
			logger.info("diffTime : " + diffTime3 + " in (ns)" + " / " + diffTime3/1000000.0 + " in (ms)");
			//--------------------------------------------------
			
			//--------------------------------------------------
			Long startTime4 = System.nanoTime();
			//--------------------------------------------------
			Map<String, Object> ret = new HashMap<>();
			for (JsonElement jeIter : jsonArray) {
			    JsonObject selectedJsonkey = jeIter.getAsJsonObject();
			    ret.put(selectedJsonkey.toString(), map.get(selectedJsonkey));
			}
			//--------------------------------------------------
			Long endTime4 = System.nanoTime();
			Long diffTime4 = endTime4 - startTime4;
			logger.info("diffTime : " + diffTime4 + " in (ns)" + " / " + diffTime4/1000000.0 + " in (ms)");
			//--------------------------------------------------
			
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