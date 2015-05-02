import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Invariants{

    private Map<String, List<String>> invarHM;
    private Map<String, Integer> indHM;
    private Map<Pair, Integer> pairHM;
    private int T_SUPPORT;
    private double T_CONFIDENCE;

    public Invariants(Map<String, List<String>> invarHM,
                Map<String,Integer> indHM,
                Map<Pair, Integer> pairHM,
                int T_SUPPORT,
                double T_CONFIDENCE){
        this.invarHM = invarHM;
        this.indHM = indHM;
        this.pairHM = pairHM;
        this.T_SUPPORT = T_SUPPORT;
        this.T_CONFIDENCE = T_CONFIDENCE;
    }



    public void FindInvariants(){
        for(Map.Entry<Pair, Integer> entry : pairHM.entrySet()) {
            if(entry.getValue() >= T_SUPPORT){
                Pair pair = entry.getKey();
                String function1 = pair.getFunction1();
                String function2 = pair.getFunction2();
                double c1 = (double)entry.getValue()/(double)indHM.get(function1);
                double c2 = (double)entry.getValue()/(double)indHM.get(function2);

                if(c1 >= T_CONFIDENCE){
                    List<String> list = invarHM.get(function1);
                    if(list == null){
                        list = new ArrayList<String>();
                    }
                    list.add(function2);
                    invarHM.put(function1, list); 
                }

                if(c2 >= T_CONFIDENCE){
                    List<String> list = invarHM.get(function2);
                    if(list == null){
                        list = new ArrayList<String>();
                    }
                    list.add(function1);
                    invarHM.put(function2, list);     
                }
             }
         }
    }
}
