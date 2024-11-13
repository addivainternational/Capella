package nalabs.handlers;

import nalabs.views.MainView;

import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.common.util.EList;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.polarsys.capella.core.model.handler.helpers.CapellaAdapterHelper;
import org.polarsys.kitalpha.vp.requirements.Requirements.*;
import org.polarsys.kitalpha.vp.requirements.Requirements.Module;
import org.polarsys.kitalpha.vp.requirements.Requirements.Requirement;

//import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NalabsHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		// Get Requirements from selection
		IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);

		Collection<Requirement> requirements = getRequirements(selection);

		Collection<Requirement> correctRequirements = getCorrectRequirements(requirements);
		Collection<Requirement> faultyRequirements = getFaultyRequirements(requirements);
		
		// Re map Requirements for transfer to NOLABS lib, and analyze
		Collection<se.addiva.nalabs_core.Requirement> nalabRequirements = analyzeRequirements(correctRequirements);
		
		// Re map results to Capella requirements, updating or adding a NOLABS Attribute
//		notifyFaultyRequirement(faultyRequirements);
		
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        try {
        	MainView reqView = (MainView) page.showView("nqdin29hbfwpifgpnpw09fgew30"); // Use the ID of your view
            reqView.setRequirementData(nalabRequirements);
        } catch (PartInitException e) {
            e.printStackTrace();
        }
		
		return null;
	}
	
	private void notifyFaultyRequirement(Collection<Requirement> faultyRequirements) {
		for(Requirement requirement : faultyRequirements) {
			updateNalabsAttribute(requirement, "NALABS Error: Unable to analyze requirement. Missing ID and/or Text");
		}
	}

	private Collection<Requirement> getFaultyRequirements(Collection<Requirement> requirements) {
		return requirements
				.stream()
				.filter(r -> r.getReqIFIdentifier() == null || r.getReqIFText() == null)
				.collect(Collectors.toList())
				;
	}

	private Collection<Requirement> getCorrectRequirements(Collection<Requirement> requirements) {
		return requirements
				.stream()
				.filter(r -> r.getReqIFIdentifier() != null && r.getReqIFText() != null)
				.collect(Collectors.toList())
				;
	}

	protected void updateNalabsAttribute(Requirement requirement, String value) {
		Attribute nalabsAttribute = findAttribute(requirement.getOwnedAttributes(), "NALABS");
		// Set or add NALABS value
		if(nalabsAttribute != null) {
			setNalabsAttributeValue(nalabsAttribute, value);
		} else {
			addNalabsAttribute(requirement, value);
		}		
	}
	
	protected Collection<se.addiva.nalabs_core.Requirement> analyzeRequirements(Collection<Requirement> requirements) throws IllegalFormatException {
		
		List<se.addiva.nalabs_core.Requirement> nalabsRequirements =
				requirements.stream().map(r -> copyRequirement(r)).collect(Collectors.toList());
		
		for (se.addiva.nalabs_core.Requirement requirement : nalabsRequirements)
        {
			Pattern pattern = Pattern.compile("<p>(.*?)</p>", Pattern.DOTALL);
			Matcher matcher = pattern.matcher(requirement.Text);
			if (!matcher.find()) {
				throw new IllegalArgumentException("The requirement text does not contain the <p> html tag");
			}
			String textString = matcher.group(1);
	        
			se.addiva.nalabs_core.TextAnalysis analysis = se.addiva.nalabs_core.TextAnalyzer.AnalyzeText(textString);

			requirement.Text = textString;
            requirement.AriScore = analysis.ARI;
            requirement.WordCount = analysis.wordCount;
            requirement.TotalSmells = analysis.conjunctions.totalCount + analysis.vaguePhrases.totalCount + 
            		analysis.optionality.totalCount + analysis.subjectivity.totalCount + 
            		analysis.referenceInternal.totalCount + analysis.weakness.totalCount + 
            		analysis.imperatives.totalCount + analysis.continuances.totalCount + 
            		analysis.referenceExternal.totalCount;
            requirement.Conjunctions = analysis.conjunctions;
            requirement.VaguePhrases = analysis.vaguePhrases;
            requirement.Optionality = analysis.optionality;
            requirement.Subjectivity = analysis.subjectivity;
            requirement.ReferencesInternal = analysis.referenceInternal;
            requirement.Weakness = analysis.weakness;
            requirement.Imperatives = analysis.imperatives;
            requirement.Continuances = analysis.continuances;
            requirement.ReferencesExternal = analysis.referenceExternal;
        }
		
		return nalabsRequirements;
	}
	
	protected se.addiva.nalabs_core.Requirement copyRequirement(Requirement source){
		se.addiva.nalabs_core.Requirement target = new se.addiva.nalabs_core.Requirement(source.getReqIFIdentifier(), source.getReqIFText());
		return target;
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
}
