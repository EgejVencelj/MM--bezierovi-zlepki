import java.awt.*;
import java.applet.*;
import java.util.Vector;

class DrawPanel extends Panel {
// The drawing panel class
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

		// edit modes
    public static final int EXTEND = 0;
    public static final int EDIT = 1;
    public static final int DELETE = 2;
    int	   mode = EXTEND;
		// DeCasteljau algorithm show 
	boolean bShow = false;
	int		step = 0;
	Point	dePnts[][];
	public double tVal = 0.5;

		// vector to hold the control points
    Vector points = new Vector();

		// helper variables for mouse dragging
	Point mPoint;
	int index;

		// constractor
	//----------------------------------------------------------
    public DrawPanel() {
	//----------------------------------------------------------
		setBackground(Color.white);
		mPoint = new Point(-1,-1);
	}

		// drawing modes setting
	//----------------------------------------------------------
    public void setDrawMode(int mode) {
	//----------------------------------------------------------
		switch (mode) {
		case EXTEND:
		case EDIT:
		case DELETE:
			this.mode = mode;
			break;
		default:
			throw new IllegalArgumentException();
		}
    }

		// initialize De-Castlejau algorithm show
	//----------------------------------------------------------
	public void initShow() {
	//----------------------------------------------------------
		bShow = true;
		step = 0;
		// allocate space for points
		int i,j;
		int np = points.size();
		if (np <= 0)
			return;
		dePnts = new Point[np][];
		for (i = 0 ; i < np ; i++)
			dePnts[i] = new Point[np-i];
		// copy original polygon
		points.copyInto(dePnts[0]);
		// fill the points of all algorithm steps
		for (i = 1 ; i < np ; i++)
			for (j = 0 ; j < np-i ; j++)
				dePnts[i][j] = Geometry.interpolate(dePnts[i-1][j],dePnts[i-1][j+1],tVal);
		repaint();
	}

		// advance one step in De-Casteljau algorithm
	//----------------------------------------------------------
	public void stepShow() {
	//----------------------------------------------------------
		step++;
		if (step >= points.size())
			step = 0;		// loop around again
		repaint();
	}

		// end De-Castlejau algorithm show
	//----------------------------------------------------------
	public void endShow() {
	//----------------------------------------------------------
		bShow = false;
		step = 0;
		repaint();
	}

		// clearing all control points
	//----------------------------------------------------------
	public void clearAll() {
	//----------------------------------------------------------
		points.removeAllElements();
		repaint();
	}

		// helper function to draw the de-casteljau algorithm
	//----------------------------------------------------------
	protected void drawDeCasteljau(Graphics g) {
	//----------------------------------------------------------
		int np = points.size();
		if (np <= 0)
			return;
		Point p0,p1;
		for (int i=0 ; i <= step ; i++) {
				// draw first point
			p0 = dePnts[i][0];
			g.setColor(Color.red);
			g.fillRect(p0.x - 2, p0.y - 2, 4, 4);
				// draw polygon & other points
			for (int j = 1 ; j < np-i ; j++) {
				p1 = dePnts[i][j];
				g.setColor(Color.blue);
				g.drawLine(p0.x, p0.y, p1.x, p1.y);
				g.setColor(Color.red);
				g.fillRect(p1.x - 2, p1.y - 2, 4, 4);
				p0 = p1;
			}
		}
		if (step == np-1) {	// i.e. last step
			drawBezier(g);
				// show last point
			g.setColor(Color.black);	
			p0 = dePnts[step][0];
			g.fillRect(p0.x - 2, p0.y - 2, 4, 4);
		}
	}

		// helper function to draw the bezier spline
	//----------------------------------------------------------
	protected void drawBezier(Graphics g) {
	//----------------------------------------------------------
		int np = points.size();
		if (np < 3)
			return;

		Point ptArray[] = new Point[np];

		Point p0,p1;
		p0 = (Point)points.elementAt(0);
		for (int i = 1 ; i <= 50 ; i++) {
			double t = (double)i/50.0;
			points.copyInto(ptArray);
			//p1 = Geometry.evalBezierRec(ptArray,t,np);
			p1 = Geometry.evalBezier(ptArray,t);
			g.setColor(Color.green);
			g.drawLine(p0.x, p0.y, p1.x, p1.y);
			p0 = p1;
		}
	}
		// helper function: finds nearest control point
		// to the input argument point op
	//----------------------------------------------------------
	protected int getNearestPointIndex(Point op) {
	//----------------------------------------------------------
		int np = points.size();
		if (np == 0)
			return -1;

		int index = -1;
		double dist = Geometry.dist(op,(Point)points.elementAt(0));
		if (dist < 10.0)
			index = 0;

		for (int i=1 ; i < np; i++) {
			Point p = (Point)points.elementAt(i);
			double newDist = Geometry.dist(op,p);
			if (newDist < dist && newDist < 10.0)
				index = i;
		}
		return index;
	}

		// handle events according to edit mode
	//----------------------------------------------------------
    public boolean handleEvent(Event e) {
	//----------------------------------------------------------
		if (bShow) {
			if (e.id == Event.MOUSE_MOVE || e.id == Event.MOUSE_DRAG) {
				DrawBezier.coordLabel.setCoords(e.x,e.y);
				return true;
			}
			return false;
		}

		switch (e.id) {
		  case Event.MOUSE_MOVE:
			DrawBezier.coordLabel.setCoords(e.x,e.y);
			return true;
		  case Event.MOUSE_DOWN:
			mPoint.x = e.x;
			mPoint.y = e.y;
			switch (mode) {
			case EXTEND:
				//if (points.size() > 3)	// no more than 4 points
				//	break;
				points.addElement(new Point(e.x, e.y));
				index = points.size()-1;
				break;
			case EDIT:
			case DELETE:
			default:
				index = getNearestPointIndex(mPoint);
			}
			//repaint();
			return true;
		  case Event.MOUSE_UP:
			switch (mode) {
			case DELETE:
				if (index >=0) {
					points.removeElementAt(index);
				}
				break;
			case EXTEND:
			case EDIT:
			default:
				if (index >= 0) {
						// take care of clipping here (e.x and e.y out of window)
					((Point)points.elementAt(index)).x = e.x;
					((Point)points.elementAt(index)).y = e.y;

				}
			}
			index = -1;
			repaint();
			return true;
		  case Event.MOUSE_DRAG:
			DrawBezier.coordLabel.setCoords(e.x,e.y);
			if (index >= 0) {
				((Point)points.elementAt(index)).x = mPoint.x;
				((Point)points.elementAt(index)).y = mPoint.y;
			}
			mPoint.x = e.x;
			mPoint.y = e.y;
			repaint();
			return true;
		  case Event.WINDOW_DESTROY:
			System.exit(0);
			return true;
		  default:
			return false;
		}
    }

		// draw the bezier spline and control polygon
	//----------------------------------------------------------
	public void paint(Graphics g) {
	//----------------------------------------------------------
		g.setPaintMode();

			// draw only DeCasteljau algorithm when needed
		if (bShow) {
			drawDeCasteljau(g);
			return;
		}

			// draw the bezier spline + polygon
		int np = points.size();
		if (np == 0)
			// draw nothing
			return;

			// draw polygon
		Point p0,p1;
		p0 = (Point)points.elementAt(0);
		g.setColor(Color.red);
		g.fillRect(p0.x - 2, p0.y - 2, 4, 4);

		for (int i=1 ; i < np; i++) {
			p0 = (Point)points.elementAt(i-1);
			p1 = (Point)points.elementAt(i);
			g.setColor(Color.red);
			g.fillRect(p1.x - 2, p1.y - 2, 4, 4);
			g.setColor(Color.blue);
			g.drawLine(p0.x, p0.y, p1.x, p1.y);
		}
		
			// draw bezier
		drawBezier(g);

	/*-**
	// SOMEHOW THERE IS NO NEED FOR THIS...
			// take care of dragging actions
		if (index != -1) {
			Point oldp = (Point)points.elementAt(index);
			Point prevp,nextp;
			
			if (index < np-1) {
				nextp = (Point)points.elementAt(index+1);
				// erase the next line
				g.setXORMode(getBackground());
				g.drawLine(oldp.x, oldp.y, nextp.x, nextp.y);
				g.setColor(getForeground());
				g.setPaintMode();
				// draw new next line
				g.drawLine(mPoint.x, mPoint.y, nextp.x, nextp.y);
			}
			if (index > 0) {
				prevp = (Point)points.elementAt(index-1);
				// erase the previous line
				g.setXORMode(getBackground());
				g.drawLine(prevp.x, prevp.y, oldp.x, oldp.y);
				g.setColor(getForeground());
				g.setPaintMode();
				// draw new previous line
				g.drawLine(prevp.x, prevp.y, mPoint.x, mPoint.y);
			}
		}
	**-*/
    }
}