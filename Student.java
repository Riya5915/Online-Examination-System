//package project;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ClassNotFoundException;
import java.lang.Runnable;
import java.lang.Thread;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt. *;
import java.awt. event.*;
import javax.swing.*;


//import pro.OnlineTest.ActionHandler;

import java.util.*;
import java.net.*;
import java.io.*;

class Student extends JFrame{
	static String studentname ="";
	static String rollno ="";
	static String hostadd="",marks="";
	static String port="1500";
	static int TOTAL=0;
	boolean connected;ObjectOutputStream oos;
	ObjectInputStream ois1,ois2,ois3;
	String choice[]=new String[5];
	String addq[]=new String[5];
	String qans[][]=new String[5][4];
JTextField	tf = new JTextField();
	//private Client client;
	static {
		try{
		TOTAL = 5;		
		/* 	The input window */
		studentname = JOptionPane.showInputDialog("Enter your NAME: ");
		rollno = JOptionPane.showInputDialog("Enter your ROLLNO.: ");
		hostadd=JOptionPane.showInputDialog("Enter IPAddress of the host ");
		//port=JOptionPane.showInputDialog("Enter Port");
		if(studentname.length() < 1){
			studentname = "Anonymous   ";
			rollno="0000";
			hostadd="127.0.0.1";
			//port="1500";
		}
		else{
			studentname = studentname.trim() + " ";
			rollno = rollno.trim() + " ";
			hostadd=hostadd.trim();
		}
		}catch(NullPointerException e){ System.exit(0); 
		}
		}	
	Socket socket;
	int seconds, minutes;
	int quesnum, itemCheck, mark; 
final String TESTTITLE = "online test";
	final int TIMELIMIT = 10;
	final int PASS = 33;
	String []answers = new String[TOTAL];
JButton []choice_button = new JButton[6];
JTextArea answerboxes[] = new JTextArea[4];
JCheckBox []boxes = new JCheckBox[4];
	JTextPane pane = new JTextPane();
JLabel student, choose, message, timecounter, testresult;
	boolean start_test, check_answer, allowRestart, finishtest;
Northwindow panelNorth = new Northwindow();
Southwindow panelSouth = new Southwindow();
Centerwindow panelCenter = new Centerwindow();

protected Student(){
    for (int i=0; i<TOTAL; i++) answers[i] ="";
	getContentPane().setLayout(new BorderLayout() );
	getContentPane().add("North", panelNorth);
	getContentPane().add("South", panelSouth);
	getContentPane().add("Center", panelCenter);
	int width = 0, height=0; 
        if(java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth()<799){width=		640; 		height=460; }
        else {width=720; height=540; }
	setSize(width,height);
	Dimension dim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	setLocation((dim.width-width)/2, (dim.height-height)/2);
}

class Northwindow extends JPanel{

/**
**  Northwindow constructor 
**/
    public Northwindow(){
		setLayout(new GridLayout(2,2));
		setBackground(new Color(230, 230, 255));
		JPanel northPanel = new JPanel();
		// the server name anmd the port number
		JPanel serverAndPort = new JPanel();
		// the two JTextField with default value for server address and port number
	JTextField	tfServer = new JTextField(hostadd);
	JTextField	tfPort = new JTextField("" + port);
		tfPort.setHorizontalAlignment(SwingConstants.RIGHT);

		serverAndPort.add(new JLabel("Server Address:  "));
		serverAndPort.add(tfServer);
		serverAndPort.add(new JLabel("Port Number:  "));
		serverAndPort.add(tfPort);
		serverAndPort.add(new JLabel(""));
		// adds the Server an port field to the GUI
		northPanel.add(serverAndPort);

		student = new JLabel("\tBEST OF LUCK  "+studentname);
		student.setFont(new Font("",Font.BOLD,16) );
		message = new JLabel();
		message.setForeground(Color.blue);
		add(northPanel);
		add(student);
		add(message);
		//add(new JLabel("               ") );
		//add(new JLabel("               ") );
        setBorder(BorderFactory.createEtchedBorder() );
	}
}

class Southwindow extends JPanel{// implements ActionListener{
	public Southwindow(){
		String []key = {"Connect","start:","next:","finish:","check next:","check previous:"};
			for(int i=0; i<choice_button.length; i++)
                   {
				choice_button[i] = new JButton(key[i]);
				choice_button[i].addActionListener(new ActionHandler() );
				if(i >=0)add(choice_button[i]);
			  }
        setBorder(BorderFactory.createEtchedBorder() );
	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
class Centerwindow extends JPanel{

	public Centerwindow(){
		setLayout(new GridLayout(1,2) );
		JScrollPane west = new JScrollPane(pane);       
		pane.setForeground(Color.red);	       
   		pane.setFont(new Font ("monospaced",0,12) );
		pane.setText("PRESS CONNECT TO CONNECT TO THE SERVER \n PRESS START TO START THE TEST" );
		pane.setEditable(false);
		JPanel east = new JPanel();
		east.setLayout(new BorderLayout() );	
		JPanel northEast = new JPanel();
		northEast.setBackground(new Color(230, 230, 255) ); 
		east.add("North", northEast);
		JPanel westEast = new JPanel();
		westEast.setLayout(new GridLayout(6,1) );
		east.add("West", westEast);
		JPanel centerEast = new JPanel();
		centerEast.setLayout(new GridLayout(6,1) );
		centerEast.setBackground(new Color(255,255,200));
		east.add("Center", centerEast);       
			timecounter = new JLabel("     There are "+TOTAL+" questions in total");
			timecounter.setFont(new Font ("Arial",Font.BOLD,16) );
			timecounter.setForeground(new Color(0,90,20) );
			northEast.add(timecounter);
			westEast.add(new JLabel(" "));
        String []boxs = {" A ", " B ", " C ", " D "};
			for(int i=0; i<boxes.length; i++) { 
				boxes[i] = new JCheckBox(boxs[i]);
				boxes[i].addItemListener(new ItemHandler() );
				westEast.add(boxes[i]);
			}
        westEast.add(new JLabel() );
		choose = new JLabel("    CHOOSE CORRECT ANSWERS");
		choose.setBorder(BorderFactory.createEtchedBorder() );
		centerEast.add(choose);
        JScrollPane panes[] = new JScrollPane[4];
			for(int i=0; i<answerboxes.length; i++){
				answerboxes[i] = new JTextArea();
			    answerboxes[i].setBorder(BorderFactory.createEtchedBorder() );
				answerboxes[i].setEditable(false);
				answerboxes[i].setBackground(Color.white);
				answerboxes[i].setFont(new Font("",0,12) );
	            answerboxes[i].setLineWrap(true);      
                answerboxes[i].setWrapStyleWord(true);
                panes[i] = new JScrollPane(answerboxes[i]);
			    centerEast.add(panes[i]);
			}
		if(TIMELIMIT >0)testresult = new JLabel(studentname+",   You have only : "+TIMELIMIT+" minutes to complete.");   
		else testresult = new JLabel("     There is no time limit for this test");
		testresult.setBorder(BorderFactory.createEtchedBorder() );
		centerEast.add(testresult);
	add(west);
	add(east);
	}
}
class ActionHandler implements ActionListener{
	//DataOutputStream oos;
	
/* actionPerformed method */
	public void actionPerformed(ActionEvent evt){
	  String source = evt.getActionCommand();
	 
	  if(source.equals("Connect"))
	  {choice_button[0].setVisible(false);
	  panelSouth.remove(choice_button[0]);
		  try
	        {
	    		// Create a connection to the server socket on the server application
	            socket = new Socket(hostadd , 1500);
	            
	        	// Read a message sent by client application
	            ois1 = new ObjectInputStream(socket.getInputStream());
	           String message[] = (String[]) ois1.readObject();
	           ois2 = new ObjectInputStream(socket.getInputStream());
	           String message1[][] = (String[][]) ois2.readObject();
	           ois3 = new ObjectInputStream(socket.getInputStream());
	           String message2[] = (String[]) ois3.readObject();
	            //System.out.println("Message Received from client: " + message);
	            //b(message);
	                        printArray(message);
	                        printArray1(message1);
	                        printArray2(message2);
	           oos = new ObjectOutputStream(socket.getOutputStream());
	                        
	        	//oos = new DataOutputStream(socket.getOutputStream());
	            
	            System.out.println("Waiting for client message is...");
	        } 
	        catch (IOException e) 
	        {
	            e.printStackTrace();
	        } 
	        catch (ClassNotFoundException e) 
	        {
	            e.printStackTrace();
	        }		     
      }	if(source.equals("start:")){
			choice_button[1].setVisible(false);
			start_test=true;
			allowRestart=true;
          if(TIMELIMIT >0)new Timer(); // inner Timer class 
			panelSouth.remove(choice_button[1]); //start
          displayquestion();            
      }
	if(start_test){
		if(source.equals("previous:"))  {            			
			recordanswer();
			quesnum--;
    		if(quesnum ==  -1) quesnum=TOTAL-1;
			checkteststatus();
            displayquestion(); 
		}
		if(source.equals("next:")) {
			recordanswer();
			quesnum++;            
			if(quesnum ==  TOTAL-1) finishtest=true;
			if(quesnum ==  TOTAL) quesnum=0;
            checkteststatus();
            displayquestion(); 
		}
		if(source.equals("finish:")) {
			if (finishtest){
				recordanswer();
				quesnum = 0;
				String a="";

				choice_button[4].setBackground(Color.lightGray);
				timecounter.setForeground(Color.blue);
				timecounter.setFont(new Font ("Arial",0,14) );
				start_test=false; 
				check_answer=true;
			    connected=false;
				panelSouth.add(choice_button[0]);
				mark_ques();
				displayquestion();
				checkteststatus();
				a = calculateResult();System.out.println(a);
				try{
	            String data[]=a.split(",");
	            System.out.println(data[0]+","+data[1]+","+data[2]+","+data[3]+","+data[4]);
				            oos.writeObject(data);				
				}catch(Exception e){System.out.println("ERROR: "+e);}
				
			}
			else  JOptionPane.showMessageDialog(null,"Cycle through all questions before pressing finish",
																 "User Message",JOptionPane.INFORMATION_MESSAGE);
		}
	} 

	if (check_answer){
		if(source.equals("check next:")) {
			quesnum++;
			if(quesnum ==  TOTAL) quesnum=0;
			mark_ques();
			displayquestion();
			checkteststatus();
		}
		if(source.equals("check previous:")) {
			quesnum--;
			if(quesnum ==  -1) quesnum=TOTAL-1;
			mark_ques();
			displayquestion();
			checkteststatus();
		}
	}
	validate();        
	}
	private void b(String message) {
        List<String> list = new ArrayList<String>();
           String[] arr = list.toArray(new String[0]);
           System.out.println("Array is " + Arrays.toString(arr));
        
   }
       private void printArray(String[] arr){int m=0;
          for(String s:arr){
             //System.out.println(s);
            addq[m++]=s;
           }
       }
	private void printArray1(String[][] message) {int k=0,j=0;
			 for(String[] s:message)	{
		     qans[j][0]=s[0];
             qans[j][1]=s[1];
             qans[j][2]=s[2];
             qans[j][3]=s[3];
             j=j+1;
             }
		for(j=0;j<5;j++)
			for(k=0;k<4;k++)
				System.out.println(qans[j][k]);
	}
	 private void printArray2(String[] arr){int m=0;
     for(String s:arr){
        System.out.println(s);
       choice[m++]=s;
       
      }
  }
	class Timer extends Thread implements Runnable{
		public Timer(){
			new Thread(this).start();
		}

	    public void run() {
			while(start_test){
	            try {
	               Thread.sleep(1000);
	               seconds++;
					if(seconds % 60 == 0 && seconds != 0){
	                    seconds -= 60;
	                    minutes++;
					}
					timecounter.setText("    Time Counter:  "+(9-minutes)+" mins : "+(59-seconds)+" secs ");
					if((minutes)==(TIMELIMIT)){
						timecounter.setText("    Time Counter:  "+(10-minutes)+" mins : "+(seconds)+" secs ");
						start_test=false;
						finishtest=true;
						endTest();
	                }
			    }
	            catch(InterruptedException ex)  { System.out.print(ex); }
			}
	    }
	}

	/* checkteststatus method */

			public void checkteststatus(){
			if((quesnum ==  TOTAL-1)&&(start_test))choice_button[3].setBackground(Color.green);
			else choice_button[4].setBackground(Color.lightGray);
	      	  if(answers[quesnum].length() >0){ 
				for(int i=0; i<answers[quesnum].length(); i++)
				boxes[Integer.parseInt(answers[quesnum].substring(i,i+1) )-1].setSelected(true);
			}
			else for(int i=0; i<boxes.length; i++)boxes[i].setSelected(false);
			}
			public void displayquestion(){
				//String addq[] = null,qans[][]=null;
		        int j = quesnum+1;
				pane.setText(addq[quesnum]);
				if(start_test)message.setText("Question "+j+" out of "+TOTAL);
		        for (int i=0; i<4; i++)answerboxes[i].setText(qans[quesnum][i]);
				if(start_test){
		            String temp="";
		            choose.setText(temp);
				}
				else {
				timecounter.setText("    Your choices are shown in RED");
		            choose.setText("    Correct answers are marked in GREEN.");
				}
			}
			public void recordanswer(){
				String tmp = "";
				for(int i=0; i<boxes.length; i++) if(boxes[i].isSelected() ) tmp +=i+1;
		        answers[quesnum] = tmp;
		    }

		/* endTest method */ 

			public void endTest(){
				JOptionPane.showMessageDialog(null, "TIME OVER: please press 'finish'");
				//message.setText("TIME OVER: please press 'finish'");
		        choice_button[2].setEnabled(false); 
		        choice_button[3].setEnabled(true); 
		        choice_button[4].setEnabled(true); 
			}
			
			public void mark_ques(){
				for(int i=0; i<answerboxes.length; i++) answerboxes[i].setBackground(Color.white);
				for(int i=0; i<choice[quesnum].length(); i++)
					answerboxes[Integer.parseInt(choice[quesnum].substring(i,i+1))-1].setBackground(Color.green);
				if(choice[quesnum].equals(answers[quesnum])) message.setText("Answer correct, well done!");
				else{ message.setText("Sorry, you got this one wrong.");
				for(int i=0; i<answers[quesnum].length(); i++)
				answerboxes[Integer.parseInt(answers[quesnum].substring(i,i+1))-1].setBackground(Color.red);}
			}
			public String calculateResult(){
				mark=0;
				double temp=0.0;
		        java.text.DecimalFormat df = new java.text.DecimalFormat("#0.#");
				for(int i=0; i<TOTAL; i++)if(choice[i].equals(answers[i]))mark++;
				temp=(double)mark;
				if(temp/TOTAL*100 >=PASS) testresult.setText("  Well done "+studentname.substring(0,studentname.indexOf(' ') )+", you passed");
				else testresult.setText("  Better luck next time "+studentname.substring(0,studentname.indexOf(' ') ) );
				student.setText(" Final score for "+studentname+":  "+mark+" out of "+TOTAL+":  "+df.format(temp/TOTAL*100)+"%");
				String m=rollno+","+studentname+","+Double.toString(temp)+","+Double.toString(temp*100/TOTAL)+","+(temp*100/TOTAL >=PASS?"PASS":"FAIL");
				new Resultwindow().show();
				marks=Integer.toString(mark);
				return m;
			}
		}
class Resultwindow extends JFrame{
	Resultwindow() {
  	super( studentname+" results: " +(mark*100/TOTAL >=PASS?"PASS":"FAIL") );
		Container cont = getContentPane();
	cont.setLayout(new GridLayout(TOTAL/2+3,5,2,5) );
	cont.setBackground(new Color(255,220,255) );
	cont.add(new JLabel("  "+"Marks:    "+mark+"/"+TOTAL+": "+"Percentage:  "+(mark*100/TOTAL)+"%") );
       for(int i=0; i<3; i++)cont.add(new JLabel() );
  	  String temp[] = new String[TOTAL];
		for(int i=0; i<TOTAL; i++){
			if(choice[i].equals(answers[i])) temp[i]="correct";
			else temp[i]="wrong";
		}
		for(int i=0; i<TOTAL; i++) cont.add(new JLabel("  Question "+(i+1)+":  "+temp		[i]) );
	pack();
	setLocation(200,200);
}
}
class ItemHandler implements ItemListener{
	public void itemStateChanged(ItemEvent evt){
	  if(start_test){
		for(int i=0; i<boxes.length; i++) if(boxes[i].isSelected() ) itemCheck++; 
		if(itemCheck > 1){
			java.awt.Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null,"<html><font 		size='4' color='00308a'><center>"+
			      	"There is only one possible<br> answer to question "+(quesnum+1)+
			                    "<html>","User Information Message",JOptionPane.INFORMATION_MESSAGE);
        }
		itemCheck=0;
	  }	  
	}
	
}

/*  main method */

	public static void main(String [] args){
		Student frame = new Student();
		frame.setTitle("    "+"ONLINE TEST");
		frame.setDefaultCloseOperation( EXIT_ON_CLOSE );
		frame.setVisible(true);
    }
}


