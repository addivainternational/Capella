using System.Runtime.InteropServices;

namespace Addiva.Nalabs.Interop
{
    /// <summary>
    /// 
    /// </summary>
    /// <remarks>
    /// dotnet publish --runtime win-x64 --configuration release --self-contained false
    /// "c:\Program Files\jextract-19\bin\jextract.bat" --source --output jsrc --target-package se.addiva.nalabs.interop -l NalabsNativelib --header-class-name NalabsLib nalabslib.h
    /// "C:\Program Files\Java\jdk-19\bin\javac.exe" --release 19 --enable-preview .\jsrc\se\addiva\nalabs\interop\*.java
    /// "C:\Program Files\Java\jdk-19\bin\jar.exe" -c --file se.addiva.nalabs.interop.jar -C jsrc/ .
    /// </remarks>
    public static class NalabsLib
    {
        // ReSharper disable once InconsistentNaming
        [UnmanagedCallersOnly(EntryPoint = "analyzeRequirements")]
        public static void analyzeRequirements(IntPtr requirements, int numRequirements)
        {
            for (int i = 0; i < numRequirements; i++)
            {
                var requirement = Marshal.PtrToStructure<Requirement>(requirements + i * Marshal.SizeOf<Requirement>());
                //var id = Marshal.PtrToStringUTF8(requirement.Id);
                var text = Marshal.PtrToStringUTF8(requirement.Text);

                if(text != null)
                {
                    var analysis = TextAnalyzer.AnalyzeText(text);

                    requirement.AriScore = analysis.ARI;
                    requirement.Conjunctions = analysis.Conjunctions;
                    requirement.VaguePhrases = analysis.VaguePhrases;
                    requirement.Optionality = analysis.Optionality;
                    requirement.Subjectivity = analysis.Subjectivity;
                    requirement.References = analysis.References;
                    requirement.Weakness = analysis.Weakness;
                    requirement.Imperatives = analysis.Imperatives;
                    requirement.Continuances = analysis.Continuances;
                    requirement.Imperatives2 = analysis.Imperatives2;
                    requirement.References2 = analysis.References2;

                    Marshal.StructureToPtr(requirement, requirements + i * Marshal.SizeOf<Requirement>(), true);
                }
            }
        }
    }
}
