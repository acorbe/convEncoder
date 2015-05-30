/*Alessandro Corbetta
 * corbisoft@gmail.com
 * Conv Encoder simulator 1/02/11
 * 
 */

package encMechanicsConv;

public class StateAndInfoBit implements Comparable<StateAndInfoBit>{
	
	private State myState;
	private Integer myInfoBit;
	private Integer myCodeWord;
	static int codeWordbitCount;
	
	public State getMyState() {
		return myState;
	}


	

	public Integer getMyInfoBit() {
		return myInfoBit;
	}


	public Integer getMyCodeWord() {
		return myCodeWord;
	}
	
	public String getMyCodeWordStr(){
		int bits[] = new int[codeWordbitCount];
		int myCdWrd = this.myCodeWord.intValue();
		StringBuffer sb = new StringBuffer();
		for(int i=0 ; i < codeWordbitCount; i++){
			bits[i] = myCdWrd%2;
			myCdWrd >>= 1;			
		}
		for(int i=codeWordbitCount-1; i>= 0  ; i--){
			sb.append(bits[i]);
		}
		return new String(sb);
		
	}
	

	public StateAndInfoBit(Integer myStateInt, Integer myInfoBit){
		this.myState = new State(myStateInt);
		this.myInfoBit = myInfoBit;
		
	}
	public StateAndInfoBit(Integer myStateInt, Integer myInfoBit, Integer myCodeWord,int totCodeBit){
		this(myStateInt,myInfoBit);
		this.myCodeWord = myCodeWord;
		codeWordbitCount = totCodeBit;
		
	}
	
	public StateAndInfoBit(State myStateInt, Integer myInfoBit){
		this.myState =  myStateInt;
		this.myInfoBit = myInfoBit;
	}
	


	@Override
	public int compareTo(StateAndInfoBit arg0) {
		// TODO Auto-generated method stub
		int  compSt;
		if( (  compSt = myState.compareTo(arg0.myState)) !=0)
			return compSt;
		else
			return this.myInfoBit.compareTo(arg0.myInfoBit);	
	}
	

}
