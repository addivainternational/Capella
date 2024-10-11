using System.Runtime.InteropServices;

namespace Addiva.Nalabs.Interop
{
    public static class NalabsLib
    {
        // ReSharper disable once InconsistentNaming
        [UnmanagedCallersOnly(EntryPoint = "analyzeRequirements")]
        public static IntPtr analyzeRequirements(IntPtr requirements, int numRequirements)
        {
            for (int i = 0; i < numRequirements; i++)
            {
                var requirement = Marshal.PtrToStructure<Requirement>(requirements + i * Marshal.SizeOf<Requirement>());
                requirement.AriScore = 100 + i;
                Marshal.StructureToPtr(requirement, requirements + i * Marshal.SizeOf<Requirement>(), true);
            }

            return requirements;
        }
    }
}
