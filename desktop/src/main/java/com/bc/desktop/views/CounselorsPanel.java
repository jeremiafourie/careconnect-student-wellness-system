package com.bc.desktop.views;

import com.bc.desktop.models.Counselor;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class CounselorsPanel extends JPanel {
    private JTable counselorsTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JTextField searchField;
    private JComboBox<String> specializationFilter;
    
    private final String[] columnNames = {"ID", "Name", "Specialization", "Email", "Phone", "Available"};
    
    public CounselorsPanel() {
        initializeComponents();
        layoutComponents();
    }
    
    private void initializeComponents() {
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 5) return Boolean.class;
                return String.class;
            }
        };
        
        counselorsTable = new JTable(tableModel);
        counselorsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        counselorsTable.getTableHeader().setReorderingAllowed(false);
        
        counselorsTable.getColumnModel().getColumn(0).setMaxWidth(50);
        counselorsTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        counselorsTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        counselorsTable.getColumnModel().getColumn(3).setPreferredWidth(200);
        counselorsTable.getColumnModel().getColumn(4).setPreferredWidth(120);
        counselorsTable.getColumnModel().getColumn(5).setPreferredWidth(80);
        
        addButton = new JButton("Add Counselor");
        editButton = new JButton("Edit Counselor");
        deleteButton = new JButton("Delete Counselor");
        refreshButton = new JButton("Refresh");
        
        searchField = new JTextField(20);
        searchField.setToolTipText("Search by name or specialization");
        
        specializationFilter = new JComboBox<>(new String[]{
            "All Specializations", "General Counseling", "Academic Support", 
            "Mental Health", "Career Guidance", "Stress Management"
        });
        
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        
        counselorsTable.getSelectionModel().addListSelectionListener(e -> {
            boolean hasSelection = counselorsTable.getSelectedRow() != -1;
            editButton.setEnabled(hasSelection);
            deleteButton.setEnabled(hasSelection);
        });
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(new JLabel("Filter:"));
        searchPanel.add(specializationFilter);
        searchPanel.add(refreshButton);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        
        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        
        JScrollPane scrollPane = new JScrollPane(counselorsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Counselors"));
        
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    public void updateTable(List<Counselor> counselors) {
        tableModel.setRowCount(0);
        for (Counselor counselor : counselors) {
            Object[] row = {
                counselor.getId(),
                counselor.getName(),
                counselor.getSpecialization(),
                counselor.getEmail(),
                counselor.getPhone(),
                counselor.isAvailable()
            };
            tableModel.addRow(row);
        }
    }
    
    public Counselor getSelectedCounselor(List<Counselor> counselors) {
        int selectedRow = counselorsTable.getSelectedRow();
        if (selectedRow != -1) {
            Long id = (Long) tableModel.getValueAt(selectedRow, 0);
            return counselors.stream()
                    .filter(c -> c.getId().equals(id))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
    
    public void addAddButtonListener(ActionListener listener) {
        addButton.addActionListener(listener);
    }
    
    public void addEditButtonListener(ActionListener listener) {
        editButton.addActionListener(listener);
    }
    
    public void addDeleteButtonListener(ActionListener listener) {
        deleteButton.addActionListener(listener);
    }
    
    public void addRefreshButtonListener(ActionListener listener) {
        refreshButton.addActionListener(listener);
    }
    
    public String getSearchText() {
        return searchField.getText().trim();
    }
    
    public String getSelectedSpecialization() {
        String selected = (String) specializationFilter.getSelectedItem();
        return "All Specializations".equals(selected) ? null : selected;
    }
    
    public void clearSelection() {
        counselorsTable.clearSelection();
    }
    
    public void setAdminMode(boolean isAdmin) {
        addButton.setVisible(isAdmin);
        editButton.setVisible(isAdmin);
        deleteButton.setVisible(isAdmin);
        
        if (!isAdmin) {
            counselorsTable.clearSelection();
        }
    }
}