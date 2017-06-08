import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

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
    
    
    
    static JFrame frame=new JFrame();
	static JPanel graphPanel=new JPanel();
	static JPanel pricePanel=new JPanel();
	static JPanel instancePanel=new JPanel();
	static JPanel tradePanel=new JPanel();
	static JPanel manualTradePanel=new JPanel();
	static JPanel ScrapPanel=new JPanel();
	
	
    
    static JLabel cp=new JLabel("Current Price :");
    static JLabel bp=new JLabel("Buying Price :");
    static JLabel sp=new JLabel("Selling Price : Not sold Yet");
    static JLabel anc=new JLabel("Anchored at : ");
    static JLabel profit=new JLabel("Max Profit : ");
    static JLabel heading=new JLabel();
    static JLabel hr=new JLabel("<html><hr></html>");
    static JLabel priceLabel=new JLabel("<html><h2>Price : </hr></html>");
    static JLabel volumeLabel=new JLabel("<html><h2>Volume : </hr></html>");
    
    
    static JButton sellButton=new JButton(" Sell ");
    static JButton buyButton=new JButton(" Buy ");
    static JTextField tradePrice = new JTextField(" ");
    static JTextField volume = new JTextField(String.format("%.4f",Bot.volume));
    static String strategy[]={"Manual","Auto : Anchor","Auto : Scripted trade"};
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
    
    
    public void constructGUI()
    {
    	
    }

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
        
		pricePanel.setBounds(5,620,1355,80);
		pricePanel.add(cp);
		pricePanel.add(bp);
		pricePanel.add(sp);
		pricePanel.setBackground(Color.WHITE);
		
		exchg.setBounds(0,0,150,50);
		pair_name.setBounds(155,0,150,50);
		st.setBounds(310,0,240,50);

		instancePanel.add(exchg);
		instancePanel.add(pair_name);
		instancePanel.add(st);
		instancePanel.setBounds(810,5,550,50);
		instancePanel.setLayout(null);

		cp.setBorder(new CompoundBorder(border, margin));
		bp.setBorder(new CompoundBorder(border, margin));
		sp.setBorder(new CompoundBorder(border, margin));
		
		heading.setBounds(20, 5, 200, 50);
		hr.setBounds(20, 55, 510, 5);
		
		
		priceLabel.setBounds(65, 105, 200, 50);
		volumeLabel.setBounds(65, 175, 200, 50);
		tradePrice.setBounds(200, 105, 310, 50);
		tradePrice.setHorizontalAlignment(JTextField.CENTER);
		volume.setBounds(200, 175, 310, 50);
		volume.setHorizontalAlignment(JTextField.CENTER);
		sellButton.setBounds(70, 250, 200, 50);
		buyButton.setBounds(300, 250, 200, 50);
		
		
		
		
		tradePanel.setBounds(810,60,550,555);
		tradePanel.add(manualTradePanel);
		tradePanel.setLayout(null);
		manualTradePanel.setLayout(null);
		manualTradePanel.setBounds(0,0,550,555);
		heading.setText("<html><h1>Manual Trading</h1></html>");
		manualTradePanel.add(heading);
		manualTradePanel.add(priceLabel);
		manualTradePanel.add(tradePrice);
		manualTradePanel.add(volumeLabel);
		manualTradePanel.add(volume);
		manualTradePanel.add(sellButton);
		manualTradePanel.add(buyButton);
		
		manualTradePanel.setBackground(Color.WHITE);
		
		
		
			
		this.add(graph);
		this.add(pricePanel);
		this.add(instancePanel);
		this.add(tradePanel);
		this.add(ScrapPanel);
	    setExtendedState( this.getExtendedState()|JFrame.MAXIMIZED_BOTH );      
        
        
        
        
        
        
       
        
		sellButton.addActionListener(new ConfirmOrder());
		buyButton.addActionListener(new ConfirmOrder());
        



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