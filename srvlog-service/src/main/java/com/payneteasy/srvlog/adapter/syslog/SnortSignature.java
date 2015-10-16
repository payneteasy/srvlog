package com.payneteasy.srvlog.adapter.syslog;

import com.payneteasy.srvlog.data.SnortLogData;
import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;
import jregex.Matcher;
import jregex.Pattern;

/**
 * Object representation for snort signature.
 *
 * @author imenem
 */
public class SnortSignature {

    /**
     * Regular expression for signature parsing.
     */
    private static final Pattern SIGNATURE_REGEX =
        new Pattern("\\[({GENERATOR_ID}\\d+):({SIGNATURE_ID}\\d+):({SIGNATURE_REVISION}\\d+)\\]");

    /**
     * Url to site with signature descriptions.
     */
    private static final String BASE_DESCRIPTION_URL = "http://rootedyour.com/snortsid";

    /**
     * Parses classification from from barnyard2 syslog_full output plugin message.
     *
     * @param       classification      Classification from from barnyard2 syslog_full output plugin message.
     *
     * @return      Object representation for snort signature.
     */
    public static SnortSignature createSnortSignature(String classification) {
        SnortSignature signature = new SnortSignature();
        Matcher matcher = SIGNATURE_REGEX.matcher(classification);

        if (!matcher.find()) {
            throw new RuntimeException("Classification does not look like snort signature.");
        }

        signature.generatorId = parseInt(matcher.group("GENERATOR_ID"));
        signature.signatureId = parseInt(matcher.group("SIGNATURE_ID"));
        signature.signatureRevision = parseInt(matcher.group("SIGNATURE_REVISION"));

        return signature;
    }

    /**
     * Creates object representation for snort signature from data transfer object.
     *
     * @param       data        Data transfer object.
     *
     * @return      Object representation for snort signature.
     */
    public static SnortSignature createSnortSignature(SnortLogData data) {
        SnortSignature signature = new SnortSignature();

        signature.generatorId = data.getGeneratorId();
        signature.signatureId = data.getSignatureId();
        signature.signatureRevision = data.getSignatureRevision();

        return signature;
    }

    /**
     * Signature generator id.
     */
    private int generatorId;

    /**
     * Signature id.
     */
    private int signatureId;

    /**
     * Signature revision.
     */
    private int signatureRevision;

    /**
     * Returns signature generator id.
     *
     * @return      Signature generator id.
     */
    public int getGeneratorId() {
        return generatorId;
    }

    /**
     * Returns signature id.
     *
     * @return      Signature id.
     */
    public int getSignatureId() {
        return signatureId;
    }

    /**
     * Returns signature revision.
     *
     * @return      Signature revision.
     */
    public int getSignatureRevision() {
        return signatureRevision;
    }

    /**
     * Returns url to page with signature description.
     *
     * @return      Url to page with signature description.
     */
    public String getDescriptionUrl() {
        String sid = valueOf(signatureId);

        if (generatorId != 1) {
            sid += "-" + generatorId;
        }

        return BASE_DESCRIPTION_URL + "?sid=" + sid;
    }

    /**
     * Fills data transfer object by snort signature data.
     *
     * @param       data        Transfer object to fill.
     */
    public void fillSnortLogData(SnortLogData data) {
        data.setGeneratorId(generatorId);
        data.setSignatureId(signatureId);
        data.setSignatureRevision(signatureRevision);
    }

    @Override
    public String toString() {
        return "[" + generatorId + ":" + signatureId + ":" + signatureRevision + "]";
    }

}
