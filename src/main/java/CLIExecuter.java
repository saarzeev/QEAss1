import NamesStatistics.NamesStatistics;

public class CLIExecuter {

    private final int MAX_LENGTH_OF_ARGS = 2;
    private final NamesStatistics namesStatistics;

    private String arg;
    private String param;
    private int numOfArgs;
    private String[] args;

    public CLIExecuter(NamesStatistics namesStatistics, String[] args){
        this.namesStatistics = namesStatistics;
        this.args = args;
    }

    public void executeFromArgs()
    {
         numOfArgs = args.length;

        boolean isValidNumberOfArgs = (numOfArgs >= 1 && numOfArgs <= MAX_LENGTH_OF_ARGS);

        if(isValidNumberOfArgs){
            parseArgumentAndParameter();
            executeRelevantMethod();
        }


    }

    private void parseArgumentAndParameter()
    {
        if(args.length >= 1){
            arg = args[0];
        }

        if (args.length >= 2){
            param = args[1];
        }
    }


    private void executeRelevantMethod()
    {
        int n;
        switch (arg)
        {
            case "GenerateName":
                namesStatistics.generateName();
                break;

            case "CountSpecificString":
                namesStatistics.countSubstringInNames(param);
                break;

            case "CountAllStrings":
                if(numOfArgs != 2)
                {
                    break;
                }

                n = -1;

                try
                {
                    n = Integer.parseInt(param);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                namesStatistics.countNGramsInNames(n);
                break;

            case "CountMaxString":
                if(numOfArgs != 2)
                {
                    break;
                }

                n = -1;

                try
                {
                    n = Integer.parseInt(param);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                namesStatistics.getMostPopularNGramInNames(n);
                break;

            case "AllIncludesString":
                namesStatistics.printNamesFoundInString(param);
        }
    }
}
