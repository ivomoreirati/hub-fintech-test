package br.com.hubfintech.producer;

public enum MessagesTypeEnum {

    CARD( "card"),
    CARD_TRANSACTION( "card_transaction");

    private String value;

    MessagesTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}