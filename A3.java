import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class A3 {
    static HashMap<String, PersonNode> emailMap = new HashMap<>();//contains all unique emails
    static Graph graph = new Graph(200000);//graph
    static DisjointSet djs = new DisjointSet();//disjoint set
    static int count = 0;//vertex number of each node

    public static void readFile(File file) {
        try {
            //https://stackoverflow.com/questions/1844688/how-to-read-all-files-in-a-folder-from-java
            //read files in folder

            for (File fileEntry : file.listFiles()) {
                if (fileEntry.isDirectory()) {
                    readFile(fileEntry);//recursive call if directory
                } else {//if it is a file
                    //WARNING: the next 300 lines are mostly a strategy I used to determine whether a file was an email or not
                    //will write about how I decided to determine what is an email or not in the readMe file
                    if(validMail(fileEntry)) {//check if valid email
                        //read file and make new Node
                        FileReader fileRead = new FileReader(fileEntry);
                        BufferedReader reader = new BufferedReader(fileRead);//reader
                        String line;//line for reader
                        PersonNode sentNode = new PersonNode();//node for sender of email, null email for now
                        String sentEmail = "";//email of the sender
                        //all booleans are used to make sure email is read properly, it is kind of like a priority system
                        boolean finishFrom = false;
                        boolean foundTo = false;
                        boolean finishFile = false;
                        boolean foundCC = false;
                        boolean foundBCC = false;

                        while((line = reader.readLine()) != null) {
                            if(line.startsWith("X-From:")) {//found that this was the cutoff if reading only to Bcc:
                                finishFile = true;
                            }

                            if(finishFile) {//stop reading
                                break;
                            }

                            //most of the lines inside each section are repeated, so I am probably only going to cover them once

                            if(foundBCC && !finishFile) {
                                String[] addresses = line.trim().split(",");//split on space

                                for(int i=0; i < addresses.length; i++) {
                                    addresses[i] = addresses[i].trim();//trim

                                    //create new Node if the HashMap does not contain the valid email address
                                    if(!emailMap.containsKey(addresses[i]) && addresses[i].contains("@enron") && !addresses[i].contains(" ") && !addresses[i].contains("<")) {
                                        String email = addresses[i];
                                        PersonNode node = new PersonNode(email, count);

                                        //condition probably not required
                                        //just adds everything necessary like increased counter for email sent and received for both nodes
                                        //also connects them in graph and disjoint set and increase count since a new Node and each node needs a unique count for vertex number
                                        if(!sentNode.uniqueEmailSent.containsKey(email) && sentNode.getEmail() != null && !email.equals(sentEmail)) {
                                            sentNode.uniqueEmailSent.put(email, node);
                                            sentNode.incUniqueEmailSentCount();
                                            node.uniqueEmailReceived.put(sentEmail, sentNode);
                                            node.incUniqueEmailReceivedCount();
                                            graph.addEdge(sentNode, node);
                                            djs.union(sentNode.getVertexNum(), node.getVertexNum());
                                            count++;
                                            emailMap.put(email, node);//put in HashMap
                                        }
                                    } else if(emailMap.containsKey(addresses[i]) && addresses[i].contains("@enron") && !addresses[i].contains(" ")){
                                        PersonNode node = emailMap.get(addresses[i]);//get the node from HashMap based on email

                                        //check if it is already in the unique HashMap already or not
                                        //if not, do the same as above for when a new Node is created
                                        if(!sentNode.uniqueEmailSent.containsKey(addresses[i]) && sentNode.getEmail() != null) {
                                            sentNode.uniqueEmailSent.put(addresses[i], node);
                                            sentNode.incUniqueEmailSentCount();
                                            node.uniqueEmailReceived.put(sentEmail, sentNode);
                                            node.incUniqueEmailReceivedCount();
                                            graph.addEdge(sentNode, node);
                                            djs.union(sentNode.getVertexNum(), node.getVertexNum());
                                            emailMap.put(addresses[i], node);//put the edited node back in
                                        }
                                    }
                                }
                            }

                            if(line.startsWith("Bcc:") && line.contains("@") && finishFrom) {
                                line = line.substring(5);//substring to skip past the Bcc:, common occurrence for first line
                                String[] addresses = line.trim().split(",");

                                for(int i=0; i < addresses.length; i++) {
                                    addresses[i] = addresses[i].trim();

                                    if(!emailMap.containsKey(addresses[i]) && addresses[i].contains("@enron") && !addresses[i].contains(" ") && !addresses[i].contains("<")) {
                                        String email = addresses[i];
                                        PersonNode node = new PersonNode(email, count);

                                        if(!sentNode.uniqueEmailSent.containsKey(email) && sentNode.getEmail() != null && !email.equals(sentEmail)) {
                                            sentNode.uniqueEmailSent.put(email, node);
                                            sentNode.incUniqueEmailSentCount();
                                            node.uniqueEmailReceived.put(sentEmail, sentNode);
                                            node.incUniqueEmailReceivedCount();
                                            graph.addEdge(sentNode, node);
                                            djs.union(sentNode.getVertexNum(), node.getVertexNum());
                                            count++;
                                            emailMap.put(email, node);
                                        }
                                    } else if(emailMap.containsKey(addresses[i]) && addresses[i].contains("@enron") && !addresses[i].contains(" ")){
                                        PersonNode node = emailMap.get(addresses[i]);

                                        if(!sentNode.uniqueEmailSent.containsKey(addresses[i]) && sentNode.getEmail() != null) {
                                            sentNode.uniqueEmailSent.put(addresses[i], node);
                                            sentNode.incUniqueEmailSentCount();
                                            node.uniqueEmailReceived.put(sentEmail, sentNode);
                                            node.incUniqueEmailReceivedCount();
                                            graph.addEdge(sentNode, node);
                                            djs.union(sentNode.getVertexNum(), node.getVertexNum());
                                            emailMap.put(addresses[i], node);
                                        }
                                    }
                                }
                                foundBCC = true;
                            }

                            if(foundCC && !foundBCC) {
                                String[] addresses = line.trim().split(",");

                                for(int i=0; i < addresses.length; i++) {
                                    addresses[i] = addresses[i].trim();

                                    if(!emailMap.containsKey(addresses[i]) && addresses[i].contains("@enron") && !addresses[i].contains(" ") && !addresses[i].contains("<")) {
                                        String email = addresses[i];
                                        PersonNode node = new PersonNode(email, count);

                                        if(!sentNode.uniqueEmailSent.containsKey(email) && sentNode.getEmail() != null && !email.equals(sentEmail)) {
                                            sentNode.uniqueEmailSent.put(email, node);
                                            sentNode.incUniqueEmailSentCount();
                                            node.uniqueEmailReceived.put(sentEmail, sentNode);
                                            node.incUniqueEmailReceivedCount();
                                            graph.addEdge(sentNode, node);
                                            djs.union(sentNode.getVertexNum(), node.getVertexNum());
                                            count++;
                                            emailMap.put(email, node);
                                        }
                                    } else if(emailMap.containsKey(addresses[i]) && addresses[i].contains("@enron") && !addresses[i].contains(" ")){
                                        PersonNode node = emailMap.get(addresses[i]);

                                        if(!sentNode.uniqueEmailSent.containsKey(addresses[i]) && sentNode.getEmail() != null) {
                                            sentNode.uniqueEmailSent.put(addresses[i], node);
                                            sentNode.incUniqueEmailSentCount();
                                            node.uniqueEmailReceived.put(sentEmail, sentNode);
                                            node.incUniqueEmailReceivedCount();
                                            graph.addEdge(sentNode, node);
                                            djs.union(sentNode.getVertexNum(), node.getVertexNum());
                                            emailMap.put(addresses[i], node);
                                        }
                                    }
                                }
                            }

                            if(line.startsWith("Cc:") && line.contains("@") && finishFrom) {
                                line = line.substring(4);
                                String[] addresses = line.trim().split(",");

                                for(int i=0; i < addresses.length; i++) {
                                    addresses[i] = addresses[i].trim();

                                    if(!emailMap.containsKey(addresses[i]) && addresses[i].contains("@enron") && !addresses[i].contains(" ") && !addresses[i].contains("<")) {
                                        String email = addresses[i];
                                        PersonNode node = new PersonNode(email, count);


                                        if(!sentNode.uniqueEmailSent.containsKey(email) && sentNode.getEmail() != null && !email.equals(sentEmail)) {
                                            sentNode.uniqueEmailSent.put(email, node);
                                            sentNode.incUniqueEmailSentCount();
                                            node.uniqueEmailReceived.put(sentEmail, sentNode);
                                            node.incUniqueEmailReceivedCount();
                                            graph.addEdge(sentNode, node);
                                            djs.union(sentNode.getVertexNum(), node.getVertexNum());
                                            count++;
                                            emailMap.put(email, node);
                                        }
                                    } else if(emailMap.containsKey(addresses[i]) && addresses[i].contains("@enron") && !addresses[i].contains(" ")){
                                        PersonNode node = emailMap.get(addresses[i]);

                                        if(!sentNode.uniqueEmailSent.containsKey(addresses[i]) && sentNode.getEmail() != null) {
                                            sentNode.uniqueEmailSent.put(addresses[i], node);
                                            sentNode.incUniqueEmailSentCount();
                                            node.uniqueEmailReceived.put(sentEmail, sentNode);
                                            node.incUniqueEmailReceivedCount();
                                            graph.addEdge(sentNode, node);
                                            djs.union(sentNode.getVertexNum(), node.getVertexNum());
                                            emailMap.put(addresses[i], node);
                                        }
                                    }
                                }
                                foundCC = true;
                            }

                            if(foundTo && !foundCC && !foundBCC) {
                                String[] addresses = line.trim().split(",");

                                for(int i=0; i < addresses.length; i++) {
                                    addresses[i] = addresses[i].trim();

                                    if(!emailMap.containsKey(addresses[i]) && addresses[i].contains("@enron") && !addresses[i].contains(" ") && !addresses[i].contains("<")) {
                                        String email = addresses[i];
                                        PersonNode node = new PersonNode(email, count);

                                        if(!sentNode.uniqueEmailSent.containsKey(email) && sentNode.getEmail() != null && !email.equals(sentEmail)) {
                                            sentNode.uniqueEmailSent.put(email, node);
                                            sentNode.incUniqueEmailSentCount();
                                            node.uniqueEmailReceived.put(sentEmail, sentNode);
                                            node.incUniqueEmailReceivedCount();
                                            graph.addEdge(sentNode, node);
                                            djs.union(sentNode.getVertexNum(), node.getVertexNum());
                                            count++;
                                            emailMap.put(email, node);
                                        }
                                    } else if(emailMap.containsKey(addresses[i]) && addresses[i].contains("@enron") && !addresses[i].contains(" ")){
                                        PersonNode node = emailMap.get(addresses[i]);

                                        if(!sentNode.uniqueEmailSent.containsKey(addresses[i]) && sentNode.getEmail() != null) {
                                            sentNode.uniqueEmailSent.put(addresses[i], node);
                                            sentNode.incUniqueEmailSentCount();
                                            node.uniqueEmailReceived.put(sentEmail, sentNode);
                                            node.incUniqueEmailReceivedCount();
                                            graph.addEdge(sentNode, node);
                                            djs.union(sentNode.getVertexNum(), node.getVertexNum());
                                            emailMap.put(addresses[i], node);
                                        }
                                    }
                                }
                            }

                            if(finishFrom && !foundTo && line.startsWith("To:")) {
                                line = line.substring(4);
                                String[] addresses = line.trim().split(",");

                                for(int i=0; i < addresses.length; i++) {
                                    addresses[i] = addresses[i].trim();

                                    if(!emailMap.containsKey(addresses[i]) && addresses[i].contains("@enron") && !addresses[i].contains(" ") && !addresses[i].contains("<")) {
                                        String email = addresses[i];
                                        PersonNode node = new PersonNode(email, count);

                                        if(!sentNode.uniqueEmailSent.containsKey(email) && sentNode.getEmail() != null && !email.equals(sentEmail)) {
                                            sentNode.uniqueEmailSent.put(email, node);
                                            sentNode.incUniqueEmailSentCount();
                                            node.uniqueEmailReceived.put(sentEmail, sentNode);
                                            node.incUniqueEmailReceivedCount();
                                            graph.addEdge(sentNode, node);
                                            djs.union(sentNode.getVertexNum(), node.getVertexNum());
                                            count++;
                                            emailMap.put(email, node);
                                        }
                                    } else if(emailMap.containsKey(addresses[i]) && addresses[i].contains("@enron") && !addresses[i].contains(" ")){
                                        PersonNode node = emailMap.get(addresses[i]);

                                        if(!sentNode.uniqueEmailSent.containsKey(addresses[i]) && sentNode.getEmail() != null) {
                                            sentNode.uniqueEmailSent.put(addresses[i], node);
                                            sentNode.incUniqueEmailSentCount();
                                            node.uniqueEmailReceived.put(sentEmail, sentNode);
                                            node.incUniqueEmailReceivedCount();
                                            graph.addEdge(sentNode, node);
                                            djs.union(sentNode.getVertexNum(), node.getVertexNum());
                                            emailMap.put(addresses[i], node);
                                        }
                                    }
                                }
                                foundTo = true;
                            }

                            if(!finishFrom && line.startsWith("From:")) {
                                sentEmail = line.trim().substring(6).trim();

                                if(!emailMap.containsKey(sentEmail) && sentEmail.contains("@enron") && !sentEmail.contains(" ") && !sentEmail.contains("<")) {
                                    //if email is not in HashMap, create new Node and increase count
                                    sentNode = new PersonNode(sentEmail, count);
                                    count++;
                                } else if(emailMap.containsKey(sentEmail) && sentEmail.contains("@enron")){
                                    //if email is in HashMap, get the node
                                    sentNode = emailMap.get(sentEmail);
                                } else {
                                    break;
                                }
                                finishFrom = true;
                            }
                        }

                        if(finishFrom && (foundTo || foundCC || foundBCC)) {
                            emailMap.put(sentEmail, sentNode); //make sure it truly was a valid email file and re-add the sender node to the HashMap
                        }

                        reader.close();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean validMail(File file) {//check if it is a valid mail or not
        try {
            boolean foundFrom = false;
            boolean finish = false;
            FileReader fileRead = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileRead);
            String line;

            while((line = reader.readLine()) != null) {
                if(line.startsWith("X-From:")) {//cutoff
                    finish = true;
                }

                if(finish) {
                    break;
                }

                if(line.startsWith("From:") && line.contains("@enron") && !line.contains("<")) {
                    foundFrom = true;//found valid email in From:
                } else if((line.startsWith("To:") || line.startsWith("Cc:") || line.startsWith("Bcc:"))  && line.contains("@") && foundFrom) {
                    return true;//found emails in To, Cc, or Bcc, but still unsure if valid since might not be enron email, but must check since could be more lines that contain enron email
                }
            }
            return false;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void userInteraction() {//user interaction
        Scanner scan = new Scanner(System.in);

        System.out.println("Email address of the individual (or EXIT to quit):");
        while(scan.hasNextLine()) {
            String input = scan.nextLine().trim();

            if(input.equalsIgnoreCase("EXIT")) {//EXIT to exit program
                System.exit(0);
            } else if(input.contains("@")) {//take in emails only
                //check if email is in HashMap
                if(emailMap.containsKey(input)) {
                    PersonNode node = emailMap.get(input);//get the node associated with the email address
                    //prints out unique counts and team count of the node
                    System.out.println("* " + input + " has sent messages to " + node.getUniqueEmailSentCount() + " others");
                    System.out.println("* " + input + " has received messages from " + node.getUniqueEmailReceivedCount() + " others");
                    System.out.println("* " + input + " is in a team with " + node.getTeamCount(djs) + " individuals");
                } else {
                    System.out.println("Email address (" + input + ") not found in the dataset.");
                }
            } else {
                System.out.println("ERROR: Invalid input.");
            }
            System.out.println("Email address of the individual (or EXIT to quit):");
        }
    }

    public static void main(String[] args) {
        if(args.length > 0) {
            //create size of disjoint set, subject to change depending on size
            djs.createSets(200000);
            readFile(new File(args[0]));//read file at file path
            
            if(args.length > 1) {
                graph.findConnectors(new File(args[1]));//if there is a second input
            } else {
                graph.findConnectors(null);//if no second input
            }
            userInteraction();//user interaction
        } else {
            System.out.println("ERROR: no input");
        }
    }
}
