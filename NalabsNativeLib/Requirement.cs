﻿namespace Addiva.Nalabs.Interop;


public struct Requirement
{
    public IntPtr Id { get; set; }

    public IntPtr Text { get; set; }

    public double AriScore { get; set; }

    public int Conjunctions { get; set; }

    public int VaguePhrases { get; set; }

    public int Optionality { get; set; }

    public int Subjectivity { get; set; }

    public int References { get; set; }

    public int Weakness { get; set; }

    public int Imperatives { get; set; }

    public int Continuances { get; set; }

    public int Imperatives2 { get; set; }

    public int References2 { get; set; }
}