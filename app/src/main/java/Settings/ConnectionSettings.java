package Settings;

import Common.SystemInfo;
import Connection.Network.NetworkConnectionManager;
import Settings.Enums.ConnectionMode;
import Connection.USB.UsbConnectionManager;

public class ConnectionSettings
{
    private ConnectionMode connectionMode;

    private NetworkConnectionManager networkConnectionManager;
    private UsbConnectionManager usbConnectionManager;

    public ConnectionSettings()
    {
        loadFactoryDefaults();
    }

    private void loadFactoryDefaults()
    {
        setConnectionMode(ConnectionMode.NONE);
        setNetworkConnectionManager(new NetworkConnectionManager());
        setUsbConnectionManager(new UsbConnectionManager());
    }

    public void reinitializeServers()
    {
        if (getConnectionMode() == ConnectionMode.WIFI)
        {
            if (getNetworkConnectionManager().isRunning())
            {
                getNetworkConnectionManager().stopListening();
            }
            setNetworkConnectionManager(new NetworkConnectionManager());
            getNetworkConnectionManager().start();
        } else if (getConnectionMode() == ConnectionMode.USB)
        {
            if (getUsbConnectionManager().isRunning())
            {
                getUsbConnectionManager().stopListening();
            }
            setUsbConnectionManager(new UsbConnectionManager());
            getUsbConnectionManager().start();
        } else
        {
            return;
        }

        updateUi(getConnectionMode());
    }

    // Setters
    public void setConnectionMode(final ConnectionMode mode)
    {
        this.connectionMode = mode;
    }

    public void setNetworkConnectionManager(final NetworkConnectionManager netMan)
    {
        this.networkConnectionManager = netMan;
    }

    public void setUsbConnectionManager(final UsbConnectionManager usbMan)
    {
        this.usbConnectionManager = usbMan;
    }

    // Getters
    public ConnectionMode getConnectionMode()
    {
        return this.connectionMode;
    }

    public NetworkConnectionManager getNetworkConnectionManager()
    {
        return this.networkConnectionManager;
    }

    public UsbConnectionManager getUsbConnectionManager()
    {
        return this.usbConnectionManager;
    }


    public void updateUi(final ConnectionMode mode)
    {
        AppSettings.getInstance().getSettingsElements().getPrefConnectionStatus().setEnabled(false);
        final SystemInfo ipv4 = AppSettings.getInstance().getSystemSettings().getSystemInfo();
        if (mode == ConnectionMode.USB)
        {
            AppSettings.getInstance().getSettingsElements().getPrefConnectionStatus().setSummary(
                "Listening on USB");
        } else if (mode == ConnectionMode.WIFI)
        {
            AppSettings.getInstance().getSettingsElements().getPrefConnectionStatus().setSummary(
                "Listening on WiFi (" + ipv4.ipv4Address().get(0).getHostAddress() + ")");
        }
    }
}
