package com.ruseps.world.content.combat.strategy.impl;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Animation;
import com.ruseps.model.Graphic;
import com.ruseps.model.Locations;
import com.ruseps.model.Position;
import com.ruseps.model.Projectile;
import com.ruseps.util.Misc;
import com.ruseps.world.content.combat.CombatContainer;
import com.ruseps.world.content.combat.CombatType;
import com.ruseps.world.content.combat.prayer.PrayerHandler;
import com.ruseps.world.content.combat.strategy.CombatStrategy;
import com.ruseps.world.entity.impl.Character;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

public class Glod implements CombatStrategy {

	private static final Animation anim = new Animation(6501);

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
		NPC glod = (NPC) entity;
		if (glod.isChargingAttack() || victim.getConstitution() <= 0) {
			return true;
		}
		if (Misc.getRandom(15) <= 3) {
			for (int i = 0; i < 30; i++) {
				PrayerHandler.deactivatePrayer(((Player) victim), i);
			}
			glod.forceChat("Glod Smash!!");
		}
		if (Locations.goodDistance(glod.getPosition().copy(), victim.getPosition().copy(), 3)
				&& Misc.getRandom(5) <= 3) {
			glod.performAnimation(new Animation(glod.getDefinition().getAttackAnimation()));
			glod.getCombatBuilder().setContainer(new CombatContainer(glod, victim, 1, 1, CombatType.MELEE, true));
			glod.performAnimation(anim);

		} else {
			glod.setChargingAttack(true);
			glod.performAnimation(anim);
			glod.getCombatBuilder().setContainer(new CombatContainer(glod, victim, 1, 3, CombatType.MAGIC, true));

			glod.setChargingAttack(false);

		}
		return true;
	}

	@Override
	public int attackDelay(Character entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character entity) {
		return 20;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MIXED;
	}
}