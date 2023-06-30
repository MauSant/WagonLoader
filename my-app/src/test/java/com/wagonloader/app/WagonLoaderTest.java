package com.wagonloader.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class WagonLoaderTest {
    ObjectMapper mapper = new ObjectMapper();
    WagonLoader wagonLoader = new WagonLoader();
    JsonNode wagonData = readJson("wagonData.json");
    private static final Logger logger = Logger.getLogger(WagonLoaderTest.class.getName());

    public JsonNode readJson(String fileName){
        InputStream stream = WagonLoaderTest.class.getResourceAsStream("../../../"+fileName);
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
        "simpleKey.json",
        "simpleList.json",
        "findSimpleKey.json",
        "findNestedObj.json",
        "findNestedList.json"
    })
    public void testFillPayload(String inputFileName) throws IOException{
        JsonNode input = readJson(inputFileName);
        JsonNode targetJson = input.get("target");
        JsonNode expected = input.get("expected");

        Map<String, Object> map = new HashMap<>();
        Object r = wagonLoader.fillPayload("", targetJson, map, wagonData );
        JsonNode actual = mapper.convertValue(r, JsonNode.class);
        logger.info("["+inputFileName+"]"+prettyPrintObject(actual));
        Assert.assertEquals("msg", expected, actual); 

    }
}
