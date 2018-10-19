package com.ruseps.world.content.combat.strategy.impl;

import com.ruseps.model.*;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.content.combat.CombatContainer;
import com.ruseps.world.content.combat.CombatType;
import com.ruseps.world.content.combat.HitQueue;
import com.ruseps.world.content.combat.strategy.CombatStrategy;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.Character;
import com.ruseps.model.Hit;
import com.ruseps.world.entity.impl.player.Player;

public class CrazyArcheologist implements CombatStrategy {
	
	private static final String[] ATTACK_MESSAGES = new String[] {
			"I'm Bellock - respect me!",
			"Get off my site!",
			"No-one messes with Bellock's dig!",
			"These ruins are mine!",
			"Taste my knowledge!",
			"You belong in a museum!"
		};

	private boolean usingSpecial = false;

	@Override
	public boolean canAttack(Character entity, Character victim) {
		return true;
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		return null;
	}

	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		int random = Misc.getRandom(4);
		switch(random) {
			case 2:
				special(((NPC)entity), ((Player)victim));
				break;
		default:
			entity.performAnimation(new Animation(806));
		//	new HitQueue.CombatHit().handleAttack();
			entity.forceChat(ATTACK_MESSAGES[Misc.getRandom(ATTACK_MESSAGES.length - 1)]);
			break;
		}
		return false;
	}

	/**
	 * Handles the special attack for the crazy dood
	 * @param attacker
	 * @param player
	 */
	public void special(NPC attacker, Player player) {
		for (int i = 0; i < 3; i++) {
			int offsetX = player.getPosition().getX() - attacker.getPosition().getX();
			int offsetY = player.getPosition().getY() - attacker.getPosition().getY();
			if (i == 0 || i == 2) {
				offsetX += i == 0 ? -1 : 1;
				offsetY++;
			}
			Position end = new Position(attacker.getPosition().getX() + offsetX, attacker.getPosition().getY() + offsetY, 0);

			new Projectile(attacker, player, 551, 10, 100, 65, 10, 20).sendProjectile();
			//World.sendStillGraphic(659, 100, end);

			if (player.getPosition().getX() == end.getX() && player.getPosition().getY() == end.getY()) {
				int damage = Misc.random(15) + Misc.random(15) + 12;
				if (damage > 23) {
					damage = 23;
				}
				player.dealDamage(null);
				usingSpecial = false;
			}
		}
	}

	@Override
	public int attackDelay(Character entity) {
		return 4;
	}

	@Override
	public int attackDistance(Character entity) {
		return 7;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.RANGED;
	}

}
