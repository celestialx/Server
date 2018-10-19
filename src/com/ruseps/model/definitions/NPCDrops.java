package com.ruseps.model.definitions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ruseps.model.GameMode;
import com.ruseps.model.Graphic;
import com.ruseps.model.GraphicHeight;
import com.ruseps.model.GroundItem;
import com.ruseps.model.Item;
import com.ruseps.model.PlayerRights;
import com.ruseps.model.Position;
import com.ruseps.model.Skill;
import com.ruseps.model.Locations.Location;
import com.ruseps.model.container.impl.Bank;
import com.ruseps.model.container.impl.Equipment;
import com.ruseps.util.JsonLoader;
import com.ruseps.util.Misc;
import com.ruseps.util.RandomUtility;
import com.ruseps.world.World;
import com.ruseps.world.content.ClueScrolls;
import com.ruseps.world.content.DropLog;
import com.ruseps.world.content.PlayerLogs;
import com.ruseps.world.content.DropLog.DropLogEntry;
import com.ruseps.world.content.clan.ClanChatManager;
import com.ruseps.world.content.minigames.impl.WarriorsGuild;
import com.ruseps.world.content.skill.impl.prayer.BonesData;
import com.ruseps.world.content.skill.impl.summoning.CharmingImp;
import com.ruseps.world.entity.impl.GroundItemManager;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

/**
 * Controls the npc drops
 * 
 * @author 2012 <http://www.rune-server.org/members/dexter+morgan/>, Gabbe &
 *         Samy
 * 
 */
public class NPCDrops {

	public static boolean shouldOrb = true;
	public static boolean canDrop = true;
	/**
	 * The map containing all the npc drops.
	 */
	private static Map<Integer, NPCDrops> dropControllers = new HashMap<Integer, NPCDrops>();

	public static JsonLoader parseDrops() {

		ItemDropAnnouncer.init();

		return new JsonLoader() {

			@Override
			public void load(JsonObject reader, Gson builder) {
				int[] npcIds = builder.fromJson(reader.get("npcIds"), int[].class);
				NpcDropItem[] drops = builder.fromJson(reader.get("drops"), NpcDropItem[].class);

				NPCDrops d = new NPCDrops();
				d.npcIds = npcIds;
				d.drops = drops;
				for (int id : npcIds) {
					dropControllers.put(id, d);
				}
			}

			@Override
			public String filePath() {
				return "./data/def/json/drops.json";
			}
		};
	}

	/**
	 * The id's of the NPC's that "owns" this class.
	 */
	private int[] npcIds;

	/**
	 * All the drops that belongs to this class.
	 */
	private NpcDropItem[] drops;

	/**
	 * Gets the NPC drop controller by an id.
	 * 
	 * @return The NPC drops associated with this id.
	 */
	public static NPCDrops forId(int id) {
		return dropControllers.get(id);
	}

	public static Map<Integer, NPCDrops> getDrops() {
		return dropControllers;
	}

	/**
	 * Gets the drop list
	 * 
	 * @return the list
	 */
	public NpcDropItem[] getDropList() {
		return drops;
	}

	/**
	 * Gets the npcIds
	 * 
	 * @return the npcIds
	 */
	public int[] getNpcIds() {
		return npcIds;
	}

	/**
	 * Represents a npc drop item
	 */
	public static class NpcDropItem {

		/**
		 * The id.
		 */
		private final int id;

		/**
		 * Array holding all the amounts of this item.
		 */
		private final int[] count;

		/**
		 * The chance of getting this item.
		 */
		private final int chance;

		/**
		 * New npc drop item
		 * 
		 * @param id
		 *            the item
		 * @param count
		 *            the count
		 * @param chance
		 *            the chance
		 */
		public NpcDropItem(int id, int[] count, int chance) {
			this.id = id;
			this.count = count;
			this.chance = chance;
		}

		/**
		 * Gets the item id.
		 * 
		 * @return The item id.
		 */
		public int getId() {
			return id;
		}

		/**
		 * Gets the chance.
		 * 
		 * @return The chance.
		 */
		public int[] getCount() {
			return count;
		}

		/**
		 * Gets the chance.
		 * 
		 * @return The chance.
		 */
		public DropChance getChance() {
			switch (chance) {
			case 1:
				return DropChance.COMMON; // <-> 1/7
			case 2:
				return DropChance.COMMON; // <-> 1/7
			case 3:
				return DropChance.UNCOMMON; // <-> 1/15
			case 4:
				return DropChance.UNCOMMON; // <-> 1/15
			case 5:
				return DropChance.RARE; // <-> 1/50
			case 6:
				return DropChance.LEGENDARY; // <-> 1/100
			/*case 7:
				return DropChance.LEGENDARY_1;// <-> 1/200*/
			case 8:
				return DropChance.LEGENDARY_2;// <-> 1/333
			case 9:
				return DropChance.LEGENDARY_3;// <-> 1/750
			case 10:
				return DropChance.LEGENDARY_4;// <-> 1/1000
			default:
				return DropChance.ALWAYS; // always
			}
		}

		/**
		 * Gets the item
		 * 
		 * @return the item
		 */
		public Item getItem() {
			int amount = 0;
			for (int i = 0; i < count.length; i++)
				amount += count[i];
			if (amount > count[0])
				amount = count[0] + RandomUtility.getRandom(count[1]);
			return new Item(id, amount);
		}
	}

	//public static enum DropChance {
		public enum DropChance {
			ALWAYS(0),
			COMMON(5),
			UNCOMMON(25),
			RARE(100),
			LEGENDARY(150),
			//LEGENDARY_1(200),
			LEGENDARY_2(250),
			LEGENDARY_3(500),
			LEGENDARY_4(1000);

			DropChance(int randomModifier) {
				this.random = randomModifier;
			}

			private int random;

			public int getRandom() {
				return this.random;
			}
		}

	/**
	 * Drops items for a player after killing an npc. A player can max receive
	 * one item per drop chance.
	 * 
	 * @param p
	 *            Player to receive drop.
	 * @param npc
	 *            NPC to receive drop FROM.
	 */
	public static void dropItems(Player p, NPC npc) {
		if (npc.getLocation() == Location.WARRIORS_GUILD)
			WarriorsGuild.handleDrop(p, npc);
		NPCDrops drops = NPCDrops.forId(npc.getId());
		if (drops == null)
			return;
		final boolean goGlobal = p.getPosition().getZ() >= 0 && p.getPosition().getZ() < 4;
		final boolean ringOfWealth = p.getEquipment().get(Equipment.RING_SLOT).getId() == 2572;
		final Position npcPos = npc.getPosition().copy();
		boolean[] dropsReceived = new boolean[12];

		if (drops.getDropList().length > 0 && p.getPosition().getZ() >= 0 && p.getPosition().getZ() < 4) {

			if (npc.getId() == 3847 || npc.getId() == 2042 || npc.getId() == 2043 || npc.getId() == 2044) {
				casketDrop(p, npc.getDefinition().getCombatLevel(), p.getPosition());
			} else {
				casketDrop(p, npc.getDefinition().getCombatLevel(), npcPos);
			}
		}
		if (drops.getDropList().length > 0 && p.getPosition().getZ() >= 0 && p.getPosition().getZ() < 4) {
			if (npc.getId() == 3847 || npc.getId() == 2042 || npc.getId() == 2043 || npc.getId() == 2044) {
				if (!ClueScrolls.hasClue)
				clueDrop(p, npc.getDefinition().getCombatLevel(), p.getPosition());
			} else {
				if (!ClueScrolls.hasClue)
				clueDrop(p, npc.getDefinition().getCombatLevel(), npcPos);
			}

		}

		if (p.getInventory().contains(6821)) {
			p.performGraphic(new Graphic(94, GraphicHeight.MIDDLE));
			p.getPacketSender().sendMessage("@or2@Your drop has been converted to coins!");

		}
		for (int i = 0; i < drops.getDropList().length; i++) {
			if (drops.getDropList()[i].getItem().getId() <= 0
					|| drops.getDropList()[i].getItem().getId() > ItemDefinition.getMaxAmountOfItems()
					|| drops.getDropList()[i].getItem().getAmount() <= 0) {
				continue;
			}

			DropChance dropChance = drops.getDropList()[i].getChance();

			if (dropChance == DropChance.ALWAYS) {
				drop(p, drops.getDropList()[i].getItem(), npc, npcPos, goGlobal);
			} else {
				if (shouldDrop(dropsReceived, dropChance, ringOfWealth,
						p.getGameMode() == GameMode.IRONMAN || p.getGameMode() == GameMode.HARDCORE_IRONMAN,
						p.getRights())) {
					drop(p, drops.getDropList()[i].getItem(), npc, npcPos, goGlobal);
					dropsReceived[dropChance.ordinal()] = true;
				}
			}
		}
	}

	@SuppressWarnings("unused")
	public static void orbDrop(Player player, Item item) {
		int itemId = item.getId();
		int amount = item.getAmount();
		if (shouldOrb) {
			if (player.getInventory().contains(6821)) {
				int value = item.getDefinition().getValue();
				int formula = value * amount;
				player.getInventory().add(995, formula);
				canDrop = false;
			}
		}

	}

	public static boolean shouldDrop(boolean[] b, DropChance chance, boolean ringOfWealth, boolean EXTREME_DONATOR,
			PlayerRights rights) {
		int random = chance.getRandom();
		/*
		 * switch(rights) { case PLATINUM_MEMBER: random -= (random / 10) *
		 * 6;//540 break; case LEGENDARY_DONATOR: random -= (random / 10) * 5;//450
		 * break; case ADMINISTRATOR: break; case DEVELOPER: break; case
		 * SUPER: random -= (random / 3) * 2;//600 break; case
		 * EXTREME_DONATOR: random -= (random / 8) * 5;//562.5 break; case
		 * MODERATOR: break; case OWNER: break; case PLAYER: break; case
		 * DONATOR: random -= (random / 10) * 8;//720 break; case SUPPORT:
		 * break; case YOUTUBER: break; default: break; }
		 */
		if (ringOfWealth && random >= 60) {
			random -= (random / 10);
		 System.out.println(random);

		}
		return !b[chance.ordinal()] && RandomUtility.getRandom(random) == 1;
	}

	public static void simulate(Player player, Item item, NPC npc, Position pos, boolean goGlobal) {

		for (int z = 0; z < 100; z++) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(item, player.getPosition(), player.getUsername(), false, 150, goGlobal, 200));
		}

	}

	public static void drop(Player player, Item item, NPC npc, Position pos, boolean goGlobal) {

		if (item == null || item.getDefinition() == null || item.getDefinition().getName().equals("null")
				|| (item.getAmount() > 15000 && !item.getDefinition().isStackable()
						&& !item.getDefinition().isNoted())) {
			return;
		}

		if (player.getInventory().contains(6821)) {
			orbDrop(player, item);
		}

		if (player.getInventory().contains(18337) && BonesData.forId(item.getId()) != null) {
			player.getPacketSender().sendGlobalGraphic(new Graphic(777), pos);
			player.getSkillManager().addExperience(Skill.PRAYER, BonesData.forId(item.getId()).getBuryingXP());
			return;

		}
		int itemId = item.getId();
		int amount = item.getAmount();

		if (itemId == 18778) {
			player.getPacketSender().sendMessage("@red@ You have received an starved ancient effigy.");
		}

		if (itemId == 6731 || itemId == 6914 || itemId == 7158 || itemId == 6889 || itemId == 6733 || itemId == 15019
				|| itemId == 11235 || itemId == 15020 || itemId == 15018 || itemId == 15220 || itemId == 6735
				|| itemId == 6737 || itemId == 6585 || itemId == 4151 || itemId == 4087 || itemId == 2577
				|| itemId == 2581 || itemId == 11732 || itemId == 18782) {
			player.getPacketSender().sendMessage("@red@ YOU HAVE RECIEVED A MEDIUM DROP, CHECK THE GROUND!");
			shouldOrb = false;
		}
		shouldOrb = true;
		if (itemId == CharmingImp.GOLD_CHARM || itemId == CharmingImp.GREEN_CHARM || itemId == CharmingImp.CRIM_CHARM
				|| itemId == CharmingImp.BLUE_CHARM) {
			shouldOrb = false;
			if (player.getInventory().contains(6500) && CharmingImp.handleCharmDrop(player, itemId, amount)) {
				return;
			}

		}
		shouldOrb = true;

		Player toGive = player;

		if (itemId == 995) {
			shouldOrb = false;
		}
		shouldOrb = true;
//		if (itemId == 995 && amount > 49000000) {
//			shouldOrb = false;
//			String message = "@blu@" + toGive.getUsername() + " has just received "
//					+ Misc.insertCommasToNumber(String.valueOf(amount)) + "x Coins!";
//			World.sendMessage(message);
//			player.performGraphic(new Graphic(1177, GraphicHeight.MIDDLE));
//
//		}
		shouldOrb = true;
		boolean ccAnnounce = false;
		if (Location.inMulti(player)) {
			if (player.getCurrentClanChat() != null && player.getCurrentClanChat().getLootShare()) {
				CopyOnWriteArrayList<Player> playerList = new CopyOnWriteArrayList<Player>();
				for (Player member : player.getCurrentClanChat().getMembers()) {
					if (member != null) {
						if (member.getPosition().isWithinDistance(player.getPosition())) {
							playerList.add(member);
						}
					}
				}
				if (playerList.size() > 0) {
					toGive = playerList.get(RandomUtility.getRandom(playerList.size() - 1));
					if (toGive == null || toGive.getCurrentClanChat() == null
							|| toGive.getCurrentClanChat() != player.getCurrentClanChat()) {
						toGive = player;
					}
					ccAnnounce = true;
				}
			}
		}
		
		 if(itemId == 18778) { //Effigy, don't drop one if player already has
		 shouldOrb = false; if(toGive.getInventory().contains(18778) ||
		 toGive.getInventory().contains(18779) ||
		 toGive.getInventory().contains(18780) ||
		 toGive.getInventory().contains(18781)) { return; }
		  
		 for(Bank bank : toGive.getBanks()) { if(bank == null) { continue; }
		 if(bank.contains(18778) || bank.contains(18779) ||
		 bank.contains(18780) || bank.contains(18781)) { return; } } }
		 
		shouldOrb = true;
		if (ItemDropAnnouncer.announce(itemId)) {
			String itemName = item.getDefinition().getName();
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			String npcName = Misc.formatText(npc.getDefinition().getName());

			shouldOrb = false;
/*
			if (player.getRights() == PlayerRights.DEVELOPER || player.getRights() == PlayerRights.RUBY_MEMBER
					|| player.getRights() == PlayerRights.LEGENDARY_DONATOR
					|| player.getRights() == PlayerRights.ADMINISTRATOR) {
				if (npc.getId() == 3847 || npc.getId() == 2042 || npc.getId() == 2043 || npc.getId() == 2044) {

					GroundItemManager.spawnGroundItem(player, new GroundItem(item, player.getPosition(),
							player.getUsername(), false, 150, goGlobal, 200));

				} else {
					GroundItemManager.spawnGroundItem(toGive,
							new GroundItem(item, pos, toGive.getUsername(), false, 150, goGlobal, 200));
				}

			}*/

			switch (itemId) {
			case 14484:
				itemMessage = "Dragon Claws";
				break;
			case 20000:
			case 20001:
			case 20002:
				itemMessage = itemName;
				break;
			}
			switch (npc.getId()) {
			case 81:
				npcName = "a Cow";
				break;
			case 50:
			case 3200:
			case 8133:
			case 4540:
			case 1160:
			case 8549:
				npcName = "The " + npcName + "";
				break;
			case 51:
			case 54:
			case 5363:
			case 8349:
			case 1592:
			case 1591:
			case 1590:
			case 1615:
			case 9463:
			case 9465:
			case 9467:
			case 1382:
			case 13659:
			case 11235:
				npcName = "" + Misc.anOrA(npcName) + " " + npcName + "";
				break;
			}
			String message = "<img=10>@blu@" + toGive.getUsername() + " has just received " + itemMessage
					+ " from " + npcName;
			World.sendMessage(message);
			player.performGraphic(new Graphic(1177, GraphicHeight.MIDDLE));

			if (ccAnnounce) {
				ClanChatManager.sendMessage(player.getCurrentClanChat(),
						"<col=16777215>[<col=255>Lootshare<col=16777215>]<col=3300CC> " + toGive.getUsername()
								+ " received " + itemMessage + " from " + npcName + "!");
			}

			PlayerLogs.log(toGive.getUsername(), "" + toGive.getUsername() + " received " + itemMessage + " from " + npcName + "");
		}

		if (canDrop) {

			/*
			 * kraken and zulrah dropping under player pos vs npc pos
			 */
			if (npc.getId() == 3847 || npc.getId() == 2042 || npc.getId() == 2043 || npc.getId() == 2044 || npc.getId() == 2041 || npc.getId() == 2402) {

				GroundItemManager.spawnGroundItem(player,
						new GroundItem(item, player.getPosition(), player.getUsername(), false, 150, goGlobal, 200));

			} else {
				GroundItemManager.spawnGroundItem(toGive,
						new GroundItem(item, pos, toGive.getUsername(), false, 150, goGlobal, 200));
			}

		}
		DropLog.submit(toGive, new DropLogEntry(itemId, item.getAmount()));
		orbDrop(player, item);
	}

	public static void casketDrop(Player player, int combat, Position pos) {
		int chance = (6 + (combat / 2));
		if (RandomUtility.getRandom(combat <= 50 ? 1300 : 1000) < chance) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(7956), pos, player.getUsername(), false, 150, true, 200));
		}
	}

	private static final int[] CLUESBOY = new int[] { 2677, 2678, 2679, 2680, 2681, 2682, 2683, 2684, 2685, 2686, 2687,
			2688, 2689, 2690 };

	public static void clueDrop(Player player, int combat, Position pos) {
		int chance = (6 + (combat / 4));
		if (RandomUtility.getRandom(combat <= 80 ? 1300 : 1000) < chance) {
			int clueId = CLUESBOY[Misc.getRandom(CLUESBOY.length - 1)];
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(clueId), pos, player.getUsername(), false, 150, true, 200));
			player.getPacketSender().sendMessage("@or2@You have recieved a clue scroll!");
			ClueScrolls.hasClue = true;
		}
	}

	public static class ItemDropAnnouncer {

		private static List<Integer> ITEM_LIST;

		private static final int[] TO_ANNOUNCE = new int[] { 14484, 4212, 11702, 11704, 11706, 11708, 11704, 12646,
				11724, 19780, 10548, 15241, 15243, 11730, 11726, 11728, 11718, 11720, 11722, 11730, 11967, 13247, 12655,
				11969, 11970, 13058, 11971, 11972, 11973, 11974, 11975, 11976, 11716, 14876, 11286, 13427, 2513, 15259,
				13902, 13890, 13884, 13861, 13858, 13864, 13905, 13887, 13893, 17927, 13235, 7986, 7981, 7980, 12708,
				13239, 13045, 13047, 13899, 13051, 2572, 11614, 12601, 4565, 1038, 1040, 1042, 1044, 1046, 1048, 962,
				4566, 113873, 13879, 13876, 13870, 6571, 14008, 14009, 14010, 14011, 20555, 11283, 13263, 11716, 6500,
				14012, 2572, 12926, 1037, 1053, 1055, 1057, 1050, 11862, 13051, 11860, 11858, 5607, 15501, 13058, 12601,
				12603, 12605, 14013, 14014, 14015, 14016, 13750, 13748, 17291, 13746, 15490, 15488, 13752, 13740, 18349,
				18351, 6570, 12284, 18353, 18355, 18357, 18359, 13263, 11283, 11694, 14484, 11335, 15486, 13870, 13873,
				13876, 13884, 13890, 13896, 14479, 11554, 4151, 4084, 4705, 4706, 2572, 11848, 11700, 11694, 11730,
				11698, 11696, 11850, 11852, 11854, 11856, 11846, 13902, 13858, 13861, 13864, 13867, 11995, 11996, 11997,
				11978, 12001, 12002, 13742, 12003, 12004, 19335, 12005, 12006, 11990, 11991, 11992, 11993, 11994, 13744,
				13738, 11989, 11988, 11987, 11986, 11985, 11984, 11983, 11982, 11981, 11979, 13659, 20000, 20001, 11613,
				11287, 20002, 16425, 11282, 12712, 12713, 12714, 12715, 12716, 12717, 12718, 12719, 12720, 12721, 12722,
				12723, 12724, 12725, 12726, 12727, 12728, 12729, 12730, 12731};

		private static void init() {
			ITEM_LIST = new ArrayList<Integer>();
			for (int i : TO_ANNOUNCE) {
				ITEM_LIST.add(i);
			}
		}

		public static boolean announce(int item) {
			return ITEM_LIST.contains(item);
		}
	}
}