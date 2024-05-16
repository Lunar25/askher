package com.example.dictionary_ver3;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.cloud.sdk.core.service.exception.NotFoundException;
import com.ibm.cloud.sdk.core.service.exception.RequestTooLargeException;
import com.ibm.cloud.sdk.core.service.exception.ServiceResponseException;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.language_translator.v3.model.TranslationResult;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import java.io.*;

public class DictionaryManagement {

    /*
    public void insertFromCommandline() {
        Scanner scan = new Scanner(System.in);
        int numberOfWord = scan.nextInt();
        scan.nextLine();
        for (int i = 0; i <= numberOfWord; i++) {
            String newWord = scan.nextLine();
            String wordMean = scan.nextLine();
            Word word = new Word(newWord, wordMean);
            Dictionary.words.add(word);
        }
        scan.close();
    }
     */

    public static void insertFromFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src\\main\\java\\com\\example\\dictionary_ver3\\engToVieCleanned.txt"));// Infile
            String line = reader.readLine();
            while (line != null) {
                int indexOfTab = line.indexOf("/");
                if (indexOfTab > 1) {
                    Word newWord = new Word(line.substring(1, indexOfTab - 1), line.substring(indexOfTab));
                    Dictionary.addNewWord(newWord);
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            System.out.println("An error occurred." + e);
        }
    }

    public static String dictionaryLookup(String word_) {
        for (int i = 0; i < Dictionary.words.size(); i++) {
            if (word_.toLowerCase().equals(Dictionary.words.get(i).getWordTarget().toLowerCase())) {
                return Dictionary.words.get(i).getWordExplain();
            }
        }
        return "Can't find your word!";
    }

    public static void deleteWord(String word_) {
        for (int i = 0; i < Dictionary.words.size(); i++) {
            if (word_.equals(Dictionary.words.get(i).getWordTarget())) {
                Dictionary.words.remove(Dictionary.words.get(i));
            }
        }
    }

    public static void addNewWord(Word word) {
        Dictionary.words.add(word);
    }

    /*
    public static String editDictionary(String word, String word_) {
        for (int i = 0; i < Dictionary.words.size(); i++) {
            if (Dictionary.words.get(i).getWordTarget().equals(word)
                    && DictionaryManagement.dictionaryLookup(word_) == "Can't find your word!") {
                Dictionary.words.get(i).setWordTarget(word_);
                return "Edited!";
            }
        }
        return "Your word is already exists!";
    }
     */

    public static void dictionaryEdit(String oldWord, Word word_) {
        for (int i = 0; i < Dictionary.words.size(); i++) {
            if (Dictionary.words.get(i).getWordTarget().equals(oldWord)) {
                Dictionary.words.get(i).setWordTarget(word_.getWordTarget());
                Dictionary.words.get(i).setWordExplain(word_.getWordExplain());
                //return "Edited!";
            }
        }
        //return "Thất bại";
    }

    public static void dictionaryExportToFile() {
        try {
            FileOutputStream fileOut = new FileOutputStream("src\\main\\java\\com\\example\\dictionary_ver3\\engToVieCleanned.txt"); //Outfile
            for (int i = 0; i < Dictionary.words.size(); i++) {
                String line = "@" + Dictionary.words.get(i).getWordTarget() + " " + Dictionary.words.get(i).getWordExplain() + "\r\n";
                byte out[] = line.getBytes();
                fileOut.write(out);
            }
            fileOut.close();
        } catch (IOException e) {

            System.out.println("An error occurred." + e);
        }
    }

    public static String makeTrueForm(String str) {
        String res = "";
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '*' || str.charAt(i) == '-') {
                res += "\r\n";
            }
            res += str.charAt(i);
        }
        return res;
    }

    //https://stackoverflow.com/questions/53008424/how-to-fix-error-cannot-be-cast-to-com-sun-speech-freetts-voicedirectory
    public static void wordSpell(String word) {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        Voice voice = VoiceManager.getInstance().getVoice("kevin16");
        if (voice != null) {
            voice.allocate();// Allocating Voice
            try {
                voice.setRate(150);// Setting the rate of the voice
                voice.setPitch(150);// Setting the Pitch of the voice
                voice.setVolume(3);// Setting the volume of the voice
                voice.speak(word);// Calling speak() method
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } else {
            throw new IllegalStateException("Cannot find voice: kevin16");
        }
    }
    public static String getOnline(String translateTxt,String mode) {
        String tempTxt = "";
        try {
        IamAuthenticator authenticator = new IamAuthenticator("chud-mtwmrVpG0QpPzlfOeBQ5envevVwjhxGqG8coBCG");
        LanguageTranslator languageTranslator = new LanguageTranslator("2018-05-01", authenticator);
        languageTranslator.setServiceUrl("https://api.au-syd.language-translator.watson.cloud.ibm.com/instances/0a7f3387-c893-49e7-9b89-36177fc83ce0");

        if (mode.equals("en-vi")) {
            TranslateOptions translateOptions = new TranslateOptions.Builder()
                    .addText(translateTxt)
                    .modelId("en-vi")
                    .build();
            TranslationResult result = languageTranslator.translate(translateOptions)
                    .execute().getResult();
            tempTxt = result.getTranslations().get(0).getTranslation();
            return tempTxt;
        } else if (mode.equals("vi-en")) {
            TranslateOptions translateOptions = new TranslateOptions.Builder()
                    .addText(translateTxt)
                    .modelId("vi-en")
                    .build();
            TranslationResult result = languageTranslator.translate(translateOptions)
                    .execute().getResult();
            tempTxt = result.getTranslations().get(0).getTranslation();
            return tempTxt;
        }
        } catch (NotFoundException e) {
            // Handle Not Found (404) exception
            System.out.println("Service returned status code " + e.getStatusCode() + ": " + e.getMessage());
        } catch (RequestTooLargeException e) {
            // Handle Request Too Large (413) exception
            System.out.println("Service returned status code " + e.getStatusCode() + ": " + e.getMessage());
        } catch (ServiceResponseException e) {
            // Base class for all exceptions caused by error responses from the service
            System.out.println("Service returned status code " + e.getStatusCode() + ": " + e.getMessage());
        } catch (Exception e) {
            // Base class for all exceptions caused by error responses from the service
            System.out.println("Service returned error: " + e.getMessage());
        }
        return tempTxt;
    }
    public static String getTranslate(String s) {
        int index = s.lastIndexOf("translation");
        int index2 = s.indexOf("\"",index);
        int index3 = s.lastIndexOf("\"");
        String res = s.substring(index2+4,index3);
        return res;
    }
}
