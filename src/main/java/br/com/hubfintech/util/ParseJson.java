package br.com.hubfintech.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Class for parse json to objects . . .
 *
 * @author Ivo Moreira
 *
 */

public class ParseJson
{
    
    static private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    
    static public String convertOBJ_JSON(Object obj){
        
        try{
            return OBJECT_MAPPER.writeValueAsString(obj);
            
        }catch(JsonProcessingException e){
            
            System.err.println("Error converter object json. " + e.getMessage());
            
            return null;
        }
    }
    
    static public Object convertJSON_OBJ(String json, Class classe){
        
        try{
            return OBJECT_MAPPER.readValue(json, classe);
            
         }catch(IOException e){
            
             System.err.println("Error converter object json. " + e.getMessage());
            
             return null;
        }
    }
}
