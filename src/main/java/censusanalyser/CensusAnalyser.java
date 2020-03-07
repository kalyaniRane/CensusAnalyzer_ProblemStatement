package censusanalyser;

import com.google.gson.Gson;
//import org.apache.commons.collections.map.HashedMap;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CensusAnalyser {

    public enum Country {INDIA,USA}

    Map<String, CensusDTO> censusMap= null;
    List<CensusDTO> censusList=null;

    public CensusAnalyser() {

    }

    public int loadCensusData(Country country, String... csvFilePath) throws CensusAnalyserException {
        censusMap=CensusAdapterFactory.getCensusAdapter(country,csvFilePath);
        censusList=censusMap.values().stream().collect(Collectors.toList());

        return censusMap.size();
    }

    public String getStateWiseSortedCensusData(String csvFilePath) throws CensusAnalyserException {
        if (censusList.size() == 0 || censusList == null)
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        Comparator<CensusDTO> censusCSVComparator = Comparator.comparing(census -> census.state);
        this.sort(censusCSVComparator);
        String sortedStateCensusJson = new Gson().toJson(censusList);
        return sortedStateCensusJson;
    }

    private void sort(Comparator<CensusDTO> censusCSVComparator) {
        for (int i = 0; i < censusList.size() - 1; i++) {
            for (int j = 0; j < censusList.size() - i - 1; j++) {
                CensusDTO census1 = censusList.get(j);
                CensusDTO census2 = censusList.get(j + 1);
                if (censusCSVComparator.compare(census1, census2) > 0) {
                    censusList.set(j, census2);
                    censusList.set(j + 1, census1);
                }
            }
        }
    }

}


