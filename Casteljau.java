import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Casteljau {

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
}          