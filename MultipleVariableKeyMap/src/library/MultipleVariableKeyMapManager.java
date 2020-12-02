package library;

import java.util.Map;

import library.exception.ExceptionAlreadyExist;
import library.exception.ExceptionNotFound;

public interface MultipleVariableKeyMapManager {
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