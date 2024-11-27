package fr.nemesis07.ankhiastaff.cps;

public class CpsTesting {
    private long startTime = System.currentTimeMillis();
    private int totalClicks = 0;

    public void click() {
        ++this.totalClicks;
    }

    public double getAverageCps() {
        long endTime = System.currentTimeMillis();
        long duration = endTime - this.startTime;
        return (double)this.totalClicks / ((double)duration / 1000.0D);
    }
}