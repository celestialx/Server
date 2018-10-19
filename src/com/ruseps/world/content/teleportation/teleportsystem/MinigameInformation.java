package com.ruseps.world.content.teleportation.teleportsystem;

import com.ruseps.world.entity.impl.player.Player;

public class MinigameInformation {

	public static void handleInformation(int buttonId, Player player) {
		for (MinigameInformationEnum bie : MinigameInformationEnum.values()) {
			if (bie.getButtonId() == buttonId) {
				player.setSelectedpos(bie.getPos());
				player.getPA().sendFrame126(bie.getMiniName(), 39808);
				for (int k = 0; k < bie.getInformation().length; k++) {
					player.getPA().sendFrame126(bie.getInformation()[k], 39926 + k);
				}
			}
		}
	}
}
