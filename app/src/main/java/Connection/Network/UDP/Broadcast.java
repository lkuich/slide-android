package Connection.Network.UDP;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import com.j03.mobileinput.SettingsActivity;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import Settings.AppSettings;

public class Broadcast
    extends Thread
{
    private boolean broadcastRunning;

    @Override
    public void run()
    {
        try
        {
            this.broadcastRunning = true; // Run immediately by default
            startBroadcast();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    protected void haultBroadcast()
    {
        this.broadcastRunning = false;
    }

    private void startBroadcast()
        throws IOException
    {
        final DatagramSocket broadcastSocket = new DatagramSocket();
        broadcastSocket.setBroadcast(true);

        String ipAddress;

        final InetAddress broadcastAddress = InetAddress.getByName(getBroadcastAddress().getHostAddress());

        try
        {
            ipAddress =
                AppSettings.getInstance().getSystemSettings()
                    .getSystemInfo()
                    .ipv4Address()
                    .get(0)
                    .getHostAddress();

            if (ipAddress.equals("0.0.0.0"))
            {
                throw new IOException();
            }
        } catch (final IndexOutOfBoundsException e)
        {
            e.printStackTrace();
            return;
        }

        final byte[] sendData =
            (ipAddress + "," + AppSettings.getInstance().getSystemSettings()
                .getSystemInfo()
                .getDeviceManufacturer() + ","
                + AppSettings.getInstance().getSystemSettings().getSystemInfo().getDeviceModel()).getBytes();

        final DatagramPacket sendPacket = new DatagramPacket(
            sendData,
            sendData.length, broadcastAddress, 5000); // Package packed for sending to the broadcast address

        while (broadcastRunning)
        {
            broadcastSocket.send(sendPacket); // Send to the broadcast address
            try
            {
                Thread.sleep(2000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        broadcastSocket.close();
    }

    private InetAddress getBroadcastAddress()
        throws UnknownHostException
    {
        final WifiManager wifi = (WifiManager) SettingsActivity.getActivity().getSystemService(
            Context.WIFI_SERVICE);
        final DhcpInfo dhcp = wifi.getDhcpInfo();
        if (dhcp == null)
        {
            return InetAddress.getByName("0.0.0.0");
        }
        final int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        final byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
        {
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        }
        return InetAddress.getByAddress(quads);
    }
}