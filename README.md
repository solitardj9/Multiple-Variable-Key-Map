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

###     2.2 Add Map
<pre>
<code>
String mapName = "testMap";
manager.addMap(mapName);
</code>
</pre>

###     2.3 Delete Map
<pre>
<code>
String mapName = "testMap";
manager.deleteMap(mapName);
</code>
</pre>

###     2.4 Put Data
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

###     2.4 Get by Key
<pre>
<code>
manager.getByKey(mapName, key1);
</code>
</pre>

###     2.5 Get by Key Filter
<pre>
<code>
String filter = "{\"key2\":\"1\", \"key3\":11.3}";
manager.getByFilter(mapName, filter);
</code>
</pre>

###     2.6 Remove by Key
<pre>
<code>
manager.removeByKey(mapName, key1);
</code>
</pre>

###     2.5 Remove by Key Filter
<pre>
<code>
filter = "{\"key2\":\"2\", \"key3\":14.3}";
manager.removeByFilter(mapName, filter);
</code>
</pre>


