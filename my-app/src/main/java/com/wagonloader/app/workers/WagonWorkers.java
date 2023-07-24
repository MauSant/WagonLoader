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
import com.wagonloader.app.workers.concat.ConcatWorker;

import static com.wagonloader.app.utils.Constants.FUNCTION_REGEX_PATTERN;


public class WagonWorkers {
    ObjectMapper mapper = new ObjectMapper();
    JsonNodeFactory nodeFactory = mapper.getNodeFactory();
    FindWorker findWorker = new FindWorker(); 
    ConcatWorker concatWorker = new ConcatWorker(); 


    public JsonNode evaluate(ValueNode valueNode, JsonNode wagonData){
        
        //TODO Change valueNode to String! We dont need to pass valueNode anymore!
        
        Pattern pattern = Pattern.compile(FUNCTION_REGEX_PATTERN.toString());
        Matcher matcher = pattern.matcher(valueNode.asText());
        if (!matcher.matches()){
            return valueNode;
        }               
        String method = matcher.group(1);


        //TODO: Create method for parsing the ", ", the input will be  matcher.group(2) and the output should be the Arraynode
            //TODO: Read char by char until you find a comma. 
                // Then figure it out wheteher this comma is inside a `FUNCION(` pattern.
                    //If it is INside then do nothing
                    //Else Add the index of this comma to a Array! This array will be used later for creating subtrings that goes inside teh arrayNode!
        String[] Multipleparams = matcher.group(2).split(", ");
        ValueNode deepMethods;
        ArrayNode params = mapper.createArrayNode();
        //TODO


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
            case "CONCAT":
                return concatWorker.concat(params, wagonData); 
            default:
                System.out.println("Not found worker");
                return null;
        }
    }
}
