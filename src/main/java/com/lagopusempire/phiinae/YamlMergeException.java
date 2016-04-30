package com.lagopusempire.phiinae;

/**
 * Thrown by the {@link com.lagopusempire.phiinae.IYamlConfig#merge(java.io.InputStream) IYamlConfig.merge()}
 * method. See the documentation for that method for more details.
 * @author Zora
 */
public class YamlMergeException extends RuntimeException {
    YamlMergeException() {
        super("Yaml data type mismatch! See the javadoc of this library"
                + "for more information.");
    }
}
