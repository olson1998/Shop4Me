package com.shop4me.core.domain.port.requesting;

import com.shop4me.core.domain.port.dto.InboundMsg;

public interface MessageReceivingService extends AdminRequestRepository{

    void receiveResponse(InboundMsg inboundMsg);
}
