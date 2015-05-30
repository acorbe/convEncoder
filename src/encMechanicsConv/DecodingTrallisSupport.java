/*Alessandro Corbetta
 * corbisoft@gmail.com
 * Conv Encoder simulator 1/02/11
 * 
 */

package encMechanicsConv;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

//import encMechanicsConv.Trellis.TrellisFrame;

public class DecodingTrallisSupport {
	private ArrayList<DecTrallisColumn> myColumns;
	private int totStates;
	private Integer myTime;
	private int codewordBit;
	private Trellis refTrallis;
	
	private DecTrellisFrame myDecTrellisFrame;
	private DecTrellisWindow myDecTrellisWindow;
	
	public DecodingTrallisSupport(int totStates, Trellis refTrallis){
		this.totStates = totStates;
				
		this.refTrallis = refTrallis;
		this.codewordBit = this.refTrallis.getTotCodeBit();
		
		this.myDecTrellisFrame = new DecTrellisFrame();
		//this.myDecTrellisWindow = new DecTrellisWindow();
		
		this.reset();
		
		
		
	}
	
	public JPanel rentTrellisShow(){
		return this.myDecTrellisFrame;
	}
	
	
	private void reset(){
		this.myColumns = new ArrayList<DecTrallisColumn>();
		this.myColumns.add(new DecTrallisColumn(totStates, null,refTrallis,codewordBit));
		myTime = 0;
	}
	synchronized public String addSection(int codeWord){
		//System.out.println("qui!");
		DecTrallisColumn newCol = new DecTrallisColumn(totStates, this.myColumns.get(myTime),refTrallis,codewordBit);
		this.myColumns.add(++myTime,newCol);
		DecOutputInterface decOutput = newCol.createWordSection(codeWord);
		
		
		synchronized(this.myDecTrellisFrame){
			this.myDecTrellisFrame.repaint();
		}
		
		/*if(decOutput.toFlushOut()){
			
			this.reset();
			return decOutput.getInfowordsIcarry();			
			
		}else
			return "";*/
		return decOutput.getInfowordsIcarry();	
	}
	


	private class DecTrellisFrame extends JPanel{
		
		private int myXs,myXf;
		private Rectangle myR;
		
		//private update updateClass;
		
		final int PADh = 500;
		final int PADl = 600;
		final int firstCircleVertex = 30;
		final int circleDiameter = 15;
		//final int horDistance = 
		//final int circleVertexDistance = 80;
		int circleVertexDistanceHor;
		
		final int radius = circleDiameter/2;
		
		private DecTrellisFrame(){
			super();
			this.setPreferredSize(new Dimension(PADl,PADh));
			//this.setBounds(0, 0, 500, 20);
			this.myXs = this.getX();
			this.myXf = this.getX() + this.getWidth();			
		}
		
		synchronized protected void paintComponent(Graphics g){
			super.paintComponents(g);
			
			synchronized(myTime){
				
				this.removeAll();
				
				
				circleVertexDistanceHor = (PADl -2*firstCircleVertex)/(myTime+1);
				
				Graphics2D g2 = (Graphics2D)g;
				int w = getWidth();
		        int h = getHeight();
		        
		        int circleVertexDistance = (h)/(totStates);
		        
		        double[][][] coordinatesL = new double[totStates ][myTime+1][2];
		        double[][][] coordinatesR = new double[totStates][myTime+1][2];
		        double[][] coordinatesTx = new double[totStates ][myTime+1];
		        
				
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	                    RenderingHints.VALUE_ANTIALIAS_ON);		
				g2.setBackground(Color.gray);
				g2.clearRect(0, 0, w, h);
				
				
				
				for(int onTime = 0; onTime<=myTime; onTime++){
				
					for(int i = 0;i <totStates; i++){
						
						g2.draw(new Ellipse2D.Double(firstCircleVertex + onTime*circleVertexDistanceHor ,firstCircleVertex + i*circleVertexDistance,circleDiameter,circleDiameter));
						//g2.draw(new Ellipse2D.Double(w-firstCircleVertex-circleDiameter,firstCircleVertex + i*circleVertexDistance,circleDiameter,circleDiameter));
						g2.setFont(new Font("Arial", Font.BOLD, 16));
						if(myColumns.get(onTime).getColumn()[i].isActive()){
							myColumns.get(onTime).getColumn()[i].setActiveForGraph(false);
							g2.drawString(Integer.toString(myColumns.get(onTime).getColumn()[i].getMyWholeMetric()),firstCircleVertex + onTime*circleVertexDistanceHor ,firstCircleVertex + i*circleVertexDistance);
						}
						//g2.drawString(Integer.toBinaryString(i),w-firstCircleVertex-circleDiameter + radius/2 ,(int)(firstCircleVertex + radius* 4/3  + i*circleVertexDistance));
						coordinatesL[i][onTime][0] = firstCircleVertex + onTime*circleVertexDistanceHor;
						coordinatesL[i][onTime][1] = firstCircleVertex + i*circleVertexDistance + radius;
						
						//coordinatesR[i][0] = w-firstCircleVertex-circleDiameter;
						//coordinatesR[i][1] = firstCircleVertex + i*circleVertexDistance + radius;
						
					}
				}
				for(int i = 0;i <totStates; i++){
					myColumns.get(myTime).getColumn()[i].setActiveForGraph(true);
				}
				
				
				for(int onTime = myTime; onTime>0; onTime--){
					int stateFrom;
					for(int i = 0;i <totStates; i++){
						if(myColumns.get(onTime).getColumn()[i].isActive() && myColumns.get(onTime).getColumn()[i].isActiveForGraph() ){
							stateFrom = myColumns.get(onTime).getColumn()[i].getFrom().getMyState();
							g2.draw(new Line2D.Double(coordinatesL[stateFrom][onTime-1][0]+circleDiameter ,coordinatesL[stateFrom][onTime-1][1],coordinatesL[i][onTime][0],coordinatesL[i][onTime][1]));
							myColumns.get(onTime).getColumn()[i].getFrom().setActiveForGraph(true);
						}
						
					}
				}
			}
					
				
			
		}
		
	}
	private class DecTrellisWindow extends JFrame{
		private DecTrellisWindow(){
			super("Decoding Trellis");
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			Container globCont = this.getContentPane();
			globCont.add(myDecTrellisFrame);
			
			//this.
			
			this.pack();
			this.setVisible(true);
		}
		
	}
	

}
