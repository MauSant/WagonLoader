package com.wagonloader.app.workers;




import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ValueNode;


public class WagonParsersTest {
    ObjectMapper mapper = new ObjectMapper();
    WagonParsers wagonParsers = new WagonParsers();
    private static final Logger logger = Logger.getLogger(WagonParsersTest.class.getName());

    public JsonNode readJson(String fileName){
        InputStream stream = WagonParsersTest.class.getResourceAsStream("../../../../"+fileName);
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

    




    @Test 
    public void testSplitParserOneNested(){
        JsonNode actual = wagonParsers.SplitParser("X(a,b), Z, Y()");
        ArrayNode expected = mapper.getNodeFactory().arrayNode();
        expected.add("X(a,b)");
        expected.add("Z");
        expected.add("Y()");
        Assert.assertEquals("msg", expected, actual); 
    }

    @Test 
    public void testSplitParserSimpleComma(){
        JsonNode actual = wagonParsers.SplitParser("X, Z, Y");
        ArrayNode expected =mapper.getNodeFactory().arrayNode();
        expected.add("X");
        expected.add("Z");
        expected.add("Y");
        Assert.assertEquals("msg", expected, actual); 
    }

    @Test 
    public void testSplitParserOnlyChar(){
        JsonNode actual = wagonParsers.SplitParser("X");
        ArrayNode expected =mapper.getNodeFactory().arrayNode();
        expected.add("X");
        Assert.assertEquals("msg", expected, actual); 
    }

    @Test 
    public void testSplitParserMultipleNested(){
        JsonNode actual = wagonParsers.SplitParser("X, Z(a,b), Y(c,d)");
        ArrayNode expected =mapper.getNodeFactory().arrayNode();
        expected.add("X");
        expected.add("Z(a,b)");
        expected.add("Y(c,d)");
        Assert.assertEquals("msg", expected, actual); 
    }

    @Test 
    public void testSplitParserNestedAtLast(){
        JsonNode actual = wagonParsers.SplitParser("X, Z, Y(c,d)");
        ArrayNode expected =mapper.getNodeFactory().arrayNode();
        expected.add("X");
        expected.add("Z");
        expected.add("Y(c,d)");
        Assert.assertEquals("msg", expected, actual); 
    }

    @Test 
    public void testSplitParserSuperNested(){
        JsonNode actual = wagonParsers.SplitParser("Z(F(),K(a,L(a,b)))");
        ArrayNode expected =mapper.getNodeFactory().arrayNode();
        expected.add("Z(F(),K(a,L(a,b)))");
        Assert.assertEquals("msg", expected, actual); 
    }

   

   

    // @Test
    // public void testRecursionEvaluateWithMoreParams(){
    //     Object actual = wagonWorkers.evaluate(createValueNode("FIND(CONCAT(key1,key2), user123, FIND(key1))"), wagonData);
    //     Assert.assertEquals("msg", wagonData.get("value1"), actual); 
    // }


}
