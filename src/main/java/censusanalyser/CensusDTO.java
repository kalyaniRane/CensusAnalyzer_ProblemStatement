package censusanalyser;

public class CensusDTO {
    public String state;
    public double population;
    public double totalArea;
    public double densityPerSqKm;
    public String stateCode;
    public double populationDensity;
    public String stateId;

    public CensusDTO(IndiaCensusCSV indiaCensusCSV) {
        state = indiaCensusCSV.state;
        population = indiaCensusCSV.population;
        totalArea = indiaCensusCSV.areaInSqKm;
        densityPerSqKm = indiaCensusCSV.densityPerSqKm;
    }
    public CensusDTO(UsCensusCSV UsCensusCSV) {
        state = UsCensusCSV.state;
        stateId=UsCensusCSV.stateId;
        population = UsCensusCSV.population;
        totalArea = UsCensusCSV.totalArea;
        densityPerSqKm = UsCensusCSV.totalArea;
        populationDensity=UsCensusCSV.populationDensity;
    }
}