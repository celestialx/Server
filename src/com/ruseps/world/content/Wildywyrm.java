package com.ruseps.world.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.GroundItem;
import com.ruseps.model.Item;
import com.ruseps.model.Position;
import com.ruseps.model.definitions.NPCDrops;
import com.ruseps.util.Misc;
import com.ruseps.world.content.combat.CombatBuilder.CombatDamageCache;
import com.ruseps.world.World;
import com.ruseps.world.content.combat.CombatFactory;
import com.ruseps.world.entity.impl.GroundItemManager;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

public class Wildywyrm extends NPC {

	public static int[] COMMONLOOT = { 15273, 13883, 1726, 248, 1620, 9244, 868, 12154, 1516, 1272, 2358, 1457, 1459,
			1514, 2358, 537, 13883, 13879, 220, 1290, 2504, 4132, 1334, 1705, 4100, 4094, 4114, 15271, 6686 };
	public static int[] RARELOOT = { 20000, 20001, 20002, 15220, 15018, 15020, 15019, 6585, 4151, 11283,
			// Pvp armour
			13887, 13893, 13899, 13905, 13884, 13890, 13896, 13902, 13858, 13861, 13864, 13867, 13870, 13873, 13876,
			// Barrows
			11846, 11848, 11850, 11852, 11854, 11856 };
	public static int[] SUPERRARELOOT = { 20998, 20312, 20313, 20314, 20315, 20316, 20318, 20300, 20302, 11970 };

	/**
	 * 
	 */
	public static final int NPC_ID = 3334;

	/**
	 * add your maps to that folder open me your client.java in client
	 */
	public static final WildywyrmLocation[] LOCATIONS = { new WildywyrmLocation(3303, 3931, 0, "Rogue's Castle"),
			new WildywyrmLocation(3237, 3750, 0, "Bone Yard"), new WildywyrmLocation(3157, 3887, 0, "Spider Hill"),
			new WildywyrmLocation(3193, 3677, 0, "Graveyard") };

	/**
	 * 
	 */
	private static Wildywyrm current;

	/**
	 * 
	 * @param position
	 */
	public Wildywyrm(Position position) {

		super(NPC_ID, position);
	}

	/**
	 * 
	 */
	public static void initialize() {

		TaskManager.submit(new Task(6000, false) { // 6000

			@Override
			public void execute() {
				spawn();
			}

		});

	}

	/**
	 * 
	 */
	public static void spawn() {

		if (getCurrent() != null) {
			
			return;
		}

		WildywyrmLocation location = Misc.randomElement(LOCATIONS);
		Wildywyrm instance = new Wildywyrm(location.copy());

		// System.out.println(instance.getPosition());

		World.register(instance);
		setCurrent(instance);
		// System.out.print("spawned.");

		World.sendMessage("<img=10>@red@The WildyWyrm has spawned at " + location.getLocation() + "!");
		World.getPlayers().forEach(
				p -> p.getPacketSender().sendString(26707, "@or2@WildyWyrm: @gre@" + location.getLocation() + ""));

	}

	/**
	 * 
	 * @param npc
	 */
	public static void handleDrop(NPC npc) {
		World.getPlayers().forEach(p -> p.getPacketSender().sendString(26707, "@or2@WildyWyrm: @gre@N/A"));

		setCurrent(null);

		if (npc.getCombatBuilder().getDamageMap().size() == 0) {
			return;
		}

		Map<Player, Integer> killers = new HashMap<>();

		for (Entry<Player, CombatDamageCache> entry : npc.getCombatBuilder().getDamageMap().entrySet()) {

			if (entry == null) {
				continue;
			}

			long timeout = entry.getValue().getStopwatch().elapsed();

			if (timeout > CombatFactory.DAMAGE_CACHE_TIMEOUT) {
				continue;
			}

			Player player = entry.getKey();

			if (player.getConstitution() <= 0 || !player.isRegistered()) {
				continue;
			}

			killers.put(player, entry.getValue().getDamage());

		}

		npc.getCombatBuilder().getDamageMap().clear();

		List<Entry<Player, Integer>> result = sortEntries(killers);
		int count = 0;

		for (Entry<Player, Integer> entry : result) {

			Player killer = entry.getKey();
			int damage = entry.getValue();

			handleDrop(npc, killer, damage);

			if (++count >= 5) {
				break;
			}

		}

	}

	public static void giveLoot(Player player, NPC npc, Position pos) {
		int chance = Misc.getRandom(100);
		int common = COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)];
		int common1 = COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)];
		int rare = RARELOOT[Misc.getRandom(RARELOOT.length - 1)];
		int superrare = SUPERRARELOOT[Misc.getRandom(SUPERRARELOOT.length - 1)];

		GroundItemManager.spawnGroundItem(player,
				new GroundItem(new Item(995, 1000000), pos, player.getUsername(), false, 150, true, 200));

		if (chance >= 99) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(superrare), pos, player.getUsername(), false, 150, true, 200));
			String itemName = (new Item(superrare).getDefinition().getName());
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			World.sendMessage(
					"<img=10><col=FF0000>" + player.getUsername() + " received " + itemMessage + " from the Wildywyrm!");
			return;
		}

		if (chance >= 93) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(rare), pos, player.getUsername(), false, 150, true, 200));
			String itemName = (new Item(rare).getDefinition().getName());
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			World.sendMessage(
					"<img=10><col=FF0000>" + player.getUsername() + " received " + itemMessage + " from the Wildywyrm!");
			return;
		}
		if (chance >= 0) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(common, 25), pos, player.getUsername(), false, 150, true, 200));
			String itemName = (new Item(common).getDefinition().getName());
			World.sendMessage(
					"<img=10><col=FF0000>" + player.getUsername() + " received x25 " + itemName + " from the Wildywyrm!");
			return;
		}

	}

	/**
	 * 
	 * @param npc
	 * @param player
	 * @param damage
	 */
	private static void handleDrop(NPC npc, Player player, int damage) {
		Position pos = npc.getPosition();
		giveLoot(player, npc, pos);
	}

	/**
	 * 
	 * @param map
	 * @return
	 */
	static <K, V extends Comparable<? super V>> List<Entry<K, V>> sortEntries(Map<K, V> map) {

		List<Entry<K, V>> sortedEntries = new ArrayList<Entry<K, V>>(map.entrySet());

		Collections.sort(sortedEntries, new Comparator<Entry<K, V>>() {

			@Override
			public int compare(Entry<K, V> e1, Entry<K, V> e2) {
				return e2.getValue().compareTo(e1.getValue());
			}

		});

		return sortedEntries;

	}

	/**
	 * 
	 * @return
	 */
	public static Wildywyrm getCurrent() {
		return current;
	}

	/**
	 * 
	 * @param current
	 */
	public static void setCurrent(Wildywyrm current) {
		Wildywyrm.current = current;
	}

	/**
	 * 
	 * @author Levi <levi.patton69 @ skype>
	 *
	 */
	public static class WildywyrmLocation extends Position {

		/**
		 * 
		 */
		private String location;

		/**
		 * 
		 * @param x
		 * @param y
		 * @param z
		 * @param location
		 */
		public WildywyrmLocation(int x, int y, int z, String location) {
			super(x, y, z);
			setLocation(location);
		}

		/**
		 * 
		 * @return
		 */

		String getLocation() {
			return location;
		}

		/**
		 * 
		 * @param location
		 */
		public void setLocation(String location) {
			this.location = location;
		}

	}

}
