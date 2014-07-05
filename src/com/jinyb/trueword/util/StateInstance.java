/**
 * 
 */
package com.jinyb.trueword.util;

import com.jinyb.trueword.model.State;

/**
 * @author jinyb
 * ����8:04:41
 * copyright all reserved
 */
public class StateInstance {
	//state�ĵ�����������ȫ�֡�
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
		String tip = "��ǰ����:";
		switch(state.getCategory()){
		case 0:tip = tip + "���Ļ�   ";
		break;
		case 1:tip = tip + "��ð��   ";
		break;
		}
		switch(state.getRank()){
		case 0:tip = tip + "С����  ";
		break;
		case 1:tip = tip + "�ؿ�ζ   ";
		break;
		case 2:tip = tip + "�޽ڲ�   ";
		break;
		}
		
		switch(state.getVoice()){
		case 1:tip = tip + "������   ";
		break;
		case 0:tip = tip + "�ر�����   ";
		break;
		}
		
		return tip;
	}
	

}
