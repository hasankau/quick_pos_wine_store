/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBClasses;


import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author HASANKA
 */
public class DB_query_expen {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;

    public DB_query_expen() {
        try {
            java_connect c = java_connect.getInstance();
            conn = c.DBConnection();
            conn.setAutoCommit(true);
        } catch (SQLException ex) {
            Logger.getLogger(DB_query_subtypes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

  
   
    
    
  
    
    public boolean insertIntoExpen(String desc, String date, String amount, String uid) {
        String sql = "insert into expences(expences.desc, date, amount, user_id)values('"+desc+"', '"+date+"', '"+amount+"', '"+uid+"')";

        try {

            pst = conn.prepareStatement(sql);
            pst.execute();
            
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occured!"+e, "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (Exception e) {
            }
        }

        return false;
    }
    
    public boolean deleteExpen(String id) {
        String sql = "delete from expences where expences.id = '"+id+"'";

        try {

            pst = conn.prepareStatement(sql);
            pst.execute();
            
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occured!"+e, "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (Exception e) {
            }
        }

        return false;
    }
    
    
    
   


}
