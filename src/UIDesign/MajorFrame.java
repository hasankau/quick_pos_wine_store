/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package UIDesign;

import java.awt.Component;
import java.awt.HeadlessException;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author HASANKA
 */
public class MajorFrame extends JFrame{

    public static String WIN_NAME;
    
    public MajorFrame() throws HeadlessException {
        //setUndecorated(true);
        
        setPreferredSize(UIDesign.ScreenRes.getSize());
        setMaximumSize(UIDesign.ScreenRes.getSize());


    }
    
    public void setWindowUndecorated(boolean b){
        setUndecorated(b);
    }
    
}
