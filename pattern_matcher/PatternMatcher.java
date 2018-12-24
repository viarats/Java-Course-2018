public class PatternMatcher {

    public static boolean match(String s, String p) {

        if(p.length() <= s.length()) {

            char str[] = s.toCharArray();
            char subStr[] = p.toCharArray();

            for (int i = 0; i < str.length; i++) {

                int k = i;
                int j = 0;

                while (j < subStr.length && k < str.length && (str[k] == subStr[j] || subStr[j] == '?' || subStr[j] == '*')) {

                    if(subStr[j] == '*') {
                        if(j < subStr.length - 1) {
                            while ( k < str.length && str[k] != subStr[j + 1]) {
                                k++;
                            }
                            j++;
                        }
                    }
                    if(k == str.length) {
                        return  false;
                    }
                    k++;
                    j++;
                }
                if (j == subStr.length) {
                    return true;
                }
            }
        }
        return false;
    }
}
