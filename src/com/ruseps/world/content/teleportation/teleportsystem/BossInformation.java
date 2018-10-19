package com.ruseps.world.content.teleportation.teleportsystem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ruseps.model.Item;
import com.ruseps.model.definitions.NPCDrops;
import com.ruseps.model.definitions.NpcDefinition;
import com.ruseps.model.definitions.NPCDrops.DropChance;
import com.ruseps.model.definitions.NPCDrops.NpcDropItem;
import com.ruseps.world.content.transportation.TeleportHandler;
import com.ruseps.world.entity.impl.player.Player;

public class BossInformation {
	
	public static int arrayMax(int[] arr) {
		int max = Integer.MIN_VALUE;

	    for(int cur: arr)
	        max = Math.max(max, cur);

	    return max;
	}
	/**
	 * 
	 * @param buttonId
	 * @param player
	 */
	public static void handleInformation(int buttonId, Player player) {
		if (player.bossInterface == 0) {
			player.bossInterface = -28705;
		} else {
			player.bossInterface = buttonId;
		}
		player.getPacketSender().resetItemsOnInterface(36921, 60);
		for (BossInformationEnum bie : BossInformationEnum.values()) {
			if (bie.getButtonId() == player.bossInterface) {
				player.setSelectedpos(bie.getPos());
				player.getPA().sendFrame126(bie.getBossName(), 36808);
				
				for (int i = 0; i < bie.getItemdisplay().length; i++) {
					player.getPA().sendItemOnInterface(36921, bie.getItemdisplay()[i][0], i,
							bie.getItemdisplay()[i][1]);
				}
			
				for (int k = 0; k < bie.getInformation().length; k++) {
					player.getPA().sendFrame126(bie.getInformation()[k], 36926 + k);
				}
			}
		
		}
		
		player.getPacketSender().sendInterface(36800);

	}
	public static void handleInformationTele(int buttonId, Player player) {
		
		for (BossInformationEnum bie : BossInformationEnum.values()) {
			if (bie.getButtonId() == buttonId) {
				player.setSelectedpos(bie.getPos());
				
				TeleportHandler.teleportPlayer(player, bie.getPos(), player.getSpellbook().getTeleportType());
			}
		}
	}
	/**
	 * 
	 * @param buttonId
	 * @param player
	 */
/*	public static void handleWildyInformation(int buttonId, Player player) {
		for (BossInformationEnum bie : BossInformationEnum.values()) {
			if (bie.getButtonId() == buttonId) {
				player.setSelectedpos(bie.getPos());
				player.getPA().sendFrame126(bie.getBossName(), 37808);
				for (int i = 0; i < bie.getItemdisplay().length; i++) {
					player.getPA().sendItemOnInterface(37921, bie.getItemdisplay()[i][0], i,
							bie.getItemdisplay()[i][1]);
				}
				for (int i = 0; i < bie.getItemdisplay().length; i++) {
					player.getPA().sendItemOnInterface(36921, -1, i, bie.getItemdisplay()[i][1]);
				}
			
				for (int k = 0; k < bie.getInformation().length; k++) {
					player.getPA().sendFrame126(bie.getInformation()[k], 37926 + k);
				}
			}
		}
	}*/
}
