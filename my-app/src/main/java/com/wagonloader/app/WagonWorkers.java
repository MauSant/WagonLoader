package com.wagonloader.app;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class WagonWorkers {
    
    public JsonNode getObjectNodeNestedKeys(ObjectNode node, String nestedKey){
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
    

    public Object evaluate(String valueNode, JsonNode wagonData){
        Pattern pattern = Pattern.compile("^([A-Z]+(?:_[A-Z]+)*)\\((.*?)\\)$");
        Matcher matcher = pattern.matcher(valueNode);
        if (!matcher.matches()){
            return valueNode;
        }               
        String method = matcher.group(1);
        Object params = evaluate(matcher.group(2), wagonData);

        return callWorker(method, params, wagonData);

    }

    // Create its own POJO for this method
    private Object find(Object params, JsonNode wagonData){
        try{
            if (params instanceof JsonNode){
                params = ((JsonNode) params).asText();
            } else if (!(params instanceof String))
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


    public Object callWorker(String workerName, Object params,JsonNode wagonData){
        
        switch(workerName) {
            case "FIND":
                return find(params, wagonData); 
            default:
                System.out.println("Not found worker");
                return null;
        }
    }
}
