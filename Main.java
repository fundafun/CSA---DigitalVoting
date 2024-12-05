/*
12/4/24
Voting Ballot for Thor vs Loki
 */

import java.io.*;
import java.util.Scanner;


class Candidate {
    private final String name;
    private int votes;

    public Candidate(String name) {
        this.name = name;
        this.votes = 0;
    }

    public String getName() {
        return name;
    }

    public int getVotes() {
        return votes;
    }

    public void vote() {
        votes++;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
}

// BallotQuestion Class
class BallotQuestion {
    private final String question;
    private int yesVotes;
    private int noVotes;

    public BallotQuestion(String question) {
        this.question = question;
        this.yesVotes = 0;
        this.noVotes = 0;
    }

    public String getQuestion() {
        return question;
    }

    public int getYesVotes() {
        return yesVotes;
    }

    public int getNoVotes() {
        return noVotes;
    }

    public void voteYes() {
        yesVotes++;
    }

    public void voteNo() {
        noVotes++;
    }

    public void setYesVotes(int votes) {
        this.yesVotes = votes;
    }

    public void setNoVotes(int votes) {
        this.noVotes = votes;
    }
}

// Main Class
public class Main {
    private static final String DATA_FILE = "votes_data.txt";
    private static boolean hasVotedForCandidate = false;  // Track if the user has voted for a candidate
    private static boolean hasVotedForBallot = false;     // Track if the user has voted for the ballot question

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Language selection
        System.out.println("Choose your language:");
        System.out.println("1. English");
        System.out.println("2. Asgardian");
        System.out.print("Enter your choice: ");
        int languageChoice = scanner.nextInt();
        scanner.nextLine();

        if (languageChoice == 2) {
            System.out.println("Your device does not have the necessary plugin for Asgardian. Defaulting to English.");
        }

        Candidate candidate1 = new Candidate("Thor");
        Candidate candidate2 = new Candidate("Loki");
        BallotQuestion ballotQuestion = new BallotQuestion("Is Thor the better sibling?");
        loadVotes(candidate1, candidate2, ballotQuestion);

        boolean voting = true;

        while (voting) {
            System.out.println("\nWelcome to the voting system!");
            System.out.println("1. Vote for a candidate");
            System.out.println("2. Answer the ballot question");
            System.out.println("3. View results");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    if (hasVotedForCandidate) {
                        System.out.println("You have already voted for a candidate. You can only vote once for each category.");
                        break;
                    }
                    System.out.println("\nCandidates:");
                    System.out.println("1. " + candidate1.getName());
                    System.out.println("2. " + candidate2.getName());
                    System.out.print("Enter your choice: ");
                    int candidateChoice = scanner.nextInt();
                    if (candidateChoice == 1) {
                        candidate1.vote();
                        hasVotedForCandidate = true;
                        saveVotes(candidate1, candidate2, ballotQuestion);
                        System.out.println("Vote recorded for " + candidate1.getName());
                    } else if (candidateChoice == 2) {
                        candidate2.vote();
                        hasVotedForCandidate = true;
                        saveVotes(candidate1, candidate2, ballotQuestion);
                        System.out.println("Vote recorded for " + candidate2.getName());
                    } else {
                        System.out.println("Invalid choice!");
                    }
                    break;

                case 2:
                    if (hasVotedForBallot) {
                        System.out.println("You have already voted on the ballot question. You can only vote once for each category.");
                        break;
                    }
                    System.out.println("\nBallot Question: " + ballotQuestion.getQuestion());
                    System.out.println("1. Yes");
                    System.out.println("2. No");
                    System.out.print("Enter your choice: ");
                    int ballotChoice = scanner.nextInt();
                    if (ballotChoice == 1) {
                        ballotQuestion.voteYes();
                        hasVotedForBallot = true;
                        saveVotes(candidate1, candidate2, ballotQuestion);
                        System.out.println("You voted Yes.");
                    } else if (ballotChoice == 2) {
                        ballotQuestion.voteNo();
                        hasVotedForBallot = true;
                        saveVotes(candidate1, candidate2, ballotQuestion);
                        System.out.println("You voted No.");
                    } else {
                        System.out.println("Invalid choice!");
                    }
                    break;

                case 3:
                    System.out.println("\nResults:");
                    System.out.println(candidate1.getName() + ": " + candidate1.getVotes() + " votes");
                    System.out.println(candidate2.getName() + ": " + candidate2.getVotes() + " votes");
                    System.out.println(ballotQuestion.getQuestion());
                    System.out.println("Yes: " + ballotQuestion.getYesVotes());
                    System.out.println("No: " + ballotQuestion.getNoVotes());
                    break;

                case 4:
                    voting = false;
                    System.out.println("Thank you for participating in the vote!");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }
        }

        scanner.close();
    }

    private static void saveVotes(Candidate c1, Candidate c2, BallotQuestion bq) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE))) {
            writer.write(c1.getName() + "," + c1.getVotes() + "\n");
            writer.write(c2.getName() + "," + c2.getVotes() + "\n");
            writer.write(bq.getQuestion() + "," + bq.getYesVotes() + "," + bq.getNoVotes() + "\n");
        } catch (IOException e) {
            System.out.println("Error saving votes: " + e.getMessage());
        }
    }

    private static void loadVotes(Candidate c1, Candidate c2, BallotQuestion bq) {
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String[] candidate1Data = reader.readLine().split(",");
            c1.setVotes(Integer.parseInt(candidate1Data[1]));

            String[] candidate2Data = reader.readLine().split(",");
            c2.setVotes(Integer.parseInt(candidate2Data[1]));

            String[] ballotData = reader.readLine().split(",");
            bq.setYesVotes(Integer.parseInt(ballotData[1]));
            bq.setNoVotes(Integer.parseInt(ballotData[2]));
        } catch (IOException e) {
            System.out.println("No previous data found. Starting fresh.");
        }
    }
}
