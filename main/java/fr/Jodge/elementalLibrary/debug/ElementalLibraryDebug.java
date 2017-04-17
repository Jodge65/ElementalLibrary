package fr.Jodge.elementalLibrary.debug;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import net.minecraft.entity.EntityLivingBase;
import fr.Jodge.elementalLibrary.log.JLog;

public class ElementalLibraryDebug extends JFrame
{
	private static final long serialVersionUID = 1L;

	protected static final int debugSpeed = 1000;
	protected static int actualSize;
	protected static int maxSize;
	
	public static Thread elementUpdate;

	public static JTextArea log, caseElementID, caseElementName, caseElementActive, entityData, playerData;
	public static JScrollPane logScroll, entityScroll, playerScroll;
	public static JButton elementForceUpdate;
	public static JPanel elementPanel;
	public static JSplitPane caseOneAndTwo, allTab, entitySplit;

	protected static EntityLivingBase entity, player;
	
	public ElementalLibraryDebug()
	{
		super("ElementalLibrary Debug");
		addWindowListener(new ActionClose());
		
		//setUndecorated(true);
		
		actualSize = 0;
		maxSize = 65536;
		
		log = new JTextArea();
		log.setEditable(false);
		
		logScroll = new JScrollPane(log, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		logScroll.setPreferredSize(new Dimension(1200, 200));
		
		elementPanel = new JPanel();
		elementPanel.setLayout(new BorderLayout());
		
		elementForceUpdate = new JButton("Force Update");
		elementForceUpdate.addActionListener(new ElementForceUpdate());

		caseElementID = new JTextArea();
		caseElementID.setEditable(false);

		caseElementName = new JTextArea();
		caseElementName.setEditable(false);

		caseElementActive = new JTextArea();
		caseElementActive.setEditable(false);

		playerData = new JTextArea();
		playerData.setEditable(false);
		playerScroll = new JScrollPane(playerData, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		playerScroll.setPreferredSize(new Dimension((1200 - (75*3)) / 2, 400));
		
		entityData = new JTextArea();
		entityData.setEditable(false);
		entityScroll = new JScrollPane(entityData, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		entityScroll.setPreferredSize(new Dimension((1200 - (75*3)) / 2, 400));
		
		caseOneAndTwo = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, caseElementID, caseElementName);
		allTab = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, caseOneAndTwo, caseElementActive);
		allTab.setPreferredSize(new Dimension(75*3, 600));

		entitySplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, playerScroll, entityScroll);
		
		getContentPane().add(entitySplit, BorderLayout.CENTER);
		getContentPane().add(logScroll, BorderLayout.SOUTH);
		getContentPane().add(elementPanel, BorderLayout.EAST);
		elementPanel.add(allTab, BorderLayout.CENTER);
		elementPanel.add(elementForceUpdate, BorderLayout.SOUTH);

		// thread
		elementUpdate = new ElementDebugUptade();
		elementUpdate.start();
		
		pack();
		setVisible(true);
	}
	
	
	public void addLine(String line)
	{
		actualSize += line.length() + 1;
		log.setText(log.getText() + "\n" + line);
		if(actualSize > maxSize)
		{
			int surplus = actualSize - maxSize;
			try 
			{
				log.setText(log.getText(surplus, maxSize));
			}
			catch(Exception e){}
		}
		log.setCaretPosition(log.getDocument().getLength());
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
					writeStream.writeChars(log.getText());
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
		@Override
		public synchronized void windowClosing(WindowEvent arg)
		{
			saveLog("historique.log");
		}
	}
	
	private class ElementForceUpdate implements ActionListener
	{ 
		@Override
		public synchronized void actionPerformed(ActionEvent e)
		{
			ElementDebugUptade.updateElement();
		}
	}

	public void displayEntity(EntityLivingBase target) 
	{
		entity = target;
	}
	
}