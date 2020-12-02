package library;

import java.util.Map;

import library.exception.ExceptionAlreadyExist;
import library.exception.ExceptionNotFound;

public class Test {

	public static void main(String[] args) {
		//
		System.out.println("MultipleVariableKeyMap library test starts");
		
		MultipleVariableKeyMapManager manager = new MultipleVariableKeyMapManagerImpl();

		System.out.println("//--------------------------------------------------------------------------------------------------------");
		String mapName = "testMap";
		try {
			System.out.println("test 1) addMap : " + manager.addMap(mapName).toString());
			System.out.println();
			System.out.println("test 2) addMap : ");
			System.out.println(manager.addMap(mapName).toString());
		} catch (ExceptionAlreadyExist e) {
			e.printStackTrace();
		}
		System.out.println("//--------------------------------------------------------------------------------------------------------");
		System.out.println();
		System.out.println("//--------------------------------------------------------------------------------------------------------");
		String keyA = "{\"keyA\":\"AAA\", \"keyB\":1, \"keyC\":11.3}";
		String value1st = "{\"value\":\"this is 1st test.\"}";
		String value2nd = "{\"value\":\"this is 2nd test.\"}";
		Object ret = null;
		
		try {
			ret = manager.put(mapName, keyA, value1st);
			System.out.println("test 3) put : old value = " + ((ret != null) ? ret.toString() : "null"));
			//System.out.println("toString :" + manager.toString(mapName));
		} catch (ExceptionNotFound e) {
			e.printStackTrace();
		}
		System.out.println();
		
		try {
			System.out.println("test 4) get : key = " + keyA + " / " + manager.get(mapName, keyA).toString());
		} catch (ExceptionNotFound e) {
			System.out.println(e.toString());
		}
		System.out.println();
		
		try {
			ret = manager.put(mapName, keyA, value2nd);
			System.out.println("test 5) put : old value = " + ((ret != null) ? ret.toString() : "null"));
			//System.out.println("toString :" + manager.toString(mapName));
		} catch (ExceptionNotFound e) {
			e.printStackTrace();
		}
		System.out.println();
		
		try {
			System.out.println("test 6) get : key = " + keyA + " / " + manager.get(mapName, keyA).toString());
		} catch (ExceptionNotFound e) {
			System.out.println(e.toString());
		}		
		System.out.println();
		
		String keyB = "{\"keyA\":\"BBB\", \"keyB\":1, \"keyC\":11.3}";
		String value3rd = "{\"value\":\"this is 3rd test.\"}";
		try {
			ret = manager.put(mapName, keyB, value3rd);
			System.out.println("test 7) put : old value = " + ((ret != null) ? ret.toString() : "null"));
			//System.out.println("toString :" + manager.toString(mapName));
		} catch (ExceptionNotFound e) {
			e.printStackTrace();
		}
		System.out.println();
		
		String keyC = "{\"keyA\":\"CCC\", \"keyB\":1, \"keyC\":11.3}";
		String value4th = "{\"value\":\"this is 4th test.\"}";
		try {
			ret = manager.put(mapName, keyC, value4th);
			System.out.println("test 8) put : old value = " + ((ret != null) ? ret.toString() : "null"));
			//System.out.println("toString :" + manager.toString(mapName));
		} catch (ExceptionNotFound e) {
			e.printStackTrace();
		}
		System.out.println();
		
		String key4 = "{\"keyA\":\"DDD\", \"keyB\":1, \"keyC\":14.3}";
		String value5th = "{\"value\":\"this is 5th test.\"}";
		try {
			ret = manager.put(mapName, key4, value5th);
			System.out.println("test 9) put : old value = " + ((ret != null) ? ret.toString() : "null"));
			//System.out.println("toString :" + manager.toString(mapName));
		} catch (ExceptionNotFound e) {
			e.printStackTrace();
		}
		System.out.println();
		
		try {
			System.out.println("test 10) getMap : map = " + manager.getMap(mapName).toString());
		} catch (ExceptionNotFound e) {
			System.out.println(e.toString());
		}
		System.out.println("//--------------------------------------------------------------------------------------------------------");
		System.out.println();
		System.out.println("//--------------------------------------------------------------------------------------------------------");
		System.out.println("Test for deep copy(x) or shallow copy(O) for getMap");
		try {
			Map<String, Object> map = manager.getMap(mapName);
			
			System.out.println("test 11-1) getMap : map = " + map.toString());
			System.out.println("test 11-2) get(" + keyA + ") : value = " + map.get(keyA.replace(" ", "")));
			
			map.put(keyA.replace(" ", ""), "{\"value\":\"this is new data.\"}");
			System.out.println("test 11-3) toString :" + map.toString());
			
			System.out.println("test 11-4) toString :" + manager.toString(mapName));
		} catch (ExceptionNotFound e) {
			System.out.println(e.toString());
		}
		System.out.println("//--------------------------------------------------------------------------------------------------------");
		System.out.println();
		System.out.println("//--------------------------------------------------------------------------------------------------------");
		try {
			String predicate = "$.[?(@.keyA != 'AAA' && @.keyC >= 12)]";
			System.out.println("test 12) find : predicate = " + predicate + " / " + manager.find(mapName, predicate).toString());
		} catch (ExceptionNotFound e) {
			System.out.println(e.toString());
		}
		System.out.println("//--------------------------------------------------------------------------------------------------------");
		System.out.println();
		System.out.println("//--------------------------------------------------------------------------------------------------------");
		try {
			System.out.println("test 13-1) remove : key = " + keyA + " / " + manager.remove(mapName, keyA).toString());
			System.out.println("test 13-2) toString :" + manager.toString(mapName));
		} catch (ExceptionNotFound e) {
			System.out.println(e.toString());
		}
		System.out.println();
		
		try {
			System.out.println("test 14-1) clear : ");
			manager.clear(mapName);
			System.out.println("test 14-2) toString :" + manager.toString(mapName));
			System.out.println("test 14-3) toString :" + manager.toString());
		} catch (ExceptionNotFound e) {
			System.out.println(e.toString());
		}
		System.out.println();
		
		try {
			System.out.println("test 15-1) remove : ");
			manager.remove(mapName);
			System.out.println("test 15-2) toString :" + manager.toString());
		} catch (ExceptionNotFound e) {
			System.out.println(e.toString());
		}
		System.out.println();
		
		System.out.println("test 16-1) clear : ");
		manager.clear();
		System.out.println("test 16-2) toString :" + manager.toString());
		System.out.println("//--------------------------------------------------------------------------------------------------------");
		
		System.out.println("MultipleVariableKeyMap library test ends");
	}
}