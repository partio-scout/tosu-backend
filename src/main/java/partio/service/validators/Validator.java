package partio.service.validators;

import java.util.List;
import org.springframework.stereotype.Service;
import partio.domain.Event;

@Service
public abstract class Validator<T> {

    protected static final boolean CAN_NULL = false;
    protected static final boolean NOT_NULL = true;

    public abstract List<String> validateNew(T t);
    public abstract List<String> validateChanges(T original, T changes);
    protected abstract List<String> validateNewAndOld(T t);
    
    protected boolean validateStringLength(String toValidate, int min, int max, boolean notNull) {
        if (toValidate == null || toValidate.isEmpty()) {
            return notNull == CAN_NULL;
        }
        return !(toValidate.length() < min || toValidate.length() > max);
    }
    
    protected boolean validateStringNotOnlySpaces(String toValidate, boolean notNull) {
        if (toValidate == null || toValidate.isEmpty()) {
            return notNull == CAN_NULL;
        }
        return toValidate.trim().length() > 0;
    }
}
