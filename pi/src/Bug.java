import java.text.NumberFormat;


public class Bug {
    private final String function;
    private final Scope scope;
    private final Pair pair;
    private final int support;
    private final double confidence;
    
    public Bug(String function, Scope scope, Pair pair, int support,
            double confidence) {
        super();
        this.function = function;
        this.scope = scope;
        this.pair = pair;
        this.support = support;
        this.confidence = confidence;
    }

    public String getFunction() {
        return function;
    }

    public Scope getScope() {
        return scope;
    }

    public Pair getPair() {
        return pair;
    }

    public int getSupport() {
        return support;
    }

    public double getConfidence() {
        return confidence;
    }

    @Override
    public String toString() {            
        NumberFormat numberFormat = NumberFormat.getPercentInstance();
        numberFormat.setMinimumFractionDigits(2);
        
        return "bug: " + function + " in " + scope.getName() + ", pair: " + pair + ", support: " + support + ", confidence: " + numberFormat.format(confidence);
    }
    
    
}
