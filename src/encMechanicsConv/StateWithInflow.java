/*Alessandro Corbetta
 * corbisoft@gmail.com
 * Conv Encoder simulator 1/02/11
 * 
 */

package encMechanicsConv;

import java.util.Arrays;

public class StateWithInflow extends State {
	public StateWithInflow(Integer myState) {
		super(myState);
		
		// TODO Auto-generated constructor stub
	}

	private StateAndInfoBit[] myInflows;
	public StateAndInfoBit[] getMyInflows() {
		return myInflows;
	}

	private boolean inFlowIsInit = false;
	private int indexVect = 0;
	
	public void setInFlow(StateAndInfoBit inFlow){
		if (!this.inFlowIsInit){
			this.myInflows = new StateAndInfoBit[2];
			inFlowIsInit = true;
		}
		this.myInflows[indexVect++] = inFlow;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer("State: " +this.getMyState().toString() );
		if(inFlowIsInit){
			sb.append(this.myInflows[0] == null ? " first inflow not init " : " first inflow: " + Integer.toBinaryString(this.myInflows[0].getMyCodeWord().intValue())+ " from " + this.myInflows[0].getMyState().getMyState().toString() );
			sb.append(this.myInflows[1] == null ? " second inflow not init " :" second inflow: " + Integer.toBinaryString(this.myInflows[1].getMyCodeWord().intValue())+ " from " + this.myInflows[1].getMyState().getMyState().toString() );
			return new String(sb);
		}else
			return new String(sb.append(" not yet initialized!"));
		
	}
	

}
