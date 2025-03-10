package ru.alex;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FolderCreatorApp extends JFrame {
    private final JTextField directoryField;
    private final JTextField folderNameField;
    private final JTextField folderCountField;
    private JButton createButton;
    private JButton browseButton;

    public FolderCreatorApp() {
        // Настройка окна
        setTitle("Создание папок");
        setSize(400, 300); // Установим фиксированный размер окна
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false); // Запрещаем изменение размера окна

        // Создание компонентов
        JLabel directoryLabel = new JLabel("Директория папок:");
        directoryField = new JTextField(30);
        JLabel folderNameLabel = new JLabel("Имя серии папок: ");
        folderNameField = new JTextField(25);
        JLabel folderCountLabel = new JLabel("Количество папок:");
        folderCountField = new JTextField(5);
        createButton = new JButton("Создать папки");
        browseButton = new JButton("Обзор пути");

        // Панель для размещения компонентов
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(122, 214, 236); // Светло-голубой
                Color color2 = new Color(38, 76, 97); // Голубой
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // Добавление компонентов на панель
        panel.add(directoryLabel);
        panel.add(directoryField);
        panel.add(browseButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Отступ между элементами
        panel.add(folderNameLabel);
        panel.add(folderNameField);
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Отступ между элементами
        panel.add(folderCountLabel);
        panel.add(folderCountField);
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Отступ между элементами
        panel.add(createButton);

        // Добавление панели в окно
        add(panel);

        // Обработчик нажатия на кнопку "Обзор"
        browseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showOpenDialog(FolderCreatorApp.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                directoryField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        // Обработчик нажатия на кнопку "Создать папки"
        createButton.addActionListener(e -> createFolders());
    }

    private void createFolders() {
        String directoryPath = directoryField.getText();
        String folderName = folderNameField.getText();
        String folderCountText = folderCountField.getText();

        // Проверка введенных данных
        if (directoryPath.isEmpty() || folderName.isEmpty() || folderCountText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Заполните все поля!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int folderCount;
        try {
            folderCount = Integer.parseInt(folderCountText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Введите корректное число папок!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Проверка существования директории
        Path directory = Paths.get(directoryPath);
        if (!Files.exists(directory)) {
            JOptionPane.showMessageDialog(this, "Указанная директория не существует!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Создание папок
        try {
            for (int i = 1; i <= folderCount; i++) {
                Path newFolder = directory.resolve(folderName + "_" + i);
                Files.createDirectories(newFolder);
            }
            JOptionPane.showMessageDialog(this, "Папки успешно созданы!", "Успех", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ошибка при создании папок: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Запуск приложения
        SwingUtilities.invokeLater(() -> new FolderCreatorApp().setVisible(true));
    }
}