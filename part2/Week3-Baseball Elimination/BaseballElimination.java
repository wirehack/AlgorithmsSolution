import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class BaseballElimination {
    private int n;
    private List<String> teams;
    private int[] wins;
    private int[] losses;
    private int[] remaining;
    private int[][] against;
    private int maxWins;
    private String maxWinsTeam;
    private int totalFlow;
    private int gameVertices;

    public BaseballElimination(String filename) {
        In in = new In(filename);
        n = in.readInt();
        teams = new ArrayList<>(n);
        wins = new int[n];
        losses = new int[n];
        remaining = new int[n];
        against = new int[n][n];
        maxWins = -1;
        for (int i = 0; i < n; i++) {
            teams.add(in.readString());
            wins[i] = in.readInt();
            if (wins[i] > maxWins) {
                maxWins = wins[i];
                maxWinsTeam = teams.get(i);
            }
            losses[i] = in.readInt();
            remaining[i] = in.readInt();
            for (int j = 0; j < n; j++) {
                against[i][j] = in.readInt();
            }
        }
    }  // create a baseball division from given filename in format specified below

    public int numberOfTeams() {
        return n;
    }  // number of teams

    public Iterable<String> teams() {
        return teams;
    }  // all teams

    public int wins(String team) {
        validate(team);
        return wins[teams.indexOf(team)];
    }  // number of wins for given team

    public int losses(String team) {
        validate(team);
        return losses[teams.indexOf(team)];
    }  // number of losses for given team

    public int remaining(String team) {
        validate(team);
        return remaining[teams.indexOf(team)];
    }  // number of remaining games for given team

    public int against(String team1, String team2) {
        validate(team1);
        validate(team2);
        return against[teams.indexOf(team1)][teams.indexOf(team2)];
    }  // number of remaining games between team1 and team2

    public boolean isEliminated(String team) {
        validate(team);
        if (wins(team) + remaining(team) < maxWins)
            return true;
        else {
            FordFulkerson fordFulkerson = buildFlowNetwork(teams.indexOf(team));
            return fordFulkerson.value() < totalFlow;
        }
    }  // is given team eliminated?

    public Iterable<String> certificateOfElimination(String team) {
        validate(team);
        List<String> eliminationTeam = new ArrayList<>();
        if (wins(team) + remaining(team) < maxWins) {
            eliminationTeam.add(maxWinsTeam);
            return eliminationTeam;
        } else {
            FordFulkerson fordFulkerson = buildFlowNetwork(teams.indexOf(team));
            if (fordFulkerson.value() < totalFlow) {
                for (int i = 0; i < n; i++) {
                    if (i != teams.indexOf(team) && fordFulkerson.inCut(gameVertices + getIndex(i, teams.indexOf(team))))
                        eliminationTeam.add(teams.get(i));
                }
                return eliminationTeam;
            } else
                return null;
        }
    }  // subset R of teams that eliminates given team; null if not eliminated

    private void validate(String team) {
        if (team == null || !teams.contains(team)) {
            throw new IllegalArgumentException();
        }
    }

    private FordFulkerson buildFlowNetwork(int t) {//team i
        gameVertices = 0;
        for (int i = 0; i < n; i++) {
            if (i == t)
                continue;
            for (int j = i + 1; j < n; j++) {
                if (j != t && against[i][j] != 0)
                    gameVertices++;
            }
        }
        FlowNetwork flowNetwork = new FlowNetwork(gameVertices + n + 1);
        int gameCount = 1;
        totalFlow = 0;
        for (int i = 0; i < n; i++) {
            if (i == t)
                continue;
            FlowEdge teamEdge = new FlowEdge(gameVertices + getIndex(i, t), gameVertices + n, wins[t] + remaining[t] - wins[i]);
            flowNetwork.addEdge(teamEdge);
            for (int j = i + 1; j < n; j++) {
                if (j != t && against[i][j] != 0) {
                    FlowEdge gameEdge = new FlowEdge(0, gameCount, against[i][j]);
                    totalFlow += against[i][j];
                    FlowEdge game2team1 = new FlowEdge(gameCount, gameVertices + getIndex(i, t), Double.POSITIVE_INFINITY);
                    FlowEdge game2team2 = new FlowEdge(gameCount, gameVertices + getIndex(j, t), Double.POSITIVE_INFINITY);
                    flowNetwork.addEdge(gameEdge);
                    flowNetwork.addEdge(game2team1);
                    flowNetwork.addEdge(game2team2);
                    gameCount++;
                }
            }
        }
        return new FordFulkerson(flowNetwork, 0, gameVertices + n);
    }

    private int getIndex(int i, int t) {
        if (i < t)
            return i + 1;
        else
            return i;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
