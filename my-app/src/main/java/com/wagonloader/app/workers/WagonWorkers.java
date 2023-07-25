package com.wagonloader.app.workers;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ValueNode;

import com.wagonloader.app.workers.find.FindWorker;
import com.wagonloader.app.workers.concat.ConcatWorker;

import static com.wagonloader.app.utils.Constants.FUNCTION_REGEX_PATTERN;


public class WagonWorkers {
    ObjectMapper mapper = new ObjectMapper();
    JsonNodeFactory nodeFactory = mapper.getNodeFactory();
    FindWorker findWorker = new FindWorker(); 
    ConcatWorker concatWorker = new ConcatWorker(); 
    WagonParsers wagonParsers = new WagonParsers();


    public JsonNode evaluate(ValueNode valueNode, JsonNode wagonData){
        
        //TODO Change valueNode to String! We dont need to pass valueNode anymore!
        
        Pattern pattern = Pattern.compile(FUNCTION_REGEX_PATTERN.toString());
        Matcher matcher = pattern.matcher(valueNode.asText());
        if (!matcher.matches()){
            return valueNode;
        }               
        String method = matcher.group(1);


        List<String> multipleParams = wagonParsers.SplitParser(matcher.group(2));
        ValueNode deepMethods;
        ArrayNode params = mapper.createArrayNode();

        for (String Singleparam: multipleParams){
            deepMethods = nodeFactory.textNode(Singleparam);
            params.add(evaluate(deepMethods, wagonData));
        }
        

        return callWorker(method, params, wagonData);

    }


    public JsonNode callWorker(String workerName, ArrayNode params,JsonNode wagonData){
        
        switch(workerName) {
            case "FIND":
                return findWorker.find(params, wagonData); 
            case "CONCAT":
                return concatWorker.concat(params, wagonData); 
            default:
                System.out.println("Not found worker");
                return null;
        }
    }
}
