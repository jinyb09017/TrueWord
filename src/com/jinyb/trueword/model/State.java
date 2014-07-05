/**
 * 
 */
package com.jinyb.trueword.model;

/**
 * @author jinyb
 * 下午7:45:52
 * copyright all reserved
 */
public class State {
	private int category;
	private int rank;
	private int voice;//表示false 1 表示true;
	/**
	 * @return the voice
	 */
	public int getVoice() {
		return voice;
	}
	/**
	 * @param voice the voice to set
	 */
	public void setVoice(int voice) {
		this.voice = voice;
	}
	/**
	 * @return the category
	 */
	public int getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(int category) {
		this.category = category;
	}
	/**
	 * @return the rank
	 */
	public int getRank() {
		return rank;
	}
	/**
	 * @param rank the rank to set
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}
	/**
	 * @return the isVoice
	 */


}
