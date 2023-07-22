package com.wagonloader.app.workers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;

import com.wagonloader.app.workers.find.FindWorker;

public class WagonWorkers {
    ObjectMapper mapper = new ObjectMapper();
    FindWorker findWorker = new FindWorker(); 
    JsonNodeFactory nodeFactory = mapper.getNodeFactory();


    public JsonNode evaluate(ValueNode valueNode, JsonNode wagonData){
        
        //TODO Change valueNode to String! We dont need to pass valueNode anymore!
        
        Pattern pattern = Pattern.compile("^([A-Z]+(?:_[A-Z]+)*)\\((.*?)\\)$");
        Matcher matcher = pattern.matcher(valueNode.asText());
        if (!matcher.matches()){
            return valueNode;
        }               
        String method = matcher.group(1);
        String[] Multipleparams = matcher.group(2).split(", ");
        ValueNode deepMethods;
        ArrayNode params = mapper.createArrayNode();
        for (String Singleparam: Multipleparams){
            deepMethods = nodeFactory.textNode(Singleparam);
            params.add(evaluate(deepMethods, wagonData));
        }
        

        return callWorker(method, params, wagonData);

    }


    public JsonNode callWorker(String workerName, ArrayNode params,JsonNode wagonData){
        
        switch(workerName) {
            case "FIND":
                return findWorker.find(params, wagonData); 
            default:
                System.out.println("Not found worker");
                return null;
        }
    }
}
