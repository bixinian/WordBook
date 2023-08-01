import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class WordEditDialog extends JDialog {

    // UI 控件
    private JTextField wordField;
    private JTextField definitionField;
    private JButton okButton;
    private JButton cancelButton;

    // 是否按下了“确定”按钮
    private boolean okPressed;

    public WordEditDialog(JFrame parent, String word, String definition) {
        super(parent, "修改单词", true);

        // 初始化单词输入框、中文解释输入框和按钮
        initWordField(word);
        initDefinitionField(definition);
        initButtons();

        // 初始化窗口
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(wordField, BorderLayout.NORTH);
        contentPane.add(definitionField, BorderLayout.CENTER);
        contentPane.add(createButtonPanel(), BorderLayout.SOUTH);
        setContentPane(contentPane);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
    }

    // 初始化单词输入框
    private void initWordField(String word) {
        JPanel wordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        wordField = new JTextField(word, 20);
        wordField.setBorder(BorderFactory.createTitledBorder("当前单词："));
        wordField.setEditable(false);
        wordPanel.add(wordField, BorderLayout.CENTER);

        getContentPane().add(wordPanel, BorderLayout.NORTH);
    }

    // 初始化中文解释输入框
    private void initDefinitionField(String definition) {
        JPanel definitionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        definitionField = new JTextField(definition, 20);
        definitionField.setBorder(BorderFactory.createTitledBorder("修改后的中文解释："));
        definitionPanel.add(definitionField, BorderLayout.CENTER);

        getContentPane().add(definitionPanel, BorderLayout.CENTER);
    }

    // 初始化按钮
    private void initButtons() {
        okButton = new JButton("确定");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                okPressed = true;
                dispose();
            }
        });

        cancelButton = new JButton("取消");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    // 创建按钮面板
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        return buttonPanel;
    }

    // 是否按下了“确定”按钮
    public boolean isOkPressed() {
        return okPressed;
    }

    // 获取修改后的中文解释
    public String getDefinition() {
        return definitionField.getText().trim();
    }
}