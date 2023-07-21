package com.wagonloader.app.workers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;

import com.wagonloader.app.workers.find.FindWorker;

public class WagonWorkers {
    ObjectMapper mapper = new ObjectMapper();
    FindWorker findWorker = new FindWorker(); 


    public JsonNode evaluate(ValueNode valueNode, JsonNode wagonData){
        Pattern pattern = Pattern.compile("^([A-Z]+(?:_[A-Z]+)*)\\((.*?)\\)$");
        Matcher matcher = pattern.matcher(valueNode.asText());
        if (!matcher.matches()){
            return valueNode;
        }               
        String method = matcher.group(1);
        ValueNode deepMethods = mapper.getNodeFactory().textNode(matcher.group(2));
        JsonNode params = evaluate(deepMethods, wagonData);

        return callWorker(method, params, wagonData);

    }


    public JsonNode callWorker(String workerName, JsonNode params,JsonNode wagonData){
        
        switch(workerName) {
            case "FIND":
                return findWorker.find(params, wagonData); 
            default:
                System.out.println("Not found worker");
                return null;
        }
    }
}
