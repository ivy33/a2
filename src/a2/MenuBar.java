package a2;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.io.*;

import javax.swing.filechooser.FileFilter;

/**
 * DemoFileMenu - demo of a typical implementation of a File menu.
 * <p>
 * 
 * The most common items in a File menu are New..., Open..., Close, Save, Save as..., and Exit. The
 * behaviour of these is fairly standardized across applications, and this demo attempts to mimic
 * this behaviour.
 * <p>
 * 
 * When a file is opened, its contents are read into a <code>JEditorPane</code> for viewing,
 * editing, etc.
 * <p>
 * 
 * As evident from the screen snaps below, a variety of state information must be maintained for
 * proper operation of a File menu; e.g., whether or not a file is currently open, whether any
 * changes have been introduced on the current file, etc. Depending on such information, certain
 * menu items must be enabled or disabled to correctly reflect the available operations.
 * <p>
 * 
 * Screen snap upon launch, showing File menu...<br>
 * <center><img src="DemoFileMenu-1.gif"></center>
 * <p>
 * 
 * Screen snap showing File menu after opening a file...<br>
 * <center><img src="DemoFileMenu-2.gif"></center>
 * <p>
 * 
 * @see <a href="DemoFileMenu.java">source code</a>
 * @author Scott MacKenzie, 2009-2015
 */
public class MenuBar extends JMenu implements ActionListener, KeyListener
{
	// the following avoids a "warning" with Java 1.5.0 complier (?)
	static final long serialVersionUID = 42L;

	final String TITLE = "FileMenu";

	JMenuItem newFile;
	JMenuItem open;
	JMenuItem close;
	JMenuItem save;
	JMenuItem saveAs;
	JMenuItem exit;

	File f;
	JFileChooser fc;
	JScrollPane sp;
	JEditorPane editArea;
	JPanel blank;

	boolean dataChanged; // keep track of whether changes have been made

	public MenuBar(String fileName)
	{
		// -------------------------------
		// create and configure components
		// -------------------------------

		// create a text area to view and edit the file


		newFile = new JMenuItem("New", new ImageIcon("new.gif"));
		newFile.setMnemonic(KeyEvent.VK_N);
		newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));

		open = new JMenuItem("Open...", new ImageIcon("open.gif"));
		open.setMnemonic(KeyEvent.VK_O);
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));

		close = new JMenuItem("Close", new ImageIcon("close.gif"));
		close.setMnemonic(KeyEvent.VK_C);
		close.setEnabled(false);

		save = new JMenuItem("Save", new ImageIcon("save.gif"));
		save.setMnemonic(KeyEvent.VK_S);
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		save.setEnabled(false);

		saveAs = new JMenuItem("Save as...", new ImageIcon("blank.gif"));
		saveAs.setMnemonic(KeyEvent.VK_A);
		saveAs.setEnabled(false);

		exit = new JMenuItem("Exit", new ImageIcon("blank.gif"));
		exit.setMnemonic(KeyEvent.VK_X);

		this.add(newFile);
		this.add(open);
		this.add(close);
		this.addSeparator();
		this.add(save);
		this.add(saveAs);
		this.addSeparator();
		this.add(exit);

		// set up a file chooser to present *.txt and *.java files
		// in the current directory

		fc = new JFileChooser(new File("."));
		final String[] EXTENSIONS = { ".txt", ".java" };
		fc.addChoosableFileFilter(new MyFileFilter(EXTENSIONS));

		// -------------
		// add listeners
		// -------------

		newFile.addActionListener(this);
		open.addActionListener(this);
		close.addActionListener(this);
		save.addActionListener(this);
		saveAs.addActionListener(this);
		exit.addActionListener(this);

		// Note: a KeyListener will be added whenever new editing begins.
		// The 'dataChanged' flag will be set in the actionPerformed
		// method with the first keystroke, and then the KeyListener will
		// be removed. The next time new editing begins, the KeyListener
		// will be added again, and so on.

		// put components in a panel

		JPanel p = new JPanel(new BorderLayout());
		p.add(sp, "Center");

		// make panel this JFrame's content pane

		this.setContentPane(p);

		// open the file specified on the command line, if any

		if (fileName != null)
		{
			f = new File(fileName);
			openFile(f);
		}
	}

	// -------------------------------
	// implement ActionListener method
	// -------------------------------

	@Override
	public void actionPerformed(ActionEvent ae)
	{
		Object source = ae.getSource();

		if (source == newFile)
		{
			editArea.setText("");
			editArea.addKeyListener(this);
			dataChanged = false;
			this.setTitle("untitiled - " + TITLE);
			sp.setViewportView(editArea);
			editArea.requestFocus();
			editArea.setCaretPosition(0);
			close.setEnabled(true);
			save.setEnabled(true);
			saveAs.setEnabled(true);
		}

		else if (source == open)
		{
			// show the file chooser 'open' dialog box and get user response

			if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
			{
				f = fc.getSelectedFile();
				openFile(f);
			}
		}

		else if (source == save)
		{
			// If the edit area was loaded from a file, save the changes.
			// If the edit area was loaded from the "New" menu item,
			// get a filename from the user before saving. To do this,
			// just invoke 'doClick' on the "Save as..." component. This
			// will fire an ActionEvent, and the filename will be retrieved
			// from the corresponding code in the actionPerformed method;
			// that is, in the code corresponding to "source == saveAs".

			if (f != null)
				saveToFile(f);
			else
				saveAs.doClick();
		}

		else if (source == saveAs)
		{
			// show the file chooser 'save' dialog box and get user response

			if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
			{
				f = fc.getSelectedFile();

				// check if file exists before overwriting
				// (Note: Dialog only pops up if file exists)

				if (!f.exists() || okToReplace(f))
					saveToFile(f);
			}
		}

		else if (source == close)
		{
			int blah = changesShouldBeSaved();
			if (dataChanged && blah == JOptionPane.YES_OPTION)
				if (f != null)
					saveToFile(f);
				else
					saveAs.doClick();
			else if (blah == JOptionPane.CANCEL_OPTION)
				return;

			f = null;
			this.setTitle(TITLE);
			editArea.setText("");
			close.setEnabled(false);
			save.setEnabled(false);
			saveAs.setEnabled(false);
			sp.setViewportView(blank);
		}

		else if (source == exit)
			System.exit(0);
	}

	private boolean okToReplace(File f)
	{
		final Object[] options = { "Yes", "No", "Cancel" };
		return JOptionPane.showOptionDialog(this, "The file '" + f.getName() + "' already exists.  "
				+ "Replace existing file?", "Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE,
				null, options, options[2]) == JOptionPane.YES_OPTION;
	}

	private int changesShouldBeSaved()
	{
		final Object[] options = { "Yes", "No", "Cancel" };
		return JOptionPane.showOptionDialog(this, "Save changes before closing?", "Close",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
	}

	// -----------------------------
	// implement KeyListener methods
	// -----------------------------

	@Override
	public void keyPressed(KeyEvent ke)
	{
	}

	@Override
	public void keyReleased(KeyEvent ke)
	{
	}

	// The following is only needed for the first editing keystroke.
	// Set the 'dataChanged' flag and then remove the KeyListener.
	// This avoids an endless stream of calls to the keyTyped
	// method during editing.

	@Override
	public void keyTyped(KeyEvent ke)
	{
		dataChanged = true;
		editArea.removeKeyListener(this);
	}

	// -------------
	// other methods
	// -------------

	private void openFile(File fArg)
	{
		try
		{
			editArea.read(new FileInputStream(fArg), null);
		} catch (IOException e)
		{
			popupError("Error reading file '" + fArg.getName() + "'!");
			return;
		}

		editArea.setCaretPosition(0);
		editArea.addKeyListener(this);
		dataChanged = false;
		sp.setViewportView(editArea);

		this.setTitle(fArg.getName() + " - " + TITLE);
		close.setEnabled(true);
		save.setEnabled(true);
		saveAs.setEnabled(true);
		return;
	}

	private void popupError(String s)
	{
		// Toolkit.getDefaultToolkit().beep();
		System.out.print("\07");
		System.out.flush();
		JOptionPane.showMessageDialog(this, s, "Error", JOptionPane.ERROR_MESSAGE);
		return;
	}

	private void saveToFile(File fArg)
	{
		PrintWriter pw = null;

		try
		{
			pw = new PrintWriter(new BufferedWriter(new FileWriter(fArg)));
		} catch (IOException e)
		{
			popupError("Can't open file '" + fArg.getName() + "' for writing");
			return;
		}

		pw.print(editArea.getText());
		pw.close();
		this.setTitle(fArg.getName() + " - " + TITLE);
		dataChanged = false;
		editArea.addKeyListener(this);
	}

	// -------------
	// inner classes
	// -------------

	/**
	 * A class to extend the FileFilter class (which is abstract) and implement the 'accept' and
	 * 'getDescription' methods.
	 */
	class MyFileFilter extends FileFilter
	{
		private String[] s;

		MyFileFilter(String[] sArg)
		{
			s = sArg;
		}

		// determine which files to display in the chooser
		@Override
		public boolean accept(File fArg)
		{
			// if it's a directory, show it
			if (fArg.isDirectory())
				return true;

			// if the filename contains the extension, show it
			for (int i = 0; i < s.length; ++i)
				if (fArg.getName().toLowerCase().indexOf(s[i].toLowerCase()) > 0)
					return true;

			// filter out everything else
			return false;
		}

		@Override
		public String getDescription()
		{
			String tmp = "";
			for (int i = 0; i < s.length; ++i)
				tmp += "*" + s[i] + " ";

			return tmp;
		}
	}
}
}
