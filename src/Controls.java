import java.awt.*;
import java.applet.*;
import java.util.Vector;

class Controls extends Panel {
// the Card layout control for the two modes: drawing and viewing de-casteljau
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    Controls(DrawPanel target) {
		setLayout(new CardLayout());
		add("Draw", new DrawControls(target));
		add("View", new ViewControls(target));
	}
}