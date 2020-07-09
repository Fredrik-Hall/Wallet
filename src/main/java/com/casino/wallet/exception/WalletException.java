package com.casino.wallet.exception;

import com.casino.wallet.config.ExceptionConfig;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Optional;

@Component
public class WalletException {

    private static ExceptionConfig exceptionConfig;

    public WalletException(ExceptionConfig exceptionConfig){
        WalletException.exceptionConfig = exceptionConfig;
    }

    public static RuntimeException throwException(EntityType entityType, ExceptionType exceptionType, String... args) {
        String messageTemplate = getMessageTemplate(entityType, exceptionType);
        return throwException(exceptionType, messageTemplate, args);
    }

    public static class EntityNotFoundException extends RuntimeException {
        public EntityNotFoundException(String message) {
            super(message);
        }
    }

    public static class DuplicateEntityException extends RuntimeException {
        public DuplicateEntityException(String message) {
            super(message);
        }
    }

    public static class NotEnoughFundsException extends RuntimeException {
        public NotEnoughFundsException(String message) {
            super(message);
        }
    }

    public static class InternalException extends RuntimeException {
        public InternalException(String message) {
            super(message);
        }
    }

    public static class NotAllowedException extends RuntimeException {
        public NotAllowedException(String message) {super(message); }
    }

    private static RuntimeException throwException(ExceptionType exceptionType, String messageTemplate, String... args) {
        if (ExceptionType.ENTITY_NOT_FOUND.equals(exceptionType)) {
            return new EntityNotFoundException(format(messageTemplate, args));
        } else if (ExceptionType.DUPLICATE_ENTITY.equals(exceptionType)) {
            return new DuplicateEntityException(format(messageTemplate, args));
        } else if (ExceptionType.INTERNAL_EXCEPTION.equals(exceptionType)){
            return new InternalException(format(messageTemplate, args));
        } else if(ExceptionType.NOT_ENOUGH_FUNDS.equals(exceptionType)){
            return new NotEnoughFundsException(format(messageTemplate, args));
        }
        return new RuntimeException(format(messageTemplate, args));
    }


    private static String getMessageTemplate(EntityType entityType, ExceptionType exceptionType) {
        return entityType.name().concat(".").concat(exceptionType.getValue()).toLowerCase();
    }

    private static String format(String template, String... args) {
        Optional<String> templateContent = Optional.ofNullable(exceptionConfig.getConfigValue(template));
        return templateContent.map(s -> MessageFormat.format(s, (Object) args)).orElseGet(() -> String.format(template, (Object) args));
    }
}
