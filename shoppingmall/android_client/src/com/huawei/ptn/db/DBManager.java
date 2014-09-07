package com.huawei.ptn.db;

import com.huawei.ptn.config.ConfigManager;
import com.huawei.ptn.db.manager.IDBCommon;
import com.huawei.ptn.db.manager.impl.DBCacheManager;
import com.huawei.ptn.db.manager.impl.DBHistoryManager;

public class DBManager {
	
	public static IDBCommon getDBManager(String name) {
		if (IDBCommon.MODULE_CACHE.equals(name)) {
			return new DBCacheManager(ConfigManager.getApplicationContext());
		} else if (IDBCommon.MODULE_HISTORY.equals(name)) {
			return new DBHistoryManager(ConfigManager.getApplicationContext());
		}
		
		return null;
	}

}
