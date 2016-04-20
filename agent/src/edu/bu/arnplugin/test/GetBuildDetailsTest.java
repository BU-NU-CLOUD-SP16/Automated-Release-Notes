/*
 * Copyright 2000-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.bu.arnplugin.test;

import edu.bu.arnplugin.tc.GetBuildDetails;
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
/*
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
*/
    }

    @Test
    public void testGetLatestBuild(){
        // try {
        //     String builNo = GetBuildDetails.getLatestBuild();
        //     System.out.println(builNo);

        // } catch (IOException e) {
        //     e.printStackTrace();
        // } catch (ParserConfigurationException e) {
        //     e.printStackTrace();
        // } catch (SAXException e) {
        //     e.printStackTrace();
        // }
    }
}
