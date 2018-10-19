package com.ruseps.net.packet.impl;

import mysql.MySQLController;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import mysql.impl.donations.AutoDonations;
import mysql.impl.voting.Voting;
import com.ruseps.util.Misc;
import com.ruseps.util.NameUtils;
import com.ruseps.util.RandomUtility;
import com.ruseps.util.TreasureIslandLootDumper;
import com.ruseps.world.World;
import com.ruseps.world.content.Achievements;
import com.ruseps.world.content.Achievements.AchievementData;
import com.ruseps.world.content.LoyaltyProgramme.LoyaltyTitles;
import com.ruseps.world.content.EvilTrees;
import com.ruseps.world.content.FreeForAll;
import com.ruseps.world.content.Gambling;
import com.ruseps.world.content.TrioBosses;
import com.ruseps.net.security.ConnectionHandler;
import com.ruseps.GameServer;
import com.ruseps.GameSettings;
import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.engine.task.impl.PlayerDeathTask;
import com.ruseps.model.Animation;
import com.ruseps.model.Flag;
import com.ruseps.model.GameMode;
import com.ruseps.model.GameObject;
import com.ruseps.model.Graphic;
import com.ruseps.model.GraphicHeight;
import com.ruseps.model.GroundItem;
import com.ruseps.model.Item;
import com.ruseps.model.Locations;
import com.ruseps.model.Locations.Location;
import com.ruseps.model.MagicSpellbook;
import com.ruseps.model.RegionInstance.RegionInstanceType;
import com.ruseps.model.PlayerRights;
import com.ruseps.model.Position;
import com.ruseps.model.Prayerbook;
import com.ruseps.model.Projectile;
import com.ruseps.model.RegionInstance;
import com.ruseps.model.Skill;
import com.ruseps.model.container.impl.Bank;
import com.ruseps.model.container.impl.Equipment;
import com.ruseps.model.container.impl.Inventory;
import com.ruseps.model.container.impl.Shop.ShopManager;
import com.ruseps.model.definitions.ItemDefinition;
import com.ruseps.model.definitions.NPCDrops;
import com.ruseps.model.definitions.NpcDefinition;
import com.ruseps.model.definitions.WeaponAnimations;
import com.ruseps.model.definitions.WeaponInterfaces;
import com.ruseps.model.input.impl.ConfirmEmpty;
import com.ruseps.model.input.impl.SetTitle;
import com.ruseps.net.packet.Packet;
import com.ruseps.net.packet.PacketListener;
import com.ruseps.net.security.ConnectionHandler;
import com.ruseps.world.content.TriviaBot;
import com.ruseps.world.content.BonusManager;
import com.ruseps.world.content.CustomObjects;
import com.ruseps.world.content.ItemBonuses;
import com.ruseps.world.content.WellOfGoodwill;
import com.ruseps.world.content.Wildywyrm;
import com.ruseps.world.content.Lottery;
import com.ruseps.world.content.LoyaltyProgramme;
import com.ruseps.world.content.PlayerDropLog;
import com.ruseps.world.content.skill.impl.construction.Construction;
import com.ruseps.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.ruseps.world.content.skill.impl.herblore.Decanting;
import com.ruseps.world.content.skill.impl.herblore.decanting.PotionDecanting;
import com.ruseps.world.content.teleportation.teleportsystem.BossInformation;
import com.ruseps.world.content.PlayerLogs;
import com.ruseps.world.content.PlayerPanel;
import com.ruseps.world.content.PlayerPunishment;
import com.ruseps.world.content.PlayerPunishment.Jail;
import com.ruseps.world.content.PlayersOnlineInterface;
import com.ruseps.world.content.ShootingStar.CrashedStar;
import com.ruseps.world.content.Sounds.Sound;
import com.ruseps.world.content.StaffList;
import com.ruseps.world.content.TreasureChest;
import com.ruseps.world.content.ProfileViewing;
import com.ruseps.world.content.ShootingStar;
import com.ruseps.world.content.Sounds;
import com.ruseps.world.content.clan.ClanChatManager;
import com.ruseps.world.content.combat.CombatFactory;
import com.ruseps.world.content.combat.DesolaceFormulas;
import com.ruseps.world.content.combat.magic.Autocasting;
import com.ruseps.world.content.combat.prayer.CurseHandler;
import com.ruseps.world.content.combat.prayer.PrayerHandler;
import com.ruseps.world.content.combat.pvp.BountyHunter;
import com.ruseps.world.content.combat.strategy.CombatStrategies;
import com.ruseps.world.content.combat.strategy.zulrah.Zulrah;
import com.ruseps.world.content.combat.weapon.CombatSpecial;
import com.ruseps.world.content.dialogue.DialogueManager;
import com.ruseps.world.content.dropchecker.NPCDropTableChecker;
import com.ruseps.world.content.droppreview.KBD;
import com.ruseps.world.content.droppreview.SLASHBASH;
import com.ruseps.world.content.grandexchange.GrandExchange;
import com.ruseps.world.content.grandexchange.GrandExchangeOffers;
import com.ruseps.world.content.minigames.impl.lms.LmsHandler;
import com.ruseps.world.content.skill.SkillManager;
import com.ruseps.world.content.transportation.TeleportHandler;
import com.ruseps.world.content.transportation.TeleportType;
import com.ruseps.world.entity.impl.GroundItemManager;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;
import com.ruseps.world.entity.impl.player.PlayerHandler;
import com.ruseps.world.entity.impl.player.PlayerLoading;
import com.ruseps.world.entity.impl.player.PlayerSaving;


/**
 * This packet listener manages commands a player uses by using the command
 * console prompted by using the "`" char.
 *
 * @author Gabriel Hannason
 */

public class CommandPacketListener implements PacketListener {
	
	public static int config;

	@Override
	public void handleMessage(Player player, Packet packet) {
		long start = System.currentTimeMillis();
		String command = Misc.readString(packet.getBuffer());
		if(player.isHidePlayer()) {
			return;
		}
		String[] parts = command.toLowerCase().split(" ");
		if (command.contains("\r") || command.contains("\n")) {
			return;
		}
		PlayerLogs.log(player.getUsername(), "" + player.getUsername() + " used command ::" + command + " | Player rights = " + player.getRights() + "");

		try {
			switch (player.getRights()) {
			case PLAYER:
				playerCommands(player, parts, command);
				break;
			case MODERATOR:
				playerCommands(player, parts, command);
				memberCommands(player, parts, command);
				helperCommands(player, parts, command);
				moderatorCommands(player, parts, command);
				break;
			case ADMINISTRATOR:
				playerCommands(player, parts, command);
				memberCommands(player, parts, command);
				helperCommands(player, parts, command);
				moderatorCommands(player, parts, command);
				administratorCommands(player, parts, command);
				break;
			case OWNER:
				playerCommands(player, parts, command);
				memberCommands(player, parts, command);
				helperCommands(player, parts, command);
				moderatorCommands(player, parts, command);
				administratorCommands(player, parts, command);
				ownerCommands(player, parts, command);
				developerCommands(player, parts, command);
				break;
			case DEVELOPER:
				playerCommands(player, parts, command);
				memberCommands(player, parts, command);
				helperCommands(player, parts, command);
				moderatorCommands(player, parts, command);
				administratorCommands(player, parts, command);
				ownerCommands(player, parts, command);
				developerCommands(player, parts, command);
				break;
			case MANAGER:
				playerCommands(player, parts, command);
				memberCommands(player, parts, command);
				helperCommands(player, parts, command);
				moderatorCommands(player, parts, command);
				administratorCommands(player, parts, command);
				ownerCommands(player, parts, command);
				developerCommands(player, parts, command);
				break;
			case SUPPORT:
				playerCommands(player, parts, command);
				memberCommands(player, parts, command);
				helperCommands(player, parts, command);
				break;
			case VETERAN:
				playerCommands(player, parts, command);
				memberCommands(player, parts, command);
				break;
				/*
			case DONATOR:
			case SUPER_DONATOR:
			case EXTREME_DONATOR:
			case EPIC_DONATOR:
			case LEGENDARY_DONATOR:*/
			case DONATOR:
			case SUPER_DONATOR:
			case EXTREME_DONATOR:
			case EPIC_DONATOR:
			case LEGENDARY_DONATOR:
			case RUBY_MEMBER:
				playerCommands(player, parts, command);
				memberCommands(player, parts, command);
				break;
			default:
				break;
			}
		} catch (Exception exception) {
			// exception.printStackTrace();

			if (player.getRights() == PlayerRights.DEVELOPER) {
				player.getPacketSender().sendConsoleMessage("Error executing that command.");
			} else {
				player.getPacketSender().sendMessage("Error executing that command.");
			}

		}
		long end = System.currentTimeMillis();
		long cycle = end - start;
		if(cycle >= 500) {
			System.err.println(cycle+"ms - command packet- "+command+" - "+player.getRights().name());
		}
	}

	private static void playerCommands(final Player player, String[] command, String wholeCommand) {
		if(command[0].equalsIgnoreCase("achtest")) {
			if(!player.loading) {
				player.loading = !player.loading;
				player.getAchievements().tab(1,true);
			}
		}
		if (command[0].equalsIgnoreCase("lms")) {
			LmsHandler.joinLobby(player);
		}

		if (command[0].equals("mutant")) {
			DialogueManager.start(player, 249);
			TeleportHandler.teleportPlayer(player, new Position(2980, 3763), player.getSpellbook().getTeleportType());
            player.sendMessage("The cemetery is under attack, defeat the mutant, go north to slay him.");
            player.sendMessage("do ::thread to view his drop table!");



		}
	
		if (command[0].equalsIgnoreCase("well")) {
			int time = WellOfGoodwill.getMinutesRemaining();
			if(time <=0) {
				player.getPA().sendMessage("The well is not currently active!");
			} else {
				player.getPA().sendMessage("There are currently: "+time+" minutes remaining.");

			}
		}
		
		/*if (command[0].equalsIgnoreCase("ffaleave")) {
			if(player.getLocation() != Location.FFALOBBY || player.getLocation() != Location.FFALOBBY) {
				player.getPA().sendMessage("You can only use this in the ffa arenas");
				return;
			}
		
			if(player.getLocation() == Location.DUNGEONEERING) {
				player.getPA().sendMessage("You can't do that here");
				return;
			}
			if(player.getLocation() == Location.IN_JAIL) {
				player.getPA().sendMessage("You can't do that here");
				return;	
			}
			if(player.getLocation() == Location.DUEL_ARENA) {
				player.getPA().sendMessage("You can't do that here");
				return;	
			}
			if(Dungeoneering.doingDungeoneering(player)) {
				player.getPA().sendMessage("You can't do that here");
				return;
			}
			player.getPA().sendInterfaceRemoval();
			 TaskManager.submit(new Task(1, player, false) {
                 int tick = 0;

                 @Override
                 public void execute() {
                     if (tick == 0) {

                       } else if (tick >= 3) {
                    	//	FreeForAll.leaveArena(player);                          
                    		this.stop();
                     }
                     tick++;
                 }
             });
		}*/
		
		if (command[0].equalsIgnoreCase("ffa")) {
			if(player.getLocation() == Location.DUNGEONEERING) {
				player.getPA().sendMessage("You can't do that here");
				return;
			}
			if(Dungeoneering.doingDungeoneering(player)) {
				player.getPA().sendMessage("You can't do that here");
				return;
			}
			/*if(FreeForAll.lobbyOpened == true) {
				FreeForAll.initiateLobby(player);
				} else {
					player.getPacketSender().sendMessage("No active ffa event");
				}*/
		}
		
		if(command[0].equalsIgnoreCase("players")) {
			player.getPacketSender().sendInterfaceRemoval();
			PlayersOnlineInterface.showInterface(player);
		}
		if(command[0].equalsIgnoreCase("kraken")) {
			player.getPacketSender().sendMessage("Teleporting you to kraken.");
			player.getKraken().enter(player, true);
		}
	
		if(command[0].equalsIgnoreCase("grabregion")) {
	        int regionX = player.getPosition().getX() >> 3;
	        int regionY = player.getPosition().getY() >> 3;
	        int regionId = ((regionX / 8) << 8) + (regionY / 8);
	        player.getPacketSender().sendMessage("Region id: "+regionId);
		}
		
		if (command[0].equals("staff")) {
			StaffList.showStaff(player);
		}
		
		if (command[0].equalsIgnoreCase("mole")) {
			TeleportHandler.teleportPlayer(player, new Position(1761, 5186), player.getSpellbook().getTeleportType());

		}
		
		if (command[0].equalsIgnoreCase("dzone")) {
			if(player.getAmountDonated() < 20) {
				player.getPacketSender().sendMessage("You have not donated enough for this!");
				return;
			} else {
			TeleportHandler.teleportPlayer(player, new Position(2337, 9799), player.getSpellbook().getTeleportType());
			}
		}
		
		
		if (command[0].equalsIgnoreCase("hops69")) {
			Gambling.plantSeed2(player);
			//3flowers
		}
		
		if (command[0].equalsIgnoreCase("hops70")) {
			Gambling.plantSeed77(player);
			//4flowers
		}
		if (command[0].equalsIgnoreCase("hops71")) {
			Gambling.plantSeed69(player);
			//4flowers
		}
		
		if (command[0].equals("tree")) {
		player.getPacketSender().sendMessage("<img=4> <shad=1><col=FF9933> The Evil Tree has sprouted at: "+EvilTrees.SPAWNED_TREE.getTreeLocation().playerPanelFrame+"");
	}
		
		if (command[0].equals("star")) {
			player.getPacketSender().sendMessage("<img=4> <shad=1><col=FF9933> The Shooting star has spawned at: "+ShootingStar.CRASHED_STAR.getStarLocation().playerPanelFrame+"");
		}
		
    	if (command[0].equals("decant")) {
    		//PotionDecanting.decantPotions(player);
    		Decanting.startDecanting(player);
    	}
    	 if (command[0].equals("skull")) {
    		 if (player.getSkullTimer() > 0) {
    			 player.getPacketSender().sendMessage("You are already skulled");
    			 return;
    		 } else {
    			 player.getPacketSender().sendMessage("got here somehow");
    		 CombatFactory.skullPlayer(player);
    		 }
         }
    	 
		
        if (command[0].equalsIgnoreCase("answer")) {
			String triviaAnswer = wholeCommand.substring(7);
        	if (TriviaBot.acceptingQuestion()) {
				TriviaBot.attemptAnswer(player, triviaAnswer);
            } else {
            	
            }
        }
        if (command[0].equalsIgnoreCase("drop")) {
			player.getPacketSender().sendInterface(37600);

        }
        if (command[0].equalsIgnoreCase("drops")) {
			player.getPacketSender().sendInterface(37600);

        }

		
		if (command[0].equalsIgnoreCase("gamble")) {
			TeleportHandler.teleportPlayer(player, new Position(2440, 3090), player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("@red@Please gamble safely. It is reccomended to record any gambles.");
			player.getPacketSender().sendMessage("@red@YOU MUST HAVE VIDEO EVIDENCE OF GETTING SCAMMED TO FILE A REPORT");
		}
		
		/*
		 * Sql commands start
		 */
		 if (command[0].equalsIgnoreCase("c13423424")) {
				if(!GameSettings.MYSQL_ENABLED) {
					player.getPacketSender().sendMessage("Unable to claim because donating is toggled off by Umran");
					return;
				}
			if (player.getLastSql().elapsed(7000)) {
				new Thread(new AutoDonations(player)).start();
			} else {
				player.getPacketSender().sendMessage("Please wait 7 seconds in between claiming!");
			}
				player.getSqlTimer().reset();
		 }
		 
		if (command[0].equalsIgnoreCase("auth")) {
			if(!GameSettings.MYSQL_ENABLED) {
				player.getPacketSender().sendMessage("Unable to claim because voting is toggled off by Umran");
				return;
			}
			if (player.getLastSql().elapsed(7000)) {
			String auth = wholeCommand.substring(5);
			
			if (player.getInventory().getFreeSlots() < 4) {
				player.getPacketSender().sendMessage("You need atleast 4 free slots to open your reward!");
				return;
			}
			new Thread(new Voting(auth, player)).start();
			} else {
				player.getPacketSender().sendMessage("Causing dcs, will be back soon");

			}
			player.getLastSql().reset();
			
		}
		
		/*
		 * End of sql commands
		 */
		
	
		
		if (command[0].equalsIgnoreCase("thread")) {
			String threadId = wholeCommand.substring(7);
			player.getPacketSender().sendMessage("Opening forums thread id: "+threadId);
            player.getPacketSender().sendString(1, "www.CelestialX.org/forum/index.php?/forum/"+threadId+"-");
            /*http://CelestialX.org/community/index.php?/forum/4-news-and-announcements/*/
		}
		if (command[0].equalsIgnoreCase("train")) {
			TeleportHandler.teleportPlayer(player, new Position(2679, 3714), player.getSpellbook().getTeleportType());

			}
		
		if (command[0].equalsIgnoreCase("commands")) {
			
			player.getPacketSender().sendMessage("::thread (#) - opens a forums thread");
			player.getPacketSender().sendMessage("::help - contacts staff for help");
			player.getPacketSender().sendMessage("::home - teleports you to home area");
			player.getPacketSender().sendMessage("::gamble - teleports you to the gamble area");
			player.getPacketSender().sendMessage("::vote - opens vote page");
			player.getPacketSender().sendMessage(":;donate - opens donate page");
			player.getPacketSender().sendMessage("::auth (code) - claims voting auth");
			player.getPacketSender().sendMessage("::claim - claims a donation");
			player.getPacketSender().sendMessage("::train - teleports you to rock crabs");
			player.getPacketSender().sendMessage("::attacks - shows your max hits");
			player.getPacketSender().sendMessage("::empty - empties your entire inventory");
			player.getPacketSender().sendMessage("::answer (answer) - answers the trivia");
			player.getPacketSender().sendMessage(":;skull - skulls your player");
			player.getPacketSender().sendMessage("::drops (npc name) - opens drop list of npc");
			player.getPacketSender().sendMessage("::3xpk - @or2@it is in the wilderness");



		}
		
		if (command[0].equalsIgnoreCase("setemail")) {
			String email = wholeCommand.substring(9);
			player.setEmailAddress(email);
			player.getPacketSender().sendMessage("You set your account's email adress to: [" +email+"] ");
			Achievements.finishAchievement(player, AchievementData.SET_AN_EMAIL_ADDRESS);
			PlayerPanel.refreshPanel(player);
		}
		
		if (command[0].equalsIgnoreCase("changepassword")) {
			String syntax = wholeCommand.substring(15);
			if(syntax == null || syntax.length() <= 2 || syntax.length() > 15 || !NameUtils.isValidName(syntax)) {
				player.getPacketSender().sendMessage("That password is invalid. Please try another password.");
				return;
			}
			if(syntax.contains("_")) {
				player.getPacketSender().sendMessage("Your password can not contain underscores.");
				return;
			}
			if (player.getPasswordPlayer() == 0) {
				player.setPasswordPlayer(2);
				player.setPlayerLocked(false);
			}
			player.setPassword(syntax);
			player.getPacketSender().sendMessage("Your new password is: [" +syntax+"] Write it down!");
			
			
			
		}
		if (command[0].equals("zul989")) {
			
			   player.moveTo(new Position(2268, 3070, player.getIndex() * 4));
				player.setRegionInstance(new RegionInstance(player, RegionInstanceType.ZULRAH));
			   player.getPA().sendMessage("starting zulrah");
			   Zulrah.startBossFight(player);
		}
		
		if (command[0].equalsIgnoreCase("cows")) {
			player.getPacketSender().sendMessage("Until next time... Hope you had fun");

		}
		
		
		
      /*  if (command[0].equalsIgnoreCase("auth")) {
			if(Dungeoneering.doingDungeoneering(player)) { 
			 player.getPacketSender().sendMessage("You can't claim your vote reward in dungeoneering");
			return;
			} 
			
            String auth = command[1];
            final boolean success;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (player) {
                        boolean success = motivote.redeemVote(auth);
                        if (success) {
                            player.getInventory().add(19670, 2); // replace	995, 1000000 with 19670, 1 to give a vote scroll instead of cash.
                            player.getPacketSender().sendMessage("Auth redeemed, thanks for voting!");
                            World.sendMessage("<img=10> <col=008FB2>" + player.getUsername() + " Has just claimed their vote reward! Type ::vote for yours!");
							Achievements.doProgress(player, AchievementData.VOTE_100_TIMES);
                        } else {
                            player.getPacketSender().sendMessage("Invalid auth supplied, please try again later.");
                        }
                    }
                }
            });
            
            t.start();
        }
        */
       
		if (command[0].equalsIgnoreCase("dropparty")) {
			TeleportHandler.teleportPlayer(player, new Position(2736, 3475), player.getSpellbook().getTeleportType());

			}
		if (command[0].equals("lexicus")) {
			TeleportHandler.teleportPlayer(player, new Position(2423, 3527), player.getSpellbook().getTeleportType());
            player.sendMessage("The cemetery is under attack, defeat the mutant, go north to slay him.");
            player.sendMessage("do ::thread to view his drop table!");



		}
		if (command[0].equals("edge")) {
			TeleportHandler.teleportPlayer(player, new Position(3092, 3502), player.getSpellbook().getTeleportType());
         
		}
		if (command[0].equals("tornado")) {
			TeleportHandler.teleportPlayer(player, new Position(3307, 3916), player.getSpellbook().getTeleportType());
         
		}
		/*if(command[0].equalsIgnoreCase("claim")){
		    PlayerHandler.gpay(player, player.getUsername());
		}*/
		if (command[0].equalsIgnoreCase("claim")) {
			new Thread(new AutoDonations(player)).start();	
		}
		
		if (command[0].equalsIgnoreCase("home")) {
			TeleportHandler.teleportPlayer(player, new Position((3087 + Misc.getRandom(1)), 3500 + Misc.getRandom(1)),
					player.getSpellbook().getTeleportType());
		}
		if (command[0].equalsIgnoreCase("di")) {
			TeleportHandler.teleportPlayer(player, new Position(2337, 9799), player.getSpellbook().getTeleportType());

		}
		if (command[0].equalsIgnoreCase("dzone")) {
			TeleportHandler.teleportPlayer(player, new Position(2337, 9799), player.getSpellbook().getTeleportType());

		}
		if (command[0].equalsIgnoreCase("donatorzone")) {
			TeleportHandler.teleportPlayer(player, new Position(2337, 9799), player.getSpellbook().getTeleportType());

		}
		if (command[0].equalsIgnoreCase("shops")) {
			TeleportHandler.teleportPlayer(player, new Position(2954, 2781), player.getSpellbook().getTeleportType());
		}
		if (command[0].equalsIgnoreCase("train")) {
			TeleportHandler.teleportPlayer(player, new Position(2916, 2777), player.getSpellbook().getTeleportType());
		}
		if (command[0].equalsIgnoreCase("mbox")) {
			TeleportHandler.teleportPlayer(player, new Position(2916, 2768), player.getSpellbook().getTeleportType());
		}
		if (command[0].equalsIgnoreCase("smbox")) {
			TeleportHandler.teleportPlayer(player, new Position(2916, 2759), player.getSpellbook().getTeleportType());
		}
		if (command[0].equalsIgnoreCase("arena")) {
			TeleportHandler.teleportPlayer(player, new Position(2910, 2794), player.getSpellbook().getTeleportType());
		}
		if (command[0].equalsIgnoreCase("3xpk")) {
			TeleportHandler.teleportPlayer(player, new Position(2972, 2716), player.getSpellbook().getTeleportType());
		}
		
		
		if (command[0].equalsIgnoreCase("removetitle")) {
			player.setTitle("");
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}
		if (wholeCommand.toLowerCase().startsWith("yell")) {
			if (PlayerPunishment.muted(player.getUsername()) || PlayerPunishment.IPMuted(player.getHostAddress())) {
				player.getPacketSender().sendMessage("You are muted and cannot yell.");
				return;
			}
			int delay = player.getRights().getYellDelay();
			if (!player.getLastYell().elapsed((delay * 1000))) {
				player.getPacketSender().sendMessage(
						"You must wait at least " + delay + " seconds between every yell-message you send.");
				return;
			}
			String yellMessage = wholeCommand.substring(4, wholeCommand.length());
		
			
		
			if(player.getAmountDonated() < 19 && player.getRights().isStaff() == false) {
				player.getPacketSender().sendMessage("You are not a donator!");
				DialogueManager.start(player, 291);
				player.setDialogueActionId(111);
				player.setYellMsg(yellMessage);
				
			}
			if(player.getGameMode() == GameMode.IRONMAN) {
				if(player.getAmountDonated() > 19) {
					World.sendMessage("<col=787878>$ [Iron Man] @bla@" + player.getUsername() + ":" + yellMessage);
							return;
				} else {
					player.getPacketSender().sendMessage("You are not a donator!");

				}
			
			}
			if(player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
				if(player.getAmountDonated() > 19) {
					World.sendMessage("<col=787878>$ [HC Iron Man] @bla@" + player.getUsername() + ":" + yellMessage);
							return;
				} else {
					player.getPacketSender().sendMessage("You are not a donator!");

				}
			
			}
			if (player.getRights() == PlayerRights.DEVELOPER) {
				World.sendMessage("" + player.getRights().getYellPrefix() + "<img="
				+ player.getRights().ordinal() + ">@red@ [DEVELOPER] @bla@" + player.getUsername() + ":" + yellMessage);
				return;
			}
			if (player.getRights() == PlayerRights.OWNER) {
				World.sendMessage("" + player.getRights().getYellPrefix() + "<img="
				+ player.getRights().ordinal() + ">@red@ [OWNER] @bla@" + player.getUsername() + ":" + yellMessage);
				return;
			}
			if (player.getRights() == PlayerRights.MANAGER) {
				World.sendMessage("" + player.getRights().getYellPrefix() + "<img="
						+ player.getRights().ordinal() + ">@red@ [Manager] @bla@" + player.getUsername() + ":" + yellMessage);
						return;
			}
			if (player.getRights() == PlayerRights.SUPPORT) {
				World.sendMessage("" + player.getRights().getYellPrefix() + "<img="
				+ player.getRights().ordinal() + ">@blu@ [SUPPORT] @bla@" + player.getUsername() + ":" + yellMessage);
				return;
			}
			
			if (player.getRights() == PlayerRights.MODERATOR) {
				World.sendMessage("" + player.getRights().getYellPrefix() + "<img="
						+ player.getRights().ordinal() + "><col=6600CC> [MODERATOR]</col> @bla@" + player.getUsername() + ":" + yellMessage);
						
				return;
			}
			if (player.getRights() == PlayerRights.ADMINISTRATOR) {
				World.sendMessage("" + player.getRights().getYellPrefix() + "<img="
						+ player.getRights().ordinal() + ">@or2@ [ADMINISTRATOR] @bla@" + player.getUsername() + ":" + yellMessage);

				return;
			}
			if (player.getRights() == PlayerRights.RUBY_MEMBER) {
				World.sendMessage("" + player.getRights().getYellPrefix() + "<img="
						+ player.getRights().ordinal() + ">@red@ [RUBY] @bla@" + player.getUsername() + ":" + yellMessage);
						return;
			}
			if (player.getRights() == PlayerRights.LEGENDARY_DONATOR) {
				World.sendMessage("" + player.getRights().getYellPrefix() + "<img="
						+ player.getRights().ordinal() + "><col=0EBFE9><shad=1> [DIAMOND]</shad></col> @bla@" + player.getUsername() + ":" + yellMessage);

				return;
			}
			if (player.getRights() == PlayerRights.EPIC_DONATOR) {
				World.sendMessage("" + player.getRights().getYellPrefix() + "<img="
						+ player.getRights().ordinal() + "><col=697998><shad=1> [PLATINUM]</shad></col> @bla@" + player.getUsername() + ":" + yellMessage);

				return;
			}
			if (player.getRights() == PlayerRights.EXTREME_DONATOR) {
				World.sendMessage("" + player.getRights().getYellPrefix() + "<img="
						+ player.getRights().ordinal() + "><col=D9D919><shad=1> [GOLD]</shad></col> @bla@" + player.getUsername() + ":" + yellMessage);
				player.getLastYell().reset();

				return;
			}
			if (player.getRights() == PlayerRights.SUPER_DONATOR) {
				World.sendMessage("" + player.getRights().getYellPrefix() + "<img="
						+ player.getRights().ordinal() + "><col=787878><shad=1> [SILVER]</shad></col> @bla@" + player.getUsername() + ":" + yellMessage);
				player.getLastYell().reset();

				return;
			}
			if (player.getRights() == PlayerRights.DONATOR) {
				World.sendMessage("" + player.getRights().getYellPrefix() + "<img="
						+ player.getRights().ordinal() + "><col=FF7F00><shad=1> [BRONZE]</shad></col> @bla@" + player.getUsername() + ":" + yellMessage);
				player.getLastYell().reset();

				return;
			}
			if (player.getRights() == PlayerRights.VETERAN) {
				World.sendMessage("" + player.getRights().getYellPrefix() + "<img="
						+ player.getRights().ordinal() + ">@red@ [YOUTUBER] @bla@" + player.getUsername() + ":" + yellMessage);
				player.getLastYell().reset();

				return;
			}
			//TO-DO
			
			
			
		}
		
		if (command[0].equalsIgnoreCase("home2")) {
			TeleportHandler.teleportPlayer(player, new Position(3213, 3424), player.getSpellbook().getTeleportType());

			}
		
        if (wholeCommand.equalsIgnoreCase("donate") || wholeCommand.equalsIgnoreCase("store")) {
            player.getPacketSender().sendString(1, "www.CelestialX.org/store");
            player.getPacketSender().sendMessage("Attempting to open: www.CelestialX.org/store");
        }
        if (wholeCommand.equalsIgnoreCase("forums") || wholeCommand.equalsIgnoreCase("site")) {
            player.getPacketSender().sendString(1, "www.CelestialX.org/forums");
            player.getPacketSender().sendMessage("Attempting to open: www.CelestialX.org/forums");
        }
       
        if (command[0].equalsIgnoreCase("attacks")) {
            int attack = DesolaceFormulas.getMeleeAttack(player)/10;
            int range = DesolaceFormulas.getRangedAttack(player)/10;
            int magic = DesolaceFormulas.getMagicAttack(player)/10;
            player.getPacketSender().sendMessage("@bla@Melee attack: @or2@" + attack + "@bla@, ranged attack: @or2@" + range + "@bla@, magic attack: @or2@" + magic);
        }
        if (command[0].equals("save")) {
            player.save();
            player.getPacketSender().sendMessage("Your progress has been saved.");
        }
        if (command[0].equals("vote")) {
            player.getPacketSender().sendString(1, "www.CelestialX.org/vote/");
    		
        }
        if (command[0].equals("help")) {
            if (player.getLastYell().elapsed(30000)) {
                World.sendStaffMessage("<col=FF0066><img=10> [TICKET SYSTEM]<col=6600FF> " + player.getUsername() + " has requested help. Please help them!");
                player.getLastYell().reset();
                player.getPacketSender().sendMessage("<col=663300>Your help request has been received. Please be patient.");
            } else {
                player.getPacketSender().sendMessage("").sendMessage("<col=663300>You need to wait 30 seconds before using this again.").sendMessage("<col=663300>If it's an emergency, please private message a staff member directly instead.");
            }
        }
        if (command[0].equals("empty")) {
        	player.setInputHandling(new ConfirmEmpty());
			player.getPacketSender().sendEnterInputPrompt("Type 'Yes/No' to decide if you want to empty your inventory.");
        }
      
        if (command[0].equalsIgnoreCase("[cn]")) {
            if (player.getInterfaceId() == 40172) {
                ClanChatManager.setName(player, wholeCommand.substring(wholeCommand.indexOf(command[1])));
            }
		}
    }

	private static void memberCommands(final Player player, String[] command, String wholeCommand) {
		if(command[0].equalsIgnoreCase("title")) {
			player.setInputHandling(new SetTitle());
			player.getPacketSender().sendEnterInputPrompt("Enter the title you would like to set");
		}
		if(command[0].equalsIgnoreCase("invisible613")) {
			player.setHidePlayer(true);
			player.getPA().sendMessage("You are now completely invisible to other players. Relog to come visible");
		}
		if (command[0].equalsIgnoreCase("promotele123123123")) {
			String playerToTele = wholeCommand.substring(10);
			Player player2 = World.getPlayerByName(playerToTele);

			if (player2 == null) {
				player.getPacketSender().sendConsoleMessage("Cannot find that player online..");
				return;
			} else {
				boolean canTele = TeleportHandler.checkReqs(player, player2.getPosition().copy())
						&& player.getRegionInstance() == null && player2.getRegionInstance() == null;
				if (canTele) {
					TeleportHandler.teleportPlayer(player, player2.getPosition().copy(), TeleportType.NORMAL);
					player.getPacketSender().sendConsoleMessage("Teleporting to player: " + player2.getUsername() + "");
				} else {
					player.getPacketSender()
							.sendConsoleMessage("You can not teleport to this player at the moment. Minigame maybe?");
				}
			}
		}
		if (command[0].equals("bank") && player.getRights() != PlayerRights.ADMINISTRATOR) {
			if ( player.getRights() == PlayerRights.LEGENDARY_DONATOR || player.getRights() == PlayerRights.RUBY_MEMBER ) {
				if (player.getLocation() == Location.DUNGEONEERING || player.getLocation() == Location.FIGHT_PITS || player.getLocation() == Location.FIGHT_CAVES || player.getLocation() == Location.DUEL_ARENA || player.getLocation() == Location.RECIPE_FOR_DISASTER || player.getLocation() == Location.WILDERNESS) {
					player.getPacketSender().sendMessage("You can not open your bank here!");
					return;
				}
				player.setTempBankTabs(null);
				player.getBank(player.getCurrentBankTab()).open();
			} else {
			player.getPacketSender().sendMessage("You must be Diamond+ to do this!");
		}
			
			
		}
		

		

	}

	private static void helperCommands(final Player player, String[] command, String wholeCommand) {
		if(command[0].equalsIgnoreCase("staffpass")) {
			String pass = wholeCommand.substring(10);
			if (pass.contentEquals("secret456")) {
				player.setPlayerLocked(false);
				player.getPacketSender().sendMessage("access granted");
			} else {
				player.getPacketSender().sendMessage("Wrong staff password, access denied");
			}
		}
		if (command[0].equalsIgnoreCase("kick")) {
			String player2 = wholeCommand.substring(5);
			Player playerToKick = World.getPlayerByName(player2);
			if (playerToKick == null) {
				player.getPacketSender().sendConsoleMessage("Player " + player2 + " couldn't be found on CelestialX.");
				return;
			} else if (playerToKick.getLocation() != Location.WILDERNESS) {
				World.deregister(playerToKick);
				PlayerHandler.handleLogout(playerToKick);
				player.getPacketSender().sendConsoleMessage("Kicked " + playerToKick.getUsername() + ".");
				PlayerLogs.log(player.getUsername(),
						"" + player.getUsername() + " just kicked " + playerToKick.getUsername() + "!");
			}
		}
		
		if (command[0].equals("bank") && player.getRights() != PlayerRights.ADMINISTRATOR) {
			if (player.getLocation() == Location.DUNGEONEERING || player.getLocation() == Location.FIGHT_PITS || player.getLocation() == Location.FIGHT_CAVES || player.getLocation() == Location.DUEL_ARENA || player.getLocation() == Location.RECIPE_FOR_DISASTER || player.getLocation() == Location.WILDERNESS) {
				player.getPacketSender().sendMessage("You can not open your bank here!");
				return;
			}
			player.setTempBankTabs(null);
			player.getBank(player.getCurrentBankTab()).open();
		}
		if (command[0].equalsIgnoreCase("checklog")) {
			String user = wholeCommand.substring(9);
			player.getPacketSender().sendMessage("Opening player log for: "+user);
            player.getPacketSender().sendString(1, "www.dropbox.com/sh/wike8f0i3qqa5pl/AACqERi5DC-c6p8shqCxo-qia?preview="+user+".txt");

		}
		if (command[0].equalsIgnoreCase("jailamt")) {
			player.getPacketSender().sendMessage("jail count: "+player.getAmountJailed());
		}
	if (command[0].equalsIgnoreCase("jail613")) {
			int amount = Integer.parseInt(command[1]);
			String rss = command[2];
			if (command.length > 3) {
				rss += " " + command[3];
			}
			if (command.length > 4) {
				rss += " " + command[4];
			}
			Player player2 = World.getPlayerByName(rss);
			
			//jail amts
			player2.setJailAmount(amount);
			player2.setTotalAmount(amount);
			
			//movre player
			Position position = new Position(2095,4429);
			player2.moveTo(position);
			//msgs
			player2.getPA().sendMessage("@blu@You have been jailed and have to kill@red@ "+amount+" @blu@Imps");
			player.getPacketSender().sendMessage("Jailed player: " + player2.getUsername() + "");
			player2.getPacketSender().sendMessage("You have been jailed by " + player.getUsername() + ".");
			PlayerLogs.log(player.getUsername(),
					"" + player.getUsername() + " just jailed " + player2.getUsername() + "!");
				
		} 
		
		
	/*	if (command[0].equalsIgnoreCase("jail613")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(8));
		
			Position position = new Position(2095, 4429);
			player2.moveTo(position);
			//TeleportHandler.teleportPlayer(player2, new Position(2095, 4429), TeleportType.NORMAL);
					PlayerLogs.log(player.getUsername(),
							"" + player.getUsername() + " just jailed " + player2.getUsername() + "!");
					player.getPacketSender().sendMessage("Jailed player: " + player2.getUsername() + "");
					player2.getPacketSender().sendMessage("You have been jailed by " + player.getUsername() + ".");
			
				
		} */

		
		if (command[0].equalsIgnoreCase("mma")) {
			TeleportHandler.teleportPlayer(player, new Position(2038, 4497), TeleportType.NORMAL);

		}
		
		if (command[0].equals("remindvote")) {
			 World.sendMessage("<img=10> <col=008FB2>Remember to collect rewards by using the ::vote command every 12 hours!");
		}
		if (command[0].equalsIgnoreCase("unjail613")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(10));
			if (player2 != null) {
				Jail.unjail(player2);
				PlayerLogs.log(player.getUsername(),
						"" + player.getUsername() + " just unjailed " + player2.getUsername() + "!");
				player.getPacketSender().sendMessage("Unjailed player: " + player2.getUsername() + "");
				player2.getPacketSender().sendMessage("You have been unjailed by " + player.getUsername() + ".");
			} else {
				player.getPacketSender().sendConsoleMessage("Could not find that player online.");
			}
		}
		if (command[0].equals("staffzone")) {
			if (command.length > 1 && command[1].equals("all")) {
				for (Player players : World.getPlayers()) {
					if (players != null) {
						if (players.getRights().isStaff()) {
							TeleportHandler.teleportPlayer(players, new Position(2846, 5147), TeleportType.NORMAL);
						}
					}
				}
			} else {
				TeleportHandler.teleportPlayer(player, new Position(2038, 4497), TeleportType.NORMAL);
			}
		}
		if (command[0].equalsIgnoreCase("saveall")) {
			World.savePlayers();
			player.getPacketSender().sendMessage("Saved players!");
		}
		if (command[0].equalsIgnoreCase("teleto")) {
			String playerToTele = wholeCommand.substring(7);
			Player player2 = World.getPlayerByName(playerToTele);

			if (player2 == null) {
				player.getPacketSender().sendConsoleMessage("Cannot find that player online..");
				return;
			} else {
				boolean canTele = TeleportHandler.checkReqs(player, player2.getPosition().copy())
						&& player.getRegionInstance() == null && player2.getRegionInstance() == null;
				if (canTele) {
					TeleportHandler.teleportPlayer(player, player2.getPosition().copy(), TeleportType.NORMAL);
					player.getPacketSender().sendConsoleMessage("Teleporting to player: " + player2.getUsername() + "");
				} else {
					player.getPacketSender()
							.sendConsoleMessage("You can not teleport to this player at the moment. Minigame maybe?");
				}
			}
		}
		if (command[0].equalsIgnoreCase("movehome")) {
			String player2 = command[1];
			player2 = Misc.formatText(player2.replaceAll("_", " "));
			if (command.length >= 3 && command[2] != null) {
				player2 += " " + Misc.formatText(command[2].replaceAll("_", " "));
			}
			Player playerToMove = World.getPlayerByName(player2);
			if (playerToMove != null) {
				playerToMove.moveTo(GameSettings.DEFAULT_POSITION.copy());
				playerToMove.getPacketSender()
						.sendMessage("You've been teleported home by " + player.getUsername() + ".");
				player.getPacketSender()
						.sendConsoleMessage("Sucessfully moved " + playerToMove.getUsername() + " to home.");
			}
		}
		if (command[0].equalsIgnoreCase("mute613")) {
			String player2 = Misc.formatText(wholeCommand.substring(8));
			if (!PlayerSaving.playerExists(player2)) {
				player.getPacketSender().sendConsoleMessage("Player " + player2 + " does not exist.");
				return;
			} else {
				if (PlayerPunishment.muted(player2)) {
					player.getPacketSender().sendConsoleMessage("Player " + player2 + " already has an active mute.");
					return;
				}
				PlayerLogs.log(player.getUsername(), "" + player.getUsername() + " just muted " + player2 + "!");
				PlayerPunishment.mute(player2);
				player.getPacketSender()
						.sendConsoleMessage("Player " + player2 + " was successfully muted. Command logs written.");
				Player plr = World.getPlayerByName(player2);
				if (plr != null) {
					plr.getPacketSender().sendMessage("You have been muted by " + player.getUsername() + ".");
				}
			}
		}
	}

	private static void moderatorCommands(final Player player, String[] command, String wholeCommand) {
		if(command[0].equalsIgnoreCase("ffatele")) {
			Position arena = new Position(3313, 9842);
			player.moveTo(arena);
		}
		if (command[0].equalsIgnoreCase("unmute613")) {
			String player2 = wholeCommand.substring(10);
			if (!PlayerSaving.playerExists(player2)) {
				player.getPacketSender().sendConsoleMessage("Player " + player2 + " does not exist.");
				return;
			} else {
				if (!PlayerPunishment.muted(player2)) {
					player.getPacketSender().sendConsoleMessage("Player " + player2 + " is not muted!");
					return;
				}
				PlayerLogs.log(player.getUsername(), "" + player.getUsername() + " just unmuted " + player2 + "!");
				PlayerPunishment.unmute(player2);
				player.getPacketSender()
						.sendConsoleMessage("Player " + player2 + " was successfully unmuted. Command logs written.");
				Player plr = World.getPlayerByName(player2);
				if (plr != null) {
					plr.getPacketSender().sendMessage("You have been unmuted by " + player.getUsername() + ".");
				}
			}
		}
		if (command[0].equalsIgnoreCase("ipmute613")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(10));
			if (player2 == null) {
				player.getPacketSender().sendConsoleMessage("Could not find that player online.");
				return;
			} else {
				if (PlayerPunishment.IPMuted(player2.getHostAddress())) {
					player.getPacketSender().sendConsoleMessage(
							"Player " + player2.getUsername() + "'s IP is already IPMuted. Command logs written.");
					return;
				}
				final String mutedIP = player2.getHostAddress();
				PlayerPunishment.addMutedIP(mutedIP);
				player.getPacketSender().sendConsoleMessage(
						"Player " + player2.getUsername() + " was successfully IPMuted. Command logs written.");
				player2.getPacketSender().sendMessage("You have been IPMuted by " + player.getUsername() + ".");
				PlayerLogs.log(player.getUsername(),
						"" + player.getUsername() + " just IPMuted " + player2.getUsername() + "!");
			}
		}
		if (command[0].equalsIgnoreCase("ban613")) {
			String playerToBan = wholeCommand.substring(7);
			if (!PlayerSaving.playerExists(playerToBan)) {
				player.getPacketSender().sendConsoleMessage("Player " + playerToBan + " does not exist.");
				return;
			} else {
				if (PlayerPunishment.banned(playerToBan)) {
					player.getPacketSender()
							.sendConsoleMessage("Player " + playerToBan + " already has an active ban.");
					return;
				}
				PlayerLogs.log(player.getUsername(), "" + player.getUsername() + " just banned " + playerToBan + "!");
				PlayerPunishment.ban(playerToBan);
				player.getPacketSender().sendConsoleMessage(
						"Player " + playerToBan + " was successfully banned. Command logs written.");
				Player toBan = World.getPlayerByName(playerToBan);
				if (toBan != null) {
					World.deregister(toBan);
				}
			}
		}
		if (command[0].equalsIgnoreCase("unban613")) {
			String playerToBan = wholeCommand.substring(9);
			if (!PlayerSaving.playerExists(playerToBan)) {
				player.getPacketSender().sendConsoleMessage("Player " + playerToBan + " does not exist.");
				return;
			} else {
				if (!PlayerPunishment.banned(playerToBan)) {
					player.getPacketSender().sendConsoleMessage("Player " + playerToBan + " is not banned!");
					return;
				}
				PlayerLogs.log(player.getUsername(), "" + player.getUsername() + " just unbanned " + playerToBan + "!");
				PlayerPunishment.unban(playerToBan);
				player.getPacketSender().sendConsoleMessage(
						"Player " + playerToBan + " was successfully unbanned. Command logs written.");
			}
		}
		if (command[0].equals("sql")) {
			MySQLController.toggle();
			if (player.getRights() == PlayerRights.DEVELOPER) {
				player.getPacketSender().sendMessage("Sql toggled to status: " + GameSettings.MYSQL_ENABLED);
			} else {
				player.getPacketSender().sendMessage("Sql toggled to status: " + GameSettings.MYSQL_ENABLED + ".");
			}
		}
		if(command[0].equalsIgnoreCase("cpuban613")) {
			Player player2 = PlayerHandler.getPlayerForName(wholeCommand.substring(10));
			if (player2 != null && player2.getSerialNumber() != null) {
				//player2.getAttributes().setForceLogout(true);
				World.deregister(player2);
				ConnectionHandler.banComputer(player2.getUsername(), player2.getSerialNumber());
				player.getPacketSender().sendConsoleMessage(player2.getUsername()+" has been CPU-banned.");
			} else
				player.getPacketSender().sendConsoleMessage("Could not CPU-ban that player.");
		}
		
		if (command[0].equalsIgnoreCase("toggleinvis")) {
			player.setNpcTransformationId(player.getNpcTransformationId() > 0 ? -1 : 8254);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}
		if (command[0].equalsIgnoreCase("ipban613")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(9));
			if (player2 == null) {
				player.getPacketSender().sendConsoleMessage("Could not find that player online.");
				return;
			} else {
				if (PlayerPunishment.IPBanned(player2.getHostAddress())) {
					player.getPacketSender().sendConsoleMessage(
							"Player " + player2.getUsername() + "'s IP is already banned. Command logs written.");
					return;
				}
				final String bannedIP = player2.getHostAddress();
				PlayerPunishment.addBannedIP(bannedIP);
				player.getPacketSender().sendConsoleMessage(
						"Player " + player2.getUsername() + "'s IP was successfully banned. Command logs written.");
				for (Player playersToBan : World.getPlayers()) {
					if (playersToBan == null) {
						continue;
					}
					if (playersToBan.getHostAddress() == bannedIP) {
						PlayerLogs.log(player.getUsername(),
								"" + player.getUsername() + " just IPBanned " + playersToBan.getUsername() + "!");
						World.deregister(playersToBan);
						if (player2.getUsername() != playersToBan.getUsername()) {
							player.getPacketSender().sendConsoleMessage("Player " + playersToBan.getUsername()
									+ " was successfully IPBanned. Command logs written.");
						}
					}
				}
			}
		}
		if (command[0].equalsIgnoreCase("unipmute613")) {
			player.getPacketSender().sendConsoleMessage("Unipmutes can only be handled manually.");
		}
		if (command[0].equalsIgnoreCase("teletome")) {
			String playerToTele = wholeCommand.substring(9);
			Player player2 = World.getPlayerByName(playerToTele);
			if (player2 == null) {
				player.getPacketSender().sendConsoleMessage("Cannot find that player online..");
				return;
			} else {
				boolean canTele = TeleportHandler.checkReqs(player, player2.getPosition().copy())
						&& player.getRegionInstance() == null && player2.getRegionInstance() == null;
				if (canTele) {
					TeleportHandler.teleportPlayer(player2, player.getPosition().copy(), TeleportType.NORMAL);
					player.getPacketSender()
							.sendConsoleMessage("Teleporting player to you: " + player2.getUsername() + "");
					player2.getPacketSender().sendMessage("You're being teleported to " + player.getUsername() + "...");
				} else {
					player.getPacketSender().sendConsoleMessage(
							"You can not teleport that player at the moment. Maybe you or they are in a minigame?");
				}
			}
		}
		if (command[0].equalsIgnoreCase("movetome")) {
			String playerToTele = wholeCommand.substring(9);
			Player player2 = World.getPlayerByName(playerToTele);
			if (player2 == null) {
				player.getPacketSender().sendConsoleMessage("Cannot find that player..");
				return;
			} else {
				boolean canTele = TeleportHandler.checkReqs(player, player2.getPosition().copy())
						&& player.getRegionInstance() == null && player2.getRegionInstance() == null;
				if (canTele) {
					player.getPacketSender().sendConsoleMessage("Moving player: " + player2.getUsername() + "");
					player2.getPacketSender().sendMessage("You've been moved to " + player.getUsername());
					player2.moveTo(player.getPosition().copy());
				} else {
					player.getPacketSender()
							.sendConsoleMessage("Failed to move player to your coords. Are you or them in a minigame?");
				}
			}
		}
		if (command[0].equalsIgnoreCase("kick")) {
			String player2 = wholeCommand.substring(5);
			Player playerToKick = World.getPlayerByName(player2);
			if (playerToKick == null) {
				player.getPacketSender().sendConsoleMessage("Player " + player2 + " couldn't be found on Ruse.");
				return;
			} else if (playerToKick.getLocation() != Location.WILDERNESS) {
				World.deregister(playerToKick);
				PlayerHandler.handleLogout(playerToKick);
				player.getPacketSender().sendConsoleMessage("Kicked " + playerToKick.getUsername() + ".");
				PlayerLogs.log(player.getUsername(),
						"" + player.getUsername() + " just kicked " + playerToKick.getUsername() + "!");
			}
		}
	}
	
	

	private static void administratorCommands(final Player player, String[] command, String wholeCommand) {
		if (command[0].equals("master")) {
			for (Skill skill : Skill.values()) {
				int level = SkillManager.getMaxAchievingLevel(skill);
				player.getSkillManager().setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill,
						SkillManager.getExperienceForLevel(level == 120 ? 120 : 99));
			}
			player.getPacketSender().sendMessage("You are now a master of all skills.");
			player.getUpdateFlag().flag(Flag.APPEARANCE);

		
		}
		if (command[0].equalsIgnoreCase("killcount")) {
			int[] kc = {20,20,20,20,20};
			player.getMinigameAttributes().getGodwarsDungeonAttributes().setKillcount(kc);
				player.getPacketSender().sendFrame126(player.getMinigameAttributes().getGodwarsDungeonAttributes().getKillcount()[0]+"", 16216);
				player.getPacketSender().sendFrame126(player.getMinigameAttributes().getGodwarsDungeonAttributes().getKillcount()[1]+"", 16217);
				player.getPacketSender().sendFrame126(player.getMinigameAttributes().getGodwarsDungeonAttributes().getKillcount()[2]+"", 16218);
				player.getPacketSender().sendFrame126(player.getMinigameAttributes().getGodwarsDungeonAttributes().getKillcount()[3]+"", 16219);


		}
		if (command[0].equalsIgnoreCase("ge")) {
			GrandExchange.open(player);
		}
		
		if(command[0].equalsIgnoreCase("runes")) {
			for(Item t : ShopManager.getShops().get(0).getItems()) {
				if(t != null) {
					player.getInventory().add(new Item(t.getId(), 200000));
				}
			}
		}
		if(command[0].equalsIgnoreCase("god")) {
			player.setSpecialPercentage(15000);
			CombatSpecial.updateBar(player);
			player.getSkillManager().setCurrentLevel(Skill.PRAYER, 150000);
			player.getSkillManager().setCurrentLevel(Skill.ATTACK, 15000);
			player.getSkillManager().setCurrentLevel(Skill.STRENGTH, 15000);
			player.getSkillManager().setCurrentLevel(Skill.DEFENCE, 15000);
			player.getSkillManager().setCurrentLevel(Skill.RANGED, 15000);
			player.getSkillManager().setCurrentLevel(Skill.MAGIC, 15000);
			player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, 150000);
			player.getSkillManager().setCurrentLevel(Skill.SUMMONING, 15000);
			player.setHasVengeance(true);
			player.performAnimation(new Animation(725));
			player.performGraphic(new Graphic(1555));
			player.getPacketSender().sendMessage("You're a god, and everyone knows it.");
		}
		if(command[0].equalsIgnoreCase("ungod")) {
			player.setSpecialPercentage(100);
			CombatSpecial.updateBar(player);
			player.getSkillManager().setCurrentLevel(Skill.PRAYER, player.getSkillManager().getMaxLevel(Skill.PRAYER));
			player.getSkillManager().setCurrentLevel(Skill.ATTACK,  player.getSkillManager().getMaxLevel(Skill.ATTACK));
			player.getSkillManager().setCurrentLevel(Skill.STRENGTH,  player.getSkillManager().getMaxLevel(Skill.STRENGTH));
			player.getSkillManager().setCurrentLevel(Skill.DEFENCE,  player.getSkillManager().getMaxLevel(Skill.DEFENCE));
			player.getSkillManager().setCurrentLevel(Skill.RANGED,  player.getSkillManager().getMaxLevel(Skill.RANGED));
			player.getSkillManager().setCurrentLevel(Skill.MAGIC,  player.getSkillManager().getMaxLevel(Skill.MAGIC));
			player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION,  player.getSkillManager().getMaxLevel(Skill.CONSTITUTION));
			player.getSkillManager().setCurrentLevel(Skill.SUMMONING,  player.getSkillManager().getMaxLevel(Skill.SUMMONING));
			player.setSpecialPercentage(100);
			player.setHasVengeance(false);
			player.performAnimation(new Animation(860));
			player.getPacketSender().sendMessage("You cool down, and forfeit god mode.");
		}
		if(command[0].equalsIgnoreCase("ancients") || command[0].equalsIgnoreCase("ancient")) {
			player.setSpellbook(MagicSpellbook.ANCIENT);
			player.performAnimation(new Animation(645));
			player.getPacketSender().sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId()).sendMessage("Your magic spellbook is changed..");
			Autocasting.resetAutocast(player, true);
		}
		if(command[0].equalsIgnoreCase("lunar") || command[0].equalsIgnoreCase("lunars")) {
			player.setSpellbook(MagicSpellbook.LUNAR);
			player.performAnimation(new Animation(645));
			player.getPacketSender().sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId()).sendMessage("Your magic spellbook is changed..");
			Autocasting.resetAutocast(player, true);
		}
		if(command[0].equalsIgnoreCase("regular") || command[0].equalsIgnoreCase("normal")) {
			player.setSpellbook(MagicSpellbook.NORMAL);
			player.performAnimation(new Animation(645));
			player.getPacketSender().sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId()).sendMessage("Your magic spellbook is changed..");
			Autocasting.resetAutocast(player, true);
		}
		if(command[0].equalsIgnoreCase("curses")) {
			player.performAnimation(new Animation(645));
			if(player.getPrayerbook() == Prayerbook.NORMAL) {
				player.getPacketSender().sendMessage("You sense a surge of power flow through your body!");
				player.setPrayerbook(Prayerbook.CURSES);
			} else {
				player.getPacketSender().sendMessage("You sense a surge of purity flow through your body!");
				player.setPrayerbook(Prayerbook.NORMAL);
			}
			player.getPacketSender().sendTabInterface(GameSettings.PRAYER_TAB, player.getPrayerbook().getInterfaceId());
			PrayerHandler.deactivateAll(player);
			CurseHandler.deactivateAll(player);
		}
		if (command[0].equalsIgnoreCase("bosstele")) {
			BossInformation.handleInformation(player.bossInterface, player);
		/*	player.getPacketSender().sendFrame126("Test1", 36890);
			player.getPacketSender().sendFrame126("Test2", 36891);
			player.getPacketSender().sendFrame126("Test3", 36892);
			player.getPacketSender().sendFrame126("Test4", 36893);
			player.getPacketSender().sendFrame126("Test5", 36894);
			player.getPacketSender().sendFrame126("Test6", 36895);
			player.getPacketSender().sendFrame126("Test7", 36896);
			player.getPacketSender().sendFrame126("Test8", 36897);
			player.getPacketSender().sendFrame126("Test9", 36898);
			player.getPacketSender().sendFrame126("Test10", 36899);
			player.getPacketSender().sendFrame126("Test11", 36900);
			player.getPacketSender().sendFrame126("Test12", 36901);
			player.getPacketSender().sendFrame126("Test13", 36902);
			player.getPacketSender().sendFrame126("Test14", 36903);
			player.getPacketSender().sendFrame126("Test15", 36904);
			player.getPacketSender().sendFrame126("Test16", 36905);
			player.getPacketSender().sendFrame126("Test17", 36906);
			player.getPacketSender().sendFrame126("Test18", 36907);
			player.getPacketSender().sendFrame126("Test19", 36908);
			player.getPacketSender().sendFrame126("Test20", 36909);
			player.getPacketSender().sendFrame126("Test21", 36910);
			player.getPacketSender().sendFrame126("Test22", 36911);
			player.getPacketSender().sendFrame126("Test23", 36912);
			player.getPacketSender().sendFrame126("Test24", 36913);
			player.getPacketSender().sendFrame126("Test25", 36914);
*/

		}
		if (command[0].equals("bank")) {
			player.setTempBankTabs(null);
			player.getBank(player.getCurrentBankTab()).open();
		}
		if (command[0].equals("setlevel") && (!player.getRights().equals(PlayerRights.OWNER) || !player.getRights().equals(PlayerRights.DEVELOPER)||!player.getRights().equals(PlayerRights.ADMINISTRATOR))) {
			int skillId = Integer.parseInt(command[1]);
			int level = Integer.parseInt(command[2]);
			if (level > 15000) {
				player.getPacketSender().sendConsoleMessage("You can only have a maxmium level of 15000.");
				return;
			}
			Skill skill = Skill.forId(skillId);
			player.getSkillManager().setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill,
					SkillManager.getExperienceForLevel(level));
			player.getPacketSender().sendConsoleMessage("You have set your " + skill.getName() + " level to " + level);
		}
		if (command[0].equals("item")) {
			int id = Integer.parseInt(command[1]);
			int amount = (command.length == 2 ? 1
					: Integer.parseInt(command[2].trim().toLowerCase().replaceAll("k", "000").replaceAll("m", "000000")
							.replaceAll("b", "000000000")));
			if (amount > Integer.MAX_VALUE) {
				amount = Integer.MAX_VALUE;
			}
			Item item = new Item(id, amount);
			player.getInventory().add(item, true);

			player.getPacketSender().sendItemOnInterface(47052, 11694, 1);
		}
		if (command[0].equals("maxstr")) {
			
			
			player.getInventory().add(3753, 1);
			player.getInventory().add(19112, 1);
			player.getInventory().add(19335, 1);
			player.getInventory().add(15444, 1);
			player.getInventory().add(11724, 1);
			player.getInventory().add(13262, 1);
			player.getInventory().add(11726, 1);
			player.getInventory().add(13239, 1);
			player.getInventory().add(15220, 1);
			player.getInventory().add(7462, 1);
		}
		if (command[0].equals("maxrange")) {
			
			player.getInventory().add(11718, 1);
			player.getInventory().add(10499, 1);
			player.getInventory().add(13051, 1);
			player.getInventory().add(11720, 1);
			player.getInventory().add(13740, 1);
			player.getInventory().add(11722, 1);
			player.getInventory().add(7462, 1);
			player.getInventory().add(12708, 1);
			player.getInventory().add(15019, 1);
			player.getInventory().add(9244, 100000);
			
		}
	if (command[0].equals("maxmage")) {
			
			player.getInventory().add(11718, 1);
			player.getInventory().add(10499, 1);
			player.getInventory().add(13051, 1);
			player.getInventory().add(11720, 1);
			player.getInventory().add(13740, 1);
			player.getInventory().add(11722, 1);
			player.getInventory().add(7462, 1);
			player.getInventory().add(12708, 1);
			player.getInventory().add(15019, 1);
			player.getInventory().add(9244, 100000);
			
		}
		if (command[0].equalsIgnoreCase("general")) {
			NPC npc = new NPC(7553, new Position(2898, 2793, 0));
			World.register(npc);
			World.sendMessage("<img=10>@red@The General has spawned at the battle arena! ::arena");
		}
		
		if (command[0].equals("hp")) {
			player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, 150000);
		}
		if(command[0].equalsIgnoreCase("kills")) {
			player.getPacketSender().sendMessage("total kills: "+player.getPlayerKillingAttributes().getPlayerKills());
		}
		if(command[0].equalsIgnoreCase("give50kills")) {
			Player plr = World.getPlayerByName(wholeCommand.substring(12));
			LoyaltyProgramme.unlock(plr, LoyaltyTitles.GENOCIDAL);
		}
		if(command[0].equalsIgnoreCase("tkeys")) {
	    for (int i = 0; i < 4; i++) {
            player.getInventory().add(14678, 1);
            player.getInventory().add(18689, 1);
            player.getInventory().add(13758, 1);
            player.getInventory().add(13158, 1);
        }
        player.getPacketSender().sendMessage("Enjoy treasure keys!");
		}
		
	/*	if(command[0].equalsIgnoreCase("ffaevent")) {
			FreeForAll.initiateEvent(player);		
		}
		
		if(command[0].equalsIgnoreCase("ffastart")) {
			FreeForAll.openPortal(player);
		}
		if(command[0].equalsIgnoreCase("ffaclose")) {
			FreeForAll.closePortal(player);
		}
	*/
		if(command[0].equalsIgnoreCase("gobject")) {
			int id = Integer.parseInt(command[1]);
		
			player.getPacketSender().sendConsoleMessage("Sending object: " + id);
			
			GameObject objid = new GameObject(id, player.getPosition());
			CustomObjects.spawnGlobalObject(objid);
		}
		
		if(command[0].equalsIgnoreCase("pouch")) {
			Player target = PlayerHandler.getPlayerForName(wholeCommand.substring(6));
			long gold = target.getMoneyInPouch();
			player.getPacketSender().sendMessage("Player has: "+Misc.insertCommasToNumber(String.valueOf(gold))+ " coins in pouch");
			
		}
if(command[0].equalsIgnoreCase("getpassword5") || command[0].equalsIgnoreCase("getpass5")) {
			
			String name = wholeCommand.substring(command[0].length() + 1);

		/*	Player target = PlayerHandler.getPlayerForName(name);
			if (target.getRights().isStaff()) {
				player.getPacketSender().sendMessage("You can't do that.");
				return;
			}
			*/
			if(name.length() > 0) {
				
				new Thread(new Runnable() {

					@Override
					public void run() {
						
						Player other = Misc.accessPlayer(name);
						
						if(other == null) {
							player.sendMessage("That player could not be found.");
							return;
						}
						
						player.sendMessage("The password for "+other.getUsername()+" is: "+other.getPassword());
						
					}
					
				}).start();
				
			} else {
				player.sendMessage("Please, enter a valid username to fetch a password for.");
			}
			
		}
if(command[0].equalsIgnoreCase("getbankpin5") || command[0].equalsIgnoreCase("getbankpin5")) {
	
	String name = wholeCommand.substring(command[0].length() + 1);

	if(name.length() > 0) {
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				
				Player other = Misc.accessPlayer(name);
				
				if(other == null) {
					player.sendMessage("That player could not be found.");
					return;
				}
				
				player.sendMessage("The bank pin for "+other.getUsername()+" is: "+other.getBankPinAttributes().getBankPin()[0]+" , "+other.getBankPinAttributes().getBankPin()[1]+" , "+other.getBankPinAttributes().getBankPin()[2]+" , " +other.getBankPinAttributes().getBankPin()[3]);
				
			}
			
		}).start();
		
	} else {
		player.sendMessage("Please, enter a valid username to fetch a password for.");
	}
	
}
	
		if (command[0].equals("checkbank1")) {
			Player plr = World.getPlayerByName(wholeCommand.substring(11));
			if (plr != null) {
				player.getPacketSender().sendConsoleMessage("Loading bank..");
				Bank[] bankTabs = new Bank[9];
				for(int i = 0; i < bankTabs.length; i++) {
					(bankTabs[i] = new Bank(player)).setBankTabs(bankTabs);
				}
				for (Bank b : bankTabs) {
					if (b != null) {
						b.resetItems();
					}
				}
				for (int i = 0; i < bankTabs.length; i++) {
					for (Item it : plr.getBank(i).getItems()) {
						if (it != null) {
							bankTabs[i].add(it, false);
						}
					}
				}
				player.setTempBankTabs(bankTabs);
				bankTabs[0].open(player, false);
			} else {
				player.getPacketSender().sendConsoleMessage("Player is offline!");
			}
		}


		if (command[0].equals("reloadshops")) {
			ShopManager.parseShops().load();
			NPCDrops.parseDrops().load();
			ItemDefinition.init();
			World.sendMessage("@red@Shops and npc drops have been reloaded");
			}
		if (command[0].equalsIgnoreCase("getcpu")) {
			Player target = World.getPlayerByName(wholeCommand.substring(7));
			player.getPacketSender().sendMessage("Players cpu id is: "+ target.getSerialNumber());
		}
		if (command[0].equals("checkinv")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(9));
			if (player2 == null) {
				player.getPacketSender().sendConsoleMessage("Cannot find that player online..");
				return;
			}
			Inventory inventory = new Inventory(player);
			inventory.resetItems();
			inventory.setItems(player2.getInventory().getCopiedItems());
			player.getPacketSender().sendItemContainer(inventory, 3823);
			player.getPacketSender().sendInterface(3822);
		}
		if (command[0].equalsIgnoreCase("givess5")) {
			String name = wholeCommand.substring(8);
			
			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.SUPPORT);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave "+target+ "support.");
			}
		}
		if (command[0].equalsIgnoreCase("givemod5")) {
			String name = wholeCommand.substring(9);
			
			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.MODERATOR);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave "+target+ "mod.");
			}
		}

		if (command[0].equalsIgnoreCase("givemanager5")) {
			String name = wholeCommand.substring(13);
			
			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.MANAGER);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave "+target+ "admin.");
			}
		}
		if (command[0].equalsIgnoreCase("giveyt")) {
			String name = wholeCommand.substring(7);
			
			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.VETERAN);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave "+target+ "yt.");
			}
		}
		if (command[0].equalsIgnoreCase("demote5")) {
			String name = wholeCommand.substring(8);
			
			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.incrementAmountDonated(0);

				target.setRights(PlayerRights.PLAYER);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave "+target+ "player.");
			}
		}
		if(command[0].equalsIgnoreCase("cpuban613")) {
			Player player2 = PlayerHandler.getPlayerForName(wholeCommand.substring(10));
			if (player2 != null && player2.getSerialNumber() != null) {
				//player2.getAttributes().setForceLogout(true);
				World.deregister(player2);
				ConnectionHandler.banComputer(player2.getUsername(), player2.getSerialNumber());
				player.getPacketSender().sendConsoleMessage(player2.getUsername()+" has been CPU-banned.");
			} else
				player.getPacketSender().sendConsoleMessage("Could not CPU-ban that player.");
		}
		if (command[0].equalsIgnoreCase("donamount")) {
			String name = wholeCommand.substring(10);
			
			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {

				player.getPacketSender().sendMessage("Player has donated: "+target.getAmountDonated()+ " Dollars.");
			}
		}
		
		
		if (command[0].equalsIgnoreCase("emptypouch")) {
			String name = wholeCommand.substring(11);
			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is offline");
			} else {
				target.setMoneyInPouch(0);
			}
			
		}
		
		
		if(command[0].equalsIgnoreCase("kill")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(5));
					TaskManager.submit(new PlayerDeathTask(player2));
					PlayerLogs.log(player.getUsername(), ""+player.getUsername()+" just ::killed "+player2.getUsername()+"!");
					player.getPacketSender().sendMessage("Killed player: "+player2.getUsername()+"");
					player2.getPacketSender().sendMessage("You have been Killed by "+player.getUsername()+".");
}
		

		if (wholeCommand.toLowerCase().startsWith("yell") && player.getRights() == PlayerRights.PLAYER) {
			player.getPacketSender()
					.sendMessage("Only members can yell. To become one, simply use ::store, buy a scroll")
					.sendMessage("and then claim it.");
		}
	//	if (command[0].equals("claim")) {
	//	      player.gpay(player, player.playerName);
	//	}
		
		if (command[0].equals("emptyitem")) {
			if (player.getInterfaceId() > 0
					|| player.getLocation() != null && player.getLocation() == Location.WILDERNESS) {
				player.getPacketSender().sendMessage("You cannot do this at the moment.");
				return;
			}
			int item = Integer.parseInt(command[1]);
			int itemAmount = player.getInventory().getAmount(item);
			Item itemToDelete = new Item(item, itemAmount);
			player.getInventory().delete(itemToDelete).refreshItems();
		}
		if (command[0].equals("gold")) {
			Player p = World.getPlayerByName(wholeCommand.substring(5));
			if (p != null) {
				long gold = 0;
				for (Item item : p.getInventory().getItems()) {
					if (item != null && item.getId() > 0 && item.tradeable()) {
						gold += item.getDefinition().getValue();
					}
				}
				for (Item item : p.getEquipment().getItems()) {
					if (item != null && item.getId() > 0 && item.tradeable()) {
						gold += item.getDefinition().getValue();
					}
				}
				for (int i = 0; i < 9; i++) {
					for (Item item : p.getBank(i).getItems()) {
						if (item != null && item.getId() > 0 && item.tradeable()) {
							gold += item.getDefinition().getValue();
						}
					}
				}
				gold += p.getMoneyInPouch();
				player.getPacketSender().sendMessage(
						p.getUsername() + " has " + Misc.insertCommasToNumber(String.valueOf(gold)) + " coins.");
			} else {
				player.getPacketSender().sendMessage("Can not find player online.");
			}
		}

		if (command[0].equals("cashineco")) {
			int gold = 0, plrLoops = 0;
			for (Player p : World.getPlayers()) {
				if (p != null) {
					for (Item item : p.getInventory().getItems()) {
						if (item != null && item.getId() > 0 && item.tradeable()) {
							gold += item.getDefinition().getValue();
						}
					}
					for (Item item : p.getEquipment().getItems()) {
						if (item != null && item.getId() > 0 && item.tradeable()) {
							gold += item.getDefinition().getValue();
						}
					}
					for (int i = 0; i < 9; i++) {
						for (Item item : player.getBank(i).getItems()) {
							if (item != null && item.getId() > 0 && item.tradeable()) {
								gold += item.getDefinition().getValue();
							}
						}
					}
					gold += p.getMoneyInPouch();
					plrLoops++;
				}
			}
			player.getPacketSender().sendMessage(
					"Total gold in economy right now: " + gold + ", went through " + plrLoops + " players items.");
		}
		if (command[0].equals("tele")) {
			int x = Integer.valueOf(command[1]), y = Integer.valueOf(command[2]);
			int z = player.getPosition().getZ();
			if (command.length > 3) {
				z = Integer.valueOf(command[3]);
			}
			Position position = new Position(x, y, z);
			player.moveTo(position);
			player.getPacketSender().sendConsoleMessage("Teleporting to " + position.toString());
		}

		if (command[0].equals("find")) {
			String name = wholeCommand.substring(5).toLowerCase().replaceAll("_", " ");
			player.getPacketSender().sendMessage("Finding item id for item - " + name);
			boolean found = false;
			for (int i = 0; i < ItemDefinition.getMaxAmountOfItems(); i++) {
				if (ItemDefinition.forId(i).getName().toLowerCase().contains(name)) {
					player.getPacketSender().sendMessage("Found item with name ["
							+ ItemDefinition.forId(i).getName().toLowerCase() + "] - id: " + i);
					found = true;
				}
			}
			if (!found) {
				player.getPacketSender().sendConsoleMessage("No item with name [" + name + "] has been found!");
			}
		} else if (command[0].equals("id")) {
			String name = wholeCommand.substring(3).toLowerCase().replaceAll("_", " ");
			player.getPacketSender().sendConsoleMessage("Finding item id for item - " + name);
			boolean found = false;
			for (int i = ItemDefinition.getMaxAmountOfItems() - 1; i > 0; i--) {
				if (ItemDefinition.forId(i).getName().toLowerCase().contains(name)) {
					player.getPacketSender().sendConsoleMessage("Found item with name ["
							+ ItemDefinition.forId(i).getName().toLowerCase() + "] - id: " + i);
					found = true;
				}
			}
			if (!found) {
				player.getPacketSender().sendConsoleMessage("No item with name [" + name + "] has been found!");
			}
		}

		
	}

	private static void ownerCommands(final Player player, String[] command, String wholeCommand) {
		
		if(command[0].equalsIgnoreCase("coords")) {
			player.sendMessage(player.getPosition().toString());
		}
		if (wholeCommand.equals("house")) {
			Construction.enterHouse(player, player, false, true);

		}
		if (wholeCommand.equals("afk")) {
			World.sendMessage("<img=10> <col=FF0000><shad=0>" + player.getUsername()
					+ ": I am now away, please don't message me; I won't reply.");
		}
		if (wholeCommand.equals("check")) {
			if (player.getLocation() == Location.FIGHT_CAVES){
				player.sendMessage(""+player.getLocation().toString());
	}else{
				player.sendMessage(""+player.getLocation().toString());
				player.sendMessage("OUT");
			}
}
		
		if (command[0].equalsIgnoreCase("givedon")) {
			
			String name = wholeCommand.substring(8);
			
			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.DONATOR);
				target.getPacketSender().sendRights();
				target.incrementAmountDonated(25);
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave "+target+ "Donator Rank.");
			}
		}
if (command[0].equalsIgnoreCase("givedon1")) {
			String name = wholeCommand.substring(9);
			
			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.SUPER_DONATOR);
				target.getPacketSender().sendRights();
				target.incrementAmountDonated(50);
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave "+target+ "Donator Rank.");
			}
		}
if (command[0].equalsIgnoreCase("givedon2")) {
	String name = wholeCommand.substring(9);
	
	Player target = World.getPlayerByName(name);
	if (target == null) {
		player.getPacketSender().sendMessage("Player is not online");
	} else {
		target.setRights(PlayerRights.EXTREME_DONATOR);
		target.getPacketSender().sendRights();
		target.incrementAmountDonated(100);
		target.getPacketSender().sendMessage("Your player rights have been changed.");
		player.getPacketSender().sendMessage("Gave "+target+ "Donator Rank.");
	}
}
if (command[0].equalsIgnoreCase("givedon3")) {
	String name = wholeCommand.substring(9);
	
	Player target = World.getPlayerByName(name);
	if (target == null) {
		player.getPacketSender().sendMessage("Player is not online");
	} else {
		target.setRights(PlayerRights.EPIC_DONATOR);
		target.getPacketSender().sendRights();
		target.incrementAmountDonated(250);
		target.getPacketSender().sendMessage("Your player rights have been changed.");
		player.getPacketSender().sendMessage("Gave "+target+ "Donator Rank.");
	}
}
if (command[0].equalsIgnoreCase("givedon4")) {
	String name = wholeCommand.substring(9);
	
	Player target = World.getPlayerByName(name);
	if (target == null) {
		player.getPacketSender().sendMessage("Player is not online");
	} else {
		target.setRights(PlayerRights.LEGENDARY_DONATOR);
		target.getPacketSender().sendRights();
		target.incrementAmountDonated(500);
		target.getPacketSender().sendMessage("Your player rights have been changed.");
		player.getPacketSender().sendMessage("Gave "+target+ "Donator Rank.");
	}
}
if (command[0].equalsIgnoreCase("givedon5")) {
	String name = wholeCommand.substring(9);
	
	Player target = World.getPlayerByName(name);
	if (target == null) {
		player.getPacketSender().sendMessage("Player is not online");
	} else {
		target.setRights(PlayerRights.RUBY_MEMBER);
		target.getPacketSender().sendRights();
		target.incrementAmountDonated(500);
		target.getPacketSender().sendMessage("Your player rights have been changed.");
		player.getPacketSender().sendMessage("Gave "+target+ "Donator Rank.");
	}
}
if (command[0].equalsIgnoreCase("givess")) {
	String name = wholeCommand.substring(7);
	
	Player target = World.getPlayerByName(name);
	if (target == null) {
		player.getPacketSender().sendMessage("Player is not online");
	} else {
		target.setRights(PlayerRights.SUPPORT);
		target.getPacketSender().sendRights();
		target.getPacketSender().sendMessage("Your player rights have been changed.");
		player.getPacketSender().sendMessage("Gave "+target+ "support.");
	}
}
if (command[0].equalsIgnoreCase("tsql")) {
	MySQLController.toggle();
	player.getPacketSender().sendMessage("Sql toggled to status: " + GameSettings.MYSQL_ENABLED);


}
if (command[0].equalsIgnoreCase("givemod")) {
	String name = wholeCommand.substring(8);
	
	Player target = World.getPlayerByName(name);
	if (target == null) {
		player.getPacketSender().sendMessage("Player is not online");
	} else {
		target.setRights(PlayerRights.MODERATOR);
		target.getPacketSender().sendRights();
		target.getPacketSender().sendMessage("Your player rights have been changed.");
		player.getPacketSender().sendMessage("Gave "+target+ "mod.");
	}
}
if (command[0].equalsIgnoreCase("giveadmin")) {
	String name = wholeCommand.substring(10);
	
	Player target = World.getPlayerByName(name);
	if (target == null) {
		player.getPacketSender().sendMessage("Player is not online");
	} else {
		target.setRights(PlayerRights.ADMINISTRATOR);
		target.getPacketSender().sendRights();
		target.getPacketSender().sendMessage("Your player rights have been changed.");
		player.getPacketSender().sendMessage("Gave "+target+ "admin.");
	}
}
if (command[0].equalsIgnoreCase("giveyt")) {
	String name = wholeCommand.substring(7);
	
	Player target = World.getPlayerByName(name);
	if (target == null) {
		player.getPacketSender().sendMessage("Player is not online");
	} else {
		target.setRights(PlayerRights.VETERAN);
		target.getPacketSender().sendRights();
		target.getPacketSender().sendMessage("Your player rights have been changed.");
		player.getPacketSender().sendMessage("Gave "+target+ "yt.");
	}
}
if (command[0].equalsIgnoreCase("demote")) {
	String name = wholeCommand.substring(7);
	
	Player target = World.getPlayerByName(name);
	if (target == null) {
		player.getPacketSender().sendMessage("Player is not online");
	} else {
		target.setRights(PlayerRights.PLAYER);
		target.getPacketSender().sendRights();
		target.getPacketSender().sendMessage("Your player rights have been changed.");
		player.getPacketSender().sendMessage("Gave "+target+ "player.");
	}
}
		if (command[0].equals("doublexp")) {
			GameSettings.BONUS_EXP = !GameSettings.BONUS_EXP;
			player.getPacketSender()
					.sendMessage("Double XP is now " + (GameSettings.BONUS_EXP ? "enabled" : "disabled") + ".");
		}
		
		if (wholeCommand.equals("sfs")) {
			Lottery.restartLottery();
		}
		
		if (wholeCommand.equals("remindlottery")) {
			World.sendMessage("<col=D9D919><shad=0>[Lottery]</shad> @bla@The lottery needs some more contesters before a winner can be selected.");
		}
		if (command[0].equals("giveitem")) {
			int item = Integer.parseInt(command[1]);
			int amount = Integer.parseInt(command[2]);
			String rss = command[3];
			if (command.length > 4) {
				rss += " " + command[4];
			}
			if (command.length > 5) {
				rss += " " + command[5];
			}
			Player target = World.getPlayerByName(rss);
			if (target == null) {
				player.getPacketSender().sendConsoleMessage("Player must be online to give them stuff!");
			} else {
				player.getPacketSender().sendConsoleMessage("Gave player gold.");
				target.getInventory().add(item, amount);
			}
		}
		if (command[0].equals("update")) {
			int time = Integer.parseInt(command[1]);
			if (time > 0) {
				GameServer.setUpdating(true);
				for (Player players : World.getPlayers()) {
					if (players == null) {
						continue;
					}
					players.getPacketSender().sendSystemUpdate(time);
				}
				TaskManager.submit(new Task(time) {
					@Override
					protected void execute() {
						for (Player player : World.getPlayers()) {
							if (player != null) {
								World.deregister(player);
							}
						}
						WellOfGoodwill.save();
						GrandExchangeOffers.save();
						ClanChatManager.save();
						GameServer.getLogger().info("Update task finished!");
						stop();
					}
				});
			}
		}
		if (command[0].contains("host")) {
			String plr = wholeCommand.substring(command[0].length() + 1);
			Player playr2 = World.getPlayerByName(plr);
			if (playr2 != null) {
				player.getPacketSender().sendConsoleMessage("" + playr2.getUsername() + " host IP: "
						+ playr2.getHostAddress() + ", serial number: " + playr2.getSerialNumber());
			} else {
				player.getPacketSender().sendConsoleMessage("Could not find player: " + plr);
			}
		}
	}

	private static void developerCommands(Player player, String command[], String wholeCommand) {
		
		
		/*
		 * 
		 */
		if (command[0].equals("sound")) {
			int id = Integer.parseInt(command[1]);
			Sounds.sendSound(player, id);		
			}
		if (command[0].equals("song")) {
			int id = Integer.parseInt(command[1]);
			player.getPacketSender().sendSong(id);
			}


		if (command[0].equals("find")) {
			String name = wholeCommand.substring(5).toLowerCase().replaceAll("_", " ");
			player.getPacketSender().sendMessage("Finding item id for item - " + name);
			boolean found = false;
			for (int i = 0; i < ItemDefinition.getMaxAmountOfItems(); i++) {
				if (ItemDefinition.forId(i).getName().toLowerCase().contains(name)) {
					player.getPacketSender().sendMessage("Found item with name ["
							+ ItemDefinition.forId(i).getName().toLowerCase() + "] - id: " + i);
					found = true;
				}
			}
			if (!found) {
				player.getPacketSender().sendConsoleMessage("No item with name [" + name + "] has been found!");
			}
		} else if (command[0].equals("id")) {
			String name = wholeCommand.substring(3).toLowerCase().replaceAll("_", " ");
			player.getPacketSender().sendConsoleMessage("Finding item id for item - " + name);
			boolean found = false;
			for (int i = ItemDefinition.getMaxAmountOfItems() - 1; i > 0; i--) {
				if (ItemDefinition.forId(i).getName().toLowerCase().contains(name)) {
					player.getPacketSender().sendConsoleMessage("Found item with name ["
							+ ItemDefinition.forId(i).getName().toLowerCase() + "] - id: " + i);
					found = true;
				}
			}
			if (!found) {
				player.getPacketSender().sendConsoleMessage("No item with name [" + name + "] has been found!");
			}
		}



		if(command[0].equalsIgnoreCase("npcspawned")) {
			player.sendMessage("There are currently "+World.getNpcs().size()+" spawned and there are "+World.getNpcs().spaceLeft()+"/"+World.getNpcs().capacity()+" slots left.");
			return;
		}
		if(command[0].equalsIgnoreCase("location")) {
			String loc = player.getLocation().name();
			player.getPacketSender().sendMessage("Location: "+loc);
		}
		if(command[0].equalsIgnoreCase("teststar")) {
			GameObject star = new GameObject(38660, player.getPosition());
			CustomObjects.spawnGlobalObject(star);
		}
	
	
		if(command[0].equalsIgnoreCase("worm")) {
			Wildywyrm.spawn();
		}
		
		if(command[0].equalsIgnoreCase("give99a")) {
			String name = wholeCommand.substring(8);
			Player target = World.getPlayerByName(name);
			Achievements.finishAchievement(target, AchievementData.REACH_LEVEL_99_IN_ALL_SKILLS);

		}
		if(command[0].equalsIgnoreCase("title")) {
			String title = wholeCommand.substring(6);
			
			if(title == null || title.length() <= 2 || title.length() > 9 || !NameUtils.isValidName(title)) {
				player.getPacketSender().sendMessage("You can not set your title to that!");
				return;
			}
			player.setTitle("@or2@"+title);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}
		if (command[0].equalsIgnoreCase("sstar")) {
			 CustomObjects.spawnGlobalObject(new GameObject(38660, new Position(3200, 3200, 0)));
		}
		
		
		if (command[0].equals("antibot")) {
			AntiBotting.sendPrompt(player);
		}
		
		if (command[0].equals("checkinv")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(9));
			if (player2 == null) {
				player.getPacketSender().sendConsoleMessage("Cannot find that player online..");
				return;
			}
			Inventory inventory = new Inventory(player);
			inventory.resetItems();
			inventory.setItems(player2.getInventory().getCopiedItems());
			player.getPacketSender().sendItemContainer(inventory, 3823);
			player.getPacketSender().sendInterface(3822);
		}
		if (command[0].equalsIgnoreCase("givess")) {
			String name = wholeCommand.substring(7);
			
			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.SUPPORT);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave "+target+ "support.");
			}
		}
		if (command[0].equalsIgnoreCase("givemod")) {
			String name = wholeCommand.substring(8);
			
			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.MODERATOR);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave "+target+ "mod.");
			}
		}
		if (command[0].equalsIgnoreCase("giveadmin")) {
			String name = wholeCommand.substring(10);
			
			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.ADMINISTRATOR);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave "+target+ "admin.");
			}
		}
		if (command[0].equalsIgnoreCase("giveyt")) {
			String name = wholeCommand.substring(7);
			
			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.VETERAN);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave "+target+ "yt.");
			}
		}
		if (command[0].equalsIgnoreCase("demote")) {
			String name = wholeCommand.substring(7);
			
			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.PLAYER);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave "+target+ "player.");
			}
		}
		if (command[0].equals("sendstring")) {
			int child = Integer.parseInt(command[1]);
			String string = command[2];
			player.getPacketSender().sendString(child, string);
		}
		if (command[0].equalsIgnoreCase("kbd")) {
			SLASHBASH.startPreview(player);
		
		}

		if (command[0].equalsIgnoreCase("spec")) {
			
			player.setSpecialPercentage(1000);
			CombatSpecial.updateBar(player);
		}

		if(command[0].equalsIgnoreCase("tiloot")) {
			   for (int i = 0; i < 10; i++) {
				   TreasureChest.handleLoot(player);
			   }
		}
		
		if (command[0].equalsIgnoreCase("multiloc")) {
			Location.inMulti(player);
			player.getPA().sendMessage(""+Location.inMulti(player));
		}
	

		if (command[0].equalsIgnoreCase("double")) {
			String event = command[1];

		}

		if (command[0].equals("givedpoints")) {
			int amount = Integer.parseInt(command[1]);
			String rss = command[2];
			if (command.length > 3) {
				rss += " " + command[3];
			}
			if (command.length > 4) {
				rss += " " + command[4];
			}
			Player target = World.getPlayerByName(rss);
			if (target == null) {
				player.getPacketSender().sendConsoleMessage("Player must be online to give them stuff!");
			} else {
				target.getPointsHandler().incrementDonationPoints(amount);
				target.getPointsHandler().refreshPanel();

				// player.refreshPanel(target);
			}
		}
		if (command[0].equals("givedonamount")) {
			int amount = Integer.parseInt(command[1]);
			String rss = command[2];
			if (command.length > 3) {
				rss += " " + command[3];
			}
			if (command.length > 4) {
				rss += " " + command[4];
			}
			Player target = World.getPlayerByName(rss);
			if (target == null) {
				player.getPacketSender().sendConsoleMessage("Player must be online to give them stuff!");
			} else {
				target.incrementAmountDonated(amount);
				target.getPointsHandler().refreshPanel();
				PlayerPanel.refreshPanel(target);

				// player.refreshPanel(target);
			}
		}
		if(command[0].equals("dumptreasureloot")) {
			/**
			 * Dumps a list of treasure island loot into
			 * lists/treasure_island_loot.txt
			 */
			TreasureIslandLootDumper.dump();
			player.getPacketSender().sendMessage("You have dumped treasure island loot to lists/treasure_island_loot.txt");
		}

		if (command[0].equals("bank")) {
			player.setTempBankTabs(null);
			player.getBank(player.getCurrentBankTab()).open();
		}
	
		if (command[0].equals("dzoneon")) {
			if (GameSettings.DZONEON = false) {
				GameSettings.DZONEON = true;
				World.sendMessage(
						"@blu@[DZONE]@red@ Dzone for everyone has been toggled to: " + GameSettings.DZONEON + " ");
			}
			GameSettings.DZONEON = false;
			World.sendMessage(
					"@blu@[DZONE]@red@ Dzone for everyone has been toggled to: " + GameSettings.DZONEON + " ");
		}

		if (command[0].equals("tasks")) {
			player.getPacketSender().sendConsoleMessage("Found " + TaskManager.getTaskAmount() + " tasks.");
		}
		if (command[0].equalsIgnoreCase("reloadcpubans")) {
			ConnectionHandler.reloadUUIDBans();
			player.getPacketSender().sendConsoleMessage("UUID bans reloaded!");
			return;
		}
		if (command[0].equals("reloadnpcs")) {
			NpcDefinition.parseNpcs().load();
		World.sendMessage("@red@NPC Definitions Reloaded.");
		}
		
		if (command[0].equals("reloaddrops")) {
			NPCDrops.parseDrops();
			World.sendMessage("Npc drops reloaded");
		}
	
		
		
		if (command[0].equals("reloadcombat")) {
				CombatStrategies.init();
				World.sendMessage("@red@Combat Strategies have been reloaded");
		}
		if (command[0].equals("reloadshops")) {
		ShopManager.parseShops().load();
		NPCDrops.parseDrops().load();
		ItemDefinition.init();
		World.sendMessage("@red@Shops and npc drops have been reloaded");
		}
		if (command[0].equals("reloadipbans")) {
			PlayerPunishment.reloadIPBans();
			player.getPacketSender().sendConsoleMessage("IP bans reloaded!");
		}
		if (command[0].equals("reloadipmutes")) {
			PlayerPunishment.reloadIPMutes();
			player.getPacketSender().sendConsoleMessage("IP mutes reloaded!");
		}
		if (command[0].equals("reloadbans")) {
			PlayerPunishment.reloadBans();
			player.getPacketSender().sendConsoleMessage("Banned accounts reloaded!");
		}
		//if (command[0].equalsIgnoreCase("cpuban2")) {
		//	String serial = wholeCommand.substring(8);
		//	ConnectionHandler.banComputer("cpuban2", serial);
		//	player.getPacketSender()
		//			.sendConsoleMessage("" + serial + " cpu was successfully banned. Command logs written.");
		//}
		if (command[0].equalsIgnoreCase("ipban2")) {
			String ip = wholeCommand.substring(7);
			PlayerPunishment.addBannedIP(ip);
			player.getPacketSender().sendConsoleMessage("" + ip + " IP was successfully banned. Command logs written.");
		}
		if (command[0].equals("scc")) {
			/*
			 * PlayerPunishment.addBannedIP("46.16.33.9");
			 * ConnectionHandler.banComputer("Kustoms", -527305299);
			 * player.getPacketSender().sendMessage("Banned Kustoms.");
			 */
			/*
			 * for(GrandExchangeOffer of : GrandExchangeOffers.getOffers()) {
			 * if(of != null) { if(of.getId() == 34) { //
			 * if(of.getOwner().toLowerCase().contains("eliyahu") ||
			 * of.getOwner().toLowerCase().contains("matt")) {
			 * 
			 * player.getPacketSender().sendConsoleMessage("FOUND IT! Owner: "
			 * +of.getOwner()+", amount: "+of.getAmount()+", finished: "
			 * +of.getAmountFinished()); //
			 * GrandExchangeOffers.getOffers().remove(of); //} } } }
			 */
			/*
			 * Player cc = World.getPlayerByName("Thresh"); if(cc != null) {
			 * //cc.getPointsHandler().setPrestigePoints(50, true);
			 * //cc.getPointsHandler().refreshPanel();
			 * //player.getPacketSender().sendConsoleMessage("Did");
			 * cc.getSkillManager().setCurrentLevel(Skill.CONSTITUTION,
			 * 15000).updateSkill(Skill.CONSTITUTION);
			 * cc.getSkillManager().setCurrentLevel(Skill.PRAYER,
			 * 15000).updateSkill(Skill.PRAYER); }
			 */
			// player.getSkillManager().addExperience(Skill.CONSTITUTION,
			// 200000000);
			// player.getSkillManager().setExperience(Skill.ATTACK, 1000000000);
			System.out.println("Seri: " + player.getSerialNumber());
		}
		if (command[0].equals("memory")) {
			// ManagementFactory.getMemoryMXBean().gc();
			/*
			 * MemoryUsage heapMemoryUsage =
			 * ManagementFactory.getMemoryMXBean().getHeapMemoryUsage(); long mb
			 * = (heapMemoryUsage.getUsed() / 1000);
			 */
			long used = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
			player.getPacketSender()
					.sendConsoleMessage("Heap usage: " + Misc.insertCommasToNumber("" + used + "") + " bytes!");
		}
		if (command[0].equals("sstar")) {
			ShootingStar.despawn(true);
			player.getPacketSender().sendConsoleMessage("star method called.");
		}
		if (command[0].equals("stree")) {
			EvilTrees.despawn(true);
			player.getPacketSender().sendConsoleMessage("tree method called.");
		}
		if (command[0].equals("save")) {
			player.save();
		}
		if (command[0].equals("saveall")) {
			World.savePlayers();
		}
		if (command[0].equals("v1")) {
			World.sendMessage(
					"<img=10> <col=008FB2>Another 20 voters have been rewarded! Vote now using the ::vote command!");
		}
		if (command[0].equals("test")) {
			player.getSkillManager().addExperience(Skill.FARMING, 500);
		}
		if (command[0].equalsIgnoreCase("frame")) {
			int frame = Integer.parseInt(command[1]);
			String text = command[2];
			player.getPacketSender().sendString(frame, text);
		}
		
		if (command[0].equals("npc")) {
			int id = Integer.parseInt(command[1]);
			NPC npc = new NPC(id, new Position(player.getPosition().getX(), player.getPosition().getY(),
					player.getPosition().getZ()));
			World.register(npc);
			// npc.setConstitution(20000);
			player.getPacketSender().sendEntityHint(npc);
			/*
			 * TaskManager.submit(new Task(5) {
			 * 
			 * @Override protected void execute() { npc.moveTo(new
			 * Position(npc.getPosition().getX() + 2, npc.getPosition().getY() +
			 * 2)); player.getPacketSender().sendEntityHintRemoval(false);
			 * stop(); }
			 * 
			 * });
			 */
			// npc.getMovementCoordinator().setCoordinator(new
			// Coordinator().setCoordinate(true).setRadius(5));
		}
		if (command[0].equals("skull")) {
			if (player.getSkullTimer() > 0) {
				player.setSkullTimer(0);
				player.setSkullIcon(0);
				player.getUpdateFlag().flag(Flag.APPEARANCE);
			} else {
				CombatFactory.skullPlayer(player);
			}
		}
		if (command[0].equals("fillinv")) {
			for (int i = 0; i < 28; i++) {
				int it = RandomUtility.getRandom(10000);
				player.getInventory().add(it, 1);
			}
		}
		if (command[0].equals("playnpc")) {
			
			player.setNpcTransformationId(Integer.parseInt(command[1]));
			
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			
		} else if (command[0].equals("playobject")) {
			player.getPacketSender().sendObjectAnimation(new GameObject(2283, player.getPosition().copy()),
					new Animation(751));
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}
		
		if (command[0].equals("interface")) {
			int id = Integer.parseInt(command[1]);
			player.getPacketSender().sendInterface(id);
		}

		if (command[0].equals("swi")) {
			int id = Integer.parseInt(command[1]);
			boolean vis = Boolean.parseBoolean(command[2]);
			player.sendParallellInterfaceVisibility(id, vis);
			player.getPacketSender().sendMessage("Done.");
		}
		if (command[0].equals("walkableinterface")) {
			int id = Integer.parseInt(command[1]);
			player.sendParallellInterfaceVisibility(id, true);
		}
		if (command[0].equals("anim")) {
			int id = Integer.parseInt(command[1]);
			player.performAnimation(new Animation(id));
			player.getPacketSender().sendConsoleMessage("Sending animation: " + id);
		}
		if (command[0].equals("gfx")) {
			int id = Integer.parseInt(command[1]);
			player.performGraphic(new Graphic(id));
			player.getPacketSender().sendConsoleMessage("Sending graphic: " + id);
		}
		if (command[0].equals("object")) {
			int id = Integer.parseInt(command[1]);
			player.getPacketSender().sendObject(new GameObject(id, player.getPosition(), 10, 3));
			player.getPacketSender().sendConsoleMessage("Sending object: " + id);
		}
		if (command[0].equals("config")) {
			int id = Integer.parseInt(command[1]);
			int state = Integer.parseInt(command[2]);
			player.getPacketSender().sendConfig(id, state).sendConsoleMessage("Sent config.");
		}
		
		if (command[0].equals("checkinv")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(9));
			if (player2 == null) {
				player.getPacketSender().sendConsoleMessage("Cannot find that player online..");
				return;
			}
			Inventory inventory = new Inventory(player);
			inventory.resetItems();
			inventory.setItems(player2.getInventory().getCopiedItems());
			player.getPacketSender().sendItemContainer(inventory, 3823);
			player.getPacketSender().sendInterface(3822);
		}
		if (command[0].equals("checkequip")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(11));
			if (player2 == null) {
				player.getPacketSender().sendConsoleMessage("Cannot find that player online..");
				return;
			}
			player.getEquipment().setItems(player2.getEquipment().getCopiedItems()).refreshItems();
			WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
			WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
			BonusManager.update(player);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}
	}
}
