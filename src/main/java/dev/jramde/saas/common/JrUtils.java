package dev.jramde.saas.common;

public class JrUtils {

    private JrUtils() {
    }


    public static String resolveSchemaName(final String companyCode) {
        return ("tenant_" + companyCode.toLowerCase()).replace("-", "");
    }
}
