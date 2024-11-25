package nalabs.helpers;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import se.addiva.nalabs_core.SeverityLevel;

public class Util {
	
	public static Color getSeverityColor(SeverityLevel severityLevel) {
		switch (severityLevel) {
			case Critical:
				return new Color(Display.getCurrent(), 190, 78, 78);
			case High:
				return new Color(Display.getCurrent(), 227, 123, 79);
			case Moderate:
				return new Color(Display.getCurrent(), 240, 168, 68);
			case Low:
				return new Color(Display.getCurrent(), 249, 214, 120);
			default:
				return new Color(Display.getCurrent(), 152, 218, 116); 
		}
	}
	
	public static Color getSmellColor() {
		return new Color(Display.getCurrent(), 231, 176, 176);
	}
	
	public static java.awt.Color convertToAwtColor(Color color) {
		int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        return new java.awt.Color(red, green, blue);
	}
	
	public static SeverityLevel[] getOrderedSeverityLevels() {
		return new SeverityLevel[] { SeverityLevel.Critical, SeverityLevel.High, SeverityLevel.Moderate, SeverityLevel.Low, SeverityLevel.None };
	}
}