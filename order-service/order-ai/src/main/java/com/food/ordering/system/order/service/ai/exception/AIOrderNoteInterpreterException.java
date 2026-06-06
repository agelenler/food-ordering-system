package com.food.ordering.system.order.service.ai.exception;

public class AIOrderNoteInterpreterException extends RuntimeException {

    public AIOrderNoteInterpreterException(String message) {
        super(message);
    }

    public AIOrderNoteInterpreterException(String message, Throwable cause) {
        super(message, cause);
    }
}
