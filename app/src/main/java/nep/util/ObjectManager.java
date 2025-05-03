package nep.util;

public class ObjectManager {
    
    private String object;
    
    public ObjectManager(String rawData) {
        this.object = rawData.trim();
    }
    
    public String modifyObjectKeyValue(String key, String newValue) {
        String pattern = key + ": ";
        int start = object.indexOf(pattern);
        if (start == -1) return object; // key not found
        
        int valueStart = start + pattern.length();
        int valueEnd = object.indexOf(" |", valueStart);
        if (valueEnd == -1) valueEnd = object.indexOf("]", valueStart);
        
        if (valueEnd == -1) return object;
        
        object = object.substring(0, valueStart) + newValue + object.substring(valueEnd);
        return object;
    }
    
    public String removeObjectKeyValue(String key) {
        String pattern = key + ": ";
        int start = object.indexOf(pattern);
        if (start == -1) return object;
        
        int end = object.indexOf(" |", start);
        if (end == -1) {
            // last element before closing ]
            end = object.lastIndexOf("]");
            if (object.charAt(start - 1) == '|') start -= 2; // remove preceding delimiter
            else if (object.charAt(start - 1) == ' ') start -= 1;
        }
        
        object = object.substring(0, start) + object.substring(end + 1);
        object = object.replaceAll("\\[\\s*\\|", "["); // clean leading "|"
        object = object.replaceAll("\\|\\s*\\]", "]"); // clean trailing "|"
        return object.trim();
    }
    
    public String addIfNull(String key, String value) {
        String pattern = key + ": ";
        int start = object.indexOf(pattern);
        if (start == -1) return object;
        
        int valueStart = start + pattern.length();
        int valueEnd = object.indexOf(" |", valueStart);
        if (valueEnd == -1) valueEnd = object.indexOf("]", valueStart);
        
        String currentValue = object.substring(valueStart, valueEnd).trim();
        if (currentValue.equalsIgnoreCase("null")) {
            object = object.substring(0, valueStart) + value + object.substring(valueEnd);
        }
        
        return object;
    }
    
    public String getObject() {
        return object.trim();
    }
}
