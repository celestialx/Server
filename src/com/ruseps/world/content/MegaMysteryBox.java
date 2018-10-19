package com.ruseps.world.content;

import com.ruseps.model.Animation;
import com.ruseps.model.Graphic;
import com.ruseps.model.GraphicHeight;
import com.ruseps.util.Misc;
import com.ruseps.util.RandomUtility;
import com.ruseps.world.entity.impl.player.Player;

public class MegaMysteryBox {
	
	
	
	private static final Graphic gfx1 = new Graphic(199, 3, GraphicHeight.LOW);
	private static final Animation anim = new Animation(2107);
	private static final Graphic gfx2 = new Graphic(2128, 3, GraphicHeight.LOW);


	/*
	 * 
	 */
	
	
	
	/*
	 * Rewards
	 */
	public static final int [] shitRewards = {4151, 6585, 2572, 6199,};
	public static final int [] goodRewards = {14484, 11696, 11613, 19780, 13263, 18349, 18351,
			18353, 18355, 18357, 18359, 20000, 20001, 20002};
	
	/*
	 * Handles the opening of the donation box
	 */
	public static void open (Player player) {
		if (player.getInventory().getFreeSlots() < 3) {
			player.getPacketSender().sendMessage("You need at least 3 inventory spaces");
			return;
		}
		if (player.getClickDelay().elapsed(1000)) {
			player.forceChat("");
		player.performGraphic(gfx1);
		player.performAnimation(anim);
		player.performGraphic(gfx2);
		player.getInventory().delete(6201, 1);
		giveReward(player);
		player.getPacketSender().sendMessage("Congratulations on your reward!");
		player.getClickDelay().reset();
        player.getPacketSender().sendMessage("You Must wait a few seconds before clicking again.");
		}
	}
	
	/*
	 * Gives the reward base on misc Random chance
	 */
	public static void giveReward(Player player) {
		/*
		 * 1/3 Chance for a good reward
		 */
		if (RandomUtility.RANDOM.nextInt(3) == 2) {
			player.getInventory().add(goodRewards[Misc.getRandom(goodRewards.length - 1)], 1);
		} else {
			player.getInventory().add(shitRewards[Misc.getRandom(shitRewards.length - 1)], 1);

		}
		/*
		 * Adds 5m + a random amount up to 100m every box
		 * Max cash reward = 105m
		 */
		player.getInventory().add(995, 10000000 + RandomUtility.RANDOM.nextInt(100000000));
	}

}
