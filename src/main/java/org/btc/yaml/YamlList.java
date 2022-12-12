package org.btc.yaml;

import java.util.List;
import java.util.Map;

public interface YamlList {
    public List<Map<String, Object>> parse();
    public default Map<String, Object> get(Integer index){
        return parse().get(index);
    }
}
