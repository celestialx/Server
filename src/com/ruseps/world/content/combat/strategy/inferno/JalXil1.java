package com.ruseps.world.content.combat.strategy.inferno;

import java.util.List;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Animation;
import com.ruseps.model.CombatIcon;
import com.ruseps.model.Graphic;
import com.ruseps.model.Hit;
import com.ruseps.model.Hitmask;
import com.ruseps.model.Locations;
import com.ruseps.model.Position;
import com.ruseps.model.Projectile;
import com.ruseps.util.Misc;
import com.ruseps.world.content.combat.CombatContainer;
import com.ruseps.world.content.combat.CombatType;
import com.ruseps.world.content.combat.prayer.CurseHandler;
import com.ruseps.world.content.combat.prayer.PrayerHandler;
import com.ruseps.world.content.combat.strategy.CombatStrategy;
import com.ruseps.world.entity.impl.Character;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

public class JalXil1 implements CombatStrategy {

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
		NPC JalXil = (NPC) entity;
		if (entity.getConstitution() <= 0 || victim.getConstitution() <= 0) {
			return true;
		}
		if (JalXil.isChargingAttack()) {
			return true;
		}
		/*
		 * final Player p = (Player) victim;
		 * 
		 * if (Locations.goodDistance(JalXil.getPosition().getX(),
		 * JalXil.getPosition().getY(), victim.getPosition().getX(),
		 * victim.getPosition().getY(), 2)) { JalXil.setChargingAttack(true);
		 * JalXil.getCombatBuilder().setContainer(new CombatContainer(JalXil,
		 * victim, 1, 2, CombatType.MELEE, true)); TaskManager.submit(new
		 * Task(1, JalXil, false) { int tick = 0;
		 * 
		 * @Override public void execute() { switch (tick) { case 0:
		 * JalXil.performAnimation(melee); JalXil.setChargingAttack(false);
		 * stop(); break; } tick++; } }); } else{
		 */
		JalXil.setChargingAttack(true);
		JalXil.getCombatBuilder().setContainer(new CombatContainer(JalXil, victim, 1, 3, CombatType.RANGED, true));
		JalXil.performAnimation(range);
		TaskManager.submit(new Task(1, JalXil, false) {
			int tick = 0;

			@Override
			public void execute() {
				switch (tick) {
				case 1:
					new Projectile(JalXil, victim, 1374, 50, 5, 80, 40, 0).sendProjectile();
					new Projectile(JalXil, victim, rangeGfx.getId(), 50, 5, 80, 40, 0).sendProjectile();
					JalXil.setChargingAttack(false);
					stop();
					break;
				}
				tick++;
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
		return 35;
	}

	private static final Animation melee = new Animation(7604);
	private static final Animation range = new Animation(7605);
	private static final Graphic rangeGfx = new Graphic(1377);

	@Override
	public CombatType getCombatType() {
		return CombatType.MIXED;
	}
}
