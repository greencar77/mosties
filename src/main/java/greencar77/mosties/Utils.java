package greencar77.mosties;

public class Utils {

    public static void sleepSafe(int millis) {
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

}
