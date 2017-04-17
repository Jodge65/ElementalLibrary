package fr.Jodge.elementalLibrary.data.interfaces;

import io.netty.buffer.ByteBuf;

import com.google.gson.JsonObject;

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
	 * @param obj <i>Object</i>
	 */
	public void autoUpdate(Object obj);
}
