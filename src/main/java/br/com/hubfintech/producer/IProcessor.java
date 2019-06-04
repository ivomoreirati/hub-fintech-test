package br.com.hubfintech.producer;


public interface IProcessor {

	void processMessage(final String message) throws Exception;

	MessagesTypeEnum getMessageStatus();

}
