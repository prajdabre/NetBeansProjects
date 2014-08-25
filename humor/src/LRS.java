
import java.util.Arrays;

public class LRS {

    static String findLongestRepeatedSubstring(String str) {
        String[] suffixes = createSuffixArray(str.toCharArray());
        Arrays.sort(suffixes);

        int maxLength = 0;
        int commonLength = 0;
        int longestCommonIndex = 0;

        for (int i = 0; i < suffixes.length - 1; i++) {
            commonLength = compareCommonLength(suffixes[i], suffixes[i + 1]);
            if (commonLength > maxLength) {
                maxLength = commonLength;
                longestCommonIndex = i;
            }
        }

        return suffixes[longestCommonIndex].substring(0, maxLength);
    }

    // Finds the number of matching chars starting from the first position.
    static int compareCommonLength(String a, String b) {
        char[] arr1 = a.toCharArray();
        char[] arr2 = b.toCharArray();

        int length = 0;
        for (int i = 0; i < arr1.length && i < arr2.length; i++) {
            if (arr1[i] == arr2[i]) {
                length++;
            } else {
                break;
            }
        }
        return length;
    }

    // In C or C++ we can use Pointers instead of a String array.
    static String[] createSuffixArray(char[] arr) {
        String[] output = new String[arr.length];
        for (int i = arr.length - 1, j = 1; i >= 0; i--, j++) {
            output[i] = String.copyValueOf(arr, i, j);
        }
        return output;
    }

    public static void main(String[] args) {
        System.out.println("LCS in banana is: "
            + findLongestRepeatedSubstring("when i sit around the house i eat sit around the house"));
        System.out.println("LCS in mississippi is: "
            + findLongestRepeatedSubstring("mississippi"));
    }

}