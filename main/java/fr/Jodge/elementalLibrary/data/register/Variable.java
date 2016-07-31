package fr.Jodge.elementalLibrary.data.register;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.network.datasync.DataParameter;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import fr.Jodge.elementalLibrary.Main;
import fr.Jodge.elementalLibrary.data.element.Element;
import fr.Jodge.elementalLibrary.data.entity.AbstractStats;
import fr.Jodge.elementalLibrary.data.interfaces.IElementalWritable;
import fr.Jodge.elementalLibrary.data.matrix.AttackMatrix;
import fr.Jodge.elementalLibrary.data.matrix.DamageMatrix;
import fr.Jodge.elementalLibrary.data.matrix.DefenceMatrix;

public class Variable 
{

	/** Map of each kind of existing stats */
	public static Map<Class, Map<Class, DataParameter>> STATS;
	
	/** List of stats available for player, and default value. */
	public static List<Class<? extends IElementalWritable>> PLAYER_STATS;
	/** List of stats available for monster. Value here is just use to have access to some value */
	public static List<Class<? extends IElementalWritable>> MONSTER_STATS;

	/** Save Default monster Stats to use it again instead of ask server/read file  */
	public static Map<Class, AbstractStats> DEFAULT_STATS;

	public static SimpleNetworkWrapper STATS_SOCKET;
	public static String STATS_ID = Main.MODID;
	
	public static Map<String, Element> DEFAULT_ELEMENT_DAMAGE_SOURCES;
	
	public static Map<String, DamageMatrix> WEAPONS_DAMAGE_BRUTE;
	public static Map<Class, AttackMatrix> ENTITY_DAMAGE_PERCENT;
	public static Map<Class, DefenceMatrix> ENTITY_RESISTANCE_PERCENT;
}
