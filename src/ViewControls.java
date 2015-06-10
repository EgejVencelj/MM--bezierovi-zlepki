import java.awt.*;
import java.applet.*;
import java.util.Vector;

class ViewControls extends Panel {
// The view de-Casteljau controls
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    DrawPanel target;
	Label tValLabel;
	final int INTERVAL = 10;
	final int INITIAL_VAL = INTERVAL/2;

    public ViewControls(DrawPanel target) {
		this.target = target;
		setLayout(new FlowLayout());
		setBackground(Color.lightGray);
		
		tValLabel = new Label("t="+target.tVal+" ");
		add(tValLabel);

		Scrollbar sb = new Scrollbar(Scrollbar.HORIZONTAL,INITIAL_VAL,INTERVAL/10,0,INTERVAL+1);
		add(sb);
		
		Button stepB = new Button("Step");
		add(stepB);
		Button okB = new Button("OK");
		add(okB);

    }

    public void paint(Graphics g) {
		Rectangle r = bounds();
		g.setColor(Color.lightGray);
		g.draw3DRect(0, 0, r.width, r.height, false);
    }

    public boolean action(Event e, Object arg) {
		if (e.target instanceof Button) {
			String choice = (String)arg;

			if (choice.equals("Step")) {
				target.stepShow();
			} else if (choice.equals("OK")) {
				((CardLayout)DrawBezier.theControls.getLayout()).first(DrawBezier.theControls);
				target.endShow();
			}
		}
		return true;
    }

	public boolean handleEvent(Event e) {
		switch (e.id) {
		case Event.SCROLL_LINE_UP:
		case Event.SCROLL_LINE_DOWN:
		case Event.SCROLL_PAGE_UP:
		case Event.SCROLL_PAGE_DOWN:
		case Event.SCROLL_ABSOLUTE:
			if (e.target instanceof Scrollbar) {
				int val = ((Scrollbar)e.target).getValue();
				target.tVal = (double)val/(double)INTERVAL;
				tValLabel.setText("t="+target.tVal+" ");
				target.initShow();
			}
		}
		return super.handleEvent(e);
	}
}