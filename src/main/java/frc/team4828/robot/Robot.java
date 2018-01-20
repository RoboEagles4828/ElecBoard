package frc.team4828.robot;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.*;

public class Robot extends IterativeRobot {
	Joystick j;
	TalonSRX fl, fr, bl, br;
	Tester t;
	Pneumatic p;
	private boolean ranAuton;
	public void robotInit() {

		CameraServer.getInstance().startAutomaticCapture();

		fl = new TalonSRX(Ports.FRONT_LEFT);
		fr = new TalonSRX(Ports.FRONT_RIGHT);
		bl = new TalonSRX(Ports.BACK_LEFT);
		br = new TalonSRX(Ports.BACK_RIGHT);

		j = new Joystick(Ports.JOYSTICK);

		p = new Pneumatic(Ports.COMPRESSOR, Ports.SOLENOID_LEFT, Ports.SOLENOID_RIGHT);

		t = new Tester();
    }
    
    public void autonomousInit() {
    	System.out.println(" --- Start Auton Init ---");
    	ranAuton = false;
    	System.out.println(" --- Start Auton ---");
    }

    public void autonomousPeriodic() {
    	if(!ranAuton) {
    		t.testMotors(fl, fr, bl, br, .3);
    		ranAuton = true;
    	}
    	Timer.delay(.1);
    }

    public void teleopInit() {
    	System.out.println(" --- Start Teleop Init ---");

    	System.out.println(" --- Start Teleop ---");
    }
    
    public void teleopPeriodic() {

		if(!p.enabled()) {
			if (j.getRawButton(1)) {
				p.forward();
			}
			if (j.getRawButton(2)) {
				p.reverse();
			}
		}
		System.out.println(p.enabled() + " " + p.compVal());
		Timer.delay(.1);
    }
    
    public void testPeriodic() {
		double speed1 = j.getThrottle();
	    double speed2 = j.getThrottle();

		fl.set(ControlMode.PercentOutput, speed1);
		fr.set(ControlMode.PercentOutput, speed2);
		bl.set(ControlMode.PercentOutput, speed1);
		br.set(ControlMode.PercentOutput, speed2);
    }
    
}
