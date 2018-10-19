package com.ruseps.world.content.teleportation;

import com.ruseps.model.Locations;
import com.ruseps.model.Position;
import com.ruseps.model.Locations.Location;
import com.ruseps.util.Misc;
import com.ruseps.world.content.transportation.TeleportHandler;
import com.ruseps.world.entity.impl.player.Player;

public class Teleporting {

	private static int[][] LINE_IDS = { { 61250, 61300 }, { 61251, 61301 }, { 61252, 61302 }, { 61253, 61303 },
			{ 61254, 61304 }, { 61255, 61305 }, { 61256, 61306 }, { 61257, 61307 }, { 61258, 61308 }, { 61259, 61309 },
			{ 61260, 61310 }, { 61261, 61311 }, { 61262, 61312 }, { 61263, 61313 }, { 61264, 61314 }, { 61265, 61315 },
			{ 61266, 61316 }, { 61267, 61317 }, { 61268, 61318 }, { 61269, 61319 }, { 61270, 61320 }, { 61271, 61321 },
			{ 61272, 61322 }, { 61273, 61323 }, { 61274, 61324 }, { 61275, 61325 }, { 61276, 61326 }, { 61277, 61327 },
			{ 61278, 61328 }, { 61279, 61329 },

	};
	private static int[] BUTTON_IDS = { -4286, -4285, -4284, -4283, -4282, -4281, -4280, -4279, -4278, -4277, -4276,
			-4275, -4274, -4273, -4272, -4271, -4270, -4269, -4268, -4267, -4266, -4265, -4264, -4263, -4262, -4261,
			-4260, -4259, -4258, -4255, -4255, -4255, -4254, -4253, -4252, -4251, -4250 };
	private static int[] TAB_IDS = { -4934, -4931, -4928, -4925, -4922 };

	
	public static void teleport(Player player, int button) {
		for (int i = 0; i < BUTTON_IDS.length; i++) {
			if (button == BUTTON_IDS[i]) {
				player.destination = i;
			}
		}
		
		if (player.lastClickedTab == 1)
			teleportTraining(player, player.destination);
		else if (player.lastClickedTab == 2)
			teleportDungeons(player, player.destination);
		else if (player.lastClickedTab == 3)
			teleportBosses(player, player.destination);
		else if (player.lastClickedTab == 4)
			teleportWilderness(player, player.destination);
		else if (player.lastClickedTab == 5)
			teleportMinigames(player, player.destination);
	}

	/**
	 * Training teleport method.
	 * 
	 * @param client
	 *            The player.teleporting to a training area.
	 * @param destination
	 *            The destination being teleported to.
	 */
	public static void teleportTraining(Player player, int destination) {
		for (final TeleportTraining.Training t : TeleportTraining.Training.values()) {
			if (destination == t.ordinal()) {
				if (t.getCoordinates()[2] == 10) {
					player.sendMessage("This teleport is currently unavailable.");
					return;
				}
				TeleportHandler.teleportPlayer(player, new Position(t.getCoordinates()[0], t.getCoordinates()[1]),
						player.getSpellbook().getTeleportType());
			}
		}
	}

	/**
	 * Minigame teleport method.
	 * 
	 * @param client
	 *            The player.teleporting to a minigame area.
	 * @param destination
	 *            The destination being teleported to.
	 */
	public static void teleportMinigames(Player player, int destination) {
		for (final TeleportMinigames.Minigames m : TeleportMinigames.Minigames.values()) {
			if (destination == m.ordinal()) {
				TeleportHandler.teleportPlayer(player, new Position(m.getCoordinates()[0], m.getCoordinates()[1]),
						player.getSpellbook().getTeleportType());
			}
		}
	}

	/**
	 * Bosses teleport method.
	 * 
	 * @param client
	 *            The player.teleporting to a bosses area.
	 * @param destination
	 *            The destination being teleported to.
	 */
	public static void teleportBosses(Player player, int destination) {
		for (final TeleportBosses.Bosses b : TeleportBosses.Bosses.values()) {
			if (destination == b.ordinal()) {
				if (b.getCoordinates()[2] == 10) {
					player.sendMessage("This teleport is currently unavailable.");
					return;
				}
				if (b.getCoordinates()[2] == 20) {
					TeleportHandler.teleportPlayer(player, new Position(3696, 5807, player.getIndex() * 4),
							player.getSpellbook().getTeleportType());
					return;
				}
				if (b.getCoordinates()[2] == 3) {
					TeleportHandler.teleportPlayer(player, new Position(2973, 9517, 1),
							player.getSpellbook().getTeleportType());

					return;
				}
				if (b.getCoordinates()[2] == 2) {
					TeleportHandler.teleportPlayer(player, new Position(2871, 5319, 2),
							player.getSpellbook().getTeleportType());

					return;
				}
				TeleportHandler.teleportPlayer(player, new Position(b.getCoordinates()[0], b.getCoordinates()[1]),
						player.getSpellbook().getTeleportType());
			
			}
		}
	}

	/**
	 * Player killing teleport method.
	 * 
	 * @param client
	 *            The player.teleporting to a player killing area.
	 * @param destination
	 *            The destination being teleported to.
	 */

	public static void teleportDungeons(Player player, int destination) {

		for (final TeleportDungeon.Dungeons p : TeleportDungeon.Dungeons.values()) {
			if (destination == p.ordinal()) {
				TeleportHandler.teleportPlayer(player, new Position(p.getCoordinates()[0], p.getCoordinates()[1]),
						player.getSpellbook().getTeleportType());
			}
		}
	}

	public static void teleportWilderness(Player player, int destination) {

		for (final TeleportWilderness.Wilderness p : TeleportWilderness.Wilderness.values()) {
			if (destination == p.ordinal()) {
		
				TeleportHandler.teleportPlayer(player, new Position(p.getCoordinates()[0], p.getCoordinates()[1]),
						player.getSpellbook().getTeleportType());
			}
		}
	}

	/**
	 * Skilling teleport method.
	 * 
	 * @param client
	 *            The player.teleporting to a skilling area.
	 * @param destination
	 *            The destination being teleported to.
	 */

	/**
	 * Donator teleport method.
	 * 
	 * @param client
	 *            The player.teleporting to a donator area.
	 * @param destination
	 *            The destination being teleported to.
	 */

	/**
	 * Opening a tab in the teleports interface.
	 * 
	 * @param client
	 *            player.opening the tab.
	 * @param button
	 *            Tab id being opened.
	 */
	public static void openTab(Player player, int button) {
		for (int i = 0; i < TAB_IDS.length; i++) {
			if (button == TAB_IDS[i]) {
				player.lastClickedTab = i + 1;
				player.getPacketSender().sendInterface(61200);
			}
		}
		for (int i = 61250; i < 61280; i++) {
			player.getPacketSender().sendString(i, "");
		}
		switch (player.lastClickedTab) {

		case 1:
			for (final TeleportTraining.Training t : TeleportTraining.Training.values()) {
				player.getPacketSender().sendTeleString(t.getTeleportName()[0], LINE_IDS[t.ordinal()][0]);
				
			}
			break;
		case 2:
			for (final TeleportDungeon.Dungeons d : TeleportDungeon.Dungeons.values()) {
				player.getPacketSender().sendTeleString(d.getTeleportName()[0], LINE_IDS[d.ordinal()][0]);
				
			}
			break;
		case 3:
			for (final TeleportBosses.Bosses b : TeleportBosses.Bosses.values()) {
				player.getPacketSender().sendTeleString(b.getTeleportName()[0], LINE_IDS[b.ordinal()][0]);
				
			}
			break;

		case 4:
			for (final TeleportWilderness.Wilderness s : TeleportWilderness.Wilderness.values()) {
				player.getPacketSender().sendTeleString(s.getTeleportName()[0], LINE_IDS[s.ordinal()][0]);
			
			


			}
			break;

		case 5:
			for (final TeleportMinigames.Minigames m : TeleportMinigames.Minigames.values()) {
				player.getPacketSender().sendTeleString(m.getTeleportName()[0], LINE_IDS[m.ordinal()][0]);
				
			}
			break;

		}
	}

}
