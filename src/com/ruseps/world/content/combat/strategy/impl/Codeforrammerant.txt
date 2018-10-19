package com.ruseps.world.content.combat.strategy.impl;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Animation;
import com.ruseps.model.Graphic;
import com.ruseps.model.GraphicHeight;
import com.ruseps.model.Locations;
import com.ruseps.model.Projectile;
import com.ruseps.model.Skill;
import com.ruseps.util.Misc;
import com.ruseps.world.content.combat.CombatContainer;
import com.ruseps.world.content.combat.CombatHitTask;
import com.ruseps.world.content.combat.CombatType;
import com.ruseps.world.content.combat.strategy.CombatStrategy;
import com.ruseps.world.entity.impl.Character;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

public class Rammernaut implements CombatStrategy {


	private static final Animation anim = new Animation(13705);
	private static final Animation anim2 = new Animation(13703);
	private static final Animation anim3 = new Animation(13701);

	private static final Graphic gfx1 = new Graphic(1198);
	private static final Graphic gfx2 = new Graphic(1198);

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
		NPC ram = (NPC) entity;
		if (victim.getConstitution() <= 0) {
			return true;
		}
		if (ram.isChargingAttack()) {
			return true;
		}
		Player target = (Player) victim;
		for (Player t : Misc.getCombinedPlayerList(target)) {
			if (Locations.goodDistance(t.getPosition(), ram.getPosition(), 2)) {
				ram.performAnimation(anim);
				victim.performGraphic(gfx1);
				ram.getCombatBuilder().setVictim(t);
				new CombatHitTask(ram.getCombatBuilder(), new CombatContainer(ram, t, 1, CombatType.MELEE, true))
						.handleAttack();
			}
		}
		int random = Misc.getRandom(10);

		if (Locations.goodDistance(ram.getPosition().copy(), victim.getPosition().copy(), 2)
				 && random <= 4) {
			ram.performAnimation(anim);
			victim.performGraphic(gfx1);
			ram.getCombatBuilder().setContainer(new CombatContainer(ram, victim, 1, 2, CombatType.MELEE, true));
		} else if (random >= 6) {
			ram.performAnimation(anim2);
			ram.setChargingAttack(true);
			ram.getCombatBuilder().setContainer(new CombatContainer(ram, victim, 1, 2, CombatType.MAGIC, true));
			ram.setChargingAttack(false).getCombatBuilder()
			.setAttackTimer(ram.getDefinition().getAttackSpeed() - 1);
		} else {
			ram.performAnimation(anim3);
			victim.performGraphic(gfx2);
			ram.getCombatBuilder().setContainer(new CombatContainer(ram, victim, 1, 2, CombatType.MAGIC, true));
		}

		return true;
	}

	@Override
	public int attackDelay(Character entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character entity) {
		return 10;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MIXED;
	}

}