import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class BaseballElimination {

    private final int numberOfTeams;
    private final LinkedHashMap<String, Team> teams;
    private int maxCurrentWins;
    private final LinkedHashMap<String, ArrayList<String>> inCut;

    private static class Team {

        private final String name;
        private final int wins;
        private final int losses;
        private final int remaining;
        private final int[] againstEach;
        private final int index;

        public Team(String name, int wins, int losses, int remaining, int[] againstEach, int index) {
            this.name = name;
            this.wins = wins;
            this.losses = losses;
            this.remaining = remaining;
            this.againstEach = againstEach;
            this.index = index;
        }

        public String toString() {
            return this.name;
        }
    }

    public BaseballElimination(String filename) {

        if (filename == null) throw new IllegalArgumentException();

        In in = new In(filename);
        this.numberOfTeams = Integer.parseInt(in.readLine());
        this.teams = new LinkedHashMap<>();
        this.inCut = new LinkedHashMap<>();


        int teamIdx = 0;
        while (in.hasNextLine()) {

            String s = in.readLine();
            s = s.trim();
            String[] split = s.split("[ \t]+");
            int[] againstEach = new int[this.numberOfTeams];

            for (int i = 0; i < this.numberOfTeams; i++) {
                againstEach[i] = Integer.parseInt(split[i + 4]);
            }

            Team team = new Team(split[0], Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]), againstEach, teamIdx);

            this.teams.put(team.name, team);
            this.inCut.put(team.name, null);
            if (this.maxCurrentWins < Integer.parseInt(split[1])) this.maxCurrentWins = Integer.parseInt(split[1]);
            teamIdx++;
        }
    }

    public int numberOfTeams() {
        return this.numberOfTeams;
    }

    public Iterable<String> teams() {

        return this.teams.keySet();
    }

    public int wins(String team) {

        assertValid(team);
        return this.teams.get(team).wins;
    }

    public int losses(String team) {

        assertValid(team);
        return this.teams.get(team).losses;
    }

    public int remaining(String team) {

        assertValid(team);
        return this.teams.get(team).remaining;
    }

    public int against(String team1, String team2) {

        assertValid(team1);
        assertValid(team2);

        int teamIdx = this.teams.get(team2).index;
        return this.teams.get(team1).againstEach[teamIdx];
    }

    public boolean isEliminated(String team) {

        assertValid(team);
        return trivialElimination(team) || nonTrivialElimination(team);
    }

    public Iterable<String> certificateOfElimination(String team) {

        assertValid(team);
        if (!isEliminated(team)) return null;

        return this.inCut.get(team);
    }

    private boolean trivialElimination(String team) {

        Team t = this.teams.get(team);
        ArrayList<String> bullies = new ArrayList<>();

        int maxWins = t.remaining + t.wins;

        if (maxWins <= this.maxCurrentWins) {
            for (Team tt : this.teams.values()) {
                if (tt.wins + tt.remaining > maxWins) bullies.add(tt.name);
            }
        }
        this.inCut.put(team, bullies);

        return maxWins <= this.maxCurrentWins;
    }

    private boolean nonTrivialElimination(String team) {

        Team victim = this.teams.get(team);

        LinkedHashMap<Team, ArrayList<Team>> otherTeams = pairs(team);
        int matches = vertexes(otherTeams);
        int teamsNum = otherTeams.keySet().size();

        int V = matches + teamsNum + 2;

        int[] MTT = new int[matches * 2];

        int[] remainingGames = new int[matches];

        int idx = 0;
        int MTTidx = 0;
        for (Map.Entry<Team, ArrayList<Team>> entry : otherTeams.entrySet()) {

            Team t = entry.getKey();
            ArrayList<Team> values = entry.getValue();

            if (!values.isEmpty()) {
                for (Team tt : values) {
                    remainingGames[idx++] = t.againstEach[tt.index];
                    MTT[MTTidx++] = t.index;
                    MTT[MTTidx++] = tt.index;
                }
            }
        }

        FlowNetwork flow = new FlowNetwork(V);
        for (int i = 1; i <= remainingGames.length; i++) {
            flow.addEdge(new FlowEdge(0, i, remainingGames[i - 1]));
        }

        int infIdx = 0;
        for (int i = 1; i <= matches; i++) {
            if (i != 1) infIdx++;
            flow.addEdge(new FlowEdge(i, flowIndex(MTT[infIdx++], V, teamsNum), Double.POSITIVE_INFINITY));
            flow.addEdge(new FlowEdge(i, flowIndex(MTT[infIdx], V, teamsNum), Double.POSITIVE_INFINITY));
        }

        for (int i = V - 1 - teamsNum; i < V - 1; i++) {
            int currentIdx = realIndex(i, matches);
            Team t = this.teams.values().stream().filter(strT -> strT.index == currentIdx).findAny().orElse(null);
            flow.addEdge(new FlowEdge(i, V - 1, victim.wins + victim.remaining - t.wins));
        }

        FordFulkerson ff = new FordFulkerson(flow, 0, V - 1);
        boolean eliminated = false;

        for (FlowEdge f : flow.adj(0)) {
            if (f.flow() != f.capacity()) {
                eliminated = true;
                break;
            }
        }

        ArrayList<String> bullies = new ArrayList<>();
        for (Team t : this.teams.values()) {
            if (!team.equals(t.name)) {
                int flowIdx = flowIndex(t.index, V, teamsNum);
                if (ff.inCut(flowIdx)) {
                    bullies.add(t.name);
                }
            }
        }
        this.inCut.put(victim.name, bullies);

        return eliminated;
    }

    private int flowIndex(int i, int V, int teamsNum) {
        return V - 1 - teamsNum + i;
    }

    private int realIndex(int i, int matches) {
        return i - matches - 1;
    }


    private LinkedHashMap<Team, ArrayList<Team>> pairs(String team) {


        LinkedHashMap<Team, ArrayList<Team>> pairs = new LinkedHashMap<>();
        ArrayList<Integer> codes = new ArrayList<>();

        for (Team t : this.teams.values()) {

            if (!team.equals(t.name)) {

                ArrayList<Team> opponents = new ArrayList<>();

                for (Team opp : this.teams.values()) {

                    int oppCode = opp.name.hashCode() + t.name.hashCode();
                    if (!opp.name.equals(team) && !opp.name.equals(t.name) && !codes.contains(oppCode)) {
                        opponents.add(opp);
                        codes.add(oppCode);
                    }
                }
                pairs.put(t, opponents);
            }
        }
        return pairs;
    }

    private int vertexes(LinkedHashMap<Team, ArrayList<Team>> map) {

        int V = 0;

        for (Map.Entry<Team, ArrayList<Team>> entry : map.entrySet()) {
            ArrayList<Team> values = entry.getValue();
            if (!values.isEmpty()) {
                V += values.size();
            }
        }

        return V;
    }


    private void assertValid(String team) {

        if (team == null) throw new IllegalArgumentException();
        if (!this.teams.containsKey(team)) throw new IllegalArgumentException();
    }

    public static void main(String[] args) {

        BaseballElimination division = new BaseballElimination(args[0]);
        System.out.println(division.maxCurrentWins);

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
