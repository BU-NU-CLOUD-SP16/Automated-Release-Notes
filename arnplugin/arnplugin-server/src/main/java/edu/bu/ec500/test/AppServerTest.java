package edu.bu.ec500.test;

import edu.bu.ec500.arnplugin.AppServer;
import edu.bu.ec500.tc.GetBuildDetails;
import org.apache.http.client.ClientProtocolException;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by sesha on 4/3/2016.
 */
public class AppServerTest {

    @Test
    public void testGetAllVstsWorkItems(){
        AppServer obj = new AppServer();
        ArrayList<String> workItems = new ArrayList<String>();
        workItems.add("20");
        workItems.add("19");
        workItems.add("21");
        try {
            obj.getAllVstsWorkItems(workItems);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetVstsWorkItems(){
        AppServer obj = new AppServer();

        try {
            obj.getVstsWorkItems("19,20,21");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}