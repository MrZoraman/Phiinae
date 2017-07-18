# Phiinae
A simple wrapper around snakeyaml that makes reading/writing yaml files much easier!

Phiinae is licensed under the zlib license, so you're free to use it however you want!

## Maven
Add this to your repos:
```xml
<repository>
    <id>lago-repo</id>
    <name>Lagopus Empire Repo-releases</name>
    <url>https://repo.lagopusempire.com/artifactory/libs-release-local</url>
</repository>
```
Add this to your dependencies:
```xml
<dependency>
    <groupId>com.lagopusempire</groupId>
    <artifactId>Phiinae</artifactId>
    <version>2.1</version>
</dependency>
```

## JavaDocs
Most of what the javadocs have to say is written below.

Javadocs found [here](http://jd.lagopusempire.com/phiinae/2.1/)! (broken for now...)

## Loading
This is a very simple library that makes reading and writing from yaml files a lot easier! It is mainly designed for configuration files, but it can probably be used for other things as well.

First, you will want an InputStream that contains your YAML data. For this example, I'll read from a file on the disk, but the stream can be from anywhere.
```java
IYamlConfig yamlConfig = null;
try (FileInputStream fis = new FileInputStream(new File("test.yaml"))) {
    yamlConfig = new YamlConfig(fis);
} catch (IOException e) {
    //uh oh...
}
```
Now that you have yourself a YamlConfig file, you're free to read and write data to it!
Suppose this is the test.yaml:
```yaml
some:
  value: 2
another:
  value: 5
stuff: 'I am a string'
heres:
  a:
    double: 3.14
    boolean: true
someList:
- itemA
- itemB
- itemC
numList:
- 5
- 6
- 8
```
## Getting Values
We can read the following data all with the getValue() method!
```java
yamlConfig.getValue("some.value");//2
yamlConfig.getValue("another.value");//5
yamlConfig.getValue("stuff");//I am a string
yamlConfig.getValue("heres.a.double");//3.14
yamlConfig.getValue("heres.a.boolean");//true
yamlConfig.getValue("someList");//List<String> with ["itemA", "itemB", "ItemC"]
yamlConfig.getValue("numList");//List<Integer> with [5, 6, 8]
```
You might also want to see what child nodes a parent node has. That can be done with the getChildren() method!
```java
Set<String> children = yamlConfig.getChildren("heres.a");
//set contains {"double", "boolean"}
```
If you want to check if a key exists, there is a method for that:
```java
yamlConfig.containsKey("some.value");//returns true
yamlConfig.containsKey("does.not.exist");//returns false
```
## Setting Values
Reading is only half the fun though. Just like how you can read data, you can write data as well!
```java
yamlConfig.setValue("some.value", 200);//some.value is now 200
```
If you want to unset a value, simply pass null to the setValue method. It will be completely removed from the yaml file in memory. If a parent key is set to null, all of its children will be erased as well.
```java
yamlConfig.setValue("numList", null);//numList is now gone.
```
## Merging
If you plan on using this library for configuration purposes, you will find the following feature especially useful! Phiinae has the ability to merge two yaml files together! However, it will keep any data from the first, and only move data from the second file if it is not contained in the first file. This is useful for updating config files without overwriting/deleting the user's settings. Here is how it is done:
Suppose we have another yaml file, named template.yaml, with the data:
```yaml
some:
  value: 10
  string: 'hello'
```
You can 'merge' (note: I don't think this is merging like how the official yaml spec describes it) the two yaml files together via the followign code:
```java
try (FileInputStream fis = new FileInputStream(new File("template.yaml"))) {
    yamlConfig.merge(fis);
} catch (IOException e) {
    //uh oh...
}
```
Note that the merge method returns true if the yaml data was changed and false if nothing was merged over.

Now, the yamlConfig will contain the following data (assuming you've followed the 'tutorial' up to this point):
```yaml
some:
  value: 200
  string: 'hello'
another:
  value: 5
#...
```
note that some.string was moved over into the yaml config, but some.value was not (because it already existed in the original yaml data)!
## Saving
Once you've had your fun and it's time to save back to the disk, the following code will do what you want:
```java
try(FileOutputStream fop = new FileOutputStream(new File("test.yaml"))) {
    yamlConfig.write(fop);
} catch (IOException e) {
    //uh oh...
}
```
