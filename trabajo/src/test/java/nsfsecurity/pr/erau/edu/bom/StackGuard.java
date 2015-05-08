package nsfsecurity.pr.erau.edu.bom;
// StackGuard.java
// Jedidiah Crandall, crandaj@erau.edu
// Main java source file for the StackGuard applet

import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;

public class StackGuard extends Applet
					implements ActionListener, KeyListener
{
	
	protected Button bPlay, bStop, bStepForward, bReset;
	protected Checkbox cbStep;
	protected TextField typingArea;
	protected static final String PLAY = "play";
	protected static final String STOP = "stop";
	protected static final String STEPFORWARD = "stepforward";   
	protected static final String STEPBACK = "stepback";
	protected static final String RESET = "reset";
   
	protected StackGuardMachineContext m;
	
	int PlayDelay;
	int PCDelay;
		
	public void init() 
	{
		int r, g, b;
		Integer Red = new Integer(0), Green = new Integer(0), Blue = new Integer(0);
		String sRed, sGreen, sBlue;
		String s;
		Integer IPlayDelay;
		Integer IPCDelay;
		
   		m = new StackGuardMachineContext();
		makeContentPane();
		bStop.disable();
		bPlay.enable();
		bReset.disable();
		
		try
		{
			s = new String(this.getParameter("playdelay"));
			IPlayDelay = new Integer(s);
			PlayDelay = IPlayDelay.intValue();
		}
		catch (Exception e)
		{
			PlayDelay = 2750;
		}
		
		try
		{
			s = new String(this.getParameter("pcdelay"));
			IPCDelay = new Integer(s);
			PCDelay = IPCDelay.intValue();
		}
		catch (Exception e)
		{
			PCDelay = 30;
		}

		try
		{
			s = new String(this.getParameter("backgroundcolor"));
			sRed = new String(s.substring(0, 2));
			sGreen = new String(s.substring(2, 4));
			sBlue = new String(s.substring(4, 6));
			r = Red.parseInt(sRed, 16);
			g = Green.parseInt(sGreen, 16);
			b = Blue.parseInt(sBlue, 16);
			m.BackgroundColor = new Color(r, g, b);

			s = new String(this.getParameter("codecolor1"));
			sRed = new String(s.substring(0, 2));
			sGreen = new String(s.substring(2, 4));
			sBlue = new String(s.substring(4, 6));
			r = Red.parseInt(sRed, 16);
			g = Green.parseInt(sGreen, 16);
			b = Blue.parseInt(sBlue, 16);
			m.CodeColor1 = new Color(r, g, b);

			s = new String(this.getParameter("codecolor2"));
			sRed = new String(s.substring(0, 2));
			sGreen = new String(s.substring(2, 4));
			sBlue = new String(s.substring(4, 6));
			r = Red.parseInt(sRed, 16);
			g = Green.parseInt(sGreen, 16);
			b = Blue.parseInt(sBlue, 16);
			m.CodeColor2 = new Color(r, g, b);

			s = new String(this.getParameter("codecolor3"));
			sRed = new String(s.substring(0, 2));
			sGreen = new String(s.substring(2, 4));
			sBlue = new String(s.substring(4, 6));
			r = Red.parseInt(sRed, 16);
			g = Green.parseInt(sGreen, 16);
			b = Blue.parseInt(sBlue, 16);
			m.CodeColor3 = new Color(r, g, b);

			s = new String(this.getParameter("codecolor4"));
			sRed = new String(s.substring(0, 2));
			sGreen = new String(s.substring(2, 4));
			sBlue = new String(s.substring(4, 6));
			r = Red.parseInt(sRed, 16);
			g = Green.parseInt(sGreen, 16);
			b = Blue.parseInt(sBlue, 16);
			m.CodeColor4 = new Color(r, g, b);

			s = new String(this.getParameter("stackcontentscolor"));
			sRed = new String(s.substring(0, 2));
			sGreen = new String(s.substring(2, 4));
			sBlue = new String(s.substring(4, 6));
			r = Red.parseInt(sRed, 16);
			g = Green.parseInt(sGreen, 16);
			b = Blue.parseInt(sBlue, 16);
			m.StackContentsColor = new Color(r, g, b);

			s = new String(this.getParameter("returnpointercolor"));
			sRed = new String(s.substring(0, 2));
			sGreen = new String(s.substring(2, 4));
			sBlue = new String(s.substring(4, 6));
			r = Red.parseInt(sRed, 16);
			g = Green.parseInt(sGreen, 16);
			b = Blue.parseInt(sBlue, 16);
			m.ReturnPointerColor = new Color(r, g, b);
		}
		catch (Exception E)
		{
			// just use the default colors
			m.BackgroundColor = new Color(0, 0, 128);
			m.CodeColor1 = new Color(255, 255, 0);
			m.CodeColor2 = new Color(128, 255, 64);
			m.CodeColor3 = new Color(255, 0, 255);
			m.CodeColor4 = new Color(0, 255, 255);
			m.StackContentsColor = new Color(0, 0, 0);
			m.ReturnPointerColor = new Color(0, 0, 0);
		}
		
		m.Reset();
	}
	
	void makeContentPane()
	{
		cbStep = new Checkbox("Program Counter Delay");
		cbStep.setState(true);
		
   		bPlay = new Button("Play");
     	bPlay.setActionCommand(PLAY);

   		bStop = new Button("Stop");
     	bStop.setActionCommand(STOP);

	   	bStepForward = new Button("Step Forward");
     	bStepForward.setActionCommand(STEPFORWARD);

   		bReset = new Button("Reset");
     	bReset.setActionCommand(RESET);

    	bPlay.addActionListener(this);
		bStop.addActionListener(this);
		bStepForward.addActionListener(this);
		bReset.addActionListener(this);
		
		typingArea = new TextField(30);
		typingArea.addKeyListener(this);
		typingArea.disable();
		

		add(cbStep);
		add(bPlay);
		add(bStop);
		add(bStepForward);
		add(bReset);
		add(typingArea);
	}
	
	protected static final int MAXWIDTH = 750;
	protected static final int MAXHEIGHT = 420;
	protected static final int XADD = 10;
	protected static final int YADD = 40;
	protected static final int XCODEADD = XADD + 20;
	protected static final int YCODEADD = YADD + 20;
	protected static final int CODESPACING = 16;
	protected static final int HIGHLIGHTWIDTH = 360;
	protected static final int XMEMADD = XADD + 460;
	protected static final int YMEMADD = YADD + 124;
	protected static final int MEMSPACING = 16;  
	protected static final int XOUTPUTADD = XMEMADD;
	protected static final int YOUTPUTADD = YCODEADD - 8;
	protected static final int OUTPUTWIDTH = 256;
	protected static final int OUTPUTHEIGHT = 16 * 5;
	protected static final int OUTPUTSPACING = 16;
	
	boolean bMemoryOnly = false;
	
	public void paint(Graphics g)
	{
		if (!bMemoryOnly)
		{
			super.paint(g);
			g.setColor(m.BackgroundColor);
			g.fillRect(XADD, YADD, MAXWIDTH, MAXHEIGHT);
		}
		else
		{
			bMemoryOnly = false;
		}
		update(g);
	}
	
	public void update(Graphics g)
	{	
		Font f2 = new Font("Arial", Font.PLAIN, 12);
		g.setFont(f2);
	
		g.setColor(m.BackgroundColor);
		g.fillRect(XADD + 10, YADD + MAXHEIGHT - 40, MAXWIDTH - 10, 30);
		g.setColor(Color.white);
		g.drawString(m.sExplanation, XADD + 10, YADD + MAXHEIGHT - 20);		
		
		Font f = new Font("Monospaced", Font.PLAIN, 16);
		g.setFont(f);		
		
		int Loop;
		for (Loop = 0; Loop < m.NumCodeLines; Loop++)
		{
			if (Loop == m.HighlightedLine)
			{
				g.setColor(m.Code[Loop].FGColor);
				g.fillRect(XCODEADD, YCODEADD + Loop * CODESPACING - (CODESPACING * 3 / 4), HIGHLIGHTWIDTH, CODESPACING);
				g.setColor(m.BackgroundColor);
			}
			else
			{
				g.setColor(m.BackgroundColor);
				g.fillRect(XCODEADD, YCODEADD + Loop * CODESPACING - (CODESPACING * 3 / 4), HIGHLIGHTWIDTH, CODESPACING);
				g.setColor(m.Code[Loop].FGColor);
			}
			g.drawString(m.Code[Loop].s, XCODEADD, YCODEADD + Loop * CODESPACING);
		}
		
		int XLoop, YLoop;
		String sHex;
		
		for (XLoop = 0; XLoop < 17; XLoop++)
		{
			g.setColor(Color.gray);
			g.drawLine(XMEMADD + XLoop * MEMSPACING, YMEMADD, XMEMADD + XLoop * MEMSPACING, YMEMADD + 256);
		}
		for (YLoop = 0; YLoop < 17; YLoop++)
		{
			g.setColor(Color.gray);
			g.drawLine(XMEMADD, YMEMADD + YLoop * MEMSPACING, XMEMADD + 256, YMEMADD + YLoop * MEMSPACING);
		}
		for (XLoop = 0; XLoop < 16; XLoop++)
		{
			sHex = Integer.toHexString(XLoop);
			sHex = sHex.toUpperCase();
			g.setColor(Color.gray);
			g.drawString(sHex, XMEMADD + XLoop * MEMSPACING + (MEMSPACING / 4), YMEMADD - (MEMSPACING / 4));
		}
		for (YLoop = 0; YLoop < 16; YLoop++)
		{
			sHex = Integer.toHexString(YLoop);
			sHex = sHex.toUpperCase();
			g.setColor(Color.gray);
			g.drawString(sHex, XMEMADD - (MEMSPACING * 3 / 4), YMEMADD + YLoop * MEMSPACING + (MEMSPACING * 3 / 4 + 1));
		}
		
		for (XLoop = 0; XLoop < 16; XLoop++)
			for (YLoop = 0; YLoop < 16; YLoop++)
			{
				g.setColor(m.Memory[YLoop * 16 + XLoop].BGColor);
				g.fillRect(XMEMADD + 1 + XLoop * MEMSPACING, YMEMADD + 1 + YLoop * MEMSPACING, 15, 15);
				g.setColor(m.Memory[YLoop * 16 + XLoop].FGColor);
				g.drawString(m.Memory[YLoop * 16 + XLoop].Contents, XMEMADD + XLoop * MEMSPACING + (MEMSPACING / 4), YMEMADD + YLoop * MEMSPACING + (MEMSPACING * 3 / 4 + 1));
			}
		
		g.setColor(Color.darkGray);
		g.fillRect(XOUTPUTADD, YOUTPUTADD, OUTPUTWIDTH, OUTPUTHEIGHT);
		
		g.setColor(Color.white);
		for (Loop = 0; Loop < 5; Loop++)
		{
			g.drawString(m.Output[Loop], XOUTPUTADD + 2, YOUTPUTADD + Loop * OUTPUTSPACING + (OUTPUTSPACING * 3 / 4));
		}			
		
	}
	
	public class JedsTimer
	{
		private class Runner implements Runnable
		{			
			public void run()
			{
				int i = 1;
				while(i < 0);
			}
		}
		
		public void DelayABit()
		{
			Runner r = new Runner();
			Thread t = new Thread(r);
			t.run();
			try
			{
				t.sleep(100);
			}
			catch (Exception e)
			{
			}
		}
	}
	
	int UserInputIndex;
	boolean bWasPlaying = false;
		
	public void keyTyped(KeyEvent e)
	{
		char c = e.getKeyChar();
		if ((c == '\n') && (typingArea.isEnabled()))
		{
			if (bWasPlaying)
			{
				bWasPlaying = false;
				bPlay.disable();
				bStop.enable();
				bReset.disable();
				bStepForward.disable();
				bPlaying = true;
				MyPlayer.Play();
			}
			else if (!bStepping)
				StepOnce();
		}
		else if (((c >= 'A') && (c <= 'Z')) || ((c >= 'a') && (c <= 'z')))
		{
			Character ch = new Character(c);
			String s = ch.toString();
			s = s.toUpperCase();
			if ((UserInputIndex - m.InputStart) < 25)
			{
				m.sSaveIt = m.sSaveIt.concat(s);
				m.Output[1] = m.Output[1].concat(s);
				m.Memory[UserInputIndex].Contents = s;
				UserInputIndex++;
			}
			else
			{
				typingArea.disable();
			}
			bMemoryOnly = true;
			repaint();
		}
	}
	
	public void keyPressed(KeyEvent e)
	{
	}
	
	public void keyReleased(KeyEvent e)
	{
	}
	
	public void StepOnce()
	{
		boolean ThisIsNotTheEnd;
	
		bStepping = true;
		typingArea.disable();
		ThisIsNotTheEnd = m.StepForward();

		for (PC = m.PCStart; PC <= m.PCStop; PC++)
		{
			if (PC != m.PCStart)
			{
				m.Memory[PC - 1].Contents = " ";
			}
			m.Memory[PC].Contents = "*";
			bMemoryOnly = true;
			update(this.getGraphics());
			if (cbStep.getState())
			{
				JedsTimer MyTimer = new JedsTimer();
				MyTimer.DelayABit();
			}
		} 
		if (m.bNeedInput)
		{
			if (bPlaying)
			{
				bPlaying = false;
				bWasPlaying = true;
				bPlay.enable();
				bStepForward.enable();
				bStop.disable();
				bReset.enable();
			}
			m.sSaveIt = "";
			UserInputIndex = m.InputStart;
			typingArea.enable();
			typingArea.requestFocus();
			m.bNeedInput = false;
		}
		m.Execute();
		
		bMemoryOnly = true;
		repaint();
		typingArea.repaint();
		if (!ThisIsNotTheEnd)
		{
			if (bPlaying)
			{
				bPlaying = false;
			}
			bPlay.disable();
			bStop.disable();
			bStepForward.disable();
			bReset.enable();
			bReset.requestFocus();
		}
		bStepping = false;
	}
		
	boolean bPlaying = false;
	boolean bStepping = false;
	boolean bDelaying = false;
	
	int PC = 0;
	Thread t;
	
	public class JedsPlayer
	{
		private class Runner implements Runnable
		{			
			public void run()
			{
				bDelaying = true;
				while (bPlaying)
				{
					if (!bStepping)
						StepOnce();					
					try 
					{
						t.sleep(PlayDelay);
					}
					catch (Exception e)
					{
					}
				}
				bDelaying = false;
			}
		}
		
		public void Play()
		{
			Runner r = new Runner();
			t = new Thread(r);
			try
			{
				t.start();
			}
			catch (Exception e)
			{
			}
		}
	}
	
	JedsPlayer MyPlayer = new JedsPlayer();
		
 	public void actionPerformed(ActionEvent e) 
	{
    	if (e.getActionCommand().equals(PLAY))
	  	{
			if (!bPlaying)
			{
				bPlaying = true;
				bPlay.disable();
				bStop.enable();
				bReset.disable();
				bStepForward.disable();
				MyPlayer.Play();
				bStop.requestFocus();
			}
     	}
	  	else if (e.getActionCommand().equals(STOP))
		{
			if (bPlaying)
			{
				t.interrupt();
				bPlaying = false;
				bPlay.enable();
				bStop.disable();
				bStepForward.enable();
				bReset.enable();
				bPlay.requestFocus();
			}
		}
	  	else if (e.getActionCommand().equals(STEPFORWARD))
		{
			bReset.enable();
			if (!bStepping)
				StepOnce();
		}
	  	else if (e.getActionCommand().equals(STEPBACK))
		{
		}
	  	else if (e.getActionCommand().equals(RESET))
		{
			if (bPlaying)
			{
				bPlaying = false;
			}
			m.Reset();
			typingArea.setText("");
			typingArea.repaint();
			bMemoryOnly = true;
			repaint();
			bPlay.enable();
			bStop.disable();
			bStepForward.enable();
			bReset.disable();
		}
 	}
}	
