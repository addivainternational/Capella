using System.Runtime.InteropServices;

namespace StaticLib
{
    public static class NolabsLib
    {
        [UnmanagedCallersOnly(EntryPoint = "getHelloWorld")]
        public static IntPtr getHelloWorld()
        {
            return Marshal.StringToHGlobalAnsi("Hello World, from .NET!");
        }

        [UnmanagedCallersOnly(EntryPoint = "sqrt")]
        public static double sqrt(double x)
        {
            return Math.Sqrt(x); 
        }
    }
}
