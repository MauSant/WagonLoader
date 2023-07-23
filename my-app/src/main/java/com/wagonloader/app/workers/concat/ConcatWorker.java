package com.wagonloader.app.workers.concat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ConcatWorker {
    ObjectMapper mapper = new ObjectMapper();
    JsonNodeFactory nodeFactory = mapper.getNodeFactory();

    public JsonNode concat(ArrayNode params, JsonNode wagonData){
        try{
            StringBuilder buffer = new StringBuilder("");
            for (int i=0; i< params.size(); i++){
                buffer.append(params.get(i).asText());
            }
            return nodeFactory.textNode(buffer.toString());
            
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
