package com.lagopusempire.phiinae;

import java.util.Set;

public interface IYamlConfig {
    public <T> T getValue(String key);
    public <T> void setValue(String key, T value);
    public boolean containsValue(String key);
    public Set<String> getConfigurationSection(String key);
}
