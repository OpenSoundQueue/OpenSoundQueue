/**
 * This component is responsible for mapping the human-readable song title into the file name
 * Certain characters are not allowed in file names and therefore have to be escaped or replaced
 */

package com.example.backend.streaming;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ConvertSongTitle {
    Map<String, String> mappings = new HashMap<>();

    /**
     * initiate mappings for all illegal file name characters
     */
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

    /**
     * converts the human-readable song title into a valid file name
     * @param s title of the song as string
     * @return file name as string
     */
    public String parseToFileName(String s) {
        for (String mapping :mappings.keySet()) {
            s = s.replace(mapping, mappings.get(mapping));
        }
        return s;
    }

    /**
     * converts a file name into a human-readable song title
     * @param s file name
     * @return song title as a string
     */
    public String parseToSongTitle(String s) {
        for (String mapping :mappings.values()) {
            String key = mappings.keySet().stream().filter(x -> mappings.get(x).equals(mapping)).toList().get(0);
            s = s.replaceAll(mapping, key);
        }
        return s;
    }
}
