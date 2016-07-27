package fr.Jodge.elementalLibrary.data.entity;

import io.netty.buffer.ByteBuf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jline.internal.Log;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fr.Jodge.elementalLibrary.ElementalConfiguration;
import fr.Jodge.elementalLibrary.ElementalConstante;
import fr.Jodge.elementalLibrary.data.interfaces.IElementalWritable;
import fr.Jodge.elementalLibrary.function.JLog;

public abstract class AbstractStats
{
	/** integer refers to one of int ElementalConstante.CONSTANTE */
	protected Map<Class, IElementalWritable> value;
	
	public List<Pair<Class, IElementalWritable>> listOfAvailableStats;
	
	public Entity entity;
	public File data;
	
	/** private state that refer to file success (or not) */
	protected boolean succes;
	
	public ByteBuf buf;

	public AbstractStats(List<Pair<Class, IElementalWritable>> LIST_OF_STATS)
	{
		this.value = new HashMap<Class, IElementalWritable>();
		this.succes = true;
		this.listOfAvailableStats = LIST_OF_STATS;
	}
	
	public List<Pair<Class, IElementalWritable>> getListOfAvailableStats(){return this.listOfAvailableStats;}
	
	protected void makeByFile()
	{
		if(data != null)
		{
			if(!data.exists())
			{
				// if file not exist, then create new player
				CreateNew();
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
		for(Pair<Class, IElementalWritable> coupleOfValue : listOfAvailableStats)
		{
			try 
			{
				JsonObject jsonObjet = coupleOfValue.getValue().toJsonObject();
				IElementalWritable objet = coupleOfValue.getValue().getClass().getConstructor().newInstance();
				objet.fromJsonObject(jsonObjet);
				value.put(coupleOfValue.getKey(), objet);
			}  
			catch (Exception e) 
			{
				JLog.error("Can't create and use default constructor for " + coupleOfValue.getValue());
			}
		}
		
		if(doINeedSave)
			save();
		
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
					
					for(Pair<Class, IElementalWritable> coupleOfValue : listOfAvailableStats)
					{
						String key = coupleOfValue.getValue().getClass().getName();
						JsonObject value = coupleOfValue.getValue().toJsonObject();
						finalObject.add(key, value);
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
						} 
						catch (IOException e) 
						{
							succes = false;
							JLog.error("Can't save file " + data.getName());
						}
						if(succes)
						{
							JLog.info("Succefuly save data for at " + data.getName());

						}
					}
				}
			}
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
					
			JsonObject finalobject = (JsonObject) parser.parse(readStream);

			// if we finaly get the json file !
			if(succes)
			{
				// for each object... I guess
				for(Pair<Class, IElementalWritable> coupleOfValue : listOfAvailableStats)
				{
					// we want a class that extend IElementalWritable
					Class<? extends IElementalWritable> currentClass = coupleOfValue.getValue().getClass();
					
					// we use class name to send
					String key = currentClass.getName();
					// get current object if exist...
					JsonObject currentObject = finalobject.getAsJsonObject(key);
					
					if(currentObject != null)
					{
						IElementalWritable objet;
						try 
						{
							objet = currentClass.getConstructor().newInstance();
							objet.fromJsonObject(currentObject);
							add(coupleOfValue.getKey(), objet);
						}
						catch (Exception e) 
						{
							JLog.error("Somethink wrong happen during when try to create new instance of " + currentClass.getName());
						}
					}
					else
					{
						JLog.error("Data File need to be update. Key " + key + " doensn't exist...");
					}

				}
			}
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
		return value.getOrDefault(key, null);
	}
	
	@Override
	public String toString()
	{
		String text = "";
		
		for(Pair<Class, IElementalWritable> coupleOfValue : listOfAvailableStats)
		{
			text += getStat(coupleOfValue.getKey()).toString() + "\n";
		}
		
		return text;
	}

	public IElementalWritable get(Class key)
	{
		return value.getOrDefault(key, null);
	}
}
