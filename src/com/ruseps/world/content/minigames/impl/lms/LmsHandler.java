package com.ruseps.world.content.minigames.impl.lms;

import java.util.HashMap;
import java.util.Map;

import com.ruseps.model.Position;
import com.ruseps.model.Skill;
import com.ruseps.GameSettings;
import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Flag;
import com.ruseps.model.Item;
import com.ruseps.model.Locations.Location;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.content.dialogue.DialogueManager;
import com.ruseps.world.content.skill.SkillManager;
import com.ruseps.world.content.skill.SkillManager.Skills;
import com.ruseps.world.entity.impl.player.Player;

/**
 * 
 * @author Harryl
 *
 */
public class LmsHandler {

	/**
	 * Checks players in game / in lobby
	 */
	public static int PLAYERS_IN_GAME = 0;
	public static int PLAYERS_IN_LOBBY = 0;

	/**
	 * @note Stores player and State
	 */
	public static Map<Player, String> lobbyMap = new HashMap<Player, String>();
	public static Map<Player, String> gameMap = new HashMap<Player, String>();

	/**
	 * Handles the Game / lobby Time
	 */
	public static int gameTime = 1200;
	public static int lobbyTime = 120;

	/**
	 * Checks if game is running
	 */
	public static boolean lmsGameRunning = false;

	/**
	 * Handles the amount needed
	 */
	private static final int amountNeeded = 3000000;
	private static final int playersNeeded = 2;

	/**
	 * Handles the rewards for the winner
	 */
	private static Item[] rewards = { new Item(995, 10000000), new Item(1464, 10) };

	/**
	 * Handles random spawning in area
	 */
	private static int coords[][] = { { 1, 1, 1 } };

	/**
	 * Lobby Sequence
	 */
	public static void Lobbysequence() {
		if (lmsGameRunning == true) {
			checkEndGame();
			return;
		}

		if (lobbyTime > 0 && PLAYERS_IN_LOBBY >= 1) {
			lobbyTime--;
			if (lobbyTime == 60)
				World.sendMessage("<img=10> <col=660099>[LMS] </col>@red@" + lobbyTime
						+ " <col=660099>seconds until Lms starts, current lobby count:</col> @red@"
						+ PLAYERS_IN_LOBBY);
			updateInterface("lobby");
		}
		if (lobbyTime <= 0 && PLAYERS_IN_LOBBY < playersNeeded) {
			for (Player p : World.getPlayers()) {
				if (p != null && p.getLocation() == Location.LMS_LOBBY) {
					p.sendMessage("@or2@[LMS] The game needs a total of 10 players or more to start");
					lobbyTime += 120;
					return;
				}
			}
		}
		if (lobbyTime <= 0 && PLAYERS_IN_LOBBY >= playersNeeded) {

			if (lmsGameRunning == false)
				startGame();
			lobbyTime += 120;
		}
	}

	/**
	 * Lobby Sequence
	 */
	public static void Gamesequence() {
		if (lmsGameRunning == false) {
			return;
		}
		if (lmsGameRunning == true && gameTime > 0) {
			gameTime--;
			updateInterface("game");
		}
		if (lmsGameRunning == true && gameTime <= 0) {
			for (Player p : World.getPlayers()) {
				if (p != null && p.getLocation() == Location.LMS_GAME) {
					leaveGame(p, "game");
					gameTime = 1200;
				}
			}
		}
	}

	/**
	 * Handles the game (spawning, countdown etc..)
	 */
	public static void startGame() {
		for (Player p : World.getPlayers()) {
			if (p != null && p.getLocation() == Location.LMS_LOBBY) {
				PLAYERS_IN_LOBBY--;
				lobbyMap.remove(p, "WAITING");
				/*OldStats(p, "save");
				p.getSkillManager().newSkillManager();
				giveStats(p);
				updateSkills(p);*/
				p.moveTo(new Position(2755, 2784, 0));
				PLAYERS_IN_GAME++;
				gameMap.put(p, "PLAYING");
				lmsGameRunning = true;
				p.getPacketSender().sendInteractionOption("Attack", 2, true);
				LmsObjectHandler.spawnCrates();
				updateInterface("game");
			}
		}
	}

	/**
	 * Gives Stats
	 * @param player
	 */
	public static void giveStats(Player player) {
		player.getSkillManager().setMaxLevel(Skill.ATTACK, 99).setMaxLevel(Skill.STRENGTH, 99)
				.setMaxLevel(Skill.RANGED, 99).setMaxLevel(Skill.MAGIC, 99).setMaxLevel(Skill.DEFENCE, 99)
				.setMaxLevel(Skill.PRAYER, 990).setMaxLevel(Skill.CONSTITUTION, 990);
		for (Skill skill : Skill.values()) {
			player.getSkillManager().setCurrentLevel(skill, player.getSkillManager().getMaxLevel(skill)).setExperience(
					skill, SkillManager.getExperienceForLevel(player.getSkillManager().getMaxLevel(skill)));
		}
	}

	/**
	 * Saves / Loads the old stats
	 * 
	 * @param player
	 */
	public static void OldStats(Player player, String type) {
		switch (type) {

		case "save":
			Skills currentSkills = player.getSkillManager().getSkills();
			player.oldSkillLevels = currentSkills.level;
			player.oldSkillXP = currentSkills.experience;
			player.oldSkillMaxLevels = currentSkills.maxLevel;
			break;

		case "load":
			player.getSkillManager().getSkills().level = player.oldSkillLevels;
			player.getSkillManager().getSkills().experience = player.oldSkillXP;
			player.getSkillManager().getSkills().maxLevel = player.oldSkillMaxLevels;
			break;
		}
	}

	/**
	 * Updates the skill
	 * 
	 * @param player
	 */
	public static void updateSkills(Player player) {
		for (int i = 0; i < 24; i++) {
			player.getSkillManager().updateSkill(Skill.forId(i));
		}
	}

	/**
	 * Checks if the game needs to be ended.
	 */
	public static void checkEndGame() {
		for (Player p : gameMap.keySet()) {
			if (gameMap.size() == 1) {
				leaveGame(p, "winner");
			}
		}
	}

	/**
	 * 
	 * @param player
	 */
	public static void joinLobby(Player player) {
		if (lmsGameRunning) {
			player.getPA().removeAllWindows();
			player.sendMessage("@or2@ There is currently a game running, current time:" + gameTime);
			return;
		}
		player.getPA().removeAllWindows();
		player.setLmsCoffer(player.getLmsCoffer() - amountNeeded);
		player.moveTo(new Position(2464, 4782, 0));
		lobbyMap.put(player, "WAITING");
		PLAYERS_IN_LOBBY++;
		updateInterface("lobby");
	}

	/**
	 * Double checks if player wants to join lobby!
	 * 
	 * @param player
	 */
	public static void lobbyDialogueCheck(Player player) {
		if (lobbyCheck(player)) {
			DialogueManager.start(player, 152);
			player.setDialogueActionId(153);
		}
	}

	/**
	 * Checks if player can join lobby.
	 * 
	 * @param player
	 */
	public static boolean lobbyCheck(Player player) {

		if (player.getLmsCoffer() < amountNeeded) {
			player.sendMessage("@or2@[LMS] You need atleast 3m in your lms coffer to join the lobby!");
			return false;
		}

		if (player.getInventory().getFreeSlots() != 28) {
			player.sendMessage("@or2@[LMS] Please make sure to bank your inventory");
			return false;
		}
		for (int i = 0; i < 14; i++) {
			if (player.getEquipment().get(i).getId() > 0) {
				player.sendMessage("@or2@[LMS] Please make sure to bank your equipment");
				return false;
			}
		}
		return true;
	}

	/**
	 * Updates the lobby / game Interface
	 */
	@SuppressWarnings("unused")
	public static void updateInterface(String type) {
		switch (type) {
		case "lobby":
			int minutes, seconds;
				minutes = lobbyTime / 60;
				seconds = lobbyTime % 60;
			for (Player p : World.getPlayers()) {
				if (p != null && p.getLocation() == Location.LMS_LOBBY) {
					if (seconds > 9) {
					p.getPA().sendFrame126(22121, "Time: " + minutes + ":" + seconds);
					} else {
					p.getPA().sendFrame126(22121, "Time: " + minutes + ":0" + seconds);
					}
					p.getPA().sendFrame126(22120, "Players in lobby: " + lobbyMap.size());
				}
			}
			break;
		case "game":
			minutes = (gameTime) / 60;
			seconds = (gameTime) % 60;
			for (Player p : World.getPlayers()) {
				if (p != null && p.getLocation() == Location.LMS_GAME) {
					if (seconds > 9) {
						p.getPA().sendFrame126(22121, "Time: " + minutes + ":" + seconds);
						} else {
						p.getPA().sendFrame126(22121, "Time: " + minutes + ":0" + seconds);
					}
					p.getPA().sendFrame126(22120, "Players left: " + gameMap.size());
				}
			}
			break;
		}
	}

	/**
	 * 
	 * @param player
	 */
	public static void leaveGame(Player player, String type) {
		switch (type) {

		case "game":
			PLAYERS_IN_GAME--;
			gameMap.remove(player, "PLAYING");
			player.moveTo(GameSettings.DEFAULT_POSITION.copy());
			player.sendMessage("@or2@Thanks for participating in the lms game!");
			player.getPacketSender().sendInteractionOption("null", 2, true);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			player.getInventory().deleteAll();
			player.getEquipment().deleteAll();
			player.getEquipment().resetItems().refreshItems();
		/*	OldStats(player, "load");
			updateSkills(player);*/
			player.getCombatBuilder().reset(true);

			break;

		case "lobby":
			PLAYERS_IN_LOBBY--;
			lobbyMap.remove(player, "WAITING");
			player.setLmsCoffer(player.getLmsCoffer() + amountNeeded);
			player.moveTo(GameSettings.DEFAULT_POSITION.copy());
			player.getPacketSender().sendInteractionOption("null", 2, true);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			player.getInventory().deleteAll();
			player.getEquipment().deleteAll();
			player.getEquipment().resetItems().refreshItems();
		/*	OldStats(player, "load");
			updateSkills(player);*/
			player.getCombatBuilder().reset(true);

			break;

		case "winner":
			PLAYERS_IN_GAME = 0;
			PLAYERS_IN_LOBBY = 0;
			lobbyMap.clear();
			gameMap.clear();
			player.moveTo(GameSettings.DEFAULT_POSITION.copy());
			World.sendMessage("<img=10> <col=660099>[LMS]</col>@red@ " + Misc.formatPlayerName(player.getUsername())
					+ " <col=660099>has just won the lms game!</col>");
			lmsGameRunning = false;
			player.getPacketSender().sendInteractionOption("null", 2, true);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			player.getInventory().deleteAll();
			player.getEquipment().deleteAll();
			player.getInventory().add(rewards[0]);
			player.getEquipment().resetItems().refreshItems();
		/*	OldStats(player, "load");
			updateSkills(player);*/
			player.getCombatBuilder().reset(true);

			break;
		}
	}

	public static void gameMessage(Player killer, Player killed) {
		for (Player p : World.getPlayers()) {
			if (p != null && p.getLocation() == Location.LMS_GAME) {
				if(gameMap.size() < 2) {
					return;
				}
				p.sendMessage("<img=10> @or2@[LMS]</col>@red@ " + Misc.formatPlayerName(killed.getUsername())
					+ " <col=660099>has just been killed by " + Misc.formatPlayerName(killer.getUsername()) + "!");
			  }
		}
	}
}
