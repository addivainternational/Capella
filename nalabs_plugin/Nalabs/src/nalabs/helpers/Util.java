package nalabs.helpers;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;

import se.addiva.nalabs_core.SeverityLevel;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.polarsys.capella.core.model.handler.helpers.CapellaAdapterHelper;
import org.polarsys.kitalpha.vp.requirements.Requirements.Module;
import org.polarsys.kitalpha.vp.requirements.Requirements.Requirement;
import org.polarsys.kitalpha.vp.requirements.Requirements.impl.RequirementImpl;
import org.polarsys.capella.core.data.capellamodeller.Project;

import java.util.Collection;
import java.util.ArrayList;

public class Util {
	
	public static ProjectInfo getCapellaProjectInfoForSelection(IStructuredSelection selection) {

        if (selection != null && !selection.isEmpty()) {
            // Get the first element from the selection
        	RequirementImpl firstElement = (RequirementImpl)selection.getFirstElement();

        	Resource elementResource = firstElement.eResource();
        	
        	URI uri = elementResource.getURI();
        	
        	IProject p = null;
        	if (uri.isPlatformResource()) {
	        	String platformString = uri.toPlatformString(true);
	        	IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new org.eclipse.core.runtime.Path(platformString));
	            if (file != null) {
	                p = file.getProject();
	            }
        	}

    	    ResourceSet resourceSet = new ResourceSetImpl();
    	    Resource r = resourceSet.getResource(uri, true);

    	    return new ProjectInfo(p, r);
        }

        return null;
    }
	
	public static Collection<Requirement> getKitalphaRequirements(Resource resource) {
		Collection<Requirement> requirements = new ArrayList<Requirement>();
	    if (resource != null) {
	        // Iterate over all objects in the resource
	        TreeIterator<EObject> allContents = resource.getAllContents();
	        while (allContents.hasNext()) {
	            EObject obj = allContents.next();
	            
	            EObject eObj = CapellaAdapterHelper.resolveSemanticObject(obj);

	            // Check if the object is a Kitalpha Requirement
	            if (eObj instanceof Requirement) {
	                Requirement requirement = (Requirement) eObj;
	                requirements.add(requirement);
	            } else if(eObj instanceof Module) {
		        	EList<Requirement> ownedRequirements = ((Module)eObj).getOwnedRequirements();
		        	requirements.addAll(ownedRequirements);
		        }
	        }
	    }
	    return requirements;
	}
	
	public static Collection<Requirement> getProjectRequirements(Project project) {
		Collection<Requirement> requirements = new ArrayList<Requirement>();
		// Traverse the model to find Requirement packages and individual requirements
		TreeIterator<EObject> iterator = project.eAllContents();
		while(iterator.hasNext()) {
			Object object = iterator.next();
			if (object instanceof Requirement) {
				requirements.add((Requirement)object);
	        }
	        else if(object instanceof Module) {
	        	EList<Requirement> ownedRequirements = ((Module)object).getOwnedRequirements();
	        	requirements.addAll(ownedRequirements);
	        }
		}
		return requirements;
	}
	
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
            
            // Get current date and time 
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = currentDateTime.format(formatter);
            
            // Create pdf document, and insert itext image into the pdf 
            writer = new PdfWriter(outputPath);
            pdf = new PdfDocument(writer);
            document = new Document(pdf);
            document.add(new Paragraph(formattedDateTime).setTextAlignment(TextAlignment.RIGHT).setFontSize(10).setItalic());
            itextImage.setAutoScale(true);
            document.add(itextImage);

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
	
	public static Image getAriCategoryImage(String fileName) {
		ImageDescriptor imgDesc = AbstractUIPlugin.imageDescriptorFromPlugin("Nalabs", "icons/" + fileName);
		return imgDesc.createImage();
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