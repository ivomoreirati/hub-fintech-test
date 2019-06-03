package br.com.hubfintech.constants;

import com.fasterxml.jackson.annotation.JsonIgnore;

public enum TransactionResultCode {
    
    APPROVED("00"), INSUFFICIENT_FUNDS("51"), INVALID_ACCOUNT("14"), PROCESSING_ERROR("96");
	
	@JsonIgnore
	String code;
	
	TransactionResultCode(String code){
		this.setCode(code);
    }
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
