package vook.server.api.globalcommon.helper.jwt;

public abstract class JWTHelper {
    protected static <T> T run(CheckedSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FunctionalInterface
    protected interface CheckedSupplier<T> {
        T get() throws Exception;
    }
}
