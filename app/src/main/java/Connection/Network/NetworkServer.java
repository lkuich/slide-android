package Connection.Network;

import com.j03.mobileinput.SettingsActivity;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import Settings.AppSettings;
import Layout.Enums.PositioningMode;

public class NetworkServer
    extends Thread
{
    private ObjectOutputStream output;
    private ServerSocket server;
    private Socket client;
    private short port = 8074;
    private boolean running = false;

    @Override
    public void run()
    {
        this.running = true;
        try
        {
            server = new ServerSocket(getPort(), 0);
            server.setReuseAddress(true);
            if (!server.isBound())
            {
                server.bind(new InetSocketAddress(getPort()));
            }

            if (!server.isClosed())
            {
                client = server.accept();
                client.setTcpNoDelay(true);

                output = new ObjectOutputStream(client.getOutputStream());
                output.flush();
            }
        } catch (IOException ioException)
        {
            ioException.printStackTrace();
        }

        //connected
        if (client != null)
        {
            final short sensitivity = AppSettings.getInstance().getSystemSettings().getMouseSensitivity();

            if (AppSettings.getInstance().getSettingsElements().getPositioningMode() == PositioningMode.ABSOLUTE)
            {
                AppSettings.getInstance().getConnectionManager().getNetworkConnectionManager().send(
                    PositioningMode.ABSOLUTE, sensitivity); //, DisplayProperties.getDisplayWidth(),
                    // DisplayProperties.getDisplayHeight());
            } else
            {
                AppSettings.getInstance().getConnectionManager().getNetworkConnectionManager().send(
                    PositioningMode.RELATIVE, sensitivity);
            }

            SettingsActivity.getActivity().startActivity(
                AppSettings.getInstance().getActivitySettings()
                    .getCanvasIntent()); // Load the Canvas
        }
    }

    // Getters
    public short getPort()
    {
        return this.port;
    }

    public ObjectOutputStream getOutput()
    {
        return this.output;
    }

    public ServerSocket getServer()
    {
        return this.server;
    }

    public Socket getClient()
    {
        return this.client;
    }

    public boolean isRunning()
    {
        return this.running;
    }
}