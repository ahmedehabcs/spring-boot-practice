package com.project.project.common;

public record ApiResponse<T> (
        ResponseStatus status,
        String message,
        T data
){ }
