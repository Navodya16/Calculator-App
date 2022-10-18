import java.awt.EventQueue;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Stack;

public class CalculatorApp {
    JFrame frame;
    String result="";
    String expression="";
    String error=""; //to display errors
	
    //initial tokens in infix order
    ArrayList<String> token=new ArrayList<String>();
	
    //for key handling
    boolean num=false;  //to check whether the last character in the expression is a number 
    boolean dot=false;  //to check whether the last character in the expression is a dot
    
    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CalculatorApp window = new CalculatorApp();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	});
    }
    
    CalculatorApp(){
        prepareGui();
    }
    
    //precedence method
	private int precedence(String x)
	{
		int p=10;
		switch(x) {
		case "+":
			p=1;
			break;
		case "-":
			p=2;
			break;
		case "x":
			p=3;
			break;
		case "/":
			p=4;
			break;
		}
                
		return p;
	}
	
	//operator checking
	private boolean isoperator(String x)
	{
		if(x.equals("+") || x.equals("-") || x.equals("x") || x.equals("/") || x.equals("sin") || x.equals("cos") || x.equals("tan") || x.equals("sinI") || x.equals("cosI") || x.equals("tanI"))
			return true;
		else 
			return false;
	}
	
    //infix to postfix conversion of the expression
	private String infixTopostfix()
	{
		Stack<String> s=new Stack<String>();
		String y;
		int flag;
		String p="";   //p==postfix expression
		token.add(")");
		s.push("(");
		for(String i: token) {
			if(i.equals("(")){
				s.push(i);
			}else if(i.equals(")")){
				y=s.pop();
				while(!y.equals("("))
				{
					p=p+y+",";
					y=s.pop();
				}
			}else if(isoperator(i)){
				y=s.pop();
				flag=0;
				if(isoperator(y) && precedence(y)>precedence(i)){
					p=p+y+",";
					flag=1;
				}
				if(flag==0)
					s.push(y);
				
				s.push(i);
			}else{
				p=p+i+",";
			}
		}
		while(!s.empty()) {
			y=s.pop();
			if(!y.equals("(") && !y.equals(")")) {
				p+=y+",";
			}
		}
		return p;
	}
        
    //for actual calculation with binary operators
	private double calculate(double x,double y,String c)
	{
		double res=0;
		switch(c)
		{
			case "-":
				res= x-y;
				break;
			case "+":
				res= x+y;
				break;
			case "x":
				res= x*y;
				break;
			case "/":
				res= x/y;
				break;
			default :
				res= 0;
		}
		return res;
	}
	
	//calculation with unary operators
	private double calculate(double y,String c) {
		double res=0;
                
		switch(c) {
		case "sin":
			res= Math.sin(y*(Math.PI/180));
			break;
		case "cos":
			res = Math.cos(y*(Math.PI/180));
			break;
		case "tan":
			res =Math.tan(y*(Math.PI/180));
			break;
		case "sinI":
			res= (Math.asin(y))*(180/Math.PI);
			break;
		case "cosI":
			res= (Math.acos(y))*(180/Math.PI);
			break;
		case "tanI":
			res=(Math.atan(y))*(180/Math.PI);
			break;
		}
		return res;
	}
	//to evaluate postfix expressions using stack
	private double Eval(String p)
	{	
		String tokens[] = p.split(",");
		ArrayList<String> token2=new ArrayList<String>();
		for(int i=0; i<tokens.length; i++) {
			if(! tokens[i].equals("") && ! tokens[i].equals(" ") && ! tokens[i].equals("\n") && ! tokens[i].equals("  ")) {
				token2.add(tokens[i]);  // tokens from post fix form p actual tokens for calculation
			}
		}
		
		Stack<Double> s=new Stack<Double>();
		double x,y;
		for(String  i:token2) {
			if(isoperator(i)){
				//if unary operator
				if(i.equals("sin") ||i.equals("cos") ||i.equals("tan") ||i.equals("sinI") ||i.equals("cosI") ||i.equals("tanI") ) {
					y=s.pop();
					s.push(calculate(y,i));
				}else {
					//for binary operators
					y=s.pop();
					x=s.pop();
					s.push(calculate(x,y,i));
				}
			}else{
				s.push(Double.valueOf(i));
			}
		}
		double res=1;
		while(!s.empty()) {
			res*=s.pop();
		}
		return res;  //final result
	}

	//actual combined method for calculation 
	private void calculateMain() {
		String tokens[]=expression.split(",");
		for(int i=0; i<tokens.length; i++) {
			if(! tokens[i].equals("") && ! tokens[i].equals(" ") && ! tokens[i].equals("\n") && ! tokens[i].equals("  ")) {
				token.add(tokens[i]);  //adding token to token array list from expression 
			}
		}
		try {
			double res = Eval(infixTopostfix());
			result= Double.toString(res);
		}catch(Exception e) {
			error = "Error";
		}
	}
	
    public void prepareGui(){
        frame = new JFrame();
        frame.setTitle("My Calculator"); //title of the JFrame
        frame.setSize(300,590); //size of the frame
        frame.getContentPane().setLayout(null); //set the layout
        frame.getContentPane().setBackground(Color.white); //backgroung color=white
        frame.setResizable(false); //cannot resize
        frame.setLocationRelativeTo(null); //set the location to the center of the screen
        frame.setVisible(true); //set the windows visibility
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //set default close operation
        
        //create all the components
        //set the properties of label
        JLabel label = new JLabel();
        label.setBounds(0, 0, 270, 50);
        label.setForeground(Color.blue);
        frame.add(label);
        
        //set the properties of textfield
        JTextField textField = new JTextField();
        textField.setBounds(10, 40, 270, 40);
        textField.setFont(new Font("Arial", Font.BOLD, 20));
        textField.setEditable(false);
        textField.setHorizontalAlignment(SwingConstants.RIGHT);
        frame.add(textField);
        
        //set the properties of clear button
        JButton bClear = new JButton("C");
        bClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField.setText("0");
				label.setText("");
				expression ="";
				token.clear();
				result="";
				num=false;
				dot=false;
				error="";
			}
	});
        bClear.setBounds(80, 110, 60, 40);
        bClear.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(bClear);
        
        //set the properties of delete button
        JButton bDel = new JButton("Del");
        bDel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s=textField.getText();
				if(s != "0" && s.length() > 1) {
					String newString = s.substring(0,s.length()-1);
					textField.setText(newString);
					if(expression.charAt(expression.length()-1)=='.') {
						dot=false;
					}
					if(expression.charAt(expression.length()-1) == ',') {
						expression = expression.substring(0,expression.length()-2);
					}else {
						expression = expression.substring(0,expression.length()-1);
					}
				}else {
					textField.setText("0");
					expression="";
				}
			}
	});
        bDel.setBounds(150, 110, 60, 40);
        bDel.setFont(new Font("Arial", Font.BOLD, 15));
        frame.add(bDel);
        
        //set properties for sin button
        JButton bSin = new JButton("sin");
        bSin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(! "0".equals(textField.getText())) {
					textField.setText(textField.getText()+"sin(");
				}else {
					textField.setText("sin(");
				}
				expression+=",sin,(";
				num=false;
				dot=false;
			}
	});
        bSin.setBounds(10, 170, 60, 40);
        bSin.setFont(new Font("Arial", Font.BOLD, 15));
        frame.add(bSin);
        
        //set the properties of button opBracket
        JButton opBracket = new JButton ("(");
        opBracket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(! "0".equals(textField.getText())) {
					textField.setText(textField.getText()+"(");
				}else {
					textField.setText("(");
				}
				expression+=",(";
				num=false;
				dot=false;
			}
	});
        opBracket.setBounds(220, 170, 60, 40);
        opBracket.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(opBracket);
        
        //set the properties of button closeBracket
        JButton closeBracket = new JButton (")");
        closeBracket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(! "0".equals(textField.getText())) {
					textField.setText(textField.getText()+")");
				}else {
					textField.setText(")");
				}
				expression+=",)";
				num=false;
				dot=false;
			}
	});
        closeBracket.setBounds(220, 230, 60, 40);
        closeBracket.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(closeBracket);
        
        //set the properties of button cos
        JButton bCos = new JButton("cos");
        bCos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(! "0".equals(textField.getText())) {
					textField.setText(textField.getText()+"cos(");
				}else {
					textField.setText("cos(");
				}
				expression+=",cos,(";
				num=false;
				dot=false;
			}
		});
        bCos.setBounds(80, 170, 60, 40);
        bCos.setFont(new Font("Arial", Font.BOLD, 15));
        frame.add(bCos);
        
        //set the properties of button divide
        JButton bDivide = new JButton("<html><body><span>&#247;</span></body></html>");
        bDivide.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s=textField.getText();
				if(s.equals("0")) {
					expression+="0";
				}
//				System.out.println(s);
				if(s.charAt(s.length()-1)== '-' || s.charAt(s.length()-1)== 'x' || s.charAt(s.length()-1) == '+') {
					String newString = s.substring(0,s.length()-1);
					textField.setText(newString+Character.toString((char)247));
					expression = expression.substring(0,expression.length()-1);
//					expression += Character.toString((char)247);
					expression += "/";
				}else if(s.charAt(s.length()-1)!= (char)247) {	
					textField.setText(s+Character.toString((char)247));	
					expression+=",/";
//					expression += Character.toString((char)247);
				}else {
					textField.setText(s);	
				}
				num=false;
				dot=false;
			}
	});
        bDivide.setBounds(220, 290, 60, 40);
        bDivide.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(bDivide);
        
        //set the properties of button tan
        JButton bTan = new JButton("tan");
        bTan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(! "0".equals(textField.getText())) {
					textField.setText(textField.getText()+"tan(");
				}else {
					textField.setText("tan(");
				}
				expression+=",tan,(";
				num=false;
				dot=false;
			}
	});
        bTan.setBounds(150, 170, 60, 40);
        bTan.setFont(new Font("Arial", Font.BOLD, 15));
        frame.add(bTan);
        
         //set the properties of button multiplication
        JButton bMul = new JButton("x");
        bMul.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String s=textField.getText();
				if(s.equals("0")) {
					expression+="0";
				}
				if(s.charAt(s.length()-1)== '-' || s.charAt(s.length()-1)== '+' || s.charAt(s.length()-1) == (char)(247)) {
					String newString = s.substring(0,s.length()-1);
					newString += "x";
					textField.setText(newString);
					expression = expression.substring(0,expression.length()-1);
					expression += "x";
				}else if(s.charAt(s.length()-1)!= 'x') {
					s += "x";	
					textField.setText(s);
					expression+=",x";
				}else {
					textField.setText(s);	
				}
				num=false;
				dot=false;
			}
	});
        bMul.setBounds(220, 350, 60, 40);
        bMul.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(bMul);
        
        //set the properties of button 1
        JButton b1 = new JButton("1");
        b1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(! "0".equals(textField.getText())) {
					textField.setText(textField.getText()+"1");
				}else {
					textField.setText("1");
				}
				if(num) {
					expression+="1";
				}else {
					expression+=",1";
				}
				num=true;
			}
	});
        b1.setBounds(10, 410, 60, 40);
        b1.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(b1);
        
        //set the properties of button 2
        JButton b2 = new JButton("2");
        b2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(! "0".equals(textField.getText())) {
					textField.setText(textField.getText()+"2");
				}else {
					textField.setText("2");
				}
				if(num) {
					expression+="2";
				}else {
					expression+=",2";
				}
				num=true;
			}
	});
        b2.setBounds(80, 410, 60, 40);
        b2.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(b2);
        
        //set the properties of button 3
        JButton b3 = new JButton("3");
        b3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(! "0".equals(textField.getText())) {
					textField.setText(textField.getText()+"3");
				}else {
					textField.setText("3");
				}
				if(num) {
					expression+="3";
				}else {
					expression+=",3";
				}
				num=true;
			}
	});
        b3.setBounds(150, 410, 60, 40);
        b3.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(b3);
        
        //set the properties of button 4
        JButton b4 = new JButton("4");
        b4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(! "0".equals(textField.getText())) {
					textField.setText(textField.getText()+"4");
				}else {
					textField.setText("4");
				}
				if(num) {
					expression+="4";
				}else {
					expression+=",4";
				}
				num=true;
			}
	});
        b4.setBounds(10, 350, 60, 40);
        b4.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(b4);
        
        //set the properties of button 5
        JButton b5 = new JButton("5");
        b5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(! "0".equals(textField.getText())) {
					textField.setText(textField.getText()+"5");
				}else {
					textField.setText("5");
				}
				if(num) {
					expression+="5";
				}else {
					expression+=",5";
				}
				num=true;
			}
	});
        b5.setBounds(80, 350, 60, 40);
        b5.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(b5);
        
        //set the properties of button 6
        JButton b6 = new JButton("6");
        b6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(! "0".equals(textField.getText())) {
					textField.setText(textField.getText()+"6");
				}else {
					textField.setText("6");
				}
				if(num) {
					expression+="6";
				}else {
					expression+=",6";
				}
				num=true;
			}
	});
        b6.setBounds(150, 350, 60, 40);
        b6.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(b6);
        
         //set the properties of button 7
        JButton b7 = new JButton("7");
        b7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(! "0".equals(textField.getText())) {
					textField.setText(textField.getText()+"7");
				}else {
					textField.setText("7");
				}
				if(num) {
					expression+="7";
				}else {
					expression+=",7";
				}
				num=true;
			}
		});
        b7.setBounds(10, 290, 60, 40);
        b7.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(b7);
        
        //set the properties of button 8
        JButton b8 = new JButton("8");
        b8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(! "0".equals(textField.getText())) {
					textField.setText(textField.getText()+"8");
				}else {
					textField.setText("8");
				}
				if(num) {
					expression+="8";
				}else {
					expression+=",8";
				}
				num=true;
			}
		});
        b8.setBounds(80, 290, 60, 40);
        b8.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(b8);
        
        //set the properties of button 9
        JButton b9 = new JButton("9");
        b9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(! "0".equals(textField.getText())) {
					textField.setText(textField.getText()+"9");
				}else {
					textField.setText("9");
				}
				if(num) {
					expression+="9";
				}else {
					expression+=",9";
				}
				num=true;
			}
		});
        b9.setBounds(150, 290, 60, 40);
        b9.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(b9);
        
        //set the properties of button substract
        JButton bSubstract = new JButton("-");
        bSubstract.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s=textField.getText();
				if(s.equals("0")) {
					expression+="0";
				}
				if(s.charAt(s.length()-1)== '+') {
					String newString = s.substring(0,s.length()-1);
					newString += "-";
					expression = expression.substring(0,expression.length()-1);
					expression += "-";
					textField.setText(newString);
				}else if(s.charAt(s.length()-1)!= '-') {
					s += "-";	
					textField.setText(s);
					expression += ",-";
				}else {
					textField.setText(s);	
				}
				num=false;
				dot=false;
			}
	});
        bSubstract.setBounds(220, 410, 60, 40);
        bSubstract.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(bSubstract);
        
        //set the properties of button dot
        JButton bDot = new JButton(".");
        bDot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s=textField.getText();
				if(s.charAt(s.length()-1)!= '.') {
					if(num && dot==false) {
						expression+=".";
						s += ".";
					}else if(num==false && dot ==false){
						expression+=",.";
						s += ".";
					}
				}
				num=true;
				dot=true;
				textField.setText(s);	
			}
	});
        bDot.setBounds(10, 470, 60, 40);
        bDot.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(bDot);
        
        //set the properties of button 0
        JButton b0 = new JButton("0");
        b0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if("0".equals(textField.getText())) {
					textField.setText("0");
				}else {
					textField.setText(textField.getText()+"0");
					if(num) {
						expression+="0";
					}
					else {
						expression+=",0";
					}
				}
				num=true;
			}
	});
        b0.setBounds(80, 470, 60, 40);
        b0.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(b0);
        
        //set the properties of button =
        JButton bEqual = new JButton("=");
        bEqual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				//calculation
				calculateMain();
				if(!error.equals("")) {
					textField.setText(error);
					expression="";
					dot=false;
					num=false;
				}else {
					String s="";
					token.remove(token.size()-1);
					for(String i: token) {
						if(i.equals("/")) {
							s+=Character.toString((char)247);
						}
                                                else {
							s+=i;
						}
					}
					label.setText(s+"=");
					textField.setText(result);
					
					expression = result;
					dot=true;
					num=true;
				}
				error="";
				token.clear();
			}
	});
        bEqual.setBounds(220, 470, 60, 40);
        bEqual.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(bEqual);
        
        //set the properties of button +
        JButton bAdd = new JButton("+");
        bAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s=textField.getText();
				if(s.equals("0")) {
					expression+="0";
				}
				if(s.charAt(s.length()-1)== '-' || s.charAt(s.length()-1)== 'x' || s.charAt(s.length()-1) == (char)(247)) {
					String newString = s.substring(0,s.length()-1);
					newString += "+";
					textField.setText(newString);
					expression = expression.substring(0,expression.length()-1);
					expression += "+";
				}else if(s.charAt(s.length()-1)!= '+') {
					s += "+";	
					textField.setText(s);
					expression+=",+";
				}else {
					textField.setText(s);	
				}
				num=false;
				dot=false;
			}
	});
        bAdd.setBounds(150, 470, 60, 40);
        bAdd.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(bAdd);
        
        //set the properties of button sinInv
        JButton bSinInv = new JButton("sinI");
        bSinInv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(! "0".equals(textField.getText())) {
					textField.setText(textField.getText()+"sinI(");
				}else {
					textField.setText("sinI(");
				}
				expression+=",sinI,(";
				num=false;
				dot=false;
			}
	});
        bSinInv.setBounds(10, 230, 60, 40);
        bSinInv.setFont(new Font("Arial", Font.BOLD, 15));
        frame.add(bSinInv);
        
        //set the properties of button cosInv
        JButton bCosInv = new JButton("cosI");
        bCosInv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(! "0".equals(textField.getText())) {
					textField.setText(textField.getText()+"cosI(");
				}else {
					textField.setText("cosI(");
				}
				expression+=",cosI,(";
				num=false;
				dot=false;
			}
	});
        bCosInv.setBounds(80, 230, 60, 40);
        bCosInv.setFont(new Font("Arial", Font.BOLD, 12));
        frame.add(bCosInv);
        
        //set the properties of button tanInv
        JButton bTanInv = new JButton("tanI");
        bTanInv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(! "0".equals(textField.getText())) {
					textField.setText(textField.getText()+"tanI(");
				}else {
					textField.setText("tanI(");
				}
				expression+=",tanI,(";
				num=false;
				dot=false;
			}
	});
        bTanInv.setBounds(150, 230, 60, 40);
        bTanInv.setFont(new Font("Arial", Font.BOLD, 15));
        frame.add(bTanInv);
        
    }
}
