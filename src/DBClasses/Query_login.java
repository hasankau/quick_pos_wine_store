/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DBClasses;

import Data.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

/**
 *
 * @author HASANKA
 */
public class Query_login {
    
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;

    public Query_login() {
        java_connect c = java_connect.getInstance();
        conn = c.DBConnection();
    }
    
    
    
    public User getUser(String name, String pw){
        
        
        String sql = "SELECT * from user where user='"+name+"'";
        User u = new User();
        try {
            
            pst = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = pst.executeQuery();
            if (rs.first()) { 
                
                    u.setId(Integer.parseInt(rs.getString(1).trim()));
                    u.setUserName(rs.getString(2));
                    u.setUserRole(Integer.parseInt(rs.getString(4).trim()));
                
            }
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
        
        return u;
    }
    
        public boolean loging(String userName, String password) {
        if (!userName.equals("")) {
            if (!password.equals("")) {
                String sql = "";
                
                    sql = "select id,password from user where user=?";
                
                try {
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, userName);
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        String HashPwd = rs.getString("password");
                        rs.close();
                        pst.close();
                        if (PasswordHash.validatePassword(password, HashPwd)) {

                            return true;

                        } else {
                            JOptionPane.showMessageDialog(null, "Username or password incorrect", "Invallid login", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Username or password incorrect", "Invallid login", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please insert valid password");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please insert valid username");
        }

        return false;
    }

    public boolean saveLogin(int id, String date, String time) {
        String sql = "insert into login (date, in_time, user_id) value('" + date + "', '" + time + "', '" + id + "')";
        try {

            pst = conn.prepareStatement(sql);
            pst.execute();

            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occured! yrdr yd5e" + e, "Error", JOptionPane.ERROR_MESSAGE);
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
    
    public boolean saveLogout(int id, String time) {
        String sql = "update login set out_time = '" + time + "' where user_id = '" + id + "' order by id limit 1";
        try {

            pst = conn.prepareStatement(sql);
            pst.execute();

            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occured! yrdr yd5e" + e, "Error", JOptionPane.ERROR_MESSAGE);
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
