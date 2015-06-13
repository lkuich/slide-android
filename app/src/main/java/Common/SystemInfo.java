package Common;

import android.content.pm.PackageManager;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

import Settings.AppSettings;

public class SystemInfo
{
    private String rootSite = "http://www.slide-app.com/";
    private short version;

    public SystemInfo()
    {
        version = 0;
    }

    public short getVersion()
    {
        return this.version;
    }

    public void setVersion()
    {
        try
        {
            this.version = (short) AppSettings.getInstance().getCurrentContext().
                getPackageManager().getPackageInfo(
                AppSettings.getInstance().getCurrentContext()
                    .getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e)
        {
            this.version = 0;
        }
    }

    public String getWebsite()
    {
        return rootSite;
    }

    public String getWebsite(final String subdirectory)
    {
        return rootSite + subdirectory + "/";
    }

    public String getDeviceName()
    {
        return android.os.Build.PRODUCT;
    }

    public String getDeviceManufacturer()
    {
        return android.os.Build.MANUFACTURER;
    }

    public String getDeviceModel()
    {
        return android.os.Build.MODEL;
    }

    public ArrayList<InetAddress> ipv4Address()
    {
        final ArrayList<NetworkInterface> networkInterfaces = new ArrayList<>();
        final ArrayList<InetAddress> ip = new ArrayList<>();
        try
        {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                en.hasMoreElements(); )
            {
                while (en.hasMoreElements())
                {
                    networkInterfaces.add(en.nextElement());
                }
                for (NetworkInterface ni : networkInterfaces)
                {
                    for (Enumeration<InetAddress> enumIpAddr = ni.getInetAddresses();
                        enumIpAddr.hasMoreElements(); )
                    {
                        final InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address)
                        {
                            ip.add(inetAddress);
                        }
                    }
                }
            }
        } catch (SocketException ex)
        {
            ex.printStackTrace();
        }
        return ip;
    }
}
