import java.lang.foreign.*;
import static se.addiva.nalabs.NalabsLib.*;
import se.addiva.nalabs.Requirement;

/*
    javac  --enable-preview --release 19 -cp .;se.addiva.nalabs.jar Main.java
*/
public class Main {

    public static void main(String[] args) {
        System.out.println("Main Starting");

        try {
            int numReqs = 5;
            SegmentAllocator allocator = SegmentAllocator.implicitAllocator();
            MemorySegment requirements = Requirement.allocateArray(numReqs, allocator);

            System.out.println("Initializing array");

            for(int i = 0; i < numReqs; i++){
                MemorySegment id = allocator.allocateUtf8String("REQ_00" + i + 1);
                MemorySegment text = allocator.allocateUtf8String("Text REQ_00" + i + 1);
                
                Requirement.Id$set(requirements, i, id.address());
                Requirement.Text$set(requirements, i, text.address());
            }

            System.out.println("Running Analyzis");
            MemoryAddress result = analyzeRequirements(requirements, numReqs);

            System.out.println("Reading back data");
            for(int i = 0; i < numReqs; i++){
                String id = Requirement.Id$get(requirements, i).getUtf8String(0);
                String text = Requirement.Text$get(requirements, i).getUtf8String(0);
                int ari = Requirement.AriScore$get(requirements, i);
                System.out.printf("Id: %s, Text: '%s', ARI: %d\n", id, text, ari);
            }
        }
        catch(Exception e){
            System.out.printf("%s.\n", e);
        }
    }
}