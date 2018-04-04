package partio.service;

import java.time.LocalDate;

public class ExpirableObject<T> {

    private LocalDate lastUpdated;
    private T content;

    public ExpirableObject() {
        lastUpdated = LocalDate.MIN;
        content = null;
    }
    
    public ExpirableObject(T content) {
        lastUpdated = LocalDate.now();
        this.content = content;
    }

    public T getContent() throws IllegalStateException {
        if (!lastUpdated.equals(LocalDate.now())) {
            throw new IllegalStateException("json is expired, update first please");
        }
        return content;
    }

    public LocalDate getLastUpdated() {
        return lastUpdated;
    }

    public void setContent(T content) {
        lastUpdated = LocalDate.now();
        this.content = content;
    }

}
