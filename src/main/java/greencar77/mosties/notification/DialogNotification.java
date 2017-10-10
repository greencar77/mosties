package greencar77.mosties.notification;

import javax.swing.JOptionPane;

public class DialogNotification implements Runnable {
    private String message;
    
    public DialogNotification(String message) {
        this.message = message;
    }

    public void run() {
        JOptionPane.showMessageDialog(null, message);
    }

}
