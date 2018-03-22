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

    private static final double ENC_RATIO = 75; // [ NU / Inch ] => [ NU / Rotations / 6π ]
    private static final double TIMEOUT = 10;

    // MoveDistance Constants
    private static final double MOVE_ANGLE_FACTOR = 0.1;
    private static final double MOVE_RAMP_FACTOR = 0.05;
    private static final double MOVE_ANGLE_THRESH = 1;
    private static final double MOVE_ENC_THRESH = 50;
    private static final double MOVE_CHECK_DELAY = 0.01;

    // TurnDegrees Constants
    private static final double TURN_FACTOR = 0.2;
    private static final double TURN_ANGLE_THRESH = 1;
    private static final double TURN_CHECK_DELAY = 0.01;

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
     * /** Scales input so that it does not exceed a given maximum.
     * 
     * @param input A double that is to be normalized.
     * @param max Absolute maximum value of output.
     * @return Normalized value.
     */
//    private double normalizeAbs(double input, double factor, double max) {
//        return 2 * max / (1 + Math.pow(Math.E, (-factor * input))) - max;
//    }

    private double normalizeAbs(double input, double factor, double max) {
        input *= factor;
        if (Math.abs(input) > Math.abs(max)) {
            input = Math.abs(max) * Math.signum(input);
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
    public void moveDistance(double distance, double maxSpeed) {
        // Start values
        double startTime = Timer.getFPGATimestamp();
        double startEncL = fl.getSelectedSensorPosition(0);
        double startAngle = navx.getAngle();
        // Current values
        double currentAngle;
        double currentEnc;
        // Target values
        double targetEnc = distance * ENC_RATIO;

        maxSpeed = Math.abs(maxSpeed) * Math.signum(distance); // Ensure max speed has the right sign
        double speed = maxSpeed; // Set current speed to max

        while (Timer.getFPGATimestamp() - startTime < TIMEOUT) { // Loop until break or timeout
            currentAngle = startAngle - navx.getAngle();
            currentEnc = targetEnc - fl.getSelectedSensorPosition(0) + startEncL;
            // Correct angle
            if (Math.abs(currentAngle) > MOVE_ANGLE_THRESH) {
                currentAngle = normalizeAbs(currentAngle, MOVE_ANGLE_FACTOR, speed);
                if (speed * currentAngle > 0) {
                    fl.set(ControlMode.PercentOutput, speed);
                    fr.set(ControlMode.PercentOutput, speed - currentAngle);
                    bl.set(ControlMode.PercentOutput, speed);
                    br.set(ControlMode.PercentOutput, speed - currentAngle);
                } else {
                    fl.set(ControlMode.PercentOutput, speed + currentAngle);
                    fr.set(ControlMode.PercentOutput, speed);
                    bl.set(ControlMode.PercentOutput, speed + currentAngle);
                    br.set(ControlMode.PercentOutput, speed);
                }
            } else {
                drive(speed);
            }
            // Check encoder
            if (Math.abs(currentEnc) > MOVE_ENC_THRESH) {
                speed = normalizeAbs(currentEnc, MOVE_RAMP_FACTOR, maxSpeed);
            } else {
                brake();
                break;
            }
            Timer.delay(MOVE_CHECK_DELAY);
        }
    }

    /**
     * Turns until it faces a given direction.
     * 
     * @param angle The angle in degrees (Determined by the navx).
     * @param speed The speed.
     */
    public void turnDegAbs(double angle, double speed) {
        // Start values
        double startTime = Timer.getFPGATimestamp();
        // Current values
        double currentAngle;

        while (Timer.getFPGATimestamp() - startTime < TIMEOUT) { // Loop until break or timeout
            currentAngle = angle - navx.getAngle();
            // Check angle
            if (Math.abs(currentAngle) > TURN_ANGLE_THRESH) {
                currentAngle = normalizeAbs(currentAngle, TURN_FACTOR, speed);
                turn(currentAngle);
            } else {
                brake();
                break;
            }

            Timer.delay(TURN_CHECK_DELAY);
        }
    }

    /**
     * Resets the encoders to zero.
     */
    public void zeroEnc() {
        fl.setSelectedSensorPosition(0, 0, 10);
    }

    /**
     * Prints the current values of the encoders.
     */
    public void debugEnc() {
        System.out.println("Left: " + fl.getSelectedSensorPosition(0));
    }

    /**
     * Resets the navx.
     */
    public void reset() {
        navx.reset();
    }

    public void debugNavx() {
        System.out.println(navx.getAngle());
    }

    /**
     * Updates DriveTrain values on SmartDashboard.
     */
    public void updateDashboard() {
        double[] speeds = { fl.getMotorOutputPercent(), fr.getMotorOutputPercent() };
        double enc = fl.getSelectedSensorPosition(0);
        SmartDashboard.putNumberArray("Drive", speeds);
        SmartDashboard.putNumber("Angle", navx.getAngle());
        SmartDashboard.putNumber("Encoder", enc);
    }
}