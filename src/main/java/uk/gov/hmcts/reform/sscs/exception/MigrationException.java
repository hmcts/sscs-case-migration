package uk.gov.hmcts.reform.sscs.exception;

public class MigrationException extends RuntimeException {
    public MigrationException(String message) {
        super(message);
    }
}
