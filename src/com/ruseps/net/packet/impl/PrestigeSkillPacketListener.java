package com.ruseps.net.packet.impl;

import com.ruseps.model.Animation;
import com.ruseps.model.Graphic;
import com.ruseps.model.Skill;
import com.ruseps.net.packet.Packet;
import com.ruseps.net.packet.PacketListener;
import com.ruseps.world.World;
import com.ruseps.world.entity.impl.player.Player;

/*
*author @live:nrpker7839
*/

public class PrestigeSkillPacketListener implements PacketListener {

	/*
	 * animation and gfx for after prestige level is reached.
	 */
	private static final Animation anim1 = new Animation(2107);
	private static final Graphic gfx1 = new Graphic(1834);

	@Override
	public void handleMessage(Player player, Packet packet) {
		int prestigeId = packet.readShort();

		Skill skill = Skill.forPrestigeId(prestigeId);
		// player.sendMessage("PREOIJDFO");
		if (skill == null) {
			return;
		}
		if (player.getInterfaceId() > 0) {
			player.getPacketSender().sendMessage("Please close all interfaces before doing this.");
			return;
		}
		if (player.getSkillManager().getTotalLevel() < 2475) {
			player.sendMessage("You need 99 in all skills before you can prestige");
		}
		player.getSkillManager().resetSkill(skill, true);

	}
}
		/*
		if (prestigeId == 1) {
			// copy this format. and make sure the strings match up.
			player.setTitle("[Novice]");
			World.sendMessage("<img=10> <col=008FB2>" + player.getUsername() + " has just reached Rookie [Prestige 1]");

			player.setBossPoints(player.getBossPoints() + 5);
			player.sendMessage("you've recieved 5 boss points for prestiging, the higher the rank! more benefits!");
			player.setDonationPoints(player.getDonationPoints() + 5);
			player.sendMessage("you've recieved 5 donation points for prestiging, the higher the rank! more benefits!");
			player.setprestigePoints(player.getPrestigePoints() + 5);
			player.sendMessage("you've recieved 5 prestige points for prestiging, the higher the rank! more benefits!");
			player.setpkPoints(player.getpkPoints() + 5);
			player.sendMessage("you've recieved 5 PK points for prestiging, the higher the rank! more benefits!");
			player.setvotingPoints(player.getvotingPoints() + 5);
			player.sendMessage("you've recieved 5 Voting points for prestiging, the higher the rank! more benefits!");

			// player.setSkillPoints(player.getSkillPoints() + 10);
			// player.sendMessage("you've recieved 10 skill points for prestiging, the
			// higher the rank! more benefits!");
			player.setMinutesBonusExp(120, true);
			player.sendMessage("you've recieved 120 Minutes of Bonus Exp!");
			player.isRecoveringSpecialAttack();
			player.forceChat("I Have just reached Prestige 1!");
			player.performAnimation(anim1);
			player.performGraphic(gfx1);

			// player.item
		} else if (prestigeId == 2) {
			player.setTitle("[Sir]");
			World.sendMessage("<img=10> <col=008FB2>" + player.getUsername() + " has just reached Novice [Prestige 2]");

			player.setBossPoints(player.getBossPoints() + 10);
			player.sendMessage("you've recieved 10 boss points for prestiging, the higher the rank! more benefits!");
			player.setDonationPoints(player.getDonationPoints() + 10);
			player.sendMessage(
					"you've recieved 10 donation points for prestiging, the higher the rank! more benefits!");
			player.setprestigePoints(player.getPrestigePoints() + 10);
			player.sendMessage(
					"you've recieved 10 prestige points for prestiging, the higher the rank! more benefits!");
			player.setpkPoints(player.getpkPoints() + 10);
			player.sendMessage("you've recieved 10 PK points for prestiging, the higher the rank! more benefits!");
			player.setvotingPoints(player.getvotingPoints() + 10);
			player.sendMessage("you've recieved 10 Voting points for prestiging, the higher the rank! more benefits!");
			player.setMinutesBonusExp(180, true);
			player.sendMessage("you've recieved 120 Minutes of Bonus Exp!");
			player.isRecoveringSpecialAttack();
			player.forceChat("I Have just reached Prestige 2!");
			player.performAnimation(anim1);
			player.performGraphic(gfx1);

			// access to metal dragons
		} else if (prestigeId == 3) {
			player.setTitle("[Hero]");
			World.sendMessage("<img=10> <col=008FB2>" + player.getUsername() + " has just reached Private");

			player.setBossPoints(player.getBossPoints() + 15);
			player.sendMessage("you've recieved 15 boss points for prestiging, the higher the rank! more benefits!");
			player.setDonationPoints(player.getDonationPoints() + 15);
			player.sendMessage(
					"you've recieved 15 donation points for prestiging, the higher the rank! more benefits!");
			player.setprestigePoints(player.getPrestigePoints() + 15);
			player.sendMessage(
					"you've recieved 15 prestige points for prestiging, the higher the rank! more benefits!");
			player.setpkPoints(player.getpkPoints() + 15);
			player.sendMessage("you've recieved 15 PK points for prestiging, the higher the rank! more benefits!");
			player.setvotingPoints(player.getvotingPoints() + 15);
			player.sendMessage("you've recieved 15 Voting points for prestiging, the higher the rank! more benefits!");

			// player.setSkillPoints(player.getSkillPoints() + 10);
			// player.sendMessage("you've recieved 10 skill points for prestiging, the
			// higher the rank! more benefits!");
			player.setMinutesBonusExp(300, true);
			player.sendMessage("you've recieved 300 Minutes of Bonus Exp!");
			player.isRecoveringSpecialAttack();
			player.forceChat("I Have just reached Prestige 3!");
			player.performAnimation(anim1);
			player.performGraphic(gfx1);

			// access to revenants cave
		} else if (prestigeId == 4) {
			player.setTitle("[Lord]");
			World.sendMessage("<img=10> <col=008FB2>" + player.getUsername() + " has just reached Specialist");

			player.setBossPoints(player.getBossPoints() + 20);
			player.sendMessage("you've recieved 20 boss points for prestiging, the higher the rank! more benefits!");
			player.setDonationPoints(player.getDonationPoints() + 20);
			player.sendMessage(
					"you've recieved 20 donation points for prestiging, the higher the rank! more benefits!");
			player.setprestigePoints(player.getPrestigePoints() + 20);
			player.sendMessage(
					"you've recieved 20 prestige points for prestiging, the higher the rank! more benefits!");
			player.setpkPoints(player.getpkPoints() + 20);
			player.sendMessage("you've recieved 20 PK points for prestiging, the higher the rank! more benefits!");

			player.setvotingPoints(player.getvotingPoints() + 20);
			player.sendMessage("you've recieved 20 Voting points for prestiging, the higher the rank! more benefits!");
			player.setMinutesBonusExp(120, true);
			player.sendMessage("you've recieved 120 Minutes of Bonus Exp!");
			player.isRecoveringSpecialAttack();
			player.forceChat("I Have just reached Prestige 1!");
			player.performAnimation(anim1);
			player.performGraphic(gfx1);

		} else if (prestigeId == 5) {
			player.setTitle("[Elite]");
			World.sendMessage("<img=10> <col=008FB2>" + player.getUsername() + " has just reached Sergeant");

			player.setBossPoints(player.getBossPoints() + 25);
			player.sendMessage("you've recieved 25 boss points for prestiging, the higher the rank! more benefits!");
			player.setDonationPoints(player.getDonationPoints() + 25);
			player.sendMessage(
					"you've recieved 25 donation points for prestiging, the higher the rank! more benefits!");
			player.setprestigePoints(player.getPrestigePoints() + 25);
			player.sendMessage(
					"you've recieved 25 prestige points for prestiging, the higher the rank! more benefits!");
			player.setpkPoints(player.getpkPoints() + 25);
			player.sendMessage("you've recieved 25 PK points for prestiging, the higher the rank! more benefits!");
			player.setvotingPoints(player.getvotingPoints() + 25);
			player.sendMessage("you've recieved 25 Voting points for prestiging, the higher the rank! more benefits!");

			player.setMinutesBonusExp(120, true);
			player.sendMessage("you've recieved 120 Minutes of Bonus Exp!");
			player.isRecoveringSpecialAttack();
			player.forceChat("I Have just reached Prestige 1!");
			player.performAnimation(anim1);
			player.performGraphic(gfx1);

			//
		} else if (prestigeId == 6) {
			player.setTitle("[Colonel]");
			World.sendMessage("<img=10> <col=008FB2>" + player.getUsername() + " has just reached First Seargeant ");

			player.setBossPoints(player.getBossPoints() + 30);
			player.sendMessage("you've recieved 30 boss points for prestiging, the higher the rank! more benefits!");
			player.setDonationPoints(player.getDonationPoints() + 30);
			player.sendMessage(
					"you've recieved 30 donation points for prestiging, the higher the rank! more benefits!");
			player.setprestigePoints(player.getPrestigePoints() + 30);
			player.sendMessage(
					"you've recieved 30 prestige points for prestiging, the higher the rank! more benefits!");
			player.setpkPoints(player.getpkPoints() + 30);
			player.sendMessage("you've recieved 30 PK points for prestiging, the higher the rank! more benefits!");
			player.setvotingPoints(player.getvotingPoints() + 30);
			player.sendMessage("you've recieved 30 Voting points for prestiging, the higher the rank! more benefits!");

			// player.setSkillPoints(player.getSkillPoints() + 10);
			// player.sendMessage("you've recieved 10 skill points for prestiging, the
			// higher the rank! more benefits!");
			player.setMinutesBonusExp(120, true);
			player.sendMessage("you've recieved 120 Minutes of Bonus Exp!");
			player.isRecoveringSpecialAttack();
			player.forceChat("I Have just reached Prestige 1!");
			player.performAnimation(anim1);
			player.performGraphic(gfx1);

		} else if (prestigeId == 7) {
			player.setTitle("[Brigadier General]");
			World.sendMessage("<img=10> <col=008FB2>" + player.getUsername() + " has just reached Seargeant Major");

			player.setBossPoints(player.getBossPoints() + 35);
			player.sendMessage("you've recieved 35 boss points for prestiging, the higher the rank! more benefits!");
			player.setDonationPoints(player.getDonationPoints() + 35);
			player.sendMessage(
					"you've recieved 35 donation points for prestiging, the higher the rank! more benefits!");
			player.setprestigePoints(player.getPrestigePoints() + 35);
			player.sendMessage(
					"you've recieved 35 prestige points for prestiging, the higher the rank! more benefits!");
			player.setvotingPoints(player.getvotingPoints() + 35);
			player.sendMessage("you've recieved 35 Voting points for prestiging, the higher the rank! more benefits!");
			player.setpkPoints(player.getpkPoints() + 35);
			player.sendMessage("you've recieved 35 PK points for prestiging, the higher the rank! more benefits!");

			// player.setSkillPoints(player.getSkillPoints() + 10);
			// player.sendMessage("you've recieved 10 skill points for prestiging, the
			// higher the rank! more benefits!");
			player.setMinutesBonusExp(120, true);
			player.sendMessage("you've recieved 120 Minutes of Bonus Exp!");
			player.isRecoveringSpecialAttack();
			player.forceChat("I Have just reached Prestige 1!");
			player.performAnimation(anim1);
			player.performGraphic(gfx1);

		} else if (prestigeId == 8) {
			player.setTitle("[Major General]");
			World.sendMessage("<img=10> <col=008FB2>" + player.getUsername() + " has just reached Captain");

			player.setBossPoints(player.getBossPoints() + 40);
			player.sendMessage("you've recieved 40 boss points for prestiging, the higher the rank! more benefits!");
			player.setDonationPoints(player.getDonationPoints() + 40);
			player.sendMessage(
					"you've recieved 40 donation points for prestiging, the higher the rank! more benefits!");
			player.setprestigePoints(player.getPrestigePoints() + 40);
			player.sendMessage(
					"you've recieved 40 prestige points for prestiging, the higher the rank! more benefits!");
			player.setvotingPoints(player.getvotingPoints() + 40);
			player.sendMessage("you've recieved 40 Voting points for prestiging, the higher the rank! more benefits!");
			player.setpkPoints(player.getpkPoints() + 40);
			player.sendMessage("you've recieved 40 PK points for prestiging, the higher the rank! more benefits!");

			// player.setSkillPoints(player.getSkillPoints() + 10);
			// player.sendMessage("you've recieved 10 skill points for prestiging, the
			// higher the rank! more benefits!");
			player.setMinutesBonusExp(120, true);
			player.sendMessage("you've recieved 120 Minutes of Bonus Exp!");
			player.isRecoveringSpecialAttack();
			player.forceChat("I Have just reached Prestige 1!");
			player.performAnimation(anim1);
			player.performGraphic(gfx1);

		} else if (prestigeId == 9) {
			player.setTitle("[Lieutenant General]");
			World.sendMessage("<img=10> <col=008FB2>[Prestige 12]" + player.getUsername() + " has just reached Major");

			player.setBossPoints(player.getBossPoints() + 90); // add 90 the amount
			player.sendMessage("you've recieved 90 boss points for prestiging, the higher the rank! more benefits!");
			// player.setSkillPoints(player.getSkillPoints() + 90); // add 90 the amount
			// player.sendMessage("you've recieved 90 skill points for prestiging, the
			// higher the rank! more benefits!");
			player.setMinutesBonusExp(600, true); // sdd 600 to the amount
			player.sendMessage("you've recieved 600 Minutes of Bonus Exp!");

		} else if (prestigeId == 10) {
			player.setTitle("[First Class]");
			World.sendMessage("<img=10> <col=008FB2>[Prestige 12]" + player.getUsername()
					+ " has just reached Lieutenant Colenel");

			player.setBossPoints(player.getBossPoints() + 90); // add 90 the amount
			player.sendMessage("you've recieved 90 boss points for prestiging, the higher the rank! more benefits!");
			// player.setSkillPoints(player.getSkillPoints() + 90); // add 90 the amount
			// player.sendMessage("you've recieved 90 skill points for prestiging, the
			// higher the rank! more benefits!");
			player.setMinutesBonusExp(600, true); // sdd 600 to the amount
			player.sendMessage("you've recieved 600 Minutes of Bonus Exp!");

		} else if (prestigeId == 11) {
			player.setTitle("[Private]");
			World.sendMessage(
					"<img=10> <col=008FB2>[Prestige 11]" + player.getUsername() + " has just reached Colenel");

			player.setBossPoints(player.getBossPoints() + 100); // add 100 the amount
			player.sendMessage("you've recieved 100 boss points for prestiging, the higher the rank! more benefits!");
			// player.setSkillPoints(player.getSkillPoints() + 100); // add 100 the amount
			// player.sendMessage("you've recieved 100 skill points for prestiging, the
			// higher the rank! more benefits!");
			player.setMinutesBonusExp(660, true); // sdd 660 to the amount
			player.sendMessage("you've recieved 660 Minutes of Bonus Exp!");

		} else if (prestigeId == 12) {
			player.setTitle("[Commander]");
			World.sendMessage("<img=10> <col=008FB2>[Prestige 12]" + player.getUsername()
					+ " has just reached Brigadier General");

			player.setBossPoints(player.getBossPoints() + 110); // add 110 the amount
			player.sendMessage("you've recieved 110 boss points for prestiging, the higher the rank! more benefits!");
			// player.setSkillPoints(player.getSkillPoints() + 110); // add 110 the amount
			// player.sendMessage("you've recieved 110 skill points for prestiging, the
			// higher the rank! more benefits!");
			player.setMinutesBonusExp(720, true); // sdd 720 to the amount
			player.sendMessage("you've recieved 720 Minutes of Bonus Exp!");

		} else if (prestigeId == 13) {
			player.setTitle("[Lieutenant]");
			World.sendMessage(
					"<img=10> <col=008FB2>[Prestige 13]" + player.getUsername() + " has just reached Major General");

			player.setBossPoints(player.getBossPoints() + 120); // add 120 the amount
			player.sendMessage("you've recieved 120 boss points for prestiging, the higher the rank! more benefits!");
			// player.setSkillPoints(player.getSkillPoints() + 120); // add 120 the amount
			// player.sendMessage("you've recieved 120 skill points for prestiging, the
			// higher the rank! more benefits!");
			player.setMinutesBonusExp(780, true); // sdd 780 to the amount
			player.sendMessage("you've recieved 780 Minutes of Bonus Exp!");

		} else if (prestigeId == 14) {
			player.setTitle("[General]");
			World.sendMessage(
					"<img=10> <col=008FB2>[Prestige 14]" + player.getUsername() + " has just reached Elite Prestige");

			player.setBossPoints(player.getBossPoints() + 130); // add 130 the amount
			player.sendMessage("you've recieved 130 boss points for prestiging, the higher the rank! more benefits!");
			// player.setSkillPoints(player.getSkillPoints() + 130); // add 130 the amount
			// player.sendMessage("you've recieved 130 skill points for prestiging, the
			// higher the rank! more benefits!");
			player.setMinutesBonusExp(840, true); // sdd 840 to the amount
			player.sendMessage("you've recieved 840 Minutes of Bonus Exp!");

		} else if (prestigeId == 15) {
			player.setTitle("[God]");
			World.sendMessage(
					"<img=10> <col=008FB2>[Prestige 15]" + player.getUsername() + " has just reached Prestige GOD!");

			player.setBossPoints(player.getBossPoints() + 200); // add 200 the amount
			player.sendMessage("you've recieved 200 boss points for prestiging, the higher the rank! more benefits!");
			// player.setSkillPoints(player.getSkillPoints() + 200); // add 200 the amount
			// player.sendMessage("you've recieved 200 skill points for prestiging, the
			// higher the rank! more benefits!");
			player.setMinutesBonusExp(900, true); // sdd 900 to the amount
			player.sendMessage("you've recieved 900 Minutes of Bonus Exp!");

		} else if (prestigeId > 15) {
			player.sendMessage("You have reached the maximum Prestige Level, gz you have no life.");

		}
	}
	*/
//}
