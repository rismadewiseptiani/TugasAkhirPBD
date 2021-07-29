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
import java.sql.Statement;
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
public class Transaksi extends javax.swing.JDialog {
    public final Connection conn = new ConnectionHelper().connect();
    public long total;
    public long bayar;
    public long kembali;
    public Statement st;

    private DefaultTableModel tabmode;
    private DefaultTableModel tabmode2;

    JasperReport JasRep;
    JasperPrint JasPri;
    Map param = new HashMap();
    JasperDesign JasDes;

    private void aktif() {
        txtIdTransaksi.setEnabled(true);
        txtNamaPembeli.setEnabled(true);
        txtIdKaos.setEnabled(true);
        txtBahan.setEnabled(true);
        txtWarna.setEnabled(true);
        txtModel.setEnabled(true);
        txtUkuran.setEnabled(true);
        txtHarga.setEnabled(true);
        txtQty.setEnabled(true);
        txtTotal.setEnabled(true);
        txtBayar.setEnabled(true);
        txtKembali.setEnabled(true);
    }

    protected void kosong() {
        txtIdTransaksi.setText(null);
        txtNamaPembeli.setText(null);
        txtIdKaos.setText(null);
        txtBahan.setText(null);
        txtWarna.setText(null);
        txtModel.setText(null);
        txtUkuran.setText(null);
        txtHarga.setText(null);
        txtQty.setText(null);
        txtTotal.setText("0");
        txtBayar.setText("0");
        txtKembali.setText("0");
    }

    protected void kosong2() {
        txtIdKaos.setText(null);
        txtBahan.setText(null);
        txtWarna.setText(null);
        txtModel.setText(null);
        txtUkuran.setText(null);
        txtHarga.setText(null);
        txtQty.setText(null);
        txtTotal.setText("0");
        txtBayar.setText("0");
        txtKembali.setText("0");
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
    public void hitung() {
        
    }

    private void Id_Transaksi() {
        try {
            Connection con = new ConnectionHelper().connect();
            java.sql.Statement stat = con.createStatement();
            String sql = "select max(right (id_transaksi,6)) as no from db_transaksi";
            ResultSet res = stat.executeQuery(sql);
            while (res.next()) {
                if (res.first() == false) {
                    txtIdTransaksi.setText("A-00001");
                } else {
                    res.last();
                    int aut_id = res.getInt(1) + 1;
                    String no = String.valueOf(aut_id);
                    int no_jual = no.length();
                    for (int j = 0; j < 6 - no_jual; j++) {
                        no = "0" + no;
                    }
                    txtIdTransaksi.setText("A-" + no);
                }
            }
            res.close();
            stat.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi Kesalahan");
        }
    }

    private void Id_DetailTransaksi() {
        try {
            Connection con = new ConnectionHelper().connect();
            java.sql.Statement stat = con.createStatement();
            String sql_dt = "select max(right (id_detail,4)) as no from db_detailtransaksi";
            ResultSet res_dt = stat.executeQuery(sql_dt);
            while (res_dt.next()) {
                if (res_dt.first() == false) {
                    txtIdDetailTransaksi.setText("A-0001");
                } else {
                    res_dt.last();
                    int aut_id_dt = res_dt.getInt(1) + 1;
                    String no_dt = String.valueOf(aut_id_dt);
                    int no_jual_dt = no_dt.length();
                    // mengatur jumlah 0
                    for (int j = 0; j < 4 - no_jual_dt; j++) {
                        no_dt = "0" + no_dt;
                    }
                    txtIdDetailTransaksi.setText("A-" + no_dt);
                }
            }
            res_dt.close();
            stat.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi Kesalahan");
        }
    }
        
    public void dataTable() {
        Object[] Baris = {"No", "Tanggal", "ID Detail" ,"ID Pemasukan", "Pembeli", "ID Kaos",
            "Bahan", "Warna", "Model", "Ukuran","Harga", "Qty","Total"};
        tabmode = new DefaultTableModel(null, Baris);
        tabelTransaksi.setModel(tabmode);
        String sql = "select * from db_detailtransaksi order by tanggal asc";
        try {
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            while (hasil.next()) {
                String tanggal = hasil.getString("tanggal");
                String IdPemasukan = hasil.getString("id_transaksi");
                String IdDetail = hasil.getString("id_detail");
                String supplier = hasil.getString("nama_pembeli");
                String IdKaos = hasil.getString("id_kaos");
                String Bahan = hasil.getString("bahan");
                String Warna = hasil.getString("warna");
                String Model = hasil.getString("model");
                String Ukuran = hasil.getString("ukuran");
                String Harga = hasil.getString("harga");
                String Qty = hasil.getString("qty");
                String Total = hasil.getString("total");
                String[] data = {"", tanggal, IdDetail, IdPemasukan, supplier, IdKaos,
                    Bahan, Warna, Model,Ukuran,Harga,Qty,Total};
                tabmode.addRow(data);
                noTable();
            }
        } catch (Exception e) {
        }
    }

    public void dataTable2() {
        Object[] Baris = {"Id Kaos", "Bahan", "Warna", "Model", "Ukuran","Harga","Stok"};
        tabmode2 = new DefaultTableModel(null, Baris);
        TabelDataKaos.setModel(tabmode2);
        String sql = "select * from db_detailpemasukan order by id_kaos asc";
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
                String Stok = hasil.getString("stok");
                String[] data = {IdKaos,Bahan, Warna, Model,Ukuran,Harga,Stok};
                tabmode2.addRow(data);
            }
        } catch (Exception e) {
        }
    }

    public void lebarKolom() {
        TableColumn column;
        tabelTransaksi.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        column = tabelTransaksi.getColumnModel().getColumn(0);
        column.setPreferredWidth(40);
        column = tabelTransaksi.getColumnModel().getColumn(1);
        column.setPreferredWidth(80);
        column = tabelTransaksi.getColumnModel().getColumn(2);
        column.setPreferredWidth(100);
        column = tabelTransaksi.getColumnModel().getColumn(3);
        column.setPreferredWidth(100);
        column = tabelTransaksi.getColumnModel().getColumn(4);
        column.setPreferredWidth(80);
        column = tabelTransaksi.getColumnModel().getColumn(5);
        column.setPreferredWidth(80);
        column = tabelTransaksi.getColumnModel().getColumn(6);
        column.setPreferredWidth(80);
        column = tabelTransaksi.getColumnModel().getColumn(7);
        column.setPreferredWidth(80);
        column = tabelTransaksi.getColumnModel().getColumn(8);
        column.setPreferredWidth(80);
        column = tabelTransaksi.getColumnModel().getColumn(9);
        column.setPreferredWidth(80);
        column = tabelTransaksi.getColumnModel().getColumn(10);
        column.setPreferredWidth(80);
        column = tabelTransaksi.getColumnModel().getColumn(11);
        column.setPreferredWidth(40);
        column = tabelTransaksi.getColumnModel().getColumn(12);
        column.setPreferredWidth(80);
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
        column2 = tabelTransaksi.getColumnModel().getColumn(6);
        column2.setPreferredWidth(40);
        column2 = tabelTransaksi.getColumnModel().getColumn(7);
        column2.setPreferredWidth(80);
    }
    public void pencarian(String sql) {
        Object[] Baris = {"No", "Tanggal", "ID Detail" ,"ID Pemasukan", "Supplier", "ID Kaos",
            "Bahan", "Warna", "Model", "Ukuran","Harga", "Qty","Total"};
        tabmode = new DefaultTableModel(null, Baris);
        tabelTransaksi.setModel(tabmode);
        int brs = tabelTransaksi.getRowCount();
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
                String Qty = hasil.getString("qty");
                String Total = hasil.getString("total");
                String[] data = {"", tanggal, IdDetail, IdPemasukan, supplier, IdKaos,
                    Bahan, Warna, Model,Ukuran,Harga,Qty,Total};
                tabmode.addRow(data);
                noTable();
            }
        } catch (Exception e) {

        }
    }

    public void pencarian2(String sql) {
        Object[] Baris2 = {"Id Kaos", "Bahan", "Warna", "Model", "Ukuran","Harga","Stok"};
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
                String Stok = hasil.getString("stok");
                String[] data = {IdKaos,Bahan, Warna, Model,Ukuran,Harga,Stok};
                tabmode2.addRow(data);
            }
        } catch (Exception e) {

        }
    }

    /**
     * Creates new form Transaksi
     */
    public Transaksi(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        aktif();
        Id_Transaksi();
        Id_DetailTransaksi();
        dataTable();
        dataTable2();
        tanggal();
        lebarKolom();
        lebarKolom2();
        txtIdTransaksi.requestFocus();
        txtIdDetailTransaksi.setVisible(false);
        txtStok.setVisible(false);
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
        txtIdTransaksi = new javax.swing.JTextField();
        btnTanggal = new com.toedter.calendar.JDateChooser();
        txtNamaPembeli = new javax.swing.JTextField();
        txtWarna = new javax.swing.JTextField();
        txtBahan = new javax.swing.JTextField();
        btnSave = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelTransaksi = new javax.swing.JTable();
        txtCari = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        TabelDataKaos = new javax.swing.JTable();
        txtDataKaos = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtModel = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtUkuran = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtHarga = new javax.swing.JTextField();
        txtQty = new javax.swing.JTextField();
        btnAdd = new javax.swing.JButton();
        btnPrint = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtBayar = new javax.swing.JTextField();
        txtKembali = new javax.swing.JTextField();
        txtIdKaos = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtIdDetailTransaksi = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        btnHitung = new javax.swing.JButton();
        txtStok = new javax.swing.JTextField();
        btnproses = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel4.setBackground(new java.awt.Color(0, 102, 102));

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel7.setText("Transaksi");

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
        jLabel9.setText("ID Transaksi");

        jLabel10.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel10.setText("Bahan");

        jLabel11.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel11.setText("Warna");

        jLabel12.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel12.setText("Model");

        txtIdTransaksi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtIdTransaksiKeyPressed(evt);
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

        txtNamaPembeli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNamaPembeliKeyPressed(evt);
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

        tabelTransaksi.setModel(new javax.swing.table.DefaultTableModel(
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
        tabelTransaksi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelTransaksiMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabelTransaksi);

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

        txtDataKaos.setText("Data Kaos");
        txtDataKaos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDataKaosActionPerformed(evt);
            }
        });
        txtDataKaos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDataKaosKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtCari)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 735, Short.MAX_VALUE)
            .addComponent(txtDataKaos)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(txtDataKaos, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE))
        );

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
        jLabel13.setText("Qty");

        jLabel15.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel15.setText("Nama Pembeli");

        txtQty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtQtyKeyTyped(evt);
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

        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel3.setText("Bayar");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel5.setText("Kembali");

        txtIdKaos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtIdKaosKeyPressed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel16.setText("Id Kaos");

        jLabel14.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel14.setText("Total");

        btnHitung.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnHitung.setText("Hitung");
        btnHitung.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHitungActionPerformed(evt);
            }
        });

        btnproses.setText("proses");
        btnproses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnprosesActionPerformed(evt);
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
                            .addComponent(jLabel3)
                            .addComponent(jLabel5))
                        .addGap(70, 70, 70)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtKembali, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnproses, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnHitung, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnUpdate)
                        .addGap(18, 18, 18)
                        .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel15)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel2))
                                .addGap(24, 24, 24))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtQty, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtHarga, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtUkuran, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtModel, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtWarna, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtBahan, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnTanggal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                            .addComponent(txtIdTransaksi, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNamaPembeli, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtIdKaos, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTotal, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(txtIdDetailTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtStok, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(306, 306, 306))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8)
                            .addComponent(btnTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtIdTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNamaPembeli, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtIdKaos, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBahan, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtWarna, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(txtModel, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtUkuran, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(txtQty, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnproses, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtKembali, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnHitung, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnClear, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                                    .addComponent(btnSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(btnPrint, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(23, 23, 23)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIdDetailTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtStok, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 1086, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
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

    private void txtIdTransaksiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIdTransaksiKeyPressed
        if(evt.getKeyCode()== KeyEvent.VK_ENTER){
            txtIdTransaksi.requestFocus();
        }
    }//GEN-LAST:event_txtIdTransaksiKeyPressed

    private void btnTanggalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTanggalMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTanggalMouseClicked

    private void btnTanggalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnTanggalKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTanggalKeyPressed

    private void txtNamaPembeliKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNamaPembeliKeyPressed
        if(evt.getKeyCode()== KeyEvent.VK_ENTER){
            txtNamaPembeli.requestFocus();
        }
    }//GEN-LAST:event_txtNamaPembeliKeyPressed

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
        if(txtIdTransaksi.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Kode Kategori tidak boleh kosong");
            txtIdTransaksi.requestFocus();
        } else if (txtNamaPembeli.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Nama Kategori tidak boleh kosong");
            txtNamaPembeli.requestFocus();
        } else {
            String sql = "insert into db_transaksi values (?,?,?,?)";
            String tampilan = "dd-MM-yyyy";
            SimpleDateFormat fm = new SimpleDateFormat(tampilan);
            String tanggal = String.valueOf(fm.format(btnTanggal.getDate()));
            try {
                PreparedStatement stat = conn.prepareStatement(sql);
                stat.setString(1, tanggal.toString());
                stat.setString(2, txtIdTransaksi.getText());
                stat.setString(3, txtNamaPembeli.getText());
                stat.setString(4, txtTotal.getText());

                stat.executeUpdate();
                JOptionPane.showMessageDialog(null,"Data Berhasil Disimpan");
                //            String refresh = "select * from tb_kategori";
                kosong();
                dataTable();
                lebarKolom();
                Id_Transaksi();
                Id_DetailTransaksi();
                txtIdTransaksi.requestFocus();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Data Gagal Disimpan"+e);
            }
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        int ok = JOptionPane.showConfirmDialog (null," Apakah Anda Yakin Ingin "
            + "Menghapus Data","Konfirmasi Dialog", JOptionPane.YES_NO_OPTION);
        if (ok==0){
            String sql="delete from db_detailtransaksi where id_transaksi='"+txtIdTransaksi.getText()+"'";
            String sql2 = "delete from db_transaksi where id_transaksi='" + txtIdTransaksi.getText() + "'";
            try {
                PreparedStatement stat=conn.prepareStatement(sql);
                stat.executeUpdate();
                PreparedStatement stat2 = conn.prepareStatement(sql2);
                stat2.executeUpdate();
                JOptionPane.showMessageDialog(null,"Data Berhasil Dihapus");
                kosong();
                dataTable();
                lebarKolom();
                Id_Transaksi();
                Id_DetailTransaksi();
                txtIdTransaksi.requestFocus();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Data Gagal Dihapus"+e);
            }
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        tanggal();
        txtIdTransaksi.requestFocus();
        txtIdDetailTransaksi.requestFocus();
        txtIdKaos.setText(null);
        txtNamaPembeli.setText(null);
        txtBahan.setText(null);
        txtWarna.setText(null);
        txtModel.setText(null);
        txtUkuran.setText(null);
        txtHarga.setText(null);
        txtQty.setText(null);
        txtTotal.setText(null);
        txtBayar.setText(null);
        txtKembali.setText(null);
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        String sql = "update db_detailtransaksi set tanggal=?,id_kaos=?,bahan=?,"
        + "warna=?,model=?,ukuran=?,harga=? where id_kaos='"+txtIdTransaksi.getText()+"'";
        String tampilan = "dd-MM-yyyy";
        SimpleDateFormat fm = new SimpleDateFormat(tampilan);
        String tanggal = String.valueOf(fm.format(btnTanggal.getDate()));
        try {
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setString(1, tanggal.toString());
            stat.setString(2, txtIdDetailTransaksi.getText());
            stat.setString(3, txtIdTransaksi.getText());
            stat.setString(4, txtNamaPembeli.getText());
            stat.setString(5, txtIdKaos.getText());
            stat.setString(6, txtBahan.getText());
            stat.setString(7, txtWarna.getText());
            stat.setString(8, txtModel.getText());
            stat.setString(9, txtUkuran.getText());
            stat.setString(10, txtHarga.getText());
            stat.setString(11, txtQty.getText());
            stat.setString(12, txtTotal.getText());
            stat.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Berhasil Diubah");
            //            String refresh = "select * from tb_kategori";
            kosong();
            dataTable();
            lebarKolom();
            Id_Transaksi();
            Id_DetailTransaksi();
            txtIdTransaksi.requestFocus();
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Data Gagal Diubah"+e);
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void tabelTransaksiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelTransaksiMouseClicked
        int bar = tabelTransaksi.getSelectedRow();
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
        String m = tabmode.getValueAt(bar, 12).toString();

        SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
        Date dateValue = null;
        try{
            dateValue = date.parse((String)tabelTransaksi.getValueAt(bar, 1));
        } catch (ParseException ex){
            Logger.getLogger(DataKaos.class.getName()).log(Level.SEVERE, null, ex);
        }

        btnTanggal.setDate(dateValue);
        txtIdDetailTransaksi.setText(c);
        txtIdTransaksi.setText(d);
        txtNamaPembeli.setText(e);
        txtIdKaos.setText(f);
        txtBahan.setText(g);
        txtWarna.setText(h);
        txtModel.setText(i);
        txtUkuran.setText(j);
        txtHarga.setText(k);
        txtQty.setText(l);
        txtTotal.setText(m);
        aktif();
    }//GEN-LAST:event_tabelTransaksiMouseClicked

    private void txtCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCariActionPerformed

    private void txtCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyPressed
        if(evt.getKeyCode()== KeyEvent.VK_ENTER){
            txtIdTransaksi.requestFocus();
            txtNamaPembeli.requestFocus();
            txtIdDetailTransaksi.requestFocus();

        }
    }//GEN-LAST:event_txtCariKeyPressed

    private void txtCariKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyTyped
        String sqlPencarian = "select * from db_detailtransaksi where id_transaksi like '%" 
                + txtCari.getText() + "%'"
                + "or id_detail '%" + txtCari.getText() + "%'"
                + "or id_kaos like '%" + txtCari.getText() + "%'"
                + "or bahan like '%" + txtCari.getText() + "%'"
                + "or nama_pembeli like '%" + txtCari.getText() + "%'";
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
        if (txtIdTransaksi.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "ID Pemasukan tidak boleh kosong");
            txtIdTransaksi.requestFocus();
        } else if (txtNamaPembeli.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Nama Supplier tidak boleh kosong");
            txtNamaPembeli.requestFocus();
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
        }else if (txtQty.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Qty tidak boleh kosong");
            txtQty.requestFocus();
        }else if (txtTotal.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Total tidak boleh kosong");
            txtTotal.requestFocus();
        
        } else {
            String sql = "insert into db_detailtransaksi values (?,?,?,?,?,?,?,?,?,?,?,?)";
            String tampilan = "dd-MM-yyyy";
            SimpleDateFormat fm = new SimpleDateFormat(tampilan);
            String tanggal = String.valueOf(fm.format(btnTanggal.getDate()));
            try {
                PreparedStatement stat = conn.prepareStatement(sql);
                stat.setString(1, tanggal.toString());
                stat.setString(2, txtIdDetailTransaksi.getText());
                stat.setString(3, txtIdTransaksi.getText());
                stat.setString(4, txtNamaPembeli.getText());
                stat.setString(5, txtIdKaos.getText());
                stat.setString(6, txtBahan.getText());
                stat.setString(7, txtWarna.getText());
                stat.setString(8, txtModel.getText());
                stat.setString(9, txtUkuran.getText());
                stat.setString(10, txtHarga.getText());
                stat.setString(11, txtQty.getText());
                stat.setString(12, txtTotal.getText());
                stat.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");
                //            String refresh = "select * from tb_barang";
                //autoIdBM();
                Id_DetailTransaksi();
                kosong2();
                dataTable();
                lebarKolom();
                txtIdTransaksi.requestFocus();
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
            param.put("id_transaksi",txtIdTransaksi.getText());
            JasRep = JasperCompileManager.compileReport(JasDes);
            JasPri = JasperFillManager.fillReport(JasRep, param, konn);
            //JasperViewer.viewReport(JasPri, false);
            JasperViewer jasperViewer = new JasperViewer(JasPri, false);
            jasperViewer.setExtendedState(jasperViewer.getExtendedState()|javax.swing.JFrame.MAXIMIZED_BOTH);
            jasperViewer.setVisible(true);
            //jasperViewer.setAlwaysOnTop(maximixed);
            Transaksi.this.setVisible(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal membuka Laporan", "Cetak laporan", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnPrintActionPerformed

    private void txtDataKaosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDataKaosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDataKaosActionPerformed

    private void txtIdKaosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIdKaosKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdKaosKeyPressed

    private void TabelDataKaosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabelDataKaosMouseClicked
        int bar = TabelDataKaos.getSelectedRow();
        String a = tabmode2.getValueAt(bar, 0).toString();
        String b = tabmode2.getValueAt(bar, 1).toString();
        String c = tabmode2.getValueAt(bar, 2).toString();
        String d = tabmode2.getValueAt(bar, 3).toString();
        String e = tabmode2.getValueAt(bar, 4).toString();
        String f = tabmode2.getValueAt(bar, 5).toString();
        String g = tabmode2.getValueAt(bar, 6).toString();
        txtIdKaos.setText(a);
        txtBahan.setText(b);
        txtWarna.setText(c);
        txtModel.setText(d);
        txtUkuran.setText(e);
        txtHarga.setText(f);
        txtStok.setText(g);
        txtTotal.requestFocus();
    }//GEN-LAST:event_TabelDataKaosMouseClicked

    private void txtDataKaosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDataKaosKeyTyped
        String sqlPencarian2 = "select * from db_detailpemasukan where id_kaos like '%" + 
                txtDataKaos.getText() + "%' or bahan like '%" + txtDataKaos.getText() + "%'";
        pencarian2(sqlPencarian2);
        lebarKolom2();
    }//GEN-LAST:event_txtDataKaosKeyTyped

    private void txtQtyKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQtyKeyTyped
        char enter=evt.getKeyChar();
        if(!(Character.isDigit(enter)))
        {
            evt.consume();
            JOptionPane.showMessageDialog(null, "Masukan Hanya Angka 0-100", "Input Qty", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_txtQtyKeyTyped

    private void btnHitungActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHitungActionPerformed
    // TODO add your handling code here:
         if(txtHarga.getText().equals("") 
                 ||txtQty.getText().equals("")||txtIdTransaksi.getText().equals("") ||txtIdDetailTransaksi.getText().equals("") || 
                 txtNamaPembeli.getText().equals("")|| txtIdKaos.getText().equals("")|| txtBahan.getText().equals("")||
                 txtWarna.getText().equals("")||txtModel.getText().equals("")||txtUkuran.getText().equals("")){
            JOptionPane.showMessageDialog(null, "LENGKAPI DATA !", "Family Collection", JOptionPane.INFORMATION_MESSAGE);
        
        }else{
        String a = txtQty.getText();
        int aa = Integer.parseInt(a);
        
        String b = txtStok.getText();
        int bb = Integer.parseInt(b);
        if(aa > bb){
             JOptionPane.showMessageDialog(null, "jumlah melebihi stok", "Family Collection", JOptionPane.INFORMATION_MESSAGE);
             txtQty.setText("");
        }else{
       
        if(txtQty.getText().equals("")){
            JOptionPane.showMessageDialog(null, "ISI JUMLAH BELI !");
        }else{
        int jumlah, harga, total;
       
        jumlah = Integer.parseInt(txtQty.getText().toString());
        harga = Integer.parseInt(txtHarga.getText().toString());
        total = jumlah * harga;
       
       
             txtTotal.setText(Integer.toString(total));
        
        }
        }
        }  
        
        
    
        
    
        
    }//GEN-LAST:event_btnHitungActionPerformed

    private void btnprosesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnprosesActionPerformed
        bayar = Integer.parseInt(String.valueOf(txtBayar.getText()));
            total = Integer.parseInt(String.valueOf(txtTotal.getText()));
            kembali = bayar - total;
            
            txtKembali.setText(Long.toString(kembali));
    }//GEN-LAST:event_btnprosesActionPerformed

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
            java.util.logging.Logger.getLogger(Transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Transaksi dialog = new Transaksi(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnHitung;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnSave;
    private com.toedter.calendar.JDateChooser btnTanggal;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JButton btnproses;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tabelTransaksi;
    private javax.swing.JTextField txtBahan;
    private javax.swing.JTextField txtBayar;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtDataKaos;
    private javax.swing.JTextField txtHarga;
    private javax.swing.JTextField txtIdDetailTransaksi;
    private javax.swing.JTextField txtIdKaos;
    private javax.swing.JTextField txtIdTransaksi;
    private javax.swing.JTextField txtKembali;
    private javax.swing.JTextField txtModel;
    private javax.swing.JTextField txtNamaPembeli;
    private javax.swing.JTextField txtQty;
    private javax.swing.JTextField txtStok;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txtUkuran;
    private javax.swing.JTextField txtWarna;
    // End of variables declaration//GEN-END:variables
}
