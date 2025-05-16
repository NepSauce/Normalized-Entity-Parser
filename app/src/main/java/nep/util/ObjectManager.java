package nep.util;

public class ObjectManager{
    
    private String object;
    
    public ObjectManager(String rawData){
        this.object = rawData.trim();
    }
    
    public String modifyObjectKeyValue(String key, String newValue){
        String pattern = key + ": ";
        int start = object.indexOf(pattern);
        
        if (start == -1){
            return object;
        }
        
        int valueStart = start + pattern.length();
        int valueEnd = object.indexOf(" |", valueStart);
        
        if (valueEnd == -1){
            valueEnd = object.indexOf("]", valueStart);
        }
        if (valueEnd == -1){
            return object;
        }
        object = object.substring(0, valueStart) + newValue + object.substring(valueEnd);
        
        return object;
    }
    
    public String getObject() {
        return object.trim();
    }
}
