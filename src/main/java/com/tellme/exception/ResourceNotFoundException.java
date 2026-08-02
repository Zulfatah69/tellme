package com.tellme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a requested resource cannot be found in the data store.
 * Maps to HTTP 404 Not Found.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private final String resourceName;
    private final Object resourceId;

    /**
     * Constructs an exception with a detail message.
     *
     * @param resourceName the name of the resource type (e.g. "User", "Submission")
     * @param resourceId   the identifier that was not found
     */
    public ResourceNotFoundException(String resourceName, Object resourceId) {
        super(String.format("%s with id '%s' was not found", resourceName, resourceId));
        this.resourceName = resourceName;
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public Object getResourceId() {
        return resourceId;
    }
}
