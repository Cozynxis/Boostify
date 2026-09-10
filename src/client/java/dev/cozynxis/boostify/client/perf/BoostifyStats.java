package dev.cozynxis.boostify.client.perf;

import java.util.concurrent.atomic.AtomicLong;

public final class BoostifyStats {
    private static final AtomicLong PARTICLES_SKIPPED = new AtomicLong();
    private static final AtomicLong ENTITIES_SKIPPED = new AtomicLong();
    private static final AtomicLong BLOCK_ENTITIES_SKIPPED = new AtomicLong();

    private BoostifyStats() {}

    public static void particleSkipped() { PARTICLES_SKIPPED.incrementAndGet(); }
    public static void entitySkipped() { ENTITIES_SKIPPED.incrementAndGet(); }
    public static void blockEntitySkipped() { BLOCK_ENTITIES_SKIPPED.incrementAndGet(); }

    public static long particlesSkipped() { return PARTICLES_SKIPPED.get(); }
    public static long entitiesSkipped() { return ENTITIES_SKIPPED.get(); }
    public static long blockEntitiesSkipped() { return BLOCK_ENTITIES_SKIPPED.get(); }
}
