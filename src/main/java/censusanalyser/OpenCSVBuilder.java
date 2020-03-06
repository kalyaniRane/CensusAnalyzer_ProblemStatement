package censusanalyser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.Reader;
import java.util.Iterator;

public class OpenCSVBuilder implements ICSVBuilder {
    public Iterator<ICSVBuilder> getCSVFileIterator(Reader reader, Class csvclass) {
        CsvToBeanBuilder<ICSVBuilder> csvToBeanBuilder = new CsvToBeanBuilder<>(reader);
        csvToBeanBuilder.withType(csvclass);
        csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
        CsvToBean<ICSVBuilder> csvToBean = csvToBeanBuilder.build();
        return csvToBean.iterator();
    }
}
