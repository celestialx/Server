package com.ruseps.world.content;

import com.ruseps.GameSettings;
import com.ruseps.engine.task.impl.PoisonImmunityTask;
import com.ruseps.model.Flag;
import com.ruseps.model.Item;
import com.ruseps.model.MagicSpellbook;
import com.ruseps.model.Position;
import com.ruseps.model.Skill;
import com.ruseps.model.container.impl.Equipment;
import com.ruseps.model.definitions.WeaponAnimations;
import com.ruseps.model.definitions.WeaponInterfaces;
import com.ruseps.net.packet.impl.EquipPacketListener;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.content.BonusManager;
import com.ruseps.world.content.dialogue.DialogueManager;
import com.ruseps.world.content.skill.SkillManager;
import com.ruseps.world.content.skill.SkillManager.Skills;
import com.ruseps.world.entity.impl.player.Player;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class FreeForAll {
	public static int TOTAL_PLAYERS = 0;
	private static int PLAYERS_IN_LOBBY = 0;

	/**
	 * @note Stores player and State
	 */
	public static Map<Player, String> playerMap = new HashMap<Player, String>();
	public static Map<Player, String> playersInGame = new HashMap<Player, String>();
	public static final String PLAYING = "PLAYING";
	public static final String WAITING = "WAITING";
	public String type = "";
	public int[][] pureInv = new int[][] { { Equipment.HEAD_SLOT, 1153 }, { Equipment.CAPE_SLOT, 10499 },
			{ Equipment.AMULET_SLOT, 1725 }, { Equipment.WEAPON_SLOT, 4587 }, { Equipment.BODY_SLOT, 1129 },
			{ Equipment.SHIELD_SLOT, 1540 }, { Equipment.LEG_SLOT, 2497 }, { Equipment.HANDS_SLOT, 7459 },
			{ Equipment.FEET_SLOT, 3105 }, { Equipment.RING_SLOT, 2550 }, { Equipment.AMMUNITION_SLOT, 9244 } };
	public static boolean maxstr = false;
	public static boolean f2p = false;
	public static boolean pure = false;
	public static boolean pure2 = false;
	public static boolean brid = false;
	public static boolean dharok = false;
	private static boolean gameRunning = false;
	private static boolean eventRunning = false;
	private static int waitTimer = 121;
	public static int[][] coordinates = { { 2265, 4684, 4 }, { 2261, 4699, 4 }, { 2282, 4706, 4 }, { 2282, 4689, 4 } };

	public static String getState(Player player) {
		return playerMap.get(player);
	}

	public static void saveOldStats(Player player) {
		Skills currentSkills = player.getSkillManager().getSkills();
		player.oldSkillLevels = currentSkills.level;
		player.oldSkillXP = currentSkills.experience;
		player.oldSkillMaxLevels = currentSkills.maxLevel;
	}

	public static void startGame() {
		for (Player player : playerMap.keySet()) {
			eventRunning = false;
			gameRunning = true;
			player.getPA().closeAllWindows();
			saveOldStats(player);
			player.getSkillManager().newSkillManager();
			updateSkills(player);
			if (brid) {
				player.getSkillManager().setMaxLevel(Skill.ATTACK, 99).setMaxLevel(Skill.STRENGTH, 99)
						.setMaxLevel(Skill.RANGED, 99).setMaxLevel(Skill.MAGIC, 99).setMaxLevel(Skill.DEFENCE, 99)
						.setMaxLevel(Skill.PRAYER, 990).setMaxLevel(Skill.CONSTITUTION, 990);
				for (Skill skill : Skill.values()) {
					player.getSkillManager().setCurrentLevel(skill, player.getSkillManager().getMaxLevel(skill))
							.setExperience(skill,
									SkillManager.getExperienceForLevel(player.getSkillManager().getMaxLevel(skill)));
				}
				player.getInventory().add(4151, 1);
				player.getInventory().add(4722, 1);
				player.getInventory().add(19112,1);
				player.getInventory().add(14484, 1);
				player.getInventory().add(10551, 1);
				player.getInventory().add(11732, 1);
				player.getInventory().add(13262, 1);
				player.getInventory().add(6685, 1);
				player.getInventory().add(2436, 1);
				player.getInventory().add(2440, 1);
				player.getInventory().add(3024, 2);
				player.getInventory().add(385, 13);
				player.getInventory().add(560, 20000);
				player.getInventory().add(565, 20000);
				player.getInventory().add(555, 20000);
				player.getEquipment().set(Equipment.HEAD_SLOT, new Item(10828, 1));
				player.getEquipment().set(Equipment.CAPE_SLOT, new Item(10636, 1));
				player.getEquipment().set(Equipment.AMULET_SLOT, new Item(6585, 1));
				player.getEquipment().set(Equipment.WEAPON_SLOT, new Item(15486, 1));
				player.getEquipment().set(Equipment.BODY_SLOT, new Item(4712, 1));
				player.getEquipment().set(Equipment.LEG_SLOT, new Item(4714, 1));
				player.getEquipment().set(Equipment.SHIELD_SLOT, new Item(6889, 1));
				player.getEquipment().set(Equipment.HANDS_SLOT, new Item(7462, 1));
				player.getEquipment().set(Equipment.FEET_SLOT, new Item(6920, 1));
				player.getEquipment().set(Equipment.RING_SLOT, new Item(2550, 1));
				player.setSpellbook(MagicSpellbook.ANCIENT);
				player.getPacketSender().sendTabInterface(GameSettings.MAGIC_TAB,
						player.getSpellbook().getInterfaceId());
				player.getEquipment().refreshItems();
				player.getUpdateFlag().flag(Flag.APPEARANCE);
				EquipPacketListener.resetWeapon(player);
				BonusManager.update(player);
			} else if (maxstr) {
				player.getSkillManager().stopSkilling();
				player.getInventory().resetItems().refreshItems();
				player.getEquipment().resetItems().refreshItems();

				player.getSkillManager().setMaxLevel(Skill.ATTACK, 99).setMaxLevel(Skill.STRENGTH, 99)
						.setMaxLevel(Skill.RANGED, 99).setMaxLevel(Skill.MAGIC, 99).setMaxLevel(Skill.DEFENCE, 99)
						.setMaxLevel(Skill.PRAYER, 990).setMaxLevel(Skill.CONSTITUTION, 990);
				for (Skill skill : Skill.values()) {
					player.getSkillManager().setCurrentLevel(skill, player.getSkillManager().getMaxLevel(skill))
							.setExperience(skill,
									SkillManager.getExperienceForLevel(player.getSkillManager().getMaxLevel(skill)));
				}

				player.getInventory().add(2436, 1);
				player.getInventory().add(2440, 1);
				player.getInventory().add(6685, 2);
				player.getInventory().add(3144, 2);
				player.getInventory().add(3024, 2);
				player.getInventory().add(3144, 2);
				player.getInventory().add(391, 10);
				player.getInventory().add(11694, 1);
				player.getInventory().add(4153, 1);
				player.getInventory().add(391, 2);
				player.getInventory().add(8013, 10);
				player.getInventory().add(560, 20000);
				player.getInventory().add(9075, 20000);
				player.getInventory().add(557, 20000);
				player.getEquipment().set(Equipment.HEAD_SLOT, new Item(12282, 1));
				player.getEquipment().set(Equipment.CAPE_SLOT, new Item(19112, 1));
				player.getEquipment().set(Equipment.AMULET_SLOT, new Item(6585, 1));
				player.getEquipment().set(Equipment.WEAPON_SLOT, new Item(20061, 1));
				player.getEquipment().set(Equipment.BODY_SLOT, new Item(11724, 1));
				player.getEquipment().set(Equipment.LEG_SLOT, new Item(11726, 1));
				player.getEquipment().set(Equipment.SHIELD_SLOT, new Item(11283, 1));
				player.getEquipment().set(Equipment.HANDS_SLOT, new Item(7462, 1));
				player.getEquipment().set(Equipment.FEET_SLOT, new Item(13239, 1));
				player.getEquipment().set(Equipment.RING_SLOT, new Item(15220, 1));
				player.setSpellbook(MagicSpellbook.LUNAR);
				player.getPacketSender().sendTabInterface(GameSettings.MAGIC_TAB,
						player.getSpellbook().getInterfaceId());
				player.getEquipment().refreshItems();
				player.getUpdateFlag().flag(Flag.APPEARANCE);
				EquipPacketListener.resetWeapon(player);
				BonusManager.update(player);
			} else if (pure) {
				player.getSkillManager().stopSkilling();
				player.getInventory().resetItems().refreshItems();
				player.getEquipment().resetItems().refreshItems();
				player.getSkillManager().setMaxLevel(Skill.ATTACK, 60).setMaxLevel(Skill.STRENGTH, 99)
						.setMaxLevel(Skill.RANGED, 99).setMaxLevel(Skill.MAGIC, 94).setMaxLevel(Skill.PRAYER, 520)
						.setMaxLevel(Skill.DEFENCE, 1).setMaxLevel(Skill.CONSTITUTION, 990);
				for (Skill skill : Skill.values()) {
					player.getSkillManager().setCurrentLevel(skill, player.getSkillManager().getMaxLevel(skill))
							.setExperience(skill,
									SkillManager.getExperienceForLevel(player.getSkillManager().getMaxLevel(skill)));
				}
				player.getInventory().add(2436, 1);
				player.getInventory().add(2440, 1);
				player.getInventory().add(2444, 1);
				player.getInventory().add(6685, 1);
				player.getInventory().add(3144, 2);
				player.getInventory().add(3024, 2);
				player.getInventory().add(3144, 3);
				player.getInventory().add(8013, 1);
				player.getInventory().add(391, 8);

				player.getInventory().add(861, 1);
				player.getInventory().add(5698, 1);
				player.getInventory().add(391, 2);

				player.getInventory().add(10499, 1);
				player.getInventory().add(391, 3);

				player.getEquipment().set(Equipment.HEAD_SLOT, new Item(6109, 1));
				player.getEquipment().set(Equipment.AMMUNITION_SLOT, new Item(892, 1000));
				player.getEquipment().set(Equipment.CAPE_SLOT, new Item(6570, 1));
				player.getEquipment().set(Equipment.AMULET_SLOT, new Item(6585, 1));
				player.getEquipment().set(Equipment.WEAPON_SLOT, new Item(4587, 1));
				player.getEquipment().set(Equipment.BODY_SLOT, new Item(6107, 1));
				player.getEquipment().set(Equipment.LEG_SLOT, new Item(2497, 1));
				player.getEquipment().set(Equipment.HANDS_SLOT, new Item(7458, 1));
				player.getEquipment().set(Equipment.FEET_SLOT, new Item(3105, 1));
				player.getEquipment().set(Equipment.RING_SLOT, new Item(15220, 1));
				player.getEquipment().set(Equipment.SHIELD_SLOT, new Item(3842, 1));
				player.getEquipment().refreshItems();
				player.getUpdateFlag().flag(Flag.APPEARANCE);
				EquipPacketListener.resetWeapon(player);
				BonusManager.update(player);
			} else if (pure2) {
				player.getSkillManager().stopSkilling();
				player.getInventory().resetItems().refreshItems();
				player.getEquipment().resetItems().refreshItems();
				player.getSkillManager().setMaxLevel(Skill.ATTACK, 75).setMaxLevel(Skill.STRENGTH, 99)
						.setMaxLevel(Skill.RANGED, 99).setMaxLevel(Skill.MAGIC, 94).setMaxLevel(Skill.PRAYER, 520)
						.setMaxLevel(Skill.DEFENCE, 1).setMaxLevel(Skill.CONSTITUTION, 990);
				for (Skill skill : Skill.values()) {
					player.getSkillManager().setCurrentLevel(skill, player.getSkillManager().getMaxLevel(skill))
							.setExperience(skill,
									SkillManager.getExperienceForLevel(player.getSkillManager().getMaxLevel(skill)));
				}
				player.getInventory().add(2436, 1);
				player.getInventory().add(2440, 1);
				player.getInventory().add(2444, 1);
				player.getInventory().add(6685, 1);
				player.getInventory().add(3144, 2);
				player.getInventory().add(3024, 2);
				player.getInventory().add(3144, 3);
				player.getInventory().add(8013, 1);
				player.getInventory().add(391, 8);

				player.getInventory().add(11694, 1);
				player.getInventory().add(4153, 1);
				player.getInventory().add(391, 2);

				player.getInventory().add(6570, 1);
				player.getInventory().add(391, 3);

				player.getEquipment().set(Equipment.HEAD_SLOT, new Item(6109, 1));
				player.getEquipment().set(Equipment.AMMUNITION_SLOT, new Item(892, 1000));
				player.getEquipment().set(Equipment.CAPE_SLOT, new Item(10499, 1));
				player.getEquipment().set(Equipment.AMULET_SLOT, new Item(6585, 1));
				player.getEquipment().set(Equipment.WEAPON_SLOT, new Item(861, 1));
				player.getEquipment().set(Equipment.BODY_SLOT, new Item(6107, 1));
				player.getEquipment().set(Equipment.LEG_SLOT, new Item(2497, 1));
				player.getEquipment().set(Equipment.HANDS_SLOT, new Item(7458, 1));
				player.getEquipment().set(Equipment.FEET_SLOT, new Item(3105, 1));
				player.getEquipment().set(Equipment.RING_SLOT, new Item(15220, 1));
				player.getEquipment().refreshItems();
				player.setSpellbook(MagicSpellbook.ANCIENT);
				player.getUpdateFlag().flag(Flag.APPEARANCE);
				EquipPacketListener.resetWeapon(player);
				BonusManager.update(player);
			}

			else if (dharok) {
				player.getSkillManager().setMaxLevel(Skill.ATTACK, 99).setMaxLevel(Skill.STRENGTH, 99)
						.setMaxLevel(Skill.RANGED, 99).setMaxLevel(Skill.MAGIC, 99).setMaxLevel(Skill.DEFENCE, 99)
						.setMaxLevel(Skill.PRAYER, 990).setMaxLevel(Skill.CONSTITUTION, 990);
				for (Skill skill : Skill.values()) {
					player.getSkillManager().setCurrentLevel(skill, player.getSkillManager().getMaxLevel(skill))
							.setExperience(skill,
									SkillManager.getExperienceForLevel(player.getSkillManager().getMaxLevel(skill)));
				}
				player.getInventory().add(14484, 1);
				player.getInventory().add(2436, 1);
				player.getInventory().add(2440, 1);
				player.getInventory().add(6685, 1);
				player.getInventory().add(4718, 1);
				player.getInventory().add(3024, 2);
				player.getInventory().add(6685, 1);
				player.getInventory().add(385, 17);
				player.getInventory().add(560, 20000);
				player.getInventory().add(9075, 20000);
				player.getInventory().add(557, 20000);
				player.getEquipment().set(Equipment.HEAD_SLOT, new Item(4716, 1));
				player.getEquipment().set(Equipment.CAPE_SLOT, new Item(19112, 1));
				player.getEquipment().set(Equipment.AMULET_SLOT, new Item(6585, 1));
				player.getEquipment().set(Equipment.WEAPON_SLOT, new Item(4151, 1));
				player.getEquipment().set(Equipment.BODY_SLOT, new Item(4720, 1));
				player.getEquipment().set(Equipment.LEG_SLOT, new Item(4722, 1));
				player.getEquipment().set(Equipment.SHIELD_SLOT, new Item(13262, 1));
				player.getEquipment().set(Equipment.HANDS_SLOT, new Item(7462, 1));
				player.getEquipment().set(Equipment.FEET_SLOT, new Item(11732, 1));
				player.getEquipment().set(Equipment.RING_SLOT, new Item(6737, 1));
				player.setSpellbook(MagicSpellbook.LUNAR);
				player.getPacketSender().sendTabInterface(GameSettings.MAGIC_TAB,
						player.getSpellbook().getInterfaceId());
				player.getEquipment().refreshItems();
				player.getUpdateFlag().flag(Flag.APPEARANCE);
				EquipPacketListener.resetWeapon(player);
				BonusManager.update(player);
			}
			movePlayerToArena(player);
			player.inFFALobby = false;
			player.inFFA = true;
			player.getPacketSender().sendInteractionOption("Attack", 2, true);
			waitTimer = 120;
		}
	}

	public static void removePlayer(Player c) {
		playerMap.remove(c);
	}

	public static boolean checkEndGame() {
		if (gameRunning) {

			if (playerMap.size() <= 1) {
				return true;
			}
		}
		return false;
	}

	public static void sequence() {
		if (gameRunning) {

			if (checkEndGame()) {
				endGame();
				return;
			}
			return;
		}

		if (!eventRunning)
			return;

		if (waitTimer > 0) {
			waitTimer--;
			if (waitTimer % 60 == 0 && waitTimer > 0)
				World.sendMessage("@or2@[FFA] " + waitTimer + " seconds until FFA starts!" + " Join now @ ::ffa");
			if (waitTimer % 120 == 0 && waitTimer > 0)
				World.sendMessage("@or2@[FFA] " + waitTimer + " seconds until FFA starts!" + " Join now @ ::ffa");
		}
		updateGameInterface();
		if (waitTimer <= 0) {
			if (!gameRunning)
				startGame();
		}
	}

	private static void updateGameInterface() {
		for (Player p : playerMap.keySet()) {
			if (p == null)
				continue;

			String state = getState(p);
			if (state != null && state.equals(WAITING)) {
				p.getPacketSender().sendString(21006, "Time till start: " + waitTimer + "");
				p.getPacketSender().sendString(21007, "Players Ready: " + PLAYERS_IN_LOBBY + "");
				p.getPacketSender().sendString(21009, "");
			}
		}
	}

	public static boolean checkItems(Player c) {
		if (c.getInventory().getFreeSlots() != 28) {
			return false;
		}
		for (int i = 0; i < 14; i++) {
			if (c.getEquipment().get(i).getId() > 0)
				return false;
		}
		return true;
	}

	public static void startEvent(String type) {
		maxstr = false;
		brid = false;
		pure = false;
		pure2 = false;
		dharok = false;
		if (!eventRunning) {
			World.sendMessage("<shad>A " + type + " FFA event has been started! Type ::ffa to join!");
			if (type == "max strength") {
				maxstr = true;
			} else if (type == "brid") {
				brid = true;
			} else if (type == "60 attack pure") {
				pure = true;
			} else if (type == "75 attack pure") {
				pure2 = true;
			} else if (type == "dharok") {
				dharok = true;
			} else {
				dharok = true;
			}
			eventRunning = true;
		}
	}

	private static void movePlayerToArena(Player p) {
		int ok = Misc.getRandom(1);
		if (ok == 1) {
			p.moveTo(new Position(3312 + Misc.getRandom(3), 9843 + Misc.getRandom(3)));
		} else {
			p.moveTo(new Position(3312 - Misc.getRandom(3), 9843 - Misc.getRandom(3)));
		}
		PLAYERS_IN_LOBBY--;

	}

	public static void enterLobby(Player c) {
		if (!eventRunning) {
			c.sendMessage("There is no game available right now!");
			return;
		}
		if (getState(c) == null) {
			if (checkItems(c)) {
				c.getPA().closeAllWindows();
				playerMap.put(c, WAITING);
				TOTAL_PLAYERS++;
				PLAYERS_IN_LOBBY++;
				c.inFFALobby = true;
				c.moveTo(new Position(2223, 3799, 0));
				c.sendMessage("Welcome to FFA!");
			} else {
				c.sendMessage("Bank all your items to play FFA!");
			}
		}
	}

	public static void updateSkills(Player player) {
		player.getSkillManager().updateSkill(Skill.ATTACK);
		player.getSkillManager().updateSkill(Skill.AGILITY);
		player.getSkillManager().updateSkill(Skill.CONSTITUTION);
		player.getSkillManager().updateSkill(Skill.CONSTRUCTION);
		player.getSkillManager().updateSkill(Skill.COOKING);
		player.getSkillManager().updateSkill(Skill.CRAFTING);
		player.getSkillManager().updateSkill(Skill.DEFENCE);
		player.getSkillManager().updateSkill(Skill.DUNGEONEERING);
		player.getSkillManager().updateSkill(Skill.FARMING);
		player.getSkillManager().updateSkill(Skill.FIREMAKING);
		player.getSkillManager().updateSkill(Skill.FISHING);
		player.getSkillManager().updateSkill(Skill.FLETCHING);
		player.getSkillManager().updateSkill(Skill.HERBLORE);
		player.getSkillManager().updateSkill(Skill.HUNTER);
		player.getSkillManager().updateSkill(Skill.MAGIC);
		player.getSkillManager().updateSkill(Skill.MINING);
		player.getSkillManager().updateSkill(Skill.PRAYER);
		player.getSkillManager().updateSkill(Skill.RANGED);
		player.getSkillManager().updateSkill(Skill.RUNECRAFTING);
		player.getSkillManager().updateSkill(Skill.SLAYER);
		player.getSkillManager().updateSkill(Skill.SMITHING);
		player.getSkillManager().updateSkill(Skill.STRENGTH);
		player.getSkillManager().updateSkill(Skill.SUMMONING);
		player.getSkillManager().updateSkill(Skill.THIEVING);
		player.getSkillManager().updateSkill(Skill.WOODCUTTING);
	}

	public static void endGame() {
		eventRunning = false;
		gameRunning = false;
		for (Player p : playerMap.keySet()) {
			World.sendMessage(
					"@or2@[FFA] @blu@" + Misc.formatPlayerName(p.getUsername()) + "@or2@ has won the FFA game!");
			p.sendMessage("You have won the game! PM a admin or owner for your reward!"); // kk i gtg eat brb enjoytho
			leaveGame(p);
		}
		playerMap.clear();
	}

	public static void leaveGame(Player c) {
		c.getInventory().deleteAll();
		c.getEquipment().deleteAll();
		WeaponAnimations.update(c);
		c.getUpdateFlag().flag(Flag.APPEARANCE);

		c.getEquipment().resetItems().refreshItems();
		c.getInventory().resetItems().refreshItems();

		c.getInventory().deleteAll();
		c.getEquipment().deleteAll();
		playerMap.remove(c);
		c.getSkillManager().getSkills().level = c.oldSkillLevels;
		c.getSkillManager().getSkills().experience = c.oldSkillXP;
		c.getSkillManager().getSkills().maxLevel = c.oldSkillMaxLevels;
		updateSkills(c);
		c.moveTo(new Position(3096, 3504, 0));
		c.inFFA = false;
		c.getPacketSender().sendInteractionOption("null", 2, true);
		c.sendMessage("Thank you for participating in FFA!");

		BonusManager.update(c);
		WeaponInterfaces.assign(c, c.getEquipment().get(Equipment.WEAPON_SLOT));
		WeaponAnimations.update(c);
		c.getEquipment().refreshItems();
		c.getUpdateFlag().flag(Flag.APPEARANCE);
		c.getInventory().resetItems();
		c.setVenomDamage(0);
		PoisonImmunityTask.makeImmune(c, 346);
	}
}
