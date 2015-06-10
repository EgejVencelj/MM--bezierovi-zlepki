/**
This applet allows the user to draw and edit a 2D bezier spline
of any degree. Editing has three modes: <p>
<ul>
<li>Extending - adding point to the control polygon of the bezier.
<li>Editing - moving existing control point by picking and dragging.
<li>Deleting - removing any existing control point by picking it.
</ul>
There is a clear button and a button to view the de-Casteljau 
algorithm in action.<p>
The additional view mode is for watching de-Casteljau algorithm
steps in finding a point on the bezier curve for a given parameter
value t. The value of t can be changed using a scroll bar.
*/

import java.awt.*;
import java.applet.*;
import java.util.Vector;

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public class DrawBezier extends Applet {
/** main applet class */
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	public static CoordLable coordLabel = new CoordLable();
	public static DrawPanel dp = new DrawPanel();
	public static Controls theControls = new Controls(dp);

    public void init() {
		setLayout(new BorderLayout());

		add("North", coordLabel);
		add("Center", new FramedArea(dp));
		add("South",theControls);

			// force loading of Geometry class
		Point p1 = new Point(0,0);
		Point p2 = new Point(0,1);
		Point p3 = Geometry.interpolate(p1,p2,0.5);
    }

    public boolean handleEvent(Event e) {
		switch (e.id) {
		  case Event.WINDOW_DESTROY:
			System.exit(0);
			return true;
		  default:
			return false;
		}
    }

    public static void main(String args[]) {
		Frame f = new Frame("DrawBezier");
		DrawBezier drawBez = new DrawBezier();
		drawBez.init();
		drawBez.start();

		f.add("Center", drawBez);
		f.resize(400, 400);
		f.show();
    }
}