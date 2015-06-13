package Layout.Prompts;

import android.content.DialogInterface;
import android.preference.PreferenceActivity;
import android.widget.SeekBar;

import Settings.AppSettings;
import Layout.PreferenceName;

public class PressureSensitivityPrompt extends SliderPrompt
{
    private PreferenceActivity activity;
    public PressureSensitivityPrompt(final PreferenceActivity activity)
    {
        super(activity, "Pressure Sensitivity", "Adjust the amount of pressure you must apply before a click is registered.");
        this.activity = activity;
    }

    @Override
    public DialogInterface.OnClickListener onOk()
    {
        return new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {

                if (getSensitivity() == 0)
                {
                    setSensitivity(0.5);
                }

                AppSettings.getInstance().getSettingsElements().setPressureSensitivity(getSensitivity());

                AppSettings.getInstance().getSettingsElements().setPressureSensitivity(
                    getSensitivity());
                AppSettings.getInstance().getSettingsElements().getPref(
                    PreferenceName.CUSTOM_PRESSURE_SENSITIVITY).setSummary(
                    "Pressure sensitivity: " + Double.toString(getSensitivity()));

                AppSettings.getInstance().getSystemSettings().setPressureSensitivity(
                    activity,
                    (float) getSensitivity());

                dismissDialog();
            }
        };
    }

    @Override
    public DialogInterface.OnShowListener onShow()
    {
        return new DialogInterface.OnShowListener()
        {
            @Override
            public void onShow(DialogInterface arg0)
            {
                getSeekbarSensitivity().setProgress((int) (getSensitivity() * 100));
            }
        };
    }

    @Override
    public SeekBar.OnSeekBarChangeListener onSeekBarChange()
    {
        return new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(final SeekBar arg0, final int arg1, final boolean arg2)
            {
                setSensitivity((double) arg1 / 100);
                setText(Double.toString(getSensitivity()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0)
            {
            }

            @Override
            public void onStopTrackingTouch(SeekBar arg0)
            {
            }
        };
    }
}