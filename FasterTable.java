

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

/**
 */
public class FasterTable<T> implements Table<T> {
	
	List<Integer> columnPerm;
	List<List<T>> tab;

	int nrows, ncols, nlazycols;

	public FasterTable(Class<T> t) {
		nrows = 0;
		ncols = 0;
		nlazycols = 0;
		tab = new ArrayList<List<T>>();
		columnPerm = new ArrayList<Integer>();
	}

	public int rows() {
		return nrows;
	}

	public int cols() {
		return ncols;
	}

	public T get(int i, int j) {
		if (i < 0 || i > rows() - 1 || j < 0 || j > cols()-1)
			throw new IndexOutOfBoundsException();
		return tab.get(i).get(columnPerm.get(j));
	}

	public T set(int i, int j, T x) {
		if (i < 0 || i > rows() - 1 || j < 0 || j > cols()-1)
			throw new IndexOutOfBoundsException();
		return tab.get(i).set(columnPerm.get(j), x);
	}

	public void addRow(int i) {
		if (i < 0 || i > rows()) throw new IndexOutOfBoundsException();
		nrows++;
		List<T> row = new ArrayList<T>();
		for (int j = 0; j < nlazycols; j++) row.add(null);
		tab.add(i, row);
	}

	public void removeRow(int i) {
		if (i < 0 || i > rows() - 1) throw new IndexOutOfBoundsException();
		nrows--;
		tab.remove(i);
	}

	public void addCol(int j) {
		if (j < 0 || j > cols()) throw new IndexOutOfBoundsException();
		columnPerm.add(j, nlazycols);
		for (int i = 0; i < rows(); i++)
			tab.get(i).add(nlazycols, null);
		ncols++;
		nlazycols++;
	}

	public void removeCol(int j) {
		if (j < 0 || j > cols() - 1) throw new IndexOutOfBoundsException();
		// set the entire column to null
		int jprime = columnPerm.get(j);
		for (int i = 0; i < rows(); i++)
			tab.get(i).set(jprime, null);
		// "remove" the column (note: all this space is wasted now)
		columnPerm.remove(j);
		ncols--;
		// If half our columns are wasted, then rebuild
		if (2*ncols < nlazycols) rebuild();
	}

	protected void rebuild() {
		List<List<T>> tab2 = new ArrayList<List<T>>();
		for (int i = 0; i < rows(); i++) {
			List<T> row = new ArrayList<T>();
			for (int j = 0; j < cols(); j++) {
				row.add(get(i, j));
			}
			tab2.add(row);
		}
		tab = tab2;
		nlazycols = ncols;
		for (int j = 0; j < cols(); j++) {
			columnPerm.set(j, j);
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < rows(); i++) {
			for (int j = 0; j < cols(); j++) {
				sb.append(String.valueOf(get(i, j)));
				sb.append(" ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		Tester.testTable(new FasterTable<Integer>(Integer.class));
	}
}
