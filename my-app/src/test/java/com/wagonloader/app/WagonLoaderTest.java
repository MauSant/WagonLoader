package com.wagonloader.app;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
        "WagonLoaderTest/fillPayload/simpleKey.json",
        "WagonLoaderTest/fillPayload/simpleList.json",
        "WagonLoaderTest/fillPayload/findSimpleKey.json",
        "WagonLoaderTest/fillPayload/findNestedObj.json",
        "WagonLoaderTest/fillPayload/findNestedList.json",
        "WagonLoaderTest/fillPayload/findAndConcat.json"
    })
    public void testFillPayload(String inputFileName) throws IOException{
        JsonNode input = readJson(inputFileName);
        JsonNode targetJson = input.get("target");
        JsonNode expected = input.get("expected");

        JsonNode actual = wagonLoader.fillPayload("", targetJson, wagonData );
        logger.info("["+inputFileName+"]"+prettyPrintObject(actual));
        Assert.assertEquals("msg", expected, actual); 

    }
}
