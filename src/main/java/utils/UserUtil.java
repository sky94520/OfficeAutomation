package utils;

import org.activiti.engine.identity.User;

import javax.servlet.http.HttpSession;

public class UserUtil {
    public static final String USER = "user";

    public static void saveUserToSession(HttpSession session, User user){
        session.setAttribute(USER, user);
    }
    public static User getUserFromSession(HttpSession session){
        Object object = session.getAttribute(USER);

        return object == null?null:(User)object;
    }
}
