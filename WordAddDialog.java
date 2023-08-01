import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class WordAddDialog extends JDialog {

    // UI 控件
    private JTextField wordField;
    private JTextField definitionField;
    private JButton okButton;
    private JButton cancelButton;

    // 是否按下了“确定”按钮
    private boolean okPressed;

    public WordAddDialog(WordBook parent) {
        super(parent, "添加单词", true);

        // 初始化单词输入框、中文解释输入框和按钮
        initWordField();
        initDefinitionField();
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
    private void initWordField() {
        JPanel wordPanel = new JPanel(new BorderLayout());

        wordField = new JTextField(20);
        wordField.setBorder(BorderFactory.createTitledBorder("请输入单词"));
        wordPanel.add(wordField, BorderLayout.CENTER);

        getContentPane().add(wordPanel, BorderLayout.NORTH);
    }


    // 初始化中文解释输入框
    private void initDefinitionField() {
        JPanel definitionPanel = new JPanel(new BorderLayout());


        definitionField = new JTextField(20);
        definitionField.setBorder(BorderFactory.createTitledBorder("请输入中文解释"));
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

    // 获取单词
    public String getWord() {
        return wordField.getText();
    }

    // 获取中文解释
    public String getDefinition() {
        return definitionField.getText();
    }
}