package info.puneetsingh.fsm;//fsm stands for fuzzy string matching

import java.util.List;
import java.util.ArrayList;

/**
 * Original @author Kyle Burton
 * Taken from: https://github.com/relaynetwork/fuzzy-string-matching
 * Date: Oct 12, 2010
 * License: Apache V2
 * @author puneet singh [since I heavily modified this]
 * my site: http://www.puneetsingh.info
 * Beware this code is not fully tested yet
 * Text Brew Implementation.  See: http://www.ling.ohio-state.edu/~cbrew/795M/string-distance.html
 * @version 0.01
 */
public class TextBrew {
	public Costs costs = null;

	public TextBrew () {
		costs = new Costs();
	}
	/**
	 * static compare function to compare two string using textbrew algorithm
	 * @param left
	 * @param right
	 * @return value of distance between two texts between 0.0 and 1.0 (greater the better)
	 */
	public static Double compare(String left, String right) {
		int len = Math.min(left.length(), right.length());
		int maxlen = Math.max(left.length(), right.length());
		if ((double) len / (double) maxlen <= 0.5)
			return 0.0;
		if (len == maxlen && left.equals(right))
			return 1.0;
		return (1.0 - ((new TextBrew().computeSimilarity(left,right).cost) / (len)));
	}


	public BrewResult computeSimilarity(String left, String right) {
		if ( (null == left||left.length() == 0) && (null == right||right.length() == 0)) {
			return new BrewResult();
		}

		if ( null == left || left.length() == 0 ) {
			BrewResult result = new BrewResult();
			result.cost = right.length();
			return result;
		}

		if ( null == right || right.length() == 0 ) {
			BrewResult result = new BrewResult();
			result.cost = left.length();
			return result;
		}

		Cell[][] matrix;
		char [] leftChars  = left.toCharArray();
		char [] rightChars = right.toCharArray();

		matrix = new Cell[leftChars.length + 1][];
		for( int rowNum = 0; rowNum < matrix.length; rowNum++ ) {
			matrix[rowNum] = new Cell[rightChars.length + 1];
		}

		matrix[0][0] = new Cell();
		matrix[0][0].cost       = costs.start;
		matrix[0][0].leftChar   = '0';
		matrix[0][0].rightChar  = '0';
		matrix[0][0].hit        = false;
		matrix[0][0].tracebackX = -1;
		matrix[0][0].tracebackY = -1;
		matrix[0][0].action     = ACTION.START;
		matrix[0][0].path       = false;

		// fill in default costs
		for (int idx = 0; idx < rightChars.length; ++idx ) {
			Cell c       = matrix[0][idx+1] = new Cell();
			c.cost       = (idx+1) * costs.insert;
			c.leftChar   = '0';
			c.rightChar  = rightChars[idx];
			c.hit        = false;
			c.tracebackX = 0;
			c.tracebackY = idx;
			c.action     = ACTION.INSERT;
			c.path       = false;
		}

		for (int idx = 0; idx < leftChars.length; ++idx ) {
			Cell c       = matrix[idx+1][0] = new Cell();
			c.cost       = (idx+1) * costs.delete;
			c.leftChar   = leftChars[idx];
			c.rightChar  = '0';
			c.hit        = false;
			c.tracebackX = idx;
			c.tracebackY = 0;
			c.action     = ACTION.DELETE;
			c.path       = false;
		}

		for (Cell [] row : matrix) {
			for (int idx = 0; idx < row.length; ++idx ) {
				if (row[idx] == null) {
					row[idx] = new Cell();
				}
			}
		}

		for ( int leftIdx = 0; leftIdx < leftChars.length; ++leftIdx ) {
			int rowIdx = leftIdx + 1;
			Cell[] row = matrix[rowIdx];
			char leftChar = leftChars[leftIdx];


			for ( int rightIdx = 0; rightIdx < rightChars.length; ++rightIdx ) {
				int colIdx = rightIdx + 1;
				Cell cell = row[colIdx];
				char rightChar = rightChars[rightIdx];
				cell.leftChar = leftChar;
				cell.rightChar = rightChar;

				boolean isHit;
				double baseCost = 0.0;
				cell.action = ACTION.MATCH;
				if ( leftChar == rightChar ) {
					baseCost   += costs.match;
					isHit       = true;
					cell.action = ACTION.MATCH;
				}
				else {
					baseCost     += costs.substitute;
					isHit         = false;
					cell.action   = ACTION.SUBSTITUTE;
				}

				boolean canTranspose = false;
				double transposeCost = 0.0d;
				if ( leftIdx > 1 && rightIdx > 1
						&& leftChars[leftIdx-1] == rightChars[rightIdx]
								&& leftChars[leftIdx]   == rightChars[rightIdx-1]) {
					canTranspose = true;
					transposeCost = matrix[rowIdx-2][colIdx-2].cost + costs.transpose;
				}

				// coming from above is a DELETE
				double upCost     = costs.delete + matrix[rowIdx - 1][colIdx    ].cost;
				// coming from the left is an INSERT
				double leftCost   = costs.insert + matrix[rowIdx    ][colIdx - 1].cost;
				// diagonal is either a MATCH or a SUBSTITUTE
				double upLeftCost = baseCost     + matrix[rowIdx - 1][colIdx - 1].cost;

				double currCost   = upLeftCost;
				cell.tracebackX = colIdx-1;
				cell.tracebackY = rowIdx-1;

				if ( leftCost < currCost ) {
					currCost = leftCost;
					cell.tracebackX = colIdx - 1;
					cell.tracebackY = rowIdx;
					cell.action = ACTION.INSERT;
				}

				if ( upCost < currCost ) {
					currCost = upCost;
					cell.tracebackX = colIdx;
					cell.tracebackY = rowIdx - 1;
					cell.action = ACTION.DELETE;
				}

				if ( canTranspose && transposeCost < currCost ) {
					currCost = transposeCost;
					cell.tracebackX = colIdx - 2;
					cell.tracebackY = rowIdx - 2;
					cell.action = ACTION.TRANSPOSE;
				}

				cell.cost = currCost;
				cell.hit = isHit;
			}
		}

		// now that the matrix is filled in, compute the traceback.
		List<Cell> traceback = new ArrayList<Cell>();
		Cell resultCell = matrix[matrix.length-1][matrix[0].length-1];
		Cell curr  = resultCell;
		curr.path  = true;
		traceback.add(0,curr);
		while (true) {
			if ( curr.tracebackX == -1 && curr.tracebackY == -1 ) {
				break;
			}
			curr = matrix[curr.tracebackY][curr.tracebackX];
			traceback.add(0, curr);
			curr.path = true;
		}

		BrewResult result = new BrewResult();
		//		result.matrix    = matrix;
		result.cost      = resultCell.cost;
		//		result.traceback = traceback;
		return result;
	}

	public enum ACTION {
		NONE, START, MATCH, INSERT, DELETE, TRANSPOSE, SUBSTITUTE
	}

	private static final class Cell {
		public int tracebackX = -1;
		public int tracebackY = -1;
		public ACTION action  = ACTION.NONE;
		public char leftChar  = '0';
		public char rightChar = '0';
		public double cost    = 0.0d;
		public boolean hit    = false;
		public boolean path   = false;

		public String toString () {
			StringBuilder sb = new StringBuilder();
			sb.append("Cell{");
			sb.append(String.format("tb.x=%2d ", tracebackX));
			sb.append(String.format("tb.y=%2d ", tracebackY));
			sb.append(String.format("%s ", action.toString().substring(0,4)));
			sb.append(String.format("l=%c ", leftChar));
			sb.append(String.format("r=%c ", rightChar));
			sb.append(String.format("c=%3.2f ", cost));
			sb.append(String.format("h=%c ", hit ? 't' : 'f'));
			sb.append(String.format("p=%c", path ? 't' : 'f'));
			sb.append("}");
			return sb.toString();
		}

	}
	public static class Costs {
		public double start = 0.0d;
		public double match = 0.0d;
		public double insert = 0.1d;
		public double delete = 15.0d;
		public double transpose = 2.0d;
		public double substitute = 1.0d;
	}

	public class BrewResult {
		public double cost;

	}


}