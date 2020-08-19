package library;

import library.exception.ExceptionAlreadyExist;
import library.exception.ExceptionBadRequest;
import library.exception.ExceptionNotFound;

public class Test {

	public static void main(String[] args) {
		//
		System.out.println("MultipleVariableKeyMap library test starts");
		
		MultipleVariableKeyMapManager manager = new MultipleVariableKeyMapManagerImpl();
		
		String mapName = "testMap";
		
		try {
			System.out.println("test 1) addMap : " + manager.addMap(mapName).toString());
			System.out.println("test 2) addMap : " + manager.addMap(mapName).toString());
		} catch (ExceptionAlreadyExist e) {
			System.out.println(e.toString());
		}
		
		String key1 = "{\"key1\":\"AAA\", \"key2\":1, \"key3\":11.3}";
		String value1st = "{\"value\":\"this is 1st test.\"}";
		String value2nd = "{\"value\":\"this is 2nd test.\"}";
		Object ret = null;
		
		ret = manager.put(mapName, key1, value1st);
		System.out.println("test 3) put : old value=" + ((ret != null) ? ret.toString() : "null"));
		
		try {
			System.out.println("test 4) getByKey : key=" + key1 + " / " + manager.getByKey(mapName, key1).toString());
		} catch (ExceptionNotFound | ExceptionBadRequest e) {
			System.out.println(e.toString());
		}
		
		ret = manager.put(mapName, key1, value2nd);
		System.out.println("test 5) put : old value=" + ((ret != null) ? ret.toString() : "null"));
		
		try {
			System.out.println("test 6) getByKey : key=" + key1 + " / " + manager.getByKey(mapName, key1).toString());
		} catch (ExceptionNotFound | ExceptionBadRequest e) {
			System.out.println(e.toString());
		}
		
		String key2 = "{\"key1\":\"BBB\", \"key2\":1, \"key3\":11.3}";
		String value3rd = "{\"value\":\"this is 3rd test.\"}";
		
		ret = manager.put(mapName, key2, value3rd);
		System.out.println("test 6) put : old value=" + ((ret != null) ? ret.toString() : "null"));
		
		String key3 = "{\"key1\":\"CCC\", \"key2\":1, \"key3\":11.3}";
		String value4th = "{\"value\":\"this is 4th test.\"}";
		
		ret = manager.put(mapName, key3, value4th);
		System.out.println("test 7-1) put : old value=" + ((ret != null) ? ret.toString() : "null"));
		
		String key4 = "{\"key1\":\"DDD\", \"key2\":1, \"key3\":14.3}";
		String value5th = "{\"value\":\"this is 5th test.\"}";
		
		ret = manager.put(mapName, key4, value5th);
		System.out.println("test 7-2) put : old value=" + ((ret != null) ? ret.toString() : "null"));
		
		try {
			System.out.println("test 8) getAll :" + manager.getAll(mapName).toString());
		} catch (ExceptionNotFound e) {
			System.out.println(e.toString());
		}
		
		String filter = "{\"key2\":\"1\", \"key3\":11.3}";
		try {
			System.out.println("test 9) getByFilter : filter=" + filter + " / " + manager.getByFilter(mapName, filter).toString());
		} catch (ExceptionNotFound | ExceptionBadRequest e) {
			System.out.println(e.toString());
		}
		
		try {
			System.out.println("test 10) removeByKey : key=" + key1 + " / " + manager.removeByKey(mapName, key1).toString());
		} catch (ExceptionNotFound | ExceptionBadRequest e) {
			System.out.println(e.toString());
		}
		
		try {
			ret = manager.getByKey(mapName, key1);
			System.out.println("test 11) getByKey : key=" + key1 + " / " + ((ret != null) ? ret.toString() : "null"));
		} catch (ExceptionNotFound | ExceptionBadRequest e) {
			System.out.println(e.toString());
		}
		
		try {
			System.out.println("test 12) removeByFilter : filter=" + filter + " / " + manager.removeByFilter(mapName, filter).toString());
		} catch (ExceptionNotFound | ExceptionBadRequest e) {
			System.out.println(e.toString());
		}
		
		try {
			ret = manager.getByKey(mapName, key2);
			System.out.println("test 13) getByKey : key=" + key2 + " / " + ((ret != null) ? ret.toString() : "null"));
		} catch (ExceptionNotFound | ExceptionBadRequest e) {
			System.out.println(e.toString());
		}
		
		try {
			System.out.println("test 14) getAll :" + manager.getAll(mapName).toString());
		} catch (ExceptionNotFound e) {
			System.out.println(e.toString());
		}
		
		System.out.println("MultipleVariableKeyMap library test ends");
	}
}