


package com.ruseps.world.content.combat.strategy.impl;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Animation;
import com.ruseps.model.Graphic;
import com.ruseps.util.Misc;
import com.ruseps.world.content.combat.CombatContainer;
import com.ruseps.world.content.combat.CombatType;
import com.ruseps.world.content.combat.HitQueue.CombatHit;
import com.ruseps.world.content.combat.magic.CombatSpells;
import com.ruseps.world.content.combat.strategy.CombatStrategy;
import com.ruseps.world.entity.impl.Character;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

/**
 * @author adam trinity // 8bytes/ adamm__ skype: live:nrpker7839
 */


public class Necrolord implements CombatStrategy {

	@Override
	public boolean canAttack(Character entity, Character victim) {
		return true;
	}

	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		return false;
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		NPC cobra = (NPC)entity;
		int att = Misc.getRandom(3);
		if(att == 1) {
			cobra.prepareSpell(CombatSpells.BLOOD_BARRAGE.getSpell(), victim);
		}
		if(att == 2) {
			cobra.prepareSpell(CombatSpells.ICE_BARRAGE.getSpell(), victim);
		}
		if(att == 3) {
			cobra.prepareSpell(CombatSpells.ICE_BLITZ.getSpell(), victim);
		}
		return new CombatContainer(entity, victim, 1, CombatType.MAGIC, true);
	}
	@Override
	public int attackDelay(Character entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character entity) {
		return 15;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MAGIC;
	}
}


/*
 *
public class Necrolord implements CombatStrategy {

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
		NPC cobra = (NPC) entity;
		if (cobra.isChargingAttack() || cobra.getConstitution() <= 0) {
			return true;
		}
		cobra.performAnimation(new Animation(1979));
		cobra.setChargingAttack(true);
		Player target = (Player) victim;
		TaskManager.submit(new Task(2, target, false) {
			@Override
			public void execute() {
				cobra.getCombatBuilder().setVictim(target);
				if (victim.isFrozen()) {
					victim.performGraphic(new Graphic(1677));
				} else {
					victim.performGraphic(new Graphic(369));
					//victim.getWalkingQueue().freeze(15);
				}
			//	new CombatHit().handleAttack();
				cobra.setChargingAttack(false);
				stop();
			}
		});
		return true;
	}

	@Override
	public int attackDelay(Character entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character entity) {
		return 7;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MAGIC;
	}
}
*/