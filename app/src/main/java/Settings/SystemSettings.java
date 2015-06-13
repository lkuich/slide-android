package Settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.preference.PreferenceManager;

import Settings.AppSettings;
import Common.SystemInfo;

public class SystemSettings
{
    private SystemInfo systemInfo;
    private boolean spenRemoved;
    private boolean doubleClickCount;
    private boolean aboveKitKat;

    public SystemSettings()
    {
        systemInfo = new SystemInfo();
        spenRemoved = false;
        doubleClickCount = false;
        aboveKitKat = android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT; // TODO: Substitute with real value
    }

    // Setters
    public void setSystemInfo(final SystemInfo info)
    {
        this.systemInfo = info;
    }

    public void setSpenRemoved(final boolean enabled)
    {
        this.spenRemoved = enabled;
    }

    public void setDoubleClickCount(final boolean enabled)
    {
        this.doubleClickCount = enabled;
    }

    public void setPressureSensitivity(final Context c, final float sensitivity)
    {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        prefs.edit().putFloat("custom_pressure", sensitivity).commit();
    }

    public void setMouseSensitivity(final Context c, final float sensitivity)
    {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        prefs.edit().putFloat("custom_mouse", sensitivity).commit();
    }

    public void setFirstRun(final boolean firstRun, final Context c)
    {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        prefs.edit().putBoolean("firstrun", firstRun).commit();
    }

    // Getters
    public SystemInfo getSystemInfo()
    {
        return this.systemInfo;
    }

    public boolean getSpenRemoved()
    {
        return this.spenRemoved;
    }

    public boolean getDoubleClickCount()
    {
        return this.doubleClickCount;
    }

    public boolean getKitKat()
    {
        return aboveKitKat;
    }

    public boolean getFirstRun(final Context c)
    {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);

        // If it's the first run
        if (prefs.getBoolean("firstrun", true))
        {
            // Set it to be the first run
            return prefs.edit().putBoolean("firstrun", false).commit();
            //return true;
        } else
        {
            return prefs.getBoolean("firstrun", false);
        }
    }

    public float getPressureSensitivity(final Context c)
    {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        return prefs.getFloat("custom_pressure", 0);
    }

    public float getMouseSensitivity(final Context c)
    {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        return prefs.getFloat("custom_mouse", 0);
    }

    public short getMouseSensitivity()
    {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AppSettings.getInstance().getCurrentContext());
        return (short) (prefs.getFloat("custom_mouse", 0) * 10);
    }

    public boolean isWifiConnected(final Context c)
    {
        final WifiManager wifi = (WifiManager)c.getSystemService(Context.WIFI_SERVICE);
        NetworkInfo networkInfo;
        if (wifi.isWifiEnabled())
        {
            final ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(
                Context.CONNECTIVITY_SERVICE);
            networkInfo =
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        } else
        {
            return false;
        }
        return networkInfo.isConnected();
    }

    public boolean isUsbConnected(final Activity a)
    {
        final Intent intent = a.registerReceiver(
            null, new IntentFilter(
                "android.hardware.usb.action.USB_STATE")
        );
        return intent != null && intent.getExtras().getBoolean("connected");
    }
}