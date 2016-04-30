package com.lagopusempire.phiinae;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

public interface IYamlConfig {
    public <T> T getValue(String key);
    public <T> void setValue(String key, T value);
    public boolean containsValue(String key);
    public Set<String> getConfigurationSection(String key);
    public void write(OutputStream stream) throws IOException; 
}
