package com.j03.mobileinput;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import Settings.AppSettings;

public class WelcomeActivity
    extends ActionBarActivity
    implements OnClickListener
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        AppSettings.getInstanceSetContext(this);

        //final Typeface sspr = Typeface.createFromAsset(getAssets(), "fonts/sspr.otf");
        //final Typeface sspl = Typeface.createFromAsset(getAssets(), "fonts/asspl.otf");

        final Button btnContinue = (Button) findViewById(R.id.contButton);
        //lblNext.setTypeface(sspr);

        final TextView i1 = (TextView) findViewById(R.id.txtWelc1);
        //i1.setTypeface(sspl);

        final TextView i3 = (TextView) findViewById(R.id.nlblPort);
        //i3.setTypeface(sspr);

        final String link = "<font color='#009688'>" + AppSettings.getInstance().getSystemSettings().getSystemInfo().getWebsite() + "</font>";
        i3.append("\n" +
            Html.fromHtml(link));

        btnContinue.setOnClickListener(this);
        i3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.contButton:
                finish();
                break;

            case R.id.nlblPort:
                final Intent i = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(AppSettings.getInstance().getSystemSettings().getSystemInfo().getWebsite()));
                startActivity(i);
                break;
        }
    }
}