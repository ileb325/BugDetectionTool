import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Driver {
    public static void main(String [] args) {
        Map<String, Integer> functionHashMap = new HashMap<String, Integer>();
        Map<Pair, Integer> functionPairHashMap = new HashMap<Pair, Integer>();
        Map<String, List<String>> likelyInvariants = new HashMap<String, List<String>>();
        List<Scope> scopes = new ArrayList<Scope>();

        // default arguments
        String directoryPath = "empty directory";
        String callgraphFileName = "empty file";
        int T_SUPPORT = 3;
        double T_CONFIDENCE = 0.65;
        int T_DEPTH = 0;
        
        // Process command line args
        switch(args.length) {
            case 5:
                T_DEPTH = Integer.parseInt(args[4]);
                // fall through
            case 4:
                T_CONFIDENCE = Double.parseDouble(args[3]) / 100; // this is because it is an integer in command line args
                // fall through
            case 3:
                T_SUPPORT = Integer.parseInt(args[2]);
                // fall through
            case 2:
                callgraphFileName = args[1] + ".callgraph"; // inside pipair file, it generates nameOfTheTest.bc.callgraph using OPT command
                // fall through
            case 1:
                directoryPath = args[0];
                break;
            default:
                System.out.println("Wrong number of arguments!");
                System.exit(-1);
        }

        // Part 1 - parse the call graph and populate scopes and hash maps
        Parser parser = new Parser(functionHashMap, functionPairHashMap, scopes, directoryPath, callgraphFileName, T_DEPTH);
        parser.run();
        
        // Part 2 - Generate the likely invariants
        Invariants invariants = new Invariants(likelyInvariants, functionHashMap, functionPairHashMap, T_SUPPORT, T_CONFIDENCE);
        invariants.FindInvariants();

        // Part 3 - Find potential bugs
        BugDetector bugDetector = new BugDetector(likelyInvariants, scopes, functionHashMap, functionPairHashMap);
        List<Bug> bugs = bugDetector.findPotentialBugs();

        // Print bugs
        for(Bug bug : bugs){
            System.out.println(bug);
        }
    }
}
