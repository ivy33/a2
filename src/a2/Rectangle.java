package a2;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

public class Rectangle extends Rectangle2D{
	double xCoord;
	double yCoord;
	double width;
	double height;
	Color colour;
	
	public Rectangle(double x, double y, double w, double h, Color c){
		xCoord = x;
		yCoord = y;
		width = w;
		height = h;
		colour = c;
		
	}
	@Override
	public Rectangle2D createIntersection(Rectangle2D arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rectangle2D createUnion(Rectangle2D arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int outcode(double arg0, double arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setRect(double newX, double newY, double newW, double newH) {
		xCoord=newX;
		yCoord=newY;
		width=newW;
		height=newH;
			
	}

	@Override
	public double getHeight() {
		
		return height;
	}

	@Override
	public double getWidth() {
		return width;
	}

	@Override
	public double getX() {
		return xCoord;
	}

	@Override
	public double getY() {
		return yCoord;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

}
