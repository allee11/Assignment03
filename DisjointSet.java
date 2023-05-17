public class DisjointSet {
    //most are copy and pasted from lecture slides
    int[] sets;

    public void createSets(int items) {
        sets = new int[items];

        for (int i = 0; i < items; i++) {
            sets[i] = -1;
        }
    }

    public int find(int item) {
        if(item < 0 || sets[item] < 0) {
            return item;
        }

        sets[item] = find(sets[item]);
        return sets[item];
    }

    public void union(int item1, int item2) {
        int set1 = find(item1);
        int set2 = find(item2);

        if (set1 != set2 && sets[set2] < sets[set1]) {
            sets[set2] = sets[set1] + sets[set2];
            sets[set1] = set2;
        } else if (set1 != set2) {
            sets[set1] = sets[set1] + sets[set2];
            sets[set2] = set1;
        }
    }

    public int getTeam(int num) { //get team count
        int check = num;//vertex number

        while(sets[check] > -1) {//loop until top of the tree
            check = sets[check];
        }
        return sets[check] * -1;//return team count
    }
}
