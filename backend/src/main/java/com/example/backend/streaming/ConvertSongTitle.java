package com.example.backend.streaming;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ConvertSongTitle {
    Map<String, String> mappings = new HashMap<>();

    public ConvertSongTitle() {
        mappings.put("?", "$[qm]");
        mappings.put("\"", "$[qu]");
        mappings.put("|", "$[ln]");
        mappings.put(":", "$[sc]");
        mappings.put("*", "$[st]");
        mappings.put("<", "$[cr-open]");
        mappings.put(">", "$[cr-close]");
        mappings.put("\\", "$[bs]");
        mappings.put("/", "$[sl]");
    }

    public String parseToFileName(String s) {
        for (String mapping :mappings.keySet()) {
            s = s.replace(mapping, mappings.get(mapping));
        }
        return s;
    }

    public String parseToSongTitle(String s) {
        for (String mapping :mappings.values()) {
            String key = mappings.keySet().stream().filter(x -> mappings.get(x).equals(mapping)).toList().get(0);
            s = s.replaceAll(mapping, key);
        }
        return s;
    }
}
