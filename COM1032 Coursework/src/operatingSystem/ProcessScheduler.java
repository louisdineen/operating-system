package operatingSystem;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.*;

class Resource {
	static Integer time=0;
	static Queue ReadyQueue = new Queue();
	static Queue CompletedQueue = new Queue();
	static boolean preempted = false;
	static PCB currentPCB;
	static boolean finished = false;	
	static int temp_time =0;
	static int cnt = 0 ;
	static PCB temp_pcb =null; 
}
class Queue extends LinkedList<PCB> {
	public boolean equals(LinkedList<PCB> obj) {
		if(obj.size() != this.size()) {
			return false;
		}
		for(int i=0; i<this.size(); i++) {
			if(!obj.get(i).equals(this.get(i))) {
				return false;
			}
		}
		return true;
	}
}

class PCB implements Comparable<PCB> {
	int startTime;
	int endTime;
	int arrive;
	int id;
	int pri;
	int bust;
	int remain;
	int waittime;
	double key;
	
	PCB(int arrive, int id, int pri, int bust) {
		this.arrive = arrive; 
		this.id = id; this.pri = pri; this.bust = bust; this.remain = bust;
	}
	
	public int compareTo(PCB ob) {
		double targetKey = ob.key;
		if(key == targetKey) return 0;
		else if(key > targetKey) return 1;
		else return -1;
	}
	
	public boolean equals(PCB ob) {
		if(ob.id != this.id) 
			return false;
		return true;
	}
}

class WorkTimer extends Thread {
	int msec;
	WorkTimer() {
		this.msec = 100;
	}
	WorkTimer(int msec) {
		this.msec = msec;
	}
	public void run() {
		Resource.finished = false;
		PCB p1 = new PCB(0, 1, 3,10);
		PCB p2 = new PCB(1, 2, 2,28);
		PCB p3 = new PCB(2, 3, 4, 6);
		PCB p4 = new PCB(3, 4, 1, 4);
		PCB p5 = new PCB(4, 5, 2,14);
		
		int endtime = p1.bust + p2.bust + p3.bust + p4.bust + p5.bust;
		while(endtime >= Resource.time)
		{
			try {
				if(Resource.time == p1.arrive) {
					synchronized(Resource.ReadyQueue) {
						Resource.ReadyQueue.offer(p1);
					}
				}
				if(Resource.time == p2.arrive) {
					synchronized(Resource.ReadyQueue) {
						Resource.ReadyQueue.offer(p2);
					}
				}
				if(Resource.time == p3.arrive) {
					synchronized(Resource.ReadyQueue) {
						Resource.ReadyQueue.offer(p3);
					}
				}
				if(Resource.time == p4.arrive) {
					synchronized(Resource.ReadyQueue) {
						Resource.ReadyQueue.offer(p4);
					}
				}
				if(Resource.time == p5.arrive) {
					synchronized(Resource.ReadyQueue) {
						Resource.ReadyQueue.offer(p5);
					}
				}
				Thread.sleep(msec);
				Resource.time++; 
				synchronized(Resource.ReadyQueue) {
					for(PCB p : Resource.ReadyQueue) {
						p.waittime++;
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		Resource.finished = true;
		for(PCB p : Resource.CompletedQueue) {
			p.key = p.id;
		}
		
		Resource.CompletedQueue.sort(Comparator.naturalOrder());
		System.out.println();
		
		double totalwait = 0, totalturn = 0; int size = Resource.CompletedQueue.size(); 
		for(PCB p : Resource.CompletedQueue) {
			System.out.println("p" + p.id + ")     wait:" + p.waittime + 
					                          "\treturn:" + (p.endTime-p.arrive));
			
			totalwait += p.waittime;
			totalturn += p.endTime-p.arrive;
		}
		System.out.println("avg wait:" + (totalwait/size) + "\tavg return:" + (totalturn/size));
	}
}

class CPU_Scheduler extends Thread {
	final static int FCFS = 0;
	final static int SJF = 1;
	final static int PRIORITY = 2;
	final static int HRN = 3;
	final static int RR = 4;
	
	int type = FCFS;
	boolean PreemptionMode = false;
	CPU_Scheduler() {
		type = FCFS;
	}
	
	CPU_Scheduler(int type, boolean PreemtionMode) {
		this.type = type;
		this.PreemptionMode = PreemtionMode;
	}
	
	void SortSJF() {
		synchronized(Resource.ReadyQueue) {
			for(PCB p : Resource.ReadyQueue) {
				p.key = p.bust;
			}
			Resource.ReadyQueue.sort(Comparator.naturalOrder());
		}
		if(PreemptionMode) {
			synchronized(Resource.ReadyQueue) {
				if(Resource.ReadyQueue.peek() != null && Resource.currentPCB != null) {
					if(Resource.currentPCB.remain > Resource.ReadyQueue.peek().remain) {
						Resource.preempted = true;
						for(PCB p : Resource.ReadyQueue) {
							p.key = p.remain;
						}
						Resource.ReadyQueue.sort(Comparator.naturalOrder());
					}
				}
			}
		}
	}

	void SortPRI() {
		synchronized(Resource.ReadyQueue)
		{
			for(PCB p : Resource.ReadyQueue) {
				p.key = p.pri;
			}
			Resource.ReadyQueue.sort(Comparator.reverseOrder());
		}
		if(PreemptionMode) {
			synchronized(Resource.ReadyQueue) {
				if(Resource.ReadyQueue.peek() != null && Resource.currentPCB != null) {
					if(Resource.currentPCB.pri < Resource.ReadyQueue.peek().pri) {
						Resource.preempted = true;
						for(PCB p : Resource.ReadyQueue) {
							p.key = p.pri;
						}
						Resource.ReadyQueue.sort(Comparator.reverseOrder());
					}
				}
			}
		}
	}
	
	void SortHRN() {		
		synchronized(Resource.ReadyQueue) {
			for(PCB p : Resource.ReadyQueue) {
				int a = p.bust + p.waittime;
				int pri = a / p.bust;
				p.key = pri;
			}
			Resource.ReadyQueue.sort(Comparator.reverseOrder());
		}
	}
	
	void SortRR() {
		synchronized(Resource.ReadyQueue) {	
			if(Resource.currentPCB != null && Resource.temp_time != Resource.time) {		       
				if(Resource.temp_pcb != null && Resource.currentPCB != null) {
				   if(!Resource.temp_pcb.equals(Resource.currentPCB)) {
					   Resource.cnt=0;
					   Resource.temp_pcb = Resource.currentPCB;
				      }
				}
				    Resource.cnt++;
			        		       
				if(Resource.currentPCB.remain!=0) {
			       if(Resource.cnt==5 && Resource.currentPCB.remain>=5) {			  
			    	   Resource.cnt=0;
			    	   Resource.preempted = true;
			       }else if(Resource.currentPCB.remain<5 && Resource.cnt==1) {
			    	   Resource.temp_pcb = Resource.currentPCB;
			       }
				     Resource.temp_time=Resource.time;		
				}    
			  }
			}
		}

	public void run() {
		Queue copyQueue = (Queue) Resource.ReadyQueue.clone();
		int time = Resource.time;
		while(!Resource.finished) {
			try {
				synchronized(Resource.ReadyQueue) {
					if(type == HRN && time != Resource.time) {
						time = Resource.time;
						SortHRN(); 
					}
					else if(!copyQueue.equals(Resource.ReadyQueue) && type != FCFS && type !=RR) {
						copyQueue = (Queue) Resource.ReadyQueue.clone();
						switch(type)
						{
						case SJF : SortSJF(); break;
						case PRIORITY : SortPRI(); break;
						case RR : SortRR(); break;
						}
					}
					else if(type == RR ) {     
						switch(type)
						{
						case RR : SortRR(); break;
						}
					}
				}
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

class MyProcessor extends Thread {
	public void run() {
		System.out.print("0");
		while(!Resource.finished) {
			try {
				synchronized (Resource.ReadyQueue) {
					Resource.currentPCB = Resource.ReadyQueue.poll();
				}
				if(Resource.currentPCB != null) { 
					synchronized (Resource.currentPCB) {
						Resource.currentPCB.startTime = Resource.time;
					}
					while(true) {
						int runtime;
						synchronized (Resource.currentPCB) {
							runtime = Resource.time - Resource.currentPCB.startTime;
						}
						if(Resource.currentPCB.remain == runtime || Resource.preempted) {
							System.out.print("--p" + Resource.currentPCB.id 
									+ "--" + Resource.time) ;
							Resource.preempted = false;
							synchronized (Resource.currentPCB) {
								Resource.currentPCB.remain -= runtime;
							}
							if(Resource.currentPCB.remain > 0) {
								synchronized(Resource.ReadyQueue) {
									PCB t = Resource.currentPCB;
									Resource.currentPCB = null;
									Resource.ReadyQueue.offer(t);
								}								
							}
							else {
								Resource.currentPCB.endTime = Resource.time;
								Resource.CompletedQueue.offer(Resource.currentPCB);
								Resource.currentPCB = null;
							}
							break;
						}
						Thread.sleep(10);
					}
				}
				Thread.sleep(10);		
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

public class ProcessScheduler{
	public static void main(String[] args) {
		System.out.println("");
		CPU_Scheduler sche = new CPU_Scheduler(CPU_Scheduler.RR, false);
		MyProcessor process = new MyProcessor();
		WorkTimer timer = new WorkTimer(50);
		sche.start();
		process.start();
		timer.start();
	}
}