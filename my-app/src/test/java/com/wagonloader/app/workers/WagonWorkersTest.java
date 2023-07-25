package com.wagonloader.app.workers;



import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ValueNode;


public class WagonWorkersTest {
    ObjectMapper mapper = new ObjectMapper();
    WagonWorkers wagonWorkers = new WagonWorkers();
    JsonNode wagonData = readJson("wagonData.json");
    private static final Logger logger = Logger.getLogger(WagonWorkersTest.class.getName());

    public JsonNode readJson(String fileName){
        InputStream stream = WagonWorkersTest.class.getResourceAsStream("../../../../"+fileName);
        try {
            return mapper.readValue(stream, JsonNode.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ValueNode createValueNode(String str){
        return  mapper.getNodeFactory().textNode(str);

    }

    public String prettyPrintObject(Object target){
        JsonNode j = mapper.convertValue(target,JsonNode.class);
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(j);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

  
    @ParameterizedTest
    @ValueSource(strings = {
        "WagonWorkersTest/find/oneNest.json",
        "WagonWorkersTest/find/twoNest.json",
    })
    public void testingFind(String inputFileName){
        JsonNode input = readJson(inputFileName);
        ArrayNode params = mapper.createArrayNode().add(input.get("params"));
        String workerName =  "FIND";
        JsonNode expected = input.get("expected");
        JsonNode actual = wagonWorkers.callWorker(workerName, params, wagonData);
        logger.info("actual :: "+ actual);
        Assert.assertEquals("msg", expected, actual); 
    }

    @Test
    public void testSimpleEvaluate(){
        JsonNode actual = wagonWorkers.evaluate(createValueNode("FIND(key1)"), wagonData);
        Assert.assertEquals("msg", wagonData.get("key1"), actual); 
    }

    @Test
    public void testSimpleRecursionEvaluate(){
        JsonNode actual = wagonWorkers.evaluate(createValueNode("FIND(FIND(key1))"), wagonData);
        Assert.assertEquals("msg", wagonData.get("value1"), actual); 
    }

    @Test
    public void testRecursionEvaluateWithMoreFuncions(){
        JsonNode actual = wagonWorkers.evaluate(createValueNode("FIND(CONCAT(FIND(key1), THISthing))"), wagonData);
        JsonNode expected = wagonData.get("value1THISthing");
        Assert.assertEquals("msg", expected, actual); 
    }

    @Test
    public void testEvaluateWithMoreParams(){
        JsonNode actual = wagonWorkers.evaluate(createValueNode("CONCAT(key1, =, key2)"), wagonData);
        Assert.assertEquals("msg", "key1=key2", actual.asText()); 
    }
}
