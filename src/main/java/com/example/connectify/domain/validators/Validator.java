package com.example.connectify.domain.validators;

public interface Validator<T> {
    public void validate(T entity) throws ValidationException;
}
