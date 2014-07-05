/**
 * 
 */
package com.jinyb.trueword.util;

import com.jinyb.trueword.model.State;

/**
 * @author jinyb
 * 下午8:04:41
 * copyright all reserved
 */
public class StateInstance {
	//state的单例对象，用于全局。
	private static State state;
	
	public static State getStateInstance(){
		if(state == null){
			state = new State();
		}
		return state;
	}
	/**
	 * 
	 * @return
	 */
	
	public static String getTip(){
		String tip = "当前设置:";
		switch(state.getCategory()){
		case 0:tip = tip + "真心话   ";
		break;
		case 1:tip = tip + "大冒险   ";
		break;
		}
		switch(state.getRank()){
		case 0:tip = tip + "小清新  ";
		break;
		case 1:tip = tip + "重口味   ";
		break;
		case 2:tip = tip + "无节操   ";
		break;
		}
		
		switch(state.getVoice()){
		case 1:tip = tip + "打开声音   ";
		break;
		case 0:tip = tip + "关闭声音   ";
		break;
		}
		
		return tip;
	}
	

}
