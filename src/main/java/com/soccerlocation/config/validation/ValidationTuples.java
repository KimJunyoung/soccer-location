package com.soccerlocation.config.validation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Getter
public class ValidationTuples {
        private final String fieldName;
        private final String errorMessage;

}
