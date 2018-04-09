public class Trie {
    private static final int R = 26;

    private static class Node {
        private boolean isWord;
        private Node[] next = new Node[R];
    }

    private Node root = new Node();

    public void put(String key) {
        put(root, key, 0);
    }

    private Node put(Node x, String key, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            x.isWord = true;
            return x;
        }
        int i = key.charAt(d) - 'A';
        x.next[i] = put(x.next[i], key, d + 1);
        return x;
    }

    public boolean containsPrefix(String key) {
        return get(root, key, 0) != null;
    }

    public boolean contains(String key) {
        Node x = get(root, key, 0);
        if (x == null)
            return false;
        else return x.isWord;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        int i = key.charAt(d) - 'A';
        return get(x.next[i], key, d + 1);
    }
}
