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
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(600, 600);
		
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
	//private List<Ellipse2D> paintPoints = new ArrayList<Point2D>();
	private List<Ellipse2D> paintControlPoints = new ArrayList<Ellipse2D>();
	private List<CubicCurve2D> curves = new ArrayList<CubicCurve2D>();
	private int mouseDownOn = -1;

	private int stSamoPresekov = 0;
	private double dolzinaKrivulje = 0;

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

				if(pointsSize > 1) {
					Point2D prev = points.get(pointsSize - 2);
					System.out.println(x + " " + y + " - " + prev.getX() + " " + prev.getY());
					
					// Dodamo novo krivuljo (tole je samo zacasno)
					double ctrlX1 = x - 10;
					double ctrlY1 = y - 10;
					double ctrlX2 = prev.getX() - 10;
					double ctrlY2 = prev.getY() - 10;
					
					CubicCurve2D c = new CubicCurve2D.Double(x, y, ctrlX1, ctrlY1, ctrlX2, ctrlY2, prev.getX(), prev.getY());
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
				System.out.println(e.getX());
			}
		});
		this.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if(mouseDownOn > -1) {
					CubicCurve2D temp = curves.get(mouseDownOn / 2);
					int i = mouseDownOn % 2;
					double ctrlX1 = (i == 0 ? e.getX() : temp.getCtrlX1());
					double ctrlY1 = (i == 0 ? e.getY() : temp.getCtrlY1());
					double ctrlX2 = (i == 1 ? e.getX() : temp.getCtrlX2());
					double ctrlY2 = (i == 1 ? e.getY() : temp.getCtrlY2());
					
					paintControlPoints.set(mouseDownOn, new Ellipse2D.Double(e.getX() - (POINTSIZE / 2), e.getY() - (POINTSIZE / 2), POINTSIZE, POINTSIZE));
					temp.setCurve(temp.getX1(), temp.getY1(), ctrlX1, ctrlY1, ctrlX2, ctrlY2, temp.getX2(), temp.getY2());
					
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
			g2.draw(new Ellipse2D.Double(p.getX() - 4, p.getY() - 4, 8, 8));
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

	@Override
	public void removeAll() {
		super.removeAll();
		points = new ArrayList<Point2D>();
		paintControlPoints = new ArrayList<Ellipse2D>();
		curves = new ArrayList<CubicCurve2D>();
		mouseDownOn = -1;
	}

	@Override
	public void updateUI() {
		super.updateUI();
	}

	/**
	 * Za izpisovanje dolzine loka krivulje.
	 * Zelo grdo in neprefesionalno, ampak (glede na stisko s casom) deluje.
	 * @return dolzina krivulje in stevilo samopresecisc
	 */
	@Override
	public String toString() {
		// todo
		return "Dolžina krivulje: " + this.dolzinaKrivulje + "        " +
				"Število samopresečišč: " + this.stSamoPresekov;
	}


}

