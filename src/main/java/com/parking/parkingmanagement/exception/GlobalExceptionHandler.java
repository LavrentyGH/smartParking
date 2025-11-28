package com.parking.parkingmanagement.exception;

import com.parking.parkingmanagement.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("VALIDATION_ERROR", errorMessage));
    }

    // Специфичные бизнес-исключения
    @ExceptionHandler(CarNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleCarNotFound(CarNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("CAR_NOT_FOUND", e.getMessage()));
    }

    @ExceptionHandler(CarAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleCarAlreadyExists(CarAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT) // 409 более подходит
                .body(ApiResponse.error("CAR_ALREADY_EXISTS", e.getMessage()));
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            IllegalStateException.class
            // Добавьте другие непредвиденные бизнес-ошибки
    })
    public ResponseEntity<ApiResponse<Object>> handleBusinessExceptions(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("BUSINESS_ERROR", ex.getMessage()));
    }

    // Резервный обработчик для непредвиденных RuntimeException
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleUnexpectedRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("INTERNAL_SERVER_ERROR",
                        "Произошла непредвиденная ошибка"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("INTERNAL_SERVER_ERROR",
                        "Внутренняя ошибка сервера"));
    }

    @ExceptionHandler(OwnerNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleOwnerNotFound(OwnerNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("OWNER_NOT_FOUND", e.getMessage()));
    }

    @ExceptionHandler(OwnerAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleOwnerAlreadyExists(OwnerAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error("OWNER_ALREADY_EXISTS", e.getMessage()));
    }

    @ExceptionHandler(ParkingSpotNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleParkingSpotNotFound(ParkingSpotNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("PARKING_SPOT_NOT_FOUND", e.getMessage()));
    }

    @ExceptionHandler(ParkingSpotAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleParkingSpotAlreadyExists(ParkingSpotAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error("PARKING_SPOT_ALREADY_EXISTS", e.getMessage()));
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleReservationNotFound(ReservationNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("RESERVATION_NOT_FOUND", e.getMessage()));
    }

    @ExceptionHandler(ReservationAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleReservationAlreadyExists(ReservationAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error("RESERVATION_ALREADY_EXISTS", e.getMessage()));
    }

    @ExceptionHandler(ParkingSpotNotAvailableException.class)
    public ResponseEntity<ApiResponse<Void>> handleParkingSpotNotAvailable(ParkingSpotNotAvailableException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error("PARKING_SPOT_NOT_AVAILABLE", e.getMessage()));
    }
}