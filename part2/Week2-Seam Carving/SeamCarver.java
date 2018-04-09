import edu.princeton.cs.algs4.Picture;

import java.awt.Color;
import java.util.Deque;
import java.util.LinkedList;

public class SeamCarver {
    private Picture picture;

    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException();
        this.picture = new Picture(picture);
    }  // create a seam carver object based on the given picture

    public Picture picture() {
        return new Picture(picture);
    }  // current picture

    public int width() {
        return picture.width();
    }  // width of current picture

    public int height() {
        return picture.height();
    }  // height of current picture

    public double energy(int x, int y) {
        if (x < 0 || x > width() - 1 || y < 0 || y > height() - 1)
            throw new IllegalArgumentException();
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) {
            return 1000.0;
        }
        Color xLeft = picture.get(x - 1, y);
        Color xRight = picture.get(x + 1, y);
        Color yTop = picture.get(x, y - 1);
        Color yBottom = picture.get(x, y + 1);
        return Math.sqrt(centralDifference(xLeft, xRight) + centralDifference(yTop, yBottom));
    }  // energy of pixel at column x and row y

    private double centralDifference(Color a, Color b) {
        double rDifference = (a.getRed() - b.getRed()) * (a.getRed() - b.getRed());
        double gDifference = (a.getGreen() - b.getGreen()) * (a.getGreen() - b.getGreen());
        double bDifference = (a.getBlue() - b.getBlue()) * (a.getBlue() - b.getBlue());
        return rDifference + gDifference + bDifference;
    }

    public int[] findHorizontalSeam() {
        double[][] energyMatrix = getEnergyMatrix();
        double[][] distTo = new double[width()][height()];
        return findSeam(distTo, transpose(energyMatrix), width(), height());
    }  // sequence of indices for horizontal seam

    public int[] findVerticalSeam() {
        double[][] energyMatrix = getEnergyMatrix();
        double[][] distTo = new double[height()][width()];
        return findSeam(distTo, energyMatrix, height(), width());
    }

    private int[] findSeam(double[][] distTo, double[][] energyMatrix, int height, int width) {
        for (int j = 1; j < height; j++)
            for (int i = 0; i < width; i++)
                distTo[j][i] = Double.MAX_VALUE;
        for (int j = 1; j < height; j++)
            for (int i = 0; i < width; i++)
                relax(distTo, energyMatrix, i, j, width);
        int minIndex = -1;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < width; i++)
            if (distTo[height - 1][i] < min) {
                min = distTo[height - 1][i];
                minIndex = i;
            }
        Deque<Integer> stack = new LinkedList<>();
        stack.push(minIndex);
        for (int j = height - 1; j > 0; j--) {
            int x = stack.peek();
            minIndex = x;
            min = distTo[j - 1][x] + energyMatrix[j - 1][x];
            if (x > 0 && min > distTo[j - 1][x - 1] + energyMatrix[j - 1][x - 1]) {
                min = distTo[j - 1][x - 1] + energyMatrix[j - 1][x - 1];
                minIndex = x - 1;
            }
            if (x < width - 1 && min > distTo[j - 1][x + 1] + energyMatrix[j - 1][x + 1]) {
                min = distTo[j - 1][x + 1] + energyMatrix[j - 1][x + 1];
                minIndex = x + 1;
            }
            stack.push(minIndex);
        }
        int[] results = new int[stack.size()];
        for (int j = 0; j < height; j++) {
            results[j] = stack.pop();
        }
        return results;
    }


    // sequence of indices for vertical seam
    private void relax(double[][] distTo, double[][] energyMatrix, int x, int y, int width) {
        double min = distTo[y - 1][x] + energyMatrix[y - 1][x];
        if (x > 0 && min > distTo[y - 1][x - 1] + energyMatrix[y - 1][x - 1]) {
            min = distTo[y - 1][x - 1] + energyMatrix[y - 1][x - 1];
        }
        if (x < width - 1 && min > distTo[y - 1][x + 1] + energyMatrix[y - 1][x + 1]) {
            min = distTo[y - 1][x + 1] + energyMatrix[y - 1][x + 1];
        }
        distTo[y][x] = min;
    }

    private double[][] getEnergyMatrix() {
        double[][] energyMatrix = new double[height()][width()];
        for (int j = 0; j < height(); j++)
            for (int i = 0; i < width(); i++)
                energyMatrix[j][i] = energy(i, j);
        return energyMatrix;
    }

    private double[][] transpose(double[][] m) {
        double[][] temp = new double[m[0].length][m.length];
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++)
                temp[j][i] = m[i][j];
        return temp;
    }

    public void removeHorizontalSeam(int[] seam) {
        if (seam == null)
            throw new IllegalArgumentException();
        if (seam.length != width())
            throw new IllegalArgumentException();
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] > height() - 1)
                throw new IllegalArgumentException();
            if (i < seam.length - 1 && Math.abs(seam[i] - seam[i + 1]) > 1)
                throw new IllegalArgumentException();
        }
        if (height() <= 1)
            throw new IllegalArgumentException();
        Picture updatedPicture = new Picture(width(), height() - 1);
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < seam[i]; j++) {
                updatedPicture.setRGB(i, j, picture.getRGB(i, j));
            }
            for (int j = seam[i] + 1; j < height(); j++) {
                updatedPicture.setRGB(i, j - 1, picture.getRGB(i, j));
            }
        }
        this.picture = updatedPicture;

    }// remove horizontal seam from current picture

    public void removeVerticalSeam(int[] seam) {
        if (seam == null)
            throw new IllegalArgumentException();
        if (seam.length != height())
            throw new IllegalArgumentException();
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] > width() - 1)
                throw new IllegalArgumentException();
            if (i < seam.length - 1 && Math.abs(seam[i] - seam[i + 1]) > 1)
                throw new IllegalArgumentException();
        }
        if (width() <= 1)
            throw new IllegalArgumentException();
        Picture updatedPicture = new Picture(width() - 1, height());
        for (int j = 0; j < height(); j++) {
            for (int i = 0; i < seam[j]; i++) {
                updatedPicture.setRGB(i, j, picture.getRGB(i, j));
            }
            for (int i = seam[j] + 1; i < width(); i++) {
                updatedPicture.setRGB(i - 1, j, picture.getRGB(i, j));
            }
        }
        this.picture = updatedPicture;
    }  // remove vertical seam from current picture

    public static void main(String[] args) {
        Picture pic = new Picture(args[0]);
        SeamCarver sc = new SeamCarver(pic);
        System.out.println("Picture width = " + sc.width());
        System.out.println("Picture height = " + sc.height());

//        System.out.print("Horizontal Seam = ");
//        int[] horSeam = sc.findHorizontalSeam();
//        for (int i = 0; i < horSeam.length; i++) {
//            System.out.print(horSeam[i] + " ");
//        }
//        System.out.println();
//        sc.removeHorizontalSeam(horSeam);


        System.out.print("Vertical Seam = ");
        int[] verSeam = sc.findVerticalSeam();
        for (int i = 0; i < verSeam.length; i++) {
            System.out.print(verSeam[i] + " ");
        }
        System.out.println();
        sc.removeVerticalSeam(verSeam);

//        System.out.println("Energies after removal = ");
//        for (int row = 0; row < sc.height(); row++) {
//            for (int col = 0; col < sc.width(); col++) {
//                System.out.print(sc.energy(col, row) + " ");
//            }
//            System.out.println();
//        }
//        System.out.println();
    }

}