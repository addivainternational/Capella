package nalabs.handlers;

import static se.addiva.nalabs.NalabsLib.getHelloWorld;
import static se.addiva.nalabs.NalabsLib.sqrt;

import java.lang.foreign.MemoryAddress;
import java.util.*;
import java.time.LocalTime;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.common.util.EList;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.polarsys.capella.core.model.handler.helpers.CapellaAdapterHelper;
import org.polarsys.kitalpha.vp.requirements.Requirements.*;
import org.polarsys.kitalpha.vp.requirements.Requirements.Module;

public class NalabsHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		// Get Requirements from selection
		IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
		Collection<Requirement> requirements = getRequirements(selection);
		
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

		// TODO: Re map Requirements for transfer to NOLABS lib
		// TODO: Re map results to Capella requirements, updating or adding a NOLABS Attribute 
		
		for(Requirement r : requirements) {
			Attribute nalabsAttribute = findAttribute(r.getOwnedAttributes(), "NALABS");
			if(nalabsAttribute != null) {
				// Set attribute value
				setNalabsAttributeValue(nalabsAttribute, "SUCCESS: " + LocalTime.now().toString());
			} else {
				if(addNalabsAttribute(r, "SUCCESS: " + LocalTime.now().toString()) == null) {
					MessageDialog.openInformation(
							window.getShell(),
							"NALABS",
							"NALABS Attribute not found on RequirementType, or RequirementType not set.");
				};
			}
		}
		return null;
	}

	protected Attribute addNalabsAttribute(Requirement requirement, String value) {
		RequirementType rt = requirement.getRequirementType();
		
		if(rt == null) {
			return null;
		}
		
		AttributeDefinition ad = findAttributeDefinition(rt.getOwnedAttributes(), "NALABS");

		if(ad == null) {
			return null;
		}
		
		RequirementsFactory factory = RequirementsFactory.eINSTANCE;
		StringValueAttribute stringValueAttribute = factory.createStringValueAttribute();

		stringValueAttribute.setDefinition(ad);
		stringValueAttribute.setValue(value);
		
		addRequirementAttribute(requirement, stringValueAttribute);
		return stringValueAttribute;
	}
	
	protected void setNalabsAttributeValue(Attribute attribute, String value) {
		EStructuralFeature attributeValueFeature = attribute.eClass().getEStructuralFeature("value");
		setStructuralFeature(attribute, attributeValueFeature, value);
	}
	

	protected AttributeDefinition findAttributeDefinition(Collection<AttributeDefinition> attributes, String id) {
		for(AttributeDefinition ad : attributes) {
			if(ad.getReqIFIdentifier().equalsIgnoreCase(id)) {
				return ad;
			}
		}
		
		return null;
	}

	protected Attribute findAttribute(Collection<Attribute> attributes, String id) {
		for(Attribute a : attributes) {
			if(a.getDefinition().getReqIFIdentifier().equalsIgnoreCase(id)) {
				return a;
			}
		}
		
		return null;
	}

	protected void addRequirementAttribute(Requirement requirement, Attribute attribute) {
		EditingDomain editingDomain = TransactionUtil.getEditingDomain(requirement);
		if(editingDomain == null) {
			editingDomain = TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain();
		}
		
		editingDomain.getCommandStack().execute(new RecordingCommand((TransactionalEditingDomain) editingDomain) {
	        @Override
	        protected void doExecute() {
	    		requirement.getOwnedAttributes().add(attribute);	        	
	        }
	    });
	}

	protected void setStructuralFeature(EObject object, EStructuralFeature feature, Object value) {
		EditingDomain editingDomain = TransactionUtil.getEditingDomain(object);
		if(editingDomain == null) {
			editingDomain = TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain();
		}
		
		editingDomain.getCommandStack().execute(new RecordingCommand((TransactionalEditingDomain) editingDomain) {
	        @Override
	        protected void doExecute() {
	    		object.eSet(feature, value);
	        }
	    });
	}
	

	/*
	 * Collects Requirements from selection, also getting from Modules if selected. 
	 * */
	protected Collection<Requirement> getRequirements(IStructuredSelection selection){
	    Collection<EObject> objects = CapellaAdapterHelper.resolveSemanticObjects(selection.toList());

		Collection<Requirement> reqs = new ArrayList<Requirement>();
		
		for (EObject object : objects) {
			if (object instanceof Requirement) {
				reqs.add((Requirement)object);
	        }
	        else if(object instanceof Module) {
	        	EList<Requirement> ownedRequirements = ((Module)object).getOwnedRequirements();
	        	reqs.addAll(ownedRequirements);
	        }
		}
		
		return reqs;
	}
	
	/*
	MemoryAddress result = getHelloWorld();
    String greeting = result.getUtf8String(0);
	double y = sqrt(4);
	*/ 
    
	/*
	MessageDialog.openInformation(
			window.getShell(),
			"NALABS",
			greeting + "\n" + y);
	*/

	/*
	String rText = r.getReqIFText();
	
	MessageDialog.openInformation(
			window.getShell(),
			"ReqIFText",
			rText);

	EList<Attribute> attributes = r.getOwnedAttributes();
	Collection<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
	Collection<Object> attributeValues = new ArrayList<Object>();
	List<String> attributeIds = new ArrayList<String>();

	
	
	for(Attribute a : attributes) {
		AttributeDefinition ad = a.getDefinition();
		attributeDefinitions.add(ad);
		
		attributeIds.add(ad.getReqIFIdentifier());
		
		if(ad.getReqIFIdentifier().equalsIgnoreCase("NALABS")) {
			EStructuralFeature attributeValueFeature = a.eClass().getEStructuralFeature("value");

			if(attributeValueFeature != null) {
				Object v = a.eGet(attributeValueFeature);
				if(v != null) {
					attributeValues.add(v);
					setStructuralFeature(a, attributeValueFeature, "SUCCESS: " + LocalTime.now().toString());
				}
			}
		}
	}
				
	MessageDialog.openInformation(
			window.getShell(),
			"Attributes",
			attributes.toString() + 
			"\n\nDefinitions:\n" + attributeDefinitions.toString() + 
			"\n\nValues:\n" + attributeValues.toString() +
			"\n\nIDs:\n" + attributeIds.toString()
			);
	 */

}
