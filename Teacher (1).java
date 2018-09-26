import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.awt.event.*;

public class Teacher extends Frame implements ActionListener{
	JButton tt,vm,addq,remq,s,s1,s2,s3;
	JTextField name,course,ques,a,b,c,d,tPortNumber;
	JComboBox ans;
	JFrame f,f1,f2,f3;
	JLabel l1,l2,lq,lans,la,lb,lc,ld;
	JPanel p1,p2,p3,p4,p5,p6,p;
	String tname,cors,roll,name1,tablename,cano,ipadd;int m;
	String answers[][] = new String[5][4];
	String corrans[] = new String[5];
	String questions[] = new String[5];
	String remques[] = new String[10];
	JCheckBox[] chk1 = new JCheckBox[10];
	boolean cond = true;

	 ServerSocket server;
	int i,j,port=1500;
	
	protected Teacher()
	{		
		tname = JOptionPane.showInputDialog("Enter your name: ");
		if(tname.length() < 1) 
			tname = "Anonymous ";
		else 
			tname = tname.trim();
		
		cors = JOptionPane.showInputDialog("Enter your course: ");
		if(cors.length() < 1) 
			cors = "Anonymous ";
		else 
			cors = cors.trim() + " ";
		f = new JFrame("Teacher's Login");
		f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
		f.setLayout(new GridLayout(3,1,10,10));
		l1 = new JLabel("Welcome "+tname+" to Online CA System",JLabel.CENTER);
		tt = new JButton("Take A Test");
		vm = new JButton("View Marks");
		f.add(l1);
		f.add(tt);
		f.add(vm);
		f.setSize(350,300);
		f.setVisible(true);
		tt.addActionListener(this);
		vm.addActionListener(this);
	}
	public void actionPerformed(ActionEvent e){
		try{
			if(e.getSource()==tt)
			{
				new takeatest();
				f.dispose();
			}
			if(e.getSource()==vm)
			{
				new viewmarks();
				//f.dispose();
			}
		}
		catch(Exception e1)
		{ System.out.println("Error= "+e1); }
	}
class takeatest implements ActionListener{
	takeatest()
	{
		cano=JOptionPane.showInputDialog("Enter the CA# ");
	   cano=cano.trim();
		f1 = new JFrame("Take a Test");
		f1.setDefaultCloseOperation(f1.EXIT_ON_CLOSE);
		f1.setLayout(new GridLayout(5,1,10,10));
		tPortNumber = new JTextField("PORT NUMBER:  " + port);
		l2 = new JLabel("Welcome "+tname+" to Online CA System",JLabel.CENTER);
		addq = new JButton("Add Question");
		remq = new JButton("Remove Question");
		s = new JButton("Submit & Start Server");
		f1.add(l2);
		f1.add(addq);
		f1.add(remq);
		f1.add(s);
		f1.add(tPortNumber);
		f1.setSize(350,400);
		f1.setVisible(true);
		try{
				Class.forName("com.mysql.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/DB1?user=root&password=hps");
				Statement s = con.createStatement();
				s.execute("create table "+ cano+"(Sname varchar(30), RNO varchar(10)  marks integer(10), ipadd varchar(30), primary key(RNO,ipadd)");
				System.out.println("Table created");}
		catch(Exception err){
			System.out.println("Error: "+err);
		}
		addq.addActionListener(this);
		remq.addActionListener(this);
		s.addActionListener(this);
	}
	public void actionPerformed(ActionEvent e1){
		if(e1.getSource()==addq)
		{	
			new addquestion();
		}
		if(e1.getSource()==remq)
		{
			new removeques();
		}
		if(e1.getSource()==s)
		{
			// if running we have to stop
			if(server != null){
				cond = false;
				try { 
		            new Socket("localhost", port); 
		        } 
		        catch(Exception e) { 
		        } 
		        server = null;
				tPortNumber.setEditable(true);
				s.setText("Start");
				return;
			}
			// OK start the server
			try {
				port = 1500;
			}
			catch(Exception er) {
				//appendEvent("Invalid port number");
				return;
			}
			//create a new server
			s.setText("Stop");
			server1 example = new server1();
			f2.dispose();
			example.handleConnection(cond); 
		}	
	}
}
public class viewmarks extends JFrame{
  viewmarks(){
   	 JFrame frame = new JFrame();
       tablename=	JOptionPane.showInputDialog("Enter the CA# to see marks ");
       tablename=tablename.trim().toLowerCase();
		    frame.setSize(500, 300);
	        frame.setLocationRelativeTo(null);
	        frame.setVisible(true);
	        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		DefaultTableModel model = new DefaultTableModel();
	    Container cnt = this.getContentPane();
	    JTable jtbl = new JTable(model);
	        cnt.setLayout(new FlowLayout(FlowLayout.LEFT));
	        model.addColumn("S.name");
	        model.addColumn("Roll#");
	        model.addColumn("Marks");
	        model.addColumn("IPAddress");
	        frame.add(jtbl);
	        
		try{
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/DB1?user=root&password=hps");
	
			PreparedStatement ps1=con.prepareStatement("show tables");
			ResultSet tables = ps1.executeQuery();
			int i=0;
			if(tables!=null)
				while(tables.next()){
					String tbn=tables.getString(1);
			 if (tbn.equals(tablename)) {
				System.out.println("inside if");
			   //Table exists
				 PreparedStatement pstm = con.prepareStatement("SELECT *  FROM "+tablename);
		            ResultSet Rs = pstm.executeQuery();
		            while(Rs.next()){
		                model.addRow(new Object[]{Rs.getString(1), Rs.getString(2),Rs.getInt(3),Rs.getString(4)});
		               }System.out.println("TABLE EXIST");
		            break;
		           }
			else {
				i++;
			
			}
		}	
	}
	catch (Exception err){
		 	System.out.println("ERROR "+err);
		}
		        JScrollPane pg = new JScrollPane(jtbl);
		        cnt.add(pg);
		        this.pack();
		    frame.add(cnt);
	}
}
public class server1 
{
    private ServerSocket server;
    private int port = 1500;
 
    public server1()
    {
    	if(cond){
        try
        {
            server = new ServerSocket(port);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }}
    	else
    	{
    		System.out.println("Server stoppped...");
    		//new Teacher();
    		//return;
    	}
    }
    public void handleConnection(boolean con) 
    {
        System.out.println("Waiting for client message got...");
        
        while (con) 
        {
            try
            {
                Socket socket = server.accept();
            	if(!con)
            		break;
                new ConnectionHandler(socket);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }System.out.println("Stopped...");
       // new viewmarks();
        System.out.println("ok");
        return;
    }
}
 
class ConnectionHandler implements Runnable 
{
    public Socket socket;
    public ConnectionHandler(Socket socket)
    {
        this.socket = socket;
        Thread t = new Thread(this);
        t.start();
    }
    public void run() 
    {
    	try {
    		// Send a message to the client application
    		   ObjectOutputStream oos1 = new ObjectOutputStream(socket.getOutputStream());
    		   oos1.writeObject(questions);
    		   ObjectOutputStream oos2 = new ObjectOutputStream(socket.getOutputStream());
               oos2.writeObject(answers);
               ObjectOutputStream oos3 = new ObjectOutputStream(socket.getOutputStream());
    		   oos3.writeObject(corrans);
    		   ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
    		   String result[] = (String[]) ois.readObject();
    		   System.out.println(result[0]+","+result[1]+","+result[2]+","+result[3]+","+result[4]);
    		   roll = result[0];
    		   name1 = result[1];
    	       m = (int)Double.parseDouble(result[2]);
    		   printArray(result);
               System.out.println(this.socket.getRemoteSocketAddress().toString());
          	   ipadd=this.socket.getRemoteSocketAddress().toString();
  // System.out.println((((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress()).toString().replace("/",""));
          	   try{
          		   Class.forName("com.mysql.jdbc.Driver");
          		   Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/DB1?user=root&password=hps");
          			PreparedStatement ps=con.prepareStatement("insert into "+cano+"(sname,rno,marks,ipadd)"+"values(?,?,?,?)");
        		    con.setAutoCommit(false);
        			 ps.setString(1,name1);
        			 ps.setString(2, roll);
        			 ps.setInt(3,m);
        			 ps.setString(4,ipadd);
        			 ps.execute();
        		     con.commit();
        			 ps.close();
        					
        			}catch(Exception err){
        					System.out.println("ERROR: "+err);}
       		         ois.close();
       		         oos1.close();
       		         oos2.close();
       		         oos3.close();
       		         cond = false;
       		         server1 o = new server1(); 
       		         o.handleConnection(cond);
       		         System.out.println(cond);
       		         System.out.println("done");
       		        
       		         //socket.close();
    		        } catch (UnknownHostException e) {
    		            e.printStackTrace();
    		        } catch (IOException e) {
    		            e.printStackTrace(); 
    		        } catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    }
    private void printArray(String[] arr){
        for(String s:arr){
           System.out.println(s);
         }
    }
}
class addquestion implements ActionListener{
	addquestion(){	
		i=0;
		f2 = new JFrame("Add Questions in your Test");
		f2.setLayout(new GridLayout(7,2,10,30));
		f2.setDefaultCloseOperation(f2.DISPOSE_ON_CLOSE);
		lq = new JLabel("Enter your question :",JLabel.CENTER);
		ques = new JTextField(30);
		la = new JLabel("Enter Option 1 :",JLabel.CENTER);
		a = new JTextField(30);
		lb = new JLabel("Enter Option 2 :",JLabel.CENTER);
		b = new JTextField(30);
		lc = new JLabel("Enter Option 3 :",JLabel.CENTER);
		c = new JTextField(30);
		ld = new JLabel("Enter Option 4 :",JLabel.CENTER);
		d = new JTextField(30);
		lans = new JLabel("Enter the Correct Answer Option:",JLabel.CENTER);
		String[] o = new String[] {"<-Select The Correct Answer->","Option 1","Option 2","Option 3","Option 4"};
		// create a combo box with the fixed array:
		ans = new JComboBox(o);
		//ans = new JTextField(30);
		s1 = new JButton("Submit");
		f2.add(lq);f2.add(ques);
		f2.add(la);f2.add(a);
		f2.add(lb);f2.add(b);
		f2.add(lc);f2.add(c);
		f2.add(ld);f2.add(d);
		f2.add(lans);f2.add(ans);
		f2.add(s1);
		f2.setSize(500,500);
		f2.setVisible(true);
		s1.addActionListener(this);
	}
	public void actionPerformed(ActionEvent e2){
		try{
			if(e2.getSource()==s1)
			{
				questions[i] = ques.getText().trim();
				remques[i] = ques.getText().trim();
				String str = (String)ans.getSelectedItem();
				if(str.equals("Option 1"))
				{
					corrans[i] = "1";
				}
				else if(str.equals("Option 2"))
				{
					corrans[i] = "2";
				}
				else if(str.equals("Option 3"))
				{
					corrans[i] = "3";
				}
				else if(str.equals("Option 4"))
				{
					corrans[i] = "4";
				}
				answers[i][0] = a.getText();
				answers[i][1] = b.getText();
				answers[i][2] = c.getText();
				answers[i][3] = d.getText();
				System.out.println(i+","+questions[i]+","+corrans[i]+" ; "+answers[i][0]+";"+answers[i][1]+";"+answers[i][2]+";"+answers[i][3]);
				ques.setText("");
				ans.setSelectedIndex(0);
				a.setText("");
				b.setText("");
				c.setText("");
				d.setText("");
				i=i+1;
			}
		}
		catch(Exception err)
		{ System.out.println("Error= "+err); }
	}
}
class removeques implements ActionListener{
	removeques(){
		f3 = new JFrame("View Questions");
		f3.setDefaultCloseOperation(f3.DISPOSE_ON_CLOSE);
		int l = questions.length;
		f3.setLayout(new GridLayout(l,1,10,10));
		for(int k=0;k<l;k++)
		{
			if(questions[k]!=null)
			{
				chk1[k] = new JCheckBox(questions[k]);
				f3.add(chk1[k]);
			}
			else
				continue;
		}
		s2 = new JButton("Remove Selected Questions");
		s3 = new JButton("Remove All Questions");
		f3.add(s2);
		f3.add(s3);
		f3.setSize(500,600);
		f3.setVisible(true);
		s2.addActionListener(this);
		s3.addActionListener(this);
	}
	public void actionPerformed(ActionEvent e3){
		try{
			if(e3.getSource()==s2)
			{
				int p=0;
				for(int a=0;a<chk1.length;a++)
				{
					//if(chk1[k1]!=null)
					//{
						if(chk1[a].isSelected())
						{
							List<String> list1 = new ArrayList<String>(Arrays.asList(questions));
							list1.remove(chk1[a].getText());
							questions = list1.toArray(new String[5]);
							List<String> list2 = new ArrayList<String>(Arrays.asList(corrans));
							list2.remove(a);
							corrans = list2.toArray(new String[5]);
							List<String[]> list3 = new ArrayList<String[]>(Arrays.asList(answers));
							list3.remove(a);
							answers = list3.toArray(new String[5][4]);
						}
						else
							continue;
					/*}
					else
						continue;*/
				}System.out.println("out for");
				f3.dispose();
				new removeques();
			}
			if(e3.getSource()==s3);
			{
				questions = new String[5];
				answers = new String[5][4];
				corrans = new String[5];
				f3.dispose();
				new removeques();

			}
		}
		catch(Exception e){ System.out.println("Error: "+e);}
	}
}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Teacher();
	}
}
