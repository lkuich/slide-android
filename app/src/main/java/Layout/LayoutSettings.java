package Layout;

import android.preference.ListPreference;

import Layout.Enums.PositioningMode;
import Layout.Enums.PresetSensitivity;
import Layout.Enums.PressureMode;
import Layout.Prompts.PressureSensitivityPrompt;

public abstract class LayoutSettings
{
    private PositioningMode positioningMode;
    private PressureMode pressureMode;
    private PresetSensitivity presetSensitivity;

    private double mouseSensitivity;
    private double pressureSensitivity;

    private boolean actionBarEnabled;
    private boolean autoClearEnabled;
    private boolean drawPathEnabled;
    private boolean rightClickEnabled;
    private boolean zoomEnabled;
    private boolean scrollEnabled;
    private boolean pressureClickEnabled;

    protected int getPrefIndex(final ListPreference pref)
    {
        return pref.findIndexOfValue(pref.getValue());
    }

    public void setPressureMode(final PressureMode mode)
    {
        this.pressureMode = mode;
    }

    public void setPressureMode(final int index)
    {
        switch (index)
        {
            case 0:
                this.pressureMode = PressureMode.INITIAL_TOUCH;
                break;
            case 1:
                this.pressureMode = PressureMode.TOGGLE;
                break;
        }
    }

    public void setPositioningMode(final PositioningMode mode)
    {
        this.positioningMode = mode;
    }

    public void setPositioningMode(final int index)
    {
        switch (index)
        {
            case 0:
                this.positioningMode = PositioningMode.RELATIVE;
                break;
            case 1:
                this.positioningMode = PositioningMode.ABSOLUTE;
                break;
        }
    }

    public void setPresetSensitivity(final PresetSensitivity sensitivity)
    {
        this.presetSensitivity = sensitivity;
    }

    public void setPresetSensitivity(final int index)
    {
        switch (index)
        {
            case 0:
                this.presetSensitivity = PresetSensitivity.HIGH;
                break;
            case 1:
                this.presetSensitivity = PresetSensitivity.MEDIUM;
                break;
            case 2:
                this.presetSensitivity = PresetSensitivity.LOW;
                break;
            case 3:
                this.presetSensitivity = PresetSensitivity.CUSTOM;
                break;
        }
    }

    public void setPressureSensitivity(final double sensitivity)
    {
        this.pressureSensitivity = sensitivity;
    }

    public void setMouseSensitivity(final double sensitivity)
    {
        this.mouseSensitivity = sensitivity;
    }

    public void setActionBarEnabled(final boolean enabled)
    {
        this.actionBarEnabled = enabled;
    }

    public void setAutoClearEnabled(final boolean enabled)
    {
        this.autoClearEnabled = enabled;
    }

    public void setDrawPathEnabled(final boolean enabled)
    {
        this.drawPathEnabled = enabled;
    }

    public void setRightClickEnabled(final boolean enabled)
    {
        this.rightClickEnabled = enabled;
    }

    public void setZoomEnabled(final boolean enabled)
    {
        this.zoomEnabled = enabled;
    }

    public void setScrollEnabled(final boolean enabled)
    {
        this.scrollEnabled = enabled;
    }

    public void setPressureClickEnabled(final boolean enabled)
    {
        this.pressureClickEnabled = enabled;
    }

    // Getters
    public PressureMode getPressureMode()
    {
        return this.pressureMode;
    }

    public PositioningMode getPositioningMode()
    {
        return this.positioningMode;
    }

    public PresetSensitivity getPresetSensitivity()
    {
        return this.presetSensitivity;
    }

    public double getPressureSensitivity()
    {
        return this.pressureSensitivity;
    }

    public double getMouseSensitivity()
    {
        return this.mouseSensitivity;
    }

    public boolean getActionBarEnabled()
    {
        return this.actionBarEnabled;
    }

    public boolean getAutoClearEnabled()
    {
        return this.autoClearEnabled;
    }

    public boolean getDrawPathEnabled()
    {
        return this.drawPathEnabled;
    }

    public boolean getRightClickEnabled()
    {
        return this.rightClickEnabled;
    }

    public boolean getZoomEnabled()
    {
        return this.zoomEnabled;
    }

    public boolean getScrollEnabled()
    {
        return this.scrollEnabled;
    }

    public boolean getPressureClickEnabled()
    {
        return this.pressureClickEnabled;
    }

    /*     */

    //Config
    protected PressureMode getPressureMode(final ListPreference listPreference)
    {
        switch (listPreference.findIndexOfValue(listPreference.getValue()))
        {
            case 0:
                return PressureMode.INITIAL_TOUCH;
            case 1:
                return PressureMode.TOGGLE;
            default:
                return PressureMode.INITIAL_TOUCH;
        }
    }

    protected PositioningMode getPositioningMode(final ListPreference listPreference)
    {
        switch (listPreference.findIndexOfValue(listPreference.getValue()))
        {
            case 0:
                return PositioningMode.RELATIVE;
            case 1:
                return PositioningMode.ABSOLUTE;
            default:
                return PositioningMode.RELATIVE;
        }
    }

    // Used for setting sensitivity value
    protected double getDoublePresetSensitivity(final PresetSensitivity sensitivity)
    {
        switch (sensitivity)
        {
            case HIGH:
                return 0.88;
            case MEDIUM:
                return 0.72;
            case LOW:
                return 0.56;
            default:
                return 0.72;
        }
    }

    // Used for setting custom sensitivity value
    private double getDoublePresetSensitivity(final PressureSensitivityPrompt prompt)
    {
        return prompt.getValue();
    }

    protected PresetSensitivity getPresetSensitivity(final ListPreference listPreference)
    {
        switch (listPreference.findIndexOfValue(listPreference.getValue()))
        {
            case 0:
                return PresetSensitivity.HIGH;
            case 1:
                return PresetSensitivity.MEDIUM;
            case 2:
                return PresetSensitivity.LOW;
            case 3:
                return PresetSensitivity.CUSTOM;

            default:
                return PresetSensitivity.MEDIUM;
        }
    }
}