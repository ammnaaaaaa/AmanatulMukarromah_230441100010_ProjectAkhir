/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package projectakhir;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class RiwayatFrame extends javax.swing.JFrame {

    Connection conn;
    private DefaultTableModel modelLahan;
    private DefaultTableModel modelTanaman;
    private DefaultTableModel modelPemupukan;
    
    public RiwayatFrame() {
    initComponents();
    conn = koneksi.getConnection(); // Inisialisasi koneksi ke database
    
    // Model untuk tabel lahan (tidak bisa diedit)
    modelLahan = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Membuat semua sel tidak bisa diedit
        }
    };
    tblLahan.setModel(modelLahan);
    modelLahan.addColumn("ID");
    modelLahan.addColumn("JENIS LAHAN");
    modelLahan.addColumn("UKURAN TANAH");
    modelLahan.addColumn("STATUS");
    
    // Model untuk tabel tanaman (tidak bisa diedit)
    modelTanaman = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Membuat semua sel tidak bisa diedit
        }
    };
    tblTanaman.setModel(modelTanaman);
    modelTanaman.addColumn("ID");
    modelTanaman.addColumn("ID LAHAN");
    modelTanaman.addColumn("JENIS TANAMAN");
    modelTanaman.addColumn("MUSIM PANEN");
    
    // Model untuk tabel pemupukan (tidak bisa diedit)
    modelPemupukan = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Membuat semua sel tidak bisa diedit
        }
    };
    tblPemupukan.setModel(modelPemupukan);
    modelPemupukan.addColumn("ID");
    modelPemupukan.addColumn("ID TANAMAN");
    modelPemupukan.addColumn("TANGGAL");
    modelPemupukan.addColumn("JENIS PUPUK");
    
    // Load data ke dalam tabel
    loadDataLahan();
    loadDataTanaman();
    loadDataPemupukan();
}

    

    
    private void loadDataLahan() {
        modelLahan.setRowCount(0);
        try {
            String sql = "SELECT * FROM lahan";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                modelLahan.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("jenis_tanah"),
                    rs.getString("ukuran_tanah"),
                    rs.getString("status")
                });
            }
        } catch (SQLException e) {
            System.out.println("Error loading data lahan: " + e.getMessage());
        }
    }

    private void loadDataTanaman() {
        modelTanaman.setRowCount(0);
        try {
            String sql = "SELECT * FROM tanaman";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                modelTanaman.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("id_lahan"),
                    rs.getString("jenis_tanaman"),
                    rs.getString("musim_panen")
                });
            }
        } catch (SQLException e) {
            System.out.println("Error loading data tanaman: " + e.getMessage());
        }
    }
    private void loadDataPemupukan() {
        modelPemupukan.setRowCount(0);
        try {
            String sql = "SELECT * FROM pemupukan";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                modelPemupukan.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("id_tanaman"),
                    rs.getString("tanggal"),
                    rs.getString("jenis_pupuk")
                });
            }
        } catch (SQLException e) {
            System.out.println("Error loading data pemupukan: " + e.getMessage());
        }
    }
    
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblLahan = new javax.swing.JTable();
        btn_print_lahan = new javax.swing.JButton();
        btn_back_lahan = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblTanaman = new javax.swing.JTable();
        btn_print_tanaman = new javax.swing.JButton();
        btn_back_tanaman = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblPemupukan = new javax.swing.JTable();
        btn_print_pemupukan = new javax.swing.JButton();
        btn_back_pemupukan = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(51, 102, 0));

        tblLahan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "JENIS LAHAN", "UKURAN TANAH", "STATUS"
            }
        ));
        jScrollPane1.setViewportView(tblLahan);

        btn_print_lahan.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        btn_print_lahan.setText("PRINT");
        btn_print_lahan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_print_lahanActionPerformed(evt);
            }
        });

        btn_back_lahan.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N
        btn_back_lahan.setText("BACK");
        btn_back_lahan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_back_lahanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 539, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_print_lahan)
                    .addComponent(btn_back_lahan))
                .addGap(41, 41, 41))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btn_print_lahan)
                        .addGap(27, 27, 27)
                        .addComponent(btn_back_lahan))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(61, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("LAHAN", jPanel2);

        jPanel3.setBackground(new java.awt.Color(51, 102, 0));

        tblTanaman.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "ID LAHAN", "JENIS TANAMAN", "MUSIM PANEN"
            }
        ));
        jScrollPane2.setViewportView(tblTanaman);

        btn_print_tanaman.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N
        btn_print_tanaman.setText("PRINT");
        btn_print_tanaman.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_print_tanamanActionPerformed(evt);
            }
        });

        btn_back_tanaman.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N
        btn_back_tanaman.setText("BACK");
        btn_back_tanaman.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_back_tanamanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 522, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_print_tanaman)
                    .addComponent(btn_back_tanaman))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btn_print_tanaman)
                        .addGap(24, 24, 24)
                        .addComponent(btn_back_tanaman))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(41, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("TANAMAN", jPanel3);

        jPanel4.setBackground(new java.awt.Color(51, 102, 0));

        tblPemupukan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "ID TANAMAN", "TANGGAL ", "JENIS PUPUK"
            }
        ));
        jScrollPane3.setViewportView(tblPemupukan);

        btn_print_pemupukan.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N
        btn_print_pemupukan.setText("PRINT");
        btn_print_pemupukan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_print_pemupukanActionPerformed(evt);
            }
        });

        btn_back_pemupukan.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N
        btn_back_pemupukan.setText("BACK");
        btn_back_pemupukan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_back_pemupukanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 521, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_back_pemupukan)
                    .addComponent(btn_print_pemupukan))
                .addGap(63, 63, 63))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(btn_print_pemupukan)
                        .addGap(35, 35, 35)
                        .addComponent(btn_back_pemupukan))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("PEMUPUKAN", jPanel4);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 802, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 29, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_print_lahanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_print_lahanActionPerformed
     String fileName = JOptionPane.showInputDialog(null, "Masukkan nama file PDF:", "Nama File", JOptionPane.PLAIN_MESSAGE);

    // Jika pengguna menekan "Cancel" atau tidak mengisi nama, batalkan proses
    if (fileName == null || fileName.trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Pembuatan PDF dibatalkan.");
        return;
    }

    // Set ekstensi .pdf jika pengguna tidak menambahkannya
    if (!fileName.toLowerCase().endsWith(".pdf")) {
        fileName += ".pdf";
    }

    // Tentukan lokasi penyimpanan file PDF
    String filePath = "D:/pemvis c/pdf/" + fileName;

    Document document = new Document(PageSize.A4);

    try {
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Judul PDF
        Font fontHeader = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        Paragraph title = new Paragraph("Laporan Data Lahan", fontHeader);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph("\n"));

        // Membuat tabel PDF dengan jumlah kolom sesuai dengan tabel tblLahan
        PdfPTable table = new PdfPTable(tblLahan.getColumnCount());
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Menambahkan header kolom ke tabel PDF
        for (int i = 0; i < tblLahan.getColumnCount(); i++) {
            PdfPCell cell = new PdfPCell(new Phrase(tblLahan.getColumnName(i), fontHeader));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);
        }

        // Menambahkan data baris dari tabel tblLahan ke tabel PDF
        for (int i = 0; i < tblLahan.getRowCount(); i++) {
            for (int j = 0; j < tblLahan.getColumnCount(); j++) {
                Object cellValue = tblLahan.getValueAt(i, j);
                String cellText = (cellValue != null) ? cellValue.toString() : "";
                PdfPCell cell = new PdfPCell(new Phrase(cellText));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }
        }

        // Menambahkan tabel ke dokumen PDF
        document.add(table);

        JOptionPane.showMessageDialog(null, "PDF berhasil disimpan di lokasi: " + filePath);

    } catch (DocumentException | FileNotFoundException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Gagal menyimpan PDF: " + e.getMessage());
    } finally {
        document.close();
    }
    }//GEN-LAST:event_btn_print_lahanActionPerformed

    private void btn_print_tanamanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_print_tanamanActionPerformed
                                             
    String fileName = JOptionPane.showInputDialog(null, "Masukkan nama file PDF:", "Nama File", JOptionPane.PLAIN_MESSAGE);

    // Jika pengguna menekan "Cancel" atau tidak mengisi nama, batalkan proses
    if (fileName == null || fileName.trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Pembuatan PDF dibatalkan.");
        return;
    }

    // Set ekstensi .pdf jika pengguna tidak menambahkannya
    if (!fileName.toLowerCase().endsWith(".pdf")) {
        fileName += ".pdf";
    }

    // Tentukan lokasi penyimpanan file PDF
    String filePath = "D:/pemvis c/pdf/" + fileName;

    Document document = new Document(PageSize.A4);

    try {
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Judul PDF
        Font fontHeader = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        Paragraph title = new Paragraph("Laporan Data Tanaman", fontHeader);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph("\n"));

        // Membuat tabel PDF dengan jumlah kolom sesuai dengan tabel tblTanaman
        PdfPTable table = new PdfPTable(tblTanaman.getColumnCount());
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Menambahkan header kolom ke tabel PDF
        for (int i = 0; i < tblTanaman.getColumnCount(); i++) {
            PdfPCell cell = new PdfPCell(new Phrase(tblTanaman.getColumnName(i), fontHeader));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);
        }

        // Menambahkan data baris dari tabel tblTanaman ke tabel PDF
        for (int i = 0; i < tblTanaman.getRowCount(); i++) {
            for (int j = 0; j < tblTanaman.getColumnCount(); j++) {
                Object cellValue = tblTanaman.getValueAt(i, j);
                String cellText = (cellValue != null) ? cellValue.toString() : "";
                PdfPCell cell = new PdfPCell(new Phrase(cellText));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }
        }

        // Menambahkan tabel ke dokumen PDF
        document.add(table);

        JOptionPane.showMessageDialog(null, "PDF berhasil disimpan di lokasi: " + filePath);

    } catch (DocumentException | FileNotFoundException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Gagal menyimpan PDF: " + e.getMessage());
    } finally {
        document.close();
    }
    }//GEN-LAST:event_btn_print_tanamanActionPerformed

    private void btn_print_pemupukanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_print_pemupukanActionPerformed
    String fileName = JOptionPane.showInputDialog(null, "Masukkan nama file PDF:", "Nama File", JOptionPane.PLAIN_MESSAGE);

    // Jika pengguna menekan "Cancel" atau tidak mengisi nama, batalkan proses
    if (fileName == null || fileName.trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Pembuatan PDF dibatalkan.");
        return;
    }

    // Set ekstensi .pdf jika pengguna tidak menambahkannya
    if (!fileName.toLowerCase().endsWith(".pdf")) {
        fileName += ".pdf";
    }

    // Tentukan lokasi penyimpanan file PDF
    String filePath = "D:/pemvis c/pdf/" + fileName;

    Document document = new Document(PageSize.A4);

    try {
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Judul PDF
        Font fontHeader = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        Paragraph title = new Paragraph("Laporan Data Pemupukan", fontHeader);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph("\n"));

        // Membuat tabel PDF dengan jumlah kolom sesuai dengan tabel tblPemupukan
        PdfPTable table = new PdfPTable(tblPemupukan.getColumnCount());
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Menambahkan header kolom ke tabel PDF
        for (int i = 0; i < tblPemupukan.getColumnCount(); i++) {
            PdfPCell cell = new PdfPCell(new Phrase(tblPemupukan.getColumnName(i), fontHeader));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);
        }

        // Menambahkan data baris dari tabel tblPemupukan ke tabel PDF
        for (int i = 0; i < tblPemupukan.getRowCount(); i++) {
            for (int j = 0; j < tblPemupukan.getColumnCount(); j++) {
                Object cellValue = tblPemupukan.getValueAt(i, j);
                String cellText = (cellValue != null) ? cellValue.toString() : "";
                PdfPCell cell = new PdfPCell(new Phrase(cellText));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }
        }

        // Menambahkan tabel ke dokumen PDF
        document.add(table);

        JOptionPane.showMessageDialog(null, "PDF berhasil disimpan di lokasi: " + filePath);

    } catch (DocumentException | FileNotFoundException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Gagal menyimpan PDF: " + e.getMessage());
    } finally {
        document.close();
    }
 

    }//GEN-LAST:event_btn_print_pemupukanActionPerformed

    private void btn_back_lahanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_back_lahanActionPerformed
      Home vw = new Home();
      vw.setVisible(true);
      this.dispose();      
    }//GEN-LAST:event_btn_back_lahanActionPerformed

    private void btn_back_tanamanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_back_tanamanActionPerformed
     Home vw = new Home();
      vw.setVisible(true);
      this.dispose();   
    }//GEN-LAST:event_btn_back_tanamanActionPerformed

    private void btn_back_pemupukanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_back_pemupukanActionPerformed
      Home vw = new Home();
      vw.setVisible(true);
      this.dispose();   
    }//GEN-LAST:event_btn_back_pemupukanActionPerformed

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
            java.util.logging.Logger.getLogger(RiwayatFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RiwayatFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RiwayatFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RiwayatFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RiwayatFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_back_lahan;
    private javax.swing.JButton btn_back_pemupukan;
    private javax.swing.JButton btn_back_tanaman;
    private javax.swing.JButton btn_print_lahan;
    private javax.swing.JButton btn_print_pemupukan;
    private javax.swing.JButton btn_print_tanaman;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tblLahan;
    private javax.swing.JTable tblPemupukan;
    private javax.swing.JTable tblTanaman;
    // End of variables declaration//GEN-END:variables
}
