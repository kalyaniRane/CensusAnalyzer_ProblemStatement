package censusanalyser;

import com.google.gson.Gson;
import org.apache.commons.collections.map.HashedMap;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CensusAnalyser {
    Map<String, CensusDTO> censusMap;
    List<CensusDTO> indiaCensusDTOList;

    public CensusAnalyser() {
        censusMap = new HashMap<>();
    }

    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        try(Reader reader = Files.newBufferedReader(Paths.get(csvFilePath))) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaCensusCSV> censusCSVIterator = csvBuilder.getCSVFileIterator(reader, IndiaCensusCSV.class);
            while (censusCSVIterator.hasNext()) {
                IndiaCensusCSV indiaCensusCSV = censusCSVIterator.next();
                censusMap.put(indiaCensusCSV.state, new CensusDTO(indiaCensusCSV));
            }
            indiaCensusDTOList = censusMap.values().stream().collect(Collectors.toList());            return censusMap.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }

    public int loadIndianStateCodeData(String csvFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath))) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaStateCodeCSV> stateCodeCSVIterator = csvBuilder.getCSVFileIterator(reader, IndiaStateCodeCSV.class);
            Iterable<IndiaStateCodeCSV> csvIterable = () -> stateCodeCSVIterator;
            StreamSupport.stream(csvIterable.spliterator(), false)
                    .filter(csvState -> censusMap.get(csvState.state) != null)
                    .forEach(csvState -> censusMap.get(csvState.state).stateCode = csvState.stateCode);
            return censusMap.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }catch (IllegalStateException e) {
            throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
        }
    }

    private <E> int getCount(Iterator<E> iterator) {
        Iterable<E> csvIterable = () -> iterator;
        int namOfEntries = (int) StreamSupport.stream(csvIterable.spliterator(), false).count();
        return namOfEntries;
    }

    public String getStateWiseSortedCensusData(String csvFilePath) throws CensusAnalyserException {
        if (indiaCensusDTOList.size() == 0 || indiaCensusDTOList == null)
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        Comparator<CensusDTO> censusCSVComparator = Comparator.comparing(census -> census.state);        this.sort(censusCSVComparator);
        String sortedStateCensusJson = new Gson().toJson(indiaCensusDTOList);
        return sortedStateCensusJson;
    }

    private void sort(Comparator<CensusDTO> censusCSVComparator) {
        for (int i = 0; i < indiaCensusDTOList.size() - 1; i++) {
            for (int j = 0; j < indiaCensusDTOList.size() - i - 1; j++) {
                CensusDTO census1 = indiaCensusDTOList.get(j);
                CensusDTO census2 = indiaCensusDTOList.get(j + 1);
                if (censusCSVComparator.compare(census1, census2) > 0) {
                    indiaCensusDTOList.set(j, census2);
                    indiaCensusDTOList.set(j + 1, census1);
                }
            }
        }
    }

    public int loadUsCensusData(String csvFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath))) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<UsCensusCSV> usCensusCSVIterator = csvBuilder.getCSVFileIterator(reader, UsCensusCSV.class);

            while (usCensusCSVIterator.hasNext()) {
                UsCensusCSV usCensusCSV = usCensusCSVIterator.next();
                censusMap.put(usCensusCSV.state, new CensusDTO(usCensusCSV));
            }
            indiaCensusDTOList = censusMap.values().stream().collect(Collectors.toList());
            return censusMap.size();

        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }

}


