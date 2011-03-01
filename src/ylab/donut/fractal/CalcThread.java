package ylab.donut.fractal;

interface FractalCalc{
	public void calc();
	public boolean isDone();
}

public class CalcThread implements Runnable{
	Thread th = null;
	FractalCalc fractalcalc;
	public void start(FractalCalc calc){
		fractalcalc = calc;
		th = new Thread(this);
		th.start();
	}
	
	public void run(){
		while(th!=null){
			try{
				if(!fractalcalc.isDone()){
					fractalcalc.calc();
				}
				Thread.sleep(200);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void stop(){
		th = null;
	}
}

