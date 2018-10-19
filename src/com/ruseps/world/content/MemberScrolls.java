package com.ruseps.world.content;

import com.ruseps.model.Animation;
import com.ruseps.model.GameMode;
import com.ruseps.model.Graphic;
import com.ruseps.model.GraphicHeight;
import com.ruseps.model.PlayerRights;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.content.dialogue.Dialogue;
import com.ruseps.world.content.dialogue.DialogueExpression;
import com.ruseps.world.content.dialogue.DialogueManager;
import com.ruseps.world.content.dialogue.DialogueType;
import com.ruseps.world.entity.impl.player.Player;

public class MemberScrolls {
	
	public static void checkForRankUpdate(Player player) {
		if(player.getRights().isStaff()) {
			return;
		}
		if(player.getGameMode() == GameMode.IRONMAN) {
			player.getPacketSender().sendMessage("@red@You did not recieve donator rank because you are an iron man!");
			return;
		}
		if(player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
			player.getPacketSender().sendMessage("You did not recieve donator rank because you are an iron man!");
			return;
		}
		PlayerRights rights = null;
		if(player.getAmountDonated() >= 20)
			rights = PlayerRights.DONATOR;
		if(player.getAmountDonated() >= 50)
			rights = PlayerRights.SUPER_DONATOR;
		if(player.getAmountDonated() >= 100)
			rights = PlayerRights.EXTREME_DONATOR;
		if(player.getAmountDonated() >= 250)
			rights = PlayerRights.EPIC_DONATOR;
		if(player.getAmountDonated() >= 500)
			rights = PlayerRights.LEGENDARY_DONATOR;
     	if(player.getAmountDonated() >= 1000)
			rights = PlayerRights.RUBY_MEMBER;
		if(rights != null && rights != player.getRights()) {
			player.getPacketSender().sendMessage("You've become a "+Misc.formatText(rights.toString().toLowerCase())+"! Congratulations!");
			World.sendMessage(player+"has become a "+Misc.formatText(rights.toString().toLowerCase())+"! Congratulations!");
			player.setRights(rights);
			player.getPacketSender().sendRights();
		}
	}
	private static final Graphic gfx1 = new Graphic(199, 3, GraphicHeight.LOW);
	private static final Animation anim = new Animation(2107);
	private static final Graphic gfx2 = new Graphic(2128, 3, GraphicHeight.LOW);


	public static void giveWarning(Player player) {
		DialogueManager.start(player, 391);
		player.setDialogueActionId(391);
	}
	

	
	public static void handleScrollClaim(Player player) {
		player.getPacketSender().sendInterfaceRemoval();
			if(player.getScrollAmount() == 1) {
				player.getInventory().delete(6798, 1);
				player.getPointsHandler().incrementDonationPoints(1);
				player.incrementAmountDonated(1);
				player.getPointsHandler().refreshPanel();
                player.sendMessage("You now have 1 Donator points.");
                player.forceChat("I have recieved 1 Donator points by the blessed god!");
        		player.performGraphic(gfx1);
        		player.performAnimation(anim);
        		player.performGraphic(gfx2);

			//	player.increment
			//	player.getDonationPoints().setDonationPoints(10)
				player.getPacketSender().sendMessage("Your account has gained funds worth $1. Your total is now at $"+player.getAmountDonated()+".");
				checkForRankUpdate(player);
				PlayerPanel.refreshPanel(player);
		        World.sendAdminMessage("@red@[ADMIN]@bla@ " +player.getUsername()+ " Has just claimed donation");

			}
			if(player.getScrollAmount() == 2) {
				player.getInventory().delete(6799, 1);
				player.getPointsHandler().incrementDonationPoints(5);
				player.incrementAmountDonated(5);
				player.getPointsHandler().refreshPanel();
                player.sendMessage("You now have 5 Donator points.");
				player.getPacketSender().sendMessage("Your account has gained funds worth $5. Your total is now at $"+player.getAmountDonated()+".");
	        		player.performGraphic(gfx1);
	        		player.performAnimation(anim);
	        		player.performGraphic(gfx2);
				checkForRankUpdate(player);
				PlayerPanel.refreshPanel(player);
		        World.sendAdminMessage("@red@[ADMIN]@bla@ " +player.getUsername()+ " Has just claimed donation");

			}
			if(player.getScrollAmount() == 3) {
				player.getInventory().delete(6800, 1);
				player.getPointsHandler().incrementDonationPoints(10);
				player.incrementAmountDonated(10);
				player.getPointsHandler().refreshPanel();
                player.sendMessage("You now have 10 Donator points.");
				player.getPacketSender().sendMessage("Your account has gained funds worth $10. Your total is now at $"+player.getAmountDonated()+".");
	        		player.performGraphic(gfx1);
	        		player.performAnimation(anim);
	        		player.performGraphic(gfx2);

				checkForRankUpdate(player);
				PlayerPanel.refreshPanel(player);
		        World.sendAdminMessage("@red@[ADMIN]@bla@ " +player.getUsername()+ " Has just claimed donation");

			}
			if(player.getScrollAmount() == 4) {
				player.getInventory().delete(6801, 1);
				player.getPointsHandler().incrementDonationPoints(25);
				player.incrementAmountDonated(25);
				player.getPointsHandler().refreshPanel();
                player.sendMessage("You now have 25 Donator points.");
				player.getPacketSender().sendMessage("Your account has gained funds worth $25. Your total is now at $"+player.getAmountDonated()+".");
	    		player.performGraphic(gfx1);
        		player.performAnimation(anim);
        		player.performGraphic(gfx2);
				checkForRankUpdate(player);
				PlayerPanel.refreshPanel(player);
		        World.sendAdminMessage("@red@[ADMIN]@bla@ " +player.getUsername()+ " Has just claimed donation");

			}
			if(player.getScrollAmount() == 5) {
				player.getInventory().delete(6802, 1);
				player.getPointsHandler().incrementDonationPoints(50);
				player.incrementAmountDonated(50);
				player.getPointsHandler().refreshPanel();
                player.sendMessage("You now have 50 Donator points.");
				player.getPacketSender().sendMessage("Your account has gained funds worth $50. Your total is now at $"+player.getAmountDonated()+".");
	    		player.performGraphic(gfx1);
        		player.performAnimation(anim);
        		player.performGraphic(gfx2);
				checkForRankUpdate(player);
				PlayerPanel.refreshPanel(player);
		        World.sendAdminMessage("@red@[ADMIN]@bla@ " +player.getUsername()+ " Has just claimed donation");

			}
			if(player.getScrollAmount() == 6) {
				player.getInventory().delete(6803, 1);
				player.getPointsHandler().incrementDonationPoints(100);
				player.incrementAmountDonated(100);
				player.getPointsHandler().refreshPanel();
                player.sendMessage("You now have 100 Donator points.");
				player.getPacketSender().sendMessage("Your account has gained funds worth $75. Your total is now at $"+player.getAmountDonated()+".");
	    		player.performGraphic(gfx1);
        		player.performAnimation(anim);
        		player.performGraphic(gfx2);
				checkForRankUpdate(player);
				PlayerPanel.refreshPanel(player);
		        World.sendAdminMessage("@red@[ADMIN]@bla@ " +player.getUsername()+ " Has just claimed donation");

			}
			if(player.getScrollAmount() == 7) {
				player.getInventory().delete(6804, 1);
				player.getPointsHandler().incrementDonationPoints(150);
				player.incrementAmountDonated(150);
				player.getPointsHandler().refreshPanel();
                player.sendMessage("You now have 150 Donator points.");
				player.getPacketSender().sendMessage("Your account has gained funds worth $150. Your total is now at $"+player.getAmountDonated()+".");
	    		player.performGraphic(gfx1);
        		player.performAnimation(anim);
        		player.performGraphic(gfx2);
				checkForRankUpdate(player);
				PlayerPanel.refreshPanel(player);
		        World.sendAdminMessage("@red@[ADMIN]@bla@ " +player.getUsername()+ " Has just claimed donation");

			}
			if(player.getScrollAmount() == 8) {
				player.getInventory().delete(6805, 1);
				player.getPointsHandler().incrementDonationPoints(250);
				player.incrementAmountDonated(250);
				player.getPointsHandler().refreshPanel();
                player.sendMessage("You now have 250 Donator points.");
				player.getPacketSender().sendMessage("Your account has gained funds worth $250. Your total is now at $"+player.getAmountDonated()+".");
	    		player.performGraphic(gfx1);
        		player.performAnimation(anim);
        		player.performGraphic(gfx2);
				checkForRankUpdate(player);
				PlayerPanel.refreshPanel(player);
		        World.sendAdminMessage("@red@[ADMIN]@bla@ " +player.getUsername()+ " Has just claimed donation");

			}
			if(player.getScrollAmount() == 9) {
				player.getInventory().delete(6808, 1);
				player.getPointsHandler().incrementDonationPoints(10);
				player.incrementAmountDonated(10);
				player.getPointsHandler().refreshPanel();
                player.sendMessage("You now have 10 Donator points.");
				player.getPacketSender().sendMessage("Your account has gained funds worth $10. Your total is now at $"+player.getAmountDonated()+".");
	    		player.performGraphic(gfx1);
        		player.performAnimation(anim);
        		player.performGraphic(gfx2);
				checkForRankUpdate(player);
				PlayerPanel.refreshPanel(player);
		        World.sendAdminMessage("@red@[ADMIN]@bla@ " +player.getUsername()+ " Has just claimed donation");

			}
				
				
	}
	
	public static Dialogue getTotalFunds(final Player player) {
		return new Dialogue() {

			@Override
			public DialogueType type() {
				return DialogueType.NPC_STATEMENT;
			}

			@Override
			public DialogueExpression animation() {
				return DialogueExpression.NORMAL;
			}
			
			@Override
			public int npcId() {
				return 4657;
			}

			@Override
			public String[] dialogue() {
				return player.getAmountDonated() > 0 ? new String[]{"Your account has claimed scrolls worth $"+player.getAmountDonated()+" in total.", "Thank you for supporting us!"} : new String[]{"Your account has claimed scrolls worth $"+player.getAmountDonated()+" in total."};
			}
			
			@Override
			public Dialogue nextDialogue() {
				return DialogueManager.getDialogues().get(5);
			}
		};
	}
}
