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

    private Country country;

    public enum Country {INDIA,USA}

    Map<String, CensusDTO> censusMap= null;
    Map<SORTFIELD, Comparator <CensusDTO>> sortMap= null;
    List<CensusDTO> censusList=null;

    public CensusAnalyser() {
        this.sortMap=new HashMap<>();
        this.sortMap.put(SORTFIELD.STATE,Comparator.comparing(census->census.state));
        this.sortMap.put(SORTFIELD.POPULATION,Comparator.comparing(census->census.population));
        this.sortMap.put(SORTFIELD.POPULATIONDENSITY,Comparator.comparing(census->census.populationDensity));
        this.sortMap.put(SORTFIELD.TOTALAREA,Comparator.comparing(census->census.totalArea));
        this.sortMap.put(SORTFIELD.STATECODE,Comparator.comparing(census->census.stateCode));
        this.sortMap.put(SORTFIELD.STATEID,Comparator.comparing(census->census.stateId));
    }

    public int loadCensusData(Country country, String... csvFilePath) throws CensusAnalyserException {
        censusMap=CensusAdapterFactory.getCensusAdapter(country,csvFilePath);
        censusList=censusMap.values().stream().collect(Collectors.toList());

        return censusMap.size();
    }

    public String getStateWiseSortedCensusData(SORTFIELD sortfield) throws CensusAnalyserException {
        if (censusList.size() == 0 || censusList == null)
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        censusList=censusMap.values().stream().collect(Collectors.toList());
        this.sort(this.sortMap.get(sortfield).reversed());
        String sortedStateCensusJson = new Gson().toJson(censusList);
        return sortedStateCensusJson;
    }

    private void sort(Comparator<CensusDTO> censusCSVComparator) {
        for (int i = 0; i < this.censusList.size() - 1; i++) {
            for (int j = 0; j < this.censusList.size() - i - 1; j++) {
                CensusDTO census1 = this.censusList.get(j);
                CensusDTO census2 = this.censusList.get(j + 1);
                if (censusCSVComparator.compare(census1, census2) > 0) {
                    this.censusList.set(j, census2);
                    this.censusList.set(j + 1, census1);
                }
            }
        }
    }

}


