package Settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import com.j03.mobileinput.R;

import Layout.LayoutSettings;
import Layout.PreferenceName;
import Settings.AppSettings;
import Common.Enums.AppActivity;
import Layout.Enums.PositioningMode;
import Layout.Enums.PresetSensitivity;
import Layout.Enums.PressureMode;
import Layout.Exceptions.WrongTypeException;
import Layout.Prompts.PressureSensitivityPrompt;
import Layout.Prompts.MouseSensitivityPrompt;

@SuppressWarnings("deprecation")
public class SettingsElements
    extends LayoutSettings
    implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener
{
    private SharedPreferences sliderSettings;

    private PreferenceActivity activity;

    private Preference prefConnectionStatus;
    private Preference prefMouseSensitivity;
    private Preference prefHelp;

    private ListPreference prefPositioning;
    private ListPreference prefPressureMode;
    private ListPreference prefPresetSensitivity;

    private Preference prefCustomPressureSensitivity;

    private SwitchPreference prefActionBarEnabled;
    private SwitchPreference prefDrawPathEnabled;
    private SwitchPreference prefAutoClearEnabled;
    private SwitchPreference prefRightClickEnabled;
    private SwitchPreference prefZoomEnabled;
    private SwitchPreference prefScrollEnabled;
    private SwitchPreference prefPressureEnabled;

    private MouseSensitivityPrompt mouseSliderPrompt;
    private PressureSensitivityPrompt pressureSliderPrompt;

    public SettingsElements(final PreferenceActivity activity, boolean firstRun)
    {
        this.activity = activity;
        setPrefs();

        if (firstRun)
        {
            loadFactoryDefaults();
        } else
        {
            loadSavedDefaults();
        }

        setPrefValues();
        setPrefListeners();
    }

    // Setters
    public void loadFactoryDefaults()
    {
        setPressureMode(PressureMode.INITIAL_TOUCH);
        setPositioningMode(PositioningMode.RELATIVE);

        setPresetSensitivity(PresetSensitivity.MEDIUM);
        setPressureSensitivity(getDoublePresetSensitivity(PresetSensitivity.MEDIUM));

        setActionBarEnabled(true);
        setAutoClearEnabled(true);
        setDrawPathEnabled(true);

        setRightClickEnabled(false);
        setZoomEnabled(false);
        setScrollEnabled(false);
        setPressureClickEnabled(false);

        setPositioningMode(getPrefIndex(getPrefPositioningMode()));
        setPressureMode(getPrefIndex(getPrefPressureMode()));
        setPresetSensitivity(getPrefIndex(getPrefPressureSensitivity()));
        if (getPrefIndex(getPrefPressureSensitivity()) != 3)
        {
            setPressureSensitivity(getDoublePresetSensitivity(getPresetSensitivity(getPrefPressureSensitivity())));
        }
        setActionBarEnabled(getPrefActionBarEnabled().isChecked());
        setAutoClearEnabled(getPrefAutoClearEnabled().isChecked());
        setDrawPathEnabled(getPrefDrawPathEnabled().isChecked());
        setRightClickEnabled(getPrefRightClickEnabled().isChecked());
        setZoomEnabled(getPrefZoomEnabled().isChecked());
        setScrollEnabled(getPrefScrollEnabled().isChecked());
        setPressureClickEnabled(getPrefPressureEnabled().isChecked());

        setMouseSensitivity(1);
        AppSettings.getInstance().getSystemSettings().setMouseSensitivity(
            activity,
            (float) getMouseSensitivity());

        getPrefMouseSensitivity().setSummary("Mouse sensitivity: 1.0");
    }

    public void loadSavedDefaults()
    {
        setPressureMode(getPressureMode(getPrefPressureMode()));
        setPositioningMode(getPositioningMode(getPrefPositioningMode()));

        final PresetSensitivity preset =
            getPresetSensitivity(getPrefPressureSensitivity());
        setPresetSensitivity(preset);

        double sensitivity = getDoublePresetSensitivity(
            getPresetSensitivity(getPrefPressureSensitivity()));

        if (preset == PresetSensitivity.CUSTOM)
        {
            sensitivity = getPressureSliderPrompt().getValue();
        }
        setPressureSensitivity(sensitivity);
        setMouseSensitivity(getMouseSliderPrompt().getValue());

        setActionBarEnabled(getPrefActionBarEnabled().isChecked());
        setAutoClearEnabled(getPrefAutoClearEnabled().isChecked());
        setDrawPathEnabled(getPrefDrawPathEnabled().isChecked());
        setRightClickEnabled(getPrefRightClickEnabled().isChecked());
        setZoomEnabled(getPrefZoomEnabled().isChecked());
        setScrollEnabled(getPrefScrollEnabled().isChecked());
        setPressureClickEnabled(getPrefPressureEnabled().isChecked());

        final float pSen = AppSettings.getInstance().getSystemSettings().getPressureSensitivity(activity);
        final float mSen = AppSettings.getInstance().getSystemSettings().getMouseSensitivity(activity);

        setPressureSensitivity(pSen);
        setMouseSensitivity(mSen);

        getPrefCustomPressureSensitivity().setSummary("Pressure sensitivity: " + Float.toString(pSen));
        getPrefMouseSensitivity().setSummary("Mouse sensitivity: " + Float.toString(mSen));
    }

    // Setters
    public void setPrefs()
    {
        this.prefConnectionStatus = getPref(PreferenceName.CONNECTION_STATUS);
        this.prefMouseSensitivity = getPref(PreferenceName.MOUSE_SENSITIVITY);
        this.prefHelp = getPref(PreferenceName.HELP);

        this.prefPositioning = (ListPreference) getPref(PreferenceName.POSITIONING_MODE);
        this.prefPressureMode = (ListPreference) getPref(PreferenceName.PRESSURE_MODE);
        this.prefPresetSensitivity =
            (ListPreference) getPref(PreferenceName.PRESSURE_SENSITIVITY);

        this.prefCustomPressureSensitivity =
            getPref(PreferenceName.CUSTOM_PRESSURE_SENSITIVITY);

        this.prefActionBarEnabled = (SwitchPreference) getPref(PreferenceName.ACTION_BAR);
        this.prefDrawPathEnabled = (SwitchPreference) getPref(PreferenceName.DRAW_PATH);
        this.prefAutoClearEnabled = (SwitchPreference) getPref(PreferenceName.AUTO_CLEAR);
        this.prefRightClickEnabled = (SwitchPreference) getPref(PreferenceName.RIGHT_CLICK);
        this.prefZoomEnabled = (SwitchPreference) getPref(PreferenceName.ZOOM);
        this.prefScrollEnabled = (SwitchPreference) getPref(PreferenceName.SCROLL);
        this.prefPressureEnabled = (SwitchPreference) getPref(PreferenceName.PRESSURE_ENABLED);

        this.sliderSettings = activity.getSharedPreferences("settings", Context.MODE_PRIVATE);

        this.pressureSliderPrompt = new PressureSensitivityPrompt(activity);
        this.mouseSliderPrompt = new MouseSensitivityPrompt(activity);

        //this.setPressureSensitivity(this.customPressureSetting.getFloat("custom_pressure", 0));
        //setMouseSensitivity((int)this.customPressureSetting.getFloat("", 0));
    }

    public void setPrefListeners()
    {
        getPrefConnectionStatus().setOnPreferenceClickListener(this);
        getPrefMouseSensitivity().setOnPreferenceClickListener(this);
        getPrefCustomPressureSensitivity().setOnPreferenceClickListener(this);
        getPrefHelp().setOnPreferenceClickListener(this);

        getPrefPositioningMode().setOnPreferenceChangeListener(this);
        getPrefPressureMode().setOnPreferenceChangeListener(this);
        getPrefPressureSensitivity().setOnPreferenceChangeListener(this);
        getPrefMouseSensitivity().setOnPreferenceChangeListener(this);
        getPrefCustomPressureSensitivity().setOnPreferenceChangeListener(this);

        getPrefActionBarEnabled().setOnPreferenceChangeListener(this);
        getPrefDrawPathEnabled().setOnPreferenceChangeListener(this);
        getPrefAutoClearEnabled().setOnPreferenceChangeListener(this);
        getPrefRightClickEnabled().setOnPreferenceChangeListener(this);
        getPrefZoomEnabled().setOnPreferenceChangeListener(this);
        getPrefScrollEnabled().setOnPreferenceChangeListener(this);
        getPrefPressureEnabled().setOnPreferenceChangeListener(this);
    }

    public void setPrefValues()
    {
        if (getPresetSensitivity() == PresetSensitivity.CUSTOM)
        {
            getPrefCustomPressureSensitivity().setEnabled(true);
        } else
        {
            getPrefCustomPressureSensitivity().setEnabled(false);
        }

        setMouseSensitivity(getMouseSensitivity());//(int)setNewValue(getPrefMouseSensitivity(), getMouseSliderPrompt().getValue()));

        // Elements that have summaries
        setPositioningMode(
            (Integer) setNewValue(
                getPrefPositioningMode(), getPositioningModeIndex(
                    getPositioningMode())));
        setPressureMode((Integer)setNewValue(getPrefPressureMode(), getPressureModeIndex(
                getPressureMode())));
        setPresetSensitivity((Integer)setNewValue(getPrefPressureSensitivity(), getPresetSensitivityModeIndex(
                getPresetSensitivity())));

//        setNewValue(getPrefPressureSensitivity(), getLayoutSettings().getPresetSensitivity());
        //setNewValue(getPrefCustomPressureSensitivity(), getLayoutSettings().getPressureSensitivity());

        setNewValue(getPrefActionBarEnabled(), getActionBarEnabled());
        setNewValue(getPrefAutoClearEnabled(), getAutoClearEnabled());
        setNewValue(getPrefDrawPathEnabled(), getDrawPathEnabled());
        setNewValue(getPrefRightClickEnabled(), getRightClickEnabled());
        setNewValue(getPrefZoomEnabled(), getZoomEnabled());
        setNewValue(getPrefScrollEnabled(), getScrollEnabled());
        setNewValue(getPrefPressureEnabled(), getPressureClickEnabled());
    }

    // Getters
    public Preference getPrefConnectionStatus()
    {
        return this.prefConnectionStatus;
    }

    public Preference getPrefMouseSensitivity() {
        return this.prefMouseSensitivity;
    }

    public Preference getPrefHelp()
    {
        return this.prefHelp;
    }

    public ListPreference getPrefPositioningMode()
    {
        return this.prefPositioning;
    }

    public ListPreference getPrefPressureMode()
    {
        return this.prefPressureMode;
    }

    public ListPreference getPrefPressureSensitivity()
    {
        return this.prefPresetSensitivity;
    }

    public Preference getPrefCustomPressureSensitivity()
    {
        return this.prefCustomPressureSensitivity;
    }

    public SwitchPreference getPrefActionBarEnabled()
    {
        return this.prefActionBarEnabled;
    }

    public SwitchPreference getPrefDrawPathEnabled()
    {
        return this.prefDrawPathEnabled;
    }

    public SwitchPreference getPrefAutoClearEnabled()
    {
        return this.prefAutoClearEnabled;
    }

    public SwitchPreference getPrefRightClickEnabled()
    {
        return this.prefRightClickEnabled;
    }

    public SwitchPreference getPrefZoomEnabled()
    {
        return this.prefZoomEnabled;
    }

    public SwitchPreference getPrefScrollEnabled()
    {
        return this.prefScrollEnabled;
    }

    public SwitchPreference getPrefPressureEnabled()
    {
        return this.prefPressureEnabled;
    }

    public MouseSensitivityPrompt getMouseSliderPrompt()
    {
        return this.mouseSliderPrompt;
    }

    public PressureSensitivityPrompt getPressureSliderPrompt()
    {
        return this.pressureSliderPrompt;
    }

    public Preference getPref(final PreferenceName name)
    {
        switch (name)
        {
            case CONNECTION_STATUS:
                return findPreference(R.string.connectionStatusKey);
            case MOUSE_SENSITIVITY:
                return findPreference(R.string.mouseSensitivityKey);
            case POSITIONING_MODE:
                return findPreference(R.string.posModeKey);
            case ACTION_BAR:
                return findPreference(R.string.actionbarKey);
            case DRAW_PATH:
                return findPreference(R.string.drawpathKey);
            case AUTO_CLEAR:
                return findPreference(R.string.autoclearKey);
            case PRESSURE_ENABLED:
                return findPreference(R.string.prclickKey);
            case PRESSURE_MODE:
                return findPreference(R.string.pressureClickModeKey);
            case PRESSURE_SENSITIVITY:
                return findPreference(R.string.pressureClickSensitivityKey);
            case CUSTOM_PRESSURE_SENSITIVITY:
                return findPreference(R.string.pressureClickCustomSensitivityKey);
            case RIGHT_CLICK:
                return findPreference(R.string.rclickKey);
            case ZOOM:
                return findPreference(R.string.zoomKey);
            case SCROLL:
                return findPreference(R.string.scrollKey);
            case HELP:
                return findPreference(R.string.helpKey);
            default:
                return null;
        }
    }

    private Preference findPreference(final int key)
    {
        return getActivity().findPreference(getActivity().getString(key));
    }

    private PreferenceName getPrefName(final String key)
    {
        if (keyEquals(key, R.string.connectionStatusKey))
        {
            return PreferenceName.CONNECTION_STATUS;
        } else if (keyEquals(key, R.string.mouseSensitivityKey))
        {
            return PreferenceName.MOUSE_SENSITIVITY;
        } else if (keyEquals(key, R.string.posModeKey))
        {
            return PreferenceName.POSITIONING_MODE;
        } else if (keyEquals(key, R.string.actionbarKey))
        {
            return PreferenceName.ACTION_BAR;
        } else if (keyEquals(key, R.string.drawpathKey))
        {
            return PreferenceName.DRAW_PATH;
        } else if (keyEquals(key, R.string.autoclearKey))
        {
            return PreferenceName.AUTO_CLEAR;
        } else if (keyEquals(key, R.string.pressureClickModeKey))
        {
            return PreferenceName.PRESSURE_MODE;
        } else if (keyEquals(key, R.string.pressureClickSensitivityKey))
        {
            return PreferenceName.PRESSURE_SENSITIVITY;
        } else if (keyEquals(key, R.string.pressureClickCustomSensitivityKey))
        {
            return PreferenceName.CUSTOM_PRESSURE_SENSITIVITY;
        } else if (keyEquals(key, R.string.rclickKey))
        {
            return PreferenceName.RIGHT_CLICK;
        } else if (keyEquals(key, R.string.zoomKey))
        {
            return PreferenceName.ZOOM;
        } else if (keyEquals(key, R.string.scrollKey))
        {
            return PreferenceName.SCROLL;
        } else if (keyEquals(key, R.string.prclickKey))
        {
            return PreferenceName.PRESSURE_ENABLED;
        } else if (keyEquals(key, R.string.helpKey))
        {
            return PreferenceName.HELP;
        } else
        {
            return PreferenceName.NONE;
        }
    }

    private boolean keyEquals(final String key, final int ref)
    {
        return key.equals(getActivity().getString(ref));
    }

    //Config
    private int getPressureModeIndex(final PressureMode mode)
    {
        switch (mode)
        {
            case INITIAL_TOUCH:
                return 0;
            case TOGGLE:
                return 1;
            default:
                return 0;
        }
    }

    private String getPressureModeValue(final PressureMode mode)
    {
        return mode.name();
    }

    private int getPositioningModeIndex(final PositioningMode mode)
    {
        switch (mode)
        {
            case RELATIVE:
                return 0;
            case ABSOLUTE:
                return 1;
            default:
                return 0;
        }
    }

    private int getPresetSensitivityModeIndex(final PresetSensitivity mode)
    {
        switch (mode)
        {
            case HIGH:
                return 0;
            case MEDIUM:
                return 1;
            case LOW:
                return 2;
            case CUSTOM:
                return 3;
            default:
                return 0;
        }
    }

    private String getPresetSensitivityModeValue(final PresetSensitivity mode)
    {
        return mode.name();
    }

    @Override
    public boolean onPreferenceClick(final Preference preference)
    {
        switch (getPrefName(preference.getKey()))
        {
            case CONNECTION_STATUS:
                AppSettings.getInstance().getActivitySettings().loadActivity(getActivity(), AppActivity.CANVAS);
                break;

            case MOUSE_SENSITIVITY:
                AppSettings.getInstance().getSettingsElements().getMouseSliderPrompt().show();
                break;

            case CUSTOM_PRESSURE_SENSITIVITY:
                AppSettings.getInstance().getSettingsElements().getPressureSliderPrompt().show();
                break;

            case HELP:
                getActivity().startActivity(new Intent(
                    Intent.ACTION_VIEW, Uri.parse(
                        AppSettings.getInstance().getSystemSettings().getSystemInfo().getWebsite("troubleshooting")
                    )
                ));
                break;
        }
        return false;
    }

    @Override
    public boolean onPreferenceChange(final Preference preference, final Object newValue)
    {
        switch (getPrefName(preference.getKey()))
        {
            case POSITIONING_MODE:
                setPositioningMode(
                    (Integer) setNewValue(
                        getPrefPositioningMode(),
                        newValue));
                break;

            case MOUSE_SENSITIVITY:
                setMouseSensitivity(
                    (Integer) setNewValue(
                        getPrefMouseSensitivity(),
                        newValue));
                break;

            case ACTION_BAR:
                setActionBarEnabled(
                    (Boolean) setNewValue(
                        getPrefActionBarEnabled(),
                        newValue));
                break;

            case DRAW_PATH:
                setDrawPathEnabled(
                    (Boolean) setNewValue(
                        getPrefDrawPathEnabled(),
                        newValue));
                break;

            case AUTO_CLEAR:
                setAutoClearEnabled(
                    (Boolean) setNewValue(
                        getPrefAutoClearEnabled(),
                        newValue));
                break;

            case PRESSURE_ENABLED:
                setPressureClickEnabled(
                    (Boolean) setNewValue(
                        getPrefPressureEnabled(),
                        newValue));
                break;

            case PRESSURE_MODE:
                setPressureMode(
                    (Integer) setNewValue(
                        getPrefPressureMode(),
                        newValue));
                break;

            case PRESSURE_SENSITIVITY:
                setPressureSensitivity(
                    (Integer) setNewValue(
                        getPrefPressureSensitivity(),
                        newValue));
                getPrefCustomPressureSensitivity().setEnabled(getPrefPressureSensitivity().findIndexOfValue(getPrefPressureSensitivity().getValue()) == 3);
                break;

            case CUSTOM_PRESSURE_SENSITIVITY:
                setPressureSensitivity(
                    (Integer) setNewValue(
                        getPrefCustomPressureSensitivity(),
                        newValue));
                break;

            case RIGHT_CLICK:
                setRightClickEnabled(
                    (Boolean) setNewValue(
                        getPrefRightClickEnabled(),
                        newValue));
                break;

            case ZOOM:
                setZoomEnabled(
                    (Boolean) setNewValue(
                        getPrefZoomEnabled(),
                        newValue));
                break;

            case SCROLL:
                setScrollEnabled(
                    (Boolean) setNewValue(
                        getPrefScrollEnabled(),
                        newValue));
                break;
        }
        return false;
    }

    public SharedPreferences getSliderSettings()
    {
        return this.sliderSettings;
    }

    private PreferenceActivity getActivity()
    {
        return this.activity;
    }

    private Object setNewValue(final Object element, final Object newValue)
    {
        if (element instanceof ListPreference && newValue instanceof String)
        {
            final ListPreference pref = (ListPreference) element;
            final String val = (String) newValue;
            final int index = pref.findIndexOfValue(val);

            pref.setValueIndex(index);
            pref.setSummary(val);

            return index;
        } else if (element instanceof SwitchPreference && newValue instanceof Boolean)
        {
            final SwitchPreference pref = (SwitchPreference) element;
            final Boolean val = (Boolean) newValue;
            pref.setChecked(val);
            return val;
        } else if (element instanceof ListPreference && newValue instanceof Integer)
        {
            final ListPreference pref = (ListPreference) element;
            final String summary = ((ListPreference) element).getValue();
            final int val = (Integer) newValue;

            pref.setValueIndex(val);
            pref.setSummary(summary);

            return val;
        } else
        {
            try
            {
                throw new WrongTypeException("Object needs to be a type of Preference");
            } catch (final WrongTypeException e)
            {
                e.printStackTrace();
                return 0;
            }
        }
    }
}
