
import java.awt.*;
import javax.swing.*;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.InetSocketAddress;
import java.util.regex.Pattern;


public class SSHJ_GUI extends JFrame {
    public static boolean CheckStatus(String host, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 10);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static void CheckAndUpdateStatus(DefaultTableModel pattern) {
        String[] statuses = new String[pattern.getRowCount()];
        for (int row = 0; row < pattern.getRowCount(); row++) {
            String ip = (String) pattern.getValueAt(row, 1);
            statuses[row] = CheckStatus(ip, 22) ? "Online" : "Offline";
        }
        for (int row = 0; row < pattern.getRowCount(); row++) {
            pattern.setValueAt(statuses[row], row, 2);

        }
        pattern.fireTableDataChanged();
    }

    String[][] data;
    int numRows;
    int numCols;

    public JTextField search;
    public JTable table;
    SSHJ_GUI(ReadFile readData) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        super("J&SSH");

        this.data = readData.getFileData();
        this.numCols = readData.getNumCols();
        this.numRows = readData.getNumRows();

        search = new JTextField(20);
        JTextField login = new JTextField(20);


        login.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String loginSsh = login.getText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                String loginSsh = login.getText();
            }
        });

        JPanel panel = new JPanel();
        JLabel l1 = new JLabel("Login: ");
        panel.add(l1);
        panel.add(login);
        l1.setFont(new Font("Courier New", Font.BOLD, 24));
        JLabel s1 = new JLabel("Search for platform [hostname]: ");
        panel.add(s1);
        panel.add(search);
        s1.setFont(new Font("Courier New", Font.BOLD, 24));
        panel.setBackground(new Color(204, 255, 255));
        getContentPane().add(panel, BorderLayout.NORTH);



        String[] columnNames = {"Hostname", "IP", "Status"};
        DefaultTableModel pattern = new DefaultTableModel(data, columnNames);

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                CheckAndUpdateStatus(pattern);
            }
        }).start();

        table = new JTable(pattern) {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component colorCells = super.prepareRenderer(renderer, row, column);
                if (row % 2 == 0) {
                    colorCells.setBackground(Color.WHITE);
                    colorCells.setForeground(Color.BLACK);
                } else {
                    colorCells.setBackground(Color.GRAY);
                    colorCells.setForeground(Color.BLACK);
                }
                String status = (String) getValueAt(row, 2);
                 if(status == "Offline"){
                     colorCells.setForeground(Color.RED);
                 }

                return colorCells;
            }
        };

        search.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterData();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterData();

            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterData();

            }
        });

        JScrollPane scrollPanel = new JScrollPane(table);

        getContentPane().add(scrollPanel);
        for (int row = 0; row < numRows; row++) {
            CheckAndUpdateStatus(pattern);
        }
        setVisible(true);
        setSize(1200, 800);

        table.setFont(new Font("Courier New", Font.BOLD, 18));
        table.setBackground(Color.WHITE);
        table.setGridColor(Color.WHITE);
        table.setSize(600, 400);
        table.setRowHeight(30);
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        table.getTableHeader().setFont(new Font("Courier new", Font.BOLD, 20));
        table.getTableHeader().setOpaque(false);
        table.getTableHeader().setBackground(new Color(30, 30, 30));
        table.getTableHeader().setForeground(new Color(200, 200, 200));


        table.setDefaultEditor(Object.class, null);
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent cnt) {
                if (cnt.getClickCount() == 2 && table.getSelectedColumn() == 1) {
                    JTable target = (JTable) cnt.getSource();
                    int row = target.getSelectedRow();
                    String ipaddr = (String) target.getValueAt(row, 1);
                    try {
                        ProcessBuilder builder = new ProcessBuilder("wt.exe", "powershell", "ssh " + login.getText() + "@" + ipaddr);
                        builder.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        });

    }

    private void filterData() {
        String searchText = search.getText().trim();
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());

        if (searchText.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            try {
                RowFilter<TableModel, Integer> filter = RowFilter.regexFilter("(?i)" + Pattern.quote(searchText));
                sorter.setRowFilter(filter);
            } catch (Exception e) {
                sorter.setRowFilter(null);
            }
        }

        table.setRowSorter(sorter);
    }

    public static void main(String[] args) throws IOException, UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        ReadFile readData = new ReadFile();
        SSHJ_GUI GUI = new SSHJ_GUI(readData);



    }


}

