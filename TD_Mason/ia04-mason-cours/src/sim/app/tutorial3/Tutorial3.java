/*
  Copyright 2006 by Sean Luke and George Mason University
  Licensed under the Academic Free License version 3.0
  See the file "LICENSE" for more information
*/

package sim.app.tutorial3;
import sim.app.tutorial1and2.Tutorial2;
import sim.display.Console;
import sim.engine.*;
import sim.field.grid.*;
import sim.util.*;


public class Tutorial3 extends SimState
    {
    private static final long serialVersionUID = 1;

    public DoubleGrid2D trails;
    public SparseGrid2D particles;
    
//    public int gridWidth = 100;
//    public int gridHeight = 100;
//    public int numParticles = 500;
    
    public int gridWidth = 40;
    public int gridHeight = 40;
    public int numParticles = 20;
    
    public Tutorial3(long seed)
        {
        super(seed);
        }

    public void start()
        {
        super.start();
        trails = new DoubleGrid2D(gridWidth, gridHeight);
        particles = new SparseGrid2D(gridWidth, gridHeight);
        
        Particle p;
        
        for(int i=0 ; i<numParticles ; i++)
            {
            p = new Particle(random.nextInt(3) - 1, random.nextInt(3) - 1);  // random direction
            schedule.scheduleRepeating(p);
            particles.setObjectLocation(p,
                new Int2D(random.nextInt(gridWidth),random.nextInt(gridHeight)));  // random location
            }
        
        // Schedule the decreaser
        Steppable decreaser = new Steppable()
            {
            private static final long serialVersionUID = 1;
            
            public void step(SimState state)
                {
                // decrease the trails
                trails.multiply(0.9);
                }
            };
            
        schedule.scheduleRepeating(Schedule.EPOCH,2,decreaser,1);
        }

    public static void main(String[] args)
        {
//        doLoop(Tutorial3.class, args);
//        System.exit(0);
        
        Tutorial3 tutorial3 = new Tutorial3(System.currentTimeMillis());
        Tutorial3WithUI gui = new Tutorial3WithUI(tutorial3);
        Console c = new Console(gui);
        c.setVisible(true);
        }    
    }
