package com.wagonloader.app;


import static org.mockito.Mockito.mockStatic;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class WagonWorkersTest {
    ObjectMapper mapper = new ObjectMapper();
    WagonWorkers wagonWorkers = new WagonWorkers();
    JsonNode wagonData = readJson("wagonData.json");
    private static final Logger logger = Logger.getLogger(WagonWorkersTest.class.getName());

    public JsonNode readJson(String fileName){
        InputStream stream = WagonWorkersTest.class.getResourceAsStream("../../../"+fileName);
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
        Object params = input.get("params").asText();
        String workerName =  "FIND";
        JsonNode expected = input.get("expected");
        Object actual = WagonWorkers.callWorker(workerName, params, wagonData);
        logger.info("actual :: "+ actual);
        Assert.assertEquals("msg", expected, actual); 
    }



}
