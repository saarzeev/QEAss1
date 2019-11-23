import NamesStatistics.NamesStatistics;

public class Main {
    public static void main (String[] args){
        NamesStatistics stats = new NamesStatistics();
        System.out.println(stats.countSubstringInNames("re"));
        stats.countNGramsInNames(2);
    }
}
