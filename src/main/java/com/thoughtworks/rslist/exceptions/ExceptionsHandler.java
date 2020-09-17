package com.thoughtworks.rslist.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public ResponseEntity<CommentError> handleStartAndEndException() {
        CommentError commentError = new CommentError();
        commentError.setError("invalid request param");
        return ResponseEntity.status(400).body(commentError);
    }

    @ExceptionHandler(RsListIndexOutOfBoundException.class)
    public ResponseEntity<CommentError> handleGetInvalidIndexFromRsListException() {
        CommentError commentError = new CommentError();
        commentError.setError("invalid index");
        return ResponseEntity.status(400).body(commentError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommentError> handleInvalidUserRegisterException() {
        CommentError commentError = new CommentError();
        commentError.setError("invalid user");
        return ResponseEntity.status(400).body(commentError);
    }
}
