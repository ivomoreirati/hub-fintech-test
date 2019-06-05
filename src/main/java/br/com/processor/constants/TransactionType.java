package br.com.processor.constants;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Class enum for transaction type . . .
 *
 * @author Ivo Moreira
 *
 */

public enum TransactionType {
    
    WITHDRAW("withdraw");
	
	@JsonIgnore
	String type;
	
	TransactionType(String type){
		this.setType(type);
    }
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
