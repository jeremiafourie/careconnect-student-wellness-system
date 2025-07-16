package com.bc.desktop;

import com.bc.desktop.controllers.MainController;
import javax.swing.*;

public class Desktop {
    public static void main(String[] args) {
        // Use default look and feel
        
        SwingUtilities.invokeLater(() -> {
            try {
                MainController mainController = new MainController();
                mainController.start();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                    null,
                    "Failed to start application: " + e.getMessage(),
                    "Startup Error",
                    JOptionPane.ERROR_MESSAGE
                );
                System.exit(1);
            }
        });
    }
}
