

import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Tester {
  public static <T> boolean tableEquals(Table<T> t1, Table<T> t2) {
    if (t1.rows() != t2.rows()) return false;
    if (t1.cols() != t2.cols()) return false;
    for (int i = 0; i < t1.rows(); i++) {
      for (int j = 0; j < t2.cols(); j++) {
        T x1 = t1.get(i, j);
        T x2 = t2.get(i, j);
        if (x1 != null && x2 == null) return false;
        if (x1 == null && x2 != null) return false;
        if (x1 != null && !x1.equals(x2)) return false;
      }
    }
    return true;
  }

  protected static boolean testPart1Correctness(Table<Integer> t) {
    int n = 10000;
		Random r = new java.util.Random(0);
		Table<Integer> t1 = new A2Table<Integer>(Integer.class);
    Table<Integer> t2 = t;
		int i, j;
		for (int _u = 0; _u < n; _u++) {
			switch(r.nextInt(6)) {
				case 0:
					i = r.nextInt(t1.rows()+1);
					t1.addRow(i);
					t2.addRow(i);
					break;
				case 1:
					j = r.nextInt(t1.cols()+1);
					t1.addCol(j);
					t2.addCol(j);
					break;
				case 2:
					if (t1.rows() > 0) {
						i = r.nextInt(t1.rows());
						t1.removeRow(i);
						t2.removeRow(i);
					}
					break;
				case 3:
					if (t1.cols() > 0) {
						j = r.nextInt(t1.cols());
						t1.removeCol(j);
						t2.removeCol(j);
					}
				case 4:
        case 5:
					if (t1.cols() > 0 && t1.rows() > 0) {
						i = r.nextInt(t1.rows());
						j = r.nextInt(t1.cols());
						int x = r.nextInt();
						t1.set(i, j, x);
						t2.set(i, j, x);
					}
					break;
			}
      if (!tableEquals(t1, t2)) return false;
		}
    System.out.print(t1.rows() + "x" + t1.cols() + "...");
    System.out.flush();
    while (t1.cols() > 0) {
      j = r.nextInt(t1.cols());
      t1.removeCol(j);
      t2.removeCol(j);
      if (!tableEquals(t1, t2)) return false;
    }
    while (t1.rows() > 0) {
      i = r.nextInt(t1.rows());
      t1.removeRow(i);
      t2.removeRow(i);
      if (!tableEquals(t1, t2)) return false;
    }
    return true;
  }

  protected static boolean testPart1Performance(Table<Integer> t) {
    int thickness = 750;
    int length = 10000;

    Random r = new Random(1);

    System.out.print(length + "x" + thickness + "...");
    System.out.flush();
    for (int j = 0; j < thickness; j++) {
      t.addCol(j);
    }
    for (int i = 0; i < length; i++) {
      t.addRow(r.nextInt(t.rows()+1));
    }
    while (t.rows() > 0) {
      t.removeRow(r.nextInt(t.rows()));
    }
    while (t.cols() > 0) {
      t.removeCol(t.cols()-1);
    }

    System.out.print(thickness + "x" + length + "...");
    System.out.flush();
    for (int i = 0; i < thickness; i++) {
      t.addRow(i);
    }
    for (int j = 0; j < length; j++) {
      t.addCol(r.nextInt(t.cols()+1));
    }
    while (t.cols() > 0) {
      t.removeCol(r.nextInt(t.cols()));
    }
    while (t.rows() > 0) {
      t.removeRow(t.rows()-1);
    }

    return true;
  }

  public static boolean testPart1(Table<Integer> t) {
		System.out.print("Testing correctness...");
		System.out.flush();
    boolean result = testPart1Correctness(t);
		System.out.println("done");
    if (!result) return result;

    System.out.print("Testing performance...");
    System.out.flush();
    result = testPart1Performance(t);
    System.out.println("done");

    return result;
  }

  public static void testTable(Table<Integer> tab) {
    long start = System.nanoTime();
    boolean result = Tester.testPart1(tab);
    long stop = System.nanoTime();
    double elapsed = (stop-start)/1e9;
    System.out.println("testPart1 returns " + result + " in " + elapsed + "s"
                       + " when testing a " + tab.getClass().getName());
  }

  