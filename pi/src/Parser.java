import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Parser {
    private String mDirectoryPath;
    private String mCallgraphFileName;
    private Map<String, Integer> mIndividualHashMap;
    private Map<Pair, Integer> mFunctionPairHashMap;
    private List<Scope> mScopes;
    private int mDepth;
    private HashMap<String, HashSet<String>> mTempScopes = new HashMap<String, HashSet<String>>(); // this is used for depth

    public Parser(Map<String, Integer> IndividualHashMap, Map<Pair, Integer> FunctionPairHashMap, List<Scope> Scopes, String directoryPath, String callgraphFileName, int Depth) {
        mIndividualHashMap = IndividualHashMap;
        mFunctionPairHashMap = FunctionPairHashMap;
        mScopes = Scopes;
        mDepth = Depth;
        mCallgraphFileName = callgraphFileName;
        mDirectoryPath = directoryPath;
    }

    private void populateScopes() {
        // read from file
        final String fileLocation = System.getProperty("user.home") + mDirectoryPath + "/" + mCallgraphFileName; // TODO: USE THIS WHEN YOU'RE RUNNING FROM YOUR MACHINE
//        final String fileLocation = mDirectoryPath + "/" + mCallgraphFileName; // TODO: USE THIS WHEN YOU'RE RUNNING FROM ECE.LINUX

        try {
            FileReader fileReader = new FileReader(fileLocation);
            BufferedReader br = new BufferedReader(fileReader);
            String line = br.readLine();

            // skip first paragraph <<null function>>
            while (!line.isEmpty()) {
                line = br.readLine();
            }

            Scope currentScope = null;
            while (line != null) {

                // function name
                if (line.isEmpty()) {

                    // add previous scope to the mScopes if it already exists
                    if (currentScope != null) {
                        mScopes.add(currentScope);
                        currentScope = null; // dereference for next scope
                    }

                    // get next line (function name)
                    line = br.readLine();
                    if(line == null) break; // Expect a line at the end of the callgraph file for EOF

                    Pattern p = Pattern.compile( "'([^']*)'" );
                    Matcher m = p.matcher(line);
                    m.find();
                    String currentFunctionName = m.group(1).replace("'", "");

                    // assign new scope
                    currentScope = new Scope(currentFunctionName, new HashSet<String>());
                }

                // find call string
                else {
                    Pattern p = Pattern.compile( "'([^']*)'" );
                    Matcher m = p.matcher(line);

                    // found call string
                    if (m.find()) {
                        String currentCall = m.group(1).replace("'", "");
                        currentScope.getFunctions().add(currentCall);
                    }

                    // no call string
                    // currentScope.getFunctions().add(nothing here);
                }

                // iterate next line
                line = br.readLine();
            }

            // finished reading call graph

        } catch (Exception e) {
            // somethings gone wrong
            System.out.println("Failed parsing file... Exiting with message: ");
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    private void createTempScopes() {
        for (Scope scope : mScopes) {
            String scopeName = scope.getName();
            HashSet<String> scopeFunctions = new HashSet<String>(scope.getFunctions());
            mTempScopes.put(scopeName, scopeFunctions);
        }
    }

    /*
    input: depth should be 0 or greater
    description: Inter-procedure analysis helper. It goes through each scope in mScope and expand each function to scope[function].functions and remove cycles
     */
    public void populateDepthScopes(int depth) {
        while(depth > 0) {
            for (Scope scope : mScopes) {
                // new functions that need to be added to the scope.functions()
                Set<String> tempNewFunctions = new HashSet<String>();

                for (Iterator<String> currentFunctionIterator = scope.getFunctions().iterator() ; currentFunctionIterator.hasNext(); ) {
                    String currentFunctionName = currentFunctionIterator.next();
                    if (mTempScopes.containsKey(currentFunctionName)) {
                        currentFunctionIterator.remove();
                        tempNewFunctions.addAll(mTempScopes.get(currentFunctionName));

                        // remove cycles
                        tempNewFunctions.remove(currentFunctionName);
                    }
                }

                // add new functions back to scope.functions() and clear
                scope.getFunctions().addAll(tempNewFunctions);
                tempNewFunctions.clear();
            }
            depth = depth - 1;
        }
    }

    private void populateIndividualHashMap() {
        for (Scope scope : mScopes) {
            for (String function : scope.getFunctions()) {
                // increment count, increment and put
                int count = mIndividualHashMap.containsKey(function) ? mIndividualHashMap.get(function) : 0;
                mIndividualHashMap.put(function, count + 1);
            }
        }
    }

    private void populateFunctionPairHashMap() {
        for (Scope scope : mScopes) {
            // convert HashSet into ArrayList.  This is because we need to iterate through HashSet.
            List<String> functions = new ArrayList<String>(scope.getFunctions());

            for (int i = 0; i < functions.size(); i++) {
                for (int j = i + 1; j < functions.size(); j++) {
                    String function1 = functions.get(i);
                    String function2 = functions.get(j);
                    Pair functionPair = new Pair(function1, function2);

                    // get count, increment and put
                    int count = mFunctionPairHashMap.containsKey(functionPair) ? mFunctionPairHashMap.get(functionPair) : 0;
                    mFunctionPairHashMap.put(functionPair, count + 1);
                }
            }
        }
    }

    public void run() {
        populateScopes();
        createTempScopes();
        populateDepthScopes(mDepth);
        populateIndividualHashMap();
        populateFunctionPairHashMap();
    }
}