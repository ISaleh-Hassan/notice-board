package experis.noticeboard.config;
import java.util.HashSet;

public class SessionConfiguration {
    private static SessionConfiguration sessionKeeperInstance = null;

    private HashSet<String> validSessions = new HashSet<>();

    public boolean CheckSession(String session) {
        return validSessions.contains(session);
    }

    public void AddSession(String session){
        validSessions.add(session);
    }

    public void RemoveSession(String session){
        if(validSessions.contains(session)) {
            validSessions.remove(session);
        }
    }

    // Singleton
    public static SessionConfiguration getInstance(){
        if (sessionKeeperInstance == null){
            sessionKeeperInstance = new SessionConfiguration();
        }

        return sessionKeeperInstance;
    }
}

