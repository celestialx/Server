package com.ruseps.model.input.impl;

import com.ruseps.model.input.EnterAmount;
import com.ruseps.util.Misc;
import com.ruseps.world.entity.impl.player.Player;

public class EnterAmountToRemoveFromCoffer extends EnterAmount {

	@Override
	public void handleAmount(Player player, int amount) {
	
		if(!(player.getLmsCoffer() >= amount)) {
			player.getPA().removeAllWindows();
			player.sendMessage("@or2@[LMS] You do not have this amount in your lms coffer!");
			player.sendMessage("@or2@[LMS] Current coffer amount:" +Misc.sendCashToString(player.getLmsCoffer()));
			return;
		}
		
		if(player.getInventory().getFreeSlots() < 3) {
			player.getPA().removeAllWindows();
			player.sendMessage("@or2@[LMS] You need to have atleast 3 inventory slots free!");
			return;
		}
		
		if(player != null) {
			player.setLmsCoffer(player.getLmsCoffer() - amount);
			player.getInventory().add(995, amount);
			player.getPA().removeAllWindows();
			player.sendMessage("@or2@[LMS] A total of "+Misc.sendCashToString(amount)+" has been removed from your Lms coffer");
		}
	} 

}
