package com.shop4me.core.domain.service.processing;

import com.shop4me.core.domain.port.processing.MessageResponseListenersManager;
import com.shop4me.core.domain.port.processing.ResponseListener;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@NoArgsConstructor
public class MessageResponseListenersManagingService implements MessageResponseListenersManager {

    private final ConcurrentHashMap<String, ResponseListener> messageResponseListenersMap = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, String> messageClaimingServicesFingerprints = new ConcurrentHashMap<>();

    @Override
    public ResponseListener getListener(String correlationId){
        return messageResponseListenersMap.get(correlationId);
    }

    @Override
    public String getServiceFingerprint(String correlationId) {
        return messageClaimingServicesFingerprints.get(correlationId);
    }

    @Override
    public void registerListener(String servicesFingerprint,
                                 String correlationId,
                                 ResponseListener listener){
        log.trace("REGISTERING TRACKING FOR: '{}', CLIENT: '{}'", correlationId, servicesFingerprint);
        messageResponseListenersMap.put(correlationId, listener);
        messageClaimingServicesFingerprints.put(correlationId, servicesFingerprint);
    }

    @Override
    public void unregisterListener(String correlationId){
        log.trace("UNREGISTERING TRACKING FOR: '{}'", correlationId);
        messageResponseListenersMap.remove(correlationId);
        messageClaimingServicesFingerprints.remove(correlationId);
    }
}
