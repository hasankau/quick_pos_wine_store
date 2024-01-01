/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Home;

import DBClasses.DB_invoice;
import DBClasses.DB_query_invoice;
import DBClasses.java_connect;
import Data.Customer;
import Data.Item;
import UIDesign.PanelFrame;
import UIDesign.Theme;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;

/**
 *
 * @author HASANKA
 */
public class Cashier extends PanelFrame {

    DBClasses.DB_query_invoice db = new DB_query_invoice();
    DBClasses.DB_invoice dbi = new DB_invoice();
    Home home = null;
    PopupView1 pop = null;
    DefaultTableModel dtm;

    /**
     * Creates new form Inventory
     */
    public Cashier() {
        initComponents();
        tb.update_table("select barcode, name, description, manufacturer from item", jTable1);
        up.setText("0.0");
        setInno();
        item_code.grabFocus();
        pm.setModel(new DefaultComboBoxModel(db.getPaymentMethods()));
        setHotKeys();
        cust.setText(db.loadCustmer());

        this.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String key = item_code.getText().trim();
                if (!key.isEmpty()) {

                    try {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                            String barc = item_code.getText().trim();
                            Item item = db.getItem(barc);

                            if (item != null) {
                                if ("Item".equals(item.getType())) {

                                    item_code.setText(item.getBarcode());
                                    name.setText(item.getName());
                                    up.setText(item.getCash_price());
                                    qty.setText("1");
                                    aqty.setText(item.getQty());
                                    disc.setText("0");
                                    addSubTotal();
                                    disc.grabFocus();
                                    disc.selectAll();
                                } else if ("NonItem".equals(item.getType())) {

                                    //pop = new PopupView1(home, this, item);
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Could not find item!", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, ex);
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a valid barcode number", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }

    public Cashier(Home home) {
        this();
        this.home = home;
    }

    private void setHotKeys() {
        prinout.setMnemonic(KeyEvent.VK_Q);
        photo.setMnemonic(KeyEvent.VK_W);
        lamin.setMnemonic(KeyEvent.VK_E);
        bind.setMnemonic(KeyEvent.VK_R);

        add.setMnemonic(KeyEvent.VK_A);
        del.setMnemonic(KeyEvent.VK_D);
        del1.setMnemonic(KeyEvent.VK_F);
        sav.setMnemonic(KeyEvent.VK_S);
        print.setMnemonic(KeyEvent.VK_P);
        res.setMnemonic(KeyEvent.VK_Z);
    }

    /**
     * Design methods
     *
     * @param bt
     */
    /**
     * Select from table
     *
     */
    private void selectItemStock() {
        try {

            int row = jTable1.getSelectedRow();
            String barc = jTable1.getModel().getValueAt(row, 0).toString().trim();
            item_code.setText(barc);
            Item item = db.getItem(barc);

            if (item != null) {
                if ("Item".equals(item.getType())) {

                    name.setText(jTable1.getModel().getValueAt(row, 1).toString().trim());
                    up.setText(item.getCash_price());
                    qty.setText("1");
                    aqty.setText(item.getQty());
                    addSubTotal();
                    qty.grabFocus();
                    qty.selectAll();
                } else if ("NonItem".equals(item.getType())) {

                    pop = new PopupView1(home, this, item);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void setInno() {

        String inv = db.getInno();
        if (inv == null) {
            inv = "0001";
        } else {
            inv = db.getInno();
        }
        inno.setText(inv);
    }

    private void clearFields() {
        item_code.setText("");
        name.setText("");
        up.setText("0");
        disc.setText("0");
        qty.setText("0");
        sbt.setText("0");
        tot.setText("0.0");
        py.setText("0");
        bal.setText("0");
        setInno();
    }

    public void addNonItem(Item item) {
        Vector v = new Vector();
        v.add(item.getBarcode());
        v.add(item.getName());
        v.add(item.getCash_price());
        v.add("0");
        v.add(item.getQty());

        BigDecimal r = new BigDecimal(item.getCash_price());
        BigDecimal q = new BigDecimal(item.getQty());
        BigDecimal p = q.multiply(r);

        item.setCash_price(p.toPlainString());

        v.add(p.toPlainString());
        dtm = (DefaultTableModel) jTable3.getModel();

        boolean b = false;
        int id = 0;
        check:
        if (dtm.getRowCount() > 0) {
            for (int i = 0; i < dtm.getRowCount(); i++) {
                if (jTable3.getValueAt(i, 0).toString().equals(item.getBarcode())
                        & jTable3.getValueAt(i, 1).toString().equals(item.getName())) {
                    id = i;
                    b = true;
                    break;
                }
            }
        }

        if (b) {

            BigDecimal s = new BigDecimal(item.getCash_price().trim())
                    .add(new BigDecimal(dtm.getValueAt(id, 5).toString().trim()));

            price = new BigDecimal(item.getCash_price().trim())
                    .subtract(new BigDecimal(disc.getText()));
            q = new BigDecimal("1");
            BigDecimal sub = price.multiply(q);
            sbt.setText(sub.toPlainString());

            dtm.setValueAt(item.getCash_price().trim(), id, 2);
            dtm.setValueAt(s.toPlainString(), id, 5);
            jTable3.setSelectionBackground(Theme.btColorOrange);
            jTable3.setRowSelectionInterval(id, id);

            ft = new BigDecimal(tot.getText());
            ft = ft.add(new BigDecimal(sbt.getText()));
            tot.setText(ft.toPlainString());

        } else {
            price = new BigDecimal(item.getCash_price().trim())
                    .subtract(new BigDecimal(disc.getText()));
            q = new BigDecimal("1");
            BigDecimal sub = price.multiply(q);
            sbt.setText(sub.toPlainString());

            dtm.addRow(v);
            jTable3.setSelectionBackground(Theme.navColorSelect);
            jTable3.setRowSelectionInterval(jTable3.getRowCount() - 1, jTable3.getRowCount() - 1);

            ft = new BigDecimal(tot.getText());
            ft = ft.add(new BigDecimal(sbt.getText()));
            tot.setText(ft.toPlainString());
        }
    }

    private void addSubTotal() {
        if (qty.getText().length() == 0) {
            qty.setText("1");
        }
        price = new BigDecimal(up.getText().toString().trim())
                .subtract(new BigDecimal(disc.getText()));
        q = new BigDecimal(qty.getText());
        BigDecimal sub = price.multiply(q);
        sbt.setText(sub.toPlainString());
    }

    private void addToTable() {
        int ji = JOptionPane.NO_OPTION;
        if (!item_code.getText().isEmpty()) {

            dtm = (DefaultTableModel) jTable3.getModel();
            String barc = item_code.getText();
            boolean b = false;
            int id = 0;
            check:
            if (dtm.getRowCount() > 0) {
                for (int i = 0; i < dtm.getRowCount(); i++) {
                    if (jTable3.getValueAt(i, 0).toString().equals(barc)) {
                        id = i;
                        b = true;
                        break;
                    }
                }
            }

            Vector<String> v = new Vector<>();
            v.add(item_code.getText());
            v.add(name.getText());
            v.add(up.getText().toString());
            v.add(disc.getText());
            v.add(qty.getText());
            v.add(sbt.getText());

            Item item = db.getItem(barc);

            double eqty = Double.parseDouble(item.getQty());
            double nqty = Double.parseDouble(qty.getText().trim());

            if (eqty >= nqty) {

                if (b) {
                    BigDecimal qt = new BigDecimal(qty.getText().trim())
                            .add(new BigDecimal(dtm.getValueAt(id, 4).toString().trim()));
                    BigDecimal s = new BigDecimal(sbt.getText().trim())
                            .add(new BigDecimal(dtm.getValueAt(id, 5).toString().trim()));

                    if (eqty >= qt.doubleValue()) {

                        dtm.setValueAt(up.getText().trim(), id, 2);
                        dtm.setValueAt(qt.toPlainString(), id, 4);
                        dtm.setValueAt(s.toPlainString(), id, 5);
                        jTable3.setSelectionBackground(Theme.btColorOrange);
                        jTable3.setRowSelectionInterval(id, id);

                        ft = new BigDecimal(tot.getText());
                        ft = ft.add(new BigDecimal(sbt.getText()));
                        tot.setText(ft.toPlainString());
                    } else {
                        JOptionPane.showMessageDialog(null, "Quantity is less than " + qt.doubleValue() + ". Existing Quantity is " + eqty);
                    }
                } else {
                    dtm.addRow(v);
                    jTable3.setSelectionBackground(Theme.navColorSelect);
                    jTable3.setRowSelectionInterval(jTable3.getRowCount() - 1, jTable3.getRowCount() - 1);

                    ft = new BigDecimal(tot.getText());
                    ft = ft.add(new BigDecimal(sbt.getText()));
                    tot.setText(ft.toPlainString());
                }

            } else {
                JOptionPane.showMessageDialog(null, "Quantity is less than " + nqty + ". Existing Quantity is " + eqty);
            }
            item_code.grabFocus();
        }

    }

    public void copyCustomer(String id) {
        cust.setText(id);
    }

    String adid = "";

    public void copyAdvanceId(String id) {
        adid = id;
        setAdvanceInvoice(id);

    }

    private synchronized void setAdvanceInvoice(String id) {
        Vector v = dbi.getItem(id);
        Vector vi = new Vector();
        dtm = (DefaultTableModel) jTable3.getModel();

        for (int i = 0; i < v.size(); i++) {
            Item it = (Item) v.get(i);
            vi.add(it.getBarcode());
            vi.add(it.getName());
            vi.add(it.getCash_price());
            vi.add("0");
            vi.add(it.getAqty());
            vi.add(it.getAdis());
            dtm.addRow(vi);
        }

        Vector v2 = dbi.getAdvance(id);
        tot.setText(v2.get(1).toString());
        py.setText(v2.get(0).toString());

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        item_code = new javax.swing.JTextField();
        name = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        add = new javax.swing.JButton();
        sav = new javax.swing.JButton();
        del = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        print = new javax.swing.JButton();
        tot = new javax.swing.JLabel();
        qty = new javax.swing.JTextField();
        sbt = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        py = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        pm = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        bal = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        up = new javax.swing.JTextField();
        disc = new javax.swing.JTextField();
        del1 = new javax.swing.JButton();
        card = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        aqty = new javax.swing.JLabel();
        sav1 = new javax.swing.JButton();
        sav2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        res = new javax.swing.JButton();
        inno = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        prinout = new javax.swing.JButton();
        photo = new javax.swing.JButton();
        lamin = new javax.swing.JButton();
        bind = new javax.swing.JButton();
        cust = new javax.swing.JTextField();
        logo = new javax.swing.JLabel();

        jMenuItem1.setText("Delete Item");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        jMenuItem2.setText("Delete All");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem2);

        jScrollPane4.setMinimumSize(new java.awt.Dimension(23, 168));
        jScrollPane4.setPreferredSize(new java.awt.Dimension(452, 168));

        jTable3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jTable3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Itemcode", "Name", "Rate", "Discount", "Qty", "Sub Total"
            }
        ));
        jTable3.setMaximumSize(new java.awt.Dimension(2147483647, 225));
        jTable3.setRowHeight(30);
        jTable3.setSelectionBackground(new java.awt.Color(0, 153, 153));
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3MouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTable3);

        jPanel2.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Item Code");

        item_code.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                item_codeActionPerformed(evt);
            }
        });
        item_code.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                item_codeKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                item_codeKeyTyped(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Name");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Paid Amount");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("Unit Price");

        add.setBackground(new java.awt.Color(0, 153, 153));
        add.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        add.setForeground(new java.awt.Color(255, 255, 255));
        add.setText("Add");
        add.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        add.setContentAreaFilled(false);
        add.setOpaque(true);
        add.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                addMouseExited(evt);
            }
        });
        add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addActionPerformed(evt);
            }
        });

        sav.setBackground(new java.awt.Color(0, 153, 153));
        sav.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        sav.setForeground(new java.awt.Color(255, 255, 255));
        sav.setText("Save Invoice");
        sav.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        sav.setContentAreaFilled(false);
        sav.setOpaque(true);
        sav.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                savMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                savMouseExited(evt);
            }
        });
        sav.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savActionPerformed(evt);
            }
        });

        del.setBackground(new java.awt.Color(0, 153, 153));
        del.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        del.setForeground(new java.awt.Color(255, 255, 255));
        del.setText("Delete");
        del.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        del.setContentAreaFilled(false);
        del.setOpaque(true);
        del.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                delMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                delMouseExited(evt);
            }
        });
        del.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setText("Qty ");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText("<html>Total <font color='orange'>LKR</font></html>");

        print.setBackground(new java.awt.Color(255, 153, 0));
        print.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        print.setForeground(new java.awt.Color(255, 255, 255));
        print.setText("Print Invoice");
        print.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        print.setContentAreaFilled(false);
        print.setOpaque(true);
        print.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                printMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                printMouseExited(evt);
            }
        });
        print.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printActionPerformed(evt);
            }
        });

        tot.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        tot.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        tot.setText("0.0");

        qty.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        qty.setText("0");
        qty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                qtyActionPerformed(evt);
            }
        });
        qty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                qtyKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                qtyKeyTyped(evt);
            }
        });

        sbt.setEditable(false);
        sbt.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        sbt.setText("0");
        sbt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                sbtKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                sbtKeyTyped(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setText("Sub Total");

        py.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        py.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        py.setText("0");
        py.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pyKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pyKeyTyped(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel12.setText("Balance");

        pm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pmActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel13.setText("Payment Method");

        bal.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        bal.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        bal.setText("0.0");

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel15.setText("<html>Discount <font color='orange'>%</font></html>");

        up.setEditable(false);

        disc.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        disc.setText("0");
        disc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                discActionPerformed(evt);
            }
        });

        del1.setBackground(new java.awt.Color(0, 153, 153));
        del1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        del1.setForeground(new java.awt.Color(255, 255, 255));
        del1.setText("Delete All");
        del1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        del1.setContentAreaFilled(false);
        del1.setOpaque(true);
        del1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                del1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                del1MouseExited(evt);
            }
        });
        del1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                del1ActionPerformed(evt);
            }
        });

        card.setEditable(false);
        card.setForeground(new java.awt.Color(153, 153, 153));

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("Available Qty");

        aqty.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        aqty.setForeground(new java.awt.Color(0, 153, 153));
        aqty.setText("***");

        sav1.setBackground(new java.awt.Color(255, 51, 51));
        sav1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        sav1.setForeground(new java.awt.Color(255, 255, 255));
        sav1.setText("+");
        sav1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        sav1.setContentAreaFilled(false);
        sav1.setOpaque(true);
        sav1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                sav1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                sav1MouseExited(evt);
            }
        });
        sav1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sav1ActionPerformed(evt);
            }
        });

        sav2.setBackground(new java.awt.Color(255, 51, 51));
        sav2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        sav2.setForeground(new java.awt.Color(255, 255, 255));
        sav2.setText("Advances");
        sav2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        sav2.setContentAreaFilled(false);
        sav2.setOpaque(true);
        sav2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                sav2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                sav2MouseExited(evt);
            }
        });
        sav2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sav2ActionPerformed(evt);
            }
        });

        jButton1.setText("+");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(aqty, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(card, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(add, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(del, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(del1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(226, 226, 226)
                        .addComponent(sav, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(print, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(item_code, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(up, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(name, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(qty, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                            .addComponent(sbt, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(disc, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(sav2, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(sav1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addComponent(tot, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(2, 2, 2)
                                .addComponent(bal, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel13)
                                    .addGap(6, 6, 6)
                                    .addComponent(pm, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel3)
                                    .addGap(30, 30, 30)
                                    .addComponent(py, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(item_code, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(tot, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jLabel4))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(up, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(disc, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(4, 4, 4)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(py, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(qty, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(5, 5, 5)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(bal, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(sbt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                            .addComponent(pm, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                            .addComponent(card)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(aqty))))
                .addGap(13, 13, 13)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(add, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(del, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(del1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(print, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(sav, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(sav1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(sav2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(51, 51, 51));
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/white/SEARCH_16x16-32.png"))); // NOI18N
        jButton5.setContentAreaFilled(false);
        jButton5.setOpaque(true);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        res.setBackground(new java.awt.Color(0, 153, 153));
        res.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        res.setForeground(new java.awt.Color(255, 255, 255));
        res.setText("Reset");
        res.setContentAreaFilled(false);
        res.setMinimumSize(new java.awt.Dimension(50, 30));
        res.setOpaque(true);
        res.setPreferredSize(new java.awt.Dimension(50, 30));
        res.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                resMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                resMouseExited(evt);
            }
        });
        res.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resActionPerformed(evt);
            }
        });

        inno.setEditable(false);
        inno.setMinimumSize(new java.awt.Dimension(50, 30));
        inno.setPreferredSize(new java.awt.Dimension(50, 30));

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setText("INV No");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel9.setText("Customer");

        jButton7.setBackground(new java.awt.Color(51, 51, 51));
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/white/SEARCH_16x16-32.png"))); // NOI18N
        jButton7.setContentAreaFilled(false);
        jButton7.setMinimumSize(new java.awt.Dimension(49, 30));
        jButton7.setOpaque(true);
        jButton7.setPreferredSize(new java.awt.Dimension(49, 30));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jScrollPane1.setMinimumSize(new java.awt.Dimension(23, 168));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(452, 168));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.setMaximumSize(new java.awt.Dimension(2147483647, 175));
        jTable1.setRowHeight(30);
        jTable1.setSelectionBackground(new java.awt.Color(0, 153, 153));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jPanel1.setLayout(new java.awt.GridLayout(1, 4, 2, 0));

        prinout.setBackground(new java.awt.Color(0, 153, 153));
        prinout.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        prinout.setForeground(new java.awt.Color(255, 255, 255));
        prinout.setText("Photocopy");
        prinout.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        prinout.setContentAreaFilled(false);
        prinout.setOpaque(true);
        prinout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prinoutActionPerformed(evt);
            }
        });
        jPanel1.add(prinout);

        photo.setBackground(new java.awt.Color(0, 204, 102));
        photo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        photo.setForeground(new java.awt.Color(255, 255, 255));
        photo.setText("Printout");
        photo.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        photo.setContentAreaFilled(false);
        photo.setOpaque(true);
        photo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                photoActionPerformed(evt);
            }
        });
        jPanel1.add(photo);

        lamin.setBackground(new java.awt.Color(255, 102, 102));
        lamin.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lamin.setForeground(new java.awt.Color(255, 255, 255));
        lamin.setText("Laminating");
        lamin.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lamin.setContentAreaFilled(false);
        lamin.setOpaque(true);
        lamin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                laminActionPerformed(evt);
            }
        });
        jPanel1.add(lamin);

        bind.setBackground(new java.awt.Color(255, 153, 51));
        bind.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bind.setForeground(new java.awt.Color(255, 255, 255));
        bind.setText("Binding");
        bind.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        bind.setContentAreaFilled(false);
        bind.setOpaque(true);
        bind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bindActionPerformed(evt);
            }
        });
        jPanel1.add(bind);

        cust.setEditable(false);

        logo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/dcnewflatcut2.png"))); // NOI18N
        logo.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        logo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        logo.setIconTextGap(0);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jButton5))
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(inno, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cust, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(res, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(logo)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jTextField1)
                        .addComponent(jButton5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(res, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(inno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cust))
                    .addComponent(jButton7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(logo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addMouseEntered
        setButtonOver(add, Theme.btColorMouseOver);
    }//GEN-LAST:event_addMouseEntered

    private void savMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_savMouseEntered
        //setButtonOver(sav, Theme.btColorMouseOver);
    }//GEN-LAST:event_savMouseEntered

    private void delMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_delMouseEntered
        setButtonOver(del, Theme.btColorMouseOver);
    }//GEN-LAST:event_delMouseEntered

    private void addMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addMouseExited
        setButtonExit(add, Theme.navColorSelect);
    }//GEN-LAST:event_addMouseExited

    private void savMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_savMouseExited
        //setButtonExit(sav, Theme.navColorSelect);
    }//GEN-LAST:event_savMouseExited

    private void delMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_delMouseExited
        setButtonExit(del, Theme.navColorSelect);
    }//GEN-LAST:event_delMouseExited

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        String key = "'" + jTextField1.getText().trim() + "%'";
        tb.update_table("select barcode, name, description, manufacturer from item where barcode like " + key + " or name like " + key + "  or manufacturer like " + key, jTable1);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        String key = "'" + jTextField1.getText().trim() + "%'";
        tb.update_table("select barcode, name, description, brand from item where barcode like " + key + " or name like " + key + " or manufacturer like " + key, jTable1);
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addActionPerformed
        addToTable();
    }//GEN-LAST:event_addActionPerformed

    private void resMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_resMouseEntered
        setButtonOver(res, Theme.btColorMouseOver);
    }//GEN-LAST:event_resMouseEntered

    private void resMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_resMouseExited
        setButtonExit(res, Theme.navColorSelect);
    }//GEN-LAST:event_resMouseExited

    private void resActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resActionPerformed
        clearFields();
        jTable1.getSelectionModel().clearSelection();
        dtm = (DefaultTableModel) jTable3.getModel();
        dtm.setRowCount(0);
        cust.setText(db.getMaxCusId());
        inno.setText(db.getInno());
    }//GEN-LAST:event_resActionPerformed

    private void printMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_printMouseEntered
        //  setButtonOver(print, Theme.btColorOrange);
    }//GEN-LAST:event_printMouseEntered

    private void printMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_printMouseExited
        // setButtonExit(print, Theme.btColorOrangeExit);
    }//GEN-LAST:event_printMouseExited

    private void delActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delActionPerformed
        dtm = (DefaultTableModel) jTable3.getModel();
        if (jTable3.getSelectedRows().length > 0) {

            int i = JOptionPane.showConfirmDialog(null, "Remove item?");
            if (i == JOptionPane.YES_OPTION) {
                ft = ft.subtract(new BigDecimal(jTable3.getValueAt(jTable3.getSelectedRow(), 5).toString().trim()));
                tot.setText(ft.toPlainString());
                dtm.removeRow(jTable3.getSelectedRow());
                //ft = new BigDecimal(tot.getText().trim());
                tot.setText(ft.toPlainString());

                payed = new BigDecimal(py.getText().trim());
                BigDecimal balance = payed.subtract(ft);
                balance = balance.setScale(2, RoundingMode.HALF_UP);
                bal.setText(balance.toPlainString());
            }
        } else {
            JOptionPane.showMessageDialog(null, "No row selected", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_delActionPerformed
    SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd");
    private void printActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printActionPerformed

        if (jTable3.getRowCount() > 0) {
            String in = inno.getText().trim();
            save();
            try {

                InputStream is = new FileInputStream(new File("src/invoice.jrxml"));

                JasperReport jr = JasperCompileManager.compileReport(is);
                Map m = new HashMap();
                m.put("iid", in);
                //JRTableModelDataSource tb = new JRTableModelDataSource(jTable1.getModel());            
                JasperPrint jp = JasperFillManager.fillReport(jr, m, java_connect.ConnecrDb());
                JasperPrintManager.printReport(jp, false);
                //JasperViewer.viewReport(jp, false);
            } catch (Exception e) {
                //e.printStackTrace();
                JOptionPane.showMessageDialog(null, e);
            }
        }

    }//GEN-LAST:event_printActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
//        new Customers(home, this);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        selectItemStock();
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable3MouseClicked

    SimpleDateFormat sdft = new SimpleDateFormat("hh.mm.ss a");

    private boolean save() {
        if (jTable3.getRowCount() > 0) {

            if (adid != null) {
                dbi.updateAdvance(adid);
                adid = null;
            }

            try {

                String invid = inno.getText();
                Date d = new Date();
                String date = sdf.format(d);
                String time = sdft.format(d);
                String to = tot.getText();
                String itemc = jTable3.getRowCount() + "";
                String balance = bal.getText();
                String uid = home.getCurrentUser().getId() + "";
                String paid = py.getText();
                String pym = pm.getSelectedIndex() + 1 + "";

                String cus = cust.getText().trim();

                String ref = card.getText();

                if (!db.isCustomerExists(cus)) {
                    db.saveCustomer("New", "Anon", "Anon");
                    cus = db.getMaxCusId();
                } else {
                    cus = cust.getText();
                }

                boolean b2 = false;
                boolean b = dbi.insertIntoInvoice(invid, date, to, itemc, balance, uid, paid, pym, cus, ref, time);
                if (b) {

                    for (int i = 0; i < jTable3.getRowCount(); i++) {

                        String barc = jTable3.getValueAt(i, 0).toString().trim();
                        String upr = jTable3.getValueAt(i, 2).toString().trim();
                        String dis = jTable3.getValueAt(i, 3).toString().trim();
                        String qt = jTable3.getValueAt(i, 4).toString().trim();
                        String sub = jTable3.getValueAt(i, 5).toString().trim();

//                        b = dbi.insertIntoInvoiceDetails(barc, invid, upr, qt, dis, sub);
                        if (b) {
                            b2 = dbi.updateStock(barc, qt);
                        }
                    }
                }
                if (b2) {
                    clearFields();
                    jTable1.getSelectionModel().clearSelection();
                    jTable3.getSelectionModel().clearSelection();
                    dtm = (DefaultTableModel) jTable3.getModel();
                    dtm.setRowCount(0);
                    cust.setText(db.loadCustmer());
                    return b2;
                }

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "An error occured!" + e, "Error", JOptionPane.ERROR_MESSAGE);

            }

        }
        return false;
    }

    private void savActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savActionPerformed

        if (save()) {
            JOptionPane.showMessageDialog(null, "Successful");

        }

    }//GEN-LAST:event_savActionPerformed
    BigDecimal price = new BigDecimal("0");
    BigDecimal q = new BigDecimal("0");
    BigDecimal ft = new BigDecimal("0");
    private void qtyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_qtyKeyReleased
        addSubTotal();
    }//GEN-LAST:event_qtyKeyReleased

    private void qtyKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_qtyKeyTyped
        int c = evt.getKeyChar();
        if (!(Character.isDigit(c) || evt.getKeyCode() == KeyEvent.VK_PERIOD)) {
            evt.consume();
        }
    }//GEN-LAST:event_qtyKeyTyped

    private void sbtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sbtKeyTyped
        int c = evt.getKeyChar();
        if (!(Character.isDigit(c) || evt.getKeyCode() == KeyEvent.VK_PERIOD)) {
            evt.consume();
        }
    }//GEN-LAST:event_sbtKeyTyped

    private void pyKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pyKeyTyped
        int c = evt.getKeyChar();
        if (!(Character.isDigit(c) || evt.getKeyCode() == KeyEvent.VK_PERIOD)) {
            evt.consume();
        }
    }//GEN-LAST:event_pyKeyTyped
    BigDecimal payed;
    private void pyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pyKeyReleased
        if (py.getText().length() == 0) {
            py.setText("0.0");
        }
        ft = new BigDecimal(tot.getText().trim());
        payed = new BigDecimal(py.getText().trim());
        BigDecimal balance = payed.subtract(ft);
        if (balance.doubleValue() >= BigDecimal.ZERO.doubleValue()) {
            bal.setText(balance.toPlainString());
        }
        if (balance.doubleValue() < 0.0) {
            sav.setEnabled(false);
            print.setEnabled(false);
            sav.setBackground(new Color(102, 102, 102));
            print.setBackground(new Color(102, 102, 102));

        } else {
            sav.setEnabled(true);
            print.setEnabled(true);
            print.setBackground(new Color(255, 153, 0));
            sav.setBackground(new Color(0, 153, 153));
        }

    }//GEN-LAST:event_pyKeyReleased

    private void item_codeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_item_codeKeyReleased

        String key = item_code.getText().trim();
        if (!key.isEmpty()) {

            try {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

                    String barc = item_code.getText().trim();
                    Item item = db.getItem(barc);

                    if (item != null) {
                        if ("Item".equals(item.getType())) {

                            item_code.setText(item.getBarcode());
                            name.setText(item.getName());
                            up.setText(item.getCash_price());
                            qty.setText("1");
                            aqty.setText(item.getQty());
                            disc.setText("0");
                            addSubTotal();
                            disc.grabFocus();
                            disc.selectAll();
                        } else if ("NonItem".equals(item.getType())) {

                            pop = new PopupView1(home, this, item);
                        }
                        item_code.setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "Could not find item!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, e);
            }

        } else {
            JOptionPane.showMessageDialog(null, "Please enter a valid barcode number", "Error", JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_item_codeKeyReleased

    private void item_codeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_item_codeActionPerformed
        String key = item_code.getText().trim();
        if (!key.isEmpty()) {

            try {

                String barc = item_code.getText().trim();
                Item item = db.getItem(barc);

                if (item != null) {
                    if ("Item".equals(item.getType())) {

                        item_code.setText(item.getBarcode());
                        name.setText(item.getName());
                        up.setText(item.getCash_price());
                        qty.setText("1");
                        aqty.setText(item.getQty());
                        disc.setText("0");
                        addSubTotal();
                        disc.grabFocus();
                        disc.selectAll();
                    } else if ("NonItem".equals(item.getType())) {

                        pop = new PopupView1(home, this, item);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Could not find item!", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, e);
            }

        } else {
            JOptionPane.showMessageDialog(null, "Please enter a valid barcode number", "Error", JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_item_codeActionPerformed

    private void sbtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sbtKeyReleased
        if (sbt.getText().length() == 0) {
            sbt.setText("0.0");
        }
    }//GEN-LAST:event_sbtKeyReleased

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        String key = "'" + jTextField1.getText().trim() + "%'";
        tb.update_table("select barcode, name, description, manufacturer from item where barcode like " + key + " or name like " + key + "  or manufacturer like " + key, jTable1);
    }//GEN-LAST:event_jTextField1KeyReleased

    private void prinoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prinoutActionPerformed

        Date d = new Date();
        Date to = null;
        try {
            to = sd.parse("2017/07/25");
        } catch (ParseException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        //if(d.before(to)){

        Item item = new Item();
        item.setBarcode("1001");
        item.setName("Photocopy");
        pop = new PopupView1(home, this, item);

//            }else{
//            JOptionPane.showMessageDialog(null, "Virtual memory exceeded");
//                }

    }//GEN-LAST:event_prinoutActionPerformed

    private void photoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_photoActionPerformed

        Date d = new Date();
        Date to = null;
        try {
            to = sd.parse("2017/07/25");
        } catch (ParseException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        //if(d.before(to)){

        Item item = new Item();
        item.setBarcode("1002");
        item.setName("Printout");
        pop = new PopupView1(home, this, item);

//        }else{
//            JOptionPane.showMessageDialog(null, "Virtual memory exceeded");
//                }
    }//GEN-LAST:event_photoActionPerformed
    SimpleDateFormat sd = new SimpleDateFormat("yyyy/MM/dd");
    private void laminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_laminActionPerformed

        Date d = new Date();
        Date to = null;
        try {
            to = sd.parse("2017/07/25");
        } catch (ParseException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        //if(d.before(to)){

        Item item = new Item();
        item.setBarcode("1003");
        item.setName("Laminating");
        pop = new PopupView1(home, this, item);

//        }else{
//            JOptionPane.showMessageDialog(null, "Virtual memory exceeded");
//                }

    }//GEN-LAST:event_laminActionPerformed

    private void bindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bindActionPerformed

        Date d = new Date();
        Date to = null;
        try {
            to = sd.parse("2017/07/25");
        } catch (ParseException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        //if(d.before(to)){

        Item item = new Item();
        item.setBarcode("1004");
        item.setName("Binding");
        pop = new PopupView1(home, this, item);

//        }else{
//            JOptionPane.showMessageDialog(null, "Virtual memory exceeded");
//                }

    }//GEN-LAST:event_bindActionPerformed

    private void qtyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_qtyActionPerformed
        addToTable();
    }//GEN-LAST:event_qtyActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        del.doClick();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void del1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_del1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_del1MouseEntered

    private void del1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_del1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_del1MouseExited

    private void del1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_del1ActionPerformed
        dtm = (DefaultTableModel) jTable3.getModel();
        if (jTable3.getSelectedRows().length > 0) {

            int i = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove all items?");

            if (i == JOptionPane.YES_OPTION) {
                ft = ft.subtract(new BigDecimal("0"));
                tot.setText(ft.toPlainString());
                dtm.setRowCount(0);

                tot.setText(ft.toPlainString());

                payed = new BigDecimal(py.getText().trim());
                BigDecimal balance = payed.subtract(ft);
                balance = balance.setScale(2, RoundingMode.HALF_UP);
                bal.setText(balance.toPlainString());
            }
        } else {
            JOptionPane.showMessageDialog(null, "No row selected", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_del1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        del1.doClick();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void pmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pmActionPerformed
        if (pm.getSelectedIndex() == 2) {
            card.setEditable(true);
        } else {
            card.setEditable(false);
            card.setText("");
        }
    }//GEN-LAST:event_pmActionPerformed

    private void sav1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sav1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_sav1MouseEntered

    private void sav1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sav1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_sav1MouseExited

    private void sav1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sav1ActionPerformed
        String code = JOptionPane.showInputDialog(null, "Are you sure you want to save this invoice as advance?");

        if (code != null) {
            try {

                String invid = inno.getText();
                Date d = new Date();
                String date = sdf.format(d);
                String to = tot.getText();
                String itemc = jTable3.getRowCount() + "";
                String balance = bal.getText();
                String uid = home.getCurrentUser().getId() + "";
                String paid = py.getText();
                String pym = pm.getSelectedIndex() + 1 + "";

                String cus = cust.getText().trim();

                String ref = card.getText();

                if (!db.isCustomerExists(cus)) {
                    db.saveCustomer("New", "Anon", "Anon");
                    cus = db.getMaxCusId();
                } else {
                    cus = cust.getText();
                }

                boolean b2 = false;
                boolean b = dbi.insertIntoAdvance(date, paid, to, uid, cus, code);
                if (b) {

                    for (int i = 0; i < jTable3.getRowCount(); i++) {

                        String barc = jTable3.getValueAt(i, 0).toString().trim();
                        String upr = jTable3.getValueAt(i, 2).toString().trim();
                        String dis = jTable3.getValueAt(i, 3).toString().trim();
                        String qt = jTable3.getValueAt(i, 4).toString().trim();
                        String sub = jTable3.getValueAt(i, 5).toString().trim();

                        b = dbi.insertIntoAdvanceDetails(barc, dbi.getAdno(), upr, qt, dis, sub);

                    }
                }
                if (b) {
                    JOptionPane.showMessageDialog(null, "Successful");
                    clearFields();
                    jTable1.getSelectionModel().clearSelection();
                    jTable3.getSelectionModel().clearSelection();
                    dtm = (DefaultTableModel) jTable3.getModel();
                    dtm.setRowCount(0);
                    cust.setText(db.loadCustmer());
                }

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "An error occured!" + e, "Error", JOptionPane.ERROR_MESSAGE);

            }
        }
    }//GEN-LAST:event_sav1ActionPerformed

    private void sav2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sav2MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_sav2MouseEntered

    private void sav2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sav2MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_sav2MouseExited

    private void sav2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sav2ActionPerformed
        new Advances(home, this);
    }//GEN-LAST:event_sav2ActionPerformed

    private void item_codeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_item_codeKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_item_codeKeyTyped

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (disc.getText().length() == 0) {
            disc.setText("0.0");
        } else {
            BigDecimal dis = new BigDecimal(disc.getText());
            BigDecimal stot = new BigDecimal(price.multiply(q).toPlainString());

            BigDecimal div = stot.multiply(dis).divide(new BigDecimal("100"));

            sbt.setText(stot.subtract(div).toPlainString());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void discActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_discActionPerformed
        if (disc.getText().length() == 0) {
            disc.setText("0.0");
        } else {
            BigDecimal dis = new BigDecimal(disc.getText());
            BigDecimal stot = new BigDecimal(price.multiply(q).toPlainString());

            BigDecimal div = stot.multiply(dis).divide(new BigDecimal("100"));

            sbt.setText(stot.subtract(div).toPlainString());
            qty.grabFocus();
            qty.selectAll();
        }
    }//GEN-LAST:event_discActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton add;
    private javax.swing.JLabel aqty;
    private javax.swing.JLabel bal;
    private javax.swing.JButton bind;
    private javax.swing.JTextField card;
    private javax.swing.JTextField cust;
    private javax.swing.JButton del;
    private javax.swing.JButton del1;
    private javax.swing.JTextField disc;
    private javax.swing.JTextField inno;
    private javax.swing.JTextField item_code;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JButton lamin;
    public static javax.swing.JLabel logo;
    private javax.swing.JTextField name;
    private javax.swing.JButton photo;
    private javax.swing.JComboBox pm;
    private javax.swing.JButton prinout;
    private javax.swing.JButton print;
    private javax.swing.JTextField py;
    private javax.swing.JTextField qty;
    private javax.swing.JButton res;
    private javax.swing.JButton sav;
    private javax.swing.JButton sav1;
    private javax.swing.JButton sav2;
    private javax.swing.JTextField sbt;
    private javax.swing.JLabel tot;
    private javax.swing.JTextField up;
    // End of variables declaration//GEN-END:variables

}
