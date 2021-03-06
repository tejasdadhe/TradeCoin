import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class AutoTrailTrade implements ActionListener 
{

	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getActionCommand().equals(" Start ")) 
		{	
			
			JFrame f=new JFrame(); 
			JPanel trailingTradePanelInstance= new JPanel();
			
			
			
			trailingTradePanelInstance.setLayout(null);
			trailingTradePanelInstance.setBounds(0,0,550,555);
			trailingTradePanelInstance.setBackground(Color.WHITE);
			trailingTradePanelInstance.add(ChartPlotter.scroll);
			trailingTradePanelInstance.add(ChartPlotter.stopAutoTrailTrade);
			
			System.out.println("Start button has been clicked");
			int a=JOptionPane.showConfirmDialog(f,"<html><h2>Do you want to start Auto Trading?</h2></html>","Start?", JOptionPane.YES_NO_OPTION);
			if(a==JOptionPane.YES_OPTION)
			{  
				
				ChartPlotter.exchg.disable();
				ChartPlotter.pair_name.disable();
				ChartPlotter.st.disable();
				ChartPlotter.instancePanel.revalidate();
				ChartPlotter.instancePanel.repaint();
				Bot.strategy=3;
				System.out.println("Auto Trail trading started");
				ChartPlotter.trailingTradeLog.append("Auto Trail trading started\n");
				ChartPlotter.tradePanel.removeAll();
				ChartPlotter.tradePanel.add(trailingTradePanelInstance);
				ChartPlotter.tradePanel.revalidate();
				ChartPlotter.tradePanel.repaint();
				trailingTradePanelInstance.add(ChartPlotter.heading);
				ChartPlotter.heading.setText("<html><h1>Automatic : Trailing Trade<i> (Running)</i></h1></html>");
								
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
			}
		}	
		
		else if (e.getActionCommand().equals(" Stop ")) 
		{
			JFrame f=new JFrame(); 
			int a=JOptionPane.showConfirmDialog(f,"<html><h2>Are you sure you want to stop?</h2></html>","Stop?", JOptionPane.YES_NO_OPTION);
			if(a==JOptionPane.YES_OPTION)
			{
				Bot.strategy=0;
				System.out.println("Auto Trail trading stopped");
				ChartPlotter.exchg.enable();
				ChartPlotter.pair_name.enable();
				ChartPlotter.st.enable();
				ChartPlotter.instancePanel.revalidate();
				ChartPlotter.instancePanel.repaint();
				
				
				ChartPlotter.tradePanel.removeAll();
				ChartPlotter.tradePanel.add(ChartPlotter.trailingTradePanel);
				ChartPlotter.tradePanel.revalidate();
				ChartPlotter.tradePanel.repaint();
				ChartPlotter.trailingTradePanel.add(ChartPlotter.heading);
				ChartPlotter.heading.setText("<html><h1>Automatic : Trailing Trade<i> (Stopped)</i></h1></html>");
				
				
			}
		}

	}

}
