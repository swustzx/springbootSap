package com.fl.integration.sap.idoc.listener;

import org.springframework.messaging.Message;

/**
 * @author david
 * @create 2018-03-06 14:33
 **/
public interface GenericMessageListener<T> extends IdocDataListener<T> {

	Message<?> onMessage(T data);
}
