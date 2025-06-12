package roomlistconfiguratormain;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import java.text.NumberFormat;
import listintegratorlibrary.LimitedLengthDocument;
import listintegratorlibrary.ListIntegrator;
import listintegratorlibrary.ListReader;
import listintegratorlibrary.ListWriter;
import listintegratorlibrary.Room;

/**
 * RoomListConfiguratorFrame inherits JFrame and
 * implements ActionListener, KeyListeners interfaces
 * creates GUI components
 * 
 * @author Hardikkumar Bhakta
 */
public class RoomListConfiguratorFrame extends JFrame implements ActionListener, KeyListener{
    
    private ListIntegrator listIntegrator;
    private JPanel liPanel;
    
    private JLabel enterRoomNoTextLabel;
    private JFormattedTextField integerField;
    
    private JLabel roomTypeTextLabel;
    private JTextField roomTypeTextField;
    
    private JLabel roomListLabel;
    private JTextArea displayArea;
    private JScrollPane scrollPane;
    
    private JButton addButton;
    private JButton removeButton;
    private JButton saveButton;
    
    public RoomListConfiguratorFrame() throws HeadlessException {
        
        listIntegrator = new ListIntegrator();
        setTitle("Room List Configurator");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        liPanel = new JPanel();
        
        liPanel.setLayout(new GridLayout(3, 3));
        
        Font fieldFont = new Font("Arial", Font.BOLD, 36);
        Font font = new Font("Arial", Font.BOLD, 20);
        
        enterRoomNoTextLabel = new JLabel("Enter Number: ");
        enterRoomNoTextLabel.setFont(font);
                
        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        numberFormat.setGroupingUsed(false);
        integerField = new JFormattedTextField(numberFormat);
        integerField.setColumns(5);
        
        integerField.setFont(fieldFont);
        integerField.setDocument(new LimitedLengthDocument(4));
        integerField.addKeyListener(this);
        
        roomTypeTextLabel = new JLabel();
        roomTypeTextLabel.setText("Room Type: ");
        
        roomTypeTextLabel.setFont(font);
        roomTypeTextField = new JTextField();
        roomTypeTextField.setFont(fieldFont);
        roomTypeTextField.setDocument(new LimitedLengthDocument(5));
        roomTypeTextField.addActionListener(this);
        
        addButton = new JButton();
        addButton.setName("Add button");
        addButton.setText("ADD");
        
        addButton.setFont(font);
        addButton.addActionListener(this);

        removeButton = new JButton();
        removeButton.setName("Remove button");
        removeButton.setText("REMOVE");
        
        removeButton.setFont(font);
        removeButton.addActionListener(this);
        
        liPanel.add(enterRoomNoTextLabel);
        liPanel.add(integerField);
        liPanel.add(addButton);
        liPanel.add(roomTypeTextLabel);
        liPanel.add(roomTypeTextField);
        liPanel.add(removeButton);
        liPanel.setLocation(1, 1);
        
        roomListLabel = new JLabel();
        roomListLabel.setText("Room List: ");
        
        roomListLabel.setFont(font);
        
        liPanel.add(roomListLabel);
        displayArea = new JTextArea();
        displayArea.setFont(font);
        
        scrollPane = new JScrollPane(displayArea);
        liPanel.add(scrollPane);
        
        saveButton = new JButton();
        saveButton.setText("SAVE");
        saveButton.setFont(font);
        saveButton.addActionListener(this);
        liPanel.add(saveButton);
        add(liPanel);
        completeReadingTask();
        pack();
        setVisible(true);

    }

    public RoomListConfiguratorFrame(GraphicsConfiguration gc) {
        super(gc);
    }

    public RoomListConfiguratorFrame(String title) throws HeadlessException {
        super(title);
    }

    public RoomListConfiguratorFrame(String title, GraphicsConfiguration gc) {
        super(title, gc);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "ADD") {
            disableAddButton();
            completeSortingTask();
            enableAddButton();
        } else if (e.getActionCommand() == "REMOVE") {
            disableRemoveButton();
            completeSortingTask2();
            enableRemoveButton();
        } else if (e.getActionCommand() == "SAVE") {
            disableSaveButton();
            completeSavingTask();
            enableSaveButton();
            openNewDialog();
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() ==  KeyEvent.VK_ENTER) {
            completeSortingTask();
        }
    }
    
    private void addRoomToList(Room room) {
        listIntegrator.buildSortedList(room);
    }

    private String getDisplayableContent() {
        StringBuilder sb = new StringBuilder();
        for (Room room : listIntegrator.getIntegratedUniuqeList()) {
            sb.append(room.getNumber()).append(" ").append(room.getType()).append("\n");
        }
        return sb.toString();
    }

    private void disableAddButton() {
        addButton.setEnabled(false);
    }
    
    private void enableAddButton() {
        addButton.setEnabled(true);
    }

    private void completeSortingTask() {
        if (integerField.getValue() != null) {
            Integer roomNumber = Integer.valueOf(integerField.getValue().toString());
            String roomType = roomTypeTextField.getText().toUpperCase();
            addRoomToList(new Room(roomNumber, roomType));
            displayArea.setEditable(false);
            String content = getDisplayableContent();
            displayArea.setText(content);
        }
        
    }

    private void disableRemoveButton() {
        removeButton.setEnabled(false);
    }

    private void enableRemoveButton() {
        removeButton.setEnabled(true);
    }

    private void completeSortingTask2() {
        String displayAreaSelectedText = displayArea.getSelectedText();
        if (displayAreaSelectedText != null) {
            String[] list = displayAreaSelectedText.split("\n");
            for (int i = 0; i < list.length; i++) {
                String[] room = list[i].split(" ");
                Integer roomNumber = Integer.valueOf(room[0]);
                String roomType = "";
                if (room.length > 1) {
                    roomType = room[1];
                }
                removeRoomFromList(roomNumber, roomType);
                displayArea.setEditable(false);
                String content = getDisplayableContent();
                displayArea.setText(content);
            }
        }
    }

    private void removeRoomFromList(Integer roomNumber, String roomType) {
        listIntegrator.remove(roomNumber, roomType);
    }

    private void disableSaveButton() {
        saveButton.setEnabled(false);
    }

    private void enableSaveButton() {
        saveButton.setEnabled(true);
    }

    private void completeSavingTask() {
        String content = displayArea.getText();
        ListWriter lw = new ListWriter();
        lw.writeToConfigurationTextFile(content);
    }

    private void openNewDialog() {
        JOptionPane.showMessageDialog(this, "Room list saved successfully.", "Save Confirmation", JOptionPane.INFORMATION_MESSAGE);        
    }
    
    private void completeReadingTask() {
        disableAddButton();
        disableRemoveButton();
        disableSaveButton();
        ListReader lr = new ListReader();
        if (lr.configurationFileExists()) {
            String content = lr.readFromConfigurationTextFile();
            if (content != null || !"".equals(content)) {
                String[] list = content.split("\n");
                for (int i = 0; i < list.length; i++) {
                    String[] room = list[i].split(" ");
                    Integer roomNumber = null;
                    if (!room[0].equals("")) {
                        roomNumber = Integer.valueOf(room[0]);
                    }
                    String roomType = "";
                    if (room.length > 1) {
                        roomType = room[1];
                    }
                    if (roomNumber != null && !roomType.equals("")) {
                        addRoomToList(new Room(roomNumber, roomType));
                    }
                    displayArea.setEditable(false);
                    content = getDisplayableContent();
                    displayArea.setText(content);
                }
            }
        }
        enableAddButton();
        enableRemoveButton();
        enableSaveButton();
    }
}
