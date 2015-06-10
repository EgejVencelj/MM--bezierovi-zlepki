import java.awt.*;
import java.applet.*;
import java.util.Vector;

class FramedArea extends Panel {
// This class exists solely to put a frame around the drawing area. 
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public FramedArea(DrawPanel target) {
		super();

		//Set layout to one that makes its contents as big as possible.
		setLayout(new GridLayout(1,0));

		add(target);
		validate();
    }

    public Insets insets() {
		return new Insets(4,4,5,5);
    }

    public void paint(Graphics g) {
        Dimension d = size();
        Color bg = getBackground();
 
        g.setColor(bg);
        g.draw3DRect(0, 0, d.width - 1, d.height - 1, true);
        g.draw3DRect(3, 3, d.width - 7, d.height - 7, false);
    }
}