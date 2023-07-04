package com.wagonloader.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;

public class WagonLoader {
    ObjectMapper mapper = new ObjectMapper();

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

    public Object evaluate(String valueNode, JsonNode wagonData){
        Pattern pattern = Pattern.compile("^([A-Z]+(?:_[A-Z]+)*)\\((.*?)\\)$");
        Matcher matcher = pattern.matcher(valueNode);
        if (!matcher.matches()){
            return valueNode;
        }               
        String method = matcher.group(1);
        Object params = evaluate(matcher.group(2), wagonData);

        return WagonWorkers.callWorker(method, params, wagonData);

    }

    public Object fillPayload(String currentPath, JsonNode jsonNode, Map<String, Object>map, JsonNode wagonData) throws IOException{
        String separator = "/";
        String arraySeparator = "#";
        Object result = null;

        if(jsonNode.isObject()){
            ObjectNode objectNode = (ObjectNode) jsonNode;
            Iterator<Map.Entry<String, JsonNode>> iter = objectNode.fields();
            String pathPrefix = currentPath.isEmpty() ? "" : currentPath + separator;
            Map<String, Object> bigNode = new HashMap<>();
            while(iter.hasNext()){
                Map.Entry<String, JsonNode> entry = iter.next();
                result = fillPayload(pathPrefix+ entry.getKey(), entry.getValue(), bigNode, wagonData); // I think we can remove passing bigNode as parameter 
                bigNode.put(entry.getKey(), result);
            }
            return bigNode;
        } else if(jsonNode.isArray()){
            ArrayNode arrayNode = (ArrayNode) jsonNode;
            String pathPrefixList = currentPath.isEmpty() ? "" : currentPath + arraySeparator;

            List<Object> bigList = new ArrayList<>();
            for (int i=0; i< arrayNode.size(); i++){
                result = fillPayload(pathPrefixList, arrayNode.get(i), map, wagonData);//TODO `map` been pass here it is wrong! I think we can remove
                bigList.add(result);
            }
            return bigList;
        } else if(jsonNode.isValueNode()){
            ValueNode valueNode = (ValueNode) jsonNode;
            String textValue = valueNode.asText();
            
            Object realvalue = null;
            if(textValue.substring(0).contains("$")){
                textValue = textValue.replaceFirst("[$]", "");
                JsonNode trueValue = getObjectNodeNestedKeys( (ObjectNode) wagonData, textValue );
                if (trueValue != null){
                    realvalue = trueValue;
                }
            } else {
                realvalue = textValue;
            }
            return realvalue;
        }

        return null;
    }

}


