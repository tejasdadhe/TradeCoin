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
			int a=JOptionPane.showConfirmDialog(f,"Are you sure you want to sell" + Bot.pair+" at rate" +Bot.anchor,null, JOptionPane.YES_NO_OPTION);
			if(a==JOptionPane.YES_OPTION)
			{  
			    try
			    {
					Api.SellCoin(Bot.pair,Bot.anchor);
				} 
			    catch (InterruptedException e1)
			    {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
			}  
		}
		else if (e.getActionCommand().equals(" Buy "))
		{
			System.out.println("Buy button has been clicked");
			int a=JOptionPane.showConfirmDialog(f,"Are you sure you want to buy" + Bot.pair+" at rate" +Bot.anchor,null, JOptionPane.YES_NO_OPTION);
			if(a==JOptionPane.YES_OPTION)
			{  
				try
			    {
					Api.BuyCoin(Bot.pair,Bot.anchor);
				} 
			    catch (InterruptedException e1)
			    {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
			}
		}

	}

}
