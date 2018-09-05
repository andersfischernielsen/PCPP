class MyAtomicInteger {
    private int value = 0;
    private Object lock = new Object();

    public int addAndGet(int amount) {
        synchronized (lock) {
            value += amount;
            return value;
        }
    }

    public int get() {
        synchronized (lock) {
            return value;
        }
    }
}