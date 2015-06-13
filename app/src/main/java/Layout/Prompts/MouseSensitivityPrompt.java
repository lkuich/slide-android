package Layout.Prompts;

import android.app.Activity;
import android.content.DialogInterface;
import android.preference.PreferenceActivity;
import android.widget.SeekBar;
import Settings.AppSettings;
import Layout.PreferenceName;

public class MouseSensitivityPrompt extends SliderPrompt
{
    private Activity activity;

    public MouseSensitivityPrompt(final PreferenceActivity activity)
    {
        super(activity, "Mouse sensitivity", "Adjust how fast the mouse moves.");
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
                    setSensitivity(1);
                }

                AppSettings.getInstance().getSettingsElements().setMouseSensitivity(getSensitivity());

                AppSettings.getInstance().getSettingsElements().getPref(
                    PreferenceName.MOUSE_SENSITIVITY).setSummary(
                    "Mouse Sensitivity: "
                        + Double.toString(getSensitivity()));


                AppSettings.getInstance().getSystemSettings().setMouseSensitivity(
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
                getSeekbarSensitivity().setProgress((int) (getSensitivity() * 10));
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
                setSensitivity((double)arg1 / 10);
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
