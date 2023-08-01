import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class WordBook extends JFrame implements ActionListener {

    // 单词簿文件名
    private static final String FILENAME = "wordbook.txt";

    // 单词簿数据
    private final Map<String, String> wordbook;

    // UI 控件
    private JTable WordList;
    private DefaultTableModel model;

    private JTextField searchField;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;

    public WordBook() {
        setTitle("英语单词簿");

        // 初始化工具栏
        initToolbar();

        // 初始化单词列表和查询框
        initWordList();
        initSearchBox();

        // 读取单词簿数据
        wordbook = new HashMap<>();
        readWordBook();

        // 初始化窗口
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    // 初始化工具栏
    private void initToolbar() {
        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        addButton = new JButton("添加");
        addButton.addActionListener(this);
        toolbarPanel.add(addButton);

        editButton = new JButton("修改");
        editButton.addActionListener(this);
        toolbarPanel.add(editButton);

        deleteButton = new JButton("删除");
        deleteButton.addActionListener(this);
        toolbarPanel.add(deleteButton);

        add(toolbarPanel, BorderLayout.NORTH);
    }

    // 初始化单词列表
    private void initWordList() {
        // 创建数据模型和表格对象
        model = new DefaultTableModel(new String[]{"单词", "中文解释"}, 0) {
            // 重写 isCellEditable() 方法，禁止单元格编辑
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        WordList = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(WordList);
        add(scrollPane, BorderLayout.CENTER);


        // 设置表格样式
        WordList.getColumnModel().getColumn(0).setPreferredWidth(150);
        WordList.getColumnModel().getColumn(1).setPreferredWidth(300);
        WordList.setRowHeight(25);
    }



    // 初始化查询框
    private void initSearchBox() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel searchLabel = new JLabel("查找单词：");
        searchPanel.add(searchLabel);

        searchField = new JTextField(20);
        searchPanel.add(searchField);

        JButton searchButton = new JButton("查询");
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String word = searchField.getText().trim();
                if (word.isEmpty()) {
                    JOptionPane.showMessageDialog(WordBook.this, "请输入要查询的单词。", "错误", JOptionPane.ERROR_MESSAGE);
                } else if (!wordbook.containsKey(word)) {
                    JOptionPane.showMessageDialog(WordBook.this, "单词不存在，请重新输入。", "错误", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(WordBook.this, word + ": " + wordbook.get(word), "查询结果", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.SOUTH);
    }

    // 显示添加单词对话框
    private void showAddDialog() {
        WordAddDialog dialog = new WordAddDialog(this);
        dialog.setVisible(true);

        if (dialog.isOkPressed()) {
            String word = dialog.getWord();
            String definition = dialog.getDefinition();

            if (wordbook.containsKey(word)) {
                showErrorMessage("单词已存在，请修改。");
            } else {
                wordbook.put(word, definition);
                model.addRow(new Object[]{word, definition});
            }
        }
    }

    // 显示修改单词对话框
    private void showEditDialog() {
        int selectedIndex = WordList.getSelectedRow();

        if (selectedIndex == -1) {
            showErrorMessage("请选择要修改的单词。");
            return;
        }

        String word = WordList.getValueAt(selectedIndex, 0).toString();
        String definition = wordbook.get(word);

        WordEditDialog dialog = new WordEditDialog(this, word, definition);
        dialog.setVisible(true);

        if (dialog.isOkPressed()) {
            String newDefinition = dialog.getDefinition();
            wordbook.put(word, newDefinition);
            model.setValueAt(newDefinition, selectedIndex, 1);
        }
    }

    // 显示删除单词对话框
    private void deleteSelectedWord() {
        int[] selectedIndices = WordList.getSelectedRows();

        if (selectedIndices.length == 0) {
            showErrorMessage("请选择要删除的单词。");
            return;
        }

        int option = JOptionPane.showConfirmDialog(
                this,
                "你确定要删除所选的单词吗？",
                "确认",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (option == JOptionPane.YES_OPTION) {
            for (int i = selectedIndices.length - 1; i >= 0; i--) {
                String word = WordList.getValueAt(selectedIndices[i], 0).toString();
                wordbook.remove(word);
                model.removeRow(selectedIndices[i]);
            }
        }
    }

    // 读取单词簿数据
    private void readWordBook() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("----");
                if (parts.length == 2) {
                    wordbook.put(parts[0], parts[1]);
                    model.addRow(new Object[]{parts[0], parts[1]});
                }
            }
            System.out.println(wordbook);
        } catch (IOException e) {
            showErrorMessage("读取单词簿文件出错：" + e.getMessage());
        }
    }

    // 保存单词簿数据
    private void saveWordBook() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME))) {
            for (String word : wordbook.keySet()) {
                writer.write(word + "----" + wordbook.get(word) + "\n");
            }
            System.out.println(wordbook);
        } catch (IOException e) {
            showErrorMessage("保存单词簿文件出错：" + e.getMessage());
        }
    }

    // 显示错误消息框
    public static void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "错误", JOptionPane.ERROR_MESSAGE);
    }

    // 处理按钮事件
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            showAddDialog();
            saveWordBook();
        } else if (e.getSource() == editButton) {
            showEditDialog();
            saveWordBook();
        } else if (e.getSource() == deleteButton) {
            deleteSelectedWord();
            saveWordBook();
        }
    }

    public static void main(String[] args) {
        new WordBook();
    }
}
