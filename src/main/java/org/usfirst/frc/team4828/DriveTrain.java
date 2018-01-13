package org.usfirst.frc.team4828;

import static com.ctre.phoenix.motorcontrol.ControlMode.PercentOutput;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

/**
 * Created by RoboEagles on 1/13/2018.
 */
public class DriveTrain {
    private TalonSRX fl;	
    private TalonSRX fr;
    private TalonSRX bl;
    private TalonSRX br;

    DriveTrain(int flPort, int frPort, int blPort, int brPort) {
    	fl = new TalonSRX(flPort);
    	fr = new TalonSRX(frPort);
    	bl = new TalonSRX(blPort);
    	br = new TalonSRX(brPort);
    	
    	
    } 
    
public void  driveRobot(double xComp, double yComp, double rotation) {
	double[] wheelSpeeds = new double[4];
wheelSpeeds[0] = xComp + yComp + rotation;
wheelSpeeds[1] = -xComp + yComp - rotation;
wheelSpeeds[2]  = -xComp + yComp + rotation;
wheelSpeeds[3] = xComp + yComp + rotation;
fr.set(PercentOutput,wheelSpeeds[0]);
fl.set(PercentOutput,wheelSpeeds[1]);
bl.set(PercentOutput,wheelSpeeds[2]);
br.set(PercentOutput,wheelSpeeds[3]);


		
	}

	
}





