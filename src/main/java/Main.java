import NamesStatistics.NamesStatistics;

public class Main {
    public static void main (String[] args){
        NamesStatistics stats = new NamesStatistics();
//        //System.out.println(stats.countSubstringInNames("re"));
//        //stats.countNGramsInNames(12);
//        //stats.getMostPopularNGramInNames(9);
//        //stats.printNamesFoundInString("");
//        System.out.println(stats.createProbabilityMap());
        CLIExecuter extractor = new CLIExecuter(stats, args);
        extractor.executeFromArgs();
    }
}
