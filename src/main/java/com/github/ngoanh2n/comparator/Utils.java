package com.github.ngoanh2n.comparator;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.mozilla.universalchardet.UniversalDetector;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <h3>csv-comparator<h3>
 * <a href="https://github.com/ngoanh2n/csv-comparator">https://github.com/ngoanh2n/csv-comparator<a>
 * <br>
 *
 * @author Ho Huu Ngoan (ngoanh2n@gmail.com)
 * @since 1.0.0
 */
class Utils {

    static String timeStamp() {
        //noinspection SpellCheckingInspection
        return new SimpleDateFormat("yyyyMMdd.HHmmss.SSS").format(new Date());
    }

    static String charsetOf(@Nonnull File file) throws IOException {
        return UniversalDetector.detectCharset(file);
    }

    @CanIgnoreReturnValue
    public static Path createsDirectory(@Nonnull Path location) {
        Iterator<Path> elements = location.iterator();
        Path parentElement = Paths.get("");

        while (elements.hasNext()) {
            parentElement = parentElement.resolve(elements.next());
            //noinspection ResultOfMethodCallIgnored
            parentElement.toFile().mkdirs();
        }
        return location;
    }

    static List<String[]> read(@Nonnull File csv, @Nonnull Charset encoding, @Nonnull CsvParserSettings settings) {
        checkNotNull(csv, "csv cannot be null");
        checkNotNull(encoding, "encoding cannot be null");
        checkNotNull(settings, "settings cannot be null");
        return new CsvParser(settings).parseAll(csv, encoding);
    }
}
