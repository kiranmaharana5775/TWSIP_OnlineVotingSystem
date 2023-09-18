import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OnlineVotingSystem {

    private JFrame frame;
    private JPanel panel;
    private JButton loginButton;
    private JButton registerButton;
    private JButton createBallotButton;
    private JButton castVoteButton;
    private JButton viewResultsButton;
    private JLabel statusLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;

    private Map<String, String> userCredentials;
    private List<String> registeredCandidates;
    private Map<String, List<String>> ballots;
    private Map<String, Map<String, Integer>> voteResults;

    private String loggedInUser;

    public OnlineVotingSystem() {
        frame = new JFrame("Online Voting System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        userCredentials = new HashMap<>();
        registeredCandidates = new ArrayList<>();
        ballots = new HashMap<>();
        voteResults = new HashMap<>();

        createUI();

        frame.setVisible(true);
    }

    private void createUI() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 20));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(new JLabel("Online Voting System"));

        JPanel centerPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);

        centerPanel.add(new JLabel("Username:"));
        centerPanel.add(usernameField);
        centerPanel.add(new JLabel("Password:"));
        centerPanel.add(passwordField);
        centerPanel.add(new JLabel(""));
        centerPanel.add(new JLabel(""));

        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
        createBallotButton = new JButton("Create Ballot");
        castVoteButton = new JButton("Cast Vote");
        viewResultsButton = new JButton("View Results");

        centerPanel.add(loginButton);
        centerPanel.add(registerButton);
        centerPanel.add(createBallotButton);
        centerPanel.add(castVoteButton);
        centerPanel.add(viewResultsButton);
        centerPanel.add(new JLabel(""));

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusLabel = new JLabel("");
        statusPanel.add(statusLabel);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(statusPanel, BorderLayout.SOUTH);

        frame.add(panel);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleRegistration();
            }
        });

        createBallotButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleBallotCreation();
            }
        });

        castVoteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleVoteCasting();
            }
        });

        viewResultsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleViewResults();
            }
        });

        createBallotButton.setEnabled(false);
        castVoteButton.setEnabled(false);
        viewResultsButton.setEnabled(false);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (validateLogin(username, password)) {
            loggedInUser = username;
            statusLabel.setText("Logged in as: " + loggedInUser);
            updateUIForLoggedInUser();
        } else {
            statusLabel.setText("Invalid credentials. Please try again.");
        }
    }

    private boolean validateLogin(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter both username and password.");
            return false;
        }

        if (userCredentials.containsKey(username)) {
            if (userCredentials.get(username).equals(password)) {
                return true;
            } else {
                statusLabel.setText("Invalid password. Please try again.");
                return false;
            }
        } else {
            statusLabel.setText("Username does not exist. Please register first.");
            return false;
        }
    }

    private void handleRegistration() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (registerUser(username, password)) {
            statusLabel.setText("Registration successful. You can now log in.");
        }
    }

    private boolean registerUser(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter both username and password.");
            return false;
        }

        if (userCredentials.containsKey(username)) {
            statusLabel.setText("Username already exists. Please choose a different one.");
            return false;
        }

        userCredentials.put(username, password);
        statusLabel.setText("Registration successful for user: " + username);
        return true;
    }

    private void updateUIForLoggedInUser() {
        loginButton.setEnabled(false);
        registerButton.setEnabled(false);
        createBallotButton.setEnabled(true);
        castVoteButton.setEnabled(true);
        viewResultsButton.setEnabled(true);
    }

    private void handleBallotCreation() {
        if (loggedInUser != null) {
            String electionName = JOptionPane.showInputDialog(frame, "Enter election name:");

            if (!ballots.containsKey(electionName)) {
                List<String> candidates = new ArrayList<>();
                while (true) {
                    String candidate = JOptionPane.showInputDialog(frame, "Enter candidate name (or leave blank to finish):");
                    if (candidate == null || candidate.trim().isEmpty()) {
                        break;
                    }
                    candidates.add(candidate);
                }
                if (!candidates.isEmpty()) {
                    ballots.put(electionName, candidates);
                    statusLabel.setText("Ballot created for " + electionName);
                    registeredCandidates.addAll(candidates);
                } else {
                    statusLabel.setText("No candidates added to the ballot.");
                }
            } else {
                statusLabel.setText("Election name already exists.");
            }
        } else {
            statusLabel.setText("Please log in first.");
        }
    }

    private void handleVoteCasting() {
        if (loggedInUser != null) {
            String selectedElection = (String) JOptionPane.showInputDialog(
                    frame,
                    "Select an election:",
                    "Vote Casting",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    ballots.keySet().toArray(),
                    null
            );

            if (selectedElection != null) {
                List<String> candidates = ballots.get(selectedElection);
                if (candidates != null && !candidates.isEmpty()) {
                    String selectedCandidate = (String) JOptionPane.showInputDialog(
                            frame,
                            "Select a candidate:",
                            "Vote Casting",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            candidates.toArray(),
                            null
                    );

                    if (selectedCandidate != null) {
                        statusLabel.setText("Vote cast for " + selectedCandidate);
                        updateVoteResults(selectedElection, selectedCandidate);
                    }
                } else {
                    statusLabel.setText("No candidates in the selected election.");
                }
            }
        } else {
            statusLabel.setText("Please log in first.");
        }
    }

    private void updateVoteResults(String electionName, String candidateName) {
        if (voteResults.containsKey(electionName)) {
            Map<String, Integer> results = voteResults.get(electionName);
            if (results.containsKey(candidateName)) {
                results.put(candidateName, results.get(candidateName) + 1);
            } else {
                results.put(candidateName, 1);
            }
        } else {
            Map<String, Integer> results = new HashMap<>();
            results.put(candidateName, 1);
            voteResults.put(electionName, results);
        }
    }

    private void handleViewResults() {
        if (loggedInUser != null) {
            String selectedElection = (String) JOptionPane.showInputDialog(
                    frame,
                    "Select an election to view results:",
                    "View Results",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    ballots.keySet().toArray(),
                    null
            );

            if (selectedElection != null && voteResults.containsKey(selectedElection)) {
                Map<String, Integer> results = voteResults.get(selectedElection);
                List<String> winners = new ArrayList<>();
                int maxVotes = -1;

                for (Map.Entry<String, Integer> entry : results.entrySet()) {
                    if (entry.getValue() > maxVotes) {
                        maxVotes = entry.getValue();
                        winners.clear();
                        winners.add(entry.getKey());
                    } else if (entry.getValue() == maxVotes) {
                        winners.add(entry.getKey());
                    }
                }

                if (!winners.isEmpty()) {
                    if (winners.size() == 1) {
                        // Only one winner, display as usual
                        StringBuilder resultMessage = new StringBuilder("Election Results for " + selectedElection + ":\n");
                        resultMessage.append("Winner is: ").append(String.join(", ", winners)).append("\n");
                        resultMessage.append("Total Votes: ").append(maxVotes).append("\n");
                        JOptionPane.showMessageDialog(frame, resultMessage.toString(), "Election Results", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        // Multiple winners, it's a tie
                        JOptionPane.showMessageDialog(frame, "Election for " + selectedElection + " is tied.", "Election Results", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    statusLabel.setText("No winner in the selected election.");
                }
            } else {
                statusLabel.setText("Invalid election or no results available.");
            }
        } else {
            statusLabel.setText("Please log in first.");
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new OnlineVotingSystem();
            }
        });
    }
}
