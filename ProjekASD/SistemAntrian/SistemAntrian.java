import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.*;

// Data Pelanggan
class Pelanggan {
    private String nama;
    private LocalDateTime waktuKedatangan;
    private int prioritas; // Prioritas otomatis

    public Pelanggan(String nama, LocalDateTime waktuKedatangan) {
        this.nama = nama;
        this.waktuKedatangan = waktuKedatangan;
        this.prioritas = 0; // Default 0, akan diberikan setelah sorting
    }

    public String getNama() {
        return nama;
    }

    public LocalDateTime getWaktuKedatangan() {
        return waktuKedatangan;
    }

    public int getPrioritas() {
        return prioritas;
    }

    public void setPrioritas(int prioritas) {
        this.prioritas = prioritas;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy HH:mm");
        return "Nama: " + nama + ", Waktu: " + waktuKedatangan.format(formatter) + ", Prioritas: " + prioritas;
    }
}

// Kelas utama menggunakan GUI
public class SistemAntrian extends JFrame {
    private JTextField fieldNama, fieldTanggal, fieldWaktu, fieldCari;
    private JButton tombolTambah, tombolTampilkan, tombolSortir, tombolProses, tombolCari;
    private JTextArea displayArea;
    private ArrayList<Pelanggan> antrian;

    public SistemAntrian() {
        antrian = new ArrayList<>();

        // Konfigurasi JFrame
        setTitle("Aplikasi Sistem Antrian Pelanggan");
        setSize(600, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        // Komponen input
        JLabel labelNama = new JLabel("Nama:");
        labelNama.setBounds(10, 10, 100, 25);
        add(labelNama);

        fieldNama = new JTextField();
        fieldNama.setBounds(120, 10, 200, 25);
        add(fieldNama);

        JLabel labelTanggal = new JLabel("Tanggal (dd-MM-yyyy):");
        labelTanggal.setBounds(10, 40, 200, 25);
        add(labelTanggal);

        fieldTanggal = new JTextField();
        fieldTanggal.setBounds(200, 40, 150, 25);
        add(fieldTanggal);

        JLabel labelWaktu = new JLabel("Waktu (HH:mm):");
        labelWaktu.setBounds(10, 70, 200, 25);
        add(labelWaktu);

        fieldWaktu = new JTextField();
        fieldWaktu.setBounds(200, 70, 150, 25);
        add(fieldWaktu);

        JLabel labelCari = new JLabel("Cari Nama:");
        labelCari.setBounds(10, 100, 200, 25);
        add(labelCari);

        fieldCari = new JTextField();
        fieldCari.setBounds(120, 100, 200, 25);
        add(fieldCari);

        // Tombol
        tombolTambah = new JButton("Tambah");
        tombolTambah.setBounds(10, 130, 100, 25);
        add(tombolTambah);

        tombolTampilkan = new JButton("Tampilkan");
        tombolTampilkan.setBounds(120, 130, 100, 25);
        add(tombolTampilkan);

        tombolSortir = new JButton("Sortir");
        tombolSortir.setBounds(230, 130, 100, 25);
        add(tombolSortir);

        tombolProses = new JButton("Proses");
        tombolProses.setBounds(340, 130, 100, 25);
        add(tombolProses);

        tombolCari = new JButton("Cari");
        tombolCari.setBounds(450, 130, 100, 25);
        add(tombolCari);

        // Tampilan
        displayArea = new JTextArea();
        displayArea.setBounds(10, 170, 550, 320);
        displayArea.setEditable(false);
        add(displayArea);

        // Event untuk tombol
        tombolTambah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nama = fieldNama.getText();
                String tanggalInput = fieldTanggal.getText();
                String waktuInput = fieldWaktu.getText();

                try {
                    // Parsing tanggal dan waktu
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                    LocalDate tanggal = LocalDate.parse(tanggalInput, dateFormatter);
                    LocalTime waktu = LocalTime.parse(waktuInput, timeFormatter);
                    LocalDateTime waktuKedatangan = LocalDateTime.of(tanggal, waktu);

                    Pelanggan pelanggan = new Pelanggan(nama, waktuKedatangan);
                    antrian.add(pelanggan);

                    // Mengosongkan input setelah ditambahkan
                    fieldNama.setText("");
                    fieldTanggal.setText("");
                    fieldWaktu.setText("");

                    JOptionPane.showMessageDialog(null, "Pelanggan berhasil ditambahkan!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Format tanggal atau waktu salah! Gunakan format yang benar.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        tombolTampilkan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayArea.setText(""); // Mengosongkan area tampilan
                for (Pelanggan pelanggan : antrian) {
                    displayArea.append(pelanggan + "\n");
                }
            }
        });

        tombolSortir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Quick Sort (waktu kedatangan)
                quickSort(antrian, 0, antrian.size() - 1);

                // Menetapkan prioritas (Insertion Sort)
                for (int i = 0; i < antrian.size(); i++) {
                    antrian.get(i).setPrioritas(i + 1); // Prioritas dimulai dari 1
                }
                insertionSort(antrian);

                // Hasil sorting
                displayArea.setText("Data telah disorting!\n\n");
                for (Pelanggan pelanggan : antrian) {
                    displayArea.append(pelanggan + "\n");
                }
            }
        });

        tombolProses.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (antrian.isEmpty()) {
                    displayArea.setText("Antrian kosong. Tidak ada pelanggan untuk diproses.");
                    return;
                }

                // Ambil pelanggan pertama dari antrian (FIFO)
                Pelanggan diproses = antrian.remove(0);

                displayArea.setText("Memproses pelanggan berikut:\n");
                displayArea.append(diproses.toString() + "\n");

                // Menampilkan antrian setelah diproses
                displayArea.append("\nAntrian saat ini:\n");
                for (Pelanggan pelanggan : antrian) {
                    displayArea.append(pelanggan + "\n");
                }
            }
        });

        tombolCari.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String namaCari = fieldCari.getText();
                displayArea.setText(""); // Mengosongkan area tampilan

                boolean ditemukan = false;
                for (Pelanggan pelanggan : antrian) {
                    if (pelanggan.getNama().equalsIgnoreCase(namaCari)) {
                        displayArea.append("Ditemukan: " + pelanggan + "\n");
                        ditemukan = true;
                        break;
                    }
                }

                if (!ditemukan) {
                    displayArea.append("Pelanggan dengan nama \"" + namaCari + "\" tidak ditemukan.\n");
                }
            }
        });
    }

    // Quick Sort
    private void quickSort(ArrayList<Pelanggan> list, int low, int high) {
        if (low < high) {
            int pi = partition(list, low, high);

            quickSort(list, low, pi - 1);
            quickSort(list, pi + 1, high);
        }
    }

    private int partition(ArrayList<Pelanggan> list, int low, int high) {
        Pelanggan pivot = list.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (list.get(j).getWaktuKedatangan().isBefore(pivot.getWaktuKedatangan())) {
                i++;
                Pelanggan temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }

        Pelanggan temp = list.get(i + 1);
        list.set(i + 1, list.get(high));
        list.set(high, temp);

        return i + 1;
    }

    // Insertion Sort
    private void insertionSort(ArrayList<Pelanggan> list) {
        for (int i = 1; i < list.size(); i++) {
            Pelanggan key = list.get(i);
            int j = i - 1;

            while (j >= 0 && list.get(j).getPrioritas() > key.getPrioritas()) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SistemAntrian().setVisible(true);
        });
    }
}
