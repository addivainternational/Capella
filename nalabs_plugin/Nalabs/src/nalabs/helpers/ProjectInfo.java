package nalabs.helpers;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.resource.Resource;

public class ProjectInfo {
	public Resource resource;
	public IProject project;
	
	public ProjectInfo(IProject project, Resource resource) {
		this.project = project;
		this.resource = resource;
	}
}