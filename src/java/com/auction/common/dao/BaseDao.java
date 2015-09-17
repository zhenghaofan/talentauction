package com.auction.common.dao;

import java.util.Map;

/** 
 * @author tobber
 * @version 2015年4月15日
 */

public interface BaseDao {
	/**
	 * 获取所有用户
	 * Map种必须包含3个值 tableName表名，paramsMap更新字段和值，trem条件
	 * @return
	 */
	public int baseUpdate(Map<String,Object> params); 
	
	/**
	 * @param tableName 删除数据对应表名
	 * @param trem 
	 * @return
	 */
	public int baseDelete(Map<String,Object> params); 

}
