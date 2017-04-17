package fr.Jodge.elementalLibrary.data.stats;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import fr.Jodge.elementalLibrary.ElementalConfiguration;
import fr.Jodge.elementalLibrary.Main;
import fr.Jodge.elementalLibrary.log.JLog;

public class MonsterStats extends AbstractStats
{	
	public static final String folder = "monsterdata\\";
	
	public int id;
	public boolean isDefaultStats;
	
	public MonsterStats(int id)
	{
		super(Main.constante.MONSTER_STATS);
		this.id = id;
	}
	
	public MonsterStats(EntityLivingBase entity) 
	{
		this(entity.getEntityId());
		this.obj = entity;
		
		MinecraftServer server = entity.getServer();
		String subFolder = 	entity.getClass().getSimpleName() + "\\";
		String url = folder + subFolder;
		data = null;

		// check if entity has custom name tag
		if(entity.hasCustomName())
		{
			// if yes, then we try whit custom name
			url += entity.getCustomNameTag() + ElementalConfiguration.EXTENTION;
			data = server.getActiveAnvilConverter().getFile(server.getFolderName(), url);
			
			if(!data.exists())
			{
				JLog.info("Custom File " + data.getAbsolutePath() + " not exist. Defautl file will be used");
				data = null;
			}
			else
			{
				JLog.info("Custom File " + data.getAbsolutePath() + " exist. It will be used.");
				this.isDefaultStats = false;
			}
		}
		
		
		if(data == null)
		{
			url = folder + subFolder + "default" + ElementalConfiguration.EXTENTION;
			data = server.getActiveAnvilConverter().getFile(server.getFolderName(), url);
			this.isDefaultStats = true;
		}

		JLog.info("Make monster Stats for " + entity.getClass() + " by file.");
		makeByFile();
	}
	
}
