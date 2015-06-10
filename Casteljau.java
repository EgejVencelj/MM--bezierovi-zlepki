import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Functions {

	public static double[] GetPoint(List<Point2D> points, double d){
		List<double[]> Q = new ArrayList<double[]>();
		for (Point2D point2d : points) {
			Q.add(new double[]{point2d.getX(), point2d.getY()});
		}
		
		int l = Q.size();
		for (int i = 1; i < l; i++) {
			for (int j = 0; j < l-i; j++) {
				Q.set(j, new double[]{
						(1-d)*Q.get(j)[0] + d*Q.get(j+1)[0],
						(1-d)*Q.get(j)[1] + d*Q.get(j+1)[1]								
				}); // ((1-d)*Q.get(j).getX() + d*Q.get(j+1).getX(), (1-d)*Q.get(j).getY() + d*Q.get(j+1).getY()
			}
		}
		return Q.get(0);
	}
	
	public static double LengthOfCurve(List<double[]> points){
		double sum = 0;
		double[] p1, p2;
		for (int i = 0; i < points.size()-1; i++) {
			p1 = points.get(i);
			p2 = points.get(i+1);
			
			sum += Dist(p1, p2);
		}
		return sum;
	}
	
	public static double Dist(double[] p1, double[] p2){
		return Math.sqrt(Math.pow(p1[0]-p2[0], 2) + Math.pow(p1[1]-p2[1], 2));
	}
	
	public static void PrintPoints(List<double[]> points){
		for (double[] ds : points) {
			System.out.println(ds[0] + ", " + ds[1]);
		}
	}
	
	public static ArrayList<double[]> SelfIntersections(List<double[]> points){
		ArrayList<double[]> intersections = new ArrayList<double[]>();
		
		int n = points.size();
		
		Line2D line1, line2; // = new Line2D.Float(100, 100, 200, 200);
		//boolean result = line2.intersectsLine(line1);
		
		//System.out.println(result);=
		
		for (int i = 0; i < n-2; i++) {
			for (int j = i+2; j < n-1; j++) {

				line1 = new Line2D.Double(points.get(i)[0], points.get(i)[1], points.get(i+1)[0], points.get(i+1)[1]);
				line2 = new Line2D.Double(points.get(j)[0], points.get(j)[1], points.get(j+1)[0], points.get(j+1)[1]);
				
				if(line2.intersectsLine(line1)){
					Point2D p = getIntersectionPoint(line1, line2);
					intersections.add(new double[]{p.getX(), p.getY()});
				}
			}
		}
		
		return intersections;
	}
	
	public static Point2D getIntersectionPoint(Line2D line1, Line2D line2) {
	    if (! line1.intersectsLine(line2) ) return null;
	      double px = line1.getX1(),
	            py = line1.getY1(),
	            rx = line1.getX2()-px,
	            ry = line1.getY2()-py;
	      double qx = line2.getX1(),
	            qy = line2.getY1(),
	            sx = line2.getX2()-qx,
	            sy = line2.getY2()-qy;

	      double det = sx*ry - sy*rx;
	      if (det == 0) {
	        return null;
	      } else {
	        double z = (sx*(qy-py)+sy*(px-qx))/det;
	        if (z==0 ||  z==1) return null;  // intersection at end point!
	        return new Point2D.Double(
	          (double)(px+z*rx), (double)(py+z*ry));
	      }
	 }
	
}