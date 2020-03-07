package censusanalyser;

import java.util.Map;

public class CensusAdapterFactory{
    public static Map<String, CensusDTO> getCensusAdapter(CensusAnalyser.Country country, String... csvFilePath) throws CensusAnalyserException {
        if (country.equals(CensusAnalyser.Country.INDIA))
            return new IndianCensusAdapter().loadCensusData(csvFilePath);
        else if (country.equals(CensusAnalyser.Country.USA))
            return new UsCensusAdapter().loadCensusData(csvFilePath);
        throw new CensusAnalyserException("Invalid Country",
                CensusAnalyserException.ExceptionType.INVALID_COUNTRY);
    }
}
