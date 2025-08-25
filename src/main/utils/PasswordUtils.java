package main.utils;

public class PasswordUtils {
    // hashes the password (reverses the plain password and adds #123 to it)
    public static String hashedPassword(String plainPassword)
    {
        if(plainPassword==null)
        {
            return null;
        }

        String reversed="";
        for (int i=plainPassword.length()-1; i>=0;i--)
        {
            reversed+=plainPassword.charAt(i);
        }

        return reversed + "#123";
    }

    // unhashes the password removes the #123 from the hashedpassword and reverses it again to get plain password
    public static String unhashedPassword(String hashedPassword) {
        if (hashedPassword == null || !hashedPassword.endsWith("#123")) {
            return null;
        }

        String reversedWithoutSuffix = hashedPassword.substring(0, hashedPassword.length() - "#123".length());

        String plainPassword = "";
        for (int i = reversedWithoutSuffix.length() - 1; i >= 0; i--) {
            plainPassword += reversedWithoutSuffix.charAt(i);
        }

        return plainPassword;
    }
}