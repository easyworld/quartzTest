package com.dhu.quartzTest.util;

import java.util.Calendar;

public class DateUtil {
	/**
	 * 生成 当前时间yyyy-mm-dd hh:MM:ss
	 * 
	 * @return
	 */
	public static String getMyDate() {
		Calendar c = Calendar.getInstance();
		return String.format("%4d-%02d-%02d %02d:%02d:%02d",
				c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1,
				c.get(Calendar.DATE), c.get(Calendar.HOUR_OF_DAY),
				c.get(Calendar.MINUTE), c.get(Calendar.SECOND));
	}
}
