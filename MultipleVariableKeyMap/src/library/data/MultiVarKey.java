package library.data;

import java.util.TreeMap;

public class MultiVarKey {
	//
	private TreeMap<String, Object> keyMap;
	
	private String key;
	
	public MultiVarKey() {
	}

	public MultiVarKey(TreeMap<String, Object> keyMap, String key) {
		this.keyMap = keyMap;
		this.key = key;
	}

	public TreeMap<String, Object> getKeyMap() {
		return keyMap;
	}

	public void setKeyMap(TreeMap<String, Object> keyMap) {
		this.keyMap = keyMap;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return "MultiVarKey [keyMap=" + keyMap + ", key=" + key + "]";
	}
}