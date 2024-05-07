import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class IndexTree {

	private IndexNode root;

	public IndexTree() {
		this.root = null;
	}

	public void add(String word, int lineNumber) {
		root = addNode(root, word, lineNumber);
	}

	private IndexNode addNode(IndexNode root, String word, int lineNumber) {
		if (root == null) {
			return new IndexNode(word, lineNumber);
		}

		int compareResult = word.compareTo(root.word);
		if (compareResult < 0) {
			root.left = addNode(root.left, word, lineNumber);
		} else if (compareResult > 0) {
			root.right = addNode(root.right, word, lineNumber);
		} else {
			root.occurrences++;
			// Add line number only if it's different from the last one
			if (root.list.isEmpty() || root.list.get(root.list.size() - 1) != lineNumber) {
				root.list.add(lineNumber);
			}
		}

		return root;
	}

	public boolean contains(String word) {
		return containsNode(root, word);
	}

	private boolean containsNode(IndexNode root, String word) {
		if (root == null) {
			return false;
		}

		int compareResult = word.compareTo(root.word);
		if (compareResult < 0) {
			return containsNode(root.left, word);
		} else if (compareResult > 0) {
			return containsNode(root.right, word);
		} else {
			return true;
		}
	}

	public void delete(String word) {
		root = deleteNode(root, word);
	}

	private IndexNode deleteNode(IndexNode root, String word) {
		if (root == null) {
			return null;
		}

		int compareResult = word.compareTo(root.word);
		if (compareResult < 0) {
			root.left = deleteNode(root.left, word);
		} else if (compareResult > 0) {
			root.right = deleteNode(root.right, word);
		} else {
			// Case 1: No child or one child
			if (root.left == null) {
				return root.right;
			} else if (root.right == null) {
				return root.left;
			}

			// Case 2: Two children
			// Find the minimum node in the right subtree (successor)
			root.word = minValue(root.right);
			// Delete the successor
			root.right = deleteNode(root.right, root.word);
		}

		return root;
	}

	private String minValue(IndexNode root) {
		String minVal = root.word;
		while (root.left != null) {
			minVal = root.left.word;
			root = root.left;
		}
		return minVal;
	}

	public void printIndex() {
		printIndex(root);
	}

	private void printIndex(IndexNode root) {
		if (root != null) {
			printIndex(root.left);
			System.out.println("Word: " + root.word);
			System.out.println("Occurrences: " + root.occurrences);
			System.out.println("Lines: " + root.list);
			System.out.println();
			printIndex(root.right);
		}
	}

	public static void main(String[] args) {
		IndexTree index = new IndexTree();

		// Read the Shakespeare text file and add words to the index
		// This is where the file is on my computer, you will need to change the user (anton) and extract the file to downloads
		try (BufferedReader br = new BufferedReader(new FileReader("C:/Users/anton/Downloads/Indexing Text/shakespeare.txt"))) {
			String line;
			int lineNumber = 1;
			while ((line = br.readLine()) != null) {
				// Split the line into words using whitespace as delimiter
				String[] words = line.split("\\s+");
				for (String word : words) {
					// Remove any non-alphabetic characters from the word and convert to lowercase
					word = word.replaceAll("[^a-zA-Z]", "").toLowerCase();
					// Add the word along with its line number to the index
					if (!word.isEmpty()) {
						index.add(word, lineNumber);
					}
				}
				// Increment line number for the next line
				lineNumber++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Print out the index
		index.printIndex();
	}
}
