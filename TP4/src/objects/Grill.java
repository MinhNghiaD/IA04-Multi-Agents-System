package src.objects;

import java.io.File;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;
import java.util.Arrays;
import java.util.HashMap;

public class Grill 
{
/* -------------------------------------------------------------Constructors -------------------------------------------------*/
	public Grill()
	{
		m_matrix = new Vector<Vector<Cell> >();
		
		m_states = new boolean[9][9];
		
		for (int i = 0; i < 9; ++i)
		{
			Vector<Cell> row = new Vector<Cell>();
			
			for (int j = 0; j < 9; ++j)
			{
				row.add(new Cell());
				
				m_states[i][j] = false;
			}
			
			m_matrix.add(row);
		}
	}
	
	public Grill(String fileName)
	{
		m_matrix = new Vector<Vector<Cell> >();
		
		m_states = new boolean[9][9];
		
		for (int i = 0; i < 9; ++i)
		{	
			for (int j = 0; j < 9; ++j)
			{
				m_states[i][j] = false;
			}
		}
		
		File file = new File(fileName);
		
	 	if (! file.exists())
	 	{	
	 		System.out.println("Warning :" + fileName + " does not exist");
			
			for (int i = 0; i < 9; ++i)
			{
				Vector<Cell> row = new Vector<Cell>();
				
				for (int j = 0; j < 9; ++j)
				{
					row.add(new Cell());
				}
				
				m_matrix.add(row);
			}
			
	 		return;
	 	}
	 	
		try 
		{
		    Scanner parser = new Scanner(file);
		      
		    while (parser.hasNextLine()) 
		    {
		    	String data = parser.nextLine();
		    	
		    	Vector<Cell> row = new Vector<Cell>();
		    	
		    	for (int i = 0; i < data.length(); ++i)
				{	
					int possibleValue = data.charAt(i) - '0';
					
					if (possibleValue == 0)
					{
						row.add(new Cell());
						
					}
					else if (possibleValue < 10 && possibleValue > 0)
					{
						row.add(new Cell(possibleValue));
					}
				}
		    	
		    	m_matrix.add(row);
		    }
		  
		    parser.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
	    }
	}
	
/* ---------------------------------------------------------------- Accessors ---------------------------------------------------*/
	
	public void updateCell(Cell cell, int x, int y)
	{
		if (m_states == null)
		{
			System.out.println("mstate is null");
		}
		
		if (!m_states[x][y])
		{
			m_states[x][y] = m_matrix.elementAt(x).elementAt(y).updatePossbileValues(cell.getPossibleValues());
		}
	}
	
	public void updateSudoku(Vector<Cell> cells, int high, int low, int left, int right)
	{
		int counter = 0;
		
		for (int i = high; i <= low; ++i)
		{
			for (int j = left; j <= right; ++j)
			{
				updateCell(cells.elementAt(counter++), i, j);
			}
		}
	}
	
	public String cellsToJson(int high, int low, int left, int right)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		
		// encode position of cells
		map.put("high" , high);
		map.put("low"  , low);
		map.put("left" , left);
		map.put("right", right);
		
		//encode values of cells
		int counter = 0;
		
		for (int i = high; i <= low; ++i)
		{
			for (int j = left; j <= right; ++j)
			{
				map.put("cell"+counter, m_matrix.elementAt(i).elementAt(j).cellToJson());
				
				++counter;
			}
		}
		
		return Cell.mapToJson(map);
	}
	
	public boolean isFinished()
	{
		for (int i = 0; i < 9; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				if (! m_states[i][j])
				{
					return false;
				}
			}
		}
		
		return true;
	}
	
	public void printOutSudoku()
	{
		System.out.println("Sudoku : ");
		
		for (int i = 0; i < 9; ++i)
		{
			String row = new String();
			
			for (int j = 0; j < 9; ++j)
			{
				row += (m_matrix.elementAt(i).elementAt(j).getValue());
				
				row += " ";
			}
			
			System.out.println(row);
		}
	}
	
/*-----------------------------------------------------------Attributes ----------------------------------------------*/
	private Vector<Vector<Cell> > m_matrix;
	private boolean[][] 		  m_states;
}
