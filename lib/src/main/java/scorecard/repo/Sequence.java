package scorecard.repo;

import java.util.concurrent.atomic.AtomicInteger;

public final class Sequence {
    private static final AtomicInteger sequence = new AtomicInteger(0);

    private Sequence() {
    }

    public static int getSequence() {
        return sequence.getAndIncrement();
    }
}
