package nalabs.helpers;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.*;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;

import se.addiva.nalabs_core.SeverityLevel;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Util {
	
	public static void generateReport(Composite composite, String outputPath) {
        Display display = Display.getDefault();
        PdfWriter writer = null;
        PdfDocument pdf = null;
        Document document = null;
        
        try {
        	// Create an image with the size of the control
            Rectangle bounds = composite.getBounds();
            Image swtImage = new Image(display, bounds.width - 5, bounds.height - 5);

            // Render the control onto the image
            GC gc = new GC(composite);
            gc.copyArea(swtImage, 0, 0);
            gc.dispose();
            
            // Convert the image into an itext image
            byte[] imageByteArray = convertSWTImageToBytes(swtImage);
            com.itextpdf.io.image.ImageData imageData = ImageDataFactory.create(imageByteArray);
            com.itextpdf.layout.element.Image itextImage = new com.itextpdf.layout.element.Image(imageData);
            
            // Create pdf document, and insert itext image into the pdf 
            writer = new PdfWriter(outputPath);
            pdf = new PdfDocument(writer);
            document = new Document(pdf);
            document.add(itextImage);
            
            pdf.getFirstPage().setModified();

            // Dispose of the image
            swtImage.dispose();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (document != null) {
                document.close();
            }
            if (pdf != null) {
                pdf.close();
            }
            if (writer != null) {
                try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
    }
	
	public static byte[] convertSWTImageToBytes(Image swtImage) {
        ImageLoader loader = new ImageLoader();
        loader.data = new org.eclipse.swt.graphics.ImageData[] { swtImage.getImageData() };

        // Save to a byte array output stream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        loader.save(baos, org.eclipse.swt.SWT.IMAGE_PNG);
        return baos.toByteArray();
    }
	
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