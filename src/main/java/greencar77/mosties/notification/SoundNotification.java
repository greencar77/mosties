package greencar77.mosties.notification;

import java.io.IOException;

import greencar77.mosties.Utils;

public class SoundNotification implements Runnable {
    
    private static final int LIMIT = 60 * 10; //beep max 10 minutes

    public void run() {
        Thread t = new Thread(new Runnable() {
            public void run() {
                int i = 0;
                while (i++ < LIMIT) { //beep until interrupted or timeout
                    beep();
                    Utils.sleepSafe(1000);
                }
            }
        });

        t.start();
        try {
            System.out.println("Nospiediet Enter:");
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        t.stop();
    }
    
    public static void beep() {
        System.out.print("\007");
    }
}
