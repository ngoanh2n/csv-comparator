package com.github.ngoanh2n.csv;

import com.github.ngoanh2n.Commons;
import com.github.ngoanh2n.Prop;
import com.github.ngoanh2n.RuntimeError;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Parameter;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import io.qameta.allure.util.ResultsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Write Allure results while comparing.
 *
 * @author Ho Huu Ngoan (ngoanh2n@gmail.com)
 */
public class CsvComparisonReport implements CsvComparisonVisitor {
    /**
     * Indicate which attaches CSV sources to Allure report.<br>
     * Default to {@code true}.
     */
    public static final Prop<Boolean> includeSource = Prop.bool("ngoanh2n.csv.includeSource", true);
    /**
     * Indicate which attaches CSV parser settings to Allure report.<br>
     * Default to {@code true}.
     */
    public static final Prop<Boolean> includeSettings = Prop.bool("ngoanh2n.csv.includeSettings", true);
    private static final Logger log = LoggerFactory.getLogger(CsvComparisonReport.class);

    //-------------------------------------------------------------------------------//
    private String uuid;
    private AllureLifecycle lifecycle;

    /**
     * Default constructor.
     */
    public CsvComparisonReport() { /* No implementation necessary */ }

    //-------------------------------------------------------------------------------//

    /**
     * {@inheritDoc}
     */
    @Override
    public void comparisonStarted(CsvComparisonOptions options, File exp, File act) {
        uuid = UUID.randomUUID().toString();
        lifecycle = Allure.getLifecycle();

        List<Parameter> parameters = new ArrayList<>();
        parameters.add(ResultsUtils.createParameter("exp", Commons.getRelative(exp)));
        parameters.add(ResultsUtils.createParameter("act", Commons.getRelative(act)));

        StepResult result = new StepResult().setName("CSV Comparison").setParameters(parameters);
        lifecycle.startStep(uuid, result);

        attachSource(options, exp, act);
        attachSettings(options, exp);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void comparisonFinished(CsvComparisonOptions options, File exp, File act, CsvComparisonResult result) {
        if (result.isDifferent()) {
            lifecycle.updateStep(uuid, sr -> sr.setStatus(Status.FAILED));
        } else {
            lifecycle.updateStep(uuid, sr -> sr.setStatus(Status.PASSED));
        }
        lifecycle.stopStep(uuid);
    }

    //-------------------------------------------------------------------------------//

    private void attachSource(CsvComparisonOptions options, File exp, File act) {
        if (includeSource.getValue()) {
            attachSource(options, exp, "Exp CSV");
            attachSource(options, act, "Act CSV");
        }
    }

    private void attachSource(CsvComparisonOptions options, File file, String fileDesc) {
        ByteArrayOutputStream byteOS = new ByteArrayOutputStream();
        DataOutputStream dataOS = new DataOutputStream(byteOS);

        Charset charset = CsvSource.getCharset(options, file);
        List<String[]> rows = CsvSource.parse(options, file, true).getRows();

        try {
            for (String[] row : rows) {
                String rowLetters = Arrays.toString(row);
                char[] rowChars = rowLetters.toCharArray();

                for (char rowChar : rowChars) {
                    String rowLetter = String.valueOf(rowChar);
                    byte[] letterBytes = rowLetter.getBytes(charset);
                    dataOS.write(letterBytes);
                }
                dataOS.write("\r\n".getBytes(charset));
            }
        } catch (IOException e) {
            String msg = String.format("Write %s to OutputStream", Commons.getRelative(file));
            log.error(msg);
            throw new RuntimeError(msg, e);
        } finally {
            try {
                dataOS.close();
                byteOS.close();
            } catch (IOException e) {
                log.error("Close OutputStream");
            }
        }
        lifecycle.addAttachment(fileDesc, "text/csv", "", byteOS.toByteArray());
    }

    private void attachSettings(CsvComparisonOptions options, File exp) {
        if (includeSettings.getValue()) {
            Charset charset = CsvSource.getCharset(options, exp);
            byte[] bytes = options.parserSettings().toString().getBytes(charset);
            lifecycle.addAttachment("Parser Settings", "text/plain", "", bytes);
        }
    }
}
