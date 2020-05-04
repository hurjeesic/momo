package team.ohjj.momo.mail;

import java.util.Random;

public class TempKey {
    private boolean lowerCheck;
    private int size;

    private static TempKey instance = new TempKey();

    private TempKey() {

    }

    public static TempKey getInstance() {
        return instance;
    }

    public String getKey(int size, boolean lowerCheck) {
        this.size = size;
        this.lowerCheck = lowerCheck;

        return init();
    }

    private String init() {
        Random rand = new Random();
        StringBuffer sb = new StringBuffer();
        int num = 0;
        do {
            num = rand.nextInt(75) + 48;
            if ((num >= 48 && num <= 57) || (num >= 65 && num <= 90) || (num >= 97 && num <= 122)) {
                sb.append((char)num);
            }
            else {
                continue;
            }
        } while (sb.length() < size);

        if (lowerCheck) {
            return sb.toString().toLowerCase();
        }

        return sb.toString();
    }
}
