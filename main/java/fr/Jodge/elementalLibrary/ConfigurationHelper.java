package fr.Jodge.elementalLibrary;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import com.google.common.io.Files;

import fr.Jodge.elementalLibrary.function.JLog;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ConfigurationHelper 
{
	protected static ConfigurationHelper _INSTANCE = new ConfigurationHelper();
	protected static FMLPreInitializationEvent event;
	protected static boolean isDirty = true;
	

	Configuration mainFile;
	
	public String catGeneral = "Generale";
	public String catElement = "Elements";
	public String catServer = "Server";

	protected ConfigurationHelper(){}
	
	/**
	 * Use this to get configuration
	 * @return instance of configuration.
	 */
	public static ConfigurationHelper getInstance()
	{
		if(isDirty)
		{
			JLog.warning("Instance of Configuration is not existing. Please try again.");
			return null;
		}
		else
		{
			return _INSTANCE;
		}
	}
	
	/**
	 * This is use only by ElementalLibrary ! Don't use this anymore except if you want bug !
	 * @param e
	 * @return
	 */
	public static ConfigurationHelper getInstance(FMLPreInitializationEvent e)
	{
		event = e;
		isDirty = false;
		return getInstance();
	}
	
	
	

	public ConfigurationHelper onPreInit() 
	{
		File main = new File(event.getModConfigurationDirectory() + "/" + Main.MODID + "/main.cfg");
		
		try 
		{
			Files.createParentDirs(main);
		} 
		catch (IOException e) 
		{
			JLog.error("Fail to create config directory.");
		}
		
		mainFile = new Configuration(main);
		mainFile.load();
		
		mainFile.addCustomCategoryComment(this.catGeneral, "Everything about main configuration [Server & Client].");
		
		ElementalConfiguration.DEBUG = mainFile.getBoolean("Debug", this.catGeneral, false, 
				"Set this to True to enable log.");
		ElementalConfiguration.WINDOW_DEBUG = mainFile.getBoolean("Window_Debug", this.catGeneral, false, 
				"Set this to True to enable external log window (may not work on server side).\n"
				+	"Do not use if you don't know what you do !");
		ElementalConfiguration.JLOG_LEVEL = mainFile.getInt("Log_Level", this.catGeneral, 0, 0, 4, 
				"Define log level. Need debug = true to work.\n"
				+ 	" - 0 : Nothing\n"
				+ 	" - 1 : Error\n"
				+ 	" - 2 : Warngin\n"
				+ 	" - 3 : Alerte\n"
				+ 	" - 4 : Info\n"
				);
		
		mainFile.addCustomCategoryComment(this.catServer, "Everything about Server configuration [Server] (useless if you play in multi).");
		ElementalConfiguration.USE_RAW_JSON = mainFile.getBoolean("Raw_Json", this.catServer, false, 
				"Set this to True to use raw method for Json save.");
		ElementalConfiguration.EXTENTION = mainFile.getString("Extention", this.catServer, ".eld", 
				"Change default extention name.\n"
				+	"Warning : each file whit old extention will never be used again ! Player stats will be reset !"
						, Pattern.compile("^\\.[a-zA-Z]+")
		);

		if(event.getSide().isServer() && ElementalConfiguration.WINDOW_DEBUG)
		{
			ElementalConfiguration.WINDOW_DEBUG = false;
			JLog.alert("No Windows Debug Available in Server Mode. WINDOW_DEBUG was set to false to prevent bug.");
		}
		
		return this;
	}
	
	protected ConfigurationHelper onInit()
	{
		
		return this;
	}
	
	protected ConfigurationHelper onPostInit()
	{
		
		if(mainFile.hasChanged())
		{
			mainFile.save();
		}

		return this;
	}
	
}
