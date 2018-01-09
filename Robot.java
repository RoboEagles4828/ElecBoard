
package org.usfirst.frc.team4828.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends IterativeRobot {
	CANTalon fl, fr, bl, br;
	Tester t;
	private boolean ranAuton;
	public void robotInit() {
		fl = new CANTalon(Ports.FRONT_LEFT);
		fr = new CANTalon(Ports.FRONT_RIGHT);
		bl = new CANTalon(Ports.BACK_LEFT);
		br = new CANTalon(Ports.BACK_RIGHT);
		
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
