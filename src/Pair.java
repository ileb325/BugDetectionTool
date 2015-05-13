
public class Pair {
    private final String function1;
    private final String function2;
    
    public Pair(String function1, String function2) {
    	//Set in alphabetical order
    	if (function1.compareTo(function2) < 0){
    		this.function1 = function1;
    		this.function2 = function2;
        } else {
        	this.function1 = function2;
        	this.function2 = function1;
        }
    }

    public String getFunction1() {
        return function1;
    }

    public String getFunction2() {
        return function2;
    }

    @Override
    public String toString() {
        return "(" + function1 + ", " + function2 + ")";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((function1 == null) ? 0 : function1.hashCode());
        result = prime * result
                + ((function2 == null) ? 0 : function2.hashCode());
        return result;
    }                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              b

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pair other = (Pair) obj;
        if (function1 == null) {
            if (other.function1 != null)
                return false;
        } else if (!function1.equals(other.function1))
            return false;
        if (function2 == null) {
            if (other.function2 != null)
                return false;
        } else if (!function2.equals(other.function2))
            return false;
        return true;
    }

}
