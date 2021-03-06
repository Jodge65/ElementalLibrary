package fr.Jodge.elementalLibrary.data.stats;

import io.netty.buffer.ByteBuf;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jline.internal.Log;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fr.Jodge.elementalLibrary.ElementalConfiguration;
import fr.Jodge.elementalLibrary.data.interfaces.IElementalWritable;
import fr.Jodge.elementalLibrary.log.ElementalCrashReport;
import fr.Jodge.elementalLibrary.log.JLog;

public abstract class AbstractStats
{
	protected Map<Class, IElementalWritable> value;
	
	public List<Class<? extends IElementalWritable>> listOfAvailableStats;
	
	public Object obj;
	public File data;
	
	/** private state that refer to file success (or not) */
	protected boolean succes;
	
	public ByteBuf buf;

	public AbstractStats(List<Class<? extends IElementalWritable>> LIST_OF_STATS)
	{
		this.value = new HashMap<Class, IElementalWritable>();
		this.succes = true;
		this.listOfAvailableStats = LIST_OF_STATS;
	}
	
	public List<Class<? extends IElementalWritable>> getListOfAvailableStats(){return this.listOfAvailableStats;}
	
	protected void makeByFile()
	{
		if(data != null)
		{
			if(obj != null)
			{
				if(!data.exists())
				{
					// if file not exist, then create new player
					CreateNew();
					JLog.info("Default File not already exist for " + obj.getClass() + ". It will be generated.");
				}

				// when file exist : read it !
				ReadFromFile();
				if(succes)
				{
					JLog.info("Succefuly load data from " + data.getAbsolutePath());
				}
				else
				{
					Log.warn("Something wrong happen whit file " + data.getAbsolutePath() + ". Data will be reset.");
					succes = true;
					CreateNew();
				}
			}
			else
			{
				JLog.error("Try to make monster stats by file for null obj...");
			}
		}
		else
		{
			JLog.error("Data file is not initialized...");
		}
	}
	
	/**
	 * Try to use all default constructor available in listOfAvailableStats
	 * @param data <i>File</i>
	 * @param doINeedSave <i>boolean</i> put this to false to not save file. Unsaved file will be generated each time needed...
	 */
	protected boolean CreateNew(boolean doINeedSave) 
	{
		for(Class<? extends IElementalWritable> clazz : listOfAvailableStats)
		{
			try 
			{
				IElementalWritable objet = clazz.newInstance();
				objet.autoUpdate(obj);
				value.put(clazz, objet);
			}  
			catch (Exception e) 
			{
				JLog.error("Can't create and use default constructor for " + clazz);
			}
		}
		
		if(doINeedSave)
		{
			JLog.info("New Stats for " + obj.getClass() + " was save because doIneedSave is true.");
			save();
		}
		
		return true;

	}

	protected boolean CreateNew() 
	{
		return CreateNew(true);
	}
	
	/**
	 * Save the current value in the file
	 * @param data <i>File</i>
	 */
	public boolean save() 
	{
		if(data != null)
		{
			// if data already exist, delete.
			if(data.exists())
			{
				data.delete();
			}

			
			// start save
			try
			{
				Files.createParentDirs(data);
				data.createNewFile();
			}
			catch (IOException e)
			{
				succes = false;
				JLog.warning("File " + data.getAbsolutePath() + " can't be create.");
			}
			
			// if save is create
			if(succes)
			{
				FileWriter writeStream = null;

				try 
				{
					writeStream = new FileWriter(data);
				} 
				catch (IOException e) 
				{
					succes = false;
					JLog.error("Can't create an FileWriter for file " + data.getName());
				}
				
				// if writer is ready
				if(succes)
				{

					JsonObject finalObject = new JsonObject();
					for(Class<? extends IElementalWritable> clazz : listOfAvailableStats)
					{
						IElementalWritable instance = value.get(clazz);
						if(instance != null)
						{
							JsonObject jsonValue = instance.toJsonObject();
							finalObject.add(clazz.getName(), jsonValue);
						}

					}

					String finalString = finalObject.toString();
					
					if(!ElementalConfiguration.USE_RAW_JSON)
					{
						Gson gson = new GsonBuilder().setPrettyPrinting().create();
						JsonParser jsonParser = new JsonParser();
						
						JsonElement writableElement = jsonParser.parse(finalString);
						finalString = gson.toJson(writableElement);	
					}
					
					try 
					{
						writeStream.write(finalString);
					} 
					catch (IOException e)
					{
						JLog.warning("Can't write on file " + data.getName());
					}
					
					if(succes)
					{
						try 
						{
							writeStream.flush();
							writeStream.close();
							JLog.info("File " + data.getName() + " is now save for  " + obj.getClass());
						} 
						catch (IOException e) 
						{
							succes = false;
							JLog.error("Can't save file " + data.getName());
						}
						if(succes)
						{
							JLog.info("Succefuly save data for " + obj.getClass() + " in file " + data.getName());
						}
					}
					try
					{
						writeStream.close();
					}
					catch (IOException e)
					{
						JLog.warning("File isn't close. You may have memory issue if this problem happen often. File concern : " + data.getName());
					}
				} // end of if write stream if create
			} // end of if file is created
		}
		else // else data == null
		{
			succes = false;
			JLog.error("Can't save file because no file was initialized...");
		}
		
		
		return succes;
	} // end of save
	
	/**
	 * Initialize current object whit value read in file
	 * @param data <i>File</i>
	 */
	protected void ReadFromFile() 
	{
		FileReader readStream = null;

		try 
		{
			readStream = new FileReader(data);
		} 
		catch (IOException e) 
		{
			succes = false;
			JLog.error("Can't create an FileReader for file " + data.getName());
		}
		
		if(succes)
		{
			JsonParser parser = new JsonParser();
					
			JsonObject finalObject = (JsonObject) parser.parse(readStream);
			// if we finaly get the json file !
			if(succes)
			{
				// for each object... I guess
				for(Class<? extends IElementalWritable> clazz : listOfAvailableStats)
				{
					// we want a class that extend IElementalWritable
					
					// we use class name to send
					String key = clazz.getName();
					// get current object if exist...
					JsonObject currentObject = finalObject.getAsJsonObject(key);
					
					if(currentObject != null)
					{
						IElementalWritable objet;
						try 
						{
							objet = clazz.newInstance();
							objet.fromJsonObject(currentObject);
							add(clazz, objet);
						}
						catch (Exception e) 
						{
							JLog.error("Somethink wrong happen when try to create new instance of " + clazz.getName());
						}
					}
					else
					{
						JLog.error("Data File need to be update. Key " + key + " doensn't exist...");
					}
				} // for each class
			} // if succes
		}
		
		
	}
	
	/**
	 * 
	 * @param key <i>integer</i> key to put value
	 * @param obj <i>IElementalWritable</i> instance of writable element
	 * @return itself
	 */
	public AbstractStats add(Class key, IElementalWritable obj)
	{
		value.put(key, obj);
		return this;
	}
	
	/**
	 * 
	 * @param key <i>integer</i> key to get
	 * @return <i>IElementalWritable</i> instance of something writable
	 */
	public IElementalWritable getStat(Class key)
	{
		try
		{
			return value.get(key);
		}
		catch (Throwable throwable) 
		{
			String text = "The class named " + key + " doesn't exist in current class.";
			ElementalCrashReport.crashReport(throwable, text);
		}
		return null;
	}
	
	@Deprecated
	public IElementalWritable get(Class key)
	{
		return getStat(key);
	}
	
	@Override
	public String toString()
	{
		String text = "";
		
		for(Class<? extends IElementalWritable> clazz : listOfAvailableStats)
		{

			text += getStat(clazz).toString() + "\n";
		}
		
		return text;
	}

}
