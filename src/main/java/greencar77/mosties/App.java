package greencar77.mosties;

import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;

public class App {
    private static int DEFAULT_DURATION_MINS = 30; //minutes
    private static Integer INFO_INTERVAL = 1; //set null if not used

    public static void main(String[] args) {
        int duration = DEFAULT_DURATION_MINS;
        
        if (args != null) {
            duration = Integer.valueOf(args[0]);
        }

        wait(duration);
        notifyUser(duration);
    }

    private static void wait(int duration) {
        Date current = new Date();
        Calendar end = Calendar.getInstance();
        end.add(Calendar.MINUTE, duration);

        out(Message.CURRENT_TIME, current);
        out(Message.STARTING, duration, end.getTime());

        if (INFO_INTERVAL == null) {
            sleepSafe(duration * 60 * 1000);
        }
        else {
            while (current.compareTo(end.getTime()) < 0) {
                long deltaSeconds = (end.getTimeInMillis() - current.getTime()) / 1000;
                long minutes = deltaSeconds / 60;
                long seconds = deltaSeconds - minutes * 60;
                //http://www.rgagnon.com/javadetails/java-0448.html
                out(Message.INTERMEDIATE, minutes, String.format("%1$-" + 2 + "s", "" + seconds).replace(' ', '0'));

                if (deltaSeconds < INFO_INTERVAL * 60) {
                    //partial interval
                    sleepSafe((int)(end.getTimeInMillis() - current.getTime()));
                } else {
                    //full interval
                    sleepSafe(INFO_INTERVAL * 60 * 1000);
                }
                current = new Date();
            }
        }
        current = new Date();
        out(Message.CURRENT_TIME, current);
    }

    private static void notifyUser(int duration) {
        String message = String.format(Message.USER_NOTIFICATION, duration);
        
        out(Message.USER_NOTIFICATION, duration);
        JOptionPane.showMessageDialog(null, message);
    }

    private static void sleepSafe(int millis) {
        if (millis < 0) {
            //Avoid "java.lang.IllegalArgumentException: timeout value is negative"
            return;
        }
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void out(String message, Object... args) {
        System.out.println(String.format(message, args));
    }
}
