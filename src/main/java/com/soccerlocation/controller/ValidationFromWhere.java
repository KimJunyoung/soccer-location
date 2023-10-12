package com.soccerlocation.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ValidationFromWhere {

    private final String field;
    private final String message;
}
