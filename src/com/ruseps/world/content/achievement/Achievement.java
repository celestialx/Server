package com.ruseps.world.content.achievement;

public class Achievement {

	private String description;
	private int toComplete;
	private String tier;
	private int itemId;
	private String skill;
	private String style;
	
	public Achievement(String description, int toComplete, String tier, int itemId, String skill, String style){
		this.description = description;
		this.toComplete = toComplete;
		this.tier = tier;
		this.itemId = itemId;
		this.skill = skill;
		this.setStyle(style);
	}
	
	public String getDesc(){
		return description;
	}
	
	public int getToComplete(){
		return toComplete;
	}

	public String getTier() {
		return tier;
	}
	
	public int getItemId(){
		return itemId;
	}
	
	public String getSkill(){
		return skill;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

}
