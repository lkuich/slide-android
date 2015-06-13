package Gesture.Binding.Actions;

public abstract class Trigger
{
    public final static short DEVICE_MESSAGE_ID_BASE = 10000;

    public final static short[] CUT = {DEVICE_MESSAGE_ID_BASE + 18, 0};
    public final static short[] COPY = {DEVICE_MESSAGE_ID_BASE + 19, 0};
    public final static short[] PASTE = {DEVICE_MESSAGE_ID_BASE + 20, 0};

    public final static short[] TAP = {DEVICE_MESSAGE_ID_BASE + 4, 0};
    public final static short[] LONG_HOLD = {DEVICE_MESSAGE_ID_BASE + 7, 0};
    public final static short[] DOUBLE_TAP = {DEVICE_MESSAGE_ID_BASE + 8, 0};

    public final static short[] SCREENSHOT = {DEVICE_MESSAGE_ID_BASE + 21, 0};

    public final static short[] CLOSE = {DEVICE_MESSAGE_ID_BASE + 2, 0};
}
