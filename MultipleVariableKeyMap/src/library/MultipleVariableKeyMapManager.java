package library;

import java.util.Map;

import library.exception.ExceptionAlreadyExist;
import library.exception.ExceptionBadRequest;
import library.exception.ExceptionNotFound;

public interface MultipleVariableKeyMapManager {
	//
	public Boolean addMap(String mapName) throws ExceptionAlreadyExist;
	
	public Map<String/*key*/, Object/*current value*/> getMap(String mapName) throws ExceptionNotFound;
	
	public Boolean deleteMap(String mapName) throws ExceptionNotFound;
	
	public Object/*previous value*/ put(String mapName, String key, Object value);
	
	public Object/*current value*/ getByKey(String mapName, String key) throws ExceptionNotFound, ExceptionBadRequest;
	
	public Map<String/*key*/, Object/*current value*/> getByFilter(String mapName, String filter) throws ExceptionNotFound, ExceptionBadRequest;
	
	public Map<String/*key*/, Object/*current value*/> getAll(String mapName) throws ExceptionNotFound;
	
	public Object/*previous value*/ removeByKey(String mapName, String key) throws ExceptionNotFound, ExceptionBadRequest;
	
	public Map<String/*key*/, Object/*current value*/> removeByFilter(String mapName, String filter) throws ExceptionNotFound, ExceptionBadRequest;
}