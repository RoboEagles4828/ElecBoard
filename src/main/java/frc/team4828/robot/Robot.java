package frc.team4828.robot;

import edu.wpi.first.wpilibj.*;

public class Robot extends IterativeRobot {

    Joystick j;
    DriveTrain drive;
    private boolean doneAuton;
    private Thread dashboardThread;

    public void robotInit() {
        j = new Joystick(Ports.JOYSTICK);
        drive = new DriveTrain(Ports.FRONT_LEFT, Ports.FRONT_RIGHT, Ports.BACK_LEFT, Ports.BACK_RIGHT);
        dashboardThread = new Thread() {
            public void run() {
                while (true) {
                    drive.updateDashboard();
                    Timer.delay(0.1);
                }
            }
        };
        dashboardThread.start();
    }

    public void autonomousInit() {
        System.out.println(" --- Start Auton Init ---");
        doneAuton = false;
        drive.reset();
        drive.zeroEnc();
        drive.debugNavx();
        System.out.println(" --- Start Auton ---");
    }

    public void autonomousPeriodic() {
        if (!doneAuton) {
            drive.moveDistance(108, 0.5);
            drive.debugNavx();
            drive.debugEnc();
            Timer.delay(1);
            drive.turnDegAbs(180, 0.5);
            drive.debugNavx();
            Timer.delay(1);
            drive.moveDistance(108, 0.5);
            drive.debugNavx();
            drive.debugEnc();
            Timer.delay(1);
            drive.turnDegAbs(0, 0.5);
            drive.debugNavx();
            doneAuton = true;
        }
        Timer.delay(.1);
    }

    public void teleopInit() {
        System.out.println(" --- Start Teleop Init ---");
        drive.zeroEnc();
        System.out.println(" --- Start Teleop ---");
    }

    public void teleopPeriodic() {
        drive.arcadeDrive(j.getX(), -j.getY(), j.getTwist());
        drive.updateDashboard();
        Timer.delay(.1);
        if (j.getRawButton(1)) {
            drive.turnDegAbs(0, 0.5);
        }
        if (j.getRawButton(2)) {
            drive.turnDegAbs(180, 0.5);
        }
        if (j.getRawButton(12)) {
            drive.reset();
        }
    }

    public void testInit() {
        System.out.println(" --- Start Test Init ---");

        System.out.println(" --- Start Test ---");
    }

    public void testPeriodic() {
        Timer.delay(.1);
    }

    public void disabledInit() {

    }

}
