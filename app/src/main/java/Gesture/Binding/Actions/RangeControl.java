package Gesture.Binding.Actions;

import java.util.List;

public interface RangeControl
{
    int getMaxValue();

    int getValue();

    void bind(final RangeEventHandler eventHandler);

    void unbind(final RangeEventHandler eventHandler);

    List<RangeEventHandler> getBoundEventHandlers();
}