package org.usfirst.frc.team4828.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Timer;

public class Tester {
	public void testMotors(CANTalon m1, CANTalon m2, CANTalon m3, CANTalon m4, double speed) {
		m1.set(speed);
		Timer.delay(1);
		m2.set(speed);
		Timer.delay(1);
		m3.set(speed);
		Timer.delay(1);
		m4.set(speed);
		Timer.delay(1);
		m1.set(-speed);
		Timer.delay(1);
		m2.set(-speed);
		Timer.delay(1);
		m3.set(-speed);
		Timer.delay(1);
		m4.set(-speed);
		Timer.delay(1);
		m1.set(0);
		m2.set(0);
		m3.set(0);
		m4.set(0);
		System.out.println("Finish testing motors");
	}
}
