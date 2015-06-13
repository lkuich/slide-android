package Connection;

import Settings.AppSettings;
import Settings.ConnectionSettings;
import Settings.Enums.ConnectionMode;

public class ConnectionManager
    extends ConnectionSettings
{
    public void send(final short[] pack)
    {
        if (AppSettings.getInstance().getConnectionManager().getConnectionMode() == ConnectionMode.WIFI)
        {
            AppSettings.getInstance().getConnectionManager().getNetworkConnectionManager().send(pack);
        } else if (AppSettings.getInstance().getConnectionManager().getConnectionMode() == ConnectionMode.USB)
        {
            AppSettings.getInstance().getConnectionManager().getUsbConnectionManager().send(pack);
        }
    }
}
