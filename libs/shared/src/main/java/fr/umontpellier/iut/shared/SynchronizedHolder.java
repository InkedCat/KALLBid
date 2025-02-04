package fr.umontpellier.iut.shared;

import java.util.Optional;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SynchronizedHolder<T> {
    private T object;
    private final Semaphore semaphore = new Semaphore(0);
    private int timeout;

    public SynchronizedHolder(int timeoutSeconds) {
        object = null;
        if(timeoutSeconds < 0) throw new IllegalArgumentException("timeout doit être positif");
        timeout = timeoutSeconds;
    }

    public void reset() {
        semaphore.drainPermits();
        object = null;
    }

    public void setObject(T updatedObject) {
        if(object == null) {
            this.object = updatedObject;
            semaphore.release();
        }
    }

    public T getObject() throws InterruptedException {
        semaphore.acquire();
        semaphore.release();

        return object;
    }

    public Optional<T> getObjectWait() throws InterruptedException {
        if(semaphore.tryAcquire(timeout, TimeUnit.SECONDS)) {
            semaphore.release();
            return Optional.of(object);
        }

        return Optional.empty();
    }

    public int getAvailablePermits() {
        return semaphore.availablePermits();
    }
}
