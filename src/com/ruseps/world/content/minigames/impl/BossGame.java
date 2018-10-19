package com.ruseps.world.content.minigames.impl;

import java.util.HashMap;
import java.util.Map;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.engine.task.impl.PoisonImmunityTask;
import com.ruseps.model.Flag;
import com.ruseps.model.GameObject;
import com.ruseps.model.Item;
import com.ruseps.model.Locations.Location;
import com.ruseps.model.Position;
import com.ruseps.model.RegionInstance;
import com.ruseps.model.RegionInstance.RegionInstanceType;
import com.ruseps.model.container.impl.Equipment;
import com.ruseps.model.container.impl.Shop;
import com.ruseps.model.definitions.WeaponAnimations;
import com.ruseps.model.definitions.WeaponInterfaces;
import com.ruseps.model.input.impl.EnterAmountToBuyFromShop;
import com.ruseps.model.input.impl.EnterAmountToSellToShop;
import com.ruseps.world.World;
import com.ruseps.world.content.BonusManager;
import com.ruseps.world.content.CustomObjects;
import com.ruseps.world.content.PlayerPanel;
import com.ruseps.world.content.combat.prayer.CurseHandler;
import com.ruseps.world.content.combat.prayer.PrayerHandler;
import com.ruseps.world.content.dialogue.DialogueManager;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

/**
 * @author Adam trinity skype: live:nrpker7839
 * Wrote this quickly!!
 * Handles the Boss Minigame. its sloppy ik, but il clean it up.
 */
public class BossGame {
	
	/**
	 * Object id of closed chest
	 */
	private static final int CLOSED_CHEST = 170;
	
	/**
	 * Object id of open chest
	 */
	private static final int OPEN_CHEST = 171;
	
	/**
	 * X positions where all chests will 
	 * spawn when minigame begins
	 */
	private static final int[] CHEST_X = {3102, 3103, 3104, 3105, 3106, 3107, 3108, 3117, 3117, 3117, 3117, 3117, 3117, 3117, 3117,
										  3102, 3103, 3104, 3105, 3106, 3107, 3108, 3093, 3093, 3093, 3093, 3093, 3093, 3093, 3093};
	
	/**
	 * Y positions where all chests will 
	 * spawn when minigame begins
	 */
	private static final int[] CHEST_Y = {3921, 3921, 3921, 3921, 3921, 3921, 3921, 3930, 3931, 3932, 3933, 3934, 3935, 3936, 3937,
										  3946, 3946, 3946, 3946, 3946, 3946, 3946, 3930, 3931, 3932, 3933, 3934, 3935, 3936, 3937};
	
	/**
	 * Positions in which chests will be faced
	 * after being spawned in minigame beginning
	 */
	private static final int[] CHEST_FACE = {};
	
	
	
	public static Map<Player, String> playerMap = new HashMap<Player, String>();
	public static Map<Player, String> playersInGame = new HashMap<Player, String>();
	//private static int i;


	public static void enter(Player player) {
		if(player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() == 6)
			return;
		player.moveTo(new Position(1900, 5346, player.getIndex() * 4 + 2));
		player.setRegionInstance(new RegionInstance(player, RegionInstanceType.BOSS_GAME));
		spawnWave(player, player.getMinigameAttributes().getBossgameAttributes().getWavesCompleted());
		CurseHandler.deactivateAll(player); PrayerHandler.deactivateAll(player);
	}
	
	public static void leave(Player player) {
		Location.BOSS_GAME.leave(player);
		player.moveTo(new Position(1900, 5346));//change these cords
	}
	

	public static void spawnObjects(final Object b, final int wave) {
		int timer = 600;
		for(int i = 0; i < CHEST_X.length; i++) {
			CustomObjects.spawnGlobalObject(new GameObject(CLOSED_CHEST, new Position(CHEST_X[i], CHEST_Y[i], 1)));
		}
		if(timer >= 600 || timer <=0 ) {
			//execute this code.
			//spawn all the chests.
			 
		} 
		
	}
	

	public static void spawnWave(final Player p, final int wave) {
		if(wave > 5 || p.getRegionInstance() == null)
			return;
		TaskManager.submit(new Task(2, p, false) {
			@Override
			public void execute() {
				if(p.getRegionInstance() == null) {
					stop();
					for(int i = 0; i < CHEST_X.length; i++) {
						CustomObjects.spawnGlobalObject(new GameObject(CLOSED_CHEST, new Position(CHEST_X[i], CHEST_Y[i], 1)));
					}
					return;
				}
				if(p.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() == 6) {
					leaveGame(p);
				}
				int npc = wave >= 5 ? 3491 : 3493 + wave;
				NPC n = new NPC(npc, new Position(spawnPos.getX(), spawnPos.getY(), p.getPosition().getZ())).setSpawnedFor(p);
				World.register(n);
				p.getRegionInstance().getNpcsList().add(n);	
				n.getCombatBuilder().attack(p);
				stop();
			}
		});
	}

	public static void handleNPCDeath(final Player player, NPC n) {
		if(player.getRegionInstance() == null)
			return;
		player.getRegionInstance().getNpcsList().remove(n);
		player.getMinigameAttributes().getRecipeForDisasterAttributes().setWavesCompleted(player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() + 1);
		switch(n.getId()) {
		case 3493:
		case 3494:
		case 3495:
		case 3496:
		case 3497:
			int index = n.getId() - 3490;
			player.getMinigameAttributes().getRecipeForDisasterAttributes().setPartFinished(index, true);
			break;
		case 3491:
			player.getMinigameAttributes().getRecipeForDisasterAttributes().setPartFinished(8, true);
			player.moveTo(new Position(3081, 3500, 0));
			player.restart();
			DialogueManager.start(player, 46);
			PlayerPanel.refreshPanel(player);
			break;
		}
		if(player.getLocation() != Location.BOSS_GAME || player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() == 6)
			return;
		TaskManager.submit(new Task(3, player, false) {
			@Override
			public void execute() {
				stop();
				if(player.getLocation() != Location.BOSS_GAME || player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() == 6)
					return;
				spawnWave(player, player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted());
			}
		});
	}

	

		

		public static String getState(Player player) {
			return playerMap.get(player);
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
			c.moveTo(new Position(3096, 3504, 0));
			//inBG = false;
			c.sendMessage("Thank you for participating in Boss Game!");
			BonusManager.update(c);
			WeaponInterfaces.assign(c, c.getEquipment().get(Equipment.WEAPON_SLOT));
			WeaponAnimations.update(c);
			c.getEquipment().refreshItems();
			c.getUpdateFlag().flag(Flag.APPEARANCE);
			c.getInventory().resetItems();
			c.setVenomDamage(0);
			PoisonImmunityTask.makeImmune(c, 346);
		}

		/*
	public static void openBGShop(final Player player) {
		int[] stock = new int[10];
		int[] stockAmount = new int[10];
		for(int i = 0; i < stock.length; i++) {
			stock[i] = -1;
			stockAmount[i] = 2000000000;
		}
		for(int i = 0; i <= player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted(); i++) {
			switch(i) {
			case 1:
				stock[0] = 7453;
				break;
			case 2:
				stock[1] = 7454;
				stock[2] = 7455;
				break;
			case 3:
				stock[3] = 7456;
				stock[4] = 7457;
				break;
			case 4:
				stock[5] = 7458;
				break;
			case 5:
				stock[6] = 7459;
				stock[7] = 7460;
				break;
			case 6:
				stock[8] = 7461;
				stock[9] = 7462;
				break;
			}
		}
		Item[] stockItems = new Item[stock.length];
		for(int i = 0; i < stock.length; i++)
			stockItems[i] = new Item(stock[i], stockAmount[i]);
		Shop shop = new Shop(player, Shop.RECIPE_FOR_DISASTER_STORE, "Culinaromancer's chest", new Item(995), stockItems);
		stock = stockAmount = null;
		stockItems = null;
		shop.setPlayer(player);
		player.getPacketSender().sendItemContainer(player.getInventory(), Shop.INVENTORY_INTERFACE_ID);
		player.getPacketSender().sendItemContainer(shop, Shop.ITEM_CHILD_ID);
		player.getPacketSender().sendString(Shop.NAME_INTERFACE_CHILD_ID, "Culinaromancer's chest");
		if(player.getInputHandling() == null || !(player.getInputHandling() instanceof EnterAmountToSellToShop || player.getInputHandling() instanceof EnterAmountToBuyFromShop))
			player.getPacketSender().sendInterfaceSet(Shop.INTERFACE_ID, Shop.INVENTORY_INTERFACE_ID - 1);
		player.setShop(shop).setInterfaceId(Shop.INTERFACE_ID).setShopping(true);
	}
	*/


	private static final Position spawnPos = new Position(1900, 5354);
//	private static final String questTitle = "Boss Game";
	//private static final String[] questIntro ={
	//	"The Culinaromancer has returned and only you", 
	//	"             can stop him!                  ",
	//	"",
	//};
	/*private static final String[] questGuide ={
		"Talk to the Gypsy in Edgeville and agree to help her.",
		"Enter the portal.",
		"Defeat the following servants:",
		"* Agrith-Na-Na",
		"* Flambeed",
		"* Karamel",
		"* Dessourt",
		"* Gelatinnoth mother",
		"And finally.. Defeat the Culinaromancer!"*/
	private static final String[] questGuide ={
	"E",
	"Defeat the following servants:",
	"* Agrith-Na-Na",
	"* Flambeed",
	"* Karamel",
	"* Dessourt",
	"* Gelatinnoth mother",
	"And finally.. Defeat the Culinaromancer!"
	};
}







/*
public static int TOTAL_PLAYERS = 0;
private static int PLAYERS_IN_LOBBY = 0;
private static boolean gameRunning = false;
private static boolean eventRunning = false;
private static int waitTimer = 121;
private static int wave1 = 1;
private static int wave2 = 2;
private static int wave3 = 3;
private static int wave4 = 4;
private static int wave5 = 5;






public static Map<Player, String> playerMap = new HashMap<Player, String>();
public static Map<Player, String> playersInGame = new HashMap<Player, String>();
public static final String PLAYING = "PLAYING";
public static final String WAITING = "WAITING";


public static String getState(Player player) {
	return playerMap.get(player);
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
			World.sendMessage("@or2@[BG] " + waitTimer + " seconds until BG starts!" + " Join now @ ::bg");
		if (waitTimer % 120 == 0 && waitTimer > 0)
			World.sendMessage("@or2@[BG] " + waitTimer + " seconds until BG starts!" + " Join now @ ::bg");
	}
	//updateGameInterface();
	if (waitTimer <= 0) {
		if (!gameRunning)
			startGame();
	}
}

//cades a fat ugly ass slut
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
			c.inBGLobby = true;
			c.moveTo(new Position(2223, 3799, 0)); //lobby area.
			c.sendMessage("Welcome to BG!");
		} else {
			c.sendMessage("Bank all your items to play BG!");
		}
	}
}


private final static void spawn(Player player, int wave, int level) {
	if(level== 10) {
		leaveGame(player);
		player.getPacketSender().sendMessage("You successfully cleared out the graveyard!");
		return;
	}
	TaskManager.submit(new Task(4, player, false) {
		@Override
		public void execute() {
			if(player.getRegionInstance() == null || !player.isRegistered() || player.getLocation() != Location.GRAVEYARD) {
				stop();
				return;
			}
			final int NPCAmount = (wave * 2);
			player.getMinigameAttributes().getGraveyardAttributes().setRequiredKills(zombieAmount);
			for(int i = 0; i <= NPCAmount; i++) {
				NPC n = new NPC(getSpawn(level), getSpawnPos(player.getPosition().getZ())).setSpawnedFor(player);
				World.register(n);
				player.getRegionInstance().getNpcsList().add(n);
				n.getCombatBuilder().attack(player);
			}
			stop();
		}
	});
}
private static void startGame() {
	
	if(PLAYERS_IN_LOBBY > 2) {
		 gameRunning = true;
		eventRunning = true;
		
		
		
	}
	
}

public static void endGame() {
	eventRunning = false;
	gameRunning = false;
	for (Player p : playerMap.keySet()) {
		World.sendMessage(
				"@or2@[BG] @blu@" + Misc.formatPlayerName(p.getUsername()) + "@or2@ has won the BG game!");
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
	c.moveTo(new Position(3096, 3504, 0));
	//inBG = false;
	c.sendMessage("Thank you for participating in Boss Game!");
	BonusManager.update(c);
	WeaponInterfaces.assign(c, c.getEquipment().get(Equipment.WEAPON_SLOT));
	WeaponAnimations.update(c);
	c.getEquipment().refreshItems();
	c.getUpdateFlag().flag(Flag.APPEARANCE);
	c.getInventory().resetItems();
	c.setVenomDamage(0);
	PoisonImmunityTask.makeImmune(c, 346);
}
*/