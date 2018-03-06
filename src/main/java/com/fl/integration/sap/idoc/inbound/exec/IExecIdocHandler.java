/**
 * #  *  IExecIdocHandle.java Create on 2012-4-27 上午10:20:30
 * #  *
 * #  * Copyright (c) 2012 by biz.
 * #
 */
package com.fl.integration.sap.idoc.inbound.exec;


import com.sap.conn.idoc.IDocDocument;

/**
 * @author 作者 yangyz
 * @version 创建时间：2012-4-27 上午10:20:30 类说明
 */
public interface IExecIdocHandler extends IMessageType {

	public void execute(IDocDocument doc);
}
