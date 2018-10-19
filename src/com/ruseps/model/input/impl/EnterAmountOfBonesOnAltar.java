package com.ruseps.model.input.impl;

import com.ruseps.GameSettings;
import com.ruseps.model.definitions.ItemDefinition;
import com.ruseps.model.input.EnterAmount;
import com.ruseps.util.Misc;
import com.ruseps.world.entity.impl.player.Player;

public class EnterAmountOfBonesOnAltar extends EnterAmount {

	@Override
	public void handleAmount(Player player, int amount) {
		player.getInventory().add(amount, GameSettings.MAX_INT);
	}

}

