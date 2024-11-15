/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package projectakhir;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
public class Pertanian extends javax.swing.JFrame {
    Connection conn;
    private DefaultTableModel modellahan;
    private DefaultTableModel modeltanaman;
    private DefaultTableModel modelpemupukan;
    
    public Pertanian() {
    initComponents();
    
    conn = koneksi.getConnection();
   
    // Membuat model tabel lahan agar tidak bisa diedit
    modellahan = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Membuat semua sel tidak bisa diedit
        }
    };
    tbl_lahan.setModel(modellahan);
    modellahan.addColumn("ID");
    modellahan.addColumn("JENIS LAHAN");
    modellahan.addColumn("UKURAN TANAH");
    modellahan.addColumn("STATUS");
    
    // Membuat model tabel tanaman agar tidak bisa diedit
    modeltanaman = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Membuat semua sel tidak bisa diedit
        }
    };
    tbl_tanaman.setModel(modeltanaman);
    modeltanaman.addColumn("ID");
    modeltanaman.addColumn("ID LAHAN");
    modeltanaman.addColumn("JENIS TANAMAN");
    modeltanaman.addColumn("MUSIM PANEN");
    
    // Membuat model tabel pemupukan agar tidak bisa diedit
    modelpemupukan = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Membuat semua sel tidak bisa diedit
        }
    };
    tbl_pemupukan.setModel(modelpemupukan);
    modelpemupukan.addColumn("ID");
    modelpemupukan.addColumn("ID TANAMAN");
    modelpemupukan.addColumn("TANGGAL");
    modelpemupukan.addColumn("JENIS PUPUK");
    
    // Memuat data ke tabel
    loadDatalahan();
    loadDatatanaman();
    loadDatapemupukan();
    
    // Memanggil metode ID
    idlahan();
    idtanaman();
}

    
    private void loadDatalahan() {   
    modellahan.setRowCount(0);
    try {
        String sql = "SELECT * FROM lahan";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            // Menambahkan baris ke dalam model tabel
            modellahan.addRow(new Object[]{
                rs.getInt("id"),
                rs.getString("jenis_tanah"),
                rs.getString("ukuran_tanah"),
                rs.getString("status")
            });
        }
        // Ambil ID terakhir yang dihasilkan dan tambahkan 1
        String sqlLastID = "SELECT MAX(id) AS max_id FROM lahan";
        PreparedStatement psLastID = conn.prepareStatement(sqlLastID);
        ResultSet rsLastID = psLastID.executeQuery();
        if (rsLastID.next()) {
            int nextID = rsLastID.getInt("max_id") + 1;
            tf_id_lahan.setText(String.valueOf(nextID));
        } else {
            tf_id_lahan.setText("1"); // Jika tabel kosong, ID dimulai dari 1
        }

    } catch (SQLException e) {
        System.out.println("Error Load Data: " + e.getMessage());
    }
}

 
private void idlahan() {
        try {
            String sql = "SELECT id FROM lahan";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            cb_idlahan_tanaman.removeAllItems(); 
 
            while (rs.next()) {
                cb_idlahan_tanaman.addItem(rs.getString("id"));
 
            }
        } catch (SQLException e) {
            System.out.println("Error loading Worker IDs: " + e.getMessage());
        }
    }  

private void saveDatalahan() {
    try {
        // Validasi apakah semua input telah diisi dan opsi pada ComboBox valid
        if (cb_jenis_tanah.getSelectedItem() == null || 
            tf_ukuran_tanah.getText().trim().isEmpty() || 
            cb_status.getSelectedItem() == null || 
            cb_status.getSelectedItem().toString().equals("--PILIH")) {
            
            // Menampilkan pesan peringatan jika ada field yang kosong atau pilihan tidak valid
            JOptionPane.showMessageDialog(this, "Please fill in all fields and make valid selections before saving.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return; // Menghentikan eksekusi jika validasi gagal
        }

        // Query untuk menyimpan data
        String sql = "INSERT INTO lahan (jenis_tanah, ukuran_tanah, status) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, cb_jenis_tanah.getSelectedItem().toString());
        ps.setString(2, tf_ukuran_tanah.getText().trim()); // Menghilangkan spasi di awal/akhir input
        ps.setString(3, cb_status.getSelectedItem().toString());
        ps.executeUpdate();

        // Menampilkan pesan sukses jika data berhasil disimpan
        JOptionPane.showMessageDialog(this, "Data saved successfully");
        
        // Memuat ulang data di tabel
        loadDatalahan();
        idlahan();
        
    } catch (SQLException e) {
        System.out.println("Error Save Data: " + e.getMessage());
        JOptionPane.showMessageDialog(this, "Error saving data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}


private void updateDatalahan() {
try {
        // Ambil ID dari text field untuk memastikan nilai valid
        int id = Integer.parseInt(tf_id_lahan.getText());

        // Siapkan pernyataan SQL untuk mengubah data
        String sql = "UPDATE lahan SET jenis_tanah = ?, ukuran_tanah = ?, status = ? WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, cb_jenis_tanah.getSelectedItem().toString());
        ps.setString(2, tf_ukuran_tanah.getText());
        ps.setString(3, cb_status.getSelectedItem().toString());
        ps.setInt(4, id);

        // Eksekusi pernyataan update
        int rowsAffected = ps.executeUpdate();

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(this, "Data updated successfully");

            // Perbarui tampilan tabel setelah data diubah
            loadDatalahan();

            // Kosongkan text field setelah pengeditan
            tf_id_lahan.setText("");
            tf_ukuran_tanah.setText("");
            cb_jenis_tanah.setSelectedIndex(0);
            cb_status.setSelectedIndex(0);
        } else {
            JOptionPane.showMessageDialog(this, "No data found with the given ID");
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Invalid ID format. Please select a valid row.");
    } catch (SQLException e) {
        System.out.println("Error updating data: " + e.getMessage());
        JOptionPane.showMessageDialog(this, "Error updating data: " + e.getMessage());
    }
}

private void deleteDatalahan() {
    try {
        // Ambil ID dari text field untuk memastikan nilai valid
        int id = Integer.parseInt(tf_id_lahan.getText());
        
        // Siapkan pernyataan SQL untuk menghapus data
        String sql = "DELETE FROM lahan WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        
        // Eksekusi pernyataan penghapusan
        int rowsAffected = ps.executeUpdate();
        
        if (rowsAffected > 0) {
            // Menormalkan kembali ID di tabel
            conn.prepareStatement("SET @count = 0;").execute();
            conn.prepareStatement("UPDATE lahan SET id = @count := @count + 1;").executeUpdate();
            conn.prepareStatement("ALTER TABLE lahan AUTO_INCREMENT = 1;").execute();
            
            // Tampilkan pesan sukses
            JOptionPane.showMessageDialog(this, "Data deleted successfully");
            
            // Perbarui tampilan tabel setelah data dihapus
            loadDatalahan();
            
            // Kosongkan text field ID setelah penghapusan
            tf_id_lahan.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "No data found with the given ID");
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Invalid ID format. Please select a valid row.");
    } catch (SQLException e) {
        System.out.println("Error deleting data: " + e.getMessage());
        JOptionPane.showMessageDialog(this, "Error deleting data: " + e.getMessage());
    }
}

private void loadDatatanaman() {
    modeltanaman.setRowCount(0);

    try {
        String sql = "SELECT * FROM tanaman";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            // Menambahkan baris ke dalam model tabel
            modeltanaman.addRow(new Object[]{
                rs.getInt("id"),
                rs.getString("id_lahan"),
                rs.getString("jenis_tanaman"),
                rs.getString("musim_panen")
            });
        }

        // Ambil ID terakhir yang dihasilkan dan tambahkan 1
        String sqlLastID = "SELECT MAX(id) AS max_id FROM tanaman";
        PreparedStatement psLastID = conn.prepareStatement(sqlLastID);
        ResultSet rsLastID = psLastID.executeQuery();
        if (rsLastID.next()) {
            int nextID = rsLastID.getInt("max_id") + 1;
            tf_id_tanaman.setText(String.valueOf(nextID));
        } else {
            tf_id_tanaman.setText("1"); // Jika tabel kosong, ID dimulai dari 1
        }

    } catch (SQLException e) {
        System.out.println("Error Load Data: " + e.getMessage());
    }
}


private void idtanaman() {
        try {
            String sql = "SELECT id FROM tanaman";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            cb_idtanaman_pmk.removeAllItems(); 
 
            while (rs.next()) {
                cb_idtanaman_pmk.addItem(rs.getString("id"));
 
            }
        } catch (SQLException e) {
            System.out.println("Error loading Worker IDs: " + e.getMessage());
        }
    }

private void saveDatatanaman() {
    try {
        // Validasi apakah semua input telah diisi dan opsi pada ComboBox valid
        if (cb_idlahan_tanaman.getSelectedItem() == null || 
            tf_jenis_tanaman.getText().trim().isEmpty() || 
            cb_musim_panen.getSelectedItem() == null || 
            cb_musim_panen.getSelectedItem().toString().equals("--PILIH")) {
            
            // Menampilkan pesan peringatan jika ada field yang kosong atau pilihan tidak valid
            JOptionPane.showMessageDialog(this, "Please fill in all fields and make valid selections before saving.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return; // Menghentikan eksekusi jika validasi gagal
        }

        // Query untuk menyimpan data
        String sql = "INSERT INTO tanaman (id_lahan, jenis_tanaman, musim_panen) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, cb_idlahan_tanaman.getSelectedItem().toString());
        ps.setString(2, tf_jenis_tanaman.getText().trim()); // Menghilangkan spasi di awal/akhir input
        ps.setString(3, cb_musim_panen.getSelectedItem().toString());
        ps.executeUpdate();

        // Menampilkan pesan sukses jika data berhasil disimpan
        JOptionPane.showMessageDialog(this, "Data saved successfully");
        
        // Memuat ulang data di tabel
        loadDatatanaman();
        idtanaman();

    } catch (SQLException e) {
        System.out.println("Error Save Data: " + e.getMessage());
        JOptionPane.showMessageDialog(this, "Error saving data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void updateDatatanaman() {
    try {
        // Validasi input
        if (tf_id_tanaman.getText().isEmpty() || cb_idlahan_tanaman.getSelectedItem() == null || cb_musim_panen.getSelectedItem() == null || tf_jenis_tanaman.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Pastikan ID valid dan periksa apakah data ada
        String sqlCheck = "SELECT * FROM tanaman WHERE id = ?";
        PreparedStatement psCheck = conn.prepareStatement(sqlCheck);
        psCheck.setInt(1, Integer.parseInt(tf_id_tanaman.getText()));
        ResultSet rs = psCheck.executeQuery();

        if (!rs.next()) {
            JOptionPane.showMessageDialog(this, "No data found with the given ID", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Siapkan pernyataan UPDATE
        String sqlUpdate = "UPDATE tanaman SET id_lahan = ?, jenis_tanaman = ?, musim_panen = ? WHERE id = ?";
        PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
        psUpdate.setString(1, cb_idlahan_tanaman.getSelectedItem().toString());
        psUpdate.setString(2, tf_jenis_tanaman.getText());
        psUpdate.setString(3, cb_musim_panen.getSelectedItem().toString());
        psUpdate.setInt(4, Integer.parseInt(tf_id_tanaman.getText()));

        int rowsAffected = psUpdate.executeUpdate();

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(this, "Data updated successfully");
            loadDatatanaman(); // Perbarui tampilan tabel
            resetInputFieldsTanaman(); // Kosongkan input field setelah update
        } else {
            JOptionPane.showMessageDialog(this, "Update failed", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException e) {
        System.out.println("Error updating tanaman data: " + e.getMessage());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Invalid ID format. Please check your input.", "Input Error", JOptionPane.WARNING_MESSAGE);
    }
}

private void deleteDatatanaman() {
    try {
        // Validasi input ID
        if (tf_id_tanaman.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a record to delete", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Siapkan pernyataan DELETE
        String sql = "DELETE FROM tanaman WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, Integer.parseInt(tf_id_tanaman.getText()));

        int rowsAffected = ps.executeUpdate();

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(this, "Data deleted successfully");
            conn.prepareStatement("SET @count = 0;").execute();
            conn.prepareStatement("UPDATE tanaman SET id = @count := @count + 1;").executeUpdate();
            conn.prepareStatement("ALTER TABLE tanaman AUTO_INCREMENT = 1;").execute();
            loadDatatanaman(); // Perbarui tampilan tabel
            resetInputFieldsTanaman(); // Kosongkan input field setelah delete
        } else {
            JOptionPane.showMessageDialog(this, "No data found with the specified ID", "Error", JOptionPane.WARNING_MESSAGE);
        }
    } catch (SQLException e) {
        System.out.println("Error deleting tanaman data: " + e.getMessage());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Invalid ID format. Please select a valid record.", "Input Error", JOptionPane.WARNING_MESSAGE);
    }
}
private void resetInputFieldsTanaman() {
    tf_id_tanaman.setText("");
    cb_idlahan_tanaman.setSelectedIndex(0);
    tf_jenis_tanaman.setText("");
    cb_musim_panen.setSelectedIndex(0);
}

private void loadDatapemupukan() {
    modelpemupukan.setRowCount(0);

    try {
        String sql = "SELECT * FROM pemupukan";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            // Menambahkan baris ke dalam model tabel
            modelpemupukan.addRow(new Object[]{
                rs.getInt("id"),
                rs.getString("id_tanaman"),
                rs.getString("tanggal"),
                rs.getString("jenis_pupuk")
            });
        }

        // Ambil ID terakhir yang dihasilkan dan tambahkan 1
        String sqlLastID = "SELECT MAX(id) AS max_id FROM pemupukan";
        PreparedStatement psLastID = conn.prepareStatement(sqlLastID);
        ResultSet rsLastID = psLastID.executeQuery();
        if (rsLastID.next()) {
            int nextID = rsLastID.getInt("max_id") + 1;
            tf_id_pemupukan.setText(String.valueOf(nextID));
        } else {
            tf_id_pemupukan.setText("1"); // Jika tabel kosong, ID dimulai dari 1
        }

    } catch (SQLException e) {
        System.out.println("Error Load Data: " + e.getMessage());
    }
}

private void saveDatapemupukan() {
    try {
        // Validasi apakah semua input telah diisi dan opsi pada ComboBox valid
        if (cb_idtanaman_pmk.getSelectedItem() == null || 
            date_tgl.getDate() == null || 
            cb_jenis_pupuk.getSelectedItem() == null || 
            cb_jenis_pupuk.getSelectedItem().toString().equals("--PILIH")) {

            // Menampilkan pesan peringatan jika ada field yang kosong atau pilihan tidak valid
            JOptionPane.showMessageDialog(this, "Please fill in all fields and make valid selections before saving.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return; // Menghentikan eksekusi jika validasi gagal
        }

        // Format tanggal
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tanggal = sdf.format(date_tgl.getDate());

        // Query untuk menyimpan data
        String sql = "INSERT INTO pemupukan (id_tanaman, tanggal, jenis_pupuk) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, cb_idtanaman_pmk.getSelectedItem().toString());
        ps.setString(2, tanggal);
        ps.setString(3, cb_jenis_pupuk.getSelectedItem().toString()); // Menggunakan cb_jenis_pupuk, bukan cb_musim_panen
        ps.executeUpdate();

        // Menampilkan pesan sukses jika data berhasil disimpan
        JOptionPane.showMessageDialog(this, "Data saved successfully");
        
        // Memuat ulang data di tabel
        loadDatapemupukan();
        idtanaman();

    } catch (SQLException e) {
        System.out.println("Error Save Data: " + e.getMessage());
        JOptionPane.showMessageDialog(this, "Error saving data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    } catch (NullPointerException e) {
        JOptionPane.showMessageDialog(this, "Invalid date format or other null values. Please check your inputs.", "Input Error", JOptionPane.WARNING_MESSAGE);
    }
}

private void updateDatapemupukan() {
    try {
        // Validasi input
        if (tf_id_pemupukan.getText().isEmpty() || cb_idtanaman_pmk.getSelectedItem() == null || cb_jenis_pupuk.getSelectedItem() == null || date_tgl.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Pastikan ID valid dan periksa apakah data ada
        String sqlCheck = "SELECT * FROM pemupukan WHERE id = ?";
        PreparedStatement psCheck = conn.prepareStatement(sqlCheck);
        psCheck.setInt(1, Integer.parseInt(tf_id_pemupukan.getText()));
        ResultSet rs = psCheck.executeQuery();

        if (!rs.next()) {
            JOptionPane.showMessageDialog(this, "No data found with the given ID", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Format tanggal untuk SQL
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tanggal = sdf.format(date_tgl.getDate());

        // Siapkan pernyataan UPDATE
        String sqlUpdate = "UPDATE pemupukan SET id_tanaman = ?, tanggal = ?, jenis_pupuk = ? WHERE id = ?";
        PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
        psUpdate.setString(1, cb_idtanaman_pmk.getSelectedItem().toString());
        psUpdate.setString(2, tanggal);
        psUpdate.setString(3, cb_jenis_pupuk.getSelectedItem().toString());
        psUpdate.setInt(4, Integer.parseInt(tf_id_pemupukan.getText()));

        int rowsAffected = psUpdate.executeUpdate();

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(this, "Pemupukan data updated successfully");
            loadDatapemupukan(); // Perbarui tampilan tabel
            resetInputFieldsPemupukan(); // Kosongkan input field setelah update
        } else {
            JOptionPane.showMessageDialog(this, "Update failed", "Error", JOptionPane.ERROR_MESSAGE);
        }

    } catch (SQLException e) {
        System.out.println("Error updating pemupukan data: " + e.getMessage());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Invalid ID format. Please check your input.", "Input Error", JOptionPane.WARNING_MESSAGE);
    }
}

private void deleteDatapemupukan() {
    try {
        // Validasi input ID
        if (tf_id_pemupukan.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a record to delete", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Siapkan pernyataan DELETE
        String sql = "DELETE FROM pemupukan WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, Integer.parseInt(tf_id_pemupukan.getText()));

        int rowsAffected = ps.executeUpdate();

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(this, "Pemupukan data deleted successfully");
            conn.prepareStatement("SET @count = 0;").execute();
            conn.prepareStatement("UPDATE pemupukan SET id = @count := @count + 1;").executeUpdate();
            conn.prepareStatement("ALTER TABLE pemupukan AUTO_INCREMENT = 1;").execute();
            loadDatapemupukan(); // Perbarui tampilan tabel
            resetInputFieldsPemupukan(); // Kosongkan input field setelah delete
        } else {
            JOptionPane.showMessageDialog(this, "No data found with the specified ID", "Error", JOptionPane.WARNING_MESSAGE);
        }
    } catch (SQLException e) {
        System.out.println("Error deleting pemupukan data: " + e.getMessage());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Invalid ID format. Please select a valid record.", "Input Error", JOptionPane.WARNING_MESSAGE);
    }
}

private void resetInputFieldsPemupukan() {
    tf_id_pemupukan.setText("");
    if (cb_idtanaman_pmk.getItemCount() > 0) {
        cb_idtanaman_pmk.setSelectedIndex(0);
    }
    date_tgl.setDate(null);
    if (cb_jenis_pupuk.getItemCount() > 0) {
        cb_jenis_pupuk.setSelectedIndex(0);
    }
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        tf_id_lahan = new javax.swing.JTextField();
        cb_jenis_tanah = new javax.swing.JComboBox<>();
        tf_ukuran_tanah = new javax.swing.JTextField();
        cb_status = new javax.swing.JComboBox<>();
        btn_update_lahan = new javax.swing.JButton();
        btn_tambah_lahan = new javax.swing.JButton();
        btn_delete_lahan = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_lahan = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        btn_reset_lahan = new javax.swing.JButton();
        btn_exit_lahan = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_tanaman = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        cb_idlahan_tanaman = new javax.swing.JComboBox<>();
        tf_id_tanaman = new javax.swing.JTextField();
        tf_jenis_tanaman = new javax.swing.JTextField();
        cb_musim_panen = new javax.swing.JComboBox<>();
        btn_edit_tanaman = new javax.swing.JButton();
        btn_tambah_tanaman = new javax.swing.JButton();
        btn_delete_tanaman = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        btn_reset_tanaman = new javax.swing.JButton();
        btn_exit_tanaman = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        tf_id_pemupukan = new javax.swing.JTextField();
        cb_idtanaman_pmk = new javax.swing.JComboBox<>();
        cb_jenis_pupuk = new javax.swing.JComboBox<>();
        btn_update_pemupukan = new javax.swing.JButton();
        btn_tambah_pemupukan = new javax.swing.JButton();
        btn_delete_pemupukan = new javax.swing.JButton();
        date_tgl = new com.toedter.calendar.JDateChooser();
        jPanel13 = new javax.swing.JPanel();
        btn_reset_pemupukan = new javax.swing.JButton();
        btn_exit_pemupukan = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbl_pemupukan = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPane1.setBackground(new java.awt.Color(102, 153, 0));

        jPanel5.setBackground(new java.awt.Color(51, 102, 0));

        jPanel7.setBackground(new java.awt.Color(153, 255, 153));

        jLabel2.setFont(new java.awt.Font("Schadow BT", 3, 24)); // NOI18N
        jLabel2.setText("LAHAN");

        jButton1.setBackground(new java.awt.Color(153, 255, 153));
        jButton1.setIcon(new javax.swing.ImageIcon("C:\\Users\\ASUS\\Pictures\\img\\backper.png")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 207, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(256, 256, 256))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(16, 16, 16))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jButton1)
                .addGap(0, 11, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new java.awt.Color(153, 255, 153));

        jLabel6.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N
        jLabel6.setText("ID  :");

        jLabel7.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N
        jLabel7.setText("JENIS TANAH :");

        jLabel8.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N
        jLabel8.setText("UKURAN TANAH :");

        jLabel9.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N
        jLabel9.setText("STATUS :");

        tf_id_lahan.setEditable(false);
        tf_id_lahan.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N
        tf_id_lahan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_id_lahanActionPerformed(evt);
            }
        });

        cb_jenis_tanah.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N
        cb_jenis_tanah.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--PILIH", "LEMPUNG", "LIAT", "PASIR", "HUMUS" }));

        tf_ukuran_tanah.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N

        cb_status.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N
        cb_status.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--PILIH", "AKTIF", "NONAKTIF" }));

        btn_update_lahan.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N
        btn_update_lahan.setText("EDIT");
        btn_update_lahan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_update_lahanActionPerformed(evt);
            }
        });

        btn_tambah_lahan.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N
        btn_tambah_lahan.setText("TAMBAH");
        btn_tambah_lahan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tambah_lahanActionPerformed(evt);
            }
        });

        btn_delete_lahan.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N
        btn_delete_lahan.setText("DELETE");
        btn_delete_lahan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_delete_lahanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(cb_jenis_tanah, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(tf_id_lahan, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(btn_update_lahan, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btn_tambah_lahan, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(61, 61, 61)
                                .addComponent(btn_delete_lahan)
                                .addGap(11, 11, 11))
                            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(tf_ukuran_tanah)
                                .addComponent(cb_status, 0, 388, Short.MAX_VALUE)))))
                .addGap(0, 49, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(tf_id_lahan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(cb_jenis_tanah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(tf_ukuran_tanah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(cb_status, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_tambah_lahan)
                    .addComponent(btn_update_lahan)
                    .addComponent(btn_delete_lahan))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        tbl_lahan.setBackground(new java.awt.Color(153, 255, 153));
        tbl_lahan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID ", "JENIS TANAH", "UKURAN TAHAN", "STATUS"
            }
        ));
        tbl_lahan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_lahanMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_lahan);

        jPanel11.setLayout(new java.awt.GridLayout(1, 0));

        btn_reset_lahan.setBackground(new java.awt.Color(204, 255, 204));
        btn_reset_lahan.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N
        btn_reset_lahan.setText("RESET");
        btn_reset_lahan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_reset_lahanActionPerformed(evt);
            }
        });
        jPanel11.add(btn_reset_lahan);

        btn_exit_lahan.setBackground(new java.awt.Color(204, 255, 204));
        btn_exit_lahan.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N
        btn_exit_lahan.setText("EXIT");
        btn_exit_lahan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_exit_lahanActionPerformed(evt);
            }
        });
        jPanel11.add(btn_exit_lahan);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(35, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 504, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(54, 54, 54))))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("LAHAN", jPanel1);

        jPanel2.setBackground(new java.awt.Color(51, 102, 0));

        jPanel8.setBackground(new java.awt.Color(153, 255, 153));

        jLabel3.setFont(new java.awt.Font("Schadow BT", 3, 24)); // NOI18N
        jLabel3.setText("TANAMAN");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(233, 233, 233))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel3)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        tbl_tanaman.setBackground(new java.awt.Color(153, 255, 153));
        tbl_tanaman.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID TANAMAN", "ID LAHAN", "JENIS TANAMAN", "MUSIM PANEN"
            }
        ));
        tbl_tanaman.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_tanamanMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbl_tanaman);

        jPanel10.setBackground(new java.awt.Color(153, 255, 153));

        jLabel10.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N
        jLabel10.setText("ID TANAMAN :");

        jLabel11.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N
        jLabel11.setText("ID LAHAN :");

        jLabel12.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N
        jLabel12.setText("JENIS TANAMAN :");

        jLabel13.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N
        jLabel13.setText("MUSIM PANEN :");

        cb_idlahan_tanaman.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N

        tf_id_tanaman.setEditable(false);
        tf_id_tanaman.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N

        tf_jenis_tanaman.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N

        cb_musim_panen.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N
        cb_musim_panen.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--PILIH", "KEMARAU", "HUJAN" }));

        btn_edit_tanaman.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N
        btn_edit_tanaman.setText("EDIT");
        btn_edit_tanaman.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_edit_tanamanActionPerformed(evt);
            }
        });

        btn_tambah_tanaman.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N
        btn_tambah_tanaman.setText("TAMBAH");
        btn_tambah_tanaman.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tambah_tanamanActionPerformed(evt);
            }
        });

        btn_delete_tanaman.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N
        btn_delete_tanaman.setText("DELETE");
        btn_delete_tanaman.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_delete_tanamanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addGap(18, 18, 18)
                                .addComponent(cb_musim_panen, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tf_jenis_tanaman))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel10))
                                .addGap(29, 29, 29)
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(tf_id_tanaman, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cb_idlahan_tanaman, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 6, Short.MAX_VALUE))))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(116, 116, 116)
                        .addComponent(btn_edit_tanaman)
                        .addGap(45, 45, 45)
                        .addComponent(btn_tambah_tanaman)
                        .addGap(44, 44, 44)
                        .addComponent(btn_delete_tanaman)))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(tf_id_tanaman, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(cb_idlahan_tanaman, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(tf_jenis_tanaman, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(cb_musim_panen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_edit_tanaman)
                    .addComponent(btn_tambah_tanaman)
                    .addComponent(btn_delete_tanaman))
                .addGap(21, 21, 21))
        );

        jPanel12.setLayout(new java.awt.GridLayout(1, 0));

        jPanel15.setBackground(new java.awt.Color(153, 255, 153));
        jPanel15.setLayout(new java.awt.GridLayout(1, 0));

        btn_reset_tanaman.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N
        btn_reset_tanaman.setText("RESET");
        btn_reset_tanaman.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_reset_tanamanActionPerformed(evt);
            }
        });
        jPanel15.add(btn_reset_tanaman);

        btn_exit_tanaman.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N
        btn_exit_tanaman.setText("EXIT");
        btn_exit_tanaman.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_exit_tanamanActionPerformed(evt);
            }
        });
        jPanel15.add(btn_exit_tanaman);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap(52, Short.MAX_VALUE)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(29, 29, 29))
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(46, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("TANAMAN", jPanel2);

        jPanel4.setBackground(new java.awt.Color(51, 102, 0));

        jPanel6.setBackground(new java.awt.Color(153, 255, 153));

        jLabel1.setFont(new java.awt.Font("Schadow BT", 3, 18)); // NOI18N
        jLabel1.setText("PEMUPUKAN");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(246, 246, 246))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(14, 14, 14))
        );

        jPanel16.setBackground(new java.awt.Color(153, 255, 153));

        jLabel4.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N
        jLabel4.setText("ID_PEMUPUKAN :");

        jLabel5.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N
        jLabel5.setText("ID TANAMAN :");

        jLabel14.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N
        jLabel14.setText("TANGGAL PEMUPUKAN :");

        jLabel15.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N
        jLabel15.setText("JENIS PUPUK :");

        tf_id_pemupukan.setEditable(false);
        tf_id_pemupukan.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N

        cb_idtanaman_pmk.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N

        cb_jenis_pupuk.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N
        cb_jenis_pupuk.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--PILIH", "organik", "anorganik" }));
        cb_jenis_pupuk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_jenis_pupukActionPerformed(evt);
            }
        });

        btn_update_pemupukan.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N
        btn_update_pemupukan.setText("EDIT");
        btn_update_pemupukan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_update_pemupukanActionPerformed(evt);
            }
        });

        btn_tambah_pemupukan.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N
        btn_tambah_pemupukan.setText("TAMBAH");
        btn_tambah_pemupukan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tambah_pemupukanActionPerformed(evt);
            }
        });

        btn_delete_pemupukan.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N
        btn_delete_pemupukan.setText("DELETE");
        btn_delete_pemupukan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_delete_pemupukanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tf_id_pemupukan)
                            .addComponent(cb_idtanaman_pmk, 0, 311, Short.MAX_VALUE)))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15))
                        .addGap(22, 22, 22)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(btn_update_pemupukan)
                                .addGap(37, 37, 37)
                                .addComponent(btn_tambah_pemupukan)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                                .addComponent(btn_delete_pemupukan))
                            .addComponent(cb_jenis_pupuk, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(date_tgl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(92, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(tf_id_pemupukan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cb_idtanaman_pmk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel14)
                    .addComponent(date_tgl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(cb_jenis_pupuk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_tambah_pemupukan)
                    .addComponent(btn_delete_pemupukan)
                    .addComponent(btn_update_pemupukan))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jPanel13.setBackground(new java.awt.Color(153, 255, 153));
        jPanel13.setLayout(new java.awt.GridLayout(1, 0));

        btn_reset_pemupukan.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N
        btn_reset_pemupukan.setText("RESET");
        btn_reset_pemupukan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_reset_pemupukanActionPerformed(evt);
            }
        });
        jPanel13.add(btn_reset_pemupukan);

        btn_exit_pemupukan.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N
        btn_exit_pemupukan.setText("EXIT");
        jPanel13.add(btn_exit_pemupukan);

        tbl_pemupukan.setBackground(new java.awt.Color(153, 255, 153));
        tbl_pemupukan.setFont(new java.awt.Font("Schadow BT", 3, 12)); // NOI18N
        tbl_pemupukan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID PEMUPUKAN", "ID TANAMAN", "TANGGAL", "JENIS PUPUK"
            }
        ));
        tbl_pemupukan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_pemupukanMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tbl_pemupukan);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
            .addComponent(jPanel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 557, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(67, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("PEMUPUKAN", jPanel3);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tf_id_lahanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_id_lahanActionPerformed
       
    }//GEN-LAST:event_tf_id_lahanActionPerformed

    private void btn_tambah_lahanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambah_lahanActionPerformed
        saveDatalahan();
      cb_jenis_tanah.setSelectedIndex(0);
      tf_ukuran_tanah.setText(""); // Mengatur ke item pertama pada ComboBox jabatan
      cb_status.setSelectedIndex(0);
    }//GEN-LAST:event_btn_tambah_lahanActionPerformed

    private void btn_update_pemupukanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_update_pemupukanActionPerformed
        updateDatapemupukan();
        if (cb_idtanaman_pmk.getItemCount() > 0) {
        cb_idtanaman_pmk.setSelectedIndex(0);
        }
        if (cb_jenis_pupuk.getItemCount() > 0) {
        cb_jenis_pupuk.setSelectedIndex(0);
        }
    date_tgl.setDate(null);
    }//GEN-LAST:event_btn_update_pemupukanActionPerformed

    private void btn_tambah_pemupukanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambah_pemupukanActionPerformed
    saveDatapemupukan();
     if (cb_idtanaman_pmk.getItemCount() > 0) {
        cb_idtanaman_pmk.setSelectedIndex(0);
    }
    if (cb_jenis_pupuk.getItemCount() > 0) {
        cb_jenis_pupuk.setSelectedIndex(0);
    }
    date_tgl.setDate(null);   
    }//GEN-LAST:event_btn_tambah_pemupukanActionPerformed

    private void btn_update_lahanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_update_lahanActionPerformed
        updateDatalahan();
        cb_jenis_tanah.setSelectedIndex(0);
        tf_ukuran_tanah.setText(""); // Mengatur ke item pertama pada ComboBox jabatan
        cb_status.setSelectedIndex(0);
    }//GEN-LAST:event_btn_update_lahanActionPerformed

    private void btn_delete_lahanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_delete_lahanActionPerformed
        deleteDatalahan();
        cb_jenis_tanah.setSelectedIndex(0);
        tf_ukuran_tanah.setText(""); // Mengatur ke item pertama pada ComboBox jabatan
        cb_status.setSelectedIndex(0);
    }//GEN-LAST:event_btn_delete_lahanActionPerformed

    private void btn_tambah_tanamanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambah_tanamanActionPerformed
       saveDatatanaman();
       cb_idlahan_tanaman.setSelectedIndex(0);
       tf_jenis_tanaman.setText(""); // Mengatur ke item pertama pada ComboBox jabatan
       cb_musim_panen.setSelectedIndex(0);
    }//GEN-LAST:event_btn_tambah_tanamanActionPerformed

    private void btn_edit_tanamanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_edit_tanamanActionPerformed
        updateDatatanaman();
        cb_idlahan_tanaman.setSelectedIndex(0);
        tf_jenis_tanaman.setText(""); // Mengatur ke item pertama pada ComboBox jabatan
        cb_musim_panen.setSelectedIndex(0);
    }//GEN-LAST:event_btn_edit_tanamanActionPerformed

    private void btn_delete_tanamanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_delete_tanamanActionPerformed
        deleteDatatanaman();
        cb_idlahan_tanaman.setSelectedIndex(0);
        tf_jenis_tanaman.setText(""); // Mengatur ke item pertama pada ComboBox jabatan
        cb_musim_panen.setSelectedIndex(0);
    }//GEN-LAST:event_btn_delete_tanamanActionPerformed

    private void btn_delete_pemupukanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_delete_pemupukanActionPerformed
    deleteDatapemupukan();
        if (cb_idtanaman_pmk.getItemCount() > 0) {
        cb_idtanaman_pmk.setSelectedIndex(0);
        }
        if (cb_jenis_pupuk.getItemCount() > 0) {
            cb_jenis_pupuk.setSelectedIndex(0);
        }

        // Mengosongkan Date Picker
        date_tgl.setDate(null);
    }//GEN-LAST:event_btn_delete_pemupukanActionPerformed

    private void btn_reset_pemupukanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_reset_pemupukanActionPerformed
        if (cb_idtanaman_pmk.getItemCount() > 0) {
        cb_idtanaman_pmk.setSelectedIndex(0);
        }
        if (cb_jenis_pupuk.getItemCount() > 0) {
            cb_jenis_pupuk.setSelectedIndex(0);
        }
        // Mengosongkan Date Picker
        date_tgl.setDate(null);
    }//GEN-LAST:event_btn_reset_pemupukanActionPerformed

    private void btn_reset_lahanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_reset_lahanActionPerformed

      cb_jenis_tanah.setSelectedIndex(0);
      tf_ukuran_tanah.setText(""); // Mengatur ke item pertama pada ComboBox jabatan
      cb_status.setSelectedIndex(0);
    }//GEN-LAST:event_btn_reset_lahanActionPerformed

    private void btn_exit_tanamanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_exit_tanamanActionPerformed
       System.exit(0);
    }//GEN-LAST:event_btn_exit_tanamanActionPerformed

    private void btn_exit_lahanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_exit_lahanActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btn_exit_lahanActionPerformed

    private void btn_reset_tanamanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_reset_tanamanActionPerformed
      tf_jenis_tanaman.setText(""); // Mengatur ke item pertama pada ComboBox jabatan
      cb_musim_panen.setSelectedIndex(0);
    }//GEN-LAST:event_btn_reset_tanamanActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
      Home hm = new Home();
      hm.setVisible(true);
      hm.pack();
      hm.setLocationRelativeTo(null);
      hm.setDefaultCloseOperation(Home.EXIT_ON_CLOSE);
      dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tbl_lahanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_lahanMouseClicked
       
        
        int selectedRow = tbl_lahan.getSelectedRow(); // Mendapatkan baris yang diklik
        if (selectedRow != -1) {
            // Mengisi semua field input dengan data dari baris yang dipilih
            String id = modellahan.getValueAt(selectedRow, 0).toString();
            String jenisLahan = modellahan.getValueAt(selectedRow, 1).toString();
            String ukuranTanah = modellahan.getValueAt(selectedRow, 2).toString();
            String status = modellahan.getValueAt(selectedRow, 3).toString();

            // Mengisi field input
            tf_id_lahan.setText(id);
            cb_jenis_tanah.setSelectedItem(jenisLahan);
            tf_ukuran_tanah.setText(ukuranTanah);
            cb_status.setSelectedItem(status);
        
    }
    }//GEN-LAST:event_tbl_lahanMouseClicked

    private void tbl_tanamanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_tanamanMouseClicked
        int selectedRow = tbl_tanaman.getSelectedRow(); // Mendapatkan baris yang diklik
        if (selectedRow != -1) {
            // Mengisi field input dengan data dari baris yang dipilih
            tf_id_tanaman.setText(modeltanaman.getValueAt(selectedRow, 0).toString());
            cb_idlahan_tanaman.setSelectedItem(modeltanaman.getValueAt(selectedRow, 1).toString());
            tf_jenis_tanaman.setText(modeltanaman.getValueAt(selectedRow, 2).toString());
            cb_musim_panen.setSelectedItem(modeltanaman.getValueAt(selectedRow, 3).toString());
        }
    }//GEN-LAST:event_tbl_tanamanMouseClicked

    private void tbl_pemupukanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_pemupukanMouseClicked

     int selectedRow = tbl_pemupukan.getSelectedRow(); // Mendapatkan baris yang diklik
        if (selectedRow != -1) {
            // Mengisi field input dengan data dari baris yang dipilih
            tf_id_pemupukan.setText(modelpemupukan.getValueAt(selectedRow, 0).toString());
            cb_idtanaman_pmk.setSelectedItem(modelpemupukan.getValueAt(selectedRow, 1).toString());
            cb_jenis_pupuk.setSelectedItem(modelpemupukan.getValueAt(selectedRow, 3).toString());
            date_tgl.setDate(java.sql.Date.valueOf(modelpemupukan.getValueAt(selectedRow, 2).toString()));
        
    }

    }//GEN-LAST:event_tbl_pemupukanMouseClicked

    private void cb_jenis_pupukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_jenis_pupukActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cb_jenis_pupukActionPerformed

    public static void main(String args[]) {
 
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Pertanian().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_delete_lahan;
    private javax.swing.JButton btn_delete_pemupukan;
    private javax.swing.JButton btn_delete_tanaman;
    private javax.swing.JButton btn_edit_tanaman;
    private javax.swing.JButton btn_exit_lahan;
    private javax.swing.JButton btn_exit_pemupukan;
    private javax.swing.JButton btn_exit_tanaman;
    private javax.swing.JButton btn_reset_lahan;
    private javax.swing.JButton btn_reset_pemupukan;
    private javax.swing.JButton btn_reset_tanaman;
    private javax.swing.JButton btn_tambah_lahan;
    private javax.swing.JButton btn_tambah_pemupukan;
    private javax.swing.JButton btn_tambah_tanaman;
    private javax.swing.JButton btn_update_lahan;
    private javax.swing.JButton btn_update_pemupukan;
    private javax.swing.JComboBox<String> cb_idlahan_tanaman;
    private javax.swing.JComboBox<String> cb_idtanaman_pmk;
    private javax.swing.JComboBox<String> cb_jenis_pupuk;
    private javax.swing.JComboBox<String> cb_jenis_tanah;
    private javax.swing.JComboBox<String> cb_musim_panen;
    private javax.swing.JComboBox<String> cb_status;
    private com.toedter.calendar.JDateChooser date_tgl;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tbl_lahan;
    private javax.swing.JTable tbl_pemupukan;
    private javax.swing.JTable tbl_tanaman;
    private javax.swing.JTextField tf_id_lahan;
    private javax.swing.JTextField tf_id_pemupukan;
    private javax.swing.JTextField tf_id_tanaman;
    private javax.swing.JTextField tf_jenis_tanaman;
    private javax.swing.JTextField tf_ukuran_tanah;
    // End of variables declaration//GEN-END:variables
}
