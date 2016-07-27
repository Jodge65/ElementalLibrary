package fr.Jodge.elementalLibrary;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import fr.Jodge.elementalLibrary.function.JLog;

public class ElementalLibraryDebug extends JFrame
{
	private static final long serialVersionUID = 1L;

	public static JTextArea text;
	public static JScrollPane textScrollBar;
	protected static int actualSize;
	protected static int maxSize;
	
	public ElementalLibraryDebug()
	{
		super("ElementalLibrary Debug");
		addWindowListener(new ActionClose());
		actualSize = 0;
		text = new JTextArea();
				
		textScrollBar = new JScrollPane(text, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		textScrollBar.setPreferredSize(new Dimension(800, 400));

		
		text.setEditable(false);
		getContentPane().add(textScrollBar);
		
		pack();
		setVisible(true);
	}
	
	public void addLine(String line)
	{
		actualSize += line.length();
		if(actualSize > maxSize)
		{
			
		}	
			
		text.setText(text.getText() + "\n" + line);
		
	}
	
	public void saveLog(String name)
	{
		boolean succes = true;
		File file = new File(name);
		ObjectOutputStream writeStream = null;
		if(file.exists())
			file.delete();
		
		try
		{
			file.createNewFile();
		}
		catch (IOException e)
		{
			succes = false;
			JLog.error("Can't create file " + name + ".");
		}
		if(succes)
		{
			try
			{
				writeStream = new ObjectOutputStream(new FileOutputStream(file));
			}
			catch (IOException e)
			{
				succes = false;
				JLog.error("Can't open file " + name + ".");
			}
			if(succes)
			{
				try
				{
					writeStream.writeChars(text.getText());
				}
				catch (IOException e)
				{
					succes = false;
					JLog.error("Can't write on file " + name + ".");
				}
				
				if( writeStream != null)
				{
					try
					{
						writeStream.flush();
						writeStream.close();
					}
					catch (IOException e)
					{
						succes = false;
						JLog.error("Can't close file " + name + ". Maybe Save don't work...");
					}
				}
			}
		}

	}
	private class ActionClose extends WindowAdapter
	{
		//@Override
		public synchronized void windowClosing()
		{
			saveLog("historique.log");
		}
	}
}