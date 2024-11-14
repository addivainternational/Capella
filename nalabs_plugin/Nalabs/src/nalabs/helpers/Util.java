package nalabs.helpers;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import se.addiva.nalabs_core.SeverityLevel;

public class Util {
	
	public static Color getSeverityColor(SeverityLevel severityLevel) {
		switch (severityLevel) {
			case Critical:
				return new Color(Display.getCurrent(), 255, 51, 51);
			case High:
				return new Color(Display.getCurrent(), 255, 153, 51);
			case Moderate:
				return Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
			case Low:
			default:
				return Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
		}
	}
}