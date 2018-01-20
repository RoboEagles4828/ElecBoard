package frc.team4828.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Timer;

import static com.ctre.phoenix.motorcontrol.ControlMode.*;

public class Tester {
	public void testMotors(TalonSRX m1, TalonSRX m2, TalonSRX m3, TalonSRX m4, double speed) {
		m1.set(PercentOutput,speed);
		m1.set(PercentOutput,speed);
		Timer.delay(1);
		m2.set(PercentOutput,speed);
		Timer.delay(1);
		m3.set(PercentOutput,speed);
		Timer.delay(1);
		m4.set(PercentOutput,speed);
		Timer.delay(1);
		m1.set(PercentOutput,-speed);
		Timer.delay(1);
		m2.set(PercentOutput,-speed);
		Timer.delay(1);
		m3.set(PercentOutput,-speed);
		Timer.delay(1);
		m4.set(PercentOutput,-speed);
		Timer.delay(1);
		m1.set(PercentOutput,0);
		m2.set(PercentOutput,0);
		m3.set(PercentOutput,0);
		m4.set(PercentOutput,0);
		System.out.println("Finish testing motors");
	}
}
