package com.dhu.quartzTest.util;

import net.sf.json.JSONArray;

public class JSONUtil {
	/**
	 * 将自定义的json格式转化成post请求参数 name1=value1&name2=value2的形式
	 * 
	 * @param str
	 *            [[1,2],[2,3]]这种形式
	 * @return
	 */
	public static String myJson2ParamString(String str) {
		JSONArray json = JSONArray.fromObject(str);
		JSONArray temp;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < json.size(); i++) {
			temp = JSONArray.fromObject(json.get(i));
			sb.append(temp.get(0) + "=" + temp.get(1));
			if (i != json.size() - 1)
				sb.append("&");
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		String testJson = "[[1,2],[2,3],['这是中文','也可以吗']]";
		System.out.println(myJson2ParamString(testJson));
	}
}
