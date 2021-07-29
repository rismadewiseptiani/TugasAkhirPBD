/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tampilan.ui;

import JavaConnection.ConnectionHelper;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author acer
 */
public class Pemasukan extends javax.swing.JDialog {
    public final Connection conn = new ConnectionHelper().connect();

    private DefaultTableModel tabmode;
    private DefaultTableModel tabmode2;
    private DefaultTableModel tabmode3;

    JasperReport JasRep;
    JasperPrint JasPri;
    Map param = new HashMap();
    JasperDesign JasDes;

    private void aktif() {
        txtIdPemasukan.setEnabled(true);
        txtNamaSupplier.setEnabled(true);
        txtIdKaos.setEnabled(true);
        txtBahan.setEnabled(true);
        txtWarna.setEnabled(true);
        txtModel.setEnabled(true);
        txtUkuran.setEnabled(true);
        txtHarga.setEnabled(true);
        txtStok.setEnabled(true);
    }

    protected void kosong() {
        txtIdPemasukan.setText(null);
        txtNamaSupplier.setText(null);
        txtIdKaos.setText(null);
        txtBahan.setText(null);
        txtWarna.setText(null);
        txtModel.setText(null);
        txtUkuran.setText(null);
        txtHarga.setText(null);
        txtStok.setText(null);
    }

    protected void kosong2() {
        txtIdKaos.setText(null);
        txtBahan.setText(null);
        txtWarna.setText(null);
        txtModel.setText(null);
        txtUkuran.setText(null);
        txtHarga.setText(null);
        txtStok.setText(null);
    }

    public void noTable() {
        int Baris = tabmode.getRowCount();
        for (int a = 0; a < Baris; a++) {
            String nomor = String.valueOf(a + 1);
            tabmode.setValueAt(nomor + ".", a, 0);
        }
    }

    public void tanggal() {
        Date tgl = new Date();
        btnTanggal.setDate(tgl);
    }

    private void IdPemasukan() {
        try {
            Connection con = new ConnectionHelper().connect();
            java.sql.Statement stat = con.createStatement();
            String sql = "select max(right (id_pemasukan,6)) as no from db_pemasukan";
            ResultSet res = stat.executeQuery(sql);
            while (res.next()) {
                if (res.first() == false) {
                    txtIdPemasukan.setText("A-00001");
                } else {
                    res.last();
                    int aut_id = res.getInt(1) + 1;
                    String no = String.valueOf(aut_id);
                    int no_jual = no.length();
                    // mengatur jumlah 0
                    for (int j = 0; j < 6 - no_jual; j++) {
                        no = "0" + no;
                    }
                    txtIdPemasukan.setText("A-" + no);
                }
            }
            res.close();
            stat.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi Kesalahan");
        }
    }

    private void IdDetailPemasukan() {
        try {
            Connection con = new ConnectionHelper().connect();
            java.sql.Statement stat = con.createStatement();
            String sql_dt = "select max(right (id_detail,4)) as no from db_detailpemasukan";
            ResultSet res_dt = stat.executeQuery(sql_dt);
            while (res_dt.next()) {
                if (res_dt.first() == false) {
                    txtIdDetailPemasukan.setText("A-0001");
                } else {
                    res_dt.last();
                    int aut_id_dt = res_dt.getInt(1) + 1;
                    String no_dt = String.valueOf(aut_id_dt);
                    int no_jual_dt = no_dt.length();
                    for (int j = 0; j < 4 - no_jual_dt; j++) {
                        no_dt = "0" + no_dt;
                    }
                    txtIdDetailPemasukan.setText("A-" + no_dt);
                }
            }
            res_dt.close();
            stat.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi Kesalahan");
        }
    }
        
    public void dataTable() {
        Object[] Baris = {"No", "Tanggal", "ID Detail" ,"ID Pemasukan", "Supplier", "ID Kaos",
            "Bahan", "Warna", "Model", "Ukuran","Harga", "Stok"};
        tabmode = new DefaultTableModel(null, Baris);
        TablePemasukan.setModel(tabmode);
        String sql = "select * from db_detailpemasukan order by tanggal asc";
        try {
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            while (hasil.next()) {
                String tanggal = hasil.getString("tanggal");
                String IdPemasukan = hasil.getString("id_pemasukan");
                String IdDetail = hasil.getString("id_detail");
                String supplier = hasil.getString("nama_supplier");
                String IdKaos = hasil.getString("id_kaos");
                String Bahan = hasil.getString("bahan");
                String Warna = hasil.getString("warna");
                String Model = hasil.getString("model");
                String Ukuran = hasil.getString("ukuran");
                String Harga = hasil.getString("harga");
                String Stok = hasil.getString("stok");
                String[] data = {"", tanggal, IdDetail, IdPemasukan, supplier, IdKaos,
                    Bahan, Warna, Model,Ukuran,Harga,Stok};
                tabmode.addRow(data);
                noTable();
            }
        } catch (Exception e) {
        }
    }

    public void dataTable2() {
        Object[] Baris = {"Id Kaos", "Bahan", "Warna", "Model", "Ukuran","Harga"};
        tabmode2 = new DefaultTableModel(null, Baris);
        TabelDataKaos.setModel(tabmode2);
        String sql = "select * from db_datakaos order by id_kaos asc";
        try {
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            while (hasil.next()) {
                String IdKaos = hasil.getString("id_kaos");
                String Bahan = hasil.getString("bahan");
                String Warna = hasil.getString("warna");
                String Model = hasil.getString("model");
                String Ukuran = hasil.getString("ukuran");
                String Harga = hasil.getString("harga");
                String[] data = {IdKaos,Bahan, Warna, Model,Ukuran,Harga};
                tabmode2.addRow(data);
            }
        } catch (Exception e) {
        }
    }

    public void dataTable3() {
        Object[] Baris = {"Id Supplier", "Nama Supplier"};
        tabmode3 = new DefaultTableModel(null, Baris);
        TabelDataSupplier.setModel(tabmode3);
        String sql = "select * from db_supplier order by id_supplier asc";
        try {
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            while (hasil.next()) {
                String IdSupplier = hasil.getString("id_supplier");
                String NamaSupplier = hasil.getString("nama_supplier");
                String[] data = {IdSupplier, NamaSupplier};
                tabmode3.addRow(data);
            }
        } catch (Exception e) {
        }
    }

    public void lebarKolom() {
        TableColumn column;
        TablePemasukan.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        column = TablePemasukan.getColumnModel().getColumn(0);
        column.setPreferredWidth(40);
        column = TablePemasukan.getColumnModel().getColumn(1);
        column.setPreferredWidth(80);
        column = TablePemasukan.getColumnModel().getColumn(2);
        column.setPreferredWidth(100);
        column = TablePemasukan.getColumnModel().getColumn(3);
        column.setPreferredWidth(100);
        column = TablePemasukan.getColumnModel().getColumn(4);
        column.setPreferredWidth(80);
        column = TablePemasukan.getColumnModel().getColumn(5);
        column.setPreferredWidth(80);
        column = TablePemasukan.getColumnModel().getColumn(6);
        column.setPreferredWidth(80);
        column = TablePemasukan.getColumnModel().getColumn(7);
        column.setPreferredWidth(80);
        column = TablePemasukan.getColumnModel().getColumn(8);
        column.setPreferredWidth(80);
        column = TablePemasukan.getColumnModel().getColumn(9);
        column.setPreferredWidth(80);
        column = TablePemasukan.getColumnModel().getColumn(10);
        column.setPreferredWidth(80);
        column = TablePemasukan.getColumnModel().getColumn(11);
        column.setPreferredWidth(40);
    }

    public void lebarKolom2() {
        TableColumn column2;
        TabelDataKaos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        column2 = TabelDataKaos.getColumnModel().getColumn(0);
        column2.setPreferredWidth(80);
        column2 = TabelDataKaos.getColumnModel().getColumn(1);
        column2.setPreferredWidth(100);
        column2 = TabelDataKaos.getColumnModel().getColumn(2);
        column2.setPreferredWidth(100);
        column2 = TabelDataKaos.getColumnModel().getColumn(3);
        column2.setPreferredWidth(100);
        column2 = TabelDataKaos.getColumnModel().getColumn(4);
        column2.setPreferredWidth(100);
        column2 = TabelDataKaos.getColumnModel().getColumn(5);
        column2.setPreferredWidth(100);
    }

    public void lebarKolom3() {
        TableColumn column3;
        TabelDataSupplier.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        column3 = TabelDataSupplier.getColumnModel().getColumn(0);
        column3.setPreferredWidth(90);
        column3 = TabelDataSupplier.getColumnModel().getColumn(1);
        column3.setPreferredWidth(205);
    }

    public void pencarian(String sql) {
        Object[] Baris = {"No", "Tanggal", "ID Detail" ,"ID Pemasukan", "Supplier", "ID Kaos",
            "Bahan", "Warna", "Model", "Ukuran","Harga", "Stok"};
        tabmode = new DefaultTableModel(null, Baris);
        TablePemasukan.setModel(tabmode);
        int brs = TablePemasukan.getRowCount();
        for (int i = 0; 1 < brs; i++) {
            tabmode.removeRow(1);
        }
        try {
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            while (hasil.next()) {
                String tanggal = hasil.getString("tanggal");
                String IdDetail = hasil.getString("id_detail");
                String IdPemasukan = hasil.getString("id_pemasukan");
                String supplier = hasil.getString("nama_supplier");
                String IdKaos = hasil.getString("id_kaos");
                String Bahan = hasil.getString("bahan");
                String Warna = hasil.getString("warna");
                String Model = hasil.getString("model");
                String Ukuran = hasil.getString("ukuran");
                String Harga = hasil.getString("harga");
                String Stok = hasil.getString("stok");
                String[] data = {"", tanggal, IdDetail, IdPemasukan, supplier, IdKaos,
                    Bahan, Warna, Model,Ukuran,Harga,Stok};
                tabmode.addRow(data);
                noTable();
            }
        } catch (Exception e) {

        }
    }

    public void pencarian2(String sql) {
        Object[] Baris2 = {"Id Kaos", "Bahan", "Warna", "Model", "Ukuran","Harga"};
        tabmode2 = new DefaultTableModel(null, Baris2);
        TabelDataKaos.setModel(tabmode2);
        int brs = TabelDataKaos.getRowCount();
        for (int i = 0; 1 < brs; i++) {
            tabmode2.removeRow(1);
        }
        try {
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            while (hasil.next()) {
                String IdKaos = hasil.getString("id_kaos");
                String Bahan = hasil.getString("bahan");
                String Warna = hasil.getString("warna");
                String Model = hasil.getString("model");
                String Ukuran = hasil.getString("ukuran");
                String Harga = hasil.getString("harga");
                String[] data = {IdKaos,Bahan, Warna, Model,Ukuran,Harga};
                tabmode2.addRow(data);
            }
        } catch (Exception e) {

        }
    }

    public void pencarian3(String sql) {
        Object[] Baris = {"Id Supplier", "Nama Supplier"};
        tabmode3 = new DefaultTableModel(null, Baris);
        TabelDataSupplier.setModel(tabmode3);
        int brs = TabelDataSupplier.getRowCount();
        for (int i = 0; 1 < brs; i++) {
            tabmode3.removeRow(1);
        }
        try {
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            while (hasil.next()) {
                String IdSupplier = hasil.getString("id_supplier");
                String NamaSupplier = hasil.getString("nama_supplier");
                String[] data = {IdSupplier, NamaSupplier};
                tabmode3.addRow(data);
            }
        } catch (Exception e) {

        }
    }

    /**
     * Creates new form Pemasukan
     */
    public Pemasukan(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        aktif();
        IdPemasukan();
        IdDetailPemasukan();
        dataTable();
        dataTable2();
        dataTable3();
        tanggal();
        lebarKolom();
        lebarKolom2();
        lebarKolom3();
        txtIdPemasukan.requestFocus();
        txtIdDetailPemasukan.setVisible(false);
        Locale local = new Locale("id","ID");
        Locale.setDefault(local);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        btnClose = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtIdPemasukan = new javax.swing.JTextField();
        btnTanggal = new com.toedter.calendar.JDateChooser();
        txtNamaSupplier = new javax.swing.JTextField();
        txtWarna = new javax.swing.JTextField();
        txtBahan = new javax.swing.JTextField();
        btnSave = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TablePemasukan = new javax.swing.JTable();
        txtCari = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtModel = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtUkuran = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtHarga = new javax.swing.JTextField();
        txtStok = new javax.swing.JTextField();
        btnAdd = new javax.swing.JButton();
        btnPrint = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        txtDataSupplier = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        TabelDataSupplier = new javax.swing.JTable();
        txtDataKaos = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        TabelDataKaos = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        txtIdKaos = new javax.swing.JTextField();
        txtIdDetailPemasukan = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel4.setBackground(new java.awt.Color(0, 102, 102));

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel7.setText("Pemasukan");

        btnClose.setBackground(new java.awt.Color(0, 102, 102));
        btnClose.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        btnClose.setText("X");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(430, 430, 430)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnClose))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addContainerGap(19, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(btnClose)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel8.setText("Tanggal");

        jLabel9.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel9.setText("ID Pemasukan");

        jLabel10.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel10.setText("Bahan");

        jLabel11.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel11.setText("Warna");

        jLabel12.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel12.setText("Model");

        txtIdPemasukan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtIdPemasukanKeyPressed(evt);
            }
        });

        btnTanggal.setDateFormatString("dd-MM-yyyy");
        btnTanggal.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnTanggal.setMaximumSize(new java.awt.Dimension(2147400000, 2147400000));
        btnTanggal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTanggalMouseClicked(evt);
            }
        });
        btnTanggal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnTanggalKeyPressed(evt);
            }
        });

        txtNamaSupplier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNamaSupplierKeyPressed(evt);
            }
        });

        txtWarna.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtWarnaKeyPressed(evt);
            }
        });

        txtBahan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBahanKeyPressed(evt);
            }
        });

        btnSave.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnDelete.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnClear.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnUpdate.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        TablePemasukan.setModel(new javax.swing.table.DefaultTableModel(
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
        TablePemasukan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablePemasukanMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(TablePemasukan);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 533, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        txtCari.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        txtCari.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCariActionPerformed(evt);
            }
        });
        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCariKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCariKeyTyped(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel1.setText("Ukuran");

        txtModel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtModelActionPerformed(evt);
            }
        });
        txtModel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtModelKeyPressed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel2.setText("Harga");

        jLabel13.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel13.setText("Stok");

        jLabel15.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel15.setText("Nama Supplier");

        txtStok.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtStokKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtStokKeyTyped(evt);
            }
        });

        btnAdd.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnPrint.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnPrint.setText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });

        txtDataSupplier.setText("Data Supplier");
        txtDataSupplier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDataSupplierKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDataSupplierKeyTyped(evt);
            }
        });

        TabelDataSupplier.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Title 1", "Title 2"
            }
        ));
        TabelDataSupplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabelDataSupplierMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TabelDataSupplier);

        txtDataKaos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDataKaosKeyTyped(evt);
            }
        });

        TabelDataKaos.setModel(new javax.swing.table.DefaultTableModel(
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
        TabelDataKaos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabelDataKaosMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(TabelDataKaos);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtDataSupplier, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
            .addComponent(txtDataKaos, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(txtDataSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDataKaos, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel14.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel14.setText("ID Kaos");

        txtIdKaos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtIdKaosKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel15)
                                    .addComponent(jLabel14))
                                .addGap(24, 24, 24)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtModel, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtWarna, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtNamaSupplier, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(btnTanggal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                                        .addComponent(txtIdPemasukan, javax.swing.GroupLayout.Alignment.LEADING))
                                    .addComponent(txtIdKaos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtBahan, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtUkuran, javax.swing.GroupLayout.Alignment.TRAILING)))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(88, 88, 88)
                                .addComponent(txtHarga))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addGap(104, 104, 104)
                                        .addComponent(txtIdDetailPemasukan))
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addGap(98, 98, 98)
                                        .addComponent(txtStok)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(btnUpdate)
                        .addGap(18, 18, 18)
                        .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtCari)
                            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(255, 255, 255))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8)
                            .addComponent(btnTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtIdPemasukan, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(txtNamaSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtIdKaos, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(txtBahan, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(txtWarna, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(4, 4, 4)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(txtModel, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtUkuran, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(txtStok, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(txtIdDetailPemasukan, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 1086, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 2, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void txtIdPemasukanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIdPemasukanKeyPressed
        if(evt.getKeyCode()== KeyEvent.VK_ENTER){
            txtIdPemasukan.requestFocus();
        }
    }//GEN-LAST:event_txtIdPemasukanKeyPressed

    private void btnTanggalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTanggalMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTanggalMouseClicked

    private void btnTanggalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnTanggalKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTanggalKeyPressed

    private void txtNamaSupplierKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNamaSupplierKeyPressed
        if(evt.getKeyCode()== KeyEvent.VK_ENTER){
            txtNamaSupplier.requestFocus();
        }
    }//GEN-LAST:event_txtNamaSupplierKeyPressed

    private void txtWarnaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtWarnaKeyPressed
        if(evt.getKeyCode()== KeyEvent.VK_ENTER){
            txtWarna.requestFocus();
        }
    }//GEN-LAST:event_txtWarnaKeyPressed

    private void txtBahanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBahanKeyPressed
        if(evt.getKeyCode()== KeyEvent.VK_ENTER){
            txtBahan.requestFocus();
        }
    }//GEN-LAST:event_txtBahanKeyPressed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        if(txtIdPemasukan.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Kode Kategori tidak boleh kosong");
            txtIdPemasukan.requestFocus();
        } else if (txtNamaSupplier.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Nama Kategori tidak boleh kosong");
            txtNamaSupplier.requestFocus();
        } else {
            String sql = "insert into db_pemasukan values (?,?,?,?)";
            String tampilan = "dd-MM-yyyy";
            SimpleDateFormat fm = new SimpleDateFormat(tampilan);
            String tanggal = String.valueOf(fm.format(btnTanggal.getDate()));
            try {
                PreparedStatement stat = conn.prepareStatement(sql);
                stat.setString(1, tanggal.toString());
                stat.setString(2, txtIdPemasukan.getText());
                stat.setString(3, txtNamaSupplier.getText());
                stat.setString(4, txtStok.getText());

                stat.executeUpdate();
                JOptionPane.showMessageDialog(null,"Data Berhasil Disimpan");
                //            String refresh = "select * from tb_kategori";
                kosong();
                IdPemasukan();
                IdDetailPemasukan();
                dataTable();
                lebarKolom();
                txtIdPemasukan.requestFocus();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Data Gagal Disimpan"+e);
            }
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        int ok = JOptionPane.showConfirmDialog (null," Apakah Anda Yakin Ingin "
            + "Menghapus Data","Konfirmasi Dialog", JOptionPane.YES_NO_OPTION);
        if (ok==0){
            String sql="delete from db_detailpemasukan where id_detail='"+txtIdPemasukan.getText()+"'";
            String sql2 = "delete from db_pemasukan where id_pemasukan='" + txtIdPemasukan.getText() + "'";
            try {
                PreparedStatement stat=conn.prepareStatement(sql);
                stat.executeUpdate();
                PreparedStatement stat2 = conn.prepareStatement(sql2);
                stat2.executeUpdate();
                JOptionPane.showMessageDialog(null,"Data Berhasil Dihapus");
                kosong();
                IdPemasukan();
                IdDetailPemasukan();
                dataTable();
                lebarKolom();
                txtIdPemasukan.requestFocus();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Data Gagal Dihapus"+e);
            }
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        IdPemasukan();
        IdDetailPemasukan();
        tanggal();
        txtIdPemasukan.requestFocus();
        txtIdDetailPemasukan.requestFocus();
        txtNamaSupplier.setText(null);
        txtIdKaos.setText(null);
        txtBahan.setText(null);
        txtWarna.setText(null);
        txtModel.setText(null);
        txtUkuran.setText(null);
        txtHarga.setText(null);
        txtStok.setText(null);
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        String sql = "update db_detailpemasukan set tanggal=?,id_detail=?,id_pemasukan=?,"
                + "nama_supplier=?,id_kaos=?,bahan=?,warna=?,model=?,ukuran=?,"
                + "harga=?,stok=? where id_detail='"+txtIdPemasukan.getText()+"'";
        String tampilan = "dd-MM-yyyy";
        SimpleDateFormat fm = new SimpleDateFormat(tampilan);
        String tanggal = String.valueOf(fm.format(btnTanggal.getDate()));
        try {
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setString(1, tanggal.toString());
            stat.setString(2, txtIdDetailPemasukan.getText());
            stat.setString(3, txtIdPemasukan.getText());
            stat.setString(4, txtNamaSupplier.getText());
            stat.setString(5, txtIdKaos.getText());
            stat.setString(6, txtBahan.getText());
            stat.setString(7, txtWarna.getText());
            stat.setString(8, txtModel.getText());
            stat.setString(9, txtUkuran.getText());
            stat.setString(10, txtHarga.getText());
            stat.setString(11, txtStok.getText());
            stat.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Berhasil Diubah");
            //            String refresh = "select * from tb_kategori";
            kosong();
            dataTable();
            lebarKolom();
            IdPemasukan();
            IdDetailPemasukan();
            txtIdPemasukan.requestFocus();
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Data Gagal Diubah"+e);
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void TablePemasukanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablePemasukanMouseClicked
        int bar = TablePemasukan.getSelectedRow();
        String a = tabmode.getValueAt(bar, 0).toString();
        String b = tabmode.getValueAt(bar, 1).toString();
        String c = tabmode.getValueAt(bar, 2).toString();
        String d = tabmode.getValueAt(bar, 3).toString();
        String e = tabmode.getValueAt(bar, 4).toString();
        String f = tabmode.getValueAt(bar, 5).toString();
        String g = tabmode.getValueAt(bar, 6).toString();
        String h = tabmode.getValueAt(bar, 7).toString();
        String i = tabmode.getValueAt(bar, 8).toString();
        String j = tabmode.getValueAt(bar, 9).toString();
        String k = tabmode.getValueAt(bar, 10).toString();
        String l = tabmode.getValueAt(bar, 11).toString();

        SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
        Date dateValue = null;
        try{
            dateValue = date.parse((String)TablePemasukan.getValueAt(bar, 1));
        } catch (ParseException ex){
            Logger.getLogger(DataKaos.class.getName()).log(Level.SEVERE, null, ex);
        }

        btnTanggal.setDate(dateValue);
        txtIdDetailPemasukan.setText(c);
        txtIdPemasukan.setText(d);
        txtNamaSupplier.setText(e);
        txtIdKaos.setText(f);
        txtBahan.setText(g);
        txtWarna.setText(h);
        txtModel.setText(i);
        txtUkuran.setText(j);
        txtHarga.setText(k);
        txtStok.setText(l);
        aktif();

    }//GEN-LAST:event_TablePemasukanMouseClicked

    private void txtCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyPressed
        if(evt.getKeyCode()== KeyEvent.VK_ENTER){
            txtIdPemasukan.requestFocus();
            txtNamaSupplier.requestFocus();
            txtBahan.requestFocus();

        }
    }//GEN-LAST:event_txtCariKeyPressed

    private void txtCariKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyTyped
        String sqlPencarian = "select * from db_detailpemasukan where id_pemasukan like '%" 
                + txtCari.getText() + "%'"
                + "or id_detail '%" + txtCari.getText() + "%'"
                + "or id_kaos like '%" + txtCari.getText() + "%'"
                + "or bahan like '%" + txtCari.getText() + "%'"
                + "or supplier like '%" + txtCari.getText() + "%'";
        pencarian(sqlPencarian);
        lebarKolom();
    }//GEN-LAST:event_txtCariKeyTyped

    private void txtModelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtModelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtModelActionPerformed

    private void txtModelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtModelKeyPressed
        if(evt.getKeyCode()== KeyEvent.VK_ENTER){
            txtModel.requestFocus();
        }
    }//GEN-LAST:event_txtModelKeyPressed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        if (txtIdPemasukan.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "ID Pemasukan tidak boleh kosong");
            txtIdPemasukan.requestFocus();
        } else if (txtNamaSupplier.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Nama Supplier tidak boleh kosong");
            txtNamaSupplier.requestFocus();
        } else if (txtIdKaos.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "ID Kaos tidak boleh kosong");
            txtIdKaos.requestFocus();
        } else if (txtBahan.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Bahan tidak boleh kosong");
            txtBahan.requestFocus();
        } else if (txtWarna.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Warna tidak boleh kosong");
            txtWarna.requestFocus();
        }else if (txtModel.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Model tidak boleh kosong");
            txtModel.requestFocus();
        }else if (txtUkuran.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Ukuran tidak boleh kosong");
            txtUkuran.requestFocus();
        }else if (txtHarga.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Harga tidak boleh kosong");
            txtHarga.requestFocus();
        }else if (txtStok.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Qty tidak boleh kosong");
            txtStok.requestFocus();
        } else {
            String sql = "insert into db_detailpemasukan values (?,?,?,?,?,?,?,?,?,?,?)";
            String tampilan = "dd-MM-yyyy";
            SimpleDateFormat fm = new SimpleDateFormat(tampilan);
            String tanggal = String.valueOf(fm.format(btnTanggal.getDate()));
            try {
                PreparedStatement stat = conn.prepareStatement(sql);
                stat.setString(1, tanggal.toString());
                stat.setString(2, txtIdDetailPemasukan.getText());
                stat.setString(3, txtIdPemasukan.getText());
                stat.setString(4, txtNamaSupplier.getText());
                stat.setString(5, txtIdKaos.getText());
                stat.setString(6, txtBahan.getText());
                stat.setString(7, txtWarna.getText());
                stat.setString(8, txtModel.getText());
                stat.setString(9, txtUkuran.getText());
                stat.setString(10, txtHarga.getText());
                stat.setString(11, txtStok.getText());
                stat.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");
                //            String refresh = "select * from tb_barang";
                //autoIdBM();
                IdDetailPemasukan();
                kosong2();
                dataTable();
                lebarKolom();
                txtIdPemasukan.requestFocus();
                //txtNamaSupplier.setEnabled(false);
                txtIdKaos.requestFocus();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Data Gagal Disimpan" + e);
            }
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        try {
            Connection konn = new ConnectionHelper().connect();
            File file = new File("src/laporan/transaksiBM.jrxml");
            JasDes = JRXmlLoader.load(file);
            param.put("id_pemasukan",txtIdPemasukan.getText());
            JasRep = JasperCompileManager.compileReport(JasDes);
            JasPri = JasperFillManager.fillReport(JasRep, param, konn);
            //JasperViewer.viewReport(JasPri, false);
            JasperViewer jasperViewer = new JasperViewer(JasPri, false);
            jasperViewer.setExtendedState(jasperViewer.getExtendedState()|javax.swing.JFrame.MAXIMIZED_BOTH);
            jasperViewer.setVisible(true);
            //jasperViewer.setAlwaysOnTop(maximixed);
            Pemasukan.this.setVisible(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal membuka Laporan", "Cetak laporan", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnPrintActionPerformed

    private void txtCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCariActionPerformed

    private void txtIdKaosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIdKaosKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtIdKaos.requestFocus();
        }
    }//GEN-LAST:event_txtIdKaosKeyPressed

    private void txtStokKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStokKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtStok.requestFocus();
        }
    }//GEN-LAST:event_txtStokKeyPressed

    private void TabelDataKaosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabelDataKaosMouseClicked
        int bar = TabelDataKaos.getSelectedRow();
        String a = tabmode2.getValueAt(bar, 0).toString();
        String b = tabmode2.getValueAt(bar, 1).toString();
        String c = tabmode2.getValueAt(bar, 2).toString();
        String d = tabmode2.getValueAt(bar, 3).toString();
        String e = tabmode2.getValueAt(bar, 4).toString();
        String f = tabmode2.getValueAt(bar, 5).toString();
        txtIdKaos.setText(a);
        txtBahan.setText(b);
        txtWarna.setText(c);
        txtModel.setText(d);
        txtUkuran.setText(e);
        txtHarga.setText(f);
        txtStok.requestFocus();
    }//GEN-LAST:event_TabelDataKaosMouseClicked

    private void txtDataKaosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDataKaosKeyTyped
        String sqlPencarian2 = "select * from db_datakaos where id_kaos like '%" + 
                txtDataKaos.getText() + "%' or bahan like '%" + txtDataKaos.getText() + "%'";
        pencarian2(sqlPencarian2);
        lebarKolom2();
    }//GEN-LAST:event_txtDataKaosKeyTyped

    private void txtStokKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStokKeyTyped
        char enter=evt.getKeyChar();
        if(!(Character.isDigit(enter)))
        {
            evt.consume();
            JOptionPane.showMessageDialog(null, "Masukan Hanya Angka 0-100", "Input Qty", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_txtStokKeyTyped

    private void TabelDataSupplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabelDataSupplierMouseClicked
        int bar = TabelDataSupplier.getSelectedRow();
        String a = tabmode3.getValueAt(bar, 0).toString();
        String b = tabmode3.getValueAt(bar, 1).toString();
        txtNamaSupplier.setText(b);
        txtIdKaos.requestFocus();
    }//GEN-LAST:event_TabelDataSupplierMouseClicked

    private void txtDataSupplierKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDataSupplierKeyTyped
        String sqlPencarian3 = "select * from db_supplier where id_supplier like '%"
        + txtDataSupplier.getText() + "%' or nama_supplier like '%"
        + txtDataSupplier.getText() + "%'";
        pencarian3(sqlPencarian3);
        lebarKolom3();
    }//GEN-LAST:event_txtDataSupplierKeyTyped

    private void txtDataSupplierKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDataSupplierKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtNamaSupplier.requestFocus();
        }
    }//GEN-LAST:event_txtDataSupplierKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Pemasukan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Pemasukan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Pemasukan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Pemasukan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Pemasukan dialog = new Pemasukan(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TabelDataKaos;
    private javax.swing.JTable TabelDataSupplier;
    private javax.swing.JTable TablePemasukan;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnSave;
    private com.toedter.calendar.JDateChooser btnTanggal;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField txtBahan;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtDataKaos;
    private javax.swing.JTextField txtDataSupplier;
    private javax.swing.JTextField txtHarga;
    private javax.swing.JTextField txtIdDetailPemasukan;
    private javax.swing.JTextField txtIdKaos;
    private javax.swing.JTextField txtIdPemasukan;
    private javax.swing.JTextField txtModel;
    private javax.swing.JTextField txtNamaSupplier;
    private javax.swing.JTextField txtStok;
    private javax.swing.JTextField txtUkuran;
    private javax.swing.JTextField txtWarna;
    // End of variables declaration//GEN-END:variables
}
