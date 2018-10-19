package com.ruseps.world.content.achievement;

import java.util.ArrayList;

public class AchievementLoader {

	public static ArrayList<Achievement> easy = new ArrayList<>();
	public static ArrayList<Achievement> medium = new ArrayList<>();
	public static ArrayList<Achievement> hard = new ArrayList<>();
	public static ArrayList<Achievement> elite = new ArrayList<>();
	
	public static ArrayList<Rewards> easyRewards = new ArrayList<>();
	public static ArrayList<Rewards> mediumRewards = new ArrayList<>();
	public static ArrayList<Rewards> hardRewards = new ArrayList<>();
	public static ArrayList<Rewards> eliteRewards = new ArrayList<>();
	
	public static final int REWARD_POINTS = 5;
	public static void load(){
		for(Achievements a : Achievements.values()) {
			String[] args = a.name().split("_");
			String tier = args[0].toLowerCase();
			Achievement achieve = new Achievement(a.getAchievement(),a.getToComplete(),tier,a.getItemId(),a.getSkill(),a.getStyle());
			switch(tier){
			case "easy":
				easy.add(achieve);
				break;
			case "medium":
				medium.add(achieve);
				break;
			case "hard":
				hard.add(achieve);
				break;
			case "elite":
				elite.add(achieve);
				break;
			}
		}
		
		for(Rewards r : Rewards.values()){
			switch(r.getTier()){
			case "easy":
				easyRewards.add(r);
				break;
			case "medium":
				mediumRewards.add(r);
				break;
			case "hard":
				hardRewards.add(r);
				break;
			case "elite":
				eliteRewards.add(r);
				break;
			}
		}
		System.out.println("LOADED: " + easy.size() + " Easy, " + medium.size() + " Medium, " + hard.size() + " Hard, " + elite.size() + " Elite Achievements");
	}
	
}
