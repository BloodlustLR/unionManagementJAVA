package org.eu.config.response;

/**
 * 统一返回对象的状态码全局变量
 * 
 * @author: 王越
 * @date: 2019-06-05 00:34:19
 */
public class ResponseConstant {

	/**
	 * 请求成功。一般用于GET与POST请求
	 */
	public static final Integer SUCCESS = 200;

	/**
	 * 数据格式错误
	 */
	public static final Integer DATA_FORMAT_ERROR = 420;

	/**
	 * 服务器内部错误，无法完成请求
	 */
	public static final Integer ERROR = 500;
	
	/**
	 * 支付宝模块错误
	 */
	public static final Integer ALIPAY_ERROR = 567;

}
