package greencar77.mosties;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import greencar77.mosties.notification.DialogNotification;
import greencar77.mosties.notification.SoundNotification;

public class App {
    private static int DEFAULT_DURATION_MINS = 30; //minutes
    private static Integer INFO_INTERVAL = 1; //set null if not used

    public static void main(String[] args) {
        int duration = DEFAULT_DURATION_MINS;
        
        if (args != null) {
            duration = Integer.valueOf(args[0]);
        }

        boolean beep  = false;
        if (args.length > 1 && args[1].equals("beep")) {
            beep = true;
            System.out.println("Ar skaņas signālu");
            SoundNotification.beep();
        }

        wait(duration);
        notifyUser(duration, beep);
    }

    private static void wait(int duration) {
        Date current = new Date();
        Calendar end = Calendar.getInstance();
        end.add(Calendar.MINUTE, duration);

        out(Message.CURRENT_TIME, current);
        out(Message.STARTING, duration, end.getTime());

        if (INFO_INTERVAL == null) {
            Utils.sleepSafe(duration * 60 * 1000);
        } else {
            while (current.compareTo(end.getTime()) < 0) {
                long deltaSeconds = (end.getTimeInMillis() - current.getTime()) / 1000;
                long minutes = deltaSeconds / 60;
                long seconds = deltaSeconds - minutes * 60;
                //http://www.rgagnon.com/javadetails/java-0448.html
                out(Message.INTERMEDIATE, minutes, String.format("%1$-" + 2 + "s", "" + seconds).replace(' ', '0'));

                if (deltaSeconds < INFO_INTERVAL * 60) {
                    //partial interval
                    Utils.sleepSafe((int)(end.getTimeInMillis() - current.getTime()));
                } else {
                    //full interval
                    Utils.sleepSafe(INFO_INTERVAL * 60 * 1000);
                }
                current = new Date();
            }
        }
        current = new Date();
        out(Message.CURRENT_TIME, current);
    }

    private static void notifyUser(int duration, boolean beep) {
        String message = String.format(Message.USER_NOTIFICATION, duration);
        
        out(Message.USER_NOTIFICATION, duration);
        
        ExecutorService executor = Executors.newFixedThreadPool(2); //1 for Dialog + 1 for Sound
        executor.submit(new DialogNotification(message));
        if (beep) {
            executor.submit(new SoundNotification());
        }
        executor.shutdown();
    }

    private static void out(String message, Object... args) {
        System.out.println(String.format(message, args));
    }
}
