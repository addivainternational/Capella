package nalabs.handlers;

import nalabs.views.MainView;
import nalabs.views.InitialWarningPopup;
import nalabs.views.StartupSelectionView;

import java.util.*;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.common.util.EList;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.polarsys.capella.core.model.handler.helpers.CapellaAdapterHelper;
import org.polarsys.kitalpha.vp.requirements.Requirements.Module;
import org.polarsys.kitalpha.vp.requirements.Requirements.Requirement;

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
		
		IShellProvider shellProvider = PlatformUI.getWorkbench().getModalDialogShellProvider();
		StartupSelectionView startupSelectionView = new StartupSelectionView(shellProvider.getShell());
		if (startupSelectionView.open() != Dialog.OK) {
		    return null;
		}
		
		boolean useDefaultTextField = startupSelectionView.getUseDefaultTextField();

		Collection<Requirement> correctRequirements = getCorrectRequirements(requirements, useDefaultTextField);
		Collection<Requirement> faultyRequirements = getFaultyRequirements(requirements, useDefaultTextField);
		
		if (faultyRequirements.size() > 0) {
			InitialWarningPopup initialWarningPopup = new InitialWarningPopup(shellProvider.getShell(), faultyRequirements, useDefaultTextField);
			if (initialWarningPopup.open() != Dialog.OK) {
			    return null;
			}
		}
		
		// Re map Requirements for transfer to NOLABS lib, and analyze
		Collection<se.addiva.nalabs_core.Requirement> nalabRequirements = analyzeRequirements(correctRequirements, useDefaultTextField);
		
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        try {
        	MainView reqView = (MainView) page.showView("nqdin29hbfwpifgpnpw09fgew30"); // Use the ID of your view
            reqView.setRequirementData(nalabRequirements);
        } catch (PartInitException e) {
            e.printStackTrace();
        }
		
		return null;
	}
	
	private boolean hasValidRequirementText(Requirement requirement, boolean useDefaultTextField) {
		if (useDefaultTextField) {
			return requirement.getReqIFText() != null && getHtmlTagMatcher(requirement.getReqIFText()).find();
		} else {
			return requirement.getReqIFLongName() != null;
		}
	}

	private Collection<Requirement> getFaultyRequirements(Collection<Requirement> requirements, boolean useDefaultTextField) {
		return requirements
				.stream()
				.filter(r -> r.getReqIFIdentifier() == null || !hasValidRequirementText(r, useDefaultTextField))
				.collect(Collectors.toList());
	}

	private Collection<Requirement> getCorrectRequirements(Collection<Requirement> requirements, boolean useDefaultTextField) {
		return requirements
				.stream()
				.filter(r -> r.getReqIFIdentifier() != null && hasValidRequirementText(r, useDefaultTextField))
				.collect(Collectors.toList());
	}
	
	private static Matcher getHtmlTagMatcher(String requirementText) {
		Pattern pattern = Pattern.compile("<p>(.*?)</p>", Pattern.DOTALL);
		Matcher matcher = pattern.matcher(requirementText);
		return matcher;
	}
	
	protected Collection<se.addiva.nalabs_core.Requirement> analyzeRequirements(Collection<Requirement> requirements, boolean useDefaultTextField) throws IllegalFormatException {
		
		List<se.addiva.nalabs_core.Requirement> nalabsRequirements =
				requirements.stream().map(r -> copyRequirement(r, useDefaultTextField)).collect(Collectors.toList());
		
		for (se.addiva.nalabs_core.Requirement requirement : nalabsRequirements)
        {
			String textString;
			if (useDefaultTextField) {
				Matcher matcher = getHtmlTagMatcher(requirement.text);
				if (!matcher.find()) {
					throw new IllegalArgumentException("The requirement text does not contain the <p> html tag");
				}
				textString = matcher.group(1);
			} else {
				textString = requirement.text;
			}
	        
			se.addiva.nalabs_core.TextAnalysis analysis = se.addiva.nalabs_core.TextAnalyzer.AnalyzeText(textString);

			requirement.text = textString;
            requirement.ariScore = analysis.ARI;
            requirement.wordCount = analysis.wordCount;
            analysis.forEachSmellTypeResult(c -> requirement.totalSmells += c.totalCount);
            analysis.forEachSmellTypeResult(c -> {
            	if (c.totalCount > 0 && c.severityLevel.compareTo(requirement.severityLevel) > 0) {
            		requirement.severityLevel = c.severityLevel;
            	}
            });
            requirement.conjunctions = analysis.conjunctions;
            requirement.vaguePhrases = analysis.vaguePhrases;
            requirement.optionality = analysis.optionality;
            requirement.subjectivity = analysis.subjectivity;
            requirement.referencesInternal = analysis.referenceInternal;
            requirement.weakness = analysis.weakness;
            requirement.imperatives = analysis.imperatives;
            requirement.continuances = analysis.continuances;
            requirement.referencesExternal = analysis.referenceExternal;
        }
		
		return nalabsRequirements;
	}
	
	protected se.addiva.nalabs_core.Requirement copyRequirement(Requirement source, boolean useDefaultTextField) {
		se.addiva.nalabs_core.Requirement target = new se.addiva.nalabs_core.Requirement(source.getReqIFIdentifier(), useDefaultTextField ? source.getReqIFText() : source.getReqIFLongName());
		return target;
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
