package userinterface;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public class PaintComponent extends Component implements MouseListener, MouseMotionListener {

	BufferedImage curImage;
	Graphics2D draw;
	int startX = -1, startY = -1;
	int curX = -1, curY = -1;
	
	public PaintComponent(){
		addMouseListener(this);
		addMouseMotionListener(this);
		setPreferredSize(new Dimension(10,10));
		setMinimumSize(new Dimension(10,10));
		setMaximumSize(new Dimension(10,10));
		repaint();
		setVisible(true);
	}
	
	public void refreshImage(){
		draw.setPaint(Color.white);
		draw.fillRect(0, 0, getWidth(), getHeight());
		draw.setPaint(Color.black);
		repaint();
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		Point p = e.getPoint();
		curX = p.x;
		curY = p.y;
		draw.drawLine(startX, startY, curX, curY);
		repaint();
		startX = curX;
		startY = curY;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		Point p = e.getPoint();
		startX = p.x;
		startY = p.y;
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void paint(Graphics g){
		if(curImage==null){
			curImage = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_RGB); 
					createImage(getWidth(), getHeight());
			
			draw = (Graphics2D) curImage.getGraphics();
			draw.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			draw.setPaint(Color.white);
			draw.fillRect(0, 0, getWidth(), getHeight());
			draw.setPaint(Color.black);
			repaint();
		}
		g.drawImage(curImage,0,0,null);
	}
}
