import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.List;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.CubicCurve2D;

public class Bezier {
	public static void main(String[] args) {
		new Bezier();
	}
	
	public Bezier() {
		JFrame f = new JFrame("Bezierovi zlepki");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(600, 480);
		
		Pane p = new Pane(); 
		
		f.getContentPane().add(p);
		f.setVisible(true);
	}


}

class Pane extends JPanel {
	private static final int POINTSIZE = 8;
	
	private double x;
	private double y;
	private List<Point2D> points = new ArrayList<Point2D>();
	private List<Ellipse2D> paintControlPoints = new ArrayList<Ellipse2D>();
	private List<CubicCurve2D> curves = new ArrayList<CubicCurve2D>();
	private int mouseDownOn = -1;
	
	public Pane() {
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {	
				x = e.getX();
				y = e.getY();
				
				// Preverjanje ce je uporabnik kliknil na kontrolno tocko
				int i = 0;
				for(Ellipse2D p: paintControlPoints){
					if(p.contains(e.getPoint())) {
						mouseDownOn = i;
						
						return;
					}
					i++;
				}
							
				// Dodajanje novih tock
				points.add(new Point2D.Double(x, y));
				
				int pointsSize = points.size();
				int curvesSize = curves.size();
				
				if(pointsSize > 1) {
					Point2D prevPoint = points.get(pointsSize - 2);
					CubicCurve2D prevCurve = null;
					
					if(curvesSize > 0)
						prevCurve = curves.get(curvesSize - 1);
					
					//System.out.println(x + " " + y + " - " + prevPoint.getX() + " " + prevPoint.getY());
					
					// Dodamo novo krivuljo https://www.particleincell.com/2012/bezier-splines/
					double ctrlX2 = (x + prevPoint.getX()) * 0.5;
					double ctrlY2 = (y + prevPoint.getY()) * 0.5;
					double ctrlX1 = (curvesSize > 0 ? (2 * prevCurve.getX2() - prevCurve.getCtrlX2()) : prevPoint.getX() + 10);
					double ctrlY1 = (curvesSize > 0 ? (2 * prevCurve.getY2() - prevCurve.getCtrlY2()) : prevPoint.getY() + 30);
					
					CubicCurve2D c = new CubicCurve2D.Double(prevPoint.getX(), prevPoint.getY(), ctrlX1, ctrlY1, ctrlX2, ctrlY2, x, y);
					curves.add(c);
					
					// Dodamo obe kontrolni tocki za slikanje
					paintControlPoints.add(new Ellipse2D.Double(ctrlX1 - (POINTSIZE / 2), ctrlY1 - (POINTSIZE / 2), POINTSIZE, POINTSIZE));
					paintControlPoints.add(new Ellipse2D.Double(ctrlX2 - (POINTSIZE / 2), ctrlY2 - (POINTSIZE / 2), POINTSIZE, POINTSIZE));
					
				}
				
				repaint();
			}	
			
			@Override
			public void mouseReleased(MouseEvent e) {
				mouseDownOn = -1;
			}
		});
		this.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if(mouseDownOn > -1) {	
					// i nam pove ali spreminjamo prvo kontrolno tocko ali drugo
					int i = mouseDownOn % 2;
					CubicCurve2D temp = curves.get(mouseDownOn / 2);
					CubicCurve2D temp1 = null;
					
					if((mouseDownOn / 2 + (i == 0 ? -1 : 1) >= 0) && (mouseDownOn / 2 + (i == 0 ? -1 : 1) < curves.size())) {
						temp1 = curves.get(mouseDownOn / 2 + (i == 0 ? -1 : 1));
					}

					// Ce popravljamo kontrolno tocko, moramo popraviti tudi sosede
					if(temp1 != null) {
						// Popravljamo 2. kontrolno pri prejsnji krivulji
						if(i == 0) {
							paintControlPoints.set(mouseDownOn - 1, 
								new Ellipse2D.Double(
									(2 * temp1.getX2() - e.getX()) - (POINTSIZE / 2), 
									(2 * temp1.getY2() - e.getY()) - (POINTSIZE / 2), 
									POINTSIZE, 
									POINTSIZE)
							);
							temp1.setCurve(
								temp1.getX1(), 
								temp1.getY1(), 
								temp1.getCtrlX1(), 
								temp1.getCtrlY1(), 
								(2 * temp1.getX2() - e.getX()), 
								(2 * temp1.getY2() - e.getY()), 
								temp1.getX2(), 
								temp1.getY2()
							);
						} else {
							// Popravljamo 1. kontrolno pri naslednji krivulji
							paintControlPoints.set(mouseDownOn + 1, 
								new Ellipse2D.Double(
									(2 * temp1.getX1() - e.getX()) - (POINTSIZE / 2), 
									(2 * temp1.getY1() - e.getY()) - (POINTSIZE / 2), 
									POINTSIZE, 
									POINTSIZE)
							);
							temp1.setCurve(
								temp1.getX1(), 
								temp1.getY1(), 
								(2 * temp1.getX1() - e.getX()), 
								(2 * temp1.getY1() - e.getY()), 
								temp1.getCtrlX2(), 
								temp1.getCtrlY2(), 
								temp1.getX2(), 
								temp1.getY2()
							);
						}
					}
					
					// Izbrana tocka
					paintControlPoints.set(mouseDownOn,
						new Ellipse2D.Double(
							e.getX() - (POINTSIZE / 2), 
							e.getY() - (POINTSIZE / 2), 
							POINTSIZE, 
							POINTSIZE)
					);
					temp.setCurve(
						temp.getX1(), 
						temp.getY1(), 
						(i == 0 ? e.getX() : temp.getCtrlX1()), 
						(i == 0 ? e.getY() : temp.getCtrlY1()), 
						(i == 1 ? e.getX() : temp.getCtrlX2()), 
						(i == 1 ? e.getY() : temp.getCtrlY2()), 
						temp.getX2(), 
						temp.getY2()
					);
					
					repaint();
				}
			}	
		}); 
	}
	
	 
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.blue);
		for(Point2D p: points) {
			g2.draw(new Ellipse2D.Double(p.getX() - (POINTSIZE / 2), p.getY() - (POINTSIZE / 2), POINTSIZE, POINTSIZE));
		}
			
		for(CubicCurve2D c: curves) {
			g2.setColor(Color.black);
			g2.draw(c);
			
			g.setColor(Color.gray);
			g.drawLine((int) c.getX1(), (int) c.getY1(), (int) c.getCtrlX1(), (int) c.getCtrlY1());
			g.drawLine((int) c.getX2(), (int) c.getY2(), (int) c.getCtrlX2(), (int) c.getCtrlY2());
		}
		
		g2.setColor(Color.red);
		for(Ellipse2D e: paintControlPoints) {
			g2.draw(e);
		}
	}
}

         