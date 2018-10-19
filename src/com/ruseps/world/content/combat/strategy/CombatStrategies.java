package com.ruseps.world.content.combat.strategy;

import java.util.HashMap;

import java.util.Map;

import com.ruseps.world.content.combat.strategy.impl.Abbadon;
import com.ruseps.world.content.combat.strategy.impl.BallakPummeler;
import com.ruseps.world.content.combat.strategy.impl.Cyrisus;
import com.ruseps.world.content.combat.strategy.impl.DireWolf;
import com.ruseps.world.content.combat.strategy.impl.Dragonix;
import com.ruseps.world.content.combat.strategy.impl.MiniDire;
import com.ruseps.world.content.combat.strategy.impl.MutantTarn;
import com.ruseps.world.content.combat.strategy.impl.Nazastarool;
import com.ruseps.world.content.combat.strategy.impl.ToKashBloodchiller;
import com.ruseps.world.content.combat.strategy.impl.Vladimir;
import com.ruseps.world.content.combat.strategy.impl.ArmadylAbyzou;
import com.ruseps.world.content.combat.strategy.impl.ZamorakIktomi;
import com.ruseps.world.content.combat.strategy.impl.ZamorakLefosh;
import com.ruseps.world.content.combat.strategy.impl.AbyssalSire;
import com.ruseps.world.content.combat.strategy.impl.AnimatedBook;
import com.ruseps.world.content.combat.strategy.impl.AvatarOfCreation;
import com.ruseps.world.content.combat.strategy.impl.skeletalHorror;
import com.ruseps.world.content.combat.strategy.inferno.Healer;
import com.ruseps.world.content.combat.strategy.inferno.JalTokJad;
import com.ruseps.world.content.combat.strategy.inferno.JalXil1;
import com.ruseps.world.content.combat.strategy.inferno.JalZek1;
import com.ruseps.world.content.combat.strategy.impl.TheGeneral;

import com.ruseps.world.content.combat.strategy.impl.AvatarOfMagic;
import com.ruseps.world.content.combat.strategy.impl.AvatarOfRange;
import com.ruseps.world.content.combat.strategy.impl.Aviansie;
//import com.ruseps.world.content.combat.strategy.impl.BalfrugKreeyath;
import com.ruseps.world.content.combat.strategy.impl.BandosAvatar;
import com.ruseps.world.content.combat.strategy.impl.BlueBirdThingy;
import com.ruseps.world.content.combat.strategy.impl.ChaosElemental;
import com.ruseps.world.content.combat.strategy.impl.ChaosFanatic;
import com.ruseps.world.content.combat.strategy.impl.Cobra;
import com.ruseps.world.content.combat.strategy.impl.CorporealBeast;
import com.ruseps.world.content.combat.strategy.impl.CrazyArcheologist;
import com.ruseps.world.content.combat.strategy.impl.DagannothPrime;
import com.ruseps.world.content.combat.strategy.impl.DagannothSupreme;
import com.ruseps.world.content.combat.strategy.impl.Death;
import com.ruseps.world.content.combat.strategy.impl.DefaultMagicCombatStrategy;
import com.ruseps.world.content.combat.strategy.impl.DefaultMeleeCombatStrategy;
import com.ruseps.world.content.combat.strategy.impl.DefaultRangedCombatStrategy;
import com.ruseps.world.content.combat.strategy.impl.Dragon;
import com.ruseps.world.content.combat.strategy.impl.DwarvenHandCannoneer;
import com.ruseps.world.content.combat.strategy.impl.Fear;
import com.ruseps.world.content.combat.strategy.impl.Geerin;
import com.ruseps.world.content.combat.strategy.impl.GiantMole;
import com.ruseps.world.content.combat.strategy.impl.Glacor;
import com.ruseps.world.content.combat.strategy.impl.Glod;
import com.ruseps.world.content.combat.strategy.impl.Graardor;
import com.ruseps.world.content.combat.strategy.impl.Grimspike;
import com.ruseps.world.content.combat.strategy.impl.Gritch;
import com.ruseps.world.content.combat.strategy.impl.Growler;
import com.ruseps.world.content.combat.strategy.impl.HarLakkRiftsplitter;
import com.ruseps.world.content.combat.strategy.impl.IceDemon;
import com.ruseps.world.content.combat.strategy.impl.Jad;
import com.ruseps.world.content.combat.strategy.impl.KalphiteQueen;
import com.ruseps.world.content.combat.strategy.impl.KreeArra;
import com.ruseps.world.content.combat.strategy.impl.Kreeyath;
import com.ruseps.world.content.combat.strategy.impl.Lexicus;
import com.ruseps.world.content.combat.strategy.impl.LizardMan;
import com.ruseps.world.content.combat.strategy.impl.Necrolord;
import com.ruseps.world.content.combat.strategy.impl.Nex;
import com.ruseps.world.content.combat.strategy.impl.Nomad;
import com.ruseps.world.content.combat.strategy.impl.PlaneFreezer;
import com.ruseps.world.content.combat.strategy.impl.Rammernaut;
import com.ruseps.world.content.combat.strategy.impl.Revenant;
import com.ruseps.world.content.combat.strategy.impl.Sagittare;
import com.ruseps.world.content.combat.strategy.impl.Scorpia;
import com.ruseps.world.content.combat.strategy.impl.Sire;
import com.ruseps.world.content.combat.strategy.impl.Skotizo;
import com.ruseps.world.content.combat.strategy.impl.Spinolyp;
import com.ruseps.world.content.combat.strategy.impl.Steelwill;
import com.ruseps.world.content.combat.strategy.impl.Thermonuclear;
import com.ruseps.world.content.combat.strategy.impl.TormentedDemon;
import com.ruseps.world.content.combat.strategy.impl.Tsutsuroth;
import com.ruseps.world.content.combat.strategy.impl.UnholyCursebearer;
import com.ruseps.world.content.combat.strategy.impl.WingmanSkree;
import com.ruseps.world.content.combat.strategy.impl.ZamorakianMage;
import com.ruseps.world.content.combat.strategy.impl.Venenatis;
import com.ruseps.world.content.combat.strategy.impl.Vetion;
import com.ruseps.world.content.combat.strategy.impl.WildyWyrm;
import com.ruseps.world.content.combat.strategy.impl.Callisto;
import com.ruseps.world.content.combat.strategy.impl.Cerberus;
import com.ruseps.world.content.combat.strategy.impl.ChaosDwogre;
import com.ruseps.world.content.combat.strategy.impl.Zilyana;
import com.ruseps.world.content.combat.strategy.zulrah.npc.Blue;
import com.ruseps.world.content.combat.strategy.zulrah.npc.Green;
import com.ruseps.world.content.combat.strategy.zulrah.npc.Red;
import com.ruseps.world.content.combat.strategy.impl.kraken.Kraken;
import com.ruseps.world.content.combat.strategy.impl.kraken.Tentacles;


public class CombatStrategies {

	private static final DefaultMeleeCombatStrategy defaultMeleeCombatStrategy = new DefaultMeleeCombatStrategy();
	private static final DefaultMagicCombatStrategy defaultMagicCombatStrategy = new DefaultMagicCombatStrategy();
	private static final DefaultRangedCombatStrategy defaultRangedCombatStrategy = new DefaultRangedCombatStrategy();
	private static final Map<Integer, CombatStrategy> STRATEGIES = new HashMap<Integer, CombatStrategy>();
	
	public static void init() {
		DefaultMagicCombatStrategy defaultMagicStrategy = new DefaultMagicCombatStrategy();
		STRATEGIES.put(13, defaultMagicStrategy);
		STRATEGIES.put(172, defaultMagicStrategy);
		STRATEGIES.put(174, defaultMagicStrategy);
		STRATEGIES.put(2025, defaultMagicStrategy);
		STRATEGIES.put(3495, defaultMagicStrategy);
		STRATEGIES.put(3496, defaultMagicStrategy);
		STRATEGIES.put(3491, defaultMagicStrategy);
		STRATEGIES.put(2882, defaultMagicStrategy);
		STRATEGIES.put(13451, defaultMagicStrategy);
		STRATEGIES.put(13452, defaultMagicStrategy);
		STRATEGIES.put(13453, defaultMagicStrategy);
		STRATEGIES.put(13454, defaultMagicStrategy);
		STRATEGIES.put(1643, defaultMagicStrategy);
		STRATEGIES.put(6254, defaultMagicStrategy);
		STRATEGIES.put(6257, defaultMagicStrategy);
		STRATEGIES.put(6278, defaultMagicStrategy);
		STRATEGIES.put(6221, defaultMagicStrategy);
		STRATEGIES.put(133, defaultMagicStrategy);
	;
		
		DefaultRangedCombatStrategy defaultRangedStrategy = new DefaultRangedCombatStrategy();
		STRATEGIES.put(688, defaultRangedStrategy);
		STRATEGIES.put(2028, defaultRangedStrategy);
		STRATEGIES.put(6220, defaultRangedStrategy);
		STRATEGIES.put(6256, defaultRangedStrategy);
		STRATEGIES.put(6276, defaultRangedStrategy);
		STRATEGIES.put(6252, defaultRangedStrategy);
		STRATEGIES.put(27, defaultRangedStrategy);
		
		STRATEGIES.put(3847, new Kraken());
		STRATEGIES.put(148, new Tentacles());
		STRATEGIES.put(2745, new Jad());
		STRATEGIES.put(8528, new Nomad());
		STRATEGIES.put(8349, new TormentedDemon());
		STRATEGIES.put(3200, new ChaosElemental());
		STRATEGIES.put(4540, new BandosAvatar());
		STRATEGIES.put(8133, new CorporealBeast());
		STRATEGIES.put(13447, new Nex());
		STRATEGIES.put(2896, new Spinolyp());
		STRATEGIES.put(3334, new WildyWyrm());
		STRATEGIES.put(2881, new DagannothSupreme());
		STRATEGIES.put(6260, new Graardor());
		STRATEGIES.put(6263, new Steelwill());
		STRATEGIES.put(6265, new Grimspike());
		STRATEGIES.put(6222, new KreeArra());
		STRATEGIES.put(6223, new WingmanSkree());
		STRATEGIES.put(6225, new Geerin());
		STRATEGIES.put(6203, new Tsutsuroth());
		STRATEGIES.put(3340, new GiantMole());
		STRATEGIES.put(6208, new Kreeyath());
		STRATEGIES.put(6206, new Gritch());
		STRATEGIES.put(6247, new Zilyana());
		STRATEGIES.put(6250, new Growler());
		STRATEGIES.put(1382, new Glacor());
		STRATEGIES.put(9939, new PlaneFreezer());
		STRATEGIES.put(2043, new Green());
		STRATEGIES.put(2042, new Blue());
		STRATEGIES.put(2044, new Red());
		STRATEGIES.put(135, new Fear());
		STRATEGIES.put(133, new Cobra());
		STRATEGIES.put(1472, new Death());
		STRATEGIES.put(132, new Death());
		Dragon dragonStrategy = new Dragon();
		STRATEGIES.put(50, dragonStrategy);
		STRATEGIES.put(941, dragonStrategy);
		STRATEGIES.put(55, dragonStrategy);
		STRATEGIES.put(53, dragonStrategy);
		STRATEGIES.put(54, dragonStrategy);
		STRATEGIES.put(51, dragonStrategy);
		STRATEGIES.put(1590, dragonStrategy);
		STRATEGIES.put(1591, dragonStrategy);
		STRATEGIES.put(1592, dragonStrategy);
		STRATEGIES.put(5362, dragonStrategy);
		STRATEGIES.put(5363, dragonStrategy);
		
		Aviansie aviansieStrategy = new Aviansie();
		STRATEGIES.put(6246, aviansieStrategy);
		STRATEGIES.put(6230, aviansieStrategy);
		STRATEGIES.put(6231, aviansieStrategy);
		
		KalphiteQueen kalphiteQueenStrategy = new KalphiteQueen();
		STRATEGIES.put(1158, kalphiteQueenStrategy);
		STRATEGIES.put(1160, kalphiteQueenStrategy);
		
		Revenant revenantStrategy = new Revenant();
		STRATEGIES.put(6715, revenantStrategy);
		STRATEGIES.put(6716, revenantStrategy);
		STRATEGIES.put(6701, revenantStrategy);
		STRATEGIES.put(6725, revenantStrategy);
		STRATEGIES.put(6691, revenantStrategy);
		
		STRATEGIES.put(2000, new Venenatis());
		STRATEGIES.put(2006, new Vetion());
		STRATEGIES.put(2010, new Callisto());
		STRATEGIES.put(1999, new Cerberus());
		STRATEGIES.put(6766, new LizardMan());
		STRATEGIES.put(499, new Thermonuclear());
		STRATEGIES.put(7286, new Skotizo());
		STRATEGIES.put(5886, new Sire());
		STRATEGIES.put(10126, new UnholyCursebearer());
		
		STRATEGIES.put(5996, new Glod());

		/*
		 * new npc
		 */


		
		
		/*
		* end of new npc's added by live:nrpker7839
		*/
		//STRATEGIES.put(6208, new BalfrugKreeyath());
		STRATEGIES.put(9855, new Lexicus());
		STRATEGIES.put(10745, new AnimatedBook());
		STRATEGIES.put(5421, new MutantTarn());
		STRATEGIES.put(4972, new BlueBirdThingy());
		STRATEGIES.put(7553, new TheGeneral());
		STRATEGIES.put(10051, new IceDemon());
		STRATEGIES.put(9176, new skeletalHorror());
		STRATEGIES.put(8597, new AvatarOfCreation());
		STRATEGIES.put(8596, new AvatarOfCreation());
		STRATEGIES.put(433, new Nomad());
		STRATEGIES.put(4413, new DireWolf());
		STRATEGIES.put(6305, new Dragonix());
		STRATEGIES.put(10141, new BallakPummeler());
		STRATEGIES.put(10039, new ToKashBloodchiller());
		STRATEGIES.put(6307, new ZamorakIktomi());
		STRATEGIES.put(6309, new ZamorakLefosh());
		STRATEGIES.put(6313, new ArmadylAbyzou());
		STRATEGIES.put(9357, new Vladimir());
		STRATEGIES.put(839, new MiniDire());
		STRATEGIES.put(509, new Nazastarool());
		STRATEGIES.put(433, new Cyrisus());
		STRATEGIES.put(6303, new Abbadon());
		STRATEGIES.put(9911, new HarLakkRiftsplitter());
		STRATEGIES.put(11751, new Necrolord());
		STRATEGIES.put(5886, new AbyssalSire());
		STRATEGIES.put(1857, new AvatarOfMagic());
		STRATEGIES.put(1854, new AvatarOfRange());
		STRATEGIES.put(8771, new ChaosDwogre());
		STRATEGIES.put(6368, new ZamorakianMage());
		STRATEGIES.put(6618, new CrazyArcheologist());
		STRATEGIES.put(6619, new ChaosFanatic());
		STRATEGIES.put(8776, new DwarvenHandCannoneer());
		STRATEGIES.put(2882, new DagannothPrime());
		STRATEGIES.put(9766, new Sagittare());
		STRATEGIES.put(2001, new Scorpia());
		STRATEGIES.put(6368, new ZamorakianMage());

		
		STRATEGIES.put(9772, new Rammernaut());


		STRATEGIES.put(7700, new JalTokJad());
		STRATEGIES.put(7702, new JalXil1());
		STRATEGIES.put(7703, new JalZek1());
		STRATEGIES.put(7750, new Healer());
	}
	
	public static CombatStrategy getStrategy(int npc) {
		if(STRATEGIES.get(npc) != null) {
			return STRATEGIES.get(npc);
		}
		return defaultMeleeCombatStrategy;
	}
	
	public static CombatStrategy getDefaultMeleeStrategy() {
		return defaultMeleeCombatStrategy;
	}

	public static CombatStrategy getDefaultMagicStrategy() {
		return defaultMagicCombatStrategy;
	}


	public static CombatStrategy getDefaultRangedStrategy() {
		return defaultRangedCombatStrategy;
	}
}
