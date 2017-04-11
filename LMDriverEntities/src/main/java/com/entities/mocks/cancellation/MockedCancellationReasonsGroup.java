package com.entities.mocks.cancellation;

import com.entities.cached.cancellation.CancellationReason;
import com.entities.cached.cancellation.CancellationReasonsGroup;


public class MockedCancellationReasonsGroup extends CancellationReasonsGroup {

    public MockedCancellationReasonsGroup() {

        CancellationReason reason = new CancellationReason();
        reason.setId(1);
        reason.setName("Actual cost is too high");
        add(reason);

        reason = new CancellationReason();
        reason.setId(2);
        reason.setName("Actual weight cannot fit");
        add(reason);

        reason = new CancellationReason();
        reason.setId(3);
        reason.setName("Nonlegal package content");
        add(reason);

    }
}
