public class Anagram {
	
	public boolean isAnagram(String input) {
	
		char[] word1 = removeSymbols(input.toLowerCase().split(" ")[0]).toCharArray();
		char[] word2 = removeSymbols(input.toLowerCase().split(" ")[1]).toCharArray();
		
		if(word1.length == word2.length) {
			
			int counter = 0;
			
			for(int i = 0; i < word1.length; i++) {
				for(int j = 0; j < word1.length; j++) {
					if(word1[i] == word2[j]) {
						counter++;
					}
					if(counter == word1.length) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private String removeSymbols(String str) {
		StringBuilder sb = new StringBuilder(str);
		for(int i = 0; i < sb.length(); i++) {
			if(!Character.isLetter(sb.charAt(i))) {
				sb.deleteCharAt(i);
			}
		}
		return sb.toString();
	}
}
