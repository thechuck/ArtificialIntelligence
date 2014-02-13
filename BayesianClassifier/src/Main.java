/**************
 * Charles Herb
 * CS441 Bayesian classifier.
 * fall 2013
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Math;

public class Main {

	public static void main(String[] args) throws IOException {
		trainingData train = train("testFiles\\spect-itg.train.csv");
		testingData test = Test("testFiles\\\\spect-itg.test.csv", train);
		double totalratio = (test.getGuessZeros() + test.getGuessOnes())
				/ test.getTests();
		double negratio = test.getGuessZeros() / test.getTrueZeros();
		double posratio = test.getGuessOnes() / test.getTrueOnes();

		System.out.print("itg " + (test.getGuessZeros() + test.getGuessOnes())
				+ "/" + test.getTests() + "(" + totalratio + ")" + "     "
				+ test.getGuessZeros() + "/" + test.getTrueZeros() + "("
				+ negratio + ")" + "    " + test.getGuessOnes() + "/"
				+ test.getTrueOnes() + "(" + posratio + ")" + "\n");

		trainingData train1 = train("\\testFiles\\spect-orig.train.csv");
		testingData test1 = Test("\\testFiles\\spect-orig.test.csv", train1);
		double totalratio1 = (test1.getGuessZeros() + test1.getGuessOnes())
				/ test1.getTests();
		double negratio1 = test1.getGuessZeros() / test1.getTrueZeros();
		double posratio1 = test1.getGuessOnes() / test1.getTrueOnes();
		System.out.print("orig "
				+ (test1.getGuessZeros() + test1.getGuessOnes()) + "/"
				+ test1.getTests() + "(" + totalratio1 + ")" + "     "
				+ test1.getGuessZeros() + "/" + test1.getTrueZeros() + "("
				+ negratio1 + ")" + "    " + test1.getGuessOnes() + "/"
				+ test1.getTrueOnes() + "(" + posratio1 + ")" + "\n");

		trainingData train2 = train("\\testFiles\\spect-resplit.train.csv");
		testingData test2 = Test("\\testFiles\\spect-resplit.test.csv", train2);
		double totalratio2 = (test2.getGuessZeros() + test2.getGuessOnes())
				/ test2.getTests();
		double negratio2 = test2.getGuessZeros() / test2.getTrueZeros();
		double posratio2 = test2.getGuessOnes() / test2.getTrueOnes();
		System.out.print("resplit "
				+ (test2.getGuessZeros() + test2.getGuessOnes()) + "/"
				+ test2.getTests() + "(" + totalratio2 + ")" + "     "
				+ test2.getGuessZeros() + "/" + test2.getTrueZeros() + "("
				+ negratio2 + ")" + "    " + test2.getGuessOnes() + "/"
				+ test2.getTrueOnes() + "(" + posratio2 + ")" + "\n");

	}

	public static class trainingData {
		public int[] getN() {
			return n;
		}

		int[] n;

		public int[][] getF() {
			return f;
		}

		int[][] f;

		public trainingData(int[] n, int[][] f) {
			this.n = n;
			this.f = f;
		}
	}

	public static class testingData {
		public testingData(int[] c, int trueZ, int trueO, int z, int o,
				int tests) {
			this.c = c;
			this.trueZeros = trueZ;
			this.trueOnes = trueO;
			this.guessOnes = o;
			this.guessZeros = z;
			this.tests = tests;

		}

		public int[] getc() {
			return c;
		}

		int[] c;

		public double getTrueZeros() {
			return trueZeros;
		}

		double trueZeros;

		public double getTrueOnes() {
			return trueOnes;
		}

		double trueOnes;

		public double getGuessZeros() {
			return guessZeros;
		}

		double guessZeros;

		public int[] getC() {
			return c;
		}

		public double getGuessOnes() {
			return guessOnes;
		}

		double guessOnes;

		public double getTests() {
			return tests;
		}

		double tests;

	}

	public static trainingData train(String trainFile) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(trainFile));
		String dataRow = in.readLine();
		String[] dataArray = new String[0];
		int[][] f = new int[2][45];
		int[] n = new int[2];
		for (int k = 0; k < f.length; k++) {
			f[0][k] = 0;
			f[1][k] = 0;
			n[1] = 0;
			n[0] = 0;
		}
		while (dataRow != null) {
			dataArray = dataRow.split(",");
			if (Integer.parseInt(dataArray[0]) == 1) {
				n[1] += 1;
				for (int k = 1; k < dataArray.length; k++) {
					if (Integer.parseInt(dataArray[k]) == 1)
						f[1][k] += 1;
				}
			} else if (Integer.parseInt(dataArray[0]) == 0) {
				n[0] += 1;
				for (int k = 1; k < dataArray.length; k++) {
					if (Integer.parseInt(dataArray[k]) == 1) {
						f[0][k] += 1;
					}
				}
			}
			dataRow = in.readLine();
		}

		in.close();

		return new trainingData(n, f);
	}

	public static testingData Test(String testFile, trainingData trainD)
			throws IOException {
		int[] c = new int[45];
		int trueZeros = 0;
		int trueOnes = 0;
		int correctGuessZeros = 0;
		int correctGuessOnes = 0;
		int tests = 0;
		BufferedReader in = new BufferedReader(new FileReader(testFile));
		String dataRow = in.readLine();
		String[] dataArray = new String[0];

		while (dataRow != null) {
			dataArray = dataRow.split(",");
			tests++;
			if (Integer.parseInt(dataArray[0]) == 0) {
				trueZeros++;
			} else if (Integer.parseInt(dataArray[0]) == 1) {
				trueOnes++;
			}

			for (int k = 1; k < dataArray.length; k++) {
				c[k] = Integer.parseInt(dataArray[k]);
			}
			double[] L = new double[2];
			int[] n = trainD.getN();
			int[][] F = trainD.getF();
			int s = 0;
			for (int i = 0; i < 2; i++) {
				L[i] = Math.log10(n[i] + 0.5) - Math.log10(n[0] + n[1] + 0.5);
				for (int j = 0; j < 22; j++) {
					s = F[i][j];
					if (c[j] == 0)
						s = n[i] - s;
					L[i] = L[i] + Math.log10(s + 0.5) - Math.log10(n[i] + 0.5);
				}
			}

			if ((L[1] > L[0]) && Integer.parseInt(dataArray[0]) == 1)
				correctGuessOnes++;

			else if (L[0] > L[1] && Integer.parseInt(dataArray[0]) == 0)
				correctGuessZeros++;

			dataRow = in.readLine();
		}

		in.close();
		return new testingData(c, trueZeros, trueOnes, correctGuessZeros,
				correctGuessOnes, tests);

	}

}
