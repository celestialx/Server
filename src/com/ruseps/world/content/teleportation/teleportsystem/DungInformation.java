package com.ruseps.world.content.teleportation.teleportsystem;

import com.ruseps.world.entity.impl.player.Player;

public class DungInformation {

	public static void handleInformation(int buttonId, Player player) {
		for (DungInformationEnum bie : DungInformationEnum.values()) {
			if (bie.getButtonId() == buttonId) {
				player.setSelectedpos(bie.getPos());
				player.getPA().sendFrame126(bie.getDungName(), 38808);
				for (int k = 0; k < bie.getInformation().length; k++) {
					player.getPA().sendFrame126(bie.getInformation()[k], 38926 + k);
				}
			}
		}
	}
}
