package pt.community.java.splitwise_like.exception;

import java.util.List;

public record ErrorResponse(int status, String message, String exceptionName , List<String> errors) {}