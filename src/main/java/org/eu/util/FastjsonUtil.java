package org.eu.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class FastjsonUtil {

	/**
	 * 获取json对象中的int类型的值<br>
	 * 若值为空返回null
	 * 
	 * @param jsonObject
	 * @param key
	 * @return 获取的int值
	 */
	public static Integer getIntValue(JSONObject jsonObject, String key) {
		return StringUtil.isEmpty(jsonObject.getString(key)) ? null : jsonObject.getIntValue(key);
	}

	/**
	 * json字符串转成Integer类型的List集合
	 * 
	 * @param str      json字符串
	 * @param listName 需要获取的对象名
	 * @return Integer类型的List集合
	 */
	public static List<Integer> toIntegerList(String str, String listName) {
		// 获取jsonobject对象
		JSONObject obj = JSONObject.parseObject(str);
		// 获取的结果集合转换成数组
		JSONArray jsonArray = obj.getJSONArray(listName);
		// 将array数组转换成字符串
		String jssonString = JSONObject.toJSONString(jsonArray, SerializerFeature.WriteClassName);
		// 把字符串转换成集合
		List<Integer> list = JSON.parseArray(jssonString, Integer.class);
		return list;
	}

	/**
	 * json字符串转成String类型的List集合
	 * 
	 * @param str      json字符串
	 * @param listName 需要获取的对象名
	 * @return Integer类型的List集合
	 */
	public static List<String> toStringList(String str, String listName) {
		// 获取jsonobject对象
		JSONObject obj = JSONObject.parseObject(str);
		// 获取的结果集合转换成数组
		JSONArray jsonArray = obj.getJSONArray(listName);
		// 将array数组转换成字符串
		String jssonString = JSONObject.toJSONString(jsonArray, SerializerFeature.WriteClassName);
		// 把字符串转换成集合
		List<String> list = JSON.parseArray(jssonString, String.class);
		return list;
	}

	/**
	 * 将 fastjson的JSONArray转化为泛型列表
	 * 
	 * @param jsonArray 源数据
	 * @param clz       泛型类
	 * @param <T>       泛型
	 * @return list
	 */
	public static <T> List<T> convertJSONArrayToTypeList(JSONArray jsonArray, Class<T> clz) {
		if (CollectionUtils.isEmpty(jsonArray)) {
			return new ArrayList<T>();
		}
		List<T> result = new ArrayList<T>(jsonArray.size());
		jsonArray.forEach(element -> {
			
			// 基础类型不可以转化为JSONObject，需要特殊处理
			if (element instanceof String || element instanceof Number || element instanceof Boolean) {
				result.add((T) element);
			} else {
				T t = JSONObject.toJavaObject((JSONObject) element, clz);
				result.add(t);
			}
		});
		return result;
	}

}
