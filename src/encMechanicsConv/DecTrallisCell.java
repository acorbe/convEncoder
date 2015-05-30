/*Alessandro Corbetta
 * corbisoft@gmail.com
 * Conv Encoder simulator 1/02/11
 * 
 */

package encMechanicsConv;

import java.util.ArrayList;

public class DecTrallisCell {
	private boolean active;
	private int myWholeMetric;
	private DecTrallisCell from;
	private Integer myInfoBit;
	private int myState;
	
	private boolean isActiveForGraph;
	
	public boolean isActiveForGraph() {
		return isActiveForGraph;
	}

	public void setActiveForGraph(boolean isActiveForGraph) {
		this.isActiveForGraph = isActiveForGraph;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public int getMyState(){
		return myState;
	}
	
	
	/*private int getMyPrevState(){
		return this.from
	}*/
	
	public DecTrallisCell getFrom() {
		return from;
	}
	public void setFrom(DecTrallisCell from) {
		this.from = from;
	}
	
	public void setMyState(int myState){
		this.myState = myState;
	}
	
	public Integer getMyInfoBit() {
		return myInfoBit;
	}
	public void setMyInfoBit(Integer myInfoBit) {
		this.myInfoBit = myInfoBit;
		
	}
	public void setStarter(){
		active = true;
		this.myWholeMetric = 0;
		this.from = null;
	}
	
	public void setFake(){
		active = false;
	}
	
	public boolean isActive() {
		return active;
	}
	public int getMyWholeMetric() {
		return myWholeMetric;
	}
	public void setMyWholeMetric(int myWholeMetric) {
		this.myWholeMetric = myWholeMetric;
	}
	
	public void recursivePrint(StringBuffer sb){
		if(from == null){
			//sb.append(this.myInfoBit);
		}else{
			from.recursivePrint(sb);
			sb.append(this.myInfoBit);
		}
	}

}
