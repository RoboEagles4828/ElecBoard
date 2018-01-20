package frc.team4828.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Pneumatic {
    Compressor c;
    DoubleSolenoid s;
    Pneumatic(int comp, int sol1, int sol2) {
        c = new Compressor(comp);
        c.setClosedLoopControl(true);
        s = new DoubleSolenoid(sol1, sol2);
    }
    public boolean enabled() {
        return c.enabled();
    }
    public double compVal() {
        return c.getCompressorCurrent();
    }
    public void forward() {
        s.set(DoubleSolenoid.Value.kForward);
    }
    public void reverse() {
        s.set(DoubleSolenoid.Value.kReverse);
    }

}
