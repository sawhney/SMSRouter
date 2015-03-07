package com.mubhack2015.smsrouter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.support.v7.app.ActionBarActivity;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    //private HashMap<String, String> publicKeys = new HashMap<String, String>();
    //private HashMap<String, String> privateKeys = new HashMap<String, String>();
    private ArrayList<String> numbers = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Read the numbers store and show what's in it
        readNetworkDb(this);

        TextView txt = (TextView)findViewById(R.id.outputText);
        txt.setText("");

        for (String num : numbers) {
            txt.append(num + "\n");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void readNetworkDb(MainActivity activity) {
        try {
            StringBuffer stringBuffer = new StringBuffer();
            Resources res = activity.getResources();
            XmlResourceParser xpp = res.getXml(R.xml.network_db);
            xpp.next();
            int eventType = xpp.getEventType();

            String num = "";

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {
                    stringBuffer.append("--- Start XML ---");
                } else if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equals("num")) {
                        xpp.next();
                        num = xpp.getText();
                        Log.e("Number", num);
                        numbers.add(num);
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    stringBuffer.append("\nEND_TAG: " + xpp.getName());
                } else if (eventType == XmlPullParser.TEXT) {
                    stringBuffer.append("\nTEXT: " + xpp.getText());
                }

                eventType = xpp.next();
            }
            stringBuffer.append("\n--- End XML ---");
            Log.e("Parser output", stringBuffer.toString());
        } catch (XmlPullParserException e) {
            Log.e("Parser exception", e.getMessage());
        } catch (IOException e) {
            Log.e("Parser exception", e.getMessage());
        } catch (Exception e) {
            Log.e("Parser exception", "Unknown exception");
        }
    }
}

/*
public class AndroidXmlResource extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        TextView myXmlContent = (TextView)findViewById(R.id.my_xml);
        String stringXmlContent;
        try {
            stringXmlContent = getEventsFromAnXML(this);
            myXmlContent.setText(stringXmlContent);
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private String getEventsFromAnXML(Activity activity)
            throws XmlPullParserException, IOException
    {
        StringBuffer stringBuffer = new StringBuffer();
        Resources res = activity.getResources();
        XmlResourceParser xpp = res.getXml(R.xml.myxml);
        xpp.next();
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT)
        {
            if(eventType == XmlPullParser.START_DOCUMENT)
            {
                stringBuffer.append("--- Start XML ---");
            }
            else if(eventType == XmlPullParser.START_TAG)
            {
                stringBuffer.append("\nSTART_TAG: "+xpp.getName());
            }
            else if(eventType == XmlPullParser.END_TAG)
            {
                stringBuffer.append("\nEND_TAG: "+xpp.getName());
            }
            else if(eventType == XmlPullParser.TEXT)
            {
                stringBuffer.append("\nTEXT: "+xpp.getText());
            }
            eventType = xpp.next();
        }
        stringBuffer.append("\n--- End XML ---");
        return stringBuffer.toString();
    }
}*/