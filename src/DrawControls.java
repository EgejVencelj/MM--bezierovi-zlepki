import java.awt.*;
import java.applet.*;
import java.util.Vector;

class DrawControls extends Panel {
// The drawing controls
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    DrawPanel target;

    public DrawControls(DrawPanel target) {
		this.target = target;
		setLayout(new FlowLayout());
		setBackground(Color.lightGray);
		
		Choice modes = new Choice();
		modes.addItem("Extend");
		modes.addItem("Edit");
		modes.addItem("Delete");
		modes.setBackground(Color.lightGray);
		add(modes);

		Button b = new Button("Clear");
		add(b);
		b = new Button("Show De-Casteljau");
		add(b);
    }

    public void paint(Graphics g) {
		Rectangle r = bounds();
		g.setColor(Color.lightGray);
		g.draw3DRect(0, 0, r.width, r.height, false);
    }

    public boolean action(Event e, Object arg) {
		if (e.target instanceof Choice) {
			String choice = (String)arg;

			if (choice.equals("Extend")) {
				target.setDrawMode(DrawPanel.EXTEND);
			} else if (choice.equals("Edit")) {
				target.setDrawMode(DrawPanel.EDIT);
			} else if (choice.equals("Delete")) {
				target.setDrawMode(DrawPanel.DELETE);
			}
		} else if (e.target instanceof Button) {
			String choice = (String)arg;

			if (choice.equals("Clear")) {
				target.clearAll();
			} else if (choice.equals("Show De-Casteljau")) {
				((CardLayout)DrawBezier.theControls.getLayout()).last(DrawBezier.theControls);
				target.initShow();
			}
		}
		return true;
    }                
}