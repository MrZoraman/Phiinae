package com.lagopusempire.phiinae;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.yaml.snakeyaml.Yaml;

/**
 * A simple implementation of {@link com.lagopusempire.phiinae.IYamlConfig IYamlConfig}.
 * @author Zora
 */
public class YamlConfig implements IYamlConfig {
    
    private final Yaml yaml = new Yaml();
    private final Map<String, Object> root;
    
    /**
     * Constructor. <b>The InputStream is not closed!</b> You do that yourself.
     * @param fis The InputStream containing yaml contents. This stream better
     * contain yaml formatted contents, or else snakeyaml will throw an ugly
     * exception.
     */
    public YamlConfig(InputStream fis) {
        this.root = (Map<String, Object>) yaml.load(fis);
    }
    
    @Override
    public boolean merge(InputStream templateStream) {
        Yaml template = new Yaml();
        Map<String, Object> templateRoot = (Map<String, Object>) template.load(templateStream);
        return mergeMaps(root, templateRoot);
    }
    
    private boolean mergeMaps(Map<String, Object> root, Map<String, Object> templateRoot) throws YamlMergeException {
        boolean changed = false;
        for(Entry<String, Object> entry : templateRoot.entrySet()) {
            Object templateValue = entry.getValue();
            if(templateValue instanceof Map) {
                Map<String, Object> childMap = null;
                Object child = root.get(entry.getKey());
                if(child == null) {
                    childMap = new HashMap<>();
                } else if (child instanceof Map){
                    childMap = (HashMap<String, Object>) child;
                } else {
                    throw new YamlMergeException();
                }
                Map<String, Object> childTemplateMap = (Map<String, Object>) templateValue;
                changed |= mergeMaps(childMap, childTemplateMap);
                root.put(entry.getKey(), childMap);
            } else if (templateValue instanceof List) {
                Object child = root.get(entry.getKey());
                List<String> childList = null;
                if(child == null) {
                    childList = new ArrayList();
                } else if (child instanceof List) {
                    childList = (List<String>) child;
                } else {
                    throw new YamlMergeException();
                }
                List<String> childTemplateList = (List<String>) templateValue;
                for(String item : childTemplateList) {
                    if(childList.contains(item)) {
                        continue;
                    }
                    
                    childList.add(item);
                    changed = true;
                }
                root.put(entry.getKey(), childList);
            } else if (!root.containsKey(entry.getKey())){
                root.put(entry.getKey(), templateValue);
                changed = true;
            }
        }
        return changed;
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
            if(value == null) {
                map.remove(mapKey);
            } else {
                map.put(mapKey, value);
            }
        }
    }

    @Override
    public boolean containsKey(String key) {
        return resolve(key, 0) != null;
    }

    @Override
    public Set<String> getChildren(String key) {
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

    @Override
    public void write(OutputStream stream) throws IOException {
        byte[] fileBytes = toString().getBytes();
        stream.write(fileBytes);
        stream.flush();
    }
}
