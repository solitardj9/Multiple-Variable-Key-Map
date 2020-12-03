# Multiple-Variable-Key-Map

## 1. Multiple Variable Key
> 1) shoud be defined by JSON
> 2) shoud be 1 depth
    
> example)
<pre>
<code>
    {
        "key1" : "myKey",
        "key2" : 1,
        "key3" : 11.3
    }
</code>
</pre>

## 2. Examples
###     2.1 Instance
<pre>
<code>
MultipleVariableKeyMapManager manager = new MultipleVariableKeyMapManagerImpl();
</code>
</pre>

###     2.2 Add map
<pre>
<code>
String mapName = "testMap";
manager.addMap(mapName);
</code>
</pre>

###     2.3 Put data
<pre>
<code>
String key1 = "{\"key1\":\"AAA\", \"key2\":1, \"key3\":11.3}";
String value1 = "{\"value\":\"this is 1st test.\"}";
manager.put(mapName, key1, value1);

String key2 = "{\"key1\":\"BBB\", \"key2\":2, \"key3\":14.3}";
String value2 = "{\"value\":\"this is 2nd test.\"}";
manager.put(mapName, key2, value2);

String key3 = "{\"key1\":\"CCC\", \"key2\":2, \"key3\":14.3}";
String value3 = "{\"value\":\"this is 3rd test.\"}";
manager.put(mapName, key3, value3);
</code>
</pre>

###     2.4 Get
<pre>
<code>
manager.getByKey(mapName, key1);
</code>
</pre>

###     2.5 Find by predicate
reference : Json-Path (https://github.com/json-path/JsonPath)
<pre>
<code>
String predicate = "$.[?(@.key != '0' && @.sex == 'false')]";
manager.find(mapName, predicate);
</code>
</pre>

###     2.6 Remove data in map at specific key
<pre>
<code>
manager.remove(mapName, key1);
</code>
</pre>

###     2.7 clear data in map
<pre>
<code>
manager.clear(mapName);
</code>
</pre>

###     2.8 Remove map
<pre>
<code>
manager.remove(mapName);
</code>
</pre>

###     2.9 claer Maps
<pre>
<code>
manager.clear();
</code>
</pre>
