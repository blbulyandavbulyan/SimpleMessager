package org.blbulyandavbulyan.smgeneral.message;

public class MessagesProcessing {
    static String decryptCesar(String s, int k){
        char [] eWChars = s.toCharArray();
        for (int i = 0; i < eWChars.length; i++) {
            char c = eWChars[i];
            if(c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z'){
                char opZ = (c >= 'A' && c <= 'Z' ? 'Z' : 'z'), opA  = (c >= 'A' && c <= 'Z' ? 'A' : 'a');
                if(c - k >= opA)eWChars[i] = (char)(c - k);
                else{
                    int d = c - opA - k + 1;
                    eWChars[i] = (char)(opZ + d);
                }
            }
        }
        return String.valueOf(eWChars);
    }
    static String encryptCesar(String s, int k){
        char [] eWChars = s.toCharArray();
        for (int i = 0; i < eWChars.length; i++) {
            char c = eWChars[i];
            if(c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z'){
                char opZ = (c >= 'A' && c <= 'Z' ? 'Z' : 'z'), opA  = (c >= 'A' && c <= 'Z' ? 'A' : 'a');
                if(c + k <= opZ)eWChars[i] = (char)(c + k);
                else{
                    int d = c - opZ + k - 1;
                    eWChars[i] = (char)(opA + d);
                }
            }
        }
        return String.valueOf(eWChars);
    }
}
