class MyAtomicInteger {
    private int value = 0;

    public int addAndGet(int amount) {
        synchronized (this) {
            value += amount;
            return value;
        }
    }

    public int get() {
        synchronized (this) {
            return value;
        }
    }
}