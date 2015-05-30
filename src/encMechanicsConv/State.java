/*Alessandro Corbetta
 * corbisoft@gmail.com
 * Conv Encoder simulator 1/02/11
 * 
 */

package encMechanicsConv;

public class State implements Comparable<State> {
	public static int getTotStatesLog() {
		return totStatesLog;
	}
	public static boolean isTotStatesLogSet() {
		return totStatesLogSet;
	}
	public static void setTotStatesLog(int totStatesLog) {
		if(!totStatesLogSet){
			State.totStatesLog = totStatesLog;
			totStatesLogSet = true;
		}
		
	}
	
	
	private Integer myState;
	
	public State(Integer myState){
		this.myState = myState;
	}


	private static int totStatesLog;

	private static boolean totStatesLogSet = false;


	@Override
	public int compareTo(State o) {
		// TODO Auto-generated method stub
		return this.myState.compareTo(o.myState);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		State other = (State) obj;
		if (myState == null) {
			if (other.myState != null)
				return false;
		} else if (!myState.equals(other.myState))
			return false;
		return true;
	}


	public Integer getMyState() {
		return myState;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((myState == null) ? 0 : myState.hashCode());
		return result;
	}


	public void setMyState(Integer myState) {
		this.myState = myState;
	}
	

}
