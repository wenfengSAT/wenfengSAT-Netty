package com.netty.model;

import lombok.Data;

import java.io.Serializable;

/**
 * server-client之间传输的消息
 */
@Data
public class TransformData implements Serializable {
	private static final long serialVersionUID = -812456010484377692L;

	/**
	 * id
	 */
	private String id;

	/**
	 * 消息的名字
	 */
	private String name;

	/**
	 * 消息的内容
	 */
	private String message;

}
