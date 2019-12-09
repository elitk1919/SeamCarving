import java.util.*;
import java.util.concurrent.TimeUnit;

class Seam {

    private boolean showingSeam;
    private UWECImage backup; 

    private int getVerticalEnergy(UWECImage im, int x, int y) {
        int height = im.getHeight();
        int upRed = im.getRed(x, (y + 1) % height);
        int downRed = im.getRed(x, (y - 1) % height);
        int upGreen = im.getGreen(x, (y + 1) % height);
        int downGreen = im.getGreen(x, (y - 1) % height);
        int upBlue = im.getBlue(x, (y + 1) % height);
        int downBlue = im.getBlue(x, (y - 1) % height);
        int retval = (int) Math.pow(upRed - downRed, 2) + (int) Math.pow(upGreen - downGreen, 2)
                + (int) Math.pow(upBlue - downBlue, 2);
        //System.out.println("VEnerge: " + retval);
        return retval;
    }

    private int getHorizontalEnergy(UWECImage im, int x, int y) {
        int width = im.getWidth();
        int leftRed = im.getRed((x + 1) % width, y);
        int rightRed = im.getRed((x - 1) % width, y);
        int leftGreen = im.getGreen((x + 1) % width, y);
        int rightGreen = im.getGreen((x - 1) % width, y);
        int leftBlue = im.getBlue((x + 1) % width, y);
        int rightBlue = im.getBlue((x - 1) % width, y);
        int retval = (int) Math.pow(rightRed - leftRed, 2) + 
            (int) Math.pow(rightGreen - leftGreen, 2) + 
            (int) Math.pow(rightBlue - leftBlue, 2);
        //System.out.println("HEnerge: " + retval);
        return retval;

    }

    private int getMin(int... a) {
        int min = a[0];
        for (int i : a) {
            if (i < min) {
                min = i;
            }
        }
        return min;
    }

    private Pair<Integer> getVerticalMinPair(Pair<Integer> child, UWECImage im, int[][] weights) {
        int width = im.getWidth();
        int x = child.getX();
        int y = child.getY();
        //System.out.println("X:" + x + " Y: " + y);
        if (y == 0)
            return null;
        int start;
        int end;
        if (x == 0) {
            start = 0;
            end = 1;
        } else if (x == width - 1) {
            start = width - 2;
            end = start + 1;
        } else {
            start = x - 1;
            end = x + 1;
        }
        int currentMinIndex = start;
        for (int i = start; i <= end; i++) {
            if (weights[y - 1][currentMinIndex] > weights[y - 1][i]) {
                currentMinIndex = i;
            }
        }

        return new Pair<Integer>(currentMinIndex, y - 1);

    }
    
    private Pair<Integer> getHorizontalMinPair(Pair<Integer> child, UWECImage im, int[][] weights) {
        int height = im.getHeight();
        int x = child.getX();
        int y = child.getY();
        System.out.println("X:" + x + " Y: " + y);
        if (x == 0)
            return null;
        int start;
        int end;
        if (y == 0) {
            start = 0;
            end = 1;
        } else if (y == height - 1) {
            start = height - 2;
            end = start + 1;
        } else {
            start = y - 1;
            end = y + 1;
        }
        int currentMinIndex = start;
        for (int i = start; i <= end; i++) {
            if (weights[currentMinIndex][x-1] > weights[i][x-1]) {
                currentMinIndex = i;
            }
        }
        System.out.println("returning " + (y-1) + " " + currentMinIndex);
        return new Pair<Integer>(x - 1, currentMinIndex);

    }

    private int[][] getWeights(UWECImage im) {
        int height = im.getHeight();
        int width = im.getWidth();
        System.out.println("Recalculationg seam");
        int[][] weights = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                weights[i][j] = getHorizontalEnergy(im, j, i) + getVerticalEnergy(im, j, i);
            }
        }
        return weights;
    }

    public void verticalSeamShrink(UWECImage im) {
        int height = im.getHeight();
        int width = im.getWidth();
        int[][] weights = getWeights(im);
        for (int i = 1; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (j == 0) {
                    weights[i][j] += getMin(weights[i - 1][j], weights[i - 1][j + 1]);
                } else if (j == width - 1) {
                    weights[i][j] += getMin(weights[i - 1][j], weights[i - 1][j - 1]);
                } else {
                    weights[i][j] += getMin(weights[i - 1][j], weights[i - 1][j - 1], weights[i - 1][j + 1]);
                }
            }
        }
        int minIndex = 0;
        for (int i = 0; i < width; i++)
            if (weights[height - 1][i] < weights[height - 1][minIndex])
                minIndex = i;
        List<Pair<Integer>> seam = new ArrayList<Pair<Integer>>();
        Pair<Integer> starter = new Pair<Integer>(minIndex, height - 1);
        seam.add(starter);
        int i = height - 1;
        Pair<Integer> next = starter;
        while (next != null) {
            next = getVerticalMinPair(next, im, weights);
            seam.add(next);
            i--;
        }
        for (Pair<Integer> p : seam)
            if (p != null)
                im.setRGB(p.getX(), p.getY(), 255, 0, 0);
        im.repaintCurrentDisplayWindow();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (Pair<Integer> p : seam) {
            if (p != null) {
                int y = p.getY();
                int x = p.getX();
                for (int j = x; j < width - 1; j++) {
                    int red = im.getRed(j + 1, y);
                    int green = im.getGreen(j + 1, y);
                    int blue = im.getBlue(j + 1, y);
                    im.setRGB(j, y, red, green, blue);
                }
            }
        }
        
    }

    public void horizontalSeamShrink(UWECImage im) {
        int height = im.getHeight();
        int width = im.getWidth();
        System.out.println("image height: " + height + " image width: " + width);
        int[][] weights = getWeights(im);
        for (int j = 1; j < width; j++) {
            //System.out.println("J: " + j);
            for (int i = 0; i < height; i++) {
                //System.out.println("\tI: " + i);
                if (i == 0) {
                    weights[i][j] += getMin(weights[i][j - 1], weights[i + 1][j - 1]);
                } else if (i == height - 1) {
                    weights[i][j] += getMin(weights[i][j - 1], weights[i - 1][j - 1]);
                } else {
                    weights[i][j] += getMin(weights[i - 1][j - 1], weights[i][j - 1], weights[i + 1][j - 1]);
                }
            }
        }
        int minIndex = 0;
        for (int i = 0; i < height; i++) {
            if (weights[i][width - 1] < weights[minIndex][width - 1]) minIndex = i;
        }

        List<Pair<Integer>> seam = new ArrayList<Pair<Integer>>();
        Pair<Integer> starter = new Pair<Integer>(width - 1, minIndex);
        seam.add(starter);
        int i = width - 1;
        Pair<Integer> next = starter;
        System.out.println("In while loop");
        while (next != null) {
            next = getHorizontalMinPair(next, im, weights);
            seam.add(next);
            i--;
        }
        System.out.println("Out of while loop");
        System.out.println("In for loop");
        for (Pair<Integer> p : seam)
            if (p != null)
                im.setRGB(p.getX(), p.getY(), 255, 0, 0);
        im.repaintCurrentDisplayWindow();
        System.out.println("Out of for loop");
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (Pair<Integer> p : seam) {
            if (p != null) {
                int y = p.getY();
                int x = p.getX();
                for (int j = y; j < height - 1; j++) {
                    if (j == height - 2) {
                        im.setRGB(x, j, 0, 0, 0);
                    } else { 
                        int red = im.getRed(x, j + 1);
                        int green = im.getGreen(x, j + 1);
                        int blue = im.getBlue(x, j + 1);
                        im.setRGB(x, j, red, green, blue);
                    }
                }
            }
        }

    }
}