package com.lagopusempire.phiinae;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.yaml.snakeyaml.Yaml;

public class YamlConfig implements IYamlConfig {
    
    private final Yaml yaml = new Yaml();
    private final Map<String, Object> root;
    
    public YamlConfig(InputStream is) {
        this.root = (Map<String, Object>) yaml.load(is);
    }
    
    private Object resolve(String path, int offset) {
        String[] parts = path.split("\\.");
        
        if(parts.length - 1 - offset < 0) {
            return root;
        }
        
        Map<String, Object> node = root;
        for(int ii = 0; ii < parts.length; ii++) {
            if(!node.containsKey(parts[ii])) {
                return null;
            }
            
            if(ii == parts.length - 1 - offset) {
                return node.get(parts[ii]);
            }
            
            Object child = node.get(parts[ii]);
            if(!(child instanceof Map)) {
                return null;
            }
            
            node = (Map<String, Object>) node.get(parts[ii]);
        }
        
        return null;
    }

    @Override
    public <T> T getValue(String key) {
        Object value = resolve(key, 0);
        return (T) value;
    }

    @Override
    public <T> void setValue(String key, T value) {
        Object node = resolve(key, 1);
        if(node instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) node;
            String[] parts = key.split("\\.");
            String mapKey = parts[parts.length - 1];
            map.put(mapKey, value);
        }
    }

    @Override
    public boolean containsValue(String key) {
        return resolve(key, 0) != null;
    }

    @Override
    public Set<String> getConfigurationSection(String key) {
        Object value = resolve(key, 0);
        if(!(value instanceof Map)) {
            return null;
        }
        
        return ((Map<String, Object>) value).keySet();
    }
    
    @Override
    public String toString() {
        return yaml.dumpAsMap(root);
    }
}
