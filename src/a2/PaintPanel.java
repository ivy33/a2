package a2;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

import javax.swing.*;

public class PaintPanel extends JPanel
{
	// the following avoids a "warning" with Java 1.5.0 complier (?)
			static final long serialVersionUID = 42L;
			
			//private final Color INK_COLOR = Color.black;
			private final Stroke INK_STROKE = new BasicStroke(5.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
			//private static Graphics rect;

			private Vector<Line2D.Double> v;

			PaintPanel()
			{
				v = new Vector<Line2D.Double>();
				this.setBackground(Color.white);
				this.setBorder(BorderFactory.createLineBorder(Color.gray));
				this.setPreferredSize(new Dimension(250, 250));
			}

			@Override
			public void paintComponent(Graphics g)
			{
				super.paintComponent(g); // paint background
				paintInkStrokes(g);
				
//				paintInstructions(g);
//				paintEntities(g);
			}
			/*public void drawRetangle(int x, int y, int w, int h, Color c){
				rect.drawRect(x, y, w, h);
				rect.setColor(c);
			}
*/
			private void paintInkStrokes(Graphics g)
			{
				Graphics2D g2 = (Graphics2D) g;

				// set the inking color
				//g2.setColor(INK_COLOR);

				// set the stroke thickness, and cap and join attributes ('round')
				Stroke s = g2.getStroke(); // save current stroke
				g2.setStroke(INK_STROKE); // set desired stroke

				// retrieve each line segment and draw it
				for (int i = 0; i < v.size(); ++i)
					g2.draw((Line2D.Double) v.elementAt(i));

				g2.setStroke(s); // restore stroke
			}
			
			public void drawInk(int x1, int y1, int x2, int y2, Color c, Stroke s)
			{
				// get graphics context
				Graphics2D g2 = (Graphics2D) this.getGraphics();

				// create the line
				Line2D.Double inkSegment = new Line2D.Double(x1, y1, x2, y2);

				g2.setColor(c); // set the inking color
				//Stroke s = g2.getStroke(); // save current stroke
				g2.setStroke(INK_STROKE); // set desired stroke
				g2.draw(inkSegment); // draw it!
				//g2.setStroke(s); // restore stroke
				v.add(inkSegment); // add to vector
			}
			
				
				
				

			/** Clear the panel (by clearing the vector of entities) */
			public void clear()
			{
				v.clear();
				this.repaint();
			}

			/** Draw an entity */
//			public void drawEntity(Object e, int type, int x, int y)
//			{
//				v.addElement(new Entity(e, type, x, y));
//				this.repaint();
//			}
}



