import java.lang.foreign.*;
import java.lang.reflect.*;
import java.lang.*;
import se.addiva.nalabs.*;

/*
    javac  --enable-preview --release 19 -cp .;se.addiva.nalabs.interop.jar Main.java
    javac  --enable-preview --release 19 -cp .;se.addiva.nalabs.interop.jar se/addiva/nalabs/RequirementAnalyzer.java se/addiva/nalabs/Requirement.java Main.java
    java --enable-preview --enable-native-access=ALL-UNNAMED -cp .;se.addiva.nalabs.interop.jar Main

    Packaging the se.addiva.nalabs shim package
    "C:\Program Files\Java\jdk-19\bin\jar.exe" -c --file se.addiva.nalabs.jar @se.addiva.nalabs.jarlist
*/
public class Main {

    public static void main(String[] args) {

        try{
            Requirement[] requirements = new Requirement[3];
            requirements[0] = new Requirement("SRS_001", "The user shall be able to connect to their base board");
            requirements[1] = new Requirement("SRS_009", "The system shall have ESBE logo in the HMI, and may have it in the UI or better, for example on their foot as required. Also see the reference.");
            requirements[2] = new Requirement("SRS_010", "");

            System.out.println("Analyzing");
            RequirementAnalyzer.analyzeRequirements(requirements);

            System.out.println("Results");
            for (Requirement requirement : requirements) {
                System.out.printf("Id: %s, Text: '%s'\n\tARI: %f\tCONJ: %d\tVAG: %d\tOPT: %d\tSUB: %d\tREF: %d\tWEK: %d\tIMP: %d\tCON: %d\tIMP2: %d\tREF2: %d\n\n", 
                requirement.Id, 
                requirement.Text, 
                requirement.AriScore,
                requirement.Conjunctions,
                requirement.VaguePhrases,
                requirement.Optionality,
                requirement.Subjectivity,
                requirement.References,
                requirement.Weakness,
                requirement.Imperatives,
                requirement.Continuances,
                requirement.Imperatives2,
                requirement.References2
                );
            }
        }
        catch(Exception e){
            System.out.printf("%s.\n", e);
        }
    }
}