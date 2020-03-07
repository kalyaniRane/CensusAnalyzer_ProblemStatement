package censusanalyser;

import java.util.Map;

public class UsCensusAdapter extends CensusAdapter {
    public Map<String, CensusDTO> censusMap;

    @Override
    public Map<String, CensusDTO> loadCensusData(String... csvFilePath) throws CensusAnalyserException {
        censusMap = super.loadCensusData(UsCensusCSV.class, csvFilePath[0]);
        return censusMap;
    }
}