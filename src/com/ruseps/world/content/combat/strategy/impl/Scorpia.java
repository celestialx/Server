package com.ruseps.world.content.combat.strategy.impl;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Animation;
import com.ruseps.model.Graphic;
import com.ruseps.model.GraphicHeight;
import com.ruseps.util.Misc;
import com.ruseps.world.content.combat.CombatContainer;
import com.ruseps.world.content.combat.CombatType;
import com.ruseps.world.content.combat.HitQueue.CombatHit;
import com.ruseps.world.content.combat.strategy.CombatStrategy;
import com.ruseps.world.entity.impl.Character;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

/**
 * Vados 14/05/2016 22:40PM
 */

public class Scorpia implements CombatStrategy {

	private static final Animation attack_anim = new Animation(6254);
	private static final Graphic graphic1 = new Graphic(1200, GraphicHeight.MIDDLE);

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
		NPC venantis = (NPC) entity;
		if (venantis.isChargingAttack() || venantis.getConstitution() <= 0) {
			return true;
		}
		CombatType style = Misc.getRandom(2) <= 1 ? CombatType.MELEE : CombatType.MAGIC;
		if (style == CombatType.MELEE) {
			venantis.performAnimation(new Animation(venantis.getDefinition().getAttackAnimation()));
			venantis.getCombatBuilder()
					.setContainer(new CombatContainer(venantis, victim, 1, 1, CombatType.MELEE, true));
			venantis.getCombatBuilder()
					.setContainer(new CombatContainer(venantis, victim, 1, 1, CombatType.MAGIC, true));
		} else {
			venantis.performAnimation(attack_anim);
			venantis.setChargingAttack(true);
			Player target = (Player) victim;
			for (Player t : Misc.getCombinedPlayerList(target)) {
			}
			TaskManager.submit(new Task(2, target, false) {
				@Override
				public void execute() {
					for (Player t : Misc.getCombinedPlayerList(target)) {
						venantis.getCombatBuilder().setVictim(t);
					//	new CombatHit().handleAttack();
						venantis.performAnimation(new Animation(venantis.getDefinition().getAttackAnimation()));
						venantis.getCombatBuilder()
								.setContainer(new CombatContainer(venantis, victim, 1, 1, CombatType.MELEE, true));
					}
					venantis.setChargingAttack(false);
					stop();
				}
			});
		}
		return true;
	}

	@Override
	public int attackDelay(Character entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character entity) {
		return 3;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MIXED;
	}
}
