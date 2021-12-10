package org.eu.config.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Swagger统一返回值(泛型),用于在swagger或knife4j上显示<br>
 * 所有
 * 
 * @author wangyue
 * @date 2020-04-23 01:07:43
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonRespT<T> {

	private Integer code;

	private String msg;

	private T obj;

}
