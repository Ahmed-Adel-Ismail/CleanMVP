package com.entities.mocks.cancellation;

import com.entities.cached.cancellation.CancellationReason;

public class MockedCancellationReason extends CancellationReason {

    public MockedCancellationReason() {
        super.id = (long) (Math.random() * 5000);
        super.name = "[" + id + "] name";
    }
}
