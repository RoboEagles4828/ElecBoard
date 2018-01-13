package org.usfirst.frc.team4828;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.*;

public class Robot extends IterativeRobot {
	TalonSRX fl, fr, bl, br;
	Tester t;
	private boolean ranAuton;
	public void robotInit() {
		fl = new TalonSRX(Ports.FRONT_LEFT);
		fr = new TalonSRX(Ports.FRONT_RIGHT);
		bl = new TalonSRX(Ports.BACK_LEFT);
		br = new TalonSRX(Ports.BACK_RIGHT);
		
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

    }
    
    public void testPeriodic() {

    }
    
}
