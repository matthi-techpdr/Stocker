/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2004, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc. 
 * in the United States and other countries.]
 *
 * ------------------------
 * CombinedXYPlotDemo1.java
 * ------------------------
 * (C) Copyright 2003, 2004, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited).
 * Contributor(s):   -;
 *
 * $Id $
 *
 * Changes
 * -------
 * 13-Jan-2003 : Version 1 (DG);
 *
 */
 /*LICENSING

Copyright (c) 2011, Matthew Wolfe, Andy Gospodnetich

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class drawChart extends ApplicationFrame {
	
	runPeriodicTask rpt;

	// Draw a chart by calling drawChart providing a ticker symbol, the path name, and high and low threshold values
	
	public drawChart(final String tickerSymbol, String pathName, double lowThresh, double highThresh) {

        super("Stocker Chart");
        final JFreeChart chart = createCombinedChart(tickerSymbol, pathName, lowThresh, highThresh);
        final ChartPanel panel = new ChartPanel(chart, true, true, true, false, true);
        panel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(panel);
        
        this.pack();
        RefineryUtilities.centerFrameOnScreen(this);
        this.setVisible(true);
    }

    // Creates a combined chart.
    // @return the combined chart.
    private JFreeChart createCombinedChart(String tickerSymbol, String pathName, double lowThresh, double highThresh) {

        // create the plot for the tickerSymbol data
        final XYDataset data1 = createDataset1(tickerSymbol,pathName,lowThresh,highThresh);
        final XYItemRenderer renderer1 = new StandardXYItemRenderer();
        final NumberAxis rangeAxis1 = new NumberAxis("Stock Price ($)");
        final XYPlot subplot1 = new XYPlot(data1, null, rangeAxis1, renderer1);
        subplot1.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        
        // parent plot
        final CombinedDomainXYPlot plot = new CombinedDomainXYPlot(new NumberAxis("Time"));
        plot.setGap(10.0);
        
        // add the subplot
        plot.add(subplot1, 1);
        plot.setOrientation(PlotOrientation.VERTICAL);

        // return a new chart containing the overlaid plot...
        return new JFreeChart(tickerSymbol, JFreeChart.DEFAULT_TITLE_FONT, plot, true);
    }

    // Create the dataset from the specified tickerSymbol
    private XYDataset createDataset1(String tickerSymbol, String pathName, double lowThresh, double highThresh) {

        // create dataset 1...
        final XYSeries series1 = new XYSeries(tickerSymbol);
        
        int entryNum = 1;		// keep track the the number of entries
    	try {					// entryNum is used to provide the x values for the low and high threshold plots

    		BufferedReader input =  new BufferedReader(new FileReader(pathName + tickerSymbol + ".txt"));
    		
    		try {
	            String line = null; //not declared within while loop
	            	            
	            System.out.println("* ------ START INPUT ------- *");
	            while (( line = input.readLine()) != null) {
	                System.out.println(line);
	                series1.add(entryNum, Double.parseDouble(line));
	                entryNum++;
	            }
    		} catch (IOException ioe) {
              ioe.printStackTrace();             
    		} finally {
    			System.out.println("* ------  END INPUT  ------- *\n\n");
    		}
    	} catch (FileNotFoundException fnfe ) {
    		fnfe.printStackTrace();
        }

    	System.out.println("i = " + entryNum);

    	// add low threshold
        final XYSeries series2 = new XYSeries("Low Threshold");
        series2.add(1, lowThresh);
        series2.add(entryNum, lowThresh);

    	// add high threshold
        final XYSeries series3 = new XYSeries("High Threshold");
        series3.add(1, highThresh);
        series3.add(entryNum, highThresh);

        final XYSeriesCollection collection = new XYSeriesCollection();
        collection.addSeries(series1);
        collection.addSeries(series2);
        collection.addSeries(series3);
        return collection;

    }
}
