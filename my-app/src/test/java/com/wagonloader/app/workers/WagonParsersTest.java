package com.wagonloader.app.workers;




import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


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
        List<String> actual = wagonParsers.SplitParser("X(a,b), Z, Y()");
        List<String> expected = new ArrayList<>();
        expected.add("X(a,b)");
        expected.add("Z");
        expected.add("Y()");
        Assert.assertEquals("msg", expected, actual); 
    }

    @Test 
    public void testSplitParserSimpleComma(){
        List<String> actual = wagonParsers.SplitParser("X, Z, Y");
        List<String> expected = new ArrayList<>();
        expected.add("X");
        expected.add("Z");
        expected.add("Y");
        Assert.assertEquals("msg", expected, actual); 
    }

    @Test 
    public void testSplitParserOnlyChar(){
        List<String> actual = wagonParsers.SplitParser("X");
        List<String> expected = new ArrayList<>();
        expected.add("X");
        Assert.assertEquals("msg", expected, actual); 
    }

    @Test 
    public void testSplitParserMultipleNested(){
        List<String> actual = wagonParsers.SplitParser("X, Z(a,b), Y(c,d)");
        List<String> expected = new ArrayList<>();
        expected.add("X");
        expected.add("Z(a,b)");
        expected.add("Y(c,d)");
        Assert.assertEquals("msg", expected, actual); 
    }

    @Test 
    public void testSplitParserNestedAtLast(){
        List<String> actual = wagonParsers.SplitParser("X, Z, Y(c,d)");
        List<String> expected = new ArrayList<>();
        expected.add("X");
        expected.add("Z");
        expected.add("Y(c,d)");
        Assert.assertEquals("msg", expected, actual); 
    }

    @Test 
    public void testSplitParserSuperNested(){
        List<String> actual = wagonParsers.SplitParser("Z(F(),K(a,L(a,b)))");
        List<String> expected = new ArrayList<>();
        expected.add("Z(F(),K(a,L(a,b)))");
        Assert.assertEquals("msg", expected, actual); 
    }

   

   

    // @Test
    // public void testRecursionEvaluateWithMoreParams(){
    //     Object actual = wagonWorkers.evaluate(createValueNode("FIND(CONCAT(key1,key2), user123, FIND(key1))"), wagonData);
    //     Assert.assertEquals("msg", wagonData.get("value1"), actual); 
    // }


}
