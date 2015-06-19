package com.dhu.quartzTest.dao;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.dhu.quartzTest.util.Page;

/**
 * 基础Dao 对jdbcTemplate进行薄封装，加入分页等功能
 * 
 * @author RoyZ
 *
 */
@Repository
public class BaseDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private JdbcTemplate jdbcTemplate2;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public JdbcTemplate getJdbcTemplate2() {
		return jdbcTemplate2;
	}

	public int insert(String sql, Object... args) {
		return jdbcTemplate.update(sql, args);
	}

	public int update(String sql, Object... args) {
		return jdbcTemplate.update(sql, args);
	}

	public int delete(String sql, Object... args) {
		return jdbcTemplate.update(sql, args);
	}

	public List<Map<String, Object>> queryForList(String sql, Object... args) {
		return jdbcTemplate.queryForList(sql, args);
	}

	public List<Map<String, Object>> queryForList(String sql) {
		return jdbcTemplate.queryForList(sql);
	}

	public Map<String, Object> queryForMap(String sql, Object... args) {
		return jdbcTemplate.queryForMap(sql, args);
	}

	public Page queryForPage(String sql, int start, int limit, Object... args) {
		String countQueryString = " select count(*) "
				+ removeSelect(removeOrders(sql));
		long totalCount = jdbcTemplate.queryForObject(countQueryString, args,
				Integer.class);
		if (totalCount < 1)
			return new Page();
		String querySQL = String.format("%s limit %d,%d", sql, start, limit);
		List<Map<String, Object>> list;
		if (null == args || args.length == 0) {
			list = jdbcTemplate.queryForList(querySQL);
		} else {
			list = jdbcTemplate.queryForList(querySQL, args);
		}
		return new Page(start, totalCount, limit, list);

	}

	private static String removeSelect(String sql) {
		Assert.hasText(sql);
		int beginPos = sql.toLowerCase().indexOf("from");
		Assert.isTrue(beginPos != -1, "sql: " + sql
				+ " must has a keyword 'from'");
		return sql.substring(beginPos);
	}

	private static String removeOrders(String sql) {
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(sql);
		StringBuffer sb = new StringBuffer();
		while (m.find())
			m.appendReplacement(sb, "");
		m.appendTail(sb);
		return sb.toString();
	}
}
