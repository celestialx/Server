package com.ruseps.world.content.minigames.impl.lms;

import com.ruseps.model.GameObject;
import com.ruseps.model.GroundItem;
import com.ruseps.model.Item;
import com.ruseps.model.Position;
import com.ruseps.util.Misc;
import com.ruseps.world.content.CustomObjects;
import com.ruseps.world.entity.impl.GroundItemManager;
import com.ruseps.world.entity.impl.player.Player;

/**
 * 
 * @author Harryl
 * link = ""
 */
public class LmsObjectHandler {

	/**
	 * Crate id
	 */
	private static final int CRATE_ID = 3360;
	
	/**
	 * Handles the crates spawn location
	 */
	public static GameObject crates[] = {
			new GameObject(CRATE_ID, new Position(2744, 2777, 0)),
			new GameObject(CRATE_ID, new Position(2764, 2802, 0)),
			new GameObject(CRATE_ID, new Position(2787, 2804, 0)),
			
			new GameObject(CRATE_ID, new Position(2805, 2761, 0)),
			new GameObject(CRATE_ID, new Position(2783, 2763, 0)),
			new GameObject(CRATE_ID, new Position(2784, 2746, 0)),
			
			new GameObject(CRATE_ID, new Position(2770, 2752, 0)),
			new GameObject(CRATE_ID, new Position(2704, 2769, 0)),
			new GameObject(CRATE_ID, new Position(2709, 2790, 0)),
			
			new GameObject(CRATE_ID, new Position(2724, 2733, 0)),
			new GameObject(CRATE_ID, new Position(2703, 2716, 0)),
			new GameObject(CRATE_ID, new Position(2735, 2706, 0)),
			
			new GameObject(CRATE_ID, new Position(2744, 2717, 0)),
			new GameObject(CRATE_ID, new Position(2796, 2711, 0)),
			new GameObject(CRATE_ID, new Position(2798, 2701, 0))
	};
	
	/**
	 * Spawns loot of crates
	 */
	private static Item[] SUPPLIESLOOT = {new Item(2434, 1), new Item(2440, 1), new Item(890, 15), new Item(892, 15), new Item(11212, 15)};
	private static Item[] FOODLOOT = {new Item(379, 3), new Item(373, 3), new Item(391, 3), new Item(385, 3)};
	private static Item[] RUNES = {new Item(556, 100), new Item(555, 100), new Item(557, 100), new Item(554, 100), new Item(562, 100),
			                       new Item(560, 100), new Item(565, 100)};
	private static int[] LOOT = {1333, 1319, 1373, 1127, 1079, 3753, 861, 4153, 6523, 4587, 7158, 1215, 4151,
			                     2513, 4087, 9672, 9674, 9676, 1161, 1123, 1073, 3105, 3753, 853, 857, 11235,
			                     3479, 6328, 2493, 2499, 2495, 2501, 2497, 2503, 6914, 3385, 4089, 4091, 4093,
			                     4097, 6916, 6924, 4675, 4712, 4714, 7458, 7460, 1704, 1478};
	                                  
	
	/**
	 * Handles object clicking
	 * @param player
	 * @param gameObject
	 */
	public static void handleObject(Player player, GameObject gameObject) {
		switch(gameObject.getId()) {
		
		// Zammy (TO) lobby teleport
		
			
		case 4788:
			if(player.getPosition() == new Position(2721, 2767, 0) || player.getPosition() == new Position(2720, 2767, 0)) {
				player.moveTo(new Position(2720, 2765, 0));
			} else if (player.getPosition() == new Position(2720, 2765, 0) || player.getPosition() == new Position(2721, 2765, 0)) {
				player.moveTo(new Position(2721, 2767, 0));
			}
			break;
		}
	}
	
	/**
	 * Handles the spawning of crates
	 */
	public static void spawnCrates() {
		for(int i = 0; i < crates.length; i++) {
		CustomObjects.spawnGlobalObject(crates[i]);
		}
	}
	
	/**
	 * Handles the loot ofc rates
	 * @param p
	 * @param gameobject
	 * @return
	 */
	public static boolean handleCrateSearch(Player p, GameObject gameobject) {
			if(gameobject.getId() ==  3360) {
				int random = Misc.random(100);
				Item FLOOT = FOODLOOT[Misc.getRandom(FOODLOOT.length - 1)];
				Item RUNESLOOT = RUNES[Misc.getRandom(RUNES.length - 1)];
				Item SLOOT = SUPPLIESLOOT[Misc.getRandom(SUPPLIESLOOT.length - 1)];
				
				CustomObjects.deleteGlobalObject(new GameObject(CRATE_ID, gameobject.getPosition()));
				GroundItemManager.spawnGroundItem(p, new GroundItem(new Item(FLOOT.getId(), FLOOT.getAmount()), p.getPosition(), p.getUsername(), false, 150, true, 200));
				GroundItemManager.spawnGroundItem(p, new GroundItem(new Item(LOOT[Misc.getRandom(LOOT.length - 1)], 1), p.getPosition(), p.getUsername(), false, 150, true, 200));
				GroundItemManager.spawnGroundItem(p, new GroundItem(new Item(LOOT[Misc.getRandom(LOOT.length - 1)], 1), p.getPosition(), p.getUsername(), false, 150, true, 200));
				
				if(random > 20) {
					GroundItemManager.spawnGroundItem(p, new GroundItem(new Item(SLOOT.getId(), SLOOT.getAmount()), p.getPosition(), p.getUsername(), false, 150, true, 200));
					GroundItemManager.spawnGroundItem(p, new GroundItem(new Item(RUNESLOOT.getId(), RUNESLOOT.getAmount()), p.getPosition(), p.getUsername(), false, 150, true, 200));
				}
				return true;
			}
			return false;
		}
}
