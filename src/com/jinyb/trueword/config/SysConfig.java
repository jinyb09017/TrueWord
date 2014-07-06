/**
 * 
 */
package com.jinyb.trueword.config;

/**
 * @author jinyb
 * 下午7:35:54
 * copyright all reserved
 */
//目前数据库中，真心话和大冒险都分为三个档次，总共有600多个条目。
public class SysConfig {
	public static Integer TRUEWORD = 0 ;//真心话
	public static Integer RISK = 1 ;//大冒险
	public static Integer LOW = 0 ;//等级
	public static Integer MID = 1 ;
	public static Integer HIGH = 2 ;
	
	
	public static class Constants{
		public static final int SETTING = 1;
		public static final int NOTICE = 2;
	}

}
