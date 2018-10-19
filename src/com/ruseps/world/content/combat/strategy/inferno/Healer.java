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

public class Healer implements CombatStrategy {

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
		NPC Healer = (NPC) entity;
		if (entity.getConstitution() <= 0 || victim.getConstitution() <= 0) {
			return true;
		}
		if (Healer.isChargingAttack()) {
			return true;
		}

		if (victim.isNpc()) {
			Healer.setChargingAttack(true);
			TaskManager.submit(new Task(1, Healer, false) {
				int tick = 0;

				@Override
				public void execute() {
					switch (tick) {
					case 0:
						Healer.performAnimation(attack);
						victim.heal(100 + Misc.getRandom(50));
						Healer.setChargingAttack(false);
						stop();
						break;
					}
					tick++;
				}
			});
		}  else {
			
			final Position start = victim.getPosition().copy();
			final Position second = new Position(start.getX() + 1 + Misc.getRandom(1), start.getY());
			final Position last = new Position (start.getX() - 1 - Misc.getRandom(1), start.getY());
			
			Healer.setChargingAttack(true);
			Healer.getCombatBuilder().setContainer(new CombatContainer(Healer, victim, 1, 3, CombatType.RANGED, true));

			TaskManager.submit(new Task(1, Healer, false) {
				int tick = 0;

				@Override
				public void execute() {
					switch (tick) {
					case 0:
						Healer.performAnimation(attack);
						new Projectile(Healer, victim, attackGfx.getId(), 44, 3, 43, 43, 0).sendProjectile();
						break;
					case 2:
						((Player) victim).getPacketSender().sendGlobalGraphic(hit, start);
						((Player) victim).getPacketSender().sendGlobalGraphic(hit, second);
						((Player) victim).getPacketSender().sendGlobalGraphic(hit, last);
						Healer.setChargingAttack(false);
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
		return 40;
	}

	private static final Animation attack = new Animation(2868);
	private static final Graphic attackGfx = new Graphic(660);
	private static final Graphic hit = new Graphic(659);

	@Override
	public CombatType getCombatType() {
		return CombatType.MIXED;
	}
}
