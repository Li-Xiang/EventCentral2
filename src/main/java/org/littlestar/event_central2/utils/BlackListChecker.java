package org.littlestar.event_central2.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.littlestar.event_central2.entity.BlackList;
import org.littlestar.event_central2.repository.BlackListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class BlackListChecker {
	final long maxTtl = 60 * 1000L; 
	final List<String> keys = new ArrayList<String>();
	long lastLoad = 0L;
	@Autowired
	BlackListRepository blackListRepository;
	
	public boolean check(String line, String category) {
		long now = new Date().getTime();
		long ttl = now - lastLoad;
		// 对实时性不高, 定时更新, 避免大量查询冲击数据库
		if (ttl > maxTtl) {
			keys.clear();
			List<BlackList> blackList = blackListRepository.findByCategory(category);
			for (BlackList e : blackList) {
				keys.add(e.getKeyword());
			}
			lastLoad = now;
		}
		return containKeys(line, keys);
	}
	
	/**
	 * 如果line中包含keys的字符串，返回true，否则返回false。
	 * @param line
	 * @param keys
	 * @return
	 */
	public static boolean containKeys(String line, List<String> keys) {
		if (line == null || keys == null) {
			return false;
		}
		for (String s : keys) {
			if (!isEmpty(s)) {
				if (line.contains(s.trim())) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean isEmpty(String s) {
		return s == null || s.length() == 0;
	}
}
