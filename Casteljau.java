import java.awt.geom.Point2D;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class Casteljau {

	public static List<Point2D> GetPoint(List<Point2D> points, float d){
		List<Point2D> Q = new ArrayList<Point2D>(points);
		
		int l = Q.size();
		for (int i = 1; i < l; i++) {
			for (int j = 0; j < l-i; j++) {
				Q.get(j).setLocation((1-d)*Q.get(j).getX() + d*Q.get(j+1).getX(), (1-d)*Q.get(j).getY() + d*Q.get(j+1).getY()); // (1-d)*Q.get(j) + d*Q.get(j+1);
			}
		}
		return Q;
	}

	//Input: array P[0:n] of n+1 points and real number u in [0,1] 
	//		Output: point on curve, C(u) 
	//		Working: point array Q[0:n] 
    //
	//		for i := 0 to n do 
	//		Q[i] := P[i]; // save input 
	//		for k := 1 to n do 
	//		for i := 0 to n - k do 
	//		Q[i] := (1 - u)Q[i] + u Q[i + 1];
	//		return Q[0];
}          