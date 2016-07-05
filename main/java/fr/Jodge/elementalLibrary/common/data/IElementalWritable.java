package fr.Jodge.elementalLibrary.common.data;

import io.netty.buffer.ByteBuf;

public interface IElementalWritable 
{
	/**
	 * return a string that can clone current object
	 * @return <i>String</i>
	 */
	public String write();
	
	/**
	 * clone object based on the string.
	 * @param value <i>String<i> value used to saved onfile
	 * @return <i>IElementalWritable</i> new object based on string
	 */
	public IElementalWritable createByString(String value); 
	
	/**
	 * buf where are write data. Clien receive this value and recreate object.
	 * @param buf <i>ByteBuf</i>
	 */
	public void toByte(ByteBuf buf);
	
	/**
	 * recreate object based on data send.
	 * @param buf <i>ByteBuf<i> buf where are read information
	 * @return <i>IElementalWritable</i> new object
	 */
	public IElementalWritable fromByte(ByteBuf buf); 
}
