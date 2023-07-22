package com.wagonloader.app;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.wagonloader.app.workers.WagonWorkers;

public class WagonLoader {
    ObjectMapper mapper = new ObjectMapper();
    WagonWorkers wagonWorkers = new WagonWorkers();

 

    public JsonNode fillPayload(String currentPath, JsonNode jsonNode, JsonNode wagonData) throws IOException{
        String separator = "/";
        String arraySeparator = "#";
        JsonNode result = null;

        if(jsonNode.isObject()){
            ObjectNode objectNode = (ObjectNode) jsonNode;
            Iterator<Map.Entry<String, JsonNode>> iter = objectNode.fields();
            String pathPrefix = currentPath.isEmpty() ? "" : currentPath + separator;

            ObjectNode bigNode = mapper.createObjectNode();
            
            while(iter.hasNext()){
                Map.Entry<String, JsonNode> entry = iter.next();
                result = fillPayload(pathPrefix+ entry.getKey(), entry.getValue(), wagonData);  
                bigNode.set(entry.getKey(), result);

            }
            return bigNode;
        } else if(jsonNode.isArray()){
            ArrayNode arrayNode = (ArrayNode) jsonNode;
            String pathPrefixList = currentPath.isEmpty() ? "" : currentPath + arraySeparator;

            ArrayNode bigList = mapper.createArrayNode();
            for (int i=0; i< arrayNode.size(); i++){
                result = fillPayload(pathPrefixList, arrayNode.get(i), wagonData);
                bigList.add(result);
            }
            return bigList;
        } else if(jsonNode.isValueNode()){
            ValueNode valueNode = (ValueNode) jsonNode;
            
            return wagonWorkers.evaluate(valueNode, wagonData);
            
        }

        return null;
    }

}


