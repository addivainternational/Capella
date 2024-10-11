namespace Addiva.Nalabs.Interop;


public struct Requirement
{
    public IntPtr Id { get; set; }

    public IntPtr Text { get; set; }

    public int AriScore { get; set; }
}