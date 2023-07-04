package com.wagonloader.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class WagonWorkers {
    
    public static JsonNode getObjectNodeNestedKeys(ObjectNode node, String nestedKey){
    String[] keys = nestedKey.split("\\.");

    JsonNode nestedNode = node;

    for (String key: keys){
        nestedNode = nestedNode.get(key);
        if(nestedNode == null){
            return null;
        }
    }
    
    return nestedNode;
    }
    
    private static Object find(Object params, JsonNode wagonData){
        try{
            if (!(params instanceof String))
                throw new IllegalArgumentException("Params is not a string");
            
            String paramsStr = (String) params;
            String[] paramsList = paramsStr.split("[,]");
            if (paramsList.length > 1) 
                throw new IllegalArgumentException("Too many parameters for method FIND");

            JsonNode trueValue = getObjectNodeNestedKeys( (ObjectNode) wagonData, paramsStr );
            return trueValue;

        } catch (IllegalArgumentException e){
            System.out.println("Error: "+e);
            System.exit(1);
        } catch (Exception e){
            System.out.println("Error: "+e);
            System.exit(1);
        }
        return null;
    }


    public static Object callWorker(String workerName, Object params,JsonNode wagonData){ //TODO Need to add wagonData here so FIND can use it 
        
        switch(workerName) {
            case "FIND":
                return find(params, wagonData); 
            default:
                System.out.println("Not found worker");
                return null;
        }
    }
}
