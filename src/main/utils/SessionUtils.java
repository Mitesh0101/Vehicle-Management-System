package main.utils;
import main.config.AppConstants;
import main.model.User;

public class SessionUtils
{
    private static User currentUser=null;

    public static void setCurrentUser(User user)
    {
        currentUser=user;
    }

    public static User getCurrentUser()
    {
        return currentUser;
    }

    public static void clearSession()
    {
        currentUser=null;
    }

    public static boolean isOfficer()
    {
        return currentUser!=null && "OFFICER".equalsIgnoreCase(currentUser.getRole());
    }

    public static boolean isUser()
    {
        return currentUser!=null && AppConstants.ROLE_USER.equalsIgnoreCase(currentUser.getRole());
    }
}
