package org.btc.yaml;

import org.btc.exceptions.YamlPathNotFoundException;
import org.btc.exceptions.YamlSyntaxException;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.scanner.ScannerException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class YamlConfig {
    private static final Yaml YAML = new Yaml();
    private Map<String, Object> map;
    private File from;

    public YamlConfig(File from) throws YamlSyntaxException {
        this.from = from;
        try {
            map = (new Yaml().load(Files.newInputStream(from.toPath())));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ScannerException e) {
            throw new YamlSyntaxException("Invalid Yaml Syntax!", e);
        }
    }

    private YamlConfig() {
    }

    public static YamlConfig emptyConfig() {
        YamlConfig config = new YamlConfig();
        config.map = new HashMap<>();
        return config;
    }

    public YamlConfig merge(YamlConfig config) {
        map.putAll(config.map);
        return this;
    }

    public Set<String> getKeys() {
        return map.keySet();
    }

    public <T> T get(String path, Class<T> type) {
        Map<String, Object> subMap = map;
        String[] subPaths = path.split("\\.");
        for (Integer i = 0; i < subPaths.length; i++) {
            if (i != subPaths.length - 1) {
                try {
                    subMap = (Map<String, Object>) subMap.get(subPaths[i]);
                } catch (ClassCastException e) {
                    new YamlPathNotFoundException("Yaml Path \"" + path + "\" Not Found!").printStackTrace();
                    return null;
                }
            } else {
                return (T) subMap.get(subPaths[i]);
            }
        }
        return null;
    }

    public <T> T getOrDefault(String path, Class<T> type, T def) {
        T obj = get(path, type);
        return obj != null ? obj : def;
    }

    public void set(String path, Object obj) {
        if (obj instanceof YamlParsable) {
            obj = ((YamlParsable) obj).parse();
        }
        Map<String, Object> subMap = map;
        String[] subPaths = path.split("\\.");
        for (Integer i = 0; i < subPaths.length; i++) {
            if (i != subPaths.length - 1) {
                try {
                    if (!subMap.containsKey(subPaths[i])) {
                        subMap.put(subPaths[i], new HashMap<>());
                    }
                    subMap = (Map<String, Object>) subMap.get(subPaths[i]);
                } catch (ClassCastException e) {
                    subMap.put(subPaths[i], new HashMap<>());
                    subMap = (Map<String, Object>) subMap.get(subPaths[i]);
                }
            } else {
                subMap.put(subPaths[i], obj);
            }
        }
    }

    public String getContent() {
        return YAML.dumpAsMap(map);
    }

    public void saveToFile(File file) {
        try {
            FileOutputStream output = new FileOutputStream(file);
            output.write(getContent().getBytes(StandardCharsets.UTF_8));
            output.flush();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        if (from != null) {
            saveToFile(from);
        }
    }
}
