package Settings;

import android.content.Context;

import Connection.ConnectionManager;

public class AppSettings
{
    private static Context currentContext;

    private static ActivitySettings activitySettings;
    private static CanvasSettings canvasSettings;
    private static ConnectionManager connectionManager;
    private static PenSettings penSettings;
    private static SettingsElements settingsElements;

    private static SystemSettings systemSettings;

    private static AppSettings instance = null;

    // Getter for instance
    public static synchronized AppSettings getInstance()
    {
        if (instance == null)
        {
            instance = new AppSettings();
        }
        return instance;
    }

    public static synchronized AppSettings getInstanceSetContext(final Context c)
    {
        if (instance == null)
        {
            instance = new AppSettings(c);
        }
        return instance;
    }

    private AppSettings()
    {
        activitySettings = new ActivitySettings(getCurrentContext());
        canvasSettings = new CanvasSettings();
        connectionManager = new ConnectionManager();
        penSettings = new PenSettings();
        systemSettings = new SystemSettings();
    }

    private AppSettings(final Context c)
    {
        setCurrentContext(c);

        activitySettings = new ActivitySettings(getCurrentContext());
        canvasSettings = new CanvasSettings();
        connectionManager = new ConnectionManager();
        penSettings = new PenSettings();
        systemSettings = new SystemSettings();
    }

    // Getters
    public ActivitySettings getActivitySettings()
    {
        return activitySettings;
    }

    public CanvasSettings getCanvasSettings()
    {
        return canvasSettings;
    }

    public ConnectionManager getConnectionManager()
    {
        return connectionManager;
    }

    public PenSettings getPenSettings()
    {
        return penSettings;
    }

    public SettingsElements getSettingsElements()
    {
        return settingsElements;
    }

    public SystemSettings getSystemSettings()
    {
        return systemSettings;
    }

    public Context getCurrentContext()
    {
        return currentContext;
    }

    // Setters
    public void setSettingsElements(final SettingsElements el)
    {
        settingsElements = el;
    }

    public void setCurrentContext(final Context c)
    {
        currentContext = c;
    }
}
