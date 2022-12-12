package org.btc.yaml;

import org.btc.exceptions.YamlSyntaxException;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class YamlLoader {
    private Map<String, YamlConfig> loaded = new HashMap<>();

    public void load(String id, File file) {
        try {
            YamlConfig config = new YamlConfig(file);
            loaded.put(id, config);
        } catch (YamlSyntaxException e) {
            e.printStackTrace();
        }
    }

    public void unload(String id) {
        loaded.remove(id);
    }

    public void unloadAll() {
        loaded = new HashMap<>();
    }

    public YamlConfig get(String id) {
        return loaded.get(id);
    }

    public Set<String> keySet() {
        return loaded.keySet();
    }

    public void save(String id) {
        loaded.get(id).save();
    }

    public void save(String id, File file) {
        loaded.get(id).saveToFile(file);
    }
}
