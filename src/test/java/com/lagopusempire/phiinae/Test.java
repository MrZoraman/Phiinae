package com.lagopusempire.phiinae;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.yaml.snakeyaml.Yaml;

public class Test {
    public static void main(String[] args) throws FileNotFoundException, IOException {
//        Yaml yaml = new Yaml();
        String yamlPath = "C:\\Users\\Hiiro\\Desktop\\test.yaml";
        File f = new File(yamlPath);
        FileInputStream fs = new FileInputStream(f);
//        Object root = yaml.load(fs);
        
//        Yaml yaml2 = new Yaml();
        String yaml2Path = "C:\\Users\\Hiiro\\Desktop\\test2.yaml";
        File f2 = new File(yaml2Path);
        FileInputStream fs2 = new FileInputStream(f2);
        
        
        
//        Object root2 = yaml2.load(fs2);
        
        
//        Map<String, Object> map = (Map<String, Object>) root;
//        Object hitObj = map.get("test");
//        System.out.println("map: " + (hitObj instanceof Map));
//        System.out.println("list: " + (hitObj instanceof List));
//        System.out.println("int: " + (hitObj instanceof Integer));
//        System.out.println((hitObj.getClass().getName()));
////        System.out.println("--");
//        Map<String, Object> hit = (Map<String, Object>) hitObj;
//        for(Entry<String, Object> entry : hit.entrySet()) {
//            System.out.println(entry.getKey() + " : " + entry.getValue());
//        }
//        List<String> list = (List<String>) hitObj;
//        list.forEach(item -> System.out.println(item.getClass().getName()));
        

//        System.out.println(yaml.dumpAsMap(root));


        
        YamlConfig yc = new YamlConfig(fs);
        yc.merge(fs2);
        System.out.println(yc);
        
        File f3 = new File("C:\\Users\\Hiiro\\Desktop\\test3.yaml");
        if(!f3.exists()) {
            f3.createNewFile();
        }
        
        FileOutputStream fop = new FileOutputStream(f3);
        yc.write(fop);
        fop.close();
//        
//        Integer num = yc.getValue("test.num1");
//        System.out.println(num);
//        
//        System.out.println("set to 24");
//        yc.setValue("test.num1", 24);
//        
//        num = yc.getValue("test.num1");
//        System.out.println(num);
//        
//        
////        String moof = yc.getValue("moof");
////        System.out.println(moof);
////        
////        Set<String> sect = yc.getConfigurationSection("test");
////        sect.forEach(System.out::println);
////        
//        System.out.println("--");
////        
//        List<String> list = yc.getValue("moom");
//        list.forEach(System.out::println);
//        list.add("moff tarkin");
//        yc.setValue("moom", list);
////        
////        System.out.println("--");
////        
////        List<Integer> nums = yc.getList("nums");
////        nums.forEach(System.out::println);
////        
//        System.out.println("--");
//        System.out.println(yc);
//
//        fs.close();
    }
}
