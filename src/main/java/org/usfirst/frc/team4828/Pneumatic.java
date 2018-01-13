package org.usfirst.frc.team4828;

import edu.wpi.first.wpilibj.Compressor;

public class Pneumatic {
    Compressor c;
    Pneumatic(int port) {
        c = new Compressor(port);
    }
}
