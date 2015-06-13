package Gesture;

public abstract class ControllerToggles
{
    private static int previousCommand;

    public static void setPreviousCommand(final int e)
    {
        previousCommand = e;
    }

    public static int getPreviousCommand()
    {
        return previousCommand;
    }
}
