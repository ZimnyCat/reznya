package zimnycat.reznya.libs;

public class Delay {
    private long delay = 1;
    private long last;

    public Delay() { this.last = System.currentTimeMillis(); }

    public boolean check() {
        if ((System.currentTimeMillis() - last) < delay) return false;
        last = System.currentTimeMillis();
        return true;
    }

    public void setDelay(long delay) { this.delay = delay; }
}
