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

public class JalZek1 implements CombatStrategy {

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
		NPC JalZek = (NPC) entity;
		if (entity.getConstitution() <= 0 || victim.getConstitution() <= 0) {
			return true;
		}
		if (JalZek.isChargingAttack()) {
			return true;
		}
		if (Locations.goodDistance(JalZek.getPosition().getX(), JalZek.getPosition().getY(),
				victim.getPosition().getX(), victim.getPosition().getY(), 15)) {
			JalZek.setChargingAttack(true);
			JalZek.getCombatBuilder().setContainer(new CombatContainer(JalZek, victim, 1, 3, CombatType.MAGIC, true));
			JalZek.performAnimation(mage);
			TaskManager.submit(new Task(1, JalZek, false) {
				int tick = 0;

				@Override
				public void execute() {
					switch (tick) {
					case 0:
						new Projectile(JalZek, victim, mageGfx.getId(), 50, 5, 100, 40, 0).sendProjectile();
						JalZek.setChargingAttack(false);
						stop();
						break;
					}
					tick++;
				}
			});
		
		}
		else{
			JalZek.setChargingAttack(true);
			JalZek.getCombatBuilder().setContainer(new CombatContainer(JalZek, victim, 1, 4, CombatType.MAGIC, true));
			JalZek.performAnimation(mage);
			TaskManager.submit(new Task(1, JalZek, false) {
				int tick = 0;

				@Override
				public void execute() {
					switch (tick) {
					case 0:
						new Projectile(JalZek, victim, mageGfx.getId(), 50, 5, 100, 40, 0).sendProjectile();
						JalZek.setChargingAttack(false);
						stop();
						break;
					}
					tick++;
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
		return 30;
	}
	
	private static final Animation melee = new Animation(7612);
	private static final Animation mage = new Animation(7610);
	private static final Graphic mageGfx = new Graphic(1376);

	@Override
	public CombatType getCombatType() {
		return CombatType.MIXED;
	}
}
