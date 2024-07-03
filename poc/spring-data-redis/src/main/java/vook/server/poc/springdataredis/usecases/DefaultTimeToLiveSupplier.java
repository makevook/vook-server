package vook.server.poc.springdataredis.usecases;

import org.springframework.stereotype.Component;

@Component
public class DefaultTimeToLiveSupplier implements TimeToLiveSupplier {
    @Override
    public long get() {
        return 3600L;
    }
}
