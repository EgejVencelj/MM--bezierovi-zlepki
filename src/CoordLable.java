import java.awt.*;
import java.applet.*;
import java.util.Vector;
class CoordLable extends Panel {
// This class exists solely to put a title showing where the mouse is
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	Label coords;

	public CoordLable() {
		setLayout(new GridLayout(1,0));
		coords = new Label("(?,?)");
		add(new Label("Mouse at ",Label.RIGHT));
		add(coords);
		validate();
	}

	public void setCoords(int x, int y) {
		coords.setText("(" + x + "," + y + ")");
	}

}