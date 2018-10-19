package com.ruseps.world.content.achievement;

import java.util.ArrayList;

import com.ruseps.util.Misc;
import com.ruseps.world.entity.impl.player.Player;



/**
 * 
 * @author Grant_ | Rune-server.ee | 9/25/17
 *
 */
public class AchievementHandler {

	//The player
	private Player c;
	
	
	/**
	 * Constructor
	 * @param c
	 */
	public AchievementHandler(Player c){
		this.c = c;
	}
	
	/**
	 * Responsible for incrementing a task
	 * @param task
	 * @param tier
	 */
	public void increment(int task, String tier){
		switch(tier){
		case "easy":
			if(c.easy[task] > AchievementLoader.easy.get(task).getToComplete()) {
				break;
			} else {
				c.easy[task]++;
				if(c.easy[task] == AchievementLoader.easy.get(task).getToComplete()) {
					c.easy[task]++;
					complete(AchievementLoader.easy.get(task));
				}
			}
			break;
		case "medium":
			if(c.medium[task] > AchievementLoader.medium.get(task).getToComplete()) {
				break;
			} else {
				c.medium[task]++;
				if(c.medium[task] == AchievementLoader.medium.get(task).getToComplete()) {
					c.medium[task]++;
					complete(AchievementLoader.medium.get(task));
				}
			}
			break;
		case "hard":
			if(c.hard[task] > AchievementLoader.hard.get(task).getToComplete()) {
				break;
			} else {
				c.hard[task]++;
				if(c.easy[task] == AchievementLoader.hard.get(task).getToComplete()) {
					c.hard[task]++;
					complete(AchievementLoader.hard.get(task));
				}
			}
			break;
		case "elite":
			if(c.elite[task] > AchievementLoader.elite.get(task).getToComplete()) {
				break;
			} else {
				c.easy[task]++;
				if(c.elite[task] == AchievementLoader.elite.get(task).getToComplete()) {
					c.easy[task]++;
					complete(AchievementLoader.elite.get(task));
				}
			}
			break;
		}
	}

	/**
	 * Responsible for completing an achievement
	 * @param achievement
	 */
	private void complete(Achievement achievement) {
		c.sendMessage("[Achievement]: <col=255>"+achievement.getDesc() + " has been completed!");
		c.achievementPoints += AchievementLoader.REWARD_POINTS;
		c.achievementsCompleted++;
	}
	
	/**
	 * Responsible for opening the interface and tab clicking
	 * @param tab
	 * @param initialize
	 */
	public void tab(int tab, boolean initialize){
		ArrayList<Rewards> tempReward = new ArrayList<>();
		ArrayList<Achievement> temp = new ArrayList<>();
		int[] tempAch = null;
		
		int size = 0;
		switch(c.tab){
		case 1:
			size = AchievementLoader.easy.size();
			break;
		case 2:
			size = AchievementLoader.medium.size();
			break;
		case 3:
			size = AchievementLoader.hard.size();
			break;
		case 4:
			size = AchievementLoader.elite.size();
			break;
		}
		
		switch(tab){
		case 1:
			temp = AchievementLoader.easy;
			tempAch = c.easy;
			tempReward = AchievementLoader.easyRewards;
			break;
		case 2:
			temp = AchievementLoader.medium;
			tempAch = c.medium;
			tempReward = AchievementLoader.mediumRewards;
			break;
		case 3:
			temp = AchievementLoader.hard;
			tempAch = c.hard;
			tempReward = AchievementLoader.hardRewards;
			break;
		case 4:
			temp = AchievementLoader.elite;
			tempAch = c.elite;
			tempReward = AchievementLoader.eliteRewards;
			break;
		}
		
		if(size < temp.size()){
			size = temp.size();
		}

		//This has been optimized for minimum load time.
		for(int i = 0;i<size;i++){
			if(i<temp.size()){
				Achievement a = temp.get(i);
				c.getPA().sendItemOnInterface(53404 + (i*5), a.getItemId(), 0, 1);
				c.getPA().sendFrame126(a.getDesc(), 53401 + (i*5));
				if(tempAch[i] > a.getToComplete()){
					c.getPA().sendFrame126("Progress: " + a.getToComplete() + "/" +a.getToComplete(), 53402 + (i*5));
				} else {
					c.getPA().sendFrame126("Progress: " + tempAch[i] + "/" +a.getToComplete(), 53402 + (i*5));
				}
				c.getPA().sendFrame126("Skillset: " + a.getSkill(), 53403 + (i*5));
			} else {
				c.getPA().sendItemOnInterface(53404 + (i*5), -1, 0, 1);
				c.getPA().sendFrame126("", 53401 + (i*5));
				c.getPA().sendFrame126("", 53402 + (i*5));
				c.getPA().sendFrame126("", 53403 + (i*5));
			}
		}		

		//Could be optimized, however not completely neccessary.
		for(int i = 0;i<30;i++){
			if(i<tempReward.size()){
				c.getPA().sendItemOnInterface(52950, tempReward.get(i).getItemId(), i, tempReward.get(i).getAmount());
			} else {
				c.getPA().sendItemOnInterface(52950, -1, i, 0);
			}
		}
		
		c.tab = tab;
		c.loading = false;
		
		//Only need to open the interface when neccesary
		if(initialize)
			c.getPA().sendInterface(53200);
	}
	
	/**
	 * Responsible for claiming the reward
	 */
	public void claim(){
		ArrayList<Rewards> tempReward = new ArrayList<>();
		ArrayList<Achievement> temp = new ArrayList<>();
		int[] tempAch = null;
		switch(c.tab){
		case 1:
			temp = AchievementLoader.easy;
			tempAch = c.easy;
			tempReward = AchievementLoader.easyRewards;
			break;
		case 2:
			temp = AchievementLoader.medium;
			tempAch = c.medium;
			tempReward = AchievementLoader.mediumRewards;
			break;
		case 3:
			temp = AchievementLoader.hard;
			tempAch = c.hard;
			tempReward = AchievementLoader.hardRewards;
			break;
		case 4:
			temp = AchievementLoader.elite;
			tempAch = c.elite;
			tempReward = AchievementLoader.eliteRewards;
			break;
		}
		
		if(c.completed[c.tab-1] == 1){
			c.sendMessage("You have already claimed this tier reward!");
			return;
		}
		
		for(int i = 0;i<temp.size();i++){
			if(tempAch[i] < temp.get(i).getToComplete()) {
				c.sendMessage("You must complete all tasks of this tier to claim this!");
				return;
			}
		}
		
		/**Add the reward code here, ill let you do this, just loop thru the rewards and put in bank or invy**/
		for(int i = 0;i<tempReward.size();i++){
			//Add the items to their bank or invy here, if invy check for space!
		}
		
		c.completed[c.tab-1] = 1;
	}
	
	public void sortByStyle(String style){
		ArrayList<Achievement> toSort = new ArrayList<>();
		for(int i = 0;i<AchievementLoader.easy.size();i++){
			if(AchievementLoader.easy.get(i).getStyle().equals(style)) {
				toSort.add(AchievementLoader.easy.get(i));
			}
		}
		for(int i = 0;i<AchievementLoader.medium.size();i++){
			if(AchievementLoader.medium.get(i).getStyle().equals(style)) {
				toSort.add(AchievementLoader.medium.get(i));
			}
		}
		for(int i = 0;i<AchievementLoader.hard.size();i++){
			if(AchievementLoader.hard.get(i).getStyle().equals(style)) {
				toSort.add(AchievementLoader.hard.get(i));
			}
		}
		for(int i = 0;i<AchievementLoader.elite.size();i++){
			if(AchievementLoader.elite.get(i).getStyle().equals(style)) {
				toSort.add(AchievementLoader.elite.get(i));
			}
		}
		
		for(int i = 0;i<toSort.size();i++){
			Achievement a = toSort.get(i);
			c.getPA().sendItemOnInterface(53404 + (i * 5), a.getItemId(), 0, 1);
			c.getPA().sendFrame126(a.getDesc(), 53401 + (i * 5));
			c.getPA().sendFrame126("Tier level: " + Misc.optimizeText(a.getTier()), 53402 + (i * 5));
			c.getPA().sendFrame126("Skillset: " + a.getSkill(), 53403 + (i * 5));
		}
		
		//Clear most of the ones below if there are any extras still sitting on the interface
		for(int i = toSort.size();i<10;i++){
			c.getPA().sendItemOnInterface(53404 + (i * 5), -1, 0, 1);
			c.getPA().sendFrame126("", 53401 + (i * 5));
			c.getPA().sendFrame126("", 53402 + (i * 5));
			c.getPA().sendFrame126("", 53403 + (i * 5));
		}
	}
}
