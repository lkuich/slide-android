package Gesture.Binding.Actions;

public abstract class Toggle
{
    public final static short DEVICE_MESSAGE_ID_BASE = 10000;

    public final static short[] MOVING = {DEVICE_MESSAGE_ID_BASE + 3, 0};

    public final static short[] MOUSE_DOWN = {DEVICE_MESSAGE_ID_BASE + 5, 0};
        // Finger double tapped and is down
    public final static short[] MOUSE_UP = {DEVICE_MESSAGE_ID_BASE + 6, 0};
        // Finger double tapped and is up

    public final static short[] ABSOLUTE = {DEVICE_MESSAGE_ID_BASE + 10, 1920, 1080, 0, 0}; // ID, width, height, sensitivity, version
    public final static short[] RELATIVE = {DEVICE_MESSAGE_ID_BASE + 11, 0, 0}; // ID, sensitivity, version
}
