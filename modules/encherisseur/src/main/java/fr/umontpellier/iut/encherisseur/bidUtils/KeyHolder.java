package fr.umontpellier.iut.encherisseur.bidUtils;

import fr.umontpellier.iut.shared.SynchronizedHolder;

import java.security.Key;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class KeyHolder<T extends Key> {

    private SynchronizedHolder<T> holder;
    private int expireTime;
    private ScheduledExecutorService expireService;
    private ScheduledFuture<?> expireTask;

    public KeyHolder(int expireTime, int timeout) {
        holder = new SynchronizedHolder<>(timeout);
        if(expireTime < 0) throw new IllegalArgumentException("expireTime doit être positif");
        this.expireTime = expireTime;

        expireService = Executors.newSingleThreadScheduledExecutor();
    }

    public void setKey(T key) {
        holder.reset();
        holder.setObject(key);

        if (expireTask != null && !expireTask.isDone()) {
            expireTask.cancel(true);
        }

        expireTask = expireService.schedule(new KeyExpire(), expireTime, TimeUnit.MINUTES);
    }

    public T getKey() throws InterruptedException {
        return holder.getObject();
    }

    public Optional<T> getKeyWait() throws InterruptedException {
        return holder.getObjectWait();
    }

    public synchronized boolean hasExpired() {
        return holder.getAvailablePermits() == 0;
    }

    private class KeyExpire implements Runnable {
        @Override
        public void run() {
            holder.reset();
        }
    }
}
