package context;

import java.util.HashMap;
import java.util.Map;

public class TestContext {
    private final Map<String, Object> session;

    public TestContext() {
        this.session = new HashMap<>();
    }

    public Object getSessionVar(String key) {
        return session.get(key);
    }
    public void setSessionVar(String key, Object value) {
        session.put(key, value);
    }
}
