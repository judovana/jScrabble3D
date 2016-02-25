/*
 * StartUpScrabble.java
 *
 */
package scrabble;

import Cammons.*;
import InetGameControler.ClientPlayerCreator;
import InetGameControler.JoinedPlayer;
import InetGameControler.ServerPlayerCreator;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import scrabble.Settings.Settings;
import dictionaries.MyDictionary;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import requesty.VocabularyServer;

/**
 *
 * @author Jirka
 */
public class StartUpScrabble extends javax.swing.JFrame {

    Thread thread;
    ServerPlayerCreator spc;
    ClientPlayerCreator cpc = null;
    ArrayList<JoinedPlayer> joinedPlayers = null;
    private PaintComponent preview = new PaintComponent();
    public static StartUpScrabble jScrabelFrame;
    private DefaultListModel hraci;
    private Settings settings = new Settings();
    private DefaultComboBoxModel cb2 = new DefaultComboBoxModel();
    private DefaultComboBoxModel cb3 = new DefaultComboBoxModel();
    private DefaultComboBoxModel cb4 = new DefaultComboBoxModel();
    private DefaultListModel cb5 = new DefaultListModel();
    private DefaultListModel cb8 = new DefaultListModel();
    private DefaultListModel servers = new DefaultListModel();
    private DefaultListModel lschm;

    private class TestVlakno extends Thread {

        public boolean running = false;

        public void run() {
            running = true;
            try {
                boolean[] errors = new boolean[jList2.getSelectedValues().length];
                for (int i = 0; i < errors.length; i++) {
                    errors[i] = false;
                }
                DefaultListModel m = new DefaultListModel();
                jList4.setModel(m);

                for (int i = 0; i < jList2.getSelectedValues().length; i++) {
                    VocabularyServer s = new VocabularyServer(new File("Data" + File.separator + "servers" + File.separator + (String) jList2.getSelectedValues()[i] + ".xml"));

                    m.addElement((String) jList2.getSelectedValues()[i]);
                    m.addElement(s.getAddress());
                    m.addElement(s.getInfo());
                    m.addElement("Inner maxlenght of an word=" + s.getMax_word_length());
                    m.addElement("Inner encoding=" + s.getEncoding());

                    switch (s.getPrefered_method()) {
                        case (VocabularyServer.VS_BOTH):
                            m.addElement("Method=get and post=OK");
                            break;
                        case (VocabularyServer.VS_POST):
                            m.addElement("Method=post=OK");
                            break;
                        case (VocabularyServer.VS_GET):
                            m.addElement("Method=get=OK");
                            break;
                        default:
                            m.addElement("undefined mmethod = ERROR");
                            errors[i] = true;
                            break;
                    }

                    switch (s.getPrefered_search()) {
                        case (VocabularyServer.VS_BOTH):
                            m.addElement("search for failure and sucess=OK");
                            if (s.getWord_found().trim().equals("")) {
                                m.addElement("undefined constan for succes=ERROR");
                                errors[i] = true;
                            } else {
                                m.addElement("constan for succes=" + s.getWord_found() + "=OK");
                            }
                            if (s.getWord_notfound().trim().equals("")) {
                                m.addElement("undefined constan for filure=ERROR");
                                errors[i] = true;
                            } else {
                                m.addElement("constan for failure=" + s.getWord_notfound() + "=OK");
                            }
                            break;
                        case (VocabularyServer.VS_FOUND):
                            m.addElement("search for succes=OK");
                            if (s.getWord_found().trim().equals("")) {
                                m.addElement("undefined constan for succes=ERROR");
                                errors[i] = true;
                            } else {
                                m.addElement("constan for succes=" + s.getWord_found() + "=OK");
                            }
                            break;
                        case (VocabularyServer.VS_NOTFOUND):
                            m.addElement("search for failure=OK");
                            if (s.getWord_notfound().trim().equals("")) {
                                m.addElement("undefined constan for filure=ERROR");
                                errors[i] = true;
                            } else {
                                m.addElement("constan for failure=" + s.getWord_notfound() + "=OK");
                            }
                            break;
                        default:
                            m.addElement("undefined searching= ERROR");
                            break;
                    }

                    int r = s.testWord(jTextField4.getText());
                    switch (r) {
                        case (VocabularyServer.VS_FOUND):
                            m.addElement(jTextField4.getText() + " have been found=OK");
                            break;
                        case (VocabularyServer.VS_TOLONG):
                            m.addElement(jTextField4.getText() + " have been signed as too long=ERROR");
                            errors[i] = true;
                            break;
                        case (VocabularyServer.VS_NOTFOUND):
                            m.addElement(jTextField4.getText() + " have NOT been found=ERROR");
                            errors[i] = true;
                            break;
                        default:
                            m.addElement(jTextField4.getText() + " Unsuported error=ERROR");
                            errors[i] = true;
                            break;
                    }
                    r = s.testWord(jTextField5.getText());
                    switch (r) {
                        case (VocabularyServer.VS_FOUND):
                            m.addElement(jTextField5.getText() + " have been found=ERROR");
                            errors[i] = true;
                            break;
                        case (VocabularyServer.VS_TOLONG):
                            m.addElement(jTextField5.getText() + " have been signed as too long=ERROR");
                            errors[i] = true;
                            break;
                        case (VocabularyServer.VS_NOTFOUND):
                            m.addElement(jTextField5.getText() + " have NOT been found=OK");
                            break;
                        default:
                            m.addElement(jTextField4.getText() + " Unsuported error=ERROR");
                            errors[i] = true;
                            break;
                    }
                    String q = "";
                    for (int j = 0; j <= s.getMax_word_length(); j++) {
                        q = q + "Q";

                    }
                    r = s.testWord(q);
                    switch (r) {
                        case (VocabularyServer.VS_FOUND):
                            m.addElement(q + " have been found=ERROR");
                            errors[i] = true;
                            break;
                        case (VocabularyServer.VS_TOLONG):
                            m.addElement(q + " have been signed as too long=OK");
                            break;
                        case (VocabularyServer.VS_NOTFOUND):
                            m.addElement(q + " have NOT been found=ERROR");
                            errors[i] = true;
                            break;
                        default:
                            m.addElement(jTextField4.getText() + " Unsuported error=ERROR");
                            errors[i] = true;
                            ;
                            break;
                    }
                    m.addElement("***********");
                }
                m.addElement("Finished, ");
                boolean er = false;
                for (int i = 0; i < errors.length; i++) {
                    if (errors[i] == true) {
                        er = true;
                    }
                }
                if (!er) {
                    m.addElement("no errors");
                } else {
                    int s[] = jList2.getSelectedIndices();
                    for (int i = 0; i < errors.length; i++) {
                        if (errors[i] == true) {
                            s[i] = -1;
                        }
                    }
                    int sell = 0;
                    for (int i = 0; i < errors.length; i++) {
                        if (s[i] != -1) {
                            sell++;
                        }
                    }
                    int[] sele = new int[sell];
                    sell = 0;
                    for (int i = 0; i < errors.length; i++) {
                        if (s[i] != -1) {
                            sele[sell] = s[i];
                            sell++;
                        }
                    }
                    jList2.setSelectedIndices(sele);
                    m.addElement("Servers with errors unselected");
                }
            } finally {
                running = false;
            }
        }
    ;

    }
    /** Creates new form StartUpScrabble */
    public StartUpScrabble() {
        /*System.out.println("libs:");
         System.out.println(System.getProperty("java.library.path"));
         String lp=System.getProperty("java.library.path");
         String wd=System.getProperty("user.dir");
         System.out.println(wd);
         lp=lp+File.pathSeparator+wd+File.separator+"lib";
         System.setProperty("java.library.path",lp);
         System.out.println(System.getProperty("java.library.path"));
        
         System.out.println("class:");         
         System.out.println(System.getProperty("java.class.path"));
         lp=System.getProperty("java.class.path");
         System.out.println(wd);
         lp=lp+File.pathSeparator+wd+File.separator+"lib";
         System.setProperty("java.class.path",lp);
         System.out.println(System.getProperty("java.class.path"));
         */

        initComponents();

        gameModeGroup.add(jRadioButton1);
        gameModeGroup.add(jRadioButton2);
        gameStyleGroup.add(jRadioButton3);
        gameStyleGroup.add(jRadioButton4);
        jDialog1.setSize(250, 250);
        jDialog2.setSize(400, 300);
        jDialog4.setSize(400, 300);
        jDialog5.setSize(400, 300);
        jDialog3.setSize(900, 400);
        jDialog6.setSize(400, 300);
        jFileChooser1.setCurrentDirectory(new File("saves"));

        hraci = new DefaultListModel();
        jList1.setModel(hraci);
        File f = new File("Data" + File.separator + "desks");
        String[] s = f.list();
        for (int x = 0; x < s.length; x++) {
            cb2.addElement(s[x]);
        }
        jComboBox2.setModel(cb3);

        f = new File("Data" + File.separator + "alfabets");
        s = f.list();
        for (int x = 0; x < s.length; x++) {
            cb3.addElement(s[x]);
        }
        jComboBox3.setModel(cb2);

        f = new File("Data" + File.separator + "cursors");
        s = f.list();
        for (int x = 0; x < s.length; x++) {
            cb4.addElement(s[x]);
        }
        jComboBox4.setModel(cb4);

        f = new File("Data" + File.separator + "servers");
        s = f.list();
        for (int x = 0; x < s.length; x++) {
            servers.addElement(Cammons.deleteSuffix(s[x]));
        }
        jList2.setModel(servers);
        try {

            java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            jLabel23.setText("Screen resolution: " + screenSize.getWidth() + " x " + screenSize.getHeight());
            DisplayMode[] dms = Display.getAvailableDisplayModes();
            cb8.addElement("autodetect");
            for (int x = 0; x < dms.length; x++) {
                cb8.addElement(dms[x]);
            }
            jList8.setModel(cb8);
            jList8.setSelectedIndex(0);
        } catch (LWJGLException ex) {
            ex.printStackTrace();
        }

        f = new File("Data" + File.separator + "dictionaries");
        File[] ff = f.listFiles();
        for (int x = 0; x < ff.length; x++) {
            if (!ff[x].isDirectory()) {
                cb5.addElement(ff[x].getName() + " (cca " + String.valueOf(ff[x].length() / 4L) + " words)");
            }
        }
        jList3.setModel(cb5);

        jList2.setSelectionInterval(0, servers.getSize() - 1);

        jPanel2.add(preview);
        preview.setOwener(jPanel2);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jDialog1 = new javax.swing.JDialog();
        jLabel6 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();
        jDialog2 = new javax.swing.JDialog();
        jLabel16 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jButton13 = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        jList4 = new javax.swing.JList();
        jButton14 = new javax.swing.JButton();
        gameModeGroup = new javax.swing.ButtonGroup();
        gameStyleGroup = new javax.swing.ButtonGroup();
        jDialog3 = new javax.swing.JDialog();
        jFileChooser1 = new javax.swing.JFileChooser();
        jPanel2 = new javax.swing.JPanel();
        jButton9 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jDialog4 = new javax.swing.JDialog();
        jScrollPane7 = new javax.swing.JScrollPane();
        jList5 = new javax.swing.JList();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jDialog5 = new javax.swing.JDialog();
        jButton17 = new javax.swing.JButton();
        jScrollPane8 = new javax.swing.JScrollPane();
        jList6 = new javax.swing.JList();
        jLabel21 = new javax.swing.JLabel();
        jDialog6 = new javax.swing.JDialog();
        jScrollPane9 = new javax.swing.JScrollPane();
        jList7 = new javax.swing.JList();
        jLabel22 = new javax.swing.JLabel();
        jDialog7 = new javax.swing.JDialog();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jCheckBox5 = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner();
        jCheckBox6 = new javax.swing.JCheckBox();
        jCheckBox7 = new javax.swing.JCheckBox();
        jLabel7 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox();
        jCheckBox8 = new javax.swing.JCheckBox();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList();
        jScrollPane5 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jCheckBox9 = new javax.swing.JCheckBox();
        jLabel12 = new javax.swing.JLabel();
        jSpinner4 = new javax.swing.JSpinner();
        jCheckBox10 = new javax.swing.JCheckBox();
        jCheckBox11 = new javax.swing.JCheckBox();
        jCheckBox12 = new javax.swing.JCheckBox();
        jLabel13 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jButton12 = new javax.swing.JButton();
        jTextField6 = new javax.swing.JTextField();
        jCheckBox13 = new javax.swing.JCheckBox();
        jCheckBox14 = new javax.swing.JCheckBox();
        jCheckBox15 = new javax.swing.JCheckBox();
        jLabel11 = new javax.swing.JLabel();
        jSpinner3 = new javax.swing.JSpinner();
        jCheckBox16 = new javax.swing.JCheckBox();
        jLabel18 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel19 = new javax.swing.JLabel();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jButton18 = new javax.swing.JButton();
        jCheckBox17 = new javax.swing.JCheckBox();
        jButton19 = new javax.swing.JButton();
        jCheckBox18 = new javax.swing.JCheckBox();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList8 = new javax.swing.JList();
        jLabel23 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();
        jButton7 = new javax.swing.JButton();

        jDialog1.setModal(true);
        jLabel6.setText("jLabel6");
        jLabel6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel6.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        jButton10.setText("OK");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jDialog1Layout = new org.jdesktop.layout.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jDialog1Layout.createSequentialGroup()
                .add(jDialog1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jDialog1Layout.createSequentialGroup()
                        .add(106, 106, 106)
                        .add(jButton10))
                    .add(jDialog1Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jLabel6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jDialog1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 16, Short.MAX_VALUE)
                .add(jButton10)
                .addContainerGap())
        );
        jDialog2.setTitle("Server's testing");
        jLabel16.setText("Word surely in dictionary");

        jTextField4.setText("slov");

        jLabel17.setText("Word surely NOT in dictionary");

        jTextField5.setText("mxpx");

        jButton13.setText("Close");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jScrollPane6.setViewportView(jList4);

        jButton14.setText("Start");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jDialog2Layout = new org.jdesktop.layout.GroupLayout(jDialog2.getContentPane());
        jDialog2.getContentPane().setLayout(jDialog2Layout);
        jDialog2Layout.setHorizontalGroup(
            jDialog2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jDialog2Layout.createSequentialGroup()
                .add(jDialog2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jDialog2Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jDialog2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jTextField4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                            .add(jLabel16)
                            .add(jLabel17)))
                    .add(jDialog2Layout.createSequentialGroup()
                        .add(34, 34, 34)
                        .add(jButton13)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButton14))
                    .add(jDialog2Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jTextField5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE))
                    .add(jDialog2Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jScrollPane6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jDialog2Layout.setVerticalGroup(
            jDialog2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jDialog2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel16)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTextField4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel17)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTextField5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jDialog2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton13)
                    .add(jButton14))
                .addContainerGap())
        );
        jDialog3.setModal(true);
        jFileChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jFileChooser1PropertyChange(evt);
            }
        });

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel2.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jPanel2ComponentResized(evt);
            }
        });

        jButton9.setText("Load!");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton11.setText("Cancel:(");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jDialog3Layout = new org.jdesktop.layout.GroupLayout(jDialog3.getContentPane());
        jDialog3.getContentPane().setLayout(jDialog3Layout);
        jDialog3Layout.setHorizontalGroup(
            jDialog3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jDialog3Layout.createSequentialGroup()
                .add(jDialog3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jDialog3Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jFileChooser1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 586, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE))
                    .add(jDialog3Layout.createSequentialGroup()
                        .add(80, 80, 80)
                        .add(jButton9)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButton11)))
                .addContainerGap())
        );
        jDialog3Layout.setVerticalGroup(
            jDialog3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jDialog3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jDialog3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jFileChooser1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jDialog3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton9)
                    .add(jButton11))
                .addContainerGap())
        );
        jDialog4.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        jDialog4.setModal(true);
        jList5.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane7.setViewportView(jList5);

        jButton15.setText("Finished");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton16.setText("terminate");
        jButton16.setToolTipText("Use only wnhen conection was not created. and on your own risk;)");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jLabel20.setText("Waiting wor players...");

        org.jdesktop.layout.GroupLayout jDialog4Layout = new org.jdesktop.layout.GroupLayout(jDialog4.getContentPane());
        jDialog4.getContentPane().setLayout(jDialog4Layout);
        jDialog4Layout.setHorizontalGroup(
            jDialog4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jDialog4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jDialog4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
                    .add(jDialog4Layout.createSequentialGroup()
                        .add(jButton15)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButton16))
                    .add(jLabel20))
                .addContainerGap())
        );
        jDialog4Layout.setVerticalGroup(
            jDialog4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jDialog4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel20)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jDialog4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton15)
                    .add(jButton16))
                .addContainerGap())
        );
        jDialog5.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        jDialog5.setModal(true);
        jButton17.setText("Terminate");
        jButton17.setToolTipText("Use onlly onwhen 'no connection ...' \\n and on your own risk");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jScrollPane8.setViewportView(jList6);

        org.jdesktop.layout.GroupLayout jDialog5Layout = new org.jdesktop.layout.GroupLayout(jDialog5.getContentPane());
        jDialog5.getContentPane().setLayout(jDialog5Layout);
        jDialog5Layout.setHorizontalGroup(
            jDialog5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jDialog5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jDialog5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jDialog5Layout.createSequentialGroup()
                        .add(10, 10, 10)
                        .add(jButton17)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel21, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE))
                    .add(jScrollPane8, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE))
                .addContainerGap())
        );
        jDialog5Layout.setVerticalGroup(
            jDialog5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jDialog5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane8, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jDialog5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 26, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jList7.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane9.setViewportView(jList7);

        jLabel22.setText("jLabel22");

        org.jdesktop.layout.GroupLayout jDialog6Layout = new org.jdesktop.layout.GroupLayout(jDialog6.getContentPane());
        jDialog6.getContentPane().setLayout(jDialog6Layout);
        jDialog6Layout.setHorizontalGroup(
            jDialog6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jDialog6Layout.createSequentialGroup()
                .addContainerGap()
                .add(jDialog6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel22, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane9, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE))
                .addContainerGap())
        );
        jDialog6Layout.setVerticalGroup(
            jDialog6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jDialog6Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane9, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel22)
                .addContainerGap())
        );
        org.jdesktop.layout.GroupLayout jDialog7Layout = new org.jdesktop.layout.GroupLayout(jDialog7.getContentPane());
        jDialog7.getContentPane().setLayout(jDialog7Layout);
        jDialog7Layout.setHorizontalGroup(
            jDialog7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 884, Short.MAX_VALUE)
        );
        jDialog7Layout.setVerticalGroup(
            jDialog7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 695, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Scabble launcher");
        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });

        jScrollPane1.setViewportView(jList1);

        jLabel1.setText("Name");

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField1KeyTyped(evt);
            }
        });

        jLabel2.setText("Behaviour");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Human", "Idiot", "Student", "Master", "CommitedSucide", "SuperCommited", "Random" }));
        jComboBox1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });

        jButton1.setText("Add player");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("save");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("load");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Remove player");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Save");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Laod");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Muttiple words");
        jCheckBox1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox1.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jCheckBox2.setText("Check words");
        jCheckBox2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        jCheckBox3.setText("Check words on the web");
        jCheckBox3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox3.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });

        jCheckBox4.setText("Allow players check");
        jCheckBox4.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox4.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox4ActionPerformed(evt);
            }
        });

        jLabel3.setText("Name for NOT server networkplayer\\IP addres of server");

        jCheckBox5.setText("This computer is SERVER");
        jCheckBox5.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox5.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox5ActionPerformed(evt);
            }
        });

        jLabel4.setText("Console time");

        jSpinner1.setValue(Integer.valueOf(5000));

        jLabel5.setText("Console min time");

        jSpinner2.setValue(Integer.valueOf(3000));

        jCheckBox6.setSelected(true);
        jCheckBox6.setText("Randomize players");
        jCheckBox6.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox6.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jCheckBox7.setSelected(true);
        jCheckBox7.setText("Enable input console");
        jCheckBox7.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox7.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel7.setText("Letters set");

        jLabel8.setText("Desk");

        jLabel9.setText("Cursor:");

        jCheckBox8.setText("If word is not in dictonary and is agreed add to it");
        jCheckBox8.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox8.setEnabled(false);
        jCheckBox8.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel10.setText("Dictionary(s)");

        jList3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jList3KeyPressed(evt);
            }
        });
        jList3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jList3MousePressed(evt);
            }
        });

        jScrollPane4.setViewportView(jList3);

        jScrollPane5.setViewportView(jList2);

        jCheckBox9.setSelected(true);
        jCheckBox9.setText("Allow joker to be changed");
        jCheckBox9.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox9.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel12.setText("Time for  turn (0=inf) in minutes");

        jCheckBox10.setSelected(true);
        jCheckBox10.setText("Allow turn test (f11)");
        jCheckBox10.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox10.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jCheckBox11.setText("If turn is disagreed, skip player");
        jCheckBox11.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox11.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jCheckBox12.setText("After changing the joker lost turn");
        jCheckBox12.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox12.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel13.setText("Word table for single human  //   ");

        jLabel14.setText("ms");

        jLabel15.setText("ms");

        jButton12.setText("Test selected");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jCheckBox13.setText("If word is found on web add to users.txt");
        jCheckBox13.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox13.setEnabled(false);
        jCheckBox13.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jCheckBox14.setText("AI ignore desk(faster but do not thing about doubles/triples)");
        jCheckBox14.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox14.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jCheckBox15.setText("Only one player passes enough");
        jCheckBox15.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox15.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel11.setText("Passes to finish");

        jSpinner3.setValue(Integer.valueOf(3));

        jCheckBox16.setSelected(true);
        jCheckBox16.setText("AI ignore jokers");
        jCheckBox16.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox16.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel18.setText("Game mode         ");

        jRadioButton1.setSelected(true);
        jRadioButton1.setText("HotSeat");
        jRadioButton1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButton1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        jRadioButton2.setText("Internet");
        jRadioButton2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButton2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        jLabel19.setText("Game Style");

        jRadioButton3.setSelected(true);
        jRadioButton3.setText("OpenGL (3D)");
        jRadioButton3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButton3.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jRadioButton4.setText("Swing(window)");
        jRadioButton4.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButton4.setEnabled(false);
        jRadioButton4.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jButton18.setText("Longest word");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jCheckBox17.setText("sounds");
        jCheckBox17.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox17.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jButton19.setText("Check");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jCheckBox18.setText("Distributed AI");
        jCheckBox18.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox18.setEnabled(false);
        jCheckBox18.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox18ActionPerformed(evt);
            }
        });

        jList8.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(jList8);

        jLabel23.setText("Screen resolution");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jScrollPane5, 0, 0, Short.MAX_VALUE)
                            .add(jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                            .add(jComboBox3, 0, 308, Short.MAX_VALUE)
                            .add(jCheckBox2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                            .add(jLabel7)
                            .add(jLabel8)
                            .add(jCheckBox1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                            .add(jLabel10, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(jLabel13)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jButton18))
                            .add(jTextField3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                                .add(jCheckBox3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jButton12))
                            .add(jCheckBox13)
                            .add(jCheckBox16, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, jRadioButton1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel18, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, jRadioButton2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jRadioButton4)
                                    .add(jLabel19)
                                    .add(jRadioButton3)))
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(jButton19)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jComboBox2, 0, 239, Short.MAX_VALUE)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jButton5)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButton6)
                        .add(192, 192, 192)))
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel23)
                    .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane3)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel11)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jCheckBox5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jCheckBox4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jCheckBox6)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jCheckBox7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel9)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jCheckBox8)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jCheckBox9, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1Layout.createSequentialGroup()
                            .add(jSpinner1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 203, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(jLabel14))
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1Layout.createSequentialGroup()
                            .add(jSpinner2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 203, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(jLabel15))
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jComboBox4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 204, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1Layout.createSequentialGroup()
                            .add(jTextField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 102, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(jTextField6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 105, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jCheckBox12)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jCheckBox11)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jCheckBox10)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 170, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jSpinner4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 199, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jCheckBox14, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jCheckBox15, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jCheckBox17)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jCheckBox18)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jSpinner3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 210, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(193, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jCheckBox4)
                    .add(jCheckBox1))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jCheckBox2)
                    .add(jLabel3))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton12)
                    .add(jCheckBox3)
                    .add(jTextField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextField6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jCheckBox5)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel4)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jSpinner1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel14))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel5)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jSpinner2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel15))
                        .add(18, 18, 18)
                        .add(jCheckBox6)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jCheckBox7))
                    .add(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jScrollPane5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel7)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jButton19)
                            .add(jComboBox2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel8)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jLabel9)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jComboBox4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jCheckBox8)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jCheckBox9)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 16, Short.MAX_VALUE)
                        .add(jLabel12)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jSpinner4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(6, 6, 6)
                        .add(jCheckBox10)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jCheckBox11)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jCheckBox12))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jComboBox3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel10)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jScrollPane4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel13)
                            .add(jButton18))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jTextField3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jCheckBox13)
                    .add(jCheckBox14))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jCheckBox15)
                    .add(jCheckBox16))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel11)
                    .add(jLabel18)
                    .add(jLabel19))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jSpinner3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jRadioButton1)
                        .add(jRadioButton3)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jRadioButton2)
                            .add(jRadioButton4))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jButton5)
                            .add(jButton6)))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jCheckBox17)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jCheckBox18)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel23)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        jScrollPane2.setViewportView(jPanel1);

        jButton8.setText("Run  game");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton7.setText("LoadGame");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel2)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane1, 0, 0, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                                .add(jButton2)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(jButton3))
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jButton1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jComboBox1, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jTextField1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jButton4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                                .add(10, 10, 10)
                                .add(jButton8)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jButton7))
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jProgressBar1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 712, Short.MAX_VALUE)
                            .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 712, Short.MAX_VALUE)))
                    .add(jLabel1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(layout.createSequentialGroup()
                        .add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButton1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jButton2)
                            .add(jButton3))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 618, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jProgressBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton4)
                    .add(jButton8)
                    .add(jButton7))
                .addContainerGap())
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
// TODO add your handling code here:
        setEnables();
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
// TODO add your handling code here:
        setEnables();
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jCheckBox18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox18ActionPerformed
// TODO add your handling code here:

    }//GEN-LAST:event_jCheckBox18ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
// TODO add your handling code here:
        setUpSettings(settings);
        settings.saveSettings(new File("scrabble-settings.xml"));
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
// TODO add your handling code here:
        setEnables();
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jCheckBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox4ActionPerformed
// TODO add your handling code here:
        setEnables();
    }//GEN-LAST:event_jCheckBox4ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
// TODO add your handling code here:
        lschm = new DefaultListModel();
        jList7.setModel(lschm);
        int a = checkLetterSet(lschm, (String) jComboBox2.getSelectedItem());
        if (a == 0) {
            jLabel22.setText("all textures found ;)");
        } else {
            jLabel22.setText(a + " texturess missing! (check missing files on your disk,\n try to download them from homepage,\n try tu run scrabble strightly from _start.bat");
        }
        jDialog6.setVisible(true);
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
// TODO add your handling code here:
        jDialog2.setVisible(true);
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
// TODO add your handling code here:
        settings = Settings.Load(new File("scrabble-settings.xml"));
        applySettings(settings);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
// TODO add your handling code here:
        String[] sq = new String[jList3.getSelectedValues().length];
        for (int x = 0; x < sq.length; x++) {
            sq[x] = "Data" + File.separator + "dictionaries" + File.separator + settings.parseDictName(jList3.getSelectedValues()[x].toString());
        }
        int m = MyDictionary.compWordMaxLength(sq);
        jButton18.setText("longets word - " + m);
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
// TODO add your handling code here:
        setEnables();
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void jList3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jList3KeyPressed
// TODO add your handling code here:
        int totalsize = 0;
        Object[] l = jList3.getSelectedValues();
        for (int x = 0; x < l.length; x++) {
            totalsize += Integer.parseInt(settings.parseDictWords(l[x].toString()));
        }

        jLabel10.setText("Dictionaries (total " + String.valueOf(totalsize) + "words selected)");
    }//GEN-LAST:event_jList3KeyPressed

    private void jList3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList3MousePressed
// TODO add your handling code here:
        int totalsize = 0;
        Object[] l = jList3.getSelectedValues();
        for (int x = 0; x < l.length; x++) {
            totalsize += Integer.parseInt(settings.parseDictWords(l[x].toString()));
        }

        jLabel10.setText("Dictionaries (total " + String.valueOf(totalsize) + "words selected)");
    }//GEN-LAST:event_jList3MousePressed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
// TODO add your handling code here:
        jDialog5.setVisible(false);
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
// TODO add your handling code here:
        //jDialog4.setVisible(false);
        System.exit(2);
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
// TODO add your handling code here:
        spc.setFinished(true);
        joinedPlayers = spc.getJoinedPlayers();
        for (Iterator it = joinedPlayers.iterator(); it.hasNext();) {
            JoinedPlayer elem = (JoinedPlayer) it.next();
            try {
                elem.getOut().write("ScrabbleEROVHstartingeventER4G-startok\r\n");
                elem.getOut().flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
        jDialog4.setVisible(false);
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jPanel2ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel2ComponentResized
// TODO add your handling code here:
        preview.setzoom();
        jPanel2.repaint();
        // preview.paint(jPanel2.getGraphics());
    }//GEN-LAST:event_jPanel2ComponentResized

    private void jFileChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jFileChooser1PropertyChange
// TODO add your handling code here:
        if (jFileChooser1.getSelectedFile() == null) {
            return;
        }

        BufferedImage buf = SaveLoad.LoadGamePicture(jFileChooser1.getSelectedFile().toString());
        preview.setIamge(buf);
        jPanel2.repaint();
      //  preview.paint(jPanel2.getGraphics());

    }//GEN-LAST:event_jFileChooser1PropertyChange

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
// TODO add your handling code here:
        jDialog3.setVisible(true);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
// TODO add your handling code here:
        Settings s = SaveLoad.LoadGameSettings(jFileChooser1.getSelectedFile().toString());
        jDialog3.setVisible(false);
        settings = s;
        applySettings(s);
        settings.loadGame = jFileChooser1.getSelectedFile().toString();
        startGameBody();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
// TODO add your handling code here:
        jDialog3.setVisible(false);
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
// TODO add your handling code here:
        File f;
        f = new File("scrabble-players.txt");
        BufferedReader read = null;
        hraci.clear();
        try {
            read = new BufferedReader(new FileReader(f));
            String a;

            a = read.readLine();
            while (a != null) {
                if (!a.trim().equalsIgnoreCase("")) {
                    hraci.addElement(a.trim());
                }
                a = read.readLine();
            }

            read.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                read.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        File f;
        f = new File("scrabble-players.txt");

        BufferedWriter write = null;
        try {

            write = new BufferedWriter(new FileWriter(f));

            for (int x = 0; x < hraci.size(); x++) {
                write.write(hraci.get(x) + "\r\n");
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {

                write.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }


    }//GEN-LAST:event_jButton2ActionPerformed

    private void jCheckBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox5ActionPerformed
// TODO add your handling code here:
        setEnables();
    }//GEN-LAST:event_jCheckBox5ActionPerformed


    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        jDialog2.setVisible(false);// TODO add your handling code here:
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
// TODO add your handling code here:

        TestVlakno testVlakno = new TestVlakno();

        testVlakno.start();
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
// TODO add your handling code here:
        jDialog1.setVisible(false);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
// TODO add your handling code here:
        if (evt.getKeyCode() == evt.VK_ENTER) {
            if (jList1.getSelectedIndex() >= 0) {
                hraci.set(jList1.getSelectedIndex(), jTextField1.getText() + "[" + jComboBox1.getSelectedItem() + "]");
            } else {
                hraci.addElement(jTextField1.getText() + "[" + jComboBox1.getSelectedItem() + "]");
            }

        }
    }//GEN-LAST:event_jTextField1KeyPressed

    private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped
// TODO add your handling code here:
    }//GEN-LAST:event_jTextField1KeyTyped

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
// TODO add your handling code here:
        if (jList1.getSelectedIndex() >= 0) {
            jTextField1.setText(settings.parsePlayerName((String) hraci.get(jList1.getSelectedIndex())));
            String bh = settings.parsePlayerBehaviour((String) hraci.get(jList1.getSelectedIndex()));
            int index = -1;
            for (int x = 0; x < jComboBox1.getModel().getSize(); x++) {
                if (jComboBox1.getModel().getElementAt(x).toString().equals(bh)) {
                    index = x;
                }
            };
            if (index >= 0) {
                jComboBox1.setSelectedIndex(index);
            }
        }
    }//GEN-LAST:event_jList1MouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
// TODO add your handling code here:
        if (jList1.getSelectedIndex() >= 0) {
            hraci.remove(jList1.getSelectedIndex());
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
// TODO add your handling code here:
        hraci.addElement(jTextField1.getText() + "[" + jComboBox1.getSelectedItem() + "]");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
// TODO add your handling code here:

        if (jRadioButton2.isSelected() && jCheckBox5.isSelected()) {
            if (!nessesarryTests()) {
                return;
            }
        }
        if (jRadioButton2.isSelected()) {
            if (jCheckBox5.isSelected()) {
                DefaultListModel jpl = new DefaultListModel();
                jList5.setModel(jpl);
                try {
                    spc = new ServerPlayerCreator(jpl, settings);
                } catch (Exception ex) {
                    jpl.addElement("error connection was not created,restart");
                    ex.printStackTrace();
                    jDialog4.setVisible(true);
                }

                setUpSettings(settings);
                spc.start();
                jDialog4.setVisible(true);
                settings.joinedPalyers = joinedPlayers;

            } else {
                DefaultListModel cpl = new DefaultListModel();
                jList6.setModel(cpl);
                cpc = null;
                try {
                    cpc = new ClientPlayerCreator(InetAddress.getByName(/*"localhost"/*/jTextField6.getText()), cpl, jTextField2.getText(), jDialog5, this);
                } catch (Exception ex) {
                    cpl.addElement("error connection was not created");
                    ex.printStackTrace();

                }
                if (cpc != null) {
                    cpc.start();
                }
                Settings oldSettings = new Settings();
                setUpSettings(oldSettings);
                jDialog5.setVisible(true);

                settings = cpc.getSettings();
                settings.joinedPalyers = new ArrayList();
                settings.joinedPalyers.add(cpc.joinedPalyer);
                settings.bools[Settings.imserver] = false;
                settings.strings[Settings.password] = oldSettings.strings[Settings.password];
                settings.bools[Settings.swinggame] = oldSettings.bools[Settings.swinggame];
                settings.strings[Settings.cursor] = oldSettings.strings[Settings.cursor];
                settings.strings[Settings.dysplaymode] = oldSettings.strings[Settings.dysplaymode];
                settings.strings[Settings.ipaddress] = oldSettings.strings[Settings.ipaddress];
                settings.bools[Settings.sounds] = oldSettings.bools[Settings.sounds];
                applySettings(settings);

            }
        }

        if (!jRadioButton2.isSelected()) {
            if (!playerTest()) {
                return;
            }
            if (!nessesarryTests()) {
                return;
            }
        } else if (jCheckBox5.isSelected()) {
            if (!playerTest()) {
                return;
            }
        }

        startGameBody();


    }//GEN-LAST:event_jButton8ActionPerformed

    private void startGameBody() {

        thread = new Thread(new Runnable() {
            public void run() {
                Main demo = new Main();
                demo.setThread(thread);
                demo.setSettings(jScrabelFrame.settings);

                String title = "Scrabble by Vanek Jiri!";
                String clientserver = "";
                if (settings.isInet()) {
                    if (settings.IsServer()) {
                        clientserver = "server";
                    } else {
                        clientserver = "client";
                    }
                } else {
                    clientserver = "hotSeat";
                }

                demo.windowTitle += " - " + title + " - " + clientserver;

                demo.run();  // will call init(), render(), mouse functions
                jScrabelFrame.jProgressBar1.setValue(0);
                jScrabelFrame.setAlwaysOnTop(false);
                //jScrabelFrame.setVisible(true);
                System.exit(0);
            }
        });;
        setUpSettings(settings);

        thread.start();
        this.setAlwaysOnTop(true);
        this.setVisible(true);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                jScrabelFrame = new StartUpScrabble();
                jScrabelFrame.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup gameModeGroup;
    private javax.swing.ButtonGroup gameStyleGroup;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox10;
    private javax.swing.JCheckBox jCheckBox11;
    private javax.swing.JCheckBox jCheckBox12;
    private javax.swing.JCheckBox jCheckBox13;
    private javax.swing.JCheckBox jCheckBox14;
    private javax.swing.JCheckBox jCheckBox15;
    private javax.swing.JCheckBox jCheckBox16;
    private javax.swing.JCheckBox jCheckBox17;
    private javax.swing.JCheckBox jCheckBox18;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JCheckBox jCheckBox8;
    private javax.swing.JCheckBox jCheckBox9;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog2;
    private javax.swing.JDialog jDialog3;
    private javax.swing.JDialog jDialog4;
    private javax.swing.JDialog jDialog5;
    private javax.swing.JDialog jDialog6;
    private javax.swing.JDialog jDialog7;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    public javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    public javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JList jList3;
    private javax.swing.JList jList4;
    private javax.swing.JList jList5;
    private javax.swing.JList jList6;
    private javax.swing.JList jList7;
    private javax.swing.JList jList8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    public javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JSpinner jSpinner3;
    private javax.swing.JSpinner jSpinner4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    // End of variables declaration//GEN-END:variables

    private boolean aiPlayer() {
        for (int y = 0; y < jList1.getModel().getSize(); y++) {
            String s = ((String) jList1.getModel().getElementAt(y));
            String bh = settings.parsePlayerBehaviour(s);
            int index = -1;
            for (int x = 0; x < jComboBox1.getModel().getSize(); x++) {
                if (jComboBox1.getModel().getElementAt(x).toString().equals(bh)) {
                    index = x;
                }
            };
            if (index < 0) {
                index = 0;
            }

            if (index > 0) {
                return true;
            }

        }
        return false;
    }

    public void setUpSettings(Settings settings) {
        settings.playersNames.clear();
        settings.playersValues.clear();
        for (int y = 0; y < jList1.getModel().getSize(); y++) {
            String s = ((String) jList1.getModel().getElementAt(y));
            String n = (settings.parsePlayerName(s));
            String bh = settings.parsePlayerBehaviour(s);
            int index = -1;
            for (int x = 0; x < jComboBox1.getModel().getSize(); x++) {
                if (jComboBox1.getModel().getElementAt(x).toString().equals(bh)) {
                    index = x;
                }
            };
            if (index < 0) {
                index = 0;
            }
            settings.playersNames.add(n);
            settings.playersValues.add(new Integer(index));

        }
        settings.bools[settings.distributedAi] = jCheckBox18.isSelected();
        settings.bools[settings.multipleword] = jCheckBox1.isSelected();
        settings.bools[settings.randomizeplayers] = jCheckBox6.isSelected();
        settings.bools[settings.inputkonzole] = jCheckBox7.isSelected();
        settings.bools[settings.playercheck] = jCheckBox4.isSelected();
        settings.bools[settings.wordcheck] = jCheckBox2.isSelected();
        settings.bools[settings.inetcheck] = jCheckBox3.isSelected();
        settings.bools[settings.addtodic] = jCheckBox8.isSelected();
        settings.bools[settings.jokerchanging] = jCheckBox9.isSelected();
        settings.bools[settings.turncheck] = jCheckBox10.isSelected();
        settings.bools[settings.skipplayer] = jCheckBox11.isSelected();
        settings.bools[settings.lostturnafterjoker] = jCheckBox12.isSelected();
        settings.bools[settings.addServerAgreedWordToUsers] = jCheckBox13.isSelected();
        settings.bools[settings.aiIgnoreDesk] = jCheckBox14.isSelected();
        settings.bools[settings.onlyOnePasedddEnough] = jCheckBox15.isSelected();
        settings.bools[settings.imserver] = jCheckBox5.isSelected();
        settings.bools[settings.aiIgnoreJokers] = jCheckBox16.isSelected();
        settings.bools[settings.inetgame] = jRadioButton2.isSelected();
        settings.bools[settings.swinggame] = jRadioButton4.isSelected();
        settings.strings[settings.desk] = (String) jComboBox3.getSelectedItem();
        settings.strings[settings.letterset] = (String) jComboBox2.getSelectedItem();
        settings.strings[settings.cursor] = (String) jComboBox4.getSelectedItem();
        settings.strings[settings.dysplaymode] = jList8.getSelectedValue().toString();
        settings.strings[settings.password] = (String) jTextField2.getText();
        settings.strings[settings.words] = (String) jTextField3.getText();
        settings.strings[settings.ipaddress] = (String) jTextField6.getText();
        settings.bools[settings.sounds] = jCheckBox17.isSelected();

        String[] sq = new String[jList3.getSelectedValues().length];
        for (int x = 0; x < sq.length; x++) {
            sq[x] = "Data" + File.separator + "dictionaries" + File.separator + settings.parseDictName(jList3.getSelectedValues()[x].toString());
        }
        int m = MyDictionary.compWordMaxLength(sq);
        //jSpinner3.getModel().setValue(new Integer(m));

        settings.ints[settings.maxcconsoletime] = Integer.parseInt(jSpinner1.getModel().getValue().toString());
        settings.ints[settings.minconosletime] = Integer.parseInt(jSpinner2.getModel().getValue().toString());
        settings.ints[settings.maxcheckedlength] = m;//Integer.parseInt(jSpinner3.getModel().getValue().toString());
        settings.ints[settings.turntime] = Integer.parseInt(jSpinner4.getModel().getValue().toString());
        settings.ints[settings.passestowin] = Integer.parseInt(jSpinner3.getModel().getValue().toString());

        String[] s1 = new String[jList3.getSelectedValues().length];
        for (int x = 0; x < s1.length; x++) {
            s1[x] = "Data" + File.separator + "dictionaries" + File.separator + settings.parseDictName(jList3.getSelectedValues()[x].toString());
        }
        settings.dictionaries = (s1);
        String[] s2 = new String[jList2.getSelectedValues().length];
        for (int x = 0; x < s2.length; x++) {
            s2[x] = "Data" + File.separator + "servers" + File.separator + jList2.getSelectedValues()[x].toString() + ".xml";
        }
        settings.servers = (s2);
    }

    public void applySettings(Settings settings) {
        /*settings.playersNames.clear();
         settings.playersValues.clear();
         for(int y=0;y<jList1.getModel().getSize();y++){
         String s=((String) jList1.getModel().getElementAt(y));
         String n=(settings.parsePlayerName(s));
         String bh=settings.parsePlayerBehaviour(s);
         int index=-1;
         for(int x=0;x<jComboBox1.getModel().getSize();x++){if(jComboBox1.getModel().getElementAt(x).toString().equals(bh)) index=x;};
         if (index<0)index=0;
         settings.playersNames.add(n);
         settings.playersValues.add(new Integer(index));
                
         }
         */
        jCheckBox18.setSelected(settings.bools[settings.distributedAi]);
        jCheckBox1.setSelected(settings.bools[settings.multipleword]);
        jCheckBox6.setSelected(settings.bools[settings.randomizeplayers]);
        jCheckBox7.setSelected(settings.bools[settings.inputkonzole]);
        jCheckBox4.setSelected(settings.bools[settings.playercheck]);
        jCheckBox2.setSelected(settings.bools[settings.wordcheck]);
        jCheckBox3.setSelected(settings.bools[settings.inetcheck]);
        jCheckBox8.setSelected(settings.bools[settings.addtodic]);
        jCheckBox9.setSelected(settings.bools[settings.jokerchanging]);
        jCheckBox10.setSelected(settings.bools[settings.turncheck]);
        jCheckBox11.setSelected(settings.bools[settings.skipplayer]);
        jCheckBox12.setSelected(settings.bools[settings.lostturnafterjoker]);
        jCheckBox13.setSelected(settings.bools[settings.addServerAgreedWordToUsers]);
        jCheckBox14.setSelected(settings.bools[settings.aiIgnoreDesk]);
        jCheckBox15.setSelected(settings.bools[settings.onlyOnePasedddEnough]);
        jCheckBox16.setSelected(settings.bools[settings.aiIgnoreJokers]);
        jCheckBox17.setSelected(settings.bools[settings.sounds]);
        jCheckBox5.setSelected(settings.bools[settings.imserver]);
        gameModeGroup.setSelected(jRadioButton2.getModel(), settings.bools[settings.inetgame]);
        gameModeGroup.setSelected(jRadioButton1.getModel(), !settings.bools[settings.inetgame]);
        gameStyleGroup.setSelected(jRadioButton4.getModel(), settings.bools[settings.swinggame]);
        gameStyleGroup.setSelected(jRadioButton3.getModel(), !settings.bools[settings.swinggame]);

        jComboBox3.setSelectedIndex(getSellectedIndex(jComboBox3.getModel(), settings.strings[settings.desk]));
        jComboBox2.setSelectedIndex(getSellectedIndex(jComboBox2.getModel(), settings.strings[settings.letterset]));
        jComboBox4.setSelectedIndex(getSellectedIndex(jComboBox4.getModel(), settings.strings[settings.cursor]));
        jList8.setSelectedIndex(getSellectedIndex(jList8.getModel(), settings.strings[settings.dysplaymode]));
        jTextField2.setText(settings.strings[settings.password]);
        jTextField3.setText(settings.strings[settings.words]);
        jTextField6.setText(settings.strings[settings.ipaddress]);

       // String[] sq = new String[jList3.getSelectedValues().length];
        //  for (int x=0;x<sq.length;x++)sq[x]="Data"+File.separator+"dictionaries"+File.separator+settings.parseDictName(jList3.getSelectedValues()[x].toString());
        //int m=MyDictionary.compWordMaxLength(sq);
        //jSpinner3.getModel().setValue(new Integer(m));
        jSpinner1.getModel().setValue(new Integer(settings.ints[settings.maxcconsoletime]));
        jSpinner2.getModel().setValue(new Integer(settings.ints[settings.minconosletime]));
        //settings.ints[settings.maxcheckedlength]=m;
        jSpinner4.getModel().setValue(new Integer(settings.ints[settings.turntime]));
        jSpinner3.getModel().setValue(new Integer(settings.ints[settings.passestowin]));

        for (int i = 0; i < settings.dictionaries.length; i++) {
            settings.dictionaries[i] = Cammons.deleteSuffix(Cammons.extractFileName(settings.dictionaries[i]));
        }
        int[] a = getSellectedIndexsesJustPart(jList3.getModel(), settings.dictionaries);

        jList3.setSelectedIndices(a);
        for (int i = 0; i < settings.servers.length; i++) {
            settings.servers[i] = Cammons.deleteSuffix(Cammons.extractFileName(settings.servers[i]));
        }
        int[] b = getSellectedIndexsesIgnoreCase(jList2.getModel(), settings.servers);
        jList2.setSelectedIndices(b);
        setEnables();

    }

    public int getSellectedIndex(ListModel list, String item) {
        for (int i = 0; i < list.getSize(); i++) {
            if ((list.getElementAt(i)).toString().equalsIgnoreCase(item)) {
                return i;
            }

        }
        return -1;
    }

    public int[] getSellectedIndexsesIgnoreCase(ListModel list, String[] items) {
        int velikost = 0;
        for (int i = 0; i < list.getSize(); i++) {
            for (int j = 0; j < items.length; j++) {

                if (((String) list.getElementAt(i)).equalsIgnoreCase(items[j])) {
                    velikost++;
                }

            }
        }
        int[] vysledek = new int[velikost];
        int ukazatel = 0;
        for (int i = 0; i < list.getSize(); i++) {
            for (int j = 0; j < items.length; j++) {
                if (((String) list.getElementAt(i)).equalsIgnoreCase(items[j])) {
                    vysledek[ukazatel] = i;
                    ukazatel++;

                }
            }
        }

        return vysledek;
    }

    public int[] getSellectedIndexsesJustPart(ListModel list, String[] items) {
        int velikost = 0;
        for (int i = 0; i < list.getSize(); i++) {
            for (int j = 0; j < items.length; j++) {

                if (((String) list.getElementAt(i)).indexOf(items[j]) >= 0) {
                    velikost++;
                }

            }
        }
        int[] vysledek = new int[velikost];
        int ukazatel = 0;
        for (int i = 0; i < list.getSize(); i++) {
            for (int j = 0; j < items.length; j++) {
                if (((String) list.getElementAt(i)).indexOf(items[j]) >= 0) {
                    vysledek[ukazatel] = i;
                    ukazatel++;

                }
            }
        }

        return vysledek;
    }

    private boolean playerTest() {
        if ((jList1.getModel().getSize() <= 0 && joinedPlayers == null) || (jList1.getModel().getSize() <= 0 && joinedPlayers != null && joinedPlayers.size() <= 0)) {
            jLabel6.setText("No players!!");
            jDialog1.setVisible(true);
            return false;
        }
        return true;
    }

    private boolean nessesarryTests() {

        if (jCheckBox2.isSelected() && jList3.getSelectedIndex() == -1) {
            jLabel6.setText("Word check with no dictionary...");
            jDialog1.setVisible(true);
            return false;
        }

        if (aiPlayer() && jList3.getSelectedIndex() == -1) {
            jLabel6.setText("AI with no dictionary...");
            jDialog1.setVisible(true);
            return false;
        }
        if (jCheckBox3.isSelected()) {
            if (jList2.getSelectedValues().length == 0) {
                jLabel6.setText("No servers");
                jDialog1.setVisible(true);
                return false;
            }
            TestVlakno testVlakno = new TestVlakno();
            testVlakno.start();
            jDialog2.setModal(true);
            jDialog2.setVisible(true);
        //while (testVlakno.isAlive()){
            //    jDialog2.repaint();
            //  }
            jDialog2.setModal(false);
            jDialog2.setVisible(false);
            jDialog2.setModal(false);
            if (jList2.getSelectedValues().length == 0) {
                jLabel6.setText("No servers");
                jDialog1.setVisible(true);
                return false;
            }
        }

        return true;
    }

    private void setEnables() {
        if (jRadioButton2.isSelected()) {
            jCheckBox18.setEnabled(true);
        } else {
            jCheckBox18.setSelected(false);
            jCheckBox18.setEnabled(false);
        }
        //***
        jCheckBox13.setEnabled(jCheckBox3.isSelected());
        if (!jCheckBox3.isSelected()) {
            jCheckBox13.setSelected(false);
        }
        //****
        jTextField6.setEnabled(!jCheckBox5.isSelected());
        if (!jTextField6.isEnabled()) {
            try {
                InetAddress addr;

                addr = InetAddress.getLocalHost();

                // Get IP Address
                byte[] ipAddr = addr.getAddress();

                String ipAddrStr = "";
                for (int i = 0; i < ipAddr.length; i++) {
                    if (i > 0) {
                        ipAddrStr += ".";
                    }
                    ipAddrStr += ipAddr[i] & 0xFF;
                }
                jTextField6.setText(ipAddrStr);
            } catch (UnknownHostException ex) {
                ex.printStackTrace();
            }
        } else {
            jTextField6.setText("");
        }

  //***
        if (jCheckBox4.isSelected() && jCheckBox2.isSelected()) {
            jCheckBox8.setEnabled(true);
        } else {
            jCheckBox8.setSelected(false);
            jCheckBox8.setEnabled(false);
        }

  //***
  //**
    }

    public int compareSetings(Settings s1, Settings s2, DefaultListModel dlm) {
        int chyb = 0;
        for (int i = 0; i < s1.bools.length; i++) {
            String s = s1.boolToString(i).concat(": ");
            if (s1.bools[i] == s2.bools[i]) {
                dlm.addElement("ok      - " + s + String.valueOf(s1.bools[i]));
            } else {
                dlm.addElement("WARNING - " + s + String.valueOf(s1.bools[i]) + "/" + String.valueOf(s2.bools[i]));
                chyb++;
            }
        }

        for (int i = 2; i < s1.ints.length; i++) {
            String s = s1.intToString(i).concat(": ");
            if (s1.ints[i] == s2.ints[i]) {
                dlm.addElement("ok      - " + s + String.valueOf(s1.ints[i]));
            } else {
                dlm.addElement("WARNING - " + s + String.valueOf(s1.ints[i]) + "/" + String.valueOf(s2.ints[i]));
                chyb++;
            }
        }

        for (int i = 0; i < s1.strings.length; i++) {
            if (i != Settings.dysplaymode) {//fuj fuj ale nutne
                String s = s1.strToString(i).concat(": ");
                if (s1.strings[i].equals(s2.strings[i])) {
                    dlm.addElement("ok      - " + s + s1.strings[i]);
                } else {
                    if (s1.strings[i] == null || s2.strings[i] == null) {
                        dlm.addElement("WOU  - " + s + s1.strings[i] + "/" + s2.strings[i]);
                    } else {
                        dlm.addElement("WARNING - " + s + s1.strings[i] + "/" + s2.strings[i]);
                        chyb++;
                    }
                }
            }
        }

        dlm.addElement("Servers:");
        String[] delsi, kratsi;
        if (s1.servers.length == s2.servers.length) {
            dlm.addElement("ok      - length same: " + s2.servers.length);
            delsi = s1.servers;
            kratsi = s2.servers;
        } else {
            dlm.addElement("WARNING - length diff: " + s1.servers.length + "/" + s2.servers.length);
            //sl=Math.min(s1.servers.length,s2.servers.length;
            chyb++;
            if (s1.servers.length < s2.servers.length) {
                kratsi = s1.servers;
                delsi = s2.servers;
            } else {
                kratsi = s2.servers;
                delsi = s1.servers;
            }
        }
        for (int i = 0; i < delsi.length; i++) {
            boolean ok = false;
            for (int j = 0; j < kratsi.length; j++) {
                if (delsi[i].indexOf(kratsi[j]) >= 0 || kratsi[i].indexOf(delsi[j]) >= 0) {
                    ok = true;
                    break;
                }
            }
            if (ok) {
                dlm.addElement("ok          - " + delsi[i]);
            } else {
                dlm.addElement("WARNING     - " + delsi[i]);
                chyb++;
            }
        }

        dlm.addElement("Dictionaries:");

        if (s1.dictionaries.length == s2.dictionaries.length) {
            dlm.addElement("ok      - length same: " + s2.dictionaries.length);
            delsi = s1.dictionaries;
            kratsi = s2.dictionaries;
        } else {
            dlm.addElement("WARNING - length diff: " + s1.dictionaries.length + "/" + s2.dictionaries.length);
            chyb++;
            if (s1.dictionaries.length < s2.dictionaries.length) {
                kratsi = s1.dictionaries;
                delsi = s2.dictionaries;
            } else {
                kratsi = s2.dictionaries;
                delsi = s1.dictionaries;
            }
        }
        for (int i = 0; i < delsi.length; i++) {
            boolean ok = false;
            for (int j = 0; j < kratsi.length; j++) {
                if (delsi[i].indexOf(kratsi[j]) >= 0 || kratsi[i].indexOf(delsi[j]) >= 0) {
                    ok = true;
                    break;
                }
            }
            if (ok) {
                dlm.addElement("ok          - " + delsi[i]);
            } else {
                dlm.addElement("WARNING     - " + delsi[i]);
                chyb++;
            }
        }

        return chyb;
    }

    private int checkLetterSet(DefaultListModel lschm, String string) {
        int chyb = 0;
        File src = new File("Data" + File.separator + "alfabets" + File.separator + string);
        lschm.addElement("File :" + src.toString());
        if (!src.exists()) {
            lschm.addElement("WOU - file missing?");
            return -1;
        }

        Set<String> dirs = new HashSet();

        try {
            BufferedReader read = new BufferedReader(new FileReader(src));

            // read from the file.
            String s = read.readLine();
            s = s.replace((CharSequence) "*", (CharSequence) File.separator);
            lschm.addElement("Background(walls etc...) :");
            String line = ("Data" + File.separator + "textures" + File.separator + "letters" + File.separator + s);
            if (new File(line).exists()) {
                lschm.addElement("exists  - " + line);
            } else {
                lschm.addElement("MISSING - " + line);
                chyb++;
            }

            lschm.addElement("Letters(trexture,quantity,value :");
            dirs.add(Cammons.extractFilePath(line.toLowerCase()));
            s = read.readLine();
            while (s != null) {
                s = s.replace((CharSequence) "*", (CharSequence) File.separator);
                String[] ss = s.split(" ");

                line = ("Data" + File.separator + "textures" + File.separator + "letters" + File.separator + ss[3]);
                lschm.addElement("**" + ss[0] + "**");
                if (new File(line).exists()) {
                    lschm.addElement("exists  - " + line);
                } else {
                    lschm.addElement("MISSING - " + line);
                    chyb++;
                }
                lschm.addElement("value: " + ss[1] + ",quantity: " + ss[2]);
                dirs.add(Cammons.extractFilePath(line.toLowerCase()));
                s = read.readLine();
            }
        } catch (IOException iox) {
            // I/O error: could not open file, or lost connection, or...
            iox.printStackTrace();
        }
        lschm.addElement("Dirs required: ");
        for (Iterator it = dirs.iterator(); it.hasNext();) {
            String elem = (String) it.next();
            lschm.addElement(elem);

        }
        return chyb;
    }

}
