package com.lagopusempire.phiinae;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

/**
 * A simple snakeyaml wrapper that makes reading and writing yaml data easier.
 * You might find that this interface works similarly to other apis you
 * might have used in the past.
 * <br>
 * The library makes extensive use of keys. To best explain how the keys work,
 * here are a few examples:
 * <br>
 * test.yaml:
 * <pre>
 * some:
 *   value: 2
 * another:
 *   value: 5
 * stuff: 'I am a string'
 * heres:
 *   a:
 *     double: 3.14
 *     boolean: true
 * someList:
 * - itemA
 * - itemB
 * - itemC
 * numList:
 * - 5
 * - 6
 * - 8
 * </pre>
 * The following keys map to their respective values:
 * <pre>
 * {@code 
 * getValue("some.value");//2
 * getValue("another.value");//5
 * getValue("stuff");//I am a string
 * getValue("heres.a.double");//3.14
 * getValue("heres.a.boolean");//true
 * getValue("someList");//List<String> with ["itemA", "itemB", "ItemC"]
 * getValue("numList");//List<Integer> with [5, 6, 8]
 * }
 * </pre>
 * @author Zora
 */
public interface IYamlConfig {
    /**
     * Grabs a value from the yaml file. The return type is inferred from
     * the type you are assigning it too. This returns a wrapper class to
     * the primitive type/string in the yaml file. You can assign this to
     * a primitive type, and java will unwrap the data for you automatically.
     * If the compiler says something about the return type being ambiguous,
     * you will need to cast to the wrapper class of the type you plan on
     * retrieving.
     * Here are some example usages:
     * <pre>
     * {@code 
     * IYamlConfig config = new YamlConfig(new FileInputStream(new File("test.yaml")));
     * int x = config.getValue("some.value");
     * Integer x2 = config.getValue("some.value");//same as the first retrieval method
     * //use x wherever you want...
     * //System.out.println(config.getValue("another.value"));//this is bad!
     * System.out.println((Integer) config.getValue("another.value"));//this is correct!
     * }
     * </pre>
     * @param <T> The type of the return value. This will be a primitive type
     * (or wrapper class thereof) or a String.
     * @param key The key on where to locate the desired value. See the interface
     * description for more details.
     * @return the value if it is found, or null if the key is not found. You
     * can either test if this method is null, or call the {@link #containsValue(java.lang.String) }
     * method.
     */
    public <T> T getValue(String key);
    
    /**
     * This sets a value in the yaml file to what you would like to. Note that
     * like {@link #getValue(java.lang.String) }, this method deals with both
     * primitive types, their wrappers, and strings. Due to the conveniences
     * of java, primitive types and their wrappers can be used interchangably.
     * @param <T> The type of the data you are setting. Because <b>This library
     * only works with primitive types, strings, and Lists of primitive types</b>, 
     * there should not be any ambiguity with what the type is. If there is though,
     * be sure to cast to the desired type.
     * @param key The key on where to locate the desired value. See the interface
     * description for more details.
     * @param value The value that you want to set.
     */
    public <T> void setValue(String key, T value);
    
    /**
     * Checks if a value exists at the given key location. You can also check if
     * {@link #getValue(java.lang.String) } returns null or not.
     * @param key The key on where to check if a value exists or not. See the interface
     * description for more details.
     * @return True if the value exists, false if the value does not exist.
     */
    public boolean containsValue(String key);
    
    /**
     * Gets a configuration section given the key. For instance (using the test.yaml
     * in the description of the {@link com.lagopusempire.phiinae.IYamlConfig IYamlConfig}), 
     * {@code getConfigurationSection("heres.a")} returns a set containing
     * {"double", "boolean"}.
     * @param key The key on where to get the configuration section. See the interface
     * description for more details.
     * @return A set of all the config keys that exist as a child of the key given.
     */
    public Set<String> getConfigurationSection(String key);
    
    /**
     * Merges yaml data from a template stream into this yaml document. 
     * <b>The InputStream is not closed!</b> You do that yourself.
     * Note that this yaml document takes precedence, so if both this document
     * and the document being merged from (passed as the argument) have the same
     * key, the value
     * from this yaml document will remain. This is useful for updating yaml
     * config files without overwriting values that the user may have modified.
     * @param templateStream The stream containing yaml data. It better
     * be yaml or this method will throw an ugly snakeyaml exception.
     * @throws YamlMergeException This exception is thrown if there is a
     * disagreement in major types between the two yaml documents. For instance
     * if in one document, a key resolves to a value, but in another, a key
     * resolves to a configuration section with more keys, this exception
     * will be thrown. Although it is an unmanaged exception, I recommend
     * catching it if you are using this file as a config file, as you probably
     * have no idea what the user has done to that thing.
     */
    public void merge(InputStream templateStream) throws YamlMergeException;
    
    /**
     * This writes the yaml to an {@link java.io.OutputStream OutputStream}.
     * <b>This method does not close the stream you pass it!</b> It is reccomended
     * that you use a try-with-resources to automatically close the stream.
     * <br>
     * Recommended usage (try-with-resources):
     * <pre>
     * {@code 
     * try(FileOutputStream fop = new FileOutputStream(new File("test.yaml"))) {
     *     yamlConfig.write(fop);
     * } catch (IOException e) {
     *     e.printStackTrace();
     * }
     * }
     * </pre>
     * Of course, please do not just throw a stack trace. Sure it works, but
     * it is messy!
     * @param stream The stream for the data to be written to.
     * @throws IOException If something goes wrong while writing to the file.
     */
    public void write(OutputStream stream) throws IOException; 
}
