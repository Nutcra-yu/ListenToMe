package android.ListenToMe.Utils;

public enum Gender {
    None, Boy, Girl;

    public static Gender intToGender(int genderInt) {

        Gender returnGender = None;

        switch (genderInt) {
            case 0:
                break;
            case 1:
                returnGender = Boy;
                break;
            case 2:
                returnGender = Girl;
                break;
        }
        return returnGender;
    }

    public static String intToString(int genderInt) {

        String returnStr = "none";

        switch (genderInt) {
            case 1:
                returnStr = "Boy";

            case 2:
                returnStr = "Girl";
        }
        return returnStr;
    }

    public static int genderToInt(Gender gender) {

        int returnInt = 0;

        switch (gender) {
            case None:
                break;
            case Boy:
                returnInt = 1;
            case Girl:
                returnInt = 2;
        }
        return returnInt;
    }
}
