package baseAlghoritms;

public class Coder {
    private final int p = Environ.getInstance().getP();
    private int x;

    public void setX(int x) {
        this.x = x;
    }

    public long apply(int y){
        return SpecialMath.powOnModule(y, x, p);
    }
}
