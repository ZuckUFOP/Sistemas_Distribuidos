package classes_auxiliares;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class TPanel extends JPanel{
	private boolean editavel=true;
	private Map<Integer,ArrayList<Point>> pontuation = new HashMap<Integer,ArrayList<Point>>();
	
	public boolean isEditavel() {
		return editavel;
	}
	public TPanel (int x, int y, int width, int height){
		this.setBackground(Color.WHITE);
		this.setBounds(x, y, width, height);
		this.pontuation.put(0, new ArrayList<>());
		this.pontuation.put(1, new ArrayList<>());
		this.pontuation.put(2, new ArrayList<>());
		this.pontuation.put(3, new ArrayList<>());
		new Thread() {
			public void run () {
				while(true) {
					editavel=false;
					repaint();
					editavel=true;
				}
			}
		}.start();
	}
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.BLUE);
		for(Point aux:pontuation.get(0)) {
			g.drawLine(aux.x, aux.y, aux.x, aux.y);
		}
		g.setColor(Color.GREEN);
		for(Point aux:pontuation.get(1)) {
			g.drawLine(aux.x, aux.y, aux.x, aux.y);
		}
		g.setColor(Color.YELLOW);
		for(Point aux:pontuation.get(2)) {
			g.drawLine(aux.x, aux.y, aux.x, aux.y);
		}
		g.setColor(Color.BLACK);
		for(Point aux:pontuation.get(3)) {
			g.drawLine(aux.x, aux.y, aux.x, aux.y);
		}
	}
	public void createPoint(int x, int y, int team) {
		pontuation.get(team).add(new Point(x,y));
	}
}
