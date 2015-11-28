package a2;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;

public class Paint extends JFrame implements MouseMotionListener,
		ActionListener, MouseListener {
	static final long serialVersionUID = 42L;

	final int MAX_SAMPLES = 500;

	// private PaintPanel pp;
	// private JRadioButton square;
	// private JRadioButton circle;
	// private JRadioButton text;
	// private JRadioButton image;
	// private JButton clear;
	// private JButton exit;
	// private JButton save;

	static Color paintColour = new Color(0, 0, 0);

	private PaintPanel inkPanel;
	private JToolBar toolBar;
	private JPanel leftPanel;
	private JPanel rightPanel;

	private JButton clearButton;
	private JToolBar toolBarLeft;
	private JToolBar toolBarRight;

	private Point[] stroke;
	private int sampleCount;
	private int strokeCount;

	private JButton leftButton1;
	private JButton leftButton2;
	private JButton leftButton3;
	private JButton leftButton4;
	private JButton leftButton5;
	private JButton leftButton6;

	private JMenuItem colour;
	
	JScrollPane sp;
	JPanel blank;
	
	/*private JButton rightButton1; // Black
	private JButton rightButton2; // White
	private JButton rightButton3; // Red
	private JButton rightButton4; // Green
	private JButton rightButton5; // Yellow
	private JButton rightButton6; // Blue
	private JButton rightButton7; // Cyan
	private JButton rightButton8; // Magenta
*/
	final int DEFAULT_SIZE = 24;

	public Paint() {

		final int FIELD_WIDTH = 10;

		stroke = new Point[MAX_SAMPLES];
		sampleCount = 0;
		strokeCount = 0;

		clearButton = new JButton("Clear");

		inkPanel = new PaintPanel();
		leftPanel = new JPanel();
		rightPanel = new JPanel();
		inkPanel.setBorder(BorderFactory.createLineBorder(Color.gray));

		sp = new JScrollPane();
		sp.setPreferredSize(new Dimension(600, 400));
		
		blank = new JPanel();
		blank.setOpaque(true);
		blank.setBackground(Color.gray);
		sp.setViewportView(blank);
		
		// MENU

		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		
		
		JTextField message;
		JRadioButtonMenuItem serif;
		JRadioButtonMenuItem sansSerif;
		JRadioButtonMenuItem monospaced;
		JRadioButtonMenuItem defaultSize;
		JMenuItem about;
		JMenuItem help;
		JMenuItem cancelPopup;
		JMenuItem changePopup;
		JMenuItem restorePopup;
		JPopupMenu popup;
		JCheckBoxMenuItem italic;
		JCheckBoxMenuItem bold;

		// LEFT TOOLBAR
		toolBarLeft = new JToolBar();
		toolBarRight = new JToolBar();

		leftButton1 = new JButton(new ImageIcon("red.gif"));
		leftButton1.setToolTipText("Draw free-hand");
		leftButton2 = new JButton(new ImageIcon("red.gif"));
		leftButton3 = new JButton(new ImageIcon("red.gif"));
		leftButton4 = new JButton(new ImageIcon("red.gif"));
		leftButton5 = new JButton(new ImageIcon("red.gif"));
		leftButton6 = new JButton(new ImageIcon("red.gif"));

		// ButtonGroup tools = new ButtonGroup();

		toolBarLeft.add(leftButton1);
		toolBarLeft.add(leftButton2);
		toolBarLeft.add(leftButton3);
		toolBarLeft.add(leftButton4);
		toolBarLeft.add(leftButton5);
		toolBarLeft.add(leftButton6);

		//toolBarLeft.setOrientation(1);
		//toolBarLeft.setFloatable(false);

		leftButton1.addActionListener(this);
		leftButton2.addActionListener(this);
		leftButton3.addActionListener(this);
		leftButton4.addActionListener(this);
		leftButton5.addActionListener(this);
		leftButton6.addActionListener(this);
	
		
		//File menu
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(new MenuBar(null));
		///fileMenu.add(new MenuBar());
		

		// Edit menu (for future consideration)

		JMenu editMenu = new JMenu("Edit");
		editMenu.setMnemonic(KeyEvent.VK_E);
		menuBar.add(editMenu);
		editMenu.add(new JMenuItem("Unimplemented", new ImageIcon(
				"triangle.gif")));

		// Font menu

		JMenu fontMenu = new JMenu("Font");
		fontMenu.setMnemonic(KeyEvent.VK_O);
		menuBar.add(fontMenu);
		serif = new JRadioButtonMenuItem("Serif");
		serif.setMnemonic(KeyEvent.VK_E);
		serif.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,
				ActionEvent.ALT_MASK));

		sansSerif = new JRadioButtonMenuItem("SansSerif");
		sansSerif.setMnemonic(KeyEvent.VK_A);
		sansSerif.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2,
				ActionEvent.ALT_MASK));

		monospaced = new JRadioButtonMenuItem("MonoSpaced");
		monospaced.setMnemonic(KeyEvent.VK_O);
		monospaced.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3,
				ActionEvent.ALT_MASK));

		ButtonGroup familyGroup = new ButtonGroup();
		familyGroup.add(serif);
		familyGroup.add(sansSerif);
		familyGroup.add(monospaced);
		serif.setSelected(true);

		JMenu sizeSubMenu = new JMenu("Size");

		final String[] SIZE = { "12", "16", "20", "24", "28", "32", "36", "40",
				"44" };

		JRadioButtonMenuItem[] sizeButton = new JRadioButtonMenuItem[SIZE.length];
		ButtonGroup sizeGroup = new ButtonGroup();
		for (int i = 0; i < SIZE.length; ++i) {
			sizeButton[i] = new JRadioButtonMenuItem(SIZE[i]);
			sizeSubMenu.add(sizeButton[i]);
			sizeButton[i].addActionListener(this);
			sizeGroup.add(sizeButton[i]);
			if (SIZE[i].equals("" + DEFAULT_SIZE)) {
				defaultSize = sizeButton[i];
				defaultSize.setSelected(true);
			}
		}

		italic = new JCheckBoxMenuItem("Italic");
		bold = new JCheckBoxMenuItem("Bold");

		colour = new JRadioButtonMenuItem("Colour...");

		fontMenu.add(serif);
		fontMenu.add(sansSerif);
		fontMenu.add(monospaced);
		fontMenu.addSeparator();
		fontMenu.add(sizeSubMenu);
		fontMenu.addSeparator();
		fontMenu.add(italic);
		fontMenu.add(bold);
		fontMenu.addSeparator();
		fontMenu.add(colour);

		colour.addActionListener(this);
		// Help menu

		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);
		menuBar.add(helpMenu);

		help = new JMenuItem("Help");
		help.setMnemonic(KeyEvent.VK_H);
		help.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		helpMenu.add(help);

		about = new JMenuItem("About DemoMenu2");
		about.setMnemonic(KeyEvent.VK_A);
		about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				ActionEvent.ALT_MASK));
		helpMenu.add(about);

		// Popup menu (only pops up for right-click on message)

		popup = new JPopupMenu();
		cancelPopup = new JMenuItem("Cancel ");
		changePopup = new JMenuItem("Change message...");
		restorePopup = new JMenuItem("Restore default settings");
		popup.add(cancelPopup);
		popup.add(changePopup);
		popup.add(restorePopup);

		// ----- END MENU

		// this.addWindowListener(new WindowCloser());

		inkPanel.addMouseMotionListener(this);
		inkPanel.addMouseListener(this);
		clearButton.addActionListener(this);

		// Toolbar top

		//JPanel topPanel = new JPanel();

		JButton colorSelect = new JButton("Color");
		JButton testButton2 = new JButton("Test1");
		JButton testButton3 = new JButton("Test2");
		JButton testButton4 = new JButton("Test3");

		// colorSelect.setIcon(null);

		//topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		//topPanel.add(colorSelect);
		//topPanel.add(testButton2);
		//topPanel.add(testButton3);
		//topPanel.add(testButton4);

		colorSelect.addActionListener(this);

		// exit.addActionListener(this);
		// save.addActionListener(this);

		//topPanel.setAlignmentY(Component.TOP_ALIGNMENT);

		// ---------------

		// JPanel p1 = new JPanel();
		// p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));

		// set both panels for 'top alignment'
		inkPanel.setAlignmentY(Component.TOP_ALIGNMENT);
		// p2.setAlignmentY(Component.TOP_ALIGNMENT);

		/*leftPanel.setLayout(new FlowLayout());
		leftPanel.setAlignmentX(LEFT_ALIGNMENT);
		leftPanel.add(toolBarLeft);

		rightPanel.setLayout(new FlowLayout());
		rightPanel.setAlignmentX(RIGHT_ALIGNMENT);
		rightPanel.add(toolBarRight);
*/
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		// contentPane.add(topPanel);
		//GridBagConstraints c = new GridBagConstraints();
		// c.anchor = GridBagConstraints.WEST;

		//
		// contentPane.setLayout());
		//c.anchor = GridBagConstraints.LINE_START;
		//contentPane.add(leftPanel, c);
		contentPane.add(toolBarLeft,BorderLayout.NORTH);
		//c.anchor = GridBagConstraints.CENTER;
		contentPane.add(inkPanel, BorderLayout.CENTER);

		//c.weightx = 1.0;
		//c.anchor = GridBagConstraints.LINE_END;
		//contentPane.add(rightPanel);

		// p.add(p1);
		contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		this.setContentPane(contentPane);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent me) {
		System.out.println("mouse pressed");

		int x = me.getX();
		int y = me.getY();

		// indicated which mouse button was pressed
		if (SwingUtilities.isLeftMouseButton(me)) {
			// leftButtonStatus.setForeground(Color.red);
			// leftButtonStatus.setText("down");
		} else if (SwingUtilities.isRightMouseButton(me)) {
			// rightButtonStatus.setForeground(Color.red);
			// rightButtonStatus.setText("down");
		}

		stroke[sampleCount] = new Point(x, y);
		if (sampleCount < MAX_SAMPLES - 1)
			++sampleCount;

	}

	@Override
	public void mouseReleased(MouseEvent me) {
		// indicate which mouse button was released
		if (SwingUtilities.isLeftMouseButton(me)) {
			// leftButtonStatus.setForeground(Color.black);
			// leftButtonStatus.setText("up");
		} else if (SwingUtilities.isRightMouseButton(me)) {
			// rightButtonStatus.setForeground(Color.black);
			// rightButtonStatus.setText("up");
		}

		if (SwingUtilities.isLeftMouseButton(me)) {
			++strokeCount;
			// strokeField.setText("" + strokeCount);
			sampleCount = 0;
		}

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource() == colour) {
			Color tmp = JColorChooser.showDialog(this, "Choose text color", paintColour);
			if (tmp != null)
				paintColour = tmp;
			
			//paintColour = new Color(0, 0, 0);
		}

		System.out.println(ae.getActionCommand());

		//if (ae.getSource() == leftButton1) {
			//paintRectangle(inkPanel);
		//}
/*		
		if (ae.getSource() == rightButton3) {
			paintColour = new Color(255, 0, 0);
		}
		if (ae.getSource() == rightButton4) {
			paintColour = new Color(0, 255, 0);
		}
		if (ae.getSource() == rightButton5) {
			paintColour = new Color(0, 0, 255);
		}
		if (ae.getSource() == rightButton6) {
			paintColour = new Color(255, 255, 0);
		}
		if (ae.getSource() == rightButton7) {
			paintColour = new Color(0, 255, 255);
		}
		if (ae.getSource() == rightButton8) {
			paintColour = new Color(255, 0, 255);
		}

*/
		// inkPanel.clear();

		strokeCount = 0;
		// strokeField.setText("" + strokeCount);
		sampleCount = 0;
		// sampleField.setText("" + sampleCount);

	}

	@Override
	public void mouseDragged(MouseEvent me) {
		// if (toolBar.get
		// ^ if the option is "freehand drawing", do this (below)
		int x = me.getX();
		int y = me.getY();
		// xMotion.setText("" + x);
		// yMotion.setText("" + y);

		if (SwingUtilities.isLeftMouseButton(me)) {
			// Color c = new Color(255, 0, 0);
			Stroke s = new BasicStroke(5.0f, BasicStroke.CAP_ROUND,
					BasicStroke.JOIN_ROUND);

			stroke[sampleCount] = new Point(x, y);
			int x1 = (int) stroke[sampleCount - 1].getX();
			int y1 = (int) stroke[sampleCount - 1].getY();
			int x2 = (int) stroke[sampleCount].getX();
			int y2 = (int) stroke[sampleCount].getY();
			if (sampleCount < MAX_SAMPLES - 1)
				++sampleCount;

			// sampleField.setText("" + sampleCount);

			System.out.println(paintColour);
			// draw ink trail from previous point to current point
			inkPanel.drawInk(x1, y1, x2, y2, paintColour, s);
		}

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// xMotion.setText("" + me.getX());
		// yMotion.setText("" + me.getY());
	}

}