import java.util.Set;


public class Scope {
    private final String name;
    private final Set<String> functions;

    
    public Scope(String name, Set<String> functions) {
        super();
        this.name = name;
        this.functions = functions;
    }
    
    public String getName() {
        return name;
    }

    public Set<String> getFunctions() {
        return functions;
    }

    @Override
    public String toString() {
        return name;
    }
}
