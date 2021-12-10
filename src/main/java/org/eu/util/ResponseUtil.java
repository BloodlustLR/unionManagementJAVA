package org.eu.util;

import com.alibaba.fastjson.JSON;
import org.eu.config.response.CommonResp;
import org.eu.config.response.CommonRespT;
import org.eu.config.response.ResponseEnum;

/**
 * 统一请求返回结果封装工具类
 * 
 * @author: 王越
 * @date: 2019年6月5日 上午10:50:13
 */
public class ResponseUtil {

	/**
	 * 返回成功结果
	 *
	 * @param t 泛型对象
	 * @return 成功结果集
	 */
	public static <T> CommonRespT<T> successT(T t) {
		CommonRespT<T> resp = new CommonRespT<T>();
		resp.setCode(ResponseEnum.SUCCESS.code());
		resp.setMsg(ResponseEnum.SUCCESS.msg());
		resp.setObj(t);
		return resp;
	}

	/**
	 * 返回成功结果
	 *
	 * @param t   泛型对象
	 * @param msg 返回信息
	 * @return 成功结果集
	 */
	public static <T> CommonRespT<T> successT(T t, String msg) {
		CommonRespT<T> resp = new CommonRespT<T>();
		resp.setCode(ResponseEnum.SUCCESS.code());
		resp.setMsg(msg);
		resp.setObj(t);
		return resp;
	}

	/**
	 *
	 * @param responseEnum ResponseEnum
	 * @return 返回结果集
	 */
	public static String enumResp(ResponseEnum responseEnum) {
		CommonResp resp = new CommonResp();
		resp.setCode(responseEnum.code());
		resp.setMsg(responseEnum.msg());
		return JSON.toJSONString(resp);
	}

	/**
	 * 返回成功结果
	 *
	 * @return 成功结果集
	 */
	public static String success() {
		CommonResp resp = new CommonResp();
		resp.setCode(ResponseEnum.SUCCESS.code());
		resp.setMsg(ResponseEnum.SUCCESS.msg());
		return JSON.toJSONString(resp);
	}

	/**
	 * 返回成功结果
	 *
	 * @param obj 返回参数对象
	 * @return 成功结果集
	 */
	public static String success(Object obj) {
		CommonResp resp = new CommonResp();
		resp.setCode(ResponseEnum.SUCCESS.code());
		resp.setMsg(ResponseEnum.SUCCESS.msg());
		resp.setObj(obj);
		return JSON.toJSONString(resp);
	}

	/**
	 * 返回成功结果
	 *
	 * @param obj 返回参数对象
	 * @param msg 返回信息
	 * @return 成功结果集
	 */
	public static String success(String msg, Object obj) {
		CommonResp resp = new CommonResp();
		resp.setCode(ResponseEnum.SUCCESS.code());
		resp.setMsg(msg);
		resp.setObj(obj);
		return JSON.toJSONString(resp);
	}

	/**
	 * 返回错误结果
	 *
	 * @param obj 返回参数对象
	 * @return 错误结果集
	 */
	public static String error(Object obj) {
		CommonResp resp = new CommonResp();
		resp.setCode(ResponseEnum.ERROR.code());
		resp.setMsg(ResponseEnum.ERROR.msg());
		resp.setObj(obj);
		return JSON.toJSONString(resp);
	}

	/**
	 * 返回错误结果
	 *
	 * @param msg 返回信息
	 * @param obj 返回参数对象
	 * @return 错误结果集
	 */
	public static String error(String msg, Object obj) {
		CommonResp resp = new CommonResp();
		resp.setCode(ResponseEnum.ERROR.code());
		resp.setMsg(msg);
		resp.setObj(obj);
		return JSON.toJSONString(resp);
	}

	/**
	 * 返回错误结果
	 *
	 * @param code 错误码
	 * @param msg  返回信息
	 * @return 错误结果集
	 */
	public static String error(Integer code, String msg) {
		CommonResp resp = new CommonResp();
		resp.setCode(code);
		resp.setMsg(msg);
		return JSON.toJSONString(resp);
	}

	/**
	 * 返回错误结果
	 *
	 * @param code 错误码
	 * @param msg  返回信息
	 * @param obj  返回参数对象
	 * @return 错误结果集
	 */
	public static String error(Integer code, String msg, Object obj) {
		CommonResp resp = new CommonResp();
		resp.setCode(code);
		resp.setMsg(msg);
		resp.setObj(obj);
		return JSON.toJSONString(resp);
	}

}
