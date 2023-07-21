package com.wagonloader.app.workers.find;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class FindWorker {

    
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

    // Create its own POJO for this method
    public JsonNode find(JsonNode params, JsonNode wagonData){
        try{
            String paramsStr = params.asText();
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


}
