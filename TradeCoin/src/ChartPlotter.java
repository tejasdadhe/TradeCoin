import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Second;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;


public class ChartPlotter extends ApplicationFrame 
{

    private static final int COUNT=2 * 60;
	private static final long serialVersionUID = 1L;
	private static final String TITLE = null;
    private static int time = 10000;
    
    
    private Timer timer;
    
    
    static JPanel ScrapPanel=new JPanel();
    static JFrame frame=new JFrame();
	static JPanel graphPanel=new JPanel();
	static JPanel pricePanel=new JPanel();
	static JPanel instancePanel=new JPanel();
	static JPanel tradePanel=new JPanel();
	static JPanel manualTradePanel=new JPanel();
	static JPanel trailingTradePanel=new JPanel();
	
	
    
    static JLabel cp=new JLabel("Current Price :");
    static JLabel bp=new JLabel("Buying Price :");
    static JLabel sp=new JLabel("Selling Price : Not sold Yet");
    static JLabel anc=new JLabel("Anchored at : ");
    static JLabel profit=new JLabel("Max Profit : ");
    static JLabel heading=new JLabel();
    static JLabel hr=new JLabel("<html><hr></html>");
    static JLabel priceLabel=new JLabel("<html><h2>Price : </h2></html>");
    static JLabel volumeLabel=new JLabel("<html><h2>Volume : </h2></html>");
    static JLabel totalLabel=new JLabel("<html><h2>Total : </h2></html>");
    static JLabel fromLabel=new JLabel("<html><h2>Starting from :</h2></html>");
    static JLabel upToLabel=new JLabel("<html><h2>Up to :</h2></html>");
    static JLabel trailingVolumeLabel=new JLabel("<html><h2>Volume to Invest :</h2></html>");
    
    
    static JButton sellButton=new JButton(" Sell ");
    static JButton buyButton=new JButton(" Buy ");
    static JButton startAutoTrailTrade=new JButton(" Start ");
    static JButton stopAutoTrailTrade=new JButton(" Stop ");
    
    static JTextField tradePrice = new JTextField(" ");
    static JTextField volume = new JTextField(String.format("%.4f",Bot.volume));
    static JTextField total = new JTextField(String.format("%.4f",Bot.volume*Bot.achg));
    static JTextField from=new JTextField();
    static JTextField upTo=new JTextField();
    static JTextField trailingVolume=new JTextField();
    
    static String strategy[]={"Manual","Auto : Anchor","Auto : Scripted trade","Auto : Trailing Trade"};
    static String exchange[]={"Kraken","Poloneix","GateHub"};
    static String pairNames[]={"XRPXBT","XRPUSD","XBTUSD"};
    static JComboBox<String> st=new JComboBox<String>(strategy);
    static JComboBox<String> exchg=new JComboBox<String>(exchange);
    static JComboBox<String> pair_name=new JComboBox<String>(pairNames);
    static JLabel st_label=new JLabel("Strategy");
    static JButton b=new JButton("hgfjh");
    
    Border border = cp.getBorder();
    Border margin = new EmptyBorder(25,50,25,50);
    Border buttonMargin = new EmptyBorder(20,50,20,50);
    
    

    public ChartPlotter(final String title,float range ) 
    {
        super(title);
        final DynamicTimeSeriesCollection dataset = new DynamicTimeSeriesCollection(4, COUNT, new Second());
        
        dataset.setTimeBase(new Second(0, 0, 0, 1, 1, 2017));
        dataset.addSeries(primaryData(), 0, "Current Price");
        dataset.addSeries(primaryData(), 1, "Anchor");
        dataset.addSeries(primaryData(), 2, "Buying Price");
        dataset.addSeries(primaryData(), 3, "Selling Price");
        
        JFreeChart chart = createChart(dataset,range);       
        ChartPanel graph=new ChartPanel(chart);
        
        
        graph.setBounds(5, 5, 800, 610);
        graph.setBorder(new CompoundBorder(new EmptyBorder(10,0,10,10), new EmptyBorder(10,0,10,10)));
        graph.setBackground(Color.WHITE);
        
        
        
        Font font1 = new Font("SansSerif", Font.BOLD, 20);
        cp.setFont(font1);
        bp.setFont(font1);
        sp.setFont(font1);
        from.setFont(font1);
        upTo.setFont(font1);
        trailingVolume.setFont(font1);
        
        from.setHorizontalAlignment(JTextField.CENTER);
        upTo.setHorizontalAlignment(JTextField.CENTER);
        trailingVolume.setHorizontalAlignment(JTextField.CENTER);
        
        trailingVolume.setBounds(250, 105, 260, 50);
        from.setBounds(200, 175, 310, 50);
        upTo.setBounds(200, 245, 310, 50);
        
        
		pricePanel.setBounds(5,620,1355,80);
		pricePanel.add(cp);
		pricePanel.add(bp);
		pricePanel.add(sp);
		pricePanel.setBackground(Color.WHITE);
		
		exchg.setBounds(0,0,150,50);
		exchg.setFont(font1);
		pair_name.setBounds(155,0,150,50);
		pair_name.setFont(font1);
		st.setBounds(310,0,240,50);
		st.setFont(font1);

		instancePanel.add(exchg);
		instancePanel.add(pair_name);
		instancePanel.add(st);
		instancePanel.setBounds(810,5,550,50);
		instancePanel.setLayout(null);

		cp.setBorder(new CompoundBorder(border, margin));
		bp.setBorder(new CompoundBorder(border, margin));
		sp.setBorder(new CompoundBorder(border, margin));
		
		heading.setBounds(20, 5, 500, 50);
		hr.setBounds(20, 55, 510, 5);
		
		
		volumeLabel.setBounds(65, 105, 200, 50);
		priceLabel.setBounds(65, 175, 200, 50);
		totalLabel.setBounds(65, 245, 200, 50);
		
		trailingVolumeLabel.setBounds(65, 105, 200, 50);
		fromLabel.setBounds(65, 175, 200, 50);
		upToLabel.setBounds(65, 245, 200, 50);
		
		
		tradePrice.setBounds(200, 175, 310, 50);
		tradePrice.setFont(font1);
		tradePrice.setHorizontalAlignment(JTextField.CENTER);
		volume.setBounds(200, 105, 310, 50);
		volume.setFont(font1);
		volume.setHorizontalAlignment(JTextField.CENTER);
		total.setBounds(200, 245, 310, 50);
		total.setFont(font1);
		total.setHorizontalAlignment(JTextField.CENTER);
		
		
		sellButton.setBounds(70, 350, 200, 50);
		buyButton.setBounds(300, 350, 200, 50);
		startAutoTrailTrade.setBounds(90, 350, 400, 50);
		stopAutoTrailTrade.setBounds(90, 450, 400, 50);
		
		sellButton.setIcon(new ImageIcon(getClass().getResource("/Sell.png")));
	    buyButton.setIcon(new ImageIcon(getClass().getResource("/Buy.png")));
	    startAutoTrailTrade.setIcon(new ImageIcon(getClass().getResource("/StartAutoTrading.png")));
	    stopAutoTrailTrade.setIcon(new ImageIcon(getClass().getResource("/Stop.png")));
		
		
		tradePanel.setBounds(810,60,550,555);
		tradePanel.add(manualTradePanel);
		tradePanel.setLayout(null);
		

		heading.setText("<html><h1>Manual Trading</h1></html>");
		
		manualTradePanel.setLayout(null);
		manualTradePanel.setBounds(0,0,550,555);
		manualTradePanel.add(heading);
		manualTradePanel.add(priceLabel);
		manualTradePanel.add(tradePrice);
		manualTradePanel.add(volumeLabel);
		manualTradePanel.add(volume);
		manualTradePanel.add(totalLabel);
		manualTradePanel.add(total);
		manualTradePanel.add(sellButton);
		manualTradePanel.add(buyButton);	
		manualTradePanel.setBackground(Color.WHITE);
		
		
		trailingTradePanel.setLayout(null);
		trailingTradePanel.setBounds(0,0,550,555);
		trailingTradePanel.setBackground(Color.WHITE);
		trailingTradePanel.add(trailingVolumeLabel);
		trailingTradePanel.add(fromLabel);
		trailingTradePanel.add(upToLabel);
		trailingTradePanel.add(trailingVolume);
		trailingTradePanel.add(from);
		trailingTradePanel.add(upTo);
		trailingTradePanel.add(startAutoTrailTrade);
		
		
		
		
		
			
		this.add(graph);
		this.add(pricePanel);
		this.add(instancePanel);
		this.add(tradePanel);
		this.add(ScrapPanel);
	    setExtendedState( this.getExtendedState()|JFrame.MAXIMIZED_BOTH );      
        
        
        
        
        
        
       
        
		sellButton.addActionListener(new ConfirmOrder());
		buyButton.addActionListener(new ConfirmOrder());
		startAutoTrailTrade.addActionListener(new AutoTrailTrade());
		stopAutoTrailTrade.addActionListener(new AutoTrailTrade());
		
        



        timer = new Timer(time, new ActionListener() {

            float[] newData = new float[4];
            

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                try 
                {
					newData[0] = (float) Api.CurrentPrice("xrpusd");
					newData[1] = (float) Bot.anchor;
					newData[2] = (float) Bot.buyingPrice;
					newData[3] = (float) Bot.sellingPrice;
				} 
                catch (InterruptedException e1) 
                {
					e1.printStackTrace();
				}
                dataset.advanceTime();
                dataset.appendData(newData);
            }
        });
        
        
        st.addItemListener(
        	new ItemListener() 
        	{
            public void itemStateChanged(ItemEvent event) 
            {
            
            	if(event.getStateChange() == ItemEvent.SELECTED && ItemEvent.SELECTED != ItemEvent.DESELECTED)
            	{	
            		tradePanel.removeAll();
            		tradePanel.revalidate();
            		tradePanel.repaint();
            		System.out.println(st.getSelectedIndex() + " selected");
            		if(st.getSelectedIndex()==0)
            		{
            			tradePanel.add(manualTradePanel);
            			manualTradePanel.add(heading);
            			heading.setText("<html><h1>Manual Trading</h1></html>");
            		}
            		else if(st.getSelectedIndex()==3)
            		{
            			tradePanel.add(trailingTradePanel);
            			trailingTradePanel.add(heading);
            			heading.setText("<html><h1>Automatic : Trailing Trade</h1></html>");
            		}
            		
            		
            		
            		
            		
            		
            		
            	}
            }
        });
        
        
        
        
    }

    private float[] primaryData() {
        float [] a = new float[COUNT];
        float cp=0;
        try {
			cp=(float) Api.CurrentPrice("xrpusd");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        for(int i=0;i<a.length;i++)
        	a[i]=cp;
        return a;
    }

    private JFreeChart createChart(final XYDataset dataset,float r) 
    {
        final JFreeChart result = ChartFactory.createTimeSeriesChart(TITLE, "time", "price", dataset, true, true, false);
        final XYPlot plot = result.getXYPlot();
        ValueAxis domain = plot.getDomainAxis();
        domain.setAutoRange(true);
        ValueAxis range = plot.getRangeAxis();
        range.setRange(r*0.9, r*1.1);
        return result;
    }

    public void start() {
        timer.start();
    }

}