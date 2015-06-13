package Settings;

import android.content.Context;

import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.pen.Spen;

public class PenSettings
{
    private boolean spenAvailable;

    public PenSettings()
    {
        spenAvailable = false;
    }

    // Setters
    public void setSpenAvailable(final Context context)
    {
        final Spen spenPackage = new Spen();
        try
        {
            spenPackage.initialize(context);
            this.spenAvailable = spenPackage.isFeatureEnabled(Spen.DEVICE_PEN);
        } catch (SsdkUnsupportedException e)
        {
            if (processUnsupportedException(e))
            {
                this.spenAvailable = false;
            }
        } catch (final Exception e1)
        {
            this.spenAvailable = false;
        }
    }

    // Getters
    public boolean getSpenAvailable()
    {
        return this.spenAvailable;
    }

    private boolean processUnsupportedException(final SsdkUnsupportedException e)
    {

        e.printStackTrace();
        int errType = e.getType();
        // If the device is not a Samsung device or if the device does not support Pen.
        if (errType == SsdkUnsupportedException.VENDOR_NOT_SUPPORTED
            || errType == SsdkUnsupportedException.DEVICE_NOT_SUPPORTED)
        {
            // TODO: Nothing really
            return false;
        } else if (errType == SsdkUnsupportedException.LIBRARY_NOT_INSTALLED)
        {
            // If SpenSDK APK is not installed.
            // TODO: Prompt the user
            return false;
        } else if (errType
            == SsdkUnsupportedException.LIBRARY_UPDATE_IS_REQUIRED)
        {
            // SpenSDK APK must be updated.
            // TODO: Prompt the user
            return false;
        } else if (errType
            == SsdkUnsupportedException.LIBRARY_UPDATE_IS_RECOMMENDED)
        {
            // Update of SpenSDK APK to an available new version is recommended.
            // TODO: Prompt the user
            return false;
        }
        return true;
    }
}
