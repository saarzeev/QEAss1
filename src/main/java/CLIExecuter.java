import NamesStatistics.NamesStatistics;

public class CLIExecuter {

    private final int MAX_LENGTH_OF_ARGS = 2;
    private final NamesStatistics namesStatistics;

    private String arg;
    private String param;

    public CLIExecuter(NamesStatistics namesStatistics){
        this.namesStatistics = namesStatistics;
    }

    public void executeFromArgs(String[] args)
    {
        int numOfArgs = args.length;

        boolean isValidNumberOfArgs = (numOfArgs >= 1 && numOfArgs <= MAX_LENGTH_OF_ARGS);

        if(isValidNumberOfArgs){
            parseArgumentAndParameter(args);
            executeRelevantMethod();
        }


    }

    private void parseArgumentAndParameter(String[] args)
    {
        if(args.length >= 1){
            arg = args[0];
        }

        if (args.length >= 2){
            param = args[1];
        }
    }

    //TODO validate that the amount of args actually fit the argument itself.
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
