package operatingSystem;

import java.util.LinkedList;

class FrameTable {
	Integer frame[][]; 
	int capacity;
	int cnt;
	public FrameTable() {
		this.capacity=3;
		frame = new Integer[capacity][2];
		for(int i=0; i<capacity; i++) {
			for(int j=0; j<2; j++) {
				if(j%2 == 1)
					frame[i][j] = null;
				else
					frame[i][j] = 0;
			}
		}
		framePointer = 0;
	}
	public FrameTable(int capacity) {
		this.capacity=capacity;
		frame = new Integer[capacity][2];
		for(int i=0; i<capacity; i++) {
			for(int j=0; j<2; j++) {
				if(j%2 == 1)
					frame[i][j] = 0;
				else
					frame[i][j] = null;
			}
		}
		framePointer = 0;
	}
	int GetFrameData(int index) {
		return frame[index][0];
	}	
	
	int GetFrameCount() {
		return cnt;
	}

	int FindIndex(Integer ref) {
		for(int i=0; i<capacity; i++) {
			if(this.frame[i][0] == ref) {
				return i;
			}
		}
		return -1;
	}
	
	Integer Replace(Integer victim, Integer data) {
		int index = FindIndex(victim);
		frame[index][0] = data;
		return victim;
	}
	void Add(int data) {
		if(cnt == capacity)  {
			System.out.println("프레임이 가득참");
			return;
		}
		frame[cnt++][0] = data;
	}	

	boolean isFull() { return cnt >= capacity;}
	String FrameState() {
		String ret = "";
		for(int i=0; i<cnt; i++) {
			ret += frame[i][0] + " ";
		}
		return ret;
	}
	
	int framePointer;
	void SetFramePointer() {
		int i = framePointer;
		while(frame[i][1] != 0) {
			frame[i][1] = 0;
			i = (i+1)%capacity;
		}
		framePointer = i;	
	}

	void SetFramePointer(Integer ref) {
		int i = framePointer;
		while(frame[i][0] != ref) {
			frame[i][1] = 0;
			i = (i+1)%capacity;
		}
		framePointer = i;
		frame[i][1] = 1;
	}
	
	int GetFramePointer() {
		return framePointer;
	}
	Integer Replace(Integer data) {
		Integer victim = frame[framePointer][0];
		frame[framePointer][0] = data;
		frame[framePointer][1] = 1;
		
		if(!isFull()) {
			cnt++;
		}
		return victim; //희생프레임반환
	}

	Integer Replace(int index, Integer data, Integer time) {
		Integer victim = frame[index][0];
		frame[index][0] = data;
		frame[index][1] = time;
		return victim;
	}
	
	void Add(Integer data, Integer time) {
		if(cnt == capacity) {
			System.out.println("프레임이 가득참");
			return;
		}
		frame[cnt][0] = data;
		frame[cnt++][1] = time;
	}
	
	void Update(Integer data, Integer time) {
		int index = FindIndex(data);
		frame[index][1] = time;
	}
	
	int FindIndex_MinTime() {
		int minIndex = 0;
		for(int i=0; i<cnt; i++) {
			if(frame[minIndex][1] > frame[i][1]) {
				minIndex = i;
			}
		}
		return minIndex;
	}
}

class ReplacementPolicy {
	static int FIFO(int[] refSequence, int capacity) {
		LinkedList<Integer> queue = new LinkedList<Integer>();
		FrameTable ft = new FrameTable(capacity);
		int fcnt = 0;
		
		for(int i=0; i<refSequence.length; i++) {
			int f=0;
			if(ft.FindIndex(refSequence[i]) == -1) {
				f = 1;
				fcnt++; 
				queue.offer(refSequence[i]);
				if(ft.isFull()) {
					int victim = queue.poll();
					ft.Replace(victim, refSequence[i]);
				}
				else {
					ft.Add(refSequence[i]);
				}
			}
			System.out.println("f:" + f +" ref:" + refSequence[i] + " frame: " + ft.FrameState());
		}
		return fcnt;
	}
	
	static int LRU(int[] refSequence, int capacity) {
		FrameTable ft = new FrameTable(capacity);
		int fcnt = 0;
		for(int i=0; i<refSequence.length; i++) {
			int f=0;
			if(ft.FindIndex(refSequence[i]) == -1) {
				f = 1; fcnt++;
				if(ft.isFull()) { 
					int victim = ft.FindIndex_MinTime();
					ft.Replace(victim, refSequence[i],i);
				}
				else {
					ft.Add(refSequence[i],i);
				}
			}
			else {
				ft.Update(refSequence[i], i);
			}
			System.out.println("f:" + f +" ref:" + refSequence[i] + " frame: " + ft.FrameState());
		}
		return fcnt;
	}
	
	static int OPT(int[] refSequence, int capacity) {
		FrameTable ft = new FrameTable(capacity);
		int fcnt=0;
		for(int i=0; i<refSequence.length; i++) {
			int f=0;
			if(ft.FindIndex(refSequence[i]) == -1) {
				f = 1; fcnt++;
				if(ft.isFull()) {
					int index[]= {10,10,10};
					int meet[] = {0,0,0};
					int check =0;
					int co =0;
					int victim =0;
					int fi =0;
					int same =0;
				for(int j=i+1;(meet[0]==0||meet[1]==0||meet[2]==0);j++) {
					if(j<20) {
						for(int k=0;k<3;k++) {
							if((ft.frame[k][0] ==refSequence[j])&&meet[k]==0) {
								meet[k]=1;
								index[k] =check;
								check++;
							}
						}
					}
					else {
						for(int e=0;e<2;e++) {
							if(meet[e] ==meet[e+1]) {
								same +=1;
							}
							if(meet[0] ==meet[2]) {
								same +=1;
							}
						}
						if(same ==0) {
							for(int b=0;b<3;b++) {
								if(meet[b] ==0) {
									index[b] =10000;
									meet[b] =1;
								}
							}
						}
						else {
							for(j-=1;(meet[0]==0||meet[1]==0||meet[2]==0);j--) {
								for(int k=0;k<3;k++) {
									if((ft.frame[k][0] ==refSequence[j])&&meet[k]==0) {
										meet[k]=1;
										check++;
										index[k] =check;	
									}
								}
							}
						}
					}
				
					if(meet[0]==1&&meet[1]==1&&meet[2]==1) {
						for(int a=1;a<3;a++) {
							int max=index[0];
							if(max <index[a]) {
								max =index[a];
								fi=a;
							}
						}
						victim =ft.frame[fi][0];
						ft.Replace(victim, refSequence[i]);
					}
				}
				}
				else {
					ft.Add(refSequence[i]);
				}
			}
					
			System.out.println("f:" + f +" ref:" + refSequence[i] + " frame: " + ft.FrameState());
		}
		
		return fcnt;
	}
	
	static int CLOCK(int[] refSequence, int capacity) {
		FrameTable ft = new FrameTable(capacity);
		int fcnt = 0;
		for(int i=0; i<refSequence.length; i++) {
			int f=0;
			if(ft.FindIndex(refSequence[i]) == -1) {
				f = 1; fcnt++;
				ft.SetFramePointer();
				ft.Replace(refSequence[i]);
			}
			else {
				ft.SetFramePointer(refSequence[i]);
			}
			System.out.println("f:" + f +" ref:" + refSequence[i] + " frame: " +
			ft.FrameState() + "framePointer:" + ft.GetFramePointer());
		}
		return fcnt;
	}
}

public class MemoryManagementUnit {
	public static void main(String[] args) {
		System.out.println("");
		int refSequence[] = {1,2,3,2,1,5,2,1,6,2,5,6,3,1,3,6,1,2,4,3};
		System.out.println("OPT >>");
		int optCnt = ReplacementPolicy.OPT(refSequence, 3);
		System.out.println("FIFO >>");
		int fifoCnt = ReplacementPolicy.FIFO(refSequence, 3);
		System.out.println("CLOCK >>");
		int clockCnt = ReplacementPolicy.CLOCK(refSequence, 3);
		System.out.println("LRU >>");
		int lruCnt = ReplacementPolicy.LRU(refSequence, 3);
		
		System.out.println("<Page fault comparason>");
		System.out.println("FIFO:" + fifoCnt);
		System.out.println("LRU:" + lruCnt);
		System.out.println("OPT:" + optCnt);
		System.out.println("CLOCK:" + clockCnt);	}

}
