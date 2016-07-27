package fr.Jodge.elementalLibrary.data.interfaces;

import com.google.gson.JsonObject;

import net.minecraft.entity.Entity;
import io.netty.buffer.ByteBuf;

public interface IElementalWritable 
{
	/**
	 * initialize the empty object based on JSon object read in file.
	 * @param value <i>JsonObject<i> buf where are read information
	 */
	public void fromJsonObject(JsonObject value);
	
	/**
	 * create a JSonObject that can be read by fromJsonObject.
	 * Use to save
	 */
	public JsonObject toJsonObject();
	
	/**
	 * buf where are write data. Clien receive this value and recreate object.
	 * @param buf <i>ByteBuf</i>
	 */
	public void toByte(ByteBuf buf);
	
	/**
	 * initialize the empty object based on data send.
	 * @param buf <i>ByteBuf<i> buf where are read information
	 */
	public void fromByte(ByteBuf buf);
	
	/**
	 * Use to auto update value by entity.
	 * @param entity <i>Entity</i>
	 */
	public void autoUptdate(Entity entity);
}
