import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ConfirmOrder implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		JFrame f=new JFrame(); 
		
		if (e.getActionCommand().equals(" Sell ")) 
		{
			System.out.println("Sell button has been clicked");
			int a=JOptionPane.showConfirmDialog(f,"Are you sure you want to sell?",null, JOptionPane.YES_NO_OPTION);
			if(a==JOptionPane.YES_OPTION)
			{  
			    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
			    System.out.println("sold");
			}  
		}
		else if (e.getActionCommand().equals(" Buy "))
		{
			System.out.println("Buy button has been clicked");
			int a=JOptionPane.showConfirmDialog(f,"Are you sure you want to buy?",null, JOptionPane.YES_NO_OPTION);
			if(a==JOptionPane.YES_OPTION)
			{  
			    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
			    System.out.println("bought");
			}
		}

	}

}
