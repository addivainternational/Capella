// Generated by jextract

package se.addiva.nalabs;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
public class NalabsLib  {

    /* package-private */ NalabsLib() {}
    public static OfByte C_CHAR = Constants$root.C_CHAR$LAYOUT;
    public static OfShort C_SHORT = Constants$root.C_SHORT$LAYOUT;
    public static OfInt C_INT = Constants$root.C_LONG$LAYOUT;
    public static OfInt C_LONG = Constants$root.C_LONG$LAYOUT;
    public static OfLong C_LONG_LONG = Constants$root.C_LONG_LONG$LAYOUT;
    public static OfFloat C_FLOAT = Constants$root.C_FLOAT$LAYOUT;
    public static OfDouble C_DOUBLE = Constants$root.C_DOUBLE$LAYOUT;
    public static OfAddress C_POINTER = Constants$root.C_POINTER$LAYOUT;
    public static MethodHandle getHelloWorld$MH() {
        return RuntimeHelper.requireNonNull(constants$0.getHelloWorld$MH,"getHelloWorld");
    }
    public static MemoryAddress getHelloWorld () {
        var mh$ = getHelloWorld$MH();
        try {
            return (java.lang.foreign.MemoryAddress)mh$.invokeExact();
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle sqrt$MH() {
        return RuntimeHelper.requireNonNull(constants$0.sqrt$MH,"sqrt");
    }
    public static double sqrt ( double x0) {
        var mh$ = sqrt$MH();
        try {
            return (double)mh$.invokeExact(x0);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
}


