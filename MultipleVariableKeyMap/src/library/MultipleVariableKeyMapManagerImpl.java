package library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import library.data.MultiVarKey;
import library.exception.ExceptionAlreadyExist;
import library.exception.ExceptionBadRequest;
import library.exception.ExceptionNotFound;

public class MultipleVariableKeyMapManagerImpl implements MultipleVariableKeyMapManager {

	private static final Logger logger = LoggerFactory.getLogger(MultipleVariableKeyMapManagerImpl.class);
	
	/* main  repository */
	private Map<String, Map<String, Object>> repository = new ConcurrentHashMap<>();
	
	/* repository for fast search */
	private Map<String, Map<String/*multiple-variable-key-name*/, Map<String/*multiple-variable-key-value*/, Set<String/*key*/>>>> multipleVariableKeyRepository = new ConcurrentHashMap<>();
	
	//private static Gson gson = new Gson();
	private static Gson gson = new GsonBuilder().registerTypeAdapter(TreeMap.class, new TreeMapDeserializer()).setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();
	
	@Override
	public Boolean addMap(String mapName) throws ExceptionAlreadyExist {
		//
		if (repository.containsKey(mapName)) {
			throw new ExceptionAlreadyExist(mapName + " is already exist");
		}
		else {
			repository.put(mapName, new ConcurrentHashMap<>());
			multipleVariableKeyRepository.put(mapName, new ConcurrentHashMap<>());
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
			multipleVariableKeyRepository.remove(mapName);
			return true;
		}
	}

	@Override
	public Object put(String mapName, String key, Object value) {
		//
		Object ret = null;
		Map<String, Object> map = repository.getOrDefault(mapName, new ConcurrentHashMap<>());
		
		try {
			MultiVarKey multiVarKey = makeKey(key);
			
			String sortedKey = multiVarKey.getKey();
			
			if (map.containsKey(sortedKey)) {
				ret = map.put(sortedKey, value);
				repository.put(mapName, map);
			}
			else {
				ret = map.put(sortedKey, value);
				repository.put(mapName, map);
				
				Map<String/*multiple-variable-key-name*/, Map<String/*multiple-variable-key-value*/, Set<String/*key*/>>> multipleVariableKeyMap = multipleVariableKeyRepository.getOrDefault(mapName, new ConcurrentHashMap<>());
				for (Entry<String, Object> iter : multiVarKey.getKeyMap().entrySet()) {
					String keyName = iter.getKey();
					String keyValue = iter.getValue().toString();
					
					Map<String/*multiple-variable-key-value*/, Set<String/*key*/>> multipleVariableKeyValuesAndKey = multipleVariableKeyMap.getOrDefault(keyName, new ConcurrentHashMap<>());
					Set<String> keys = multipleVariableKeyValuesAndKey.getOrDefault(keyValue, new ConcurrentSkipListSet<>());
					keys.add(sortedKey);
					
					multipleVariableKeyValuesAndKey.put(keyValue, keys);
					
					multipleVariableKeyMap.put(keyName, multipleVariableKeyValuesAndKey);
				}
				multipleVariableKeyRepository.put(mapName, multipleVariableKeyMap);
			}
		} catch (ExceptionBadRequest e) {
			logger.error(e.toString());
			return null;
		}
		
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	private MultiVarKey makeKey(String key) throws ExceptionBadRequest {
		//
		try {
			TreeMap<String, Object> keyMap = gson.fromJson(key, TreeMap.class);
			
			for (Entry<String, Object> iter : keyMap.entrySet()) {
				Object keyValue = iter.getValue();
				if (!isValidType(keyValue)) {
					throw new ExceptionBadRequest("Key inclueds invalid data type : " + iter.getKey() + ", " + keyValue.getClass());
				}
			}
			
			if (keyMap.isEmpty()) {
				throw new ExceptionBadRequest("key is empty");
			}
			
			MultiVarKey multiVarKey = new MultiVarKey(keyMap, gson.toJson(keyMap));
			return multiVarKey;
		} catch (Exception e) {
			throw new ExceptionBadRequest(e.toString());
		}
	}
	
	private Boolean isValidType(Object object) {
		//
		if (object instanceof Integer || object instanceof Long || object instanceof Double || object instanceof Float || object instanceof String) {
			return true;
		}
		return false;
	}
	

	@Override
	public Object getByKey(String mapName, String key) throws ExceptionNotFound, ExceptionBadRequest {
		//
		if (!repository.containsKey(mapName)) {
			throw new ExceptionNotFound(mapName + " is not found");
		}
		else {
			Map<String, Object> map = repository.get(mapName);
			Object ret = null;
			try {
				ret = map.get(makeKey(key).getKey());
			} catch (ExceptionBadRequest e) {
				throw new ExceptionBadRequest(e.getMessage());
			}
			return ret;
		}
	}

	@Override
	public Map<String, Object> getByFilter(String mapName, String filter) throws ExceptionNotFound, ExceptionBadRequest {
		//
		try {
			if (!multipleVariableKeyRepository.containsKey(mapName)) {
				throw new ExceptionNotFound(mapName + " is not found");
			}
			else {
				Set<String> intersectionSet = getFilteredKeys(mapName, filter);

				if (intersectionSet != null) {
					Map<String, Object> retMap = new HashMap<>();
					for (String iter : intersectionSet) {
						retMap.put(iter, repository.get(mapName).get(iter));
					}
					return retMap;
				}
				else {
					return null;
				}
			}
		} catch (ExceptionBadRequest e) {
			throw new ExceptionBadRequest(e.getMessage());
		}
	}

	@Override
	public Map<String, Object> getAll(String mapName) throws ExceptionNotFound {
		//
		if (!repository.containsKey(mapName)) {
			throw new ExceptionNotFound(mapName + " is not found");
		}
		else {
			Map<String, Object> map = repository.get(mapName);
			Map<String, Object> retMap = new HashMap<>(map);
			return retMap;
		}
	}

	@Override
	public Object removeByKey(String mapName, String key) throws ExceptionNotFound, ExceptionBadRequest {
		//
		try {
			if (!repository.containsKey(mapName)) {
				throw new ExceptionNotFound(mapName + " is not found");
			}
			else {
				MultiVarKey multiVarKey = makeKey(key);
				String sortedKey = multiVarKey.getKey();
				
				Map<String/*multiple-variable-key-name*/, Map<String/*multiple-variable-key-value*/, Set<String/*key*/>>> multipleVariableKeyMap = multipleVariableKeyRepository.get(mapName);
				for (Entry<String, Object> iter : multiVarKey.getKeyMap().entrySet()) {
					String keyName = iter.getKey();
					String keyValue = iter.getValue().toString();
					
					Map<String/*multiple-variable-key-value*/, Set<String/*key*/>> multipleVariableKeyValuesAndKey = multipleVariableKeyMap.get(keyName);
					multipleVariableKeyValuesAndKey.get(keyValue).remove(sortedKey);
					
					multipleVariableKeyMap.put(keyName, multipleVariableKeyValuesAndKey);
				}
				multipleVariableKeyRepository.put(mapName, multipleVariableKeyMap);
				
				return repository.get(mapName).remove(sortedKey);
			}
		} catch (ExceptionBadRequest e) {
			throw new ExceptionBadRequest(e.getMessage());
		}
	}

	@Override
	public Map<String, Object> removeByFilter(String mapName, String filter) throws ExceptionNotFound, ExceptionBadRequest {
		//
		try {
			if (!multipleVariableKeyRepository.containsKey(mapName)) {
				throw new ExceptionNotFound(mapName + " is not found");
			}
			else {
				Set<String> intersectionSet = getFilteredKeys(mapName, filter);

				if (intersectionSet != null) {
					Map<String, Object> retMap = new HashMap<>();
					for (String iter : intersectionSet) {
						retMap.put(iter, repository.get(mapName).get(iter));
						repository.get(mapName).remove(iter);
					}
					return retMap;
				}
				else {
					return null;
				}
			}
		} catch (ExceptionBadRequest e) {
			throw new ExceptionBadRequest(e.getMessage());
		}
	}
	
	private Set<String> getFilteredKeys(String mapName, String filter) throws ExceptionBadRequest {
		//
		try {
			MultiVarKey filterKey = makeKey(filter);
			TreeMap<String, Object> filterKeyMap = filterKey.getKeyMap();
			
			List<Set<String>> keys = new ArrayList<>();
			Map<String/*multiple-variable-key-name*/, Map<String/*multiple-variable-key-value*/, Set<String/*key*/>>> multipleVariableKeyMap = multipleVariableKeyRepository.get(mapName);
			for (Entry<String, Object> iter : filterKeyMap.entrySet()) {
				Map<String/*multiple-variable-key-value*/, Set<String/*key*/>> keyValueMap = multipleVariableKeyMap.get(iter.getKey());
				if (keyValueMap != null) {
					String value = iter.getValue().toString();
					Set<String> keySet = keyValueMap.get(value);
					keys.add(keySet);
				}
			}
			
			Set<String> intersectionSet = null;
			if (keys.size() == 1) {
				intersectionSet = keys.get(0);
			}
			else if (keys.size() > 1) {
				intersectionSet = keys.get(0);
				for (int i = 1 ; i < keys.size() ; i++) {
					intersectionSet = Sets.intersection(intersectionSet, keys.get(i));
				}
			}
			else {
				return null;
			}
			return intersectionSet;
		} catch (ExceptionBadRequest e) {
			throw new ExceptionBadRequest(e.getMessage());
		}
	}
}