package com.ruseps.model.input.impl;

import com.ruseps.model.input.EnterAmount;
import com.ruseps.util.Misc;
import com.ruseps.world.entity.impl.player.Player;

public class EnterAmountOfLmsCoins extends EnterAmount {

	@Override
	public void handleAmount(Player player, int amount) {
	
		if(!(player.getInventory().getAmount(995) >= amount)) {
			player.getPA().removeAllWindows();
			player.sendMessage("@or2@[LMS] Make sure you have the right amount of coins in your inventory!");
			return;
		}
		
		if(player != null) {
			player.getInventory().delete(995, amount);
			player.setLmsCoffer(player.getLmsCoffer() + amount);
			player.getPA().removeAllWindows();
			player.sendMessage("@or2@[LMS] You now have a total of @gre@"+Misc.sendCashToString(player.getLmsCoffer())+"@or2@ Lms coins");
		}
	}
}
