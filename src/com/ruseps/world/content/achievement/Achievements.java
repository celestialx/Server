package com.ruseps.world.content.achievement;

public enum Achievements {
	
	EASY_0("Kill 10 Rock Crabs", 10, 6570, "Combat", "PvM"),
	EASY_1("Bury 20 (Any) Bones", 20, 6570, "Prayer", "Skill"),//
	EASY_2("Kill 10 Players", 10, 3450, "Combat", "PvP"), // 
	EASY_3("Pick 30 Flaxes", 30, 3450, "N/A", "Skill"), 
	EASY_4("Light 30 Fires", 30, 3450, "Firemaking", "Skill"),//
	EASY_5("Complete 1 Slayer Task", 1, 3450, "Combat", "PvM"),
	EASY_6("Steal From A Stall 50 Times", 50, 3450, "Thieving", "Skill"),//
	EASY_7("Chop 30 (Any) Logs", 30, 3450, "Woodcutting", "Skill"),//
	EASY_8("Mine 15 Copper Ore", 15, 3450, "Mining", "Skill"),//
	EASY_9("Mine 15 Tin Ore", 15, 3450, "Mining", "Skill"),//
	EASY_10("Mine 50 Rune Essence", 15, 3450, "Mining", "Skill"),//
	EASY_11("Farm One Guam", 1, 3450, "Farming", "Skill"),
	EASY_12("Vote for Polaris-Ps", 1, 3450, "N/A", "Skill"),
	EASY_13("Craft 100 Air Runes", 100, 3450, "Runecrafting", "Skill"),//
	EASY_14("Get Your Total Level To 1000", 1000, 3450, "N/A", "Skill"),//
	EASY_15("Complete Gnome Agility 10 Times", 10, 3450, "Agility", "Skill"),
	EASY_16("Fletch 300 Arrows", 300, 3450, "Fletching", "Skill"),
	EASY_17("Wear One 99 Skill Cape", 1, 3450, "N/A", "Skill"),
	EASY_18("Smith 40 Bronze Bars", 40, 3450, "Smithing", "Skill"),
	
	MEDIUM_0("Kill 100 Rockcrabs", 100, 526, "Combat", "PvM"),
	MEDIUM_1("Kill 50 Players", 5, 526, "Combat", "PvM"),
	MEDIUM_2("Win A Duel Arena Fight", 5, 526, "Combat", "PvP"),
	MEDIUM_3("Thieve 500k From Stalls", 500000, 526, "Combat", "Skill"),
	MEDIUM_4("Kill 20 Blue Dragons", 20, 526, "Combat", "PvM"),
	MEDIUM_5("Bury 100 (Any) Bones", 100, 526, "Combat", "Skill"),
	MEDIUM_6("Fish 200 Sharks", 200, 526, "Combat", "Skill"),
	MEDIUM_7("Mine 200 Adamant Ores", 200, 526, "Combat", "Skill"),
	MEDIUM_8("Mine 50 Runite Ores", 50, 526, "Combat", "Skill"),
	MEDIUM_9("Chop 200 Yew Logs", 200, 526, "Combat", "Skill"),
	MEDIUM_10("Get A Killstreak Of 3", 3, 526, "Combat", "PvP"),
	MEDIUM_11("Vote 10 Times For Polaris-Ps", 10, 526, "Combat", "Skill"),
	
	HARD_0("Do Z", 100, 4155, "Fletching", "Skill"),
	
	ELITE_0("Do ZD", 100, 4157, "Fletching", "Skill");
	
	private String achievement;
	private int toComplete;
	private int itemId;
	private String skill;
	private String style;
	
	Achievements(String achievement, int toComplete, int itemId, String skill, String style){
		this.setAchievement(achievement);
		this.setToComplete(toComplete);
		this.setItemId(itemId);
		this.setSkill(skill);
		this.setStyle(style);
	}

	public String getAchievement() {
		return achievement;
	}

	public void setAchievement(String achievement) {
		this.achievement = achievement;
	}

	public int getToComplete() {
		return toComplete;
	}

	public void setToComplete(int toComplete) {
		this.toComplete = toComplete;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}
}
