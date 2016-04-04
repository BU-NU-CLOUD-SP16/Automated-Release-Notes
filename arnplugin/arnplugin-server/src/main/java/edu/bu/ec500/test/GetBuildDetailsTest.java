package edu.bu.ec500.test;

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
public class GetBuildDetailsTest {

    @Test
    public void testGetWorkItems(){
        ArrayList<String> workItems = new ArrayList<String>();
        System.out.println();
        try {
            workItems = GetBuildDetails.getWorkItems();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetLatestBuild(){
        try {
            String builNo = GetBuildDetails.getLatestBuild();
            System.out.println(builNo);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
}