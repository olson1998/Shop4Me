package com.shop4me.core.domain.port.processing;

public interface MessageResponseListenersManager {

    ResponseListener getListener(String correlationId);

    String getServiceFingerprint(String correlationId);

    void registerListener(String servicesFingerprint, String correlationId, ResponseListener listener);

    void unregisterListener(String correlationId);
}
