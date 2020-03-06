package censusanalyser;

public class CSVBuilderException extends RuntimeException {

    enum ExceptionType {
        UNABLE_TO_PARSE, CENSUS_FILE_PROBLEM
    }

    CSVBuilderException.ExceptionType type;

    public CSVBuilderException(String message, CSVBuilderException.ExceptionType type) {
        super(message);
        this.type = type;
    }
}

