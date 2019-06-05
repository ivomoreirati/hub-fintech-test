package br.com.processor.util;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class to convert miscellaneous objects  . . .
 *
 * @author Ivo Moreira
 *
 */

public class Util {
    
    static public String convertLongAuthorizationCode(Long n){
        
        return convertNumber_SizeFixed(n, '0', 6);
    }
    
    static public String convertNumber_SizeFixed(Number n, char caracterAdd, int numberFixed){
        
        return String.format("%" + caracterAdd + numberFixed + "d", n);
    }
    
    static public String convertDate(Date data){
    
    	if(data != null)
    		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(data);
    	
    	return null;
    } 
    
    static public String convertBigDecimalToString(BigDecimal a){
        
        if(a != null)
            return a.setScale(2, RoundingMode.HALF_UP).toString().replace(".", ",");

        return null;
    }
    
    static public BigDecimal convertStringToBigDecimal(String s){
        
        if(s != null)
            return new BigDecimal(s.replace(',', '.'));
        
        return null;
    }
    
    static public String readLinesBufferedReader(BufferedReader br, String finalizacao){
        
        StringBuilder sb = new StringBuilder();
        
        if(br != null){
            
            String line;
            
            try{
                while((line = br.readLine()) != null) {

                    sb.append(line);

                    if(line.contains(finalizacao))
                        break;
                }
            
                return sb.toString();
                
            }catch(Exception e){
                
                System.err.println("Error read lines BufferedReader. " + e.getMessage());
            }
        }
        
        return null;
    }
}
