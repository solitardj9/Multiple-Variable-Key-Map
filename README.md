# Multiple-Variable-Key-Map

1. Multiple Variable Key
    1) shoud be defined by JSON
    2) shoud have 1 depth
    
    example)
    {
        "key1" : "myKey",
        "key2" : 1,
        "key3" : 11.3
    }
    
2. Examples
    2.1 Instance
        ```java
        MultipleVariableKeyMapManager manager = new MultipleVariableKeyMapManagerImpl();
