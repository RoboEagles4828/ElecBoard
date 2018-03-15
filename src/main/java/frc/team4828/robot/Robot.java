package frc.team4828.robot;

import edu.wpi.first.wpilibj.*;

public class Robot extends IterativeRobot {

    Joystick j;
    DriveTrain drive;
    private boolean doneAuton;

    public void robotInit() {
        j = new Joystick(Ports.JOYSTICK);
        drive = new DriveTrain(Ports.FRONT_LEFT, Ports.FRONT_RIGHT, Ports.BACK_LEFT, Ports.BACK_RIGHT);
    }

    public void autonomousInit() {
        System.out.println(" --- Start Auton Init ---");
        doneAuton = false;
        drive.reset();
        drive.zeroEnc();
        System.out.println(" --- Start Auton ---");
    }

    public void autonomousPeriodic() {
        if (!doneAuton) {
            drive.moveDistance(72, 0.5);
            Timer.delay(1);
            drive.turnDegAbs(90, 0.5);
            Timer.delay(1);
            drive.moveDistance(36, 0.5);
            Timer.delay(1);
            drive.turnDegAbs(180, 0.5);
            Timer.delay(1);
            drive.moveDistance(72, 0.5);
            Timer.delay(1);
            drive.turnDegAbs(270, 0.5);
            Timer.delay(1);
            drive.moveDistance(36, 0.5);
            Timer.delay(1);
            drive.turnDegAbs(360, 0.5);
            doneAuton = true;
        }
        Timer.delay(.1);
    }

    public void teleopInit() {
        System.out.println(" --- Start Teleop Init ---");

        System.out.println(" --- Start Teleop ---");
    }

    public void teleopPeriodic() {
        drive.arcadeDrive(j.getX(), -j.getY(), j.getTwist());
        drive.updateDashboard();
        Timer.delay(.1);
    }

    public void testInit() {
        System.out.println(" --- Start Test Init ---");

        System.out.println(" --- Start Test ---");
    }

    public void testPeriodic() {
        Timer.delay(.1);
    }

}
