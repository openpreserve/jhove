package edu.harvard.hul.ois.jhove.module;

import edu.harvard.hul.ois.jhove.Property;
import edu.harvard.hul.ois.jhove.RepInfo;
import junit.framework.TestCase;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
/*
The State and University of Aarhus PLANETS project.
Author Asger Blekinge-Rasmussen
Copyright (C) 2008  The State and University Library

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
import junit.framework.*;
import edu.harvard.hul.ois.jhove.module.PdfModule;
*/

public class PdfModuleTest extends TestCase {

    PdfModule pdfModule = new PdfModule();


    public void testParsePDFA_lvlA1() throws Exception {
        parseTest("examples/pdf/imd.pdf");
    }

    public void testParsePDFA_lvlA2() throws Exception {
        parseTest("examples/pdf/AA_Banner-single.pdf");
    }


    public void testParsePDFA_lvlA3() throws Exception {
        parseTest("examples/pdf/AA_Banner.pdf");
    }


    public void testParsePDFA_lvlA4() throws Exception {

        parseTest("examples/pdf/bedfordcompressed.pdf");
    }


    public void testParsePDFA_lvlA5() throws Exception {
        parseTest("examples/pdf/fallforum03.pdf");
    }


    public void parseTest(String name){

        try {
            RandomAccessFile raf = new RandomAccessFile(name,"r");
            RepInfo info = new RepInfo(name);

            pdfModule.parse(raf, info);
            if (info.getWellFormed() == RepInfo.TRUE){
                if(info.getValid() == RepInfo.TRUE){
                    if (info.getProfile().contains("ISO PDF/A-1, Level B")){
                        fail("Should not have been pdf/a");
                    } else{
                        List reasons = (List) (info.getProperty(
                                "Profile NonCompliance Reasons").getValue());
                        for (int i=0;i<reasons.size(); i++){
                            if (((Property)(reasons.get(i))).getName().equals("ISO PDF/A-1, Level B")){
                                List theseReasons = (List)(((Property)(reasons.get(i))).getValue());
                                for (int j=0;j<theseReasons.size(); j++){
                                    Property reason = (Property) theseReasons.get(j);
                                    System.out.println(reason.getValue());
                                }
                            }
                        }
                    }

                }
            }

        }
        catch (IOException ex) {
        }



    }
}