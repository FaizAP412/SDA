import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.StringTokenizer;

public class TP102 {
    private static InputReader in;
    private static PrintWriter out;
    public static int n, m, q;
    public static Long idPelanggan;
    private static PriorityQueue<Pelanggan> antrianPelanggan;
    private static Stack<Long> tumpukanDiskon;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // PriorityQueue yang memprioritaskan berdasarkan budget (descending), kesabaran (ascending), id (ascending)
        antrianPelanggan = new PriorityQueue<>();

        // Read inputs
        n = in.nextInteger(); // read number of fish
        m = in.nextInteger(); // read number of souvenirs
        q = in.nextInteger(); // read number of queries

        // read price of fish
        int[] fishPrice = new int[n];
        for (int i = 0; i < n; i++) {
            fishPrice[i] = in.nextInteger();
        }

        // read price of souvenirs
        int[] souvenirPrice = new int[m];
        for (int i = 0; i < m; i++) {
            souvenirPrice[i] = in.nextInteger();
        }

        // read happiness of souvenirs
        int[] souvenirHappiness = new int[m];
        for (int i = 0; i < m; i++) {
            souvenirHappiness[i] = in.nextInteger();
        }

        // idpelanggan = 0;
        idPelanggan = 0L;
        // tumpukan diskon
        tumpukanDiskon = new Stack<>();

        // read queries
        for (int i = 0; i < q; i++) {
            String query = in.next();
            if (query.equals("A")) {
                int budget = in.nextInteger();
                int patience = in.nextInteger();
                if (!antrianPelanggan.isEmpty()){
                    reducePatience();
                }
                Pelanggan tempPelanggan = new Pelanggan(idPelanggan, budget, patience);
                out.println(idPelanggan);
                antrianPelanggan.offer(tempPelanggan);
                idPelanggan++;
            } else if (query.equals("S")) {
                int price = in.nextInteger();
                if (!antrianPelanggan.isEmpty()){
                    reducePatience();
                }
                lihatHargaTerdekat(price, fishPrice);
            } else if (query.equals("L")) {
                int id = in.nextInteger();
                if (!antrianPelanggan.isEmpty()){
                    reducePatience();
                }
                pelangganKeluar(id);
            } else if (query.equals("D")) {
                int discount = in.nextInteger();
                if (!antrianPelanggan.isEmpty()){
                    reducePatience();
                }
                tambahkanDiskon(discount);
            } else if (query.equals("B")) {
                if (!antrianPelanggan.isEmpty()){
                    reducePatience();
                }
                layaniPelanggan(fishPrice);
                // layaniPelanggan();

            } else if (query.equals("O")) {
                int type = in.nextInteger();
                int budget = in.nextInteger();

                // type 1
                if (type == 1){
                    hitungKebahagiaanMaksimalO1(budget, souvenirPrice, souvenirHappiness);
                } else {
                    hitungKebahagiaanMaksimalO2(budget, souvenirPrice, souvenirHappiness);
                }
            }
        }
            
        out.flush();
        // Don't forget to close/flush the output
        out.close();
    }

    // function to count maximum happiness
    public static void hitungKebahagiaanMaksimalO1(int x, int[] souvenirPrice, int[] souvenirHappiness) {
        int totalBudget = x;
        int length = souvenirPrice.length;
        long[][] tempSum = new long[length + 1][totalBudget + 1];
        boolean[][] getSouvenir = new boolean[length + 1][totalBudget + 1];
    
        for (int i = 1; i <= length; i++) {
            for (int j = 0; j <= totalBudget; j++) {
                if (souvenirPrice[i - 1] <= j) {
                    long temp = souvenirHappiness[i - 1] + tempSum[i - 1][j - souvenirPrice[i - 1]];
                    if (temp > tempSum[i - 1][j]) {
                        tempSum[i][j] = temp;
                        getSouvenir[i][j] = true;
                    } else {
                        tempSum[i][j] = tempSum[i - 1][j];
                    }
                } else {
                    tempSum[i][j] = tempSum[i - 1][j];
                    getSouvenir[i][j] = false;
                }
            }
        }
        
        long happiness = tempSum[length][totalBudget];

        ArrayList<Integer> souvenirBought = new ArrayList<>();
        int leftBudget = totalBudget;
        for (int i = length; i > 0; i--) {
            if (getSouvenir[i][leftBudget]) {
                souvenirBought.add(i);
                leftBudget -= souvenirPrice[i - 1];
            }
        }
        out.println(happiness);
    }

    public static void hitungKebahagiaanMaksimalO2(int x, int[] souvenirPrice, int[] souvenirHappiness) {
        int totalBudget = x;
        int length = souvenirPrice.length;
        long[][] tempSum = new long[length + 1][totalBudget + 1];
        boolean[][] getSouvenir = new boolean[length + 1][totalBudget + 1];
    
        for (int i = 1; i <= length; i++) {
            for (int j = 0; j <= totalBudget; j++) {
                if (souvenirPrice[i - 1] <= j) {
                    long temp = souvenirHappiness[i - 1] + tempSum[i - 1][j - souvenirPrice[i - 1]];
                    if (temp > tempSum[i - 1][j]) {
                        tempSum[i][j] = temp;
                        getSouvenir[i][j] = true;
                    } else {
                        tempSum[i][j] = tempSum[i - 1][j];
                    }
                } else {
                    tempSum[i][j] = tempSum[i - 1][j];
                    getSouvenir[i][j] = false;
                }
            }
        }
        
        long happiness = tempSum[length][totalBudget];

        ArrayList<Integer> souvenirBought = new ArrayList<>();
        int leftBudget = totalBudget;
        for (int i = length; i > 0; i--) {
            if (getSouvenir[i][leftBudget]) {
                souvenirBought.add(i);
                leftBudget -= souvenirPrice[i - 1];
            }
        }
        out.print(happiness);
        for (int i = souvenirBought.size() - 1; i >= 0; i--) {
            out.print(" " + souvenirBought.get(i));
        }
        out.println();
    }

    // public static void layaniPelanggan() {
    public static void layaniPelanggan(int[] fishPrice) {
        if (antrianPelanggan.isEmpty()) {
            out.println("-1"); // No customers in the queue
            return;
        }

        // Get the customer with the highest priority
        Pelanggan pelanggan = antrianPelanggan.poll();
        long budget = pelanggan.budget;
        long originalPatience = pelanggan.originalPatience;

        // find the closest fish price that can be bought by binary search
        int low = 0, high = fishPrice.length - 1;
        long closestFishPrice = -1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (fishPrice[mid] <= budget) {
                closestFishPrice = fishPrice[mid];
                low = mid + 1;  // Look in the lower half
            } else {
                high = mid - 1;  // Look in the upper ihalf
            }
        }

        // Find the closest fish price that can be bought
        // long closestFishPrice = -1;
        // for (int price : fishPrice) {
        //     if (price <= budget && (closestFishPrice == -1 || Math.abs(budget - price) < Math.abs(budget - closestFishPrice))) {
        //         closestFishPrice = (long) price;
        //     }
        // }

        // Check if a fish was bought
        if (closestFishPrice != -1) {
            long discount = 0;
            long remainingBudget = 0;
            if (closestFishPrice == budget) {
                if (!tumpukanDiskon.isEmpty()) {
                    discount = tumpukanDiskon.pop();
                    closestFishPrice = Math.max(1, closestFishPrice-discount);
                } else {
                    discount = 0;
                }
                remainingBudget = budget - closestFishPrice;
            } else {
                remainingBudget = budget - closestFishPrice;
                if (remainingBudget > 0) {
                    tumpukanDiskon.push(remainingBudget);
                }
            }
            out.println(remainingBudget); // Print the remaining budget
            pelanggan.patience = originalPatience;
            pelanggan.budget = remainingBudget;
            antrianPelanggan.offer(pelanggan); // Put the customer back to the queue
        } else {
            out.println(pelanggan.id); // No fish could be bought
        }
    }

    // function to add discount
    public static void tambahkanDiskon(int diskon) {
        tumpukanDiskon.push((long) diskon);
        out.println(tumpukanDiskon.size());
    }

    // function to see the nearest price
    public static void lihatHargaTerdekat(int budget, int[] fishPrice) {
        // int low = 0, high = fishPrice.length - 1;
        // long res = Integer.MAX_VALUE;
        // int mid = (low + high) / 2;

        // while (low <= high) {
        //     mid = (low + high) / 2;

        //     if (fishPrice[mid] <= budget) {
        //         if (Math.abs(fishPrice[mid]-budget) < res) {
        //             res = Math.abs(fishPrice[mid]-budget);
        //         }
        //         low = mid + 1;  // Look for a bigger price within the budget
        //     } else {
        //         if (Math.abs(fishPrice[mid+1]-budget) < res &&  mid+1 < fishPrice.length) {
        //             res = Math.abs(fishPrice[mid]-budget);
        //         } else if (Math.abs(fishPrice[mid]-budget) < res) {
        //             res = Math.abs(fishPrice[mid]-budget);
        //         }
        //         high = mid - 1;  // Look in the lower half
        //     }
        // }
        // out.println(res);

        int low = 0, high = fishPrice.length - 1;
        long closestFishPrice = 0;
        long tempclosestFishPrice1 = -1;
        long tempclosestFishPrice2 = 0;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (fishPrice[mid] <= budget) {
                tempclosestFishPrice1 = fishPrice[mid];
                if (mid + 1 < fishPrice.length) {
                    tempclosestFishPrice2 = fishPrice[mid + 1];
                }
                low = mid + 1;  // Look in the lower half
            } else {
                high = mid - 1;  // Look in the upper ihalf
            }
        }

        if (tempclosestFishPrice1 == -1) {
            closestFishPrice = fishPrice[0];
        } else {
            if (Math.abs(tempclosestFishPrice1 - budget) < Math.abs(tempclosestFishPrice2 - budget)) {
                closestFishPrice = tempclosestFishPrice1;
            } else {
                closestFishPrice = tempclosestFishPrice2;
            }
        }
        long min = Math.abs(budget - closestFishPrice);


        // int min = Integer.MAX_VALUE;
        // for (int price : fishPrice) {
        //     int selisih = Math.abs(budget - price);
        //     if (selisih < min) {
        //         min = selisih;
        //     }
        // }
        out.println(min);
    }

    // function to remove customer from queue
    public static void pelangganKeluar(int idPelanggan) {
        boolean found = false;
        long sisaUang = 0;
        for (Pelanggan currPelanggan: antrianPelanggan) {
            if (currPelanggan.id == idPelanggan) {
                found = true;
                sisaUang = currPelanggan.budget;
                antrianPelanggan.remove(currPelanggan);
                break;
            }
        }
        if (found) {
            out.println(sisaUang);
        } else {
            out.println("-1");
        }
    }


    // function to reduce patience of all customers
    public static void reducePatience() {
        Iterator<Pelanggan> iterator = antrianPelanggan.iterator();
    
        while (iterator.hasNext()) {
            Pelanggan currPelanggan = iterator.next();
            currPelanggan.patience--; 

            if (currPelanggan.patience == 0) {
                iterator.remove();  
            }
        }
    }

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the
    // usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit
    // Exceeded caused by slow input-output (IO)
    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInteger() {
            return Integer.parseInt(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
        }
    }
}

class Pelanggan implements Comparable<Pelanggan> {
    public long id;
    public long budget;
    public long patience;
    public long originalPatience;

    public Pelanggan(long id, long budget, long patience) {
        this.id = id;
        this.budget = budget;
        this.patience = patience;
        this.originalPatience = patience;
    }

    @Override
    public int compareTo(Pelanggan other) {
        if (this.budget != other.budget) {
            return Long.compare(other.budget, this.budget);
        }
        if (this.patience != other.patience) {
            return Long.compare(this.patience, other.patience);
        }
        return Long.compare(this.id, other.id);
    }
}

