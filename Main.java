import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class Main extends JFrame {

    private JPanel contentPane;
    private JPanel panel;
    private JTable table;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Main frame = new Main();
                    frame.setVisible(true);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // コンストラクタ
    public Main() {

        this.setTitle("社員マスタの CSV 入力");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 700);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        // ボタン
        JButton btnNewButton = new JButton("読み込み");
        btnNewButton.setBounds(10, 10, 91, 21);
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // CSV 読込み
                loadCsv();

            }
        });
        
        contentPane.setLayout(null);
        contentPane.add(btnNewButton);
        
        panel = new JPanel();
        panel.setBounds(12, 52, 960, 600);
        panel.setLayout(new BorderLayout(0, 0));
        contentPane.add(panel);
        
        table = new JTable() {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
        };

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane);
        
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    }
    
    // ********************
    // 初期化
    // ********************
    private void reset() {

        DefaultTableModel dtm = (DefaultTableModel) table.getModel();
        dtm.setRowCount(0);
        
        int cols = table.getColumnCount();

        for( int i = cols-1; i >= 0; i-- ) {
            table.removeColumn((table.getColumnModel()).getColumn(i));
        }
        
        // データモデルも初期化
        dtm.setColumnCount(0);

    }
    // ********************
    // 行を全て削除
    // ********************
    private void clear(){
        DefaultTableModel dtm = (DefaultTableModel) table.getModel();
        dtm.setRowCount(0);
    }
    
    // ********************
    // CSV
    // ********************
    private void loadCsv(){

        reset();

        // カレントディレクトリ
        String currentDir = System.getProperty("user.dir");
        System.out.println(currentDir);

        try {

            // テキストファイル
            FileInputStream fis = new FileInputStream(currentDir + "\\syain.csv");
            InputStreamReader isr = new InputStreamReader(fis, "MS932");
            BufferedReader br = new BufferedReader(isr);

            String line_buffer;
            String[] adata;
            int line_count = -1;
            while ( null != (line_buffer = br.readLine() ) ) {

                // カンマで分解
                adata = line_buffer.split(",");

                // 1行目はタイトル
                if ( line_count == -1 ) {
                    int count = adata.length;
                    // 先に全ての列を登録する必要があります
                    for( int i = 0; i < count; i++) {
                        String id = String.format("col%d", i);
                        addColumn( table,  id );
                    }
                    // タイトル文字列の変更を行っています。
                    // 変更しない場合は、タイトルは列を登録時の第二引数になります
                    int i = 0;
                    for( String value:  adata ) {
                        String id = String.format("col%d", i++);
                        setColumnTitle( table, id, value );
                    }
                }
                // 2行目以降がデータ
                else {
                    addRow(table);
                    int i = 0;
                    for( String value:  adata ) {
                        setColumn( table, line_count, i, value );
                        i++;
                    }
                }

                line_count++;

            }

            // テキストファイル
            br.close();
            isr.close();
            fis.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    // ****************************
    // 列の追加	
    // ****************************
    private void addColumn(JTable table, String name) {
        DefaultTableModel dtm = (DefaultTableModel) table.getModel();
        dtm.addColumn( name );
    }
    // ****************************
    // 列のタイトル文字列変更
    // ****************************
    private void setColumnTitle(JTable table, String name,String title) {
        TableColumn tc = table.getColumn(name);
        tc.setHeaderValue( title );
        tc.setIdentifier( name );
    }
    // ****************************
    // 空の行追加
    // ****************************
    private void addRow(JTable table) {
        DefaultTableModel dtm = (DefaultTableModel) table.getModel();
        Object[] obj  = null;
        dtm.addRow( obj );
    }
    // ****************************
    // 指定カラムへデータをセット
    // ****************************
    private void setColumn(JTable table,int row,int col, String data) {
        table.setValueAt( data, row,  col );
    }
    
}