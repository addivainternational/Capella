package se.addiva.nalabs;

import java.lang.foreign.MemoryAddress;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.MemorySession;
import java.lang.foreign.SegmentAllocator;

import static se.addiva.nalabs.interop.NalabsLib.*;
import se.addiva.nalabs.interop.Requirement;

public class RequirementAnalyzer {

    public static void analyzeRequirements(se.addiva.nalabs.Requirement[] requirements){
    
        try (MemorySession session = MemorySession.openConfined()) {
            int numReqs = requirements.length;

            // Allocate native meory and set data
            SegmentAllocator allocator = SegmentAllocator.implicitAllocator();
            MemorySegment nativeRequirements = Requirement.allocateArray(numReqs, allocator);

            for(int i = 0; i < numReqs; i++){
                MemorySegment id = allocator.allocateUtf8String(requirements[i].Id);
                MemorySegment text = allocator.allocateUtf8String(requirements[i].Text);
                
                Requirement.id$set(nativeRequirements, i, id.address());
                Requirement.text$set(nativeRequirements, i, text.address());
            }

            se.addiva.nalabs.interop.NalabsLib.analyzeRequirements(nativeRequirements, numReqs);

            // Transfer results to requirements
            for(int i = 0; i < numReqs; i++){
                requirements[i].AriScore = Requirement.ariScore$get(nativeRequirements, i);
                requirements[i].Conjunctions = Requirement.conjunctions$get(nativeRequirements, i);
                requirements[i].VaguePhrases = Requirement.vaguePhrases$get(nativeRequirements, i);
                requirements[i].Optionality = Requirement.optionality$get(nativeRequirements, i);
                requirements[i].Subjectivity = Requirement.subjectivity$get(nativeRequirements, i);
                requirements[i].References = Requirement.references$get(nativeRequirements, i);
                requirements[i].Weakness = Requirement.weakness$get(nativeRequirements, i);
                requirements[i].Imperatives = Requirement.imperatives$get(nativeRequirements, i);
                requirements[i].Continuances = Requirement.continuances$get(nativeRequirements, i);
                requirements[i].Imperatives2 = Requirement.imperatives2$get(nativeRequirements, i);
                requirements[i].References2 = Requirement.references2$get(nativeRequirements, i);
            }
        }
    }
}
