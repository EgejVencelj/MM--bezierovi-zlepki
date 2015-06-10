import java.awt.*;
import java.applet.*;
import java.util.Vector;

class Geometry {
// A class for some geometric global functions
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	// returns the euclidian distance between two points
	public static double dist(Point p0, Point p1) {
		int dx = p1.x - p0.x;
		int dy = p1.y - p0.y;
		int sum = dx*dx + dy*dy;
		return Math.sqrt((double)sum);
	}

	// returns the linear interpolation of two points
	public static Point interpolate(Point p0, Point p1,double t) {
		double x = t * p1.x + (1-t) * p0.x;
		double y = t * p1.y + (1-t) * p0.y;
		return new Point((int)(x+0.5),(int)(y+0.5));
	}

	// evaluates a bezier defined by the control polygon
	// which points are given in the array at the value t
	public static Point evalBezier(Point arr[],double t) {
		for (int iter = arr.length ; iter > 0 ; iter--) {
			for (int i = 1 ; i < iter ; i++) {
				arr[i-1] = interpolate(arr[i-1],arr[i],t);
			}
		}
		return arr[0];
	}

	// evaluates a bezier defined by the control polygon
	// which points are given in the array at the value t
	// Note: this function is recursive
	public static Point evalBezierRec(Point arr[],double t,int iter) {
		if (iter == 1)
			return arr[0];
		for (int i = 1 ; i < iter ; i++) {
			arr[i-1] = interpolate(arr[i-1],arr[i],t);
		}
		return evalBezierRec(arr,t,iter-1);
	}

}