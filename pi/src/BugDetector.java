import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class BugDetector {

    private final Map<String, List<String>> likelyInvariants;
    private final List<Scope> scopes;
    private final Map<String, Integer> functionHashMap;
    private final Map<Pair, Integer> functionPairHashMap;
    
    public BugDetector(Map<String, List<String>> likelyInvariants, 
                List<Scope> scopes, 
                Map<String,Integer> functionHashMap,
                Map<Pair, Integer> functionPairHashMap){
        this.likelyInvariants = likelyInvariants;
        this.scopes = scopes;
        this.functionHashMap = functionHashMap;
        this.functionPairHashMap = functionPairHashMap;        
    }
    
    public List<Bug> findPotentialBugs(){
        List<Bug> bugs = new ArrayList<Bug>();
        
        for(Scope scope : scopes){
            
            for (String function1 : scope.getFunctions()){
                
                //likelyInvariants.get(function1) = list of functions that are paired with function1
                try {
                    for (String function2 : likelyInvariants.get(function1)) {

                        //if a function is supposed to be paired but is not, then there's a potential bug
                        if (!scope.getFunctions().contains(function2)) {
                            Pair missingPair = new Pair(function1, function2);
                            double confidence = ((double) functionPairHashMap.get(missingPair) / (double) functionHashMap.get(function1));
                            Bug bug = new Bug(function1, scope, missingPair, functionPairHashMap.get(missingPair), confidence);
                            bugs.add(bug);
                        }
                    }
                } catch (NullPointerException ex){
                    //Function isn't in likelyInvariants
                }
            }
        }
        
        return bugs;
    }
}