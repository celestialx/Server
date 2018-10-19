/*package com.ruseps.world.content;

import java.util.ArrayList;

import com.ruseps.model.Item;
import com.ruseps.model.definitions.ItemDefinition;
import com.ruseps.util.Misc;
import com.ruseps.world.entity.impl.player.Player;

public class VoteBox {
	
	private final static int BOX_ID = 6120;
	
	public static int[][] rewards = {
			//COMMON
			{989, 1, 0},
			{5021, 1, 0},


			//UNCOMMON
			{19618, 1, 1},
			{6929, 1, 1},
			{4639, 1, 1},
			{939, 1, 1},
			{9396, 1, 1},
			{4067, 100, 1},

			//RARE
			{5257, 1, 2},
			{5258, 1, 2},
			{5256, 1, 2},
			{5255, 1, 2},
			{6193, 1, 2},

			//VERY RARE
			{18784, 1, 3},
			{4067, 1000, 3},
			{13561, 1, 3},
			{4761, 1, 3},
			{14940, 1, 3},
			{13281, 1, 3},
	};

	public static void openBox(Player player) {
		player.getPA().sendVoteBox();
		for(int i = 0; i < rewards.length; i++) {
			player.getPA().sendItemOnInterface(21822, i, rewards[i][0], rewards[i][1]);
		}
			for(int x = 0; x < 12; x++) {
				player.getPA().sendItemOnInterface(21831, x, -1, 1);
			}
		player.getPA().sendInterface(21800);
	}

	public static void spinBox(Player player) {

		if(!player.getInventory().contains(BOX_ID)) {
			player.sendMessage("You do not have a Vote Box to spin with!");
			return;
		}

		if(player.winnerIndex != -1) {
			player.sendMessage("Please wait before your current spin is done.");
			return;
		}

		double random = Misc.random(100);
		int rarity = 0;

		if(random >= 99.9)
			rarity = 3;
		else if(random >= 95)
			rarity = 2;
		else if(random >= 75)
			rarity = 1;

		ArrayList<Integer> indexes = new ArrayList<Integer>();
		for(int i = 0; i < rewards.length; i++) {
			if(rewards[i][2] == rarity) {
				indexes.add(i);
			}
		}

		int won = indexes.get(Misc.random(indexes.size()));

		player.getInventory().delete(BOX_ID, 1);
		player.winnerIndex = won;
		player.getPA().sendWinnerIndex(won);
	}

	public static void openQuickly(Player player) {

		if(!player.getInventory().contains(BOX_ID)) {
			player.sendMessage("You do not have a vote box to open!");
			return;
		}

		double random = Misc.random(0, 100);
		double roundOff = (double) Math.round(random * 100) / 100;
		int rarity = 0;

		if(roundOff >= 99.9)
			rarity = 3;
		else if(roundOff >= 95)
			rarity = 2;
		else if(roundOff >= 75)
			rarity = 1;

		ArrayList<Integer> indexes = new ArrayList<Integer>();
		for(int i = 0; i < rewards.length; i++) {
			if(rewards[i][2] == rarity) {
				indexes.add(i);
			}
		}

		int won = indexes.get(Misc.random(indexes.size()));

		player.getItems().deleteItem2(BOX_ID, 1);
		player.winnerIndex = won;
		handleReward(player);

	}

	public static void handleReward(Player player) {

		if(player.winnerIndex == -1)
			return;

		Item winner = new Item(rewards[player.winnerIndex][0], rewards[player.winnerIndex][1]);

		player.getInventory().add(winner.id, winner.amount);
		String a = winner.amount > 1 ? "some" : "a";
		String b = winner.amount > 1 ? "s" : "";
		player.sendMessage("You receive "+a+" @cya@"+ItemDefinition.forId(winner.id)+""+b+"@cya@ from the Vote Box.");
		player.winnerIndex = -1;
	
			for(int x = 0; x < 12; x++) {
				player.getPA().sendItemOnInterface(21831, x, -1, 1);
			}
	}

}*/