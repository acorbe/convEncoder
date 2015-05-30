/*Alessandro Corbetta
 * corbisoft@gmail.com
 * Conv Encoder simulator 1/02/11
 * 
 */

package encMechanicsConv;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.*;
import java.util.*;

import javax.swing.*;



public class Trellis {
	private int totStatesLog;
	private int totCodeBit;
	private TreeMap<StateAndInfoBit,StateWithInflow> myTrellis;
	private TreeMap<StateAndInfoBit,StateAndInfoBit> codingCorresp;
	private ArrayList<StateWithInflow> orderedFinalStates;
	public ArrayList<StateWithInflow> getOrderedFinalStates() {
		return orderedFinalStates;
	}
	public Trellis(){
		this.myTrellis = new TreeMap<StateAndInfoBit,StateWithInflow>();
		this.codingCorresp = new TreeMap<StateAndInfoBit,StateAndInfoBit>();
		 
	}
	public Trellis(String path){
		this();
		if(this.ReadTrellis(path) == false) System.exit(0);
	}
	
	public CodeWordAndFinalState codedOut(State myState, Integer myInfoBit){
		StateAndInfoBit stWithInfoTmp = new StateAndInfoBit(myState,myInfoBit);
		stWithInfoTmp = this.codingCorresp.get(stWithInfoTmp);
		return new CodeWordAndFinalState(stWithInfoTmp.getMyCodeWordStr(),this.myTrellis.get(stWithInfoTmp));
		
	}
	
	public JPanel rentMyrellisPanel(){
		return new TrellisFrame();
	}

	
	
	public int getTotCodeBit() {
		return totCodeBit;
	}
	private StringTokenizer readTrellisFileLine(BufferedReader stFile){
		String line;
		boolean flag = true;
		try {
			do{ //leggo la prima riga non commentata
				line = stFile.readLine();
				if( line==null)
					return null;
				else{			
					if(! line.startsWith("#"))
						flag =false;
				}
			}while(flag);
			return new StringTokenizer(line, " ");
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
	}
	public int getTotStatesLog() {
		return totStatesLog;
	}
	
	private boolean ReadTrellis(String path){
				
		BufferedReader stFile;
		try {
			stFile = new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		StringTokenizer st;
		this.totStatesLog = Integer.parseInt((st =readTrellisFileLine(stFile)).nextToken());
		this.totCodeBit = Integer.parseInt(st.nextToken());
		
		System.out.println("Inizializzato traliccio di " + Integer.toString(this.totStatesLog) + " stati");
		StateWithInflow[] ouputAlreadyConsid = new StateWithInflow[(1 << totStatesLog )];
		
		for(int i = 0 ; i < (1 << totStatesLog); i++ ) ouputAlreadyConsid[i] = null;
		
		
		
		for(int i = 0 ; i < (1 << totStatesLog + 1); i++ ){
			//System.out.println(i);
			st = readTrellisFileLine(stFile);
			int state = Integer.parseInt(st.nextToken());
			int infoBit = Integer.parseInt(st.nextToken());
			int codeword = Integer.parseInt(st.nextToken(),2);
			int outState = Integer.parseInt(st.nextToken());
			
			StateAndInfoBit from = new StateAndInfoBit(new Integer(state), new Integer(infoBit), new Integer(codeword),totCodeBit);
			StateWithInflow to;
			if(ouputAlreadyConsid[outState] == null){			
				to = new StateWithInflow(new Integer(outState));
				ouputAlreadyConsid[outState] = to;			
			}else
				to = ouputAlreadyConsid[outState];
						
			this.myTrellis.put(from, to);
			this.codingCorresp.put(from, from);
		}
		
		
		
		//backward link 4 decoding
		
		Iterator<StateAndInfoBit> itOnKeys =  this.myTrellis.keySet().iterator();		
		while(itOnKeys.hasNext()){
			StateAndInfoBit key = itOnKeys.next();
			this.myTrellis.get(key).setInFlow(key);
			//System.out.println(this.myTrellis.get(key));
		}
		Set<StateWithInflow> tempSet = new TreeSet<StateWithInflow>(this.myTrellis.values());
		this.orderedFinalStates = new ArrayList<StateWithInflow>(tempSet);
		Collections.sort(this.orderedFinalStates);
		
		Iterator<StateWithInflow> it = this.orderedFinalStates.iterator();
		while(it.hasNext()){
			System.out.println(it.next());
		}
		
		System.out.println("tot out states: " + Integer.toString(this.orderedFinalStates.size()));
		
		//this.trallisWindowShow();
		
		
		return true;
	}
	
	public void trallisWindowShow(){
		new TrellisWindow();
	}
	
	private class TrellisFrame extends JPanel{
		
		private int myXs,myXf;
		private Rectangle myR;
		
		//private update updateClass;
		
		final int PADh = 500;
		final int firstCircleVertex = 30;
		final int circleDiameter = 30; 
		//final int circleVertexDistance = 80;
		int circleVertexDistance;
		
		final int radius = circleDiameter/2;
		
		private TrellisFrame(){
			super();
			this.setPreferredSize(new Dimension(300,PADh));
			//this.setBounds(0, 0, 500, 20);
			this.myXs = this.getX();
			this.myXf = this.getX() + this.getWidth();			
		}
		
		protected void paintComponent(Graphics g){
			super.paintComponents(g);
			
			this.removeAll();
			
			Graphics2D g2 = (Graphics2D)g;
			int w = getWidth();
	        int h = getHeight();
	        
	        circleVertexDistance = (h)/(1 << totStatesLog);
			
	        double[][] coordinatesL = new double[1 << totStatesLog ][2];
	        double[][] coordinatesR = new double[1 << totStatesLog ][2];
	        double[][] coordinatesTx = new double[1 << totStatesLog ][2];
	        
			
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);		
			g2.setBackground(Color.gray);
			g2.clearRect(0, 0, w, h);
			
			for(int i = 0;i < (1 << totStatesLog ); i++){
				g2.draw(new Ellipse2D.Double(firstCircleVertex,firstCircleVertex + i*circleVertexDistance,circleDiameter,circleDiameter));
				g2.draw(new Ellipse2D.Double(w-firstCircleVertex-circleDiameter,firstCircleVertex + i*circleVertexDistance,circleDiameter,circleDiameter));
				g2.setFont(new Font("Arial", Font.BOLD, 16));
				g2.drawString(Integer.toBinaryString(i),firstCircleVertex + radius/2 ,(int)(firstCircleVertex + radius* 4/3  + i*circleVertexDistance));
				g2.drawString(Integer.toBinaryString(i),w-firstCircleVertex-circleDiameter + radius/2 ,(int)(firstCircleVertex + radius* 4/3  + i*circleVertexDistance));
				coordinatesL[i][0] = firstCircleVertex +circleDiameter;
				coordinatesL[i][1] = firstCircleVertex + i*circleVertexDistance + radius;
				
				coordinatesR[i][0] = w-firstCircleVertex-circleDiameter;
				coordinatesR[i][1] = firstCircleVertex + i*circleVertexDistance + radius;
				
			}
			txCoordinate(coordinatesL,coordinatesR,coordinatesTx);
			
			
			// Connections
			Stroke drawingStrokeDSH = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
			Stroke drawingStrokeDEF = new BasicStroke(1);
			Iterator<StateAndInfoBit> itOnKeys =  myTrellis.keySet().iterator();	
			while(itOnKeys.hasNext()){
				StateAndInfoBit key = itOnKeys.next();
				if(key.getMyInfoBit().intValue() == 0)
					g2.setStroke(drawingStrokeDEF);
				else
					g2.setStroke(drawingStrokeDSH);
				
				double xF = coordinatesL[key.getMyState().getMyState().intValue()][0];
				double yF = coordinatesL[key.getMyState().getMyState().intValue()][1];			                                                                ;
				
				double xT = coordinatesR[myTrellis.get(key).getMyState().intValue()][0];	
				double yT = coordinatesR[myTrellis.get(key).getMyState().intValue()][1];
				
				g2.draw(new Line2D.Double(xF,yF,xT,yT));
				g2.drawString(key.getMyCodeWordStr(),(int)( xF + (xT-xF)/5), (int) (yF + (yT-yF)/5));
			}
			
		
		}
		private void txCoordinate(double[][] coordinatesL,double[][] coordinatesR,double[][] coordinatesTx){
			for(int i = 0;i < (1 << totStatesLog ); i++){
				for(int j = 0;j<2;j++){
					coordinatesTx[i][j] = coordinatesL[i][j] + (coordinatesR[i][j] - coordinatesL[i][j])/10;
				}
				
			}
		}
		 
	}
	private class TrellisWindow extends JFrame{
		private TrellisWindow(){
			super("Trellis");
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			Container globCont = this.getContentPane();
			globCont.add(new TrellisFrame());
			
			//this.
			
			this.pack();
			this.setVisible(true);
		}
		
	}
	

}
