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
        if(sets[item] < 0) {
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

    public int getTeam(int num) { //since used path compression strategies in disjoint set
        int check = sets[num];
        int count = 0;

        if(check == -1) {//if it is the top of the tree
            count++;//increase since top of tree
            for(int i=0; i < sets.length; i++) {
                if(sets[i] == num) {
                    count++;//increase for every position in array that has data that matches position, num
                }
            }
        } else {//if not the top of tree, check for every number
            count++;//increase for top of tree
            for(int i=0; i < sets.length; i++) {
                if(sets[i] == check) {
                    count++;//increase for every array that also matches check
                }
            }
        }
        return count;//return the count
    }
}
