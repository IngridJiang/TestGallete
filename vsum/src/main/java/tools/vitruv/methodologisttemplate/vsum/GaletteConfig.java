package tools.vitruv.methodologisttemplate.vsum;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Configuration utility for reading VSUM paths and settings from properties file.
 */
public class GaletteConfig {
    
    private static final String CONFIG_FILE = "galette-config.properties";
    private final Properties properties;
    
    public GaletteConfig() {
        this.properties = loadProperties();
    }
    
    private Properties loadProperties() {
        Properties props = new Properties();
        
        // Try to load from classpath first
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (is != null) {
                props.load(is);
                return props;
            }
        } catch (IOException e) {
            // Fall through to file system loading
        }
        
        // Try to load from file system relative to current directory
        Path configPath = Paths.get(CONFIG_FILE);
        if (!Files.exists(configPath)) {
            // Try relative to vsum directory
            configPath = Paths.get("vsum", CONFIG_FILE);
        }
        
        if (Files.exists(configPath)) {
            try (InputStream is = Files.newInputStream(configPath)) {
                props.load(is);
            } catch (IOException e) {
                System.err.println("Warning: Could not load config file " + configPath + ": " + e.getMessage());
            }
        } else {
            System.err.println("Warning: Config file not found at " + configPath + ", using defaults");
        }
        
        return props;
    }
    
    public String getWorkingDir() {
        return getProperty("vsum.working.dir", "galette-output-0");
    }
    
    public String getProjectBasePath() {
        String path = getProperty("project.base.path", System.getProperty("user.dir"));
        if ("${user.dir}".equals(path)) {
            return System.getProperty("user.dir");
        }
        return path;
    }
    
    public String getModelFilePattern() {
        return getProperty("model.file.pattern", "example.model");
    }
    
    public String getModel2FilePattern() {
        return getProperty("model2.file.pattern", "example.model2");
    }
    
    public String getOutputDir() {
        return getProperty("output.dir", "galette-test-output");
    }
    
    public String getOutputFile() {
        return getProperty("output.file", "vsum-output.xmi");
    }
    
    private String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Gets the working directory as a Path, resolved relative to the current directory.
     */
    public Path getWorkingPath() {
        return Paths.get(getWorkingDir());
    }
    
    /**
     * Gets the project base path as a Path.
     */
    public Path getProjectBasePathAsPath() {
        return Paths.get(getProjectBasePath());
    }
}