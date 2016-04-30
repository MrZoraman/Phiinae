package com.lagopusempire.phiinae;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Zora
 */
public class YamlConfigTest {
    
    private static final String YAML_DATA = 
              "num: 5\n"
            + "str: 'hello'\n"
            + "dub: 3.14\n"
            + "yn: true\n"
            + "list:\n"
            + "- itemA\n"
            + "- itemB\n"
            + "- itemC\n"
            + "parent:\n"
            + "  child: 42\n"
            + "  child2: 12\n"
            + "  child3: 23\n";
    
    private YamlConfig yamlConfig;
    
    @Before
    public void setUp() throws IOException {
        try (InputStream stream = new ByteArrayInputStream(YAML_DATA.getBytes(StandardCharsets.UTF_8))) {
            yamlConfig = new YamlConfig(stream);
        }
    }

    /**
     * Test of merge method, of class YamlConfig.
     * @throws java.io.IOException
     */
    @Test
    public void testMerge() throws IOException {
        String otherYamlData = "merged: true";
        try (InputStream stream = new ByteArrayInputStream(otherYamlData.getBytes(StandardCharsets.UTF_8))) {
            yamlConfig.merge(stream);
        }
        Boolean result = yamlConfig.getValue("merged");
        assertNotNull(result);
        assertEquals(true, result);
    }

    /**
     * Test of getValue method, of class YamlConfig.
     */
    @Test
    public void testGetInt() {
        Integer num = yamlConfig.getValue("num");
        assertNotNull(num);
        assertEquals(1, num.intValue());
    }
    
    /**
     * Test of getValue method, of class YamlConfig.
     */
    @Test
    public void testGetboolean() {
        Boolean bool = yamlConfig.getValue("yn");
        assertNotNull(bool);
        assertEquals(true, bool);
    }
    
    /**
     * Test of getValue method, of class YamlConfig.
     */
    @Test
    public void testGetDouble() {
        Double num = yamlConfig.getValue("dub");
        assertNotNull(num);
        assertEquals(3.14, num, 0.001);
    }
    
    /**
     * Test of getValue method, of class YamlConfig.
     */
    @Test
    public void testGetString() {
        String str = yamlConfig.getValue("str");
        assertNotNull(str);
        assertEquals("hello", str);
    }
    
    @Test
    public void testGetList() {
        List<String> list = yamlConfig.getValue("list");
        assertNotNull(list);
        assertEquals(3, list.size());
        String[] listArray = list.toArray(new String[0]);
        String[] expected = new String[]{"itemA", "itemB", "itemC"};
        assertArrayEquals(expected, listArray);
    }

    /**
     * Test of setValue method, of class YamlConfig.
     */
    @Test
    public void testSetValue() {
        int data = yamlConfig.getValue("parent.child");
        assertEquals(42, data);
        yamlConfig.setValue("parent.child", 24);
        data = yamlConfig.getValue("parent.child");
        assertEquals(24, data);
    }

    /**
     * Test of containsValue method, of class YamlConfig.
     */
    @Test
    public void testContainsValue() {
        assertTrue(yamlConfig.containsValue("parent.child"));
        assertFalse(yamlConfig.containsValue("parent.nochild"));
        
        assertNotNull(yamlConfig.getValue("parent.child"));
        assertNull(yamlConfig.getValue("parent.nochild"));
    }

    /**
     * Test of getConfigurationSection method, of class YamlConfig.
     */
    @Test
    public void testGetConfigurationSection() {
        Set<String> configurationSection = yamlConfig.getConfigurationSection("parent");
        assertNotNull(configurationSection);
        assertEquals(3, configurationSection.size());
        String[] configSectionArray = configurationSection.toArray(new String[0]);
        String[] expected = new String[]{"child", "child2", "child3"};
        assertArrayEquals(expected, configSectionArray);
    }

    /**
     * Test of toString method, of class YamlConfig.
     */
    @Test
    public void testToString() {
        String str = yamlConfig.toString();
        assertEquals(YAML_DATA, str);
    }
}
