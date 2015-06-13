package Gesture.Binding.Actions;

public abstract class Range
{
    public final static short DEVICE_MESSAGE_ID_BASE = 10000;

    public final static short[] ZOOM_IN = {DEVICE_MESSAGE_ID_BASE + 12, 0};
    public final static short[] ZOOM_OUT = {DEVICE_MESSAGE_ID_BASE + 13, 0};
    public final static short[] SCROLL_DOWN = {DEVICE_MESSAGE_ID_BASE + 14, 0};
    public final static short[] SCROLL_UP = {DEVICE_MESSAGE_ID_BASE + 15, 0};
    public final static short[] SCROLL_LEFT = {DEVICE_MESSAGE_ID_BASE + 16, 0};
    public final static short[] SCROLL_RIGHT = {DEVICE_MESSAGE_ID_BASE + 17, 0};
}