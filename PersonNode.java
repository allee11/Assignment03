import java.util.HashMap;

public class PersonNode {
    String email;//email address
    HashMap<String, PersonNode> uniqueEmailSent = new HashMap<>();//unique emails sent to
    int uniqueEmailSentCount = 0;//count for unique emails sent to
    HashMap<String, PersonNode> uniqueEmailReceived = new HashMap<>();//unique emails received from
    int uniqueEmailReceivedCount = 0;//count for unique emails received from
    int teamCount = -1;//team count
    int vertexNum = -1;//vertex number used in graph and disjoint set

    PersonNode() {
        email = null;
    }

    PersonNode(String mailAddress, int num) {//node with email and vertex number
        email = mailAddress;
        vertexNum = num;
    }


    //all functions below are pretty obvious for what they do since its in the name of the function
    public void incUniqueEmailSentCount() {
        uniqueEmailSentCount++;
    }

    public void incUniqueEmailReceivedCount() {
        uniqueEmailReceivedCount++;
    }

    public String getEmail() {
        return email;
    }

    public int getVertexNum() {
        return vertexNum;
    }

    public int getUniqueEmailSentCount() {
        return uniqueEmailSentCount;
    }

    public int getUniqueEmailReceivedCount() {
        return uniqueEmailReceivedCount;
    }

    public int getTeamCount(DisjointSet set) {
        if(teamCount == -1) {//if teamCount = -1, find its teamCount
            teamCount = set.getTeam(vertexNum);//uses function in disjoint set
        }
        return teamCount;
    }
}
