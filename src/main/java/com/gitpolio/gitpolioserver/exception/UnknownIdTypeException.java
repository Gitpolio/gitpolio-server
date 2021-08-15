package com.gitpolio.gitpolioserver.exception;

import com.gitpolio.gitpolioserver.domain.IdType;

public class UnknownIdTypeException extends RuntimeException {
    public UnknownIdTypeException(IdType idType) {
    }
}
