package fr.Jodge.elementalLibrary;

import fr.Jodge.elementalLibrary.data.ElementalDataSerializers;
import fr.Jodge.elementalLibrary.event.DamageEvent;
import fr.Jodge.elementalLibrary.event.DataEvent;
import fr.Jodge.elementalLibrary.function.JLog;
import fr.Jodge.elementalLibrary.proxy.CommonProxy;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
/**
 * @author Jodge
 * 
 */
@Mod(modid = Main.MODID, name = Main.MODNAME, version = Main.MODVER, acceptedMinecraftVersions = "[1.10]")
public class Main
{
	public static final String MODID = "elLibrary";
	public static final String MODNAME = "Elemental Library";
	public static final String MODVER = "A0.1.0";
		
	public static ConfigurationHelper configFile;
	public static ElementalLibraryDebug DEBUG_WINDOWS;
	
	// this is needed on server side to force class to be loaded.
	public static DataSerializer ELEMENTAL_SERIALIZER = ElementalDataSerializers.ELEMENTAL_SERIALIZER;
	
	@Mod.Instance(Main.MODID)
	public static Main instance;
	
	@SidedProxy(clientSide = "fr.Jodge.elementalLibrary.proxy.ClientProxy", serverSide = "fr.Jodge.elementalLibrary.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		
		configFile = ConfigurationHelper.getInstance(event);
		configFile.onPreInit();
		
		if(ElementalConfiguration.WINDOW_DEBUG)
			DEBUG_WINDOWS = new ElementalLibraryDebug();

		// Pretty useless but... So beautiful :D... Okay i take the door :(
		JLog.write("[ ### --- INFO --- ### ]");
		JLog.write("Name : " + MODNAME);
		JLog.write("Modid : " + MODID);
		JLog.write("ModVer : " + MODVER);
		JLog.write("Author : " + getAuthor());
		JLog.write("e-mail : " + getContact());
		JLog.write("[ ### ---        --- ### ]");
		JLog.write("");
		
		ElementalConstante.init.onPreLoad();
		
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		ElementalConstante.init.onLoad();
		
		proxy.registerRender();
		
		MinecraftForge.EVENT_BUS.register(new DamageEvent());
		JLog.info("New event have been register for DamageEvent");

		MinecraftForge.EVENT_BUS.register(new DataEvent());
		JLog.info("New event have been register for DataEvent");

		configFile.onInit();
		
		if (event.getSide().isClient()) // Client
		{
		}
		else // Serveur
		{
		}
		

	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		ElementalConstante.init.onAfterLoad();

		
		configFile.onPostInit();

		JLog.info(" --- ENDINIT --- ");
	}

	protected static String getAuthorMinecraftName()
	{
		return "Jodge65";
	}
	protected static String getAuthor()
	{
		return "Jodge";
	}
	protected static String getContact()
	{
		return "joey.sarie@gmail.com";
	}
}