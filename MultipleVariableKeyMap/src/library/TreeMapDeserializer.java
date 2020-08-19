package library;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

public class TreeMapDeserializer implements JsonDeserializer<TreeMap<String, Object>> {

	@Override @SuppressWarnings("unchecked")
	public TreeMap<String, Object> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		//
		return (TreeMap<String, Object>) read(json);
	}

	public Object read(JsonElement in) {
		//
		if (in.isJsonArray()) {
			//JsonArray�� ���
			List<Object> list = new ArrayList<Object>();
			JsonArray arr = in.getAsJsonArray();
			
			for (JsonElement anArr : arr) {
				//JsonPrimitive ���� ������ for��
				list.add(read(anArr));
			}
			return list;
		} 
		else if (in.isJsonObject()) {
			Map<String, Object> map = new TreeMap<String, Object>();
			JsonObject obj = in.getAsJsonObject();
			Set<Entry<String, JsonElement>> entitySet = obj.entrySet();
			
			for (Map.Entry<String, JsonElement> entry: entitySet) {
				//JsonPrimitive ���� ������ for��
				map.put(entry.getKey(), read(entry.getValue()));
			}
			
			return map;
		}
		else if (in.isJsonPrimitive()) {
			JsonPrimitive prim = in.getAsJsonPrimitive();
			
			if (prim.isBoolean()) {
				//true , fales ������
				return prim.getAsBoolean();
			}
			else if (prim.isString()) {
				//String����
				return prim.getAsString();
			}
			else if(prim.isNumber()) {
				Number num = prim.getAsNumber();
				//Math.ceil �Ҽ����� �ø��Ѵ�.
				if (Math.ceil(num.doubleValue()) == num.longValue()) {
					//�Ҽ��� ����, Int������.
					return num.longValue();
				}
				else {
					//�Ҽ��� �ȹ���, Double ������
					return num.doubleValue();
				}
			}
		}
			
		return null;
	}
}