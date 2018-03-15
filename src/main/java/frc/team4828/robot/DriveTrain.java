package frc.team4828.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain {

    private TalonSRX fl, fr, bl, br;
    private AHRS navx;

    // MoveDistance Constants
    private static final double ENC_RATIO = 25.464; // [ NU / Inch ] => [ NU / Rotations / 6π ]
    private static final double CORRECTION_FACTOR = 0.01;
    private static final double ANGLE_THRESH = 0.1;
    private static final double ANGLE_CHECK_DELAY = 0.01;
    private static final double TIMEOUT = 10;

    // TurnDegrees Constants
    private static final double TURN_FACTOR = 0.1;

    /**
     * DriveTrain for the robot.
     * 
     * @param leftPorts Ports of the left gearbox.
     * @param rightPorts Ports of the right gearbox.
     * @param shifterPorts Ports of the gear shifter solenoid.
     */
    public DriveTrain(int flPort, int frPort, int blPort, int brPort) {
        fl = new TalonSRX(flPort);
        fr = new TalonSRX(frPort);
        bl = new TalonSRX(blPort);
        br = new TalonSRX(brPort);
        navx = new AHRS(SPI.Port.kMXP);
        navx.reset();
    }

    /**
     * Drives both gearboxes at a given speed.
     * 
     * @param speed The speed.
     */
    public void drive(double speed) {
        fl.set(ControlMode.PercentOutput, speed);
        fr.set(ControlMode.PercentOutput, speed);
        bl.set(ControlMode.PercentOutput, speed);
        br.set(ControlMode.PercentOutput, speed);
    }

    /**
     * Turns in place at a given speed.
     * 
     * @param speed The speed.
     */
    public void turn(double speed) {
        fl.set(ControlMode.PercentOutput, speed);
        fr.set(ControlMode.PercentOutput, -speed);
        bl.set(ControlMode.PercentOutput, speed);
        br.set(ControlMode.PercentOutput, -speed);
    }

    /**
     * Stops driving.
     */
    public void brake() {
        drive(0);
    }

    /**
     * Finds the maximum value of the given inputs.
     * 
     * @param input An array of doubles that is to be read.
     * @return Absolute maximum value.
     */
    private double getMaxAbs(double[] input) {
        double max = 0;
        for (double d : input) {
            max = Math.max(max, Math.abs(d));
        }
        return max;
    }

    /**
     * Scales input so that it does not exceed a given maximum.
     * 
     * @param input A double that is to be normalized.
     * @param max Absolute maximum value of output.
     * @return Normalized value.
     */
    private double normalizeAbs(double input, double max) {
        if (Math.abs(input) > Math.abs(max)) {
            input *= Math.abs(max) / Math.abs(input);
        }
        return input;
    }

    /**
     * Scales inputs so that they do not exceed a given maximum.
     *
     * @param input An array of doubles that is to be normalized.
     * @param max Absolute maximum value of output.
     * @return Normalized array.
     */
    private double[] normalizeAbs(double[] input, double max) {
        double inputMax = getMaxAbs(input);
        if (inputMax > max) {
            for (int i = 0; i < input.length; i++) {
                input[i] *= max / inputMax;
            }
        }
        return input;
    }

    /**
     * Drives the left and right gearboxes at speeds determined by the inputs.
     * <p>
     *
     * @param x The x component (Positive is right; Negative is left).
     * @param y The y component (Positive is up; Negative is down).
     * @param angle The angle (Positive is clockwise; Negative is counterclockwise).
     */
    public void arcadeDrive(double x, double y, double angle) {
        angle /= 2;
        double[] drive = new double[2];
        if (x > 0) {
            drive[0] = y + x + angle;
            drive[1] = y - angle;
        } else {
            drive[0] = y + angle;
            drive[1] = y - x - angle;
        }
        drive = normalizeAbs(drive, 1);
        fl.set(ControlMode.PercentOutput, drive[0]);
        fr.set(ControlMode.PercentOutput, drive[1]);
        bl.set(ControlMode.PercentOutput, drive[0]);
        br.set(ControlMode.PercentOutput, drive[1]);
    }

    /**
     * Moves a given distance forward.
     *
     * @param distance The distance in inches.
     * @param speed The speed.
     */
    public void moveDistance(double distance, double speed) {
        double startTime = Timer.getFPGATimestamp();
        double startEncL = fl.getSelectedSensorPosition(0);
        double startEncR = fr.getSelectedSensorPosition(0);
        double startAngle = navx.getAngle();
        double maxEnc = Math.abs(distance * ENC_RATIO);
        double changeAngle;
        if (distance < 0) {
            speed *= -1;
        }
        while (Timer.getFPGATimestamp() - startTime < TIMEOUT) {
            changeAngle = startAngle - navx.getAngle();
            if (Math.abs(changeAngle) > ANGLE_THRESH) {
                changeAngle = normalizeAbs(changeAngle * CORRECTION_FACTOR, speed);
                if (speed * changeAngle > 0) {
                    fr.set(ControlMode.PercentOutput, speed - changeAngle);
                    br.set(ControlMode.PercentOutput, speed - changeAngle);
                } else {
                    fl.set(ControlMode.PercentOutput, speed + changeAngle);
                    bl.set(ControlMode.PercentOutput, speed + changeAngle);
                }
            } else {
                drive(speed);
            }
            if (Math.abs(fl.getSelectedSensorPosition(0) - startEncL) >= maxEnc
                    || Math.abs(fr.getSelectedSensorPosition(0) - startEncR) >= maxEnc) {
                brake();
                break;
            }
            Timer.delay(ANGLE_CHECK_DELAY);
        }
    }

    /**
     * Turns until it faces a given direction.
     * 
     * @param angle The angle in degrees (Determined by the navx).
     * @param speed The speed.
     */
    public void turnDegAbs(double angle, double speed) {
        double startTime = Timer.getFPGATimestamp();
        double changeAngle;
        while (Timer.getFPGATimestamp() - startTime < TIMEOUT) {
            changeAngle = angle - navx.getAngle();
            if (Math.abs(changeAngle) > ANGLE_THRESH) {
                changeAngle = normalizeAbs(changeAngle * TURN_FACTOR, speed);
                turn(changeAngle);
            } else {
                brake();
                break;
            }
            Timer.delay(ANGLE_CHECK_DELAY);
        }
    }

    /**
     * Resets the encoders to zero.
     */
    public void zeroEnc() {
        fl.setSelectedSensorPosition(0, 0, 10);
        fr.setSelectedSensorPosition(0, 0, 10);
    }

    /**
     * Prints the current values of the encoders.
     */
    public void debugEnc() {
        System.out.println("Left: " + fl.getSelectedSensorPosition(0) + " Right: " + fr.getSelectedSensorPosition(0));
    }

    /**
     * Resets the navx.
     */
    public void reset() {
        navx.reset();
    }

    /**
     * Updates DriveTrain values on SmartDashboard.
     */
    public void updateDashboard() {
        double[] speeds = { fl.getMotorOutputPercent(), fr.getMotorOutputPercent() };
        double[] enc = { fl.getSelectedSensorPosition(0), fr.getSelectedSensorPosition(0) };
        SmartDashboard.putNumberArray("Drive", speeds);
        SmartDashboard.putNumber("Angle", navx.getAngle());
        SmartDashboard.putNumberArray("Encoders", enc);
    }
}