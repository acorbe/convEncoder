/*Alessandro Corbetta
 * corbisoft@gmail.com
 * Conv Encoder simulator 1/02/11
 * 
 */

package encMechanicsConv;

import java.util.ArrayList;
import java.util.ListIterator;

public class DecTrallisColumn {
	private DecTrallisCell[] column;
	public DecTrallisCell[] getColumn() {
		return column;
	}
	public void setColumn(DecTrallisCell[] column) {
		this.column = column;
	}


	private int totStates;
	private int codewordBit;
	private Trellis refTrellis;
	private boolean imAmFirst;
	private DecTrallisColumn prev;
	
	
	
	public DecTrallisColumn(int totStates,DecTrallisColumn prev, Trellis refTrellis,int codewordBit){
		this.column = new DecTrallisCell[totStates];
		this.totStates = totStates;
		
		if(prev == null){
			this.setFirstSection();
			this.imAmFirst = true;
		}else{
			this.imAmFirst = false;
			this.setGeneralSection();
			this.prev = prev;
		}
		
		this.refTrellis = refTrellis;
		this.codewordBit = codewordBit;
		
	}
	
	private void setGeneralSection(){
		for(int i = 0;i<totStates; i ++){
			column[i] = new DecTrallisCell();
			column[i].setMyState(i);
			column[i].setFake();
		}
	}
	private void setFirstSection(){
		column[0] = new DecTrallisCell();
		column[0].setStarter();
		column[0].setMyState(0);
		for(int i = 1;i<totStates; i ++){
			column[i] = new DecTrallisCell();
			column[i].setFake();
			column[i].setMyState(i);
		}
	}
	private int hammingWeight(int a, int b){
		int c = a ^ b;
		int weight =0;
		for(int i=0;i<this.codewordBit;i++){
			weight += c%2;
			c >>=1;
		}
		return weight;
	}
	
	
	
	
	public DecoderOutput createWordSection(int codeWord){
		
		System.out.println("Received codeword " + Integer.toString(codeWord));
		
		
		ArrayList<StateWithInflow> finalStates =  refTrellis.getOrderedFinalStates(); 	//stati di arrivo sul traliccio
		ListIterator<StateWithInflow> it = finalStates.listIterator();
		int[] from = new int[2]; 														//posso arrivare da due stati iniziali...ecco i loro indici
		StateAndInfoBit[] fromFlow; //relativi puntatori
		
		int stateCons = 0; //stato di arrivo considerato, parto da 0
		int tempMetric = 0; // metrica accumulata
		int zeroMetric = 0;
		int minMetric = 0;
		int minState = -1;
		boolean changed; // ho fatto delle modifiche al tratto che considero
		boolean rejoinZeroStat = true; //mi sono ricongiunto allo stato iniziale?
		StateWithInflow now;
		while(it.hasNext() ){ //ciclo su tutti gli stati nel traliccio
			
			//System.out.println("Entrato in ciclo");
			
			changed = false;
			
			fromFlow = (now = it.next()).getMyInflows(); //dato il mio stato capisco da quale coppia di stati entro
								
			System.out.println("Parsing state " + now.getMyState().toString());				
			for(int i = 0; i<2;i++){ //ciclio sulla coppia di stati
				
				from[i] = fromFlow[i].getMyState().getMyState().intValue(); //trovo gli indici della coppia di stati di input
				System.out.println("\t from " + Integer.toString(from[i]));
				
				if(prev.getColumn()[from[i]].isActive()){ // Lo stato di provenienza è attivo?
					System.out.println("\t\t active! analyzing...");
					System.out.println("\t\t stored metric: " + prev.getColumn()[from[i]].getMyWholeMetric());
					System.out.println("\t\t edge metric: " + this.hammingWeight(codeWord, fromFlow[i].getMyCodeWord().intValue()));
					
					tempMetric = this.hammingWeight(codeWord, fromFlow[i].getMyCodeWord().intValue()) + prev.getColumn()[from[i]].getMyWholeMetric();
					
					
					if(this.column[stateCons].isActive()){ //se io sono attivo confronto
						if(tempMetric< this.column[stateCons].getMyWholeMetric()){//devo aggiornare
							this.column[stateCons].setMyWholeMetric(tempMetric);
							this.column[stateCons].setFrom(prev.getColumn()[from[i]]);
							this.column[stateCons].setMyInfoBit(fromFlow[i].getMyInfoBit());				
							if(minState == -1){
								minState = stateCons;
								minMetric = tempMetric;
							}else{
								if(tempMetric < minMetric){
									minState = stateCons;
									minMetric = tempMetric;
								}
							}
						}
					}else{
						System.out.println("\t\t activated status");
						this.column[stateCons].setActive(true);
						this.column[stateCons].setMyWholeMetric(tempMetric);
						this.column[stateCons].setFrom(prev.getColumn()[from[i]]);
						this.column[stateCons].setMyInfoBit(fromFlow[i].getMyInfoBit());
						if(minState == -1){
							minState = stateCons;
							minMetric = tempMetric;
						}else{
							if(tempMetric < minMetric){
								minState = stateCons;
								minMetric = tempMetric;
							}
						}
					}
					System.out.println("\t\t final metric: " + this.column[stateCons].getMyWholeMetric() );
					changed = true;
				}else
					System.out.println("\t\t not active! skipping...");
			}
			if(stateCons == 0 ){
				
				zeroMetric = tempMetric;				
			}else{
				if(tempMetric<zeroMetric){
					rejoinZeroStat = false;
				}
			}
			
			stateCons++;
		}
		
		/*if(rejoinZeroStat){
			System.out.println("Flushout...");
			StringBuffer sb = new StringBuffer();
			this.column[0].recursivePrint(sb);
			System.out.println(new String(sb));
			return new DecoderOutput(true,new String(sb));
		}*/
		
		StringBuffer sb = new StringBuffer();
		this.column[minState].recursivePrint(sb);
		System.out.println("Forecast: " + new String(sb));
		
		
		return new DecoderOutput(false,new String(sb));
		
	}
	
	private class DecoderOutput implements DecOutputInterface{
		private boolean flushoutOrder;
		private String infowordsIcarry;
		private DecoderOutput(boolean flushoutOrder, String infowordsIcarry) {
			super();
			this.flushoutOrder = flushoutOrder;
			this.infowordsIcarry = infowordsIcarry;
		}
		
		public boolean toFlushOut(){
			return flushoutOrder;
		}

		public String getInfowordsIcarry() {
			return infowordsIcarry;
		}
		
		

	}
	
	
	

}
